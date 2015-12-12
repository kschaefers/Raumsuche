package de.hs_mannheim.stud.raumsuche;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hs_mannheim.stud.raumsuche.fragments.ResultFragment;
import de.hs_mannheim.stud.raumsuche.managers.BuildingFactory;
import de.hs_mannheim.stud.raumsuche.models.Building;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomQuery;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;
import de.hs_mannheim.stud.raumsuche.models.DummyData;

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback, ResultFragment.OnResultSelectedListener {

    private GoogleMap googleMap;
    private Map<String, Marker> mapMarkers;

    private List<RoomResult> results;
    private RoomQuery query;
    private SupportMapFragment mapFragment;
    private ResultFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mapMarkers = new HashMap<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listFragment = (ResultFragment) getSupportFragmentManager().findFragmentById(R.id.list);

        // TODO: Remove Test-Data
        RoomResult testA = DummyData.buildTestResult(this, "A");
        RoomResult testB = DummyData.buildTestResult(this, "B");
        RoomResult testG = DummyData.buildTestResult(this, "G");
        RoomResult testH = DummyData.buildTestResult(this, "H");
        RoomResult testL = DummyData.buildTestResult(this, "L");
        RoomResult testS = DummyData.buildTestResult(this, "S");
        results = new ArrayList<RoomResult>();
        results.add(testA);
        results.add(testB);
        results.add(testG);
        results.add(testH);
        results.add(testL);
        results.add(testS);

        // TODO: Remove Test-Query
        RoomQuery testQuery = DummyData.buildTestQuery(this);
        query = testQuery;

        listFragment.updateResultList(results, query);
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
}
