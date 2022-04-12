package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import my.edu.utar.blooddonationmadnew.adapter.AdminBloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.AdminNotificationViewAdapter;

import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.data.Notification;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminDashboardBinding;

public class AdminDashboardFragment extends Fragment {

    public final static String TAG = AdminDashboardFragment.class.getSimpleName();

    private FragmentAdminDashboardBinding binding;

    private FloatingActionButton fab;
    private SearchView searchBar;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "Notification";

    private RecyclerView mRecyclerView;
    private AdminNotificationViewAdapter adminNotificationViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.notificationRecView;
        fab = binding.fab;
        searchBar = binding.searchBar;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<Notification> options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(dbRef, Notification.class).build();
        adminNotificationViewAdapter = new AdminNotificationViewAdapter(options);

        mRecyclerView.setAdapter(adminNotificationViewAdapter);

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

        fab.setOnClickListener(v -> nAddNotification());

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Query firebaseSearchQuery = dbRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<Notification> options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(firebaseSearchQuery, Notification.class).build();
                adminNotificationViewAdapter = new AdminNotificationViewAdapter(options);
                adminNotificationViewAdapter.startListening();
                mRecyclerView.setAdapter(adminNotificationViewAdapter);
                return false;
            }
        });

        return root;
    }

    public void nAddNotification(){
        Intent intent = new Intent(this.getContext(), AdminAddNotificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        adminNotificationViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adminNotificationViewAdapter.stopListening();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = item.getOrder();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ind = 0;

                for(DataSnapshot notificationSnapshot : snapshot.getChildren()){
                    if(ind == pos){
                        Notification notification = notificationSnapshot.getValue(Notification.class);
                        new AlertDialog.Builder(getContext())
                                .setMessage("Are you sure you want to delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, ik) -> {
                                    dbRef.child(notification.getId()).removeValue();
                                    Toast.makeText(getContext(), String.format("Successfully Deleted Notification %s!", notification.getTitle()), Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("No", (dialog, ik) -> dialog.cancel())
                                .show();

                    }
                    ind++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "The read failed: " + error.getCode());
            }
        });

        return super.onContextItemSelected(item);
    }

}
