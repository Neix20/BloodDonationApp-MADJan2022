package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminAddBloodEventBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminAddBloodEventActivity extends AppCompatActivity {

    public final String TAG = AdminAddBloodEventActivity.class.getSimpleName();

    private ActivityAdminAddBloodEventBinding binding;

    private TextInputEditText title_txt;
    private TextInputEditText description_txt;
    private TextInputEditText phone_txt;

    private TextInputEditText address_line_1_txt;
    private TextInputEditText address_line_2_txt;
    private TextInputEditText post_code_txt;
    private TextInputEditText city_txt;
    private MaterialAutoCompleteTextView state_txt;
    private TextInputEditText country_txt;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    private HttpTask httpTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminAddBloodEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML Element
        title_txt = binding.titleTxt;
        description_txt = binding.descriptionTxt;
        phone_txt = binding.phoneNumberTxt;

        address_line_1_txt = binding.addressLine1Txt;
        address_line_2_txt = binding.addressLine2Txt;
        post_code_txt = binding.postCodeTxt;
        city_txt = binding.cityTxt;
        state_txt = binding.stateTxt;
        country_txt = binding.countryTxt;

        // Create Adapter for State Text
        String[] state_arr = getResources().getStringArray(R.array.state_arr);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.dropdownmenu_listitem, state_arr);
        state_txt.setAdapter(stateAdapter);

        // Add Back Button at ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Java Objects
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        httpTask = new HttpTask();
    }

    public void addBloodEvent() {
        String title = title_txt.getText().toString().trim();
        String description = description_txt.getText().toString().trim();
        String phoneNumber = phone_txt.getText().toString().trim();

        String address_line_1 = address_line_1_txt.getText().toString().trim();
        String address_line_2 = address_line_2_txt.getText().toString().trim();
        String post_code = post_code_txt.getText().toString().trim();
        String city = city_txt.getText().toString().trim();
        String state = state_txt.getText().toString().trim();
        String country = country_txt.getText().toString().trim();

        // Use GeoCoding API to get latitude and longitude from Address;
        BloodEvent bloodEvent = new BloodEvent();
        bloodEvent.setTitle(title);
        bloodEvent.setDescription(description);
        bloodEvent.setPhoneNumber(phoneNumber);

        bloodEvent.setAddress_line_1(address_line_1);
        bloodEvent.setAddress_line_2(address_line_2);
        bloodEvent.setPostCode(post_code);
        bloodEvent.setCity(city);
        bloodEvent.setState(state);
        bloodEvent.setCountry(country);

        String[] address_arr = {address_line_1, address_line_2, post_code, city, state, country};
        String address = TextUtils.join(", ", address_arr);

        address = address.replaceAll(" ", "%20");

        String api_key = getResources().getString(R.string.API_KEY);

        httpTask.getLngAndLat("json", address, api_key, bloodEvent);

        Toast.makeText(this, String.format("Blood Event %s was successfully inserted!", title), Toast.LENGTH_SHORT).show();

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
            addBloodEvent();
        }
        return super.onOptionsItemSelected(item);
    }


    private class HttpTask {
        private final OkHttpClient httpClient;

        public HttpTask() {
            httpClient = new OkHttpClient();
        }

        public void getLngAndLat(String output, String address, String API_KEY, BloodEvent bloodEvent) {

            String url = String.format("https://maps.googleapis.com/maps/api/geocode/%s?address=%s&key=%s", output, address, API_KEY);

            Log.i(TAG, url);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("custom-key", "mkyong")  // add request headers
                    .addHeader("User-Agent", "OkHttp Bot")
                    .build();

            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, IOException e) {
                    Log.e(TAG, e.toString().trim());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    String jsonData = response.body().string();
                    Log.i(TAG, jsonData);

                    if (response.isSuccessful()) {
                        JSONObject jo;
                        JSONArray ja;

                        // Get response body
                        try {
                            jo = new JSONObject(jsonData);
                            ja = (JSONArray) jo.get("results");
                            jo = (JSONObject) ja.get(0);

                            jo = (JSONObject) jo.get("geometry");
                            jo = (JSONObject) jo.get("location");

                            double longitude = (Double) jo.get("lng");
                            double latitude = (Double) jo.get("lat");

                            bloodEvent.setLongitude(longitude);
                            bloodEvent.setLatitude(latitude);

                            // Add to Firebase
                            dbRef = dbRef.push();

                            String id = dbRef.getKey();
                            bloodEvent.setId(id);

                            dbRef.setValue(bloodEvent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        public void sendPost() throws Exception {

            // form parameters
            RequestBody formBody = new FormBody.Builder()
                    .add("username", "abc")
                    .add("password", "123")
                    .add("custom", "secret")
                    .build();

            Request request = new Request.Builder()
                    .url("https://httpbin.org/post")
                    .addHeader("User-Agent", "OkHttp Bot")
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
                    System.out.println(response.body().string());
                }
            });
        }
    }


}
