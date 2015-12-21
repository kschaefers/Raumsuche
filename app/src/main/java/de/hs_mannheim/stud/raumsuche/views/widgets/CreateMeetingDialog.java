package de.hs_mannheim.stud.raumsuche.views.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.ResultActivity;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.Group;
import de.hs_mannheim.stud.raumsuche.models.Meeting;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomQuery;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.network.ApiServiceFactory;
import de.hs_mannheim.stud.raumsuche.network.services.MeetingService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Martin on 12/20/15.
 */
public class CreateMeetingDialog extends DialogFragment {

    @Bind(R.id.create_meeting_hour_label)
    TextView hourLabel;

    @Bind(R.id.create_meeting_hour)
    Spinner hourSpinner;

    @Bind(R.id.create_meeting_groups)
    ListView groupList;

    public static final String BK_GROUPS = "bk_groups";
    public static final String BK_ROOMRESULT = "bk_roomresult";
    public static final String BK_QUERY = "bk_query";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_meeting, null);
        ButterKnife.bind(this, view);

        final List<Group> groups = Parcels.unwrap(getArguments().getParcelable(BK_GROUPS));
        final RoomResult roomResult = Parcels.unwrap(getArguments().getParcelable(BK_ROOMRESULT));
        final RoomQuery query = Parcels.unwrap(getArguments().getParcelable(BK_QUERY));
        final Room room = roomResult.getRoom();

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Resources res = getResources();
        String hourLabelFormatString = res.getString(R.string.create_meeting_hour_label);
        hourLabel.setText(String.format(hourLabelFormatString, formatter.format(query.getSearchDate())));

        final String[] hourNumbers = TextUtils.split(room.getHour(), ",");
        String[] hours = new String[hourNumbers.length];

        for(int i=0; i < hours.length; i++) {
            hours[i] = hourNumbers[i] + ". Block";
        }

        hourSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, hours));

        String[] groupNames = new String[groups.size()];

        for (int i = 0; i < groups.size(); i++) {
            groupNames[i] = groups.get(i).getName();
        }

        groupList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, groupNames));
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int positionSpinner = hourSpinner.getSelectedItemPosition();

                int hour = Integer.parseInt(hourNumbers[positionSpinner]);
                Group group = groups.get(position);

                UserManager userManager = UserManager.getInstance(getActivity());
                User myUser = userManager.getUser();

                ApiServiceFactory serviceFactory = ApiServiceFactory.getInstance();
                MeetingService meetingService = serviceFactory.getMeetingService(myUser.getMtklNr(), userManager.getUserPassword());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                Meeting newMeeting = new Meeting();
                newMeeting.setRoom(room.getName());
                newMeeting.setDay(formatter.format(query.getSearchDate()));
                newMeeting.setGroup(group);
                newMeeting.setHour(hour);

                Call<Meeting> meetingCall = meetingService.createMeeting(newMeeting);
                meetingCall.enqueue(new Callback<Meeting>() {
                    @Override
                    public void onResponse(Response<Meeting> response, Retrofit retrofit) {
                        Meeting meeting = response.body();
                        if (meeting != null) {
                            Toast.makeText(getActivity(), R.string.result_meeting_created, Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();

                        } else {
                            Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Gruppentreffen in " + room.getName());
        builder.setView(view);


        return builder.create();
    }
}
