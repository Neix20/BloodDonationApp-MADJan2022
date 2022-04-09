package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private final String TABLE_NAME = "users";

    private ActivitySignupBinding binding;

    private EditText email_txt;
    private EditText pwd_txt;

    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        email_txt = binding.emailTxt;
        pwd_txt = binding.pwdTxt;

        submit_btn = binding.signupBtn;

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    private void signup(){
        String email = email_txt.getText().toString();
        String password = pwd_txt.getText().toString();

        // Create User
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(res -> {
                    // Create new user and Store into Database
                    String id = mAuth.getCurrentUser().getUid();

                    User user = new User();

                    dbRef = dbRef.child(id);
                    dbRef.setValue(user);

                    // Go to Main Activity
                    Intent intent = new Intent(this, AdminMainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());

        // Store into Database
    }

}
