package my.edu.utar.blooddonationmadnew.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminMapBinding;
import my.edu.utar.blooddonationmadnew.databinding.FragmentUserMapBinding;
import my.edu.utar.blooddonationmadnew.util.Util;

public class AdminMapFragment extends Fragment {
    public final static String TAG = AdminMapFragment.class.getSimpleName();

    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private FragmentAdminMapBinding binding;

    private GoogleMap mMap;
    private MapView mapView;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    private FusedLocationProviderClient fusedLocationClient;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.map;
        mapView.onCreate(savedInstanceState);

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        mapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return root;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    updateLocationUI(location, googleMap);
                                }
                            });
                        }
                    }
                });

        return root;
    }

    public void updateLocationUI(Location location, @NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set Current Position
        final double cur_lng = location.getLongitude();
        final double cur_lat = location.getLatitude();

        LatLng cur_latLng = new LatLng(cur_lat, cur_lng);
        MarkerOptions cur_marker = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("person", 100, 100)))
                .position(cur_latLng)
                .title("My Position");
        mMap.addMarker(cur_marker);

        // Populate Map With Blood Event Marker
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double be_lng, be_lat, distance;
                LatLng be_latLng;
                MarkerOptions be_marker;

                List<String> title_list = new ArrayList<>();
                List<Double> distance_list = new ArrayList<>();

                for (DataSnapshot bloodEventSnapshot : snapshot.getChildren()) {
                    BloodEvent be = bloodEventSnapshot.getValue(BloodEvent.class);

                    be_lng = be.getLongitude();
                    be_lat = be.getLatitude();

                    be_latLng = new LatLng(be_lat, be_lng);

                    be_marker = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("medical_center", 100, 100)))
                            .position(be_latLng)
                            .title(be.getTitle());

                    distance = Util.distance(cur_lat, cur_lng, be_lat, be_lng);

                    title_list.add(be.getTitle());
                    distance_list.add(distance);

                    mMap.addMarker(be_marker);
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        for(int i = 0; i < title_list.size(); i++){
                            String title = title_list.get(i);
                            double distance = distance_list.get(i);

                            if(marker.getTitle().equals(title)){
                                Toast.makeText(getActivity().getApplicationContext(), String.format("Distance: %skm", Util.formatNumber((int) distance, ",")), Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

        // Move Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur_latLng, 8));
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(
                getResources(),
                getResources().getIdentifier(iconName, "drawable", this.getContext().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
