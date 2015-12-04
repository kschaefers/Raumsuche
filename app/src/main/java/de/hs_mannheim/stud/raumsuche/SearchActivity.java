package de.hs_mannheim.stud.raumsuche;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SearchActivity extends AppCompatActivity {

    private Button buttonAddGroupToRoomSearch;
    private TextView textSearchDate;
    private TextView textSearchTime;
    private TextView textSearchBuilding;
    private TextView textSearchRoomSize;
    private boolean[] mSelectedBuilding;
    private boolean[] mSelectedRoomSize;
    private boolean[] mSelectedTimes;

    private GregorianCalendar selectedDate = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        buttonAddGroupToRoomSearch = (Button) findViewById(R.id.buttonAddGroupToRoomSearch);
        textSearchDate = (TextView) findViewById(R.id.textSearchDate);
        textSearchTime = (TextView) findViewById(R.id.textSearchTime);
        textSearchBuilding = (TextView) findViewById(R.id.textSearchBuilding);
        textSearchRoomSize = (TextView) findViewById(R.id.textSearchRoomSize);

        mSelectedBuilding = new boolean[getResources().getStringArray(R.array.buildings).length];
        mSelectedRoomSize = new boolean[getResources().getStringArray(R.array.roomSizes).length];
        mSelectedTimes = new boolean[getResources().getStringArray(R.array.times).length];

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
        textSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDate();
            }
        });

        textSearchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog("Zeitstunde", getResources().getStringArray(R.array.times), mSelectedTimes);
            }
        });

        textSearchBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog("Gebäude", getResources().getStringArray(R.array.buildings), mSelectedBuilding);
            }
        });

        textSearchRoomSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog("Raumgröße", getResources().getStringArray(R.array.roomSizes), mSelectedRoomSize);
            }
        });
    }

    private void showDialogDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final CalendarView calendar = (CalendarView) inflater.inflate(R.layout.calendar_dialog, null);
        calendar.setDate(selectedDate.getTimeInMillis());
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = new GregorianCalendar(year, month, dayOfMonth);
                selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                selectedDate.set(Calendar.MINUTE, 0);
                selectedDate.set(Calendar.SECOND, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GregorianCalendar today = new GregorianCalendar();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);
                if (selectedDate.compareTo(today) == 0) {
                    textSearchDate.setText("Heute");
                } else {
                    textSearchDate.setText(selectedDate.get(Calendar.DAY_OF_MONTH) + "." + (selectedDate.get(Calendar.MONTH) + 1) + "." + selectedDate.get(Calendar.YEAR));
                }
                dialog.dismiss();
            }
        });
        builder.setView(calendar);
        builder.show();
    }

    private void showSelectDialog(final String title, final String[] choices, boolean[] mSelectedItems) {
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
                switch (title) {
                    case "Gebäude":
                        acceptSelectedItemsBuilding(newSelectedItems);
                        break;
                    case "Raumgröße":
                        acceptSelectedItemsRoomSize(newSelectedItems);
                        break;
                    case "Zeitstunde":
                        acceptSelectedItemsTimes(newSelectedItems);
                        break;
                    default:
                        break;
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

    private void acceptSelectedItemsTimes(boolean[] newList) {
        mSelectedTimes = newList;

        String selectedTimes = "";
        for (int i = 0; i < mSelectedTimes.length; i++) {
            if (mSelectedTimes[i]) {
                if (!selectedTimes.equals("")) {
                    selectedTimes += ", ";
                }
                selectedTimes += getResources().getStringArray(R.array.times)[i].substring(0, 2);
            }
        }
        if (selectedTimes.equals("")) {
            selectedTimes = getString(R.string.anyTime);
        } else {
            selectedTimes += " Block";
        }
        textSearchTime.setText(selectedTimes);
    }
}
