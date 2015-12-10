package de.hs_mannheim.stud.raumsuche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.acl.Group;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
        Intent login = new Intent();
        login.setClass(getApplicationContext(), LoginActivity.class);
        startActivity(login);
    }
}
