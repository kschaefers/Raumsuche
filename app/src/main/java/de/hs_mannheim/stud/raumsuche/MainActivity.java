package de.hs_mannheim.stud.raumsuche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.net.Authenticator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_LOGIN = 100;

    @Bind(R.id.main_registered_view)
    View registeredUserView;

    @Bind(R.id.main_unregistered_view)
    View unregisteredUserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initComponents();
    }

    @OnClick(R.id.main_searchroom_button)
    public void openSearch() {
        Intent search = new Intent();
        search.setClass(getApplicationContext(), SearchActivity.class);
        startActivity(search);
    }

    @OnClick(R.id.main_showroom_button)
    public void openResult() {
        Intent result = new Intent();
        result.setClass(getApplicationContext(), ResultActivity.class);
        startActivity(result);
    }

    @OnClick(R.id.main_group_button)
    public void openGroup() {
        Intent group = new Intent();
        group.setClass(getApplicationContext(), GroupActivity.class);
        startActivity(group);
    }

    @OnClick(R.id.main_logout_button)
    public void logout() {
        UserManager manager = UserManager.getInstance(this);
        manager.removeUser();
        enableScreenForUnregisteredUser();
    }

    @OnClick(R.id.main_login_loginbutton)
    public void openLogin() {
        Intent login = new Intent();
        login.setClass(getApplicationContext(), LoginActivity.class);
        startActivityForResult(login, REQUEST_LOGIN);
    }

    @OnClick(R.id.main_login_signupbutton)
    public void openSignUp() {
        Intent signup = new Intent();
        signup.setClass(getApplicationContext(), SignUpActivity.class);
        startActivity(signup);
    }

    @OnClick(R.id.main_profile_button)
    public void openProfile() {
        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            UserManager manager = UserManager.getInstance(this);

            if (manager.isUserLoggedIn()) {
                enableScreenForRegisteredUser();
            } else {
                enableScreenForUnregisteredUser();
            }
        }
    }

    private void initComponents() {
        UserManager userManager = UserManager.getInstance(this);

        if (userManager.isUserLoggedIn()) {
            enableScreenForRegisteredUser();
        }
    }

    private void enableScreenForRegisteredUser() {
        unregisteredUserView.setVisibility(View.GONE);
        registeredUserView.setVisibility(View.VISIBLE);
    }

    private void enableScreenForUnregisteredUser() {
        unregisteredUserView.setVisibility(View.VISIBLE);
        registeredUserView.setVisibility(View.GONE);
    }

}
