package my.edu.utar.blooddonationmadnew.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.databinding.ActivityMapRouteBinding;
import my.edu.utar.blooddonationmadnew.util.Util;

public class AdminMapResultActivity extends AppCompatActivity{

    public final static String TAG = AdminMapResultActivity.class.getSimpleName();

    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ActivityMapRouteBinding binding;

    private GoogleMap mMap;

    private FusedLocationProviderClient fusedLocationClient;

    private double be_lng;
    private double be_lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.res_map);

        Intent mIntent = getIntent();
        be_lng = mIntent.getDoubleExtra("longitude", 0);
        be_lat = mIntent.getDoubleExtra("latitude", 0);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    mMap = googleMap;

                                    double longitude = location.getLongitude();
                                    double latitude = location.getLatitude();

                                    LatLng latLng = new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("person", 100, 100)))
                                            .position(latLng)
                                            .title("Depot")
                                    );

                                    // Add Medical Center marker
                                    LatLng be_latLng = new LatLng(be_lat, be_lng);

                                    double distance = Util.distance(latLng.latitude, latLng.longitude, be_lat, be_lng);

                                    mMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("medical_center",100,100)))
                                            .position(be_latLng)
                                            .title("Destination"));

                                    // Add Route
                                    PolylineOptions lineOptions = new PolylineOptions();
                                    lineOptions.add(latLng, be_latLng);
                                    lineOptions.width(4);
                                    lineOptions.color(Color.RED);
                                    lineOptions.geodesic(true);
                                    mMap.addPolyline(lineOptions);

                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));

                                    // Set On Click Listener
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(@NonNull Marker marker) {
                                            if(marker.getTitle().equals("Destination"))
                                                Toast.makeText(getApplicationContext(), String.format("Distance: %skm", Util.formatNumber((int) distance, ",")), Toast.LENGTH_SHORT).show();
                                            return false;
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
