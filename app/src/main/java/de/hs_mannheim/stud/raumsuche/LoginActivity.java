package de.hs_mannheim.stud.raumsuche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.network.ApiServiceFactory;
import de.hs_mannheim.stud.raumsuche.network.services.UserService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    public static final int RESULT_LOGGEDIN = 200;

    @Bind(R.id.login_studentid_input)
    EditText studentIdInput;

    @Bind(R.id.login_password_input)
    EditText passwordInput;

    private Button buttonLogin;
    private TextView linkSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        linkSignUp = (TextView) findViewById(R.id.linkSignUp);

        initializeButtons();
    }

    private void initializeButtons() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiServiceFactory services = ApiServiceFactory.getInstance(LoginActivity.this);
                UserService userService = services.getUserService();

                String studentId = studentIdInput.getText().toString();

                Call<User> call = userService.getUser(studentId);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Response<User> response, Retrofit retrofit) {
                        User user = response.body();

                        if(user != null){
                            UserManager manager = UserManager.getInstance(LoginActivity.this);
                            manager.setUser(user);
                            LoginActivity.this.finish();

                        } else {
                            // Show error
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent();
                signUp.setClass(getApplicationContext(), SignUpActivity.class);
                startActivity(signUp);
            }
        });
    }
}
