package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import my.edu.utar.blooddonationmadnew.adapter.AdminUserViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.UserUserViewAdapter;
import my.edu.utar.blooddonationmadnew.databinding.ActivityUserSearchResultBinding;
import my.edu.utar.blooddonationmadnew.data.User;

public class UserSearchResultActivity extends AppCompatActivity {

    public final static String TAG = UserSearchResultActivity.class.getSimpleName();

    private ActivityUserSearchResultBinding binding;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    private RecyclerView mRecyclerView;
    private UserUserViewAdapter userUserViewAdapter;
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityUserSearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRecyclerView = binding.userListRecView;
        searchBar = binding.searchBar;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        Intent mIntent = getIntent();
        String state = mIntent.getStringExtra("state");
        String bloodType = mIntent.getStringExtra("blood_type");

        Query query = dbRef.orderByChild("state_bloodType").equalTo(String.format("%s_%s", state, bloodType));
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        userUserViewAdapter = new UserUserViewAdapter(options);

        mRecyclerView.setAdapter(userUserViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Query firebaseSearchQuery = dbRef.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(firebaseSearchQuery, User.class).build();
                userUserViewAdapter = new UserUserViewAdapter(options);
                userUserViewAdapter.startListening();
                mRecyclerView.setAdapter(userUserViewAdapter);
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
        userUserViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userUserViewAdapter.stopListening();
    }
}
