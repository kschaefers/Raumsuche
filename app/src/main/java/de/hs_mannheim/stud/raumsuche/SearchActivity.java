package de.hs_mannheim.stud.raumsuche;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.network.ApiServiceFactory;
import de.hs_mannheim.stud.raumsuche.network.services.RoomService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.buttonAddGroupToRoomSearch)
    protected Button buttonAddGroupToRoomSearch;
    @Bind(R.id.textSearchDate)
    protected TextView textSearchDate;
    @Bind(R.id.textSearchTime)
    protected TextView textSearchTime;
    @Bind(R.id.textSearchBuilding)
    protected TextView textSearchBuilding;
    @Bind(R.id.textSearchRoomSize)
    protected TextView textSearchRoomSize;
    @Bind(R.id.switchSearchPool)
    protected Switch switchPool;
    @Bind(R.id.switchSearchComputer)
    protected Switch switchComputer;
    @Bind(R.id.switchSearchBeamer)
    protected Switch switchBeamer;
    @Bind(R.id.switchSearchVideo)
    protected Switch switchVideo;
    @Bind(R.id.switchSearchLooseSeating)
    protected Switch switchLooseSeating;

    @Bind(R.id.search_layout)
    protected LinearLayout searchLayout;

    private boolean[] mSelectedBuilding;
    private int mSelectedRoomSize;
    private boolean[] mSelectedTimes;

    private GregorianCalendar selectedDate = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ButterKnife.bind(this);

        mSelectedBuilding = new boolean[getResources().getStringArray(R.array.buildings).length];
        mSelectedRoomSize = -1;
        mSelectedTimes = new boolean[getResources().getStringArray(R.array.times).length];

        initializeTextViews();
    }


    @OnClick(R.id.buttonAddGroupToRoomSearch)
    public void addGroup() {
        Intent groups = new Intent();
        groups.setClass(getApplicationContext(), GroupActivity.class);
        startActivity(groups);
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
                showSingleSelectDialog("Raumgröße", getResources().getStringArray(R.array.roomSizes), mSelectedRoomSize);
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
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GregorianCalendar today = new GregorianCalendar();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);
                if (selectedDate.compareTo(today) == 0) {
                    textSearchDate.setText(getResources().getString(R.string.today));
                } else {
                    textSearchDate.setText(selectedDate.get(Calendar.DAY_OF_MONTH) + "." + (selectedDate.get(Calendar.MONTH) + 1) + "." + selectedDate.get(Calendar.YEAR));
                }
                dialog.dismiss();
            }
        });
        builder.setView(calendar);
        builder.show();
    }

    private void showSingleSelectDialog(final String title, final String[] choices, int mSelectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int[] newSelectedItem = new int[1];

        builder.setTitle(title).setSingleChoiceItems(choices, mSelectedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newSelectedItem[0] = which;
            }
        });

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                acceptSelectedItemsRoomSize(newSelectedItem[0]);
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

    private void acceptSelectedItemsRoomSize(int selected) {
        mSelectedRoomSize = selected;
        textSearchRoomSize.setText(getResources().getStringArray(R.array.roomSizes)[mSelectedRoomSize]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_menu_search:
                searchForRooms();
                break;
            case android.R.id.home:
                onBackPressed();
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchForRooms() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Suchen", "Suche Räume", true, false);
        ApiServiceFactory services = ApiServiceFactory.getInstance();
        RoomService roomService = services.getRoomService();
        HashMap<String, String> query = new HashMap<>();

        if (!textSearchRoomSize.getText().toString().equals(getString(R.string.anyRoomSize))) {
            query.put("size", getResources().getStringArray(R.array.roomSizesNumbers)[mSelectedRoomSize]);
        }
        if (!(textSearchBuilding.getText().toString().equals(getString(R.string.anyBuilding)))) {
            String selectedBuildings = "";
            for (int i = 0; i < mSelectedBuilding.length; i++) {
                selectedBuildings += mSelectedBuilding[i] ? "," + getResources().getStringArray(R.array.buildingNames)[i] : "";
            }
            query.put("building", selectedBuildings.substring(1));
        }
        // TODO: Remove this
        query.put("day", 3 + "");

        if (!textSearchDate.getText().toString().equals("Heute")) {
            query.put("day", selectedDate.get(Calendar.DAY_OF_WEEK) + "");
        }
        if (!textSearchTime.getText().toString().equals("Jede Zeitstunde")) {
            String selectedTimes = "";
            for (int i = 0; i < mSelectedTimes.length; i++) {
                selectedTimes += mSelectedTimes[i] ? "," + (i + 1) : "";
            }
            query.put("hour", selectedTimes.substring(1));
        }
        final ArrayList<String> queryParams = new ArrayList<>();
        if (switchPool.isChecked()) {
            query.put("pool", "1");
            queryParams.add("Poolraum");
        }
        if (switchComputer.isChecked()) {
            query.put("computer", "1");
            queryParams.add("Computer");
        }
        if (switchBeamer.isChecked()) {
            query.put("beamer", "1");
            queryParams.add("Beamer");
        }
        if (switchVideo.isChecked()) {
            query.put("video", "1");
            queryParams.add("Video");
        }
        if (switchLooseSeating.isChecked()) {
            query.put("looseSeating", "1");
            queryParams.add("Lose Bestuhlung");
        }

        Call<List<Room>> call = roomService.findRooms(query);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Response<List<Room>> response, Retrofit retrofit) {
                List<Room> rooms = response.body();

                if(rooms.size() > 0) {
                    Parcelable wrapped = Parcels.wrap(rooms);

                    Intent resultIntent = new Intent();
                    resultIntent.setClass(getApplicationContext(), ResultActivity.class);
                    resultIntent.putExtra("searchResult", wrapped);
                    resultIntent.putStringArrayListExtra("searchQuery", queryParams);

                    dialog.dismiss();
                    startActivity(resultIntent);
                } else {
                    dialog.dismiss();
                    Snackbar
                            .make(searchLayout, R.string.no_search_results, Snackbar.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                Snackbar
                        .make(searchLayout, "Suche fehlgeschlagen", Snackbar.LENGTH_LONG)
                        .setAction("Nochmal", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchForRooms();
                            }
                        })
                        .show();
            }
        });
    }
}
