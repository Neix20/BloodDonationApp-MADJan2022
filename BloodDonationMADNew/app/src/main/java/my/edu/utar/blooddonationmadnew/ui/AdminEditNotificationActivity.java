package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.data.Notification;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminEditNotificationBinding;

public class AdminEditNotificationActivity extends AppCompatActivity {
    public final String TAG = AdminEditNotificationActivity.class.getSimpleName();

    private EditText title_txt;
    private EditText body_txt;

    private Button submit_btn;

    private String noti_id;

    private ActivityAdminEditNotificationBinding binding;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "Notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminEditNotificationBinding.inflate(getLayoutInflater());
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

        // get notification id passed
        Intent mIntent = getIntent();
        noti_id = mIntent.getStringExtra("noti_id");

        dbRef.child(noti_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Notification notification = snapshot.getValue(Notification.class);

                title_txt.setText(notification.getTitle());
                body_txt.setText(notification.getBody());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

        submit_btn.setOnClickListener(v -> submitBtn());
    }

    public void submitBtn(){
        String title = title_txt.getText().toString();
        String body = body_txt.getText().toString();

        Notification newNoti = new Notification("", title, body);

        // Save Notification
        dbRef.child(noti_id).setValue(newNoti);

        Toast.makeText(this, String.format("Notification was successfully updated!"), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
