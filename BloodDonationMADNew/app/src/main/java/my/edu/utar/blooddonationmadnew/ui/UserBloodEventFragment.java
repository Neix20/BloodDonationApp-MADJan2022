package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import my.edu.utar.blooddonationmadnew.adapter.AdminBloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.adapter.UserBloodEventViewAdapter;
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.databinding.FragmentUserBloodEventBinding;

public class UserBloodEventFragment extends Fragment {

    public final static String TAG = UserBloodEventFragment.class.getSimpleName();

    private FragmentUserBloodEventBinding binding;

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    private RecyclerView mRecyclerView;
    private UserBloodEventViewAdapter userBloodEventViewAdapter;

    private SearchView searchBar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserBloodEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Object to XML Element
        mRecyclerView = binding.bloodEventRecView;
        searchBar = binding.searchBar;

        // Initialize Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);

        FirebaseRecyclerOptions<BloodEvent> options = new FirebaseRecyclerOptions.Builder<BloodEvent>().setQuery(dbRef, BloodEvent.class).build();
        userBloodEventViewAdapter = new UserBloodEventViewAdapter(options);

        mRecyclerView.setAdapter(userBloodEventViewAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                Query firebaseSearchQuery = dbRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<BloodEvent> options = new FirebaseRecyclerOptions.Builder<BloodEvent>().setQuery(firebaseSearchQuery, BloodEvent.class).build();
                userBloodEventViewAdapter = new UserBloodEventViewAdapter(options);
                userBloodEventViewAdapter.startListening();
                mRecyclerView.setAdapter(userBloodEventViewAdapter);
                return false;
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        userBloodEventViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userBloodEventViewAdapter.stopListening();
    }
}
