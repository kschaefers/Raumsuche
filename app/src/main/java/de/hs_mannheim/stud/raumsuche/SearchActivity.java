package de.hs_mannheim.stud.raumsuche;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Button buttonAddGroupToRoomSearch;
    private TextView textSearchDateAndTime;
    private TextView textSearchBuilding;
    private TextView textSearchRoomSize;
    private boolean[] mSelectedBuilding;
    private boolean[] mSelectedRoomSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        buttonAddGroupToRoomSearch = (Button) findViewById(R.id.buttonAddGroupToRoomSearch);
        textSearchDateAndTime = (TextView) findViewById(R.id.textSearchDateAndTime);
        textSearchBuilding = (TextView) findViewById(R.id.textSearchBuilding);
        textSearchRoomSize = (TextView) findViewById(R.id.textSearchRoomSize);

        mSelectedBuilding = new boolean[getResources().getStringArray(R.array.buildings).length];
        mSelectedRoomSize = new boolean[getResources().getStringArray(R.array.roomSizes).length];

        initializeButton();
        initializeTextViews();
    }

    private void initializeButton() {
        buttonAddGroupToRoomSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groups = new Intent();
                groups.setClass(getApplicationContext(), GroupActivity.class);
                startActivity(groups);
            }
        });
    }

    private void initializeTextViews() {
        textSearchDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textSearchBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog("Gebäude", getResources().getStringArray(R.array.buildings), mSelectedBuilding, true);
            }
        });

        textSearchRoomSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog("Raumgröße", getResources().getStringArray(R.array.roomSizes), mSelectedRoomSize, false);
            }
        });
    }

    private void showSelectDialog(String title, final String[] choices, boolean[] mSelectedItems, final boolean isBuilding) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final boolean[] newSelectedItems = mSelectedItems.clone();

        builder.setTitle(title).setMultiChoiceItems(choices, mSelectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                newSelectedItems[which] = isChecked;
            }
        });

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (isBuilding) {
                    acceptSelectedItemsBuilding(newSelectedItems);
                } else {
                    acceptSelectedItemsRoomSize(newSelectedItems);
                }
                dialog.dismiss();
            }
        }).setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void acceptSelectedItemsRoomSize(boolean[] newList) {
        mSelectedRoomSize = newList;

        String selectedRoomSizes = "";
        for (int i = 0; i < mSelectedRoomSize.length; i++) {
            if (mSelectedRoomSize[i]) {
                if (!selectedRoomSizes.equals("")) {
                    selectedRoomSizes += ", ";
                }
                selectedRoomSizes += getResources().getStringArray(R.array.roomSizes)[i];
            }
        }
        if (selectedRoomSizes.equals("")) {
            selectedRoomSizes = getString(R.string.anyRoomSize);
        }
        textSearchRoomSize.setText(selectedRoomSizes);
    }

    private void acceptSelectedItemsBuilding(boolean[] newList) {
        mSelectedBuilding = newList;

        String selectedBuildings = "";
        for (int i = 0; i < mSelectedBuilding.length; i++) {
            if (mSelectedBuilding[i]) {
                if (!selectedBuildings.equals("")) {
                    selectedBuildings += ", ";
                }
                selectedBuildings += getResources().getStringArray(R.array.buildings)[i];
            }
        }
        if (selectedBuildings.equals("")) {
            selectedBuildings = getString(R.string.anyBuilding);
        }
        textSearchBuilding.setText(selectedBuildings);
    }
}
