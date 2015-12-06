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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.models.RoomQuery;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;
import de.hs_mannheim.stud.raumsuche.views.adapters.RoomResultListAdapter;

public class ResultFragment extends Fragment {

    public static final String BK_DATA = "bk_data";

    OnResultSelectedListener callback;

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
            callback = (OnResultSelectedListener) activity;
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

        List<RoomResult> results = new ArrayList<RoomResult>();
        RoomQuery query = new RoomQuery();
        List<String> searchProperties = new ArrayList<String>();
        query.setProperties(searchProperties);

        adapter = new RoomResultListAdapter(getContext(), results, query);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RoomResult result = (RoomResult) adapter.getItem(position);
                callback.onResultSelected(result);
            }
        });
        return view;
    }

    public void updateResultList(List<RoomResult> results, RoomQuery query) {
        adapter.setResults(results);
        adapter.setQuery(query);
        adapter.notifyDataSetChanged();
    }

    public interface OnResultSelectedListener {
        public void onResultSelected(RoomResult selectedResult);
    }
}
