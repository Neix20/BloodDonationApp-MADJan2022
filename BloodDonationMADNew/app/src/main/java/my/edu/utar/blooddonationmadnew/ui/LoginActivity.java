package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import my.edu.utar.blooddonationmadnew.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private EditText email_txt;
    private EditText pwd_txt;

    private Button login_btn;
    private Button signup_btn;

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

        login_btn.setOnClickListener(v -> {});
    }

    public void loginUser(){

    }
}
