package my.edu.utar.blooddonationmadnew.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityEditTestBinding;

public class TestEditActivity extends AppCompatActivity {

    private DatabaseReference dbRef;

    private final String TABLE_NAME = "users";

    public final static String TAG = TestEditActivity.class.getSimpleName();

    private ActivityEditTestBinding binding;

    private String id;

    private EditText email_txt;
    private EditText pwd_txt;

    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email_txt = binding.emailTxt;
        pwd_txt = binding.pwdTxt;

        submit_btn = binding.submitBtn;

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

                email_txt.setText(tmpUser.getEmail());
                pwd_txt.setText(tmpUser.getPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

        submit_btn.setOnClickListener(view -> submitBtn());
    }

    public void submitBtn() {
        // Get Email and Password
        String email = email_txt.getText().toString();
        String password = pwd_txt.getText().toString();

        User tmpUser = new User();

        dbRef.child(id).setValue(tmpUser);

        Toast.makeText(this, String.format("User %s was successfully updated!", email), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
