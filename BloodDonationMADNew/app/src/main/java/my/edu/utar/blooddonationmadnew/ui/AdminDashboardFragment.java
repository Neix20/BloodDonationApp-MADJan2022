package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import my.edu.utar.blooddonationmadnew.adapter.NotificationViewAdapter;

import my.edu.utar.blooddonationmadnew.data.Notification;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminDashboardBinding;

public class AdminDashboardFragment extends Fragment {

    public final static String TAG = AdminDashboardFragment.class.getSimpleName();

    private FragmentAdminDashboardBinding binding;

    private FloatingActionButton fab;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "Notification";

    private RecyclerView mRecyclerView;
    private NotificationViewAdapter notificationViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.notificationRecView;
        fab = binding.fab;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<Notification> options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(dbRef, Notification.class).build();
        notificationViewAdapter = new NotificationViewAdapter(options);

        mRecyclerView.setAdapter(notificationViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Subscribe to Push notification
        FirebaseMessaging.getInstance().subscribeToTopic("admin").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.e(TAG, "Task has failed!");
                    return;
                }
                Log.i(TAG, "Successfully subscribed to Topic Admin!");
            }
        });

        fab.setOnClickListener(v -> nAddNotification());

        return root;
    }

    public void nAddNotification(){
        Intent intent = new Intent(this.getContext(), AdminAddNotificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        notificationViewAdapter.stopListening();
    }

}
