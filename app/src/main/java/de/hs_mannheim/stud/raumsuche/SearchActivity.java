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
    private ArrayList mSelectedBuilding = new ArrayList();
    private ArrayList<Integer> mSelectedRoomSize = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        buttonAddGroupToRoomSearch = (Button) findViewById(R.id.buttonAddGroupToRoomSearch);
        textSearchDateAndTime = (TextView) findViewById(R.id.textSearchDateAndTime);
        textSearchBuilding = (TextView) findViewById(R.id.textSearchBuilding);
        textSearchRoomSize = (TextView) findViewById(R.id.textSearchRoomSize);

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
                showDialogRoomSize("Gebäude", new String[]{"A Gebäude", "B Gebäude"}, mSelectedBuilding);
            }
        });

        textSearchRoomSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRoomSize("Raumgröße", new String[]{"10", "20"}, mSelectedRoomSize);
            }
        });
    }

    private void showDialogBuilding(String title, final String[] choices, ArrayList<Integer> mSelectedItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList newSelectedItems = (ArrayList) mSelectedItems.clone();

        builder.setTitle(title).setMultiChoiceItems(choices, toBooleanArray(choices, mSelectedItems), new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    newSelectedItems.add(which);
                } else if (newSelectedItems.contains(which)) {
                    newSelectedItems.remove(Integer.valueOf(which));
                }
            }
        });

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                acceptSelectedItemsBuilding(newSelectedItems);
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

    private void showDialogRoomSize(String title, final String[] choices, ArrayList<Integer> mSelectedItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList newSelectedItems = (ArrayList) mSelectedItems.clone();

        builder.setTitle(title).setMultiChoiceItems(choices, toBooleanArray(choices, mSelectedItems), new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    newSelectedItems.add(which);
                } else if (newSelectedItems.contains(which)) {
                    newSelectedItems.remove(Integer.valueOf(which));
                }
            }
        });

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                acceptSelectedItemsRoomSize(newSelectedItems);
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

    private void acceptSelectedItemsRoomSize(ArrayList newList) {
        mSelectedRoomSize = newList;
    }

    private void acceptSelectedItemsBuilding(ArrayList newList) {
        mSelectedBuilding = newList;
    }

    private boolean[] toBooleanArray(String[] choices, ArrayList<Integer> list) {
        boolean[] result = new boolean[choices.length];
        for (int i = 0; i < choices.length; i++) {
            if (list.contains(i)) {
                result[i] = true;
            }
        }
        return result;
    }
}
