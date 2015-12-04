package de.hs_mannheim.stud.raumsuche;

        import android.graphics.Color;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

        import java.util.ArrayList;
        import java.util.List;

public class ResultActivity extends FragmentActivity implements OnMapReadyCallback, ResultFragment.OnResultSelectedListener {

    private GoogleMap mMap;
    List<RoomResult> results;
    RoomQuery query;
    SupportMapFragment mapFragment;
    ResultFragment listFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listFragment = (ResultFragment) getSupportFragmentManager().findFragmentById(R.id.list);

        RoomResult testData = DummyData.buildTestResult(this);
        results = new ArrayList<RoomResult>();
        results.add(testData);

        RoomQuery testQuery = DummyData.buildTestQuery(this);
        query = testQuery;

        listFragment.updateResultList(results,query);
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
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if(results.size() > 0) {
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
        int buildingRGB = room.getBuildingColor();
        float[] buildingHsv = new float[3];
        Color.RGBToHSV(Color.red(buildingRGB), Color.green(buildingRGB), Color.blue(buildingRGB), buildingHsv);
        LatLng buildingPosition = new LatLng(room.geoPositionLat, room.geoPositionLng);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(buildingPosition, 18.f));

        MarkerOptions marker = new MarkerOptions().position(buildingPosition).icon(BitmapDescriptorFactory.defaultMarker(buildingHsv[0]));
        mMap.addMarker(marker);
    }
}
