package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import my.edu.utar.blooddonationmadnew.adapter.AdminNotificationViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.UserNotificationViewAdapter;
import my.edu.utar.blooddonationmadnew.data.Notification;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.FragmentUserDashboardBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserDashboardFragment extends Fragment {

    public final static String TAG = UserDashboardFragment.class.getSimpleName();

    private FirebaseUser cur_user;
    private String userID;

    private FragmentUserDashboardBinding binding;

    private FloatingActionButton fab;
    private SearchView searchBar;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "Notification";

    private RecyclerView mRecyclerView;
    private UserNotificationViewAdapter userNotificationViewAdapter;

    private HttpTask httpTask;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.notificationRecView;
        fab = binding.fab;
        searchBar = binding.searchBar;

        httpTask = new HttpTask();

        cur_user = FirebaseAuth.getInstance().getCurrentUser();
        userID = cur_user.getUid();

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<Notification> options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(dbRef, Notification.class).build();
        userNotificationViewAdapter = new UserNotificationViewAdapter(options);

        mRecyclerView.setAdapter(userNotificationViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Subscribe to Push notification
        FirebaseMessaging.getInstance().subscribeToTopic("notification").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.e(TAG, "Task has failed!");
                    return;
                }

                Log.i(TAG, "Successfully subscribed to Topic Notification!");
            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Query firebaseSearchQuery = dbRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<Notification> options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(firebaseSearchQuery, Notification.class).build();
                userNotificationViewAdapter = new UserNotificationViewAdapter(options);
                userNotificationViewAdapter.startListening();
                mRecyclerView.setAdapter(userNotificationViewAdapter);
                return false;
            }
        });

        fab.setOnClickListener(v -> addBloodRequest());

        return root;
    }

    public void addBloodRequest(){
        // Get Current User
        FirebaseDatabase.getInstance().getReference("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                String req_msg = String.format("User: %s\nAddress: %s\nPhone Number: %s\nBlood Type: %s\n", user.getName(), user.getAddress(), user.getPhoneNumber(), user.getBloodType());

                new AlertDialog.Builder(getContext())
                        .setMessage(String.format("%s\nAre you sure you want to make Blood Request?", req_msg))
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, ik) -> {
                            try {
                                dbRef = dbRef.push();
                                String id = dbRef.getKey();
                                Notification notification = new Notification(id,"New Blood Request", req_msg);
                                dbRef.setValue(notification);

                                httpTask.sendNotification("/topics/notification", "New Blood Request", req_msg);
                                Toast.makeText(getContext(), String.format("Successfully Created Blood Request for User %s!", user.getName()), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
                        .setNegativeButton("No", (dialog, ik) -> dialog.cancel())
                        .show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        userNotificationViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userNotificationViewAdapter.stopListening();
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
