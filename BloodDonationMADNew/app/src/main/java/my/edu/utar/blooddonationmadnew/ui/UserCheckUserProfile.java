package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.data.User;

import my.edu.utar.blooddonationmadnew.databinding.ActivityCheckUserProfileBinding;

public class UserCheckUserProfile extends AppCompatActivity {

    private DatabaseReference dbRef;

    private final String TABLE_NAME = "users";

    public final static String TAG = UserCheckUserProfile.class.getSimpleName();

    private ActivityCheckUserProfileBinding binding;

    private String id;

    private TextInputEditText name_txt;
    private TextInputEditText age_txt;
    private TextInputEditText bloodType_txt;
    private TextInputEditText phoneNumber_txt;
    private TextInputEditText addr1_txt;
    private TextInputEditText addr2_txt;
    private TextInputEditText postCode_txt;
    private TextInputEditText city_txt;
    private TextInputEditText state_txt;
    private TextInputEditText country_txt;


    //TOdo add image view for avatar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCheckUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        name_txt=binding.nameTxtView;
        age_txt=binding.ageTxtView;
        phoneNumber_txt=binding.phoneNoTxtView;
        bloodType_txt=binding.bloodTypeTxtView;
        addr1_txt=binding.addr1TxtView;
        addr2_txt= binding.addr2TxtView;
        postCode_txt= binding.postcodeTxtView;
        city_txt= binding.cityTxtView;
        state_txt= binding.stateTxtView;
        country_txt=binding.countryTxtView;

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent mIntent = getIntent();
        id = mIntent.getStringExtra("id");

        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User tmpUser = snapshot.getValue(User.class);

                name_txt.setText(tmpUser.getName());

                int _age = tmpUser.getAge();
                String age = (_age==0) ? "0" : String.valueOf(_age);
                age_txt.setText(age);

                bloodType_txt.setText(tmpUser.getBloodType());
                phoneNumber_txt.setText(tmpUser.getPhoneNumber());
                addr1_txt.setText(tmpUser.getAddr1());
                addr2_txt.setText(tmpUser.getAddr2());
                postCode_txt.setText(tmpUser.getPostCode());
                city_txt.setText(tmpUser.getCity());
                state_txt.setText(tmpUser.getState());
                country_txt.setText(tmpUser.getCountry());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

    }


}
