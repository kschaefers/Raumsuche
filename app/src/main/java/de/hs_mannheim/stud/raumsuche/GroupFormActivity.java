package de.hs_mannheim.stud.raumsuche;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.okhttp.ResponseBody;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.Group;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.network.ApiServiceFactory;
import de.hs_mannheim.stud.raumsuche.network.services.GroupService;
import de.hs_mannheim.stud.raumsuche.views.adapters.GroupAddListAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GroupFormActivity extends AppCompatActivity implements Validator.ValidationListener {

    public static final String BK_EDIT_GROUP = "edit_group";

    @NotEmpty
    @Bind(R.id.group_form_name_input)
    EditText nameInput;

    @Bind(R.id.group_form_user_input)
    EditText userInput;

    @Bind(R.id.group_form_user_list)
    ListView userList;

    @Bind(R.id.group_form_layout)
    View formLayout;

    @Bind(R.id.group_form_progress)
    ProgressBar progressView;

    private GroupAddListAdapter adapter;
    private Group group;
    private User user;
    private Validator validator;
    private UserManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_form);
        ButterKnife.bind(this);

        UserManager manager = UserManager.getInstance(this);
        user = manager.getUser();

        initGroup();
        initActionBar();
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.group_add_menu_save:
                validator.validate();
                break;

            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initGroup() {
        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(BK_EDIT_GROUP)) {
            group = Parcels.unwrap(extras.getParcelable(BK_EDIT_GROUP));

        } else {
            group = new Group();
            group.setName("");
            group.setOwner(user.getMtklNr());

            List<User> users = new ArrayList<User>();
            users.add(user);
            group.setUsers(users);
        }
    }

    private void initActionBar() {
        if (group.getId() > 0) {
            String editFormString = getResources().getString(R.string.group_form_edit_title);
            String editTitle = String.format(editFormString, group.getName());

            setTitle(editTitle);
        } else {
            setTitle(R.string.group_form_add_title);
        }
    }

    private void initComponents() {
        // Init EditText Validation
        validator = new Validator(this);
        validator.setValidationListener(this);

        nameInput.setText(group.getName());

        adapter = new GroupAddListAdapter(this, group.getUsers());
        userList.setAdapter(adapter);
    }

    @OnClick(R.id.group_form_user_addbutton)
    public void addUser() {
        String studentId = userInput.getText().toString();

        if (!studentId.isEmpty()) {
            User addedUser = new User();
            addedUser.setMtklNr(studentId);

            List<User> users = adapter.getUsers();
            if (users.contains(addedUser)) {
                Toast.makeText(this, R.string.group_add_user_already_added, Toast.LENGTH_SHORT).show();
            } else {
                users.add(addedUser);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.group_form_exit)
    public void exitGroup() {

    }

    @Override
    public void onValidationSucceeded() {
        group.setName(nameInput.getText().toString());
        group.setUsers(adapter.getUsers());

        ApiServiceFactory apiService = ApiServiceFactory.getInstance();
        GroupService groupService = apiService.getGroupService(user.getMtklNr(), manager.getUserPassword());

        hideForm();

        if (group.getId() > 0) {
            editGroup(groupService);
        } else {
            addGroup(groupService);
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(this, R.string.group_add_validation_error, Toast.LENGTH_LONG).show();
    }

    private void editGroup(GroupService groupService) {
        Call<ResponseBody> call = groupService.updateGroup(String.valueOf(group.getId()), group);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    finish();
                } else {
                    showError();
                    showForm();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showError();
                showForm();
            }
        });
    }

    private void addGroup(GroupService groupService) {
        Call<Group> call = groupService.createGroup(group);
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Response<Group> response, Retrofit retrofit) {
                Group group = response.body();

                if (group != null) {
                    finish();
                } else {
                    showError();
                    showForm();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showError();
                showForm();
            }
        });
    }

    private void showForm() {
        formLayout.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

    private void hideForm() {
        formLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        Toast.makeText(GroupFormActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
    }
}
