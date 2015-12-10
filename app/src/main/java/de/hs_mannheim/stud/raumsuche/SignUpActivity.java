package de.hs_mannheim.stud.raumsuche;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.signup_studentid_input)
    EditText studentIdInput;

    @Bind(R.id.signup_name_input)
    EditText nameInput;

    @Bind(R.id.signup_faculty_spinner)
    Spinner facultySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signup_submit_button)
    public void signup() {

        // mail senden an matrikelnr@stud.hs-mannheim.de
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

                    showConfirmationDialog();

                } else {
                    // Show error
                }

            }

            @Override
            public void onFailure(Throwable t) {
                // Show error
            }
        });
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // create confirmation message
        builder.setMessage(getString(R.string.signUpConfirmationMessage))
                .setTitle(getString(R.string.signUpConfirmationMessageTitle));

        // add OK button
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SignUpActivity.this.finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
