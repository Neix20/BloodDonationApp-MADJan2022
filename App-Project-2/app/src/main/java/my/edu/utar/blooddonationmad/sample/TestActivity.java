package my.edu.utar.blooddonationmad.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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

    private ListView userListView;

    private FloatingActionButton fab;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Object to XML Element
        userListView = binding.userListView;
        fab = binding.fab;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference();

        // List of user
        List<String> userStrList = new ArrayList<String>();

        // Replace with Get All Data From Firebase
//        List<User> userList = new ArrayList<User>(Arrays.asList(
//                new User("1", "txen2000@gmail.com", "abc123", "user"),
//                new User("2", "txen2000@gmail.com", "abc123", "user"),
//                new User("3", "txen2000@gmail.com", "abc123", "admin"),
//                new User("4", "txen2000@gmail.com", "abc123", "admin")
//        ));
//
//        for(User user : userList){
//            userStrList.add(user.toString());
//        }

        Log.i(TAG, "Fuck you");

        // Attach a listener to read the data at our posts reference
        // AJAX
        dbRef.child(TABLE_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User tmp = userSnapshot.getValue(User.class);
                    userStrList.add(tmp.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The read failed: " + databaseError.getCode());
            }
        });

//        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userStrList);
//
//        userListView.setAdapter(userAdapter);

        fab.setOnClickListener(view -> nAddPwd());
    }

    public void nAddPwd(){
        Intent intent = new Intent(this, TestAddActivity.class);
        startActivity(intent);
    }


}
