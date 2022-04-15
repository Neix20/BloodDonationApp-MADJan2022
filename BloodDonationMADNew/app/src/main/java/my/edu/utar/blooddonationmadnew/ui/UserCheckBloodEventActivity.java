package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.ActivityUserCheckBloodEventBinding;

public class UserCheckBloodEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    public final static String TAG = UserCheckBloodEventActivity.class.getSimpleName();

    private ActivityUserCheckBloodEventBinding binding;

    private String id;
    private String gTitle;

    private TextInputEditText title_txt;
    private TextInputEditText description_txt;
    private TextInputEditText phone_txt;

    private TextInputEditText address_line_1_txt;
    private TextInputEditText address_line_2_txt;
    private TextInputEditText post_code_txt;
    private TextInputEditText city_txt;
    private MaterialAutoCompleteTextView state_txt;
    private TextInputEditText country_txt;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    private GoogleMap mMap;

    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserCheckBloodEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        title_txt = binding.titleTxt;
        description_txt = binding.descriptionTxt;
        phone_txt = binding.phoneNumberTxt;

        address_line_1_txt = binding.addressLine1Txt;
        address_line_2_txt = binding.addressLine2Txt;
        post_code_txt = binding.postCodeTxt;
        city_txt = binding.cityTxt;
        state_txt = binding.stateTxt;
        country_txt = binding.countryTxt;

        // Create Adapter for State Text
        String[] state_arr = getResources().getStringArray(R.array.state_arr);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.dropdownmenu_listitem, state_arr);
        state_txt.setAdapter(stateAdapter);

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        // Populate Details
        Intent mIntent = getIntent();
        id = mIntent.getStringExtra("id");

        // Populate Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void showRoute() {
        Intent intent = new Intent(this, MapResultActivity.class);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        intent.putExtra("title", gTitle);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigate, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_navigate) {
            showRoute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BloodEvent be = snapshot.getValue(BloodEvent.class);

                title_txt.setText(be.getTitle());
                gTitle = be.getTitle();

                description_txt.setText(be.getDescription());
                phone_txt.setText(be.getPhoneNumber());

                address_line_1_txt.setText(be.getAddress_line_1());
                address_line_2_txt.setText(be.getAddress_line_2());
                post_code_txt.setText(be.getPostCode());
                city_txt.setText(be.getCity());
                state_txt.setText(be.getState());
                country_txt.setText(be.getCountry());

                longitude = be.getLongitude();
                latitude = be.getLatitude();

                LatLng location = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("medical_center", 100, 100)))
                        .position(location)
                        .title(be.getTitle())
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
