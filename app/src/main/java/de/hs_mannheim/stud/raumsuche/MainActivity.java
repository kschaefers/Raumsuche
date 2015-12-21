package de.hs_mannheim.stud.raumsuche;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.Meeting;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.network.ApiServiceFactory;
import de.hs_mannheim.stud.raumsuche.network.services.MeetingService;
import de.hs_mannheim.stud.raumsuche.network.services.RoomService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_LOGIN = 100;
    public static final int PERMISSION_WRITE_CALENDAR = 123;

    Parcelable wrapped;
    Meeting nextMeetingObject;

    @Bind(R.id.main_scrollView)
    View scrollView;

    @Bind(R.id.main_registered_view)
    View registeredUserView;

    @Bind(R.id.main_unregistered_view)
    View unregisteredUserView;

    @Bind(R.id.main_room_nextroom)
    View nextRoom;
    @Bind(R.id.room_availability)
    TextView availableRoomAvailability;
    @Bind(R.id.room_identifier)
    TextView availableRoomName;
    @Bind(R.id.room_properties)
    TextView availableRoomProps;
    @Bind(R.id.main_showroom_button)
    Button showRoomButton;

    @Bind(R.id.main_next_meeting)
    View nextMeeting;
    @Bind(R.id.main_meeting_text)
    TextView nextMeetingText;
    @Bind(R.id.main_meeting_topic)
    TextView nextMeetingTopic;
    @Bind(R.id.main_meeting_add_to_calendar)
    ImageButton nextMeetingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadNextRoom();
        loadNextMeeting();
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
        if(this.wrapped != null){
            result.putExtra("searchResult", this.wrapped);
        }
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

    private void loadNextRoom() {
        UserManager manager = UserManager.getInstance(this);
        User user = manager.getUser();
        ApiServiceFactory services = ApiServiceFactory.getInstance();
        RoomService roomService = services.getRoomService();
        HashMap<String,String> query = new HashMap<>();
        if(user != null){
            query.put("building", user.getBuilding());
        }
        int today = new GregorianCalendar().get(Calendar.DAY_OF_WEEK) - 1;
        if(today == 0){
            today = 7;
        }
        query.put("hour",String.valueOf(getCurrentTimeSlot()));
        query.put("day", String.valueOf(today));
        Call<List<Room>> call = roomService.findRooms(query);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Response<List<Room>> response, Retrofit retrofit) {
                List<Room> rooms = response.body();
                if (rooms.size() > 0) {
                    Room first = rooms.get(0);
                    RoomResult query = new RoomResult();
                    query.setRoom(first);
                    availableRoomName.setText(first.getName());
                    availableRoomAvailability.setText(query.getAvailable());
                    availableRoomProps.setText(TextUtils.join(", ", first.getRoomProperties()));
                    showRoomButton.setVisibility(View.VISIBLE);
                    wrapped = Parcels.wrap(rooms);
                } else {
                    availableRoomName.setText("Derzeit leider keine freien Räume");
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void loadNextMeeting() {

        UserManager manager = UserManager.getInstance(this);
        if(manager.isUserLoggedIn()){
            ApiServiceFactory services = ApiServiceFactory.getInstance();
            MeetingService meetingService = services.getMeetingService(manager.getUser().getMtklNr(), manager.getUserPassword());
            Call<List<Meeting>> call = meetingService.listUserMeetings(manager.getUser().getMtklNr());
            call.enqueue(new Callback<List<Meeting>>() {
                @Override
                public void onResponse(Response<List<Meeting>> response, Retrofit retrofit) {
                    nextMeeting.setVisibility(View.VISIBLE);
                    List<Meeting> meetings = response.body();
                    if (meetings.size() > 0) {
                        Meeting first = meetings.get(0);
                        nextMeetingObject = first;
                        nextMeetingTopic.setText(first.getGroup().getName());
                        nextMeetingText.setText(first.getDay().substring(8,10)+"."+first.getDay().substring(5,7) + ". - " + first.getHour() + ". Block in " + first.getRoom());
                        nextMeetingButton.setVisibility(View.VISIBLE);
                        nextMeetingButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkCalendarPermissions();
                            }
                        });
                    } else {
                        nextMeetingText.setText("Keine Treffen geplant");
                        nextMeetingButton.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    nextMeeting.setVisibility(View.GONE);
                }
            });
        }
    }

    private void checkCalendarPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_WRITE_CALENDAR);
            }else{
                addEventToCalendar();
            }
        }else{
            addEventToCalendar();
        }
    }

    private void addEventToCalendar(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(nextMeetingObject.getDay());
            Calendar cal = sdf.getCalendar();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            GregorianCalendar gregorianCalendar = getTimeForTimeSlot(nextMeetingObject.getHour(),cal);
            if(gregorianCalendar != null){
                intent.putExtra("beginTime", gregorianCalendar.getTimeInMillis());
                intent.putExtra("endTime", gregorianCalendar.getTimeInMillis()+60*90*1000);
                intent.putExtra("allDay", false);
            }else{
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                intent.putExtra("allDay", true);
            }
            intent.putExtra("title", "Treffen mit "+nextMeetingObject.getGroup().getName());
            startActivity(intent);
        } catch (ParseException e) {
            Log.e("MainActivity","Parsing failed",e);
        }
    }

    private int getCurrentTimeSlot(){
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar slot1 = new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),8,0);
        GregorianCalendar slot2 = new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),9,30);
        GregorianCalendar slot3 = new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),11,15);
        GregorianCalendar slot4 = new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),13,30);
        GregorianCalendar slot5 = new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),15,10);
        GregorianCalendar slot6 = new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),16,50);

        if(now.compareTo(slot1) == -1){
            return 0;
        }else if(now.compareTo(slot1) > -1 && now.compareTo(slot2) == -1){
            return 1;
        }else if(now.compareTo(slot2) > -1 && now.compareTo(slot3) == -1){
            return 2;
        }else if(now.compareTo(slot3) > -1 && now.compareTo(slot4) == -1){
            return 3;
        }else if(now.compareTo(slot4) > -1 && now.compareTo(slot5) == -1){
            return 4;
        }else if(now.compareTo(slot5) > -1 && now.compareTo(slot6) == -1){
            return 5;
        }else{
            return 6;
        }
    }

    private GregorianCalendar getTimeForTimeSlot(int hour, Calendar cal){
        switch (hour){
            case 1:
                return new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),8,0);
            case 2:
                return new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),9,30);
            case 3:
                return new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),11,15);
            case 4:
                return new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),13,30);
            case 5:
                return new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),15,10);
            case 6:
                return new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),16,50);
            default:
                return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addEventToCalendar();
                } else {
                    Snackbar.make(scrollView,"Ohne die Rechte kann kein Event hinzugefügt werden",Snackbar.LENGTH_LONG).setAction("Ups", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkCalendarPermissions();
                        }
                    }).show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
