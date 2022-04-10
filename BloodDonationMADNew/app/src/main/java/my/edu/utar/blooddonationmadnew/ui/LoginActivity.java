package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private final String TABLE_NAME = "users";

    private ActivityLoginBinding binding;

    private TextInputEditText email_txt;
    private TextInputEditText pwd_txt;

    private Button login_btn;
    private Button signup_btn;

    private FirebaseUser cur_user;
    private String cur_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML element
        email_txt = binding.emailTxt;
        pwd_txt = binding.pwdTxt;

        login_btn = binding.loginBtn;
        signup_btn = binding.signupBtn;

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        login_btn.setOnClickListener(v -> loginUser());
        signup_btn.setOnClickListener(v -> registerUser());
    }

    public void loginUser(){
        String email = email_txt.getText().toString().trim();
        String password = pwd_txt.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    cur_user = FirebaseAuth.getInstance().getCurrentUser();
                    cur_id = cur_user.getUid();

                    dbRef.child(cur_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User tmpUser = snapshot.getValue(User.class);
                            String userType = tmpUser.getUserType();

                            if(userType.equals("Admin")){
                                Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "The read failed: " + error.getCode());
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public void registerUser(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
