package my.edu.utar.blooddonationmadnew.admin.ui;

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

import my.edu.utar.blooddonationmadnew.admin.adapter.BloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.admin.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminBloodEventBinding;
import my.edu.utar.blooddonationmadnew.sample.User;
import my.edu.utar.blooddonationmadnew.sample.UserViewAdapter;

public class AdminBloodEventFragment extends Fragment {

    public final static String TAG = AdminBloodEventFragment.class.getSimpleName();

    private FragmentAdminBloodEventBinding binding;

    private FloatingActionButton fab;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    private RecyclerView mRecyclerView;
    private BloodEventViewAdapter bloodEventViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminBloodEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.bloodEventRecView;
        fab = binding.fab;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<BloodEvent> options = new FirebaseRecyclerOptions.Builder<BloodEvent>().setQuery(dbRef, BloodEvent.class).build();
        bloodEventViewAdapter = new BloodEventViewAdapter(options);

        mRecyclerView.setAdapter(bloodEventViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        fab.setOnClickListener(view -> nAddBloodEvent());

        return root;
    }

    public void nAddBloodEvent(){
        Intent intent = new Intent(this.getContext(), AdminAddBloodEventActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        bloodEventViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        bloodEventViewAdapter.stopListening();
    }

}
