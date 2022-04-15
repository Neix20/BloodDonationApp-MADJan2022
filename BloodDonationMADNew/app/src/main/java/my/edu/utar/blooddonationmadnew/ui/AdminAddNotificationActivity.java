package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.data.Notification;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminAddNotificationBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminAddNotificationActivity extends AppCompatActivity {
    public final String TAG = AdminAddNotificationActivity.class.getSimpleName();

    private EditText title_txt;
    private EditText body_txt;

    private ActivityAdminAddNotificationBinding binding;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "Notification";

    private HttpTask httpTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminAddNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Object to XML element
        title_txt = binding.titleTxt;
        body_txt = binding.bodyTxt;

        httpTask = new HttpTask();

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addNotification(){
        String title = title_txt.getText().toString();
        String body = body_txt.getText().toString();

        // Add New Notification

        String id = dbRef.push().getKey();
        Notification notification = new Notification(id, title, body);

        dbRef.child(id).setValue(notification);

        // Make Http Post Call Here
        try {
            httpTask.sendNotification("/topics/notification", title, body);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            addNotification();
        }
        return super.onOptionsItemSelected(item);
    }

    private class HttpTask {
        private final OkHttpClient httpClient;

        public HttpTask() {
            httpClient = new OkHttpClient();
        }

        public void sendNotification(String topic, String title, String body) throws Exception {

            // form parameters
            String json = String.format("{\"to\":\"%s\",\"notification\":{\"title\":\"%s\",\"body\":\"%s\"}}", topic, title, body);

            Log.i(TAG, json);

            RequestBody formBody = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .addHeader("Authorization", "key=AAAAzpUTlAY:APA91bElinNPX2qpwOh7Afo7gj4nfIbp3kHbZVOTDpsExDH0SPC240K2M9cHaliZ1Ggt1psfUKXsdpgHMqUGimB8BBwyqg3aUF6N-uY7vNTJt4dew7o8yHfM9uMJEbrzhpSxy4Bv3iUX")
                    .addHeader("Content-Type", "application/json")
                    .post(formBody)
                    .build();

            Call call = httpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, e.toString().trim());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    // Get response body
                    Log.i(TAG, "Successfully publish notification to Topic Admin!");
                }
            });
        }
    }
}
