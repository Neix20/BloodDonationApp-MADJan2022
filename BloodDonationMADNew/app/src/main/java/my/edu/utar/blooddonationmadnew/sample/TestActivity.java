package my.edu.utar.blooddonationmadnew.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    public final static String TAG = TestActivity.class.getSimpleName();
    private final String TABLE_NAME = "users";

    private ActivityTestBinding binding;

    private FloatingActionButton fab;

    private DatabaseReference dbRef;

    private RecyclerView mRecyclerView;
    private UserViewAdapter userViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Object to XML Element
        mRecyclerView = binding.recView;
        fab = binding.fab;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(dbRef, User.class).build();

        userViewAdapter = new UserViewAdapter(options);

        mRecyclerView.setAdapter(userViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        fab.setOnClickListener(view -> nAddPwd());
    }

    public void nAddPwd(){
        Intent intent = new Intent(this, TestAddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = item.getOrder();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ind = 0;

                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    if(ind == pos){
                        User tmpUser = userSnapshot.getValue(User.class);
                        dbRef.child(tmpUser.getId()).removeValue();

                        Toast.makeText(getApplicationContext(), String.format("User %s was successfully removed!", tmpUser.getEmail()), Toast.LENGTH_SHORT).show();
                    }
                    ind++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userViewAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userViewAdapter.stopListening();
    }
}
