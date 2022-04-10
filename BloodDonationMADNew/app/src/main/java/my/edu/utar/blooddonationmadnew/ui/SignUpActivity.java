package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private final String TABLE_NAME = "users";

    private ActivitySignupBinding binding;

    private TextInputEditText email_txt;
    private TextInputEditText pwd_txt;

    private TextInputEditText name_txt;
    private TextInputEditText age_txt;
    private MaterialAutoCompleteTextView bloodType_txt;
    private TextInputEditText phoneNumber_txt;
    private TextInputEditText height_txt;
    private TextInputEditText weight_txt;

    private TextInputEditText addr1_txt;
    private TextInputEditText addr2_txt;
    private TextInputEditText postCode_txt;
    private TextInputEditText city_txt;
    private MaterialAutoCompleteTextView state_txt;
    private TextInputEditText country_txt;

    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        email_txt = binding.emailTxt;
        pwd_txt = binding.pwdTxt;

        name_txt = binding.nameTxt;
        age_txt = binding.ageTxt;
        phoneNumber_txt = binding.phoneNumberTxt;
        bloodType_txt = binding.bloodTypeTxt;
        height_txt = binding.heightTxt;
        weight_txt = binding.weightTxt;

        addr1_txt = binding.addr1Txt;
        addr2_txt = binding.addr2Txt;
        postCode_txt = binding.postCodeTxt;
        city_txt = binding.cityTxt;
        state_txt = binding.stateTxt;
        country_txt = binding.countryTxt;

        signup_btn = binding.signupBtn;

        // Set Adapter
        String[] blood_type_arr = getResources().getStringArray(R.array.blood_type_arr);
        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, blood_type_arr);
        bloodType_txt.setAdapter(bloodTypeAdapter);

        String[] state_arr = getResources().getStringArray(R.array.state_arr);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, state_arr);
        state_txt.setAdapter(stateAdapter);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        signup_btn.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String email = email_txt.getText().toString().trim();
        String password = pwd_txt.getText().toString().trim();

        String name = name_txt.getText().toString().trim();
        String age = age_txt.getText().toString().trim();
        String phoneNumber = phoneNumber_txt.getText().toString().trim();
        String bloodType = bloodType_txt.getText().toString().trim();
        String height = height_txt.getText().toString().trim();
        String weight = weight_txt.getText().toString().trim();

        String addr1 = addr1_txt.getText().toString().trim();
        String addr2 = addr2_txt.getText().toString().trim();
        String postCode = postCode_txt.getText().toString().trim();
        String city = city_txt.getText().toString().trim();
        String state = state_txt.getText().toString().trim();
        String country = country_txt.getText().toString().trim();

        // Create User
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(res -> {
                    // Create new user and Store into Database
                    String id = mAuth.getCurrentUser().getUid();

                    User user = new User();
                    user.setId(id);

                    user.setEmail(email);
                    user.setPassword(password);

                    user.setName(name);
                    user.setPhoneNumber(phoneNumber);
                    user.setBloodType(bloodType);

                    user.setAddr1(addr1);
                    user.setAddr2(addr2);
                    user.setPostCode(postCode);
                    user.setCity(city);
                    user.setState(state);
                    user.setCountry(country);

                    int _age = (age.isEmpty()) ? 0 : Integer.valueOf(age);
                    user.setAge(_age);

                    int _height = (height.isEmpty()) ? 0 : Integer.valueOf(height);
                    user.setHeight(_height);

                    int _weight = (weight.isEmpty()) ? 0 : Integer.valueOf(weight);
                    user.setWeight(_weight);

                    // Default User Type is User
                    user.setUserType("User");
                    user.setState_bloodType(String.format("%s_%s", state, bloodType));

                    // Store into Database
                    dbRef = dbRef.child(id);
                    dbRef.setValue(user);

                    // Go to Main Activity
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

}
