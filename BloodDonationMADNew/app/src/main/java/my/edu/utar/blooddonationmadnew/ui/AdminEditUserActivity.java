package my.edu.utar.blooddonationmadnew.ui;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private String gTitle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminEditUserListBinding.inflate(getLayoutInflater());
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
                gTitle = tmpUser.getName();

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
    }

    public void updateUser() {
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

        User tmpUser = new User(id, email,pwd, userType,name,age,height,weight,bloodType,phoneNumber,addr1,addr2,postCode,city,state,country);
        tmpUser.setState_bloodType(String.format("%s_%s", state, bloodType));

        dbRef.child(id).setValue(tmpUser);

        Toast.makeText(this, String.format("User %s was successfully updated!", email), Toast.LENGTH_SHORT).show();

        finish();
    }

    public void deleteUser(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, ind) -> {
                    dbRef.child(id).removeValue();
                    Toast.makeText(this, String.format("Successfully Deleted User %s!", gTitle), Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", (dialog, ind) -> dialog.cancel())
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_save) {
            updateUser();
        } else if(id == R.id.option_delete) {
            deleteUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
