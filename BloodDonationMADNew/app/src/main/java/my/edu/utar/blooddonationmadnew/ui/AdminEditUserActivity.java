package my.edu.utar.blooddonationmadnew.ui;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminEditUserListBinding;
import my.edu.utar.blooddonationmadnew.databinding.ActivityEditTestBinding;
import my.edu.utar.blooddonationmadnew.sample.TestEditActivity;

public class AdminEditUserActivity extends AppCompatActivity {

    private DatabaseReference dbRef;

    private final String TABLE_NAME = "users";

    public final static String TAG = AdminEditUserActivity.class.getSimpleName();

    private ActivityAdminEditUserListBinding binding;

    private String id;

    private TextInputEditText email_txt;
    private TextInputEditText password_txt;
    private MaterialAutoCompleteTextView userType_txt;
    private TextInputEditText name_txt;
    private TextInputEditText age_txt;
    private TextInputEditText height_txt;
    private TextInputEditText weight_txt;
    private MaterialAutoCompleteTextView bloodType_txt;
    private TextInputEditText phoneNumber_txt;
    private TextInputEditText addr1_txt;
    private TextInputEditText addr2_txt;
    private TextInputEditText postCode_txt;
    private TextInputEditText city_txt;
    private MaterialAutoCompleteTextView state_txt;
    private TextInputEditText country_txt;

    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminEditUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        email_txt = binding.emailTxt;
        password_txt = binding.pwdTxt;
        userType_txt = binding.userTypeTxt;
        name_txt=binding.nameTxt;
        age_txt=binding.ageTxt;
        height_txt= binding.heightTxt;
        weight_txt=binding.weightTxt;
        bloodType_txt=binding.bloodTypeTxt;
        phoneNumber_txt=binding.phoneNumberTxt;
        addr1_txt=binding.addr1Txt;
        addr2_txt= binding.addr2Txt;
        postCode_txt= binding.postcodeTxt;
        city_txt= binding.cityTxt;
        state_txt= binding.stateTxtView;
        country_txt=binding.countryTxt;

        //Set Drop down menu debug
        ArrayList<String> stateList = new ArrayList<>(Arrays.asList("Kuala Lumpur", "Selangor", "Johor", "Penang", "Kuantan"));
        ArrayList<String> bloodTypeList = new ArrayList<>(Arrays.asList("A", "B", "O", "AB"));
        ArrayList<String> userTypeList = new ArrayList<>(Arrays.asList("user","admin"));

        ArrayAdapter<String> stateAdapter= new ArrayAdapter<>(this, R.layout.dropdownmenu_listitem, stateList);
        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdownmenu_listitem, bloodTypeList);
        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdownmenu_listitem, userTypeList);

        state_txt.setAdapter(stateAdapter);
        bloodType_txt.setAdapter(bloodTypeAdapter);
        userType_txt.setAdapter(userTypeAdapter);

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
                password_txt.setText(tmpUser.getPassword());
                userType_txt.setText(tmpUser.getUserType());
                name_txt.setText(tmpUser.getName());

                int _age = tmpUser.getAge();
                String age = (_age==0) ? "0" : String.valueOf(_age);
                age_txt.setText(age);

                double _height = tmpUser.getHeight();
                String height = (_height==0) ? "0" : String.valueOf(_height);
                height_txt.setText(height);

                double _weight =  tmpUser.getWeight();
                String weight = (_weight==0) ? "0" : String.valueOf(_weight);
                weight_txt.setText(weight);

                bloodType_txt.setText(tmpUser.getBloodType());
                phoneNumber_txt.setText(tmpUser.getPhoneNumber());
                addr1_txt.setText(tmpUser.getAddr1());
                addr2_txt.setText(tmpUser.getAddr2());
                postCode_txt.setText(tmpUser.getPostCode());
                city_txt.setText(tmpUser.getCity());
                state_txt.setText(tmpUser.getState());
                country_txt.setText(tmpUser.getCountry());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

        submit_btn.setOnClickListener(view -> submitBtn());
    }

    public void submitBtn() {
        String email = email_txt.getText().toString();
        String pwd = password_txt.getText().toString();
        String userType = userType_txt.getText().toString();
        String name = name_txt.getText().toString();

        String _age = age_txt.getText().toString();
        int age = (_age.isEmpty()) ? 0 : Integer.parseInt(_age);

        String _height = height_txt.getText().toString();
        int height = (_height.isEmpty()) ? 0 : Integer.valueOf(_height);

        String _weight = weight_txt.getText().toString();
        int weight = (_weight.isEmpty()) ? 0 : Integer.valueOf(_weight);

        String bloodType = bloodType_txt.getText().toString();
        String phoneNumber = phoneNumber_txt.getText().toString();
        String addr1 = addr1_txt.getText().toString();
        String addr2 = addr2_txt.getText().toString();
        String postCode = postCode_txt.getText().toString();
        String city = city_txt.getText().toString();
        String state = state_txt.getText().toString();
        String country = country_txt.getText().toString();

        User tmpUser = new User("", email,pwd, userType,name,age,height,weight,bloodType,phoneNumber,addr1,addr2,postCode,city,state,country);
        tmpUser.setState_bloodType(String.format("%s_%s", state, bloodType));

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
