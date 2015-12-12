package de.hs_mannheim.stud.raumsuche;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

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

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Bind(R.id.signup_studentid_input)
    @NotEmpty
    EditText studentIdInput;

    @Bind(R.id.signup_name_input)
    @NotEmpty
    EditText nameInput;

    @Bind(R.id.signup_faculty_spinner)
    Spinner facultySpinner;

    @Bind(R.id.signup_form_layout)
    View formLayout;

    @Bind(R.id.signup_submit_progress)
    ProgressBar submitProgress;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initComponents();
    }

    @OnClick(R.id.signup_submit_button)
    public void signup() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        hideForm();

        ApiServiceFactory services = ApiServiceFactory.getInstance(SignUpActivity.this);
        UserService userService = services.getUserService();

        String studentId = studentIdInput.getText().toString();
        String name = nameInput.getText().toString();
        String faculty = facultySpinner.getSelectedItem().toString();

        User newUser = new User();
        newUser.setMtklNr(studentId);
        newUser.setName(name);
        newUser.setFaculty(faculty);

        Call<User> call = userService.createUser(newUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                User user = response.body();

                if (user != null) {
                    UserManager manager = UserManager.getInstance(SignUpActivity.this);
                    manager.setUser(user);

                    showConfirmation();

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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(this, R.string.signup_validation_error, Toast.LENGTH_LONG).show();
    }

    private void initComponents() {
        // Init EditText Validation
        validator = new Validator(this);
        validator.setValidationListener(this);

        // Init Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.facultys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpinner.setAdapter(adapter);
    }

    private void showConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create confirmation message
        builder.setMessage(getString(R.string.signUpConfirmationMessage))
                .setTitle(getString(R.string.signUpConfirmationMessageTitle));

        // Add OK button
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SignUpActivity.this.finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
