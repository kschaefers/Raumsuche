package de.hs_mannheim.stud.raumsuche;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.network.ApiServiceFactory;
import de.hs_mannheim.stud.raumsuche.network.services.RoomService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_LOGIN = 100;

    Parcelable wrapped;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadNextRoom();

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
        query.put("hour",String.valueOf(getCurrentTimeSlot()));
        Call<List<Room>> call = roomService.findRooms(query);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Response<List<Room>> response, Retrofit retrofit) {
                List<Room> rooms = response.body();
                if(rooms.size()>0) {
                    Room first = rooms.get(0);
                    RoomResult query = new RoomResult();
                    query.setRoom(first);
                    availableRoomName.setText(first.getName());
                    availableRoomAvailability.setText(query.getAvailable());
                    availableRoomProps.setText(TextUtils.join(",", first.getRoomProperties()));
                    nextRoom.setVisibility(View.VISIBLE);
                    showRoomButton.setEnabled(true);
                    wrapped = Parcels.wrap(rooms);
                }else{
                    availableRoomName.setText("Derzeit leider keine freien Räume");
                    nextRoom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
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

}
