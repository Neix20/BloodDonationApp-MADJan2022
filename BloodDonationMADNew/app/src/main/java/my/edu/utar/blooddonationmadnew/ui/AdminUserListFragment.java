package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
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

import my.edu.utar.blooddonationmadnew.adapter.AdminUserListAdapter;
import my.edu.utar.blooddonationmadnew.admin.data.User;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminUserListBinding;

public class AdminUserListFragment extends Fragment {

    private final String TAG = AdminUserListFragment.class.getSimpleName();

    private FragmentAdminUserListBinding binding;

    private FloatingActionButton fab;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    private RecyclerView mRecyclerView;
    private AdminUserListAdapter adminUserListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminUserListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.userListRecView;
        fab = binding.fab;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(dbRef, User.class).build();
        adminUserListAdapter = new AdminUserListAdapter(options);

        mRecyclerView.setAdapter(adminUserListAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        fab.setOnClickListener(view -> nAddUserList());


        return root;
    }

    public void nAddUserList(){
        Intent intent = new Intent(this.getContext(), AdminAddUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        adminUserListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adminUserListAdapter.stopListening();
    }
}
