package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import my.edu.utar.blooddonationmadnew.adapter.AdminBloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.UserBloodDonationRecordViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.UserUserViewAdapter;
import my.edu.utar.blooddonationmadnew.data.BloodDonationRecord;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityUserBloodDonationHistoryBinding;

public class UserBloodDonationHistoryActivity extends AppCompatActivity {

    public final static String TAG = UserBloodDonationHistoryActivity.class.getSimpleName();

    private ActivityUserBloodDonationHistoryBinding binding;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodDonationRecords";

    private RecyclerView mRecyclerView;
    private UserBloodDonationRecordViewAdapter userBloodDonationRecordViewAdapter;

    private SearchView searchBar;

    private FirebaseUser cur_user;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityUserBloodDonationHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        mRecyclerView = binding.bdrRecView;
        searchBar = binding.searchBar;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        cur_user = FirebaseAuth.getInstance().getCurrentUser();
        id = cur_user.getUid();

        Query query = dbRef.orderByChild("user_id").equalTo(id);
        FirebaseRecyclerOptions<BloodDonationRecord> options = new FirebaseRecyclerOptions.Builder<BloodDonationRecord>().setQuery(query, BloodDonationRecord.class).build();
        userBloodDonationRecordViewAdapter = new UserBloodDonationRecordViewAdapter(options);

        mRecyclerView.setAdapter(userBloodDonationRecordViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Query firebaseSearchQuery = dbRef.orderByChild("user_id_venue").startAt(String.format("%s_%s", id, searchText)).endAt(String.format("%s_%s", id, searchText) + "\uf8ff");
                FirebaseRecyclerOptions<BloodDonationRecord> options = new FirebaseRecyclerOptions.Builder<BloodDonationRecord>().setQuery(firebaseSearchQuery, BloodDonationRecord.class).build();
                userBloodDonationRecordViewAdapter = new UserBloodDonationRecordViewAdapter(options);
                userBloodDonationRecordViewAdapter.startListening();
                mRecyclerView.setAdapter(userBloodDonationRecordViewAdapter);
                return false;
            }
        });

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        userBloodDonationRecordViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userBloodDonationRecordViewAdapter.stopListening();
    }
}
