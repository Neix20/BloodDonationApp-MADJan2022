package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.data.Notification;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminAddNotificationBinding;

public class AdminAddNotificationActivity extends AppCompatActivity {
    public final String TAG = AdminAddNotificationActivity.class.getSimpleName();

    private EditText title_txt;
    private EditText body_txt;

    private Button submit_btn;

    private ActivityAdminAddNotificationBinding binding;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "Notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminAddNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Object to XML element
        title_txt = binding.titleTxt;
        body_txt = binding.bodyTxt;

        submit_btn = binding.submitBtn;

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        submit_btn.setOnClickListener(v -> submitBtn());
    }

    public void submitBtn(){
        String title = title_txt.getText().toString();
        String body = body_txt.getText().toString();

        // Add New Notification
        dbRef = dbRef.push();

        String id = dbRef.getKey();
        Notification notification = new Notification(id, title, body);

        dbRef.setValue(notification);

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
