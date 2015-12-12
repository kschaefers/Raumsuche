package de.hs_mannheim.stud.raumsuche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @Bind(R.id.login_studentid_input)
    EditText studentIdInput;

    @NotEmpty
    @Bind(R.id.login_password_input)
    EditText passwordInput;

    @Bind(R.id.login_form_layout)
    View formLayout;

    @Bind(R.id.login_submit_progress)
    ProgressBar submitProgress;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initComponents();
    }

    @OnClick(R.id.login_submit_button)
    public void login() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        hideForm();

        ApiServiceFactory services = ApiServiceFactory.getInstance(LoginActivity.this);
        UserService userService = services.getUserService();

        String studentId = studentIdInput.getText().toString();
        String password = passwordInput.getText().toString();

        Call<User> call = userService.getUser(studentId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                User user = response.body();

                if (user != null) {
                    UserManager manager = UserManager.getInstance(LoginActivity.this);
                    manager.setUser(user);
                    LoginActivity.this.finish();

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
        Toast.makeText(this, R.string.login_validation_error, Toast.LENGTH_LONG).show();
    }

    private void initComponents() {
        // Init EditText Validation
        validator = new Validator(this);
        validator.setValidationListener(this);
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
