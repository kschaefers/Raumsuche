package de.hs_mannheim.stud.raumsuche;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import de.hs_mannheim.stud.raumsuche.views.adapters.GroupListAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GroupActivity extends AppCompatActivity {

    @Bind(R.id.group_list)
    ListView list;

    @Bind(R.id.group_list_empty)
    TextView emptyText;

    @Bind(R.id.group_loading_progress)
    ProgressBar loadingProgress;

    private GroupListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        initComponents();
        loadGroups();
    }

    @OnClick(R.id.group_add_button)
    public void addGroup() {
        Intent addGroup = new Intent(this, GroupAddActivity.class);
        startActivityForResult(addGroup, 0);
    }

    private void initComponents() {
        // Initialize group list
        adapter = new GroupListAdapter(this, new ArrayList<Group>());
        list.setAdapter(adapter);
    }

    private void loadGroups() {
        UserManager manager = UserManager.getInstance(this);
        User user = manager.getUser();

        ApiServiceFactory serviceFactory = ApiServiceFactory.getInstance();
        GroupService groupService = serviceFactory.getGroupService(user.getMtklNr(), user.getPassword());

        Call<List<Group>> call = groupService.listUserGroups(user.getMtklNr());
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Response<List<Group>> response, Retrofit retrofit) {
                loadingProgress.setVisibility(View.GONE);
                List<Group> groups = response.body();

                if (groups != null) {

                    if(groups.size() > 0) {
                        emptyText.setVisibility(View.GONE);
                        adapter.setGroups(groups);
                    } else {
                        emptyText.setVisibility(View.VISIBLE);
                    }
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                showError();
            }
        });
    }

    private void showError() {
        Toast.makeText(GroupActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
    }
}
