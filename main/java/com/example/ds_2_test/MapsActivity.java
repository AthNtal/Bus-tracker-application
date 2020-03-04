package com.example.ds_2_test;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String t;
    ArrayList <Value> values;
    double[] lat1,lon1;


    ArrayList<Marker> markers = new ArrayList<>();

    MarkerOptions markerOptions = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        t=(String) getIntent().getSerializableExtra("Bus");
        lat1 =(double[]) getIntent().getDoubleArrayExtra("Lat");
        lon1 =(double[]) getIntent().getDoubleArrayExtra("Lon");


        mapFragment.getMapAsync(this);

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
        double[][] coords =new double[5][2];

        for (int i=0;i<5; i++){
            coords[i][0] = lat1[i];
        }
        for (int j=0; j<5; j++) {
            coords[j][1] = lon1[j];
        }
        for (int i=0; i<5; i++){
            for (int j=0; j<2; j++){

                LatLng point =new LatLng(coords[i][0],coords[i][1]);

                markerOptions.position(point);
                markerOptions.title(t);
                markers.add(mMap.addMarker(markerOptions));}
        }



        LatLng athens = new LatLng(37.99, 23.731968);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(athens));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


        mMap.setTrafficEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);//easyPermissions library
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

}
