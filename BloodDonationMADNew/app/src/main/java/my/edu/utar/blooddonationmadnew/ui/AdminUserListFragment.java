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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.adapter.AdminBloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.AdminUserViewAdapter;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminUserListBinding;

public class AdminUserListFragment extends Fragment {

    private final String TAG = AdminUserListFragment.class.getSimpleName();

    private FragmentAdminUserListBinding binding;

    private FloatingActionButton fab;
    private SearchView searchBar;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    private RecyclerView mRecyclerView;
    private AdminUserViewAdapter adminUserViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminUserListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.userListRecView;
        fab = binding.fab;
        searchBar = binding.searchBar;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(dbRef.orderByChild("name"), User.class).build();
        adminUserViewAdapter = new AdminUserViewAdapter(options);

        mRecyclerView.setAdapter(adminUserViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        fab.setOnClickListener(view -> nAddUserList());

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Query firebaseSearchQuery = dbRef.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(firebaseSearchQuery, User.class).build();
                adminUserViewAdapter = new AdminUserViewAdapter(options);
                adminUserViewAdapter.startListening();
                mRecyclerView.setAdapter(adminUserViewAdapter);
                return false;
            }
        });


        return root;
    }

    public void nAddUserList(){
        Intent intent = new Intent(this.getContext(), AdminAddUserActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = item.getOrder();

        dbRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ind = 0;

                for(DataSnapshot UserSnapshot : snapshot.getChildren()){
                    if(ind == pos){
                        User user = UserSnapshot.getValue(User.class);
                        new AlertDialog.Builder(getContext())
                                .setMessage("Are you sure you want to delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, ik) -> {
                                    dbRef.child(user.getId()).removeValue();
                                    Toast.makeText(getContext(), String.format("Successfully Deleted User %s!", user.getName()), Toast.LENGTH_SHORT).show();
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
        adminUserViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adminUserViewAdapter.stopListening();
    }
}
