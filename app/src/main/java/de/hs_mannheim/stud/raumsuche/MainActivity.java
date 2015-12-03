package de.hs_mannheim.stud.raumsuche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {

    private Button nextAvailabeRoom;
    private Button otherRoom;
    private Button organizeGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextAvailabeRoom = (Button) findViewById(R.id.buttonNextAvailableRoom);
        otherRoom = (Button) findViewById(R.id.buttonSearchForRoom);
        organizeGroups = (Button) findViewById(R.id.buttonOrganizeGroups);

        initializeButtons();
    }

    private void initializeButtons() {
        nextAvailabeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.setClass(getApplicationContext(), ResultActivity.class);
                startActivity(result);
            }
        });

        otherRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent();
                search.setClass(getApplicationContext(), SearchActivity.class);
                startActivity(search);
            }
        });

        organizeGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent();
                login.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });
    }
}
