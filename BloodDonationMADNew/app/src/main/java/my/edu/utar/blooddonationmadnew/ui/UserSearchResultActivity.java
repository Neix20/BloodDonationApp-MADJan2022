package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import my.edu.utar.blooddonationmadnew.adapter.UserViewAdapter;
import my.edu.utar.blooddonationmadnew.databinding.ActivityUserSearchResultBinding;
import my.edu.utar.blooddonationmadnew.data.User;

public class UserSearchResultActivity extends AppCompatActivity {

    public final static String TAG = UserSearchResultActivity.class.getSimpleName();

    private ActivityUserSearchResultBinding binding;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    private RecyclerView mRecyclerView;
    private UserViewAdapter userViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityUserSearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRecyclerView = binding.userListRecView;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        Intent mIntent = getIntent();
        String state = mIntent.getStringExtra("state");

        Query query = dbRef.orderByChild("state").equalTo(state);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        userViewAdapter = new UserViewAdapter(options);

        mRecyclerView.setAdapter(userViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onStart() {
        super.onStart();
        userViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userViewAdapter.stopListening();
    }
}
