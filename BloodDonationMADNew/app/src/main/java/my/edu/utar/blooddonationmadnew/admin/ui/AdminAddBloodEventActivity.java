package my.edu.utar.blooddonationmadnew.admin.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.admin.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminAddBloodEventBinding;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminMainBinding;

public class AdminAddBloodEventActivity extends AppCompatActivity {

    public final String TAG = AdminAddBloodEventActivity.class.getSimpleName();

    private ActivityAdminAddBloodEventBinding binding;

    private EditText title_txt;
    private EditText address_txt;

    private Button submit_btn;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminAddBloodEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        title_txt = binding.titleTxt;
        address_txt = binding.addressTxt;

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
        String title = title_txt.getText().toString();
        String address = address_txt.getText().toString();

        BloodEvent bloodEvent = new BloodEvent("", title, address);

        // Add to Firebase
        dbRef = dbRef.push();

        String id = dbRef.getKey();
        bloodEvent.setId(id);

        dbRef.setValue(bloodEvent);

        Toast.makeText(this, String.format("Blood Event %s was successfully inserted!", title), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
