package de.hs_mannheim.stud.raumsuche.fragments;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomQuery;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.views.adapters.RoomResultListAdapter;

public class ResultFragment extends Fragment implements RoomResultListAdapter.OnGroupActionListener {

    public static final String BK_RESULTS = "bk_results";
    public static final String BK_QUERY = "bk_query";

    OnListItemAcionListener callback;

    @Bind(R.id.room_result_list)
    ListView list;
    RoomResultListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (OnListItemAcionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_result, container, false);
        ButterKnife.bind(this, view);

        UserManager manager = UserManager.getInstance(getContext());

        Bundle arguments = getArguments();
        List<RoomResult> results = Parcels.unwrap(arguments.getParcelable(BK_RESULTS));
        RoomQuery query = Parcels.unwrap(arguments.getParcelable(BK_QUERY));

        adapter = new RoomResultListAdapter(getContext(), results, query);
        adapter.setEnableGroupActions(manager.isUserLoggedIn());
        adapter.setOnGroupAction(this);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RoomResult result = (RoomResult) adapter.getItem(position);
                adapter.setSelectedRoom(position);
                callback.onResultSelected(result);
            }
        });
        return view;
    }

    public interface OnListItemAcionListener {
        void onResultSelected(RoomResult selectedResult);
        void onGroupNotify(RoomResult room);
    }

    @Override
    public void onGroupNotify(RoomResult roomResult) {
        if(callback != null) {
            callback.onGroupNotify(roomResult);
        }
    }
}
