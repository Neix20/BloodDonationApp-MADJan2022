package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.BloodDonationRecord;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminAddBloodDonorRecordBinding;

public class AdminAddBloodDonorRecordActivity extends AppCompatActivity {

    public static String TAG = AdminAddBloodDonorRecordActivity.class.getSimpleName();

    private ActivityAdminAddBloodDonorRecordBinding binding;

    private MaterialAutoCompleteTextView venue_txt;
    private MaterialAutoCompleteTextView user_txt;

    private Button submit_btn;

    private DatabaseReference beRef;
    private final String blood_event_table_name = "BloodEvents";

    private DatabaseReference userRef;
    private final String user_table_name = "users";

    private DatabaseReference bdrRef;
    private final String blood_donation_record_table_name = "BloodDonationRecords";


    private List<String> beNameList;
    private List<String> userNameList;

    private HashMap<String, String> bloodEventMap;
    private HashMap<String, String> userMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminAddBloodDonorRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Object to XML element
        venue_txt = binding.venueTxt;
        user_txt = binding.userTxt;

        submit_btn = binding.submitBtn;

        // Initialize Database Reference
        beNameList = new ArrayList<>();
        userNameList = new ArrayList<>();

        bloodEventMap = new HashMap<>();
        userMap = new HashMap<>();

        beRef = FirebaseDatabase.getInstance().getReference(blood_event_table_name);
        userRef = FirebaseDatabase.getInstance().getReference(user_table_name);
        bdrRef = FirebaseDatabase.getInstance().getReference(blood_donation_record_table_name);

        beRef.orderByChild("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot bloodEventSnapshot : snapshot.getChildren()){
                    BloodEvent bloodEvent = bloodEventSnapshot.getValue(BloodEvent.class);
                    beNameList.add(bloodEvent.getTitle());

                    bloodEventMap.put(bloodEvent.getTitle(), bloodEvent.getId());
                }

                userRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot userSnapshot : snapshot.getChildren()){
                            User user = userSnapshot.getValue(User.class);
                            userNameList.add(user.getName());
                            userMap.put(user.getName(), user.getId());
                        }

                        // Set Array Adapter
                        ArrayAdapter<String> bloodEventAdapter= new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdownmenu_listitem, beNameList);
                        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdownmenu_listitem, userNameList);

                        venue_txt.setAdapter(bloodEventAdapter);
                        user_txt.setAdapter(userAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "The read failed: " + error.getCode());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        submit_btn.setOnClickListener(v -> addBloodDonationRecord());

    }

    public void addBloodDonationRecord(){
        String blood_event = venue_txt.getText().toString().trim();
        String user = user_txt.getText().toString().trim();

        String blood_event_id = bloodEventMap.get(blood_event);
        String user_id = userMap.get(user);

        bdrRef = bdrRef.push();

        String id = bdrRef.getKey();
        BloodDonationRecord bloodDonationRecord = new BloodDonationRecord(id, blood_event_id, blood_event, user_id, user);

        bdrRef.setValue(bloodDonationRecord);

        Toast.makeText(this, String.format("Blood Donation Record of User %s was successfully inserted!", user), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
