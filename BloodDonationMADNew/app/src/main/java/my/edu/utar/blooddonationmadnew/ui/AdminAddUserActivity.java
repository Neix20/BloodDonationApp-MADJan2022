package my.edu.utar.blooddonationmadnew.ui;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Arrays;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminAddUserListBinding;


public class AdminAddUserActivity extends AppCompatActivity {
    public final String TAG = AdminAddUserActivity.class.getSimpleName();

    private ActivityAdminAddUserListBinding binding;

    private EditText email_txt;
    private EditText password_txt;
    private AutoCompleteTextView userType_txt;
    private EditText name_txt;
    private EditText age_txt;
    private EditText height_txt;
    private EditText weight_txt;
    private AutoCompleteTextView bloodType_txt;
    private EditText phoneNumber_txt;
    private EditText addr1_txt;
    private EditText addr2_txt;
    private EditText postCode_txt;
    private EditText city_txt;
    private AutoCompleteTextView state_txt;
    private EditText country_txt;

    private Button submit_btn;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminAddUserListBinding.inflate(getLayoutInflater());
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

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        submit_btn.setOnClickListener(v -> submitBtn());
    }

    public void submitBtn(){
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

        User user = new User("", email,pwd, userType,name,age,height,weight,bloodType,phoneNumber,addr1,addr2,postCode,city,state,country);
        user.setState_bloodType(String.format("%s_%s", state, bloodType));

        // Add to Firebase
        dbRef = dbRef.push();

        String id = dbRef.getKey();
        user.setId(id);

        dbRef.setValue(user);

        Toast.makeText(this, String.format("User %s was successfully inserted!", name), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
