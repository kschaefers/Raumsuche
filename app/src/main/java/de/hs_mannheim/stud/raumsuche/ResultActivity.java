package de.hs_mannheim.stud.raumsuche;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.fragments.ResultFragment;
import de.hs_mannheim.stud.raumsuche.managers.BuildingFactory;
import de.hs_mannheim.stud.raumsuche.models.Building;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomQuery;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback, ResultFragment.OnResultSelectedListener {

    private GoogleMap googleMap;
    private Map<String, Marker> mapMarkers;

    private List<RoomResult> results;
    private RoomQuery query;
    private SupportMapFragment mapFragment;
    private ResultFragment listFragment;

    @Bind(R.id.result_progress)
    ProgressBar resultProgress;

    @Bind(R.id.result_searchresult_empty_label)
    TextView emptySearchResultLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        initData();
        initComponents();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if (results != null) {
            for (RoomResult result : results) {
                Room room = result.getRoom();

                if (!mapMarkers.containsKey(room.getBuilding())) {
                    BuildingFactory buildingFactory = BuildingFactory.getInstance(this);
                    Building building = buildingFactory.getBuildingByIdentifier(room.getBuilding());

                    int buildingRGB = building.getBuildingColor();
                    float[] buildingHsv = new float[3];
                    Color.RGBToHSV(Color.red(buildingRGB), Color.green(buildingRGB), Color.blue(buildingRGB), buildingHsv);

                    LatLng buildingPosition = new LatLng(building.getGeoPositionLat(), building.getGeoPositionLng());
                    MarkerOptions marker = new MarkerOptions().position(buildingPosition).icon(BitmapDescriptorFactory.defaultMarker(buildingHsv[0]));
                    Marker buildingMarker = googleMap.addMarker(marker);
                    mapMarkers.put(building.getIdentifier(), buildingMarker);
                }
            }

            RoomResult first = results.get(0);
            updateBuilding(first);
        }
    }

    @Override
    public void onResultSelected(RoomResult selectedResult) {
        updateBuilding(selectedResult);
    }

    private void updateBuilding(RoomResult result) {
        Room room = result.getRoom();

        Marker marker = mapMarkers.get(room.getBuilding());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18.f));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initData() {
        mapMarkers = new HashMap<>();

        if (getIntent().getParcelableExtra("searchResult") != null) {
            List<Room> rooms = Parcels.unwrap(getIntent().getParcelableExtra("searchResult"));
            results = new ArrayList<RoomResult>();
            for (Room room : rooms) {
                RoomResult result = new RoomResult();
                result.setRoom(room);
                result.setId(room.getName().hashCode());

                results.add(result);
            }
        }

        if (getIntent().getStringArrayListExtra("searchQuery") != null) {
            ArrayList<String> queryParams = getIntent().getStringArrayListExtra("searchQuery");
            RoomQuery searchQuery = new RoomQuery();
            searchQuery.setProperties(queryParams);
            query = searchQuery;
        }
    }

    private void initComponents() {
        resultProgress.setVisibility(View.GONE);

        if(results.size() > 0) {
            mapFragment = SupportMapFragment.newInstance();
            listFragment = new ResultFragment();

            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.result_map_container, mapFragment);
            fragmentTransaction.add(R.id.result_list_container, listFragment);
            fragmentTransaction.commit();

            mapFragment.getMapAsync(this);
            listFragment.updateResultList(results, query);
        } else {
            emptySearchResultLabel.setVisibility(View.VISIBLE);
        }
    }
}
