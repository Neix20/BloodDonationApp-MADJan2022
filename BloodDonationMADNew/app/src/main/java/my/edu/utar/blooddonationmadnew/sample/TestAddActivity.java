package my.edu.utar.blooddonationmadnew.sample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAddTestBinding;

public class TestAddActivity extends AppCompatActivity {

    private DatabaseReference dbRef;

    private final String TABLE_NAME = "users";

    public final static String TAG = TestAddActivity.class.getSimpleName();

    private ActivityAddTestBinding binding;

    private EditText email_txt;
    private EditText pwd_txt;

    private Button submit_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityAddTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML element
        email_txt = binding.emailTxt;
        pwd_txt = binding.pwdTxt;

        submit_btn = binding.submitBtn;

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        submit_btn.setOnClickListener(view -> submitBtn());
    }

    public void submitBtn(){
        // Get Email and Password
        String email = email_txt.getText().toString();
        String password = pwd_txt.getText().toString();

        // Push New Id, Allocate space for new User
        User tmpUser = new User();

        // Add To Firebase
        dbRef = dbRef.push();

        String id = dbRef.getKey();
        tmpUser.setId(id);

        dbRef.setValue(tmpUser);

        Toast.makeText(this, String.format("User %s was successfully inserted!", email), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
