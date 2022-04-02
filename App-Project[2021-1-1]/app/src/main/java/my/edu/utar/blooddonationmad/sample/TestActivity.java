package my.edu.utar.blooddonationmad.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import my.edu.utar.blooddonationmad.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    public final static String TAG = TestActivity.class.toString();
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
        dbRef = FirebaseDatabase.getInstance().getReference();
        userViewAdapter = new UserViewAdapter(this);

        mRecyclerView.setAdapter(userViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        Log.i(TAG, "Fuck you");

        List<User> tmpUserList = new ArrayList<>();

        // Attach a listener to read the data at our posts reference
        // AJAX
        dbRef.child(TABLE_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User tmpUser = userSnapshot.getValue(User.class);
                    Log.i(TAG, tmpUser.toString());
                    tmpUserList.add(tmpUser);
                }
                Log.i(TAG, String.format("Size: %d", tmpUserList.size()));
                userViewAdapter.updateList(tmpUserList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The read failed: " + databaseError.getCode());
            }
        });

        fab.setOnClickListener(view -> nAddPwd());
    }

    public void nAddPwd(){
        Intent intent = new Intent(this, TestAddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = item.getOrder();

        // Alert Dialog
        new AlertDialog.Builder(this.getContext())
                .setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    User elem = null;
                    try {
                        pwd = viewModel.getPassword((long) pos);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    viewModel.deletePassword(pwd);

                    viewModel.getPwdList().observe(this, pwdList -> {
                        mAdapter.updateList(pwdList);
                    });
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .show();

        return super.onContextItemSelected(item);
    }


}
