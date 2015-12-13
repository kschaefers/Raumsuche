package de.hs_mannheim.stud.raumsuche;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.okhttp.ResponseBody;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.network.ApiServiceFactory;
import de.hs_mannheim.stud.raumsuche.network.services.UserService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.HTTP;

public class ProfileActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Bind(R.id.profile_name_input)
    @NotEmpty
    EditText nameInput;

    @Bind(R.id.profile_faculty_spinner)
    Spinner facultySpinner;

    @Bind(R.id.profile_form_layout)
    View formLayout;

    @Bind(R.id.profile_submit_progress)
    ProgressBar submitProgress;

    Validator validator;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initComponents();
    }

    @OnClick(R.id.profile_submit_button)
    public void changeProfile() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        hideForm();

        ApiServiceFactory services = ApiServiceFactory.getInstance(ProfileActivity.this);
        UserService userService = services.getUserService(user.getMtklNr(), user.getPassword());

        String name = nameInput.getText().toString();
        String faculty = facultySpinner.getSelectedItem().toString();

        final User updatedUser = new User();
        updatedUser.setMtklNr(user.getMtklNr());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setName(name);
        updatedUser.setFaculty(faculty);


        Call<ResponseBody> call = userService.updateUser(updatedUser.getMtklNr(), updatedUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    UserManager manager = UserManager.getInstance(ProfileActivity.this);
                    manager.setUser(updatedUser);

                    Toast.makeText(ProfileActivity.this, R.string.profile_changed, Toast.LENGTH_SHORT).show();
                } else {
                    showError();
                }

                showForm();
            }

            @Override
            public void onFailure(Throwable t) {
                showError();
                showForm();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(this, R.string.profile_validation_error, Toast.LENGTH_LONG).show();
    }

    private void initComponents() {
        // Get logged-in user
        UserManager manager = UserManager.getInstance(this);
        user = manager.getUser();

        // Init EditText Validation
        validator = new Validator(this);
        validator.setValidationListener(this);

        // Init Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.facultys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpinner.setAdapter(adapter);

        // Fill up form fields
        nameInput.setText(user.getName());
        int spinnerSelection = adapter.getPosition(user.getFaculty());
        facultySpinner.setSelection(spinnerSelection);

    }

    private void showError() {
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
    }

    private void showForm() {
        submitProgress.setVisibility(View.GONE);
        formLayout.setVisibility(View.VISIBLE);
    }

    private void hideForm() {
        submitProgress.setVisibility(View.VISIBLE);
        formLayout.setVisibility(View.GONE);
    }
}
