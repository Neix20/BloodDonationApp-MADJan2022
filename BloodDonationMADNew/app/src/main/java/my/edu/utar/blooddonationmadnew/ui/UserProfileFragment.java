package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.FragmentUserProfileBinding;

public class UserProfileFragment extends Fragment {
    public final static String TAG = UserProfileFragment.class.getSimpleName();

    private FragmentUserProfileBinding binding;

    private TextInputEditText email_txt;
    private TextInputEditText password_txt;

    private TextInputEditText name_txt;
    private TextInputEditText age_txt;
    private TextInputEditText height_txt;
    private TextInputEditText weight_txt;
    private TextInputEditText bmi_txt;
    private MaterialAutoCompleteTextView bloodType_txt;
    private TextInputEditText phoneNumber_txt;

    private TextInputEditText addr1_txt;
    private TextInputEditText addr2_txt;
    private TextInputEditText postCode_txt;
    private TextInputEditText city_txt;
    private MaterialAutoCompleteTextView state_txt;
    private TextInputEditText country_txt;

    private ImageView save_btn;
    private ImageView logout_btn;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    private FirebaseUser cur_user;
    private String id;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Objects to XML Element
        email_txt = binding.emailTxt;
        password_txt = binding.pwdTxt;

        name_txt = binding.nameTxt;
        age_txt = binding.ageTxt;
        height_txt = binding.heightTxt;
        weight_txt = binding.weightTxt;
        bmi_txt = binding.bmiTxt;
        bloodType_txt = binding.bloodTypeTxt;
        phoneNumber_txt = binding.phoneNumberTxt;

        addr1_txt = binding.addr1Txt;
        addr2_txt = binding.addr2Txt;
        postCode_txt = binding.postCodeTxt;
        city_txt = binding.cityTxt;
        state_txt = binding.stateTxt;
        country_txt = binding.countryTxt;

        save_btn = binding.saveBtn;
        logout_btn = binding.logoutBtn;

        //Set Drop down menu debug
        String[] state_arr = getResources().getStringArray(R.array.state_arr);
        ArrayAdapter<String> stateAdapter= new ArrayAdapter<>(this.getContext(), R.layout.dropdownmenu_listitem, state_arr);
        state_txt.setAdapter(stateAdapter);

        String[] blood_type_arr = getResources().getStringArray(R.array.blood_type_arr);
        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this.getContext(), R.layout.dropdownmenu_listitem, blood_type_arr);
        bloodType_txt.setAdapter(bloodTypeAdapter);

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        cur_user = FirebaseAuth.getInstance().getCurrentUser();
        id = cur_user.getUid();

        // Populate Text
        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User tmpUser = snapshot.getValue(User.class);

                email_txt.setText(tmpUser.getEmail());
                password_txt.setText(tmpUser.getPassword());

                name_txt.setText(tmpUser.getName());

                int _age = tmpUser.getAge();
                String age = (_age==0) ? "0" : String.valueOf(_age);
                age_txt.setText(age);

                int _height = tmpUser.getHeight();
                String height = (_height==0) ? "0" : String.valueOf(_height);
                height_txt.setText(height);

                int _weight =  tmpUser.getWeight();
                String weight = (_weight==0) ? "0" : String.valueOf(_weight);
                weight_txt.setText(weight);

                double BMI = _weight / (_height * _height * 1.0);
                bmi_txt.setText(BMI + "");

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

        save_btn.setOnClickListener(v -> updateUser());
        logout_btn.setOnClickListener(v -> logoutUser());


        return root;
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
    }

    public void updateUser() {
        String email = email_txt.getText().toString();
        String pwd = password_txt.getText().toString();
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

        User tmpUser = new User(id, email,pwd, "User",name,age,height,weight,bloodType,phoneNumber,addr1,addr2,postCode,city,state,country);
        tmpUser.setState_bloodType(String.format("%s_%s", state, bloodType));

        dbRef.child(id).setValue(tmpUser);

        Toast.makeText(this.getContext(), String.format("User %s was successfully updated!", email), Toast.LENGTH_SHORT).show();
    }
}
