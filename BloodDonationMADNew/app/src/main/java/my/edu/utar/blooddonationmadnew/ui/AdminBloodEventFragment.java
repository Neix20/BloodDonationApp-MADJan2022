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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.adapter.AdminBloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminBloodEventBinding;

public class AdminBloodEventFragment extends Fragment {

    public final static String TAG = AdminBloodEventFragment.class.getSimpleName();

    private FragmentAdminBloodEventBinding binding;

    private FloatingActionButton fab;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    private RecyclerView mRecyclerView;
    private AdminBloodEventViewAdapter adminBloodEventViewAdapter;

    private SearchView searchBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminBloodEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.bloodEventRecView;
        fab = binding.fab;

        searchBar = binding.searchBar;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<BloodEvent> options = new FirebaseRecyclerOptions.Builder<BloodEvent>().setQuery(dbRef.orderByChild("title"), BloodEvent.class).build();
        adminBloodEventViewAdapter = new AdminBloodEventViewAdapter(options);

        mRecyclerView.setAdapter(adminBloodEventViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        fab.setOnClickListener(view -> nAddBloodEvent());

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Query firebaseSearchQuery = dbRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<BloodEvent> options = new FirebaseRecyclerOptions.Builder<BloodEvent>().setQuery(firebaseSearchQuery, BloodEvent.class).build();
                adminBloodEventViewAdapter = new AdminBloodEventViewAdapter(options);
                adminBloodEventViewAdapter.startListening();
                mRecyclerView.setAdapter(adminBloodEventViewAdapter);
                return false;
            }
        });


        return root;
    }

    public void nAddBloodEvent(){
        Intent intent = new Intent(this.getContext(), AdminAddBloodEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = item.getOrder();

        dbRef.orderByChild("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ind = 0;

                for(DataSnapshot bloodEventSnapshot : snapshot.getChildren()){
                    if(ind == pos){
                        BloodEvent bloodEvent = bloodEventSnapshot.getValue(BloodEvent.class);
                        new AlertDialog.Builder(getContext())
                                .setMessage("Are you sure you want to delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, ik) -> {
                                    dbRef.child(bloodEvent.getId()).removeValue();
                                    Toast.makeText(getContext(), String.format("Successfully Deleted Blood Event %s!", bloodEvent.getTitle()), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart() {
        super.onStart();
        adminBloodEventViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adminBloodEventViewAdapter.stopListening();
    }

}
