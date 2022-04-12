package my.edu.utar.blooddonationmadnew.ui;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
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
    private MaterialAutoCompleteTextView userType_txt;

    private EditText name_txt;
    private EditText age_txt;
    private EditText height_txt;
    private EditText weight_txt;
    private MaterialAutoCompleteTextView bloodType_txt;
    private EditText phoneNumber_txt;

    private EditText addr1_txt;
    private EditText addr2_txt;
    private EditText postCode_txt;
    private EditText city_txt;
    private MaterialAutoCompleteTextView state_txt;
    private EditText country_txt;

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

        name_txt = binding.nameTxt;
        age_txt = binding.ageTxt;
        height_txt = binding.heightTxt;
        weight_txt = binding.weightTxt;
        bloodType_txt = binding.bloodTypeTxt;
        phoneNumber_txt = binding.phoneNumberTxt;

        addr1_txt = binding.addr1Txt;
        addr2_txt = binding.addr2Txt;
        postCode_txt = binding.postCodeTxt;
        city_txt = binding.cityTxt;
        state_txt = binding.stateTxt;
        country_txt = binding.countryTxt;

        //Set Drop down menu debug
        String[] state_arr = getResources().getStringArray(R.array.state_arr);
        ArrayAdapter<String> stateAdapter= new ArrayAdapter<>(this, R.layout.dropdownmenu_listitem, state_arr);
        state_txt.setAdapter(stateAdapter);

        String[] blood_type_arr = getResources().getStringArray(R.array.blood_type_arr);
        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdownmenu_listitem, blood_type_arr);
        bloodType_txt.setAdapter(bloodTypeAdapter);

        String[] user_type_arr = getResources().getStringArray(R.array.user_type_arr);
        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdownmenu_listitem, user_type_arr);
        userType_txt.setAdapter(userTypeAdapter);

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

    }

    public void addUser(){
        String email = email_txt.getText().toString();
        String pwd = password_txt.getText().toString();
        String userType = userType_txt.getText().toString();

        String name = name_txt.getText().toString();

        String _age = age_txt.getText().toString();
        int age = (_age.isEmpty()) ? 0 : Integer.parseInt(_age);

        String _height = height_txt.getText().toString();
        int height = (_height.isEmpty()) ? 0 : (int) Math.round(Double.valueOf(_height));

        String _weight = weight_txt.getText().toString();
        int weight = (_weight.isEmpty()) ? 0 : (int) Math.round(Double.valueOf(_weight));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_save) {
            addUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
