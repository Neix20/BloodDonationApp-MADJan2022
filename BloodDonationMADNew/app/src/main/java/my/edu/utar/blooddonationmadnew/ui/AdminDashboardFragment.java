package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.adapter.NotificationViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.BloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
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

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

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
