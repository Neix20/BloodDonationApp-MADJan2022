package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.FragmentUserMapBinding;

public class UserMapFragment extends Fragment implements OnMapReadyCallback {
    public final static String TAG = UserMapFragment.class.getSimpleName();

    private FragmentUserMapBinding binding;

    private GoogleMap mMap;
    private MapView mapView;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.map;
        mapView.onCreate(savedInstanceState);

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        mapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());

        mapView.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot bloodEventSnapshot : snapshot.getChildren()){
                    BloodEvent be = bloodEventSnapshot.getValue(BloodEvent.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });
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
