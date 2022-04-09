package my.edu.utar.blooddonationmadnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import my.edu.utar.blooddonationmadnew.databinding.FragmentUserUserListBinding;

public class UserUserListFragment extends Fragment {
    public final static String TAG = UserUserListFragment.class.getSimpleName();

    private FragmentUserUserListBinding binding;

    private AutoCompleteTextView state_auto_complete;
    private AutoCompleteTextView blood_type_auto_complete;


    private Button submit_btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserUserListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Objects to XML Element
        state_auto_complete = binding.stateAutoComplete;
        blood_type_auto_complete = binding.bloodTypeAutoComplete;

        submit_btn = binding.submitBtn;

        // Choices
        // Should Import from String Values
        ArrayList<String> stateList = new ArrayList<>(Arrays.asList("Kuala Lumpur", "Selangor", "Johor", "Penang", "Kuantan"));
        ArrayList<String> bloodTypeList = new ArrayList<>(Arrays.asList("A", "B", "O", "AB"));

        ArrayAdapter<String> stateAdapter= new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, stateList);
        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, bloodTypeList);

        state_auto_complete.setAdapter(stateAdapter);
        blood_type_auto_complete.setAdapter(bloodTypeAdapter);

        submit_btn.setOnClickListener(v -> submitBtn());

        return root;
    }

    public void submitBtn(){
        String state = state_auto_complete.getText().toString().trim();
        String blood_type = blood_type_auto_complete.getText().toString().trim();

        Toast.makeText(this.getContext(), state + " " + blood_type, Toast.LENGTH_SHORT).show();

        // Fire base logic
        Intent intent = new Intent(this.getContext(), UserSearchResultActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("blood_type", blood_type);
        startActivity(intent);

    }
}
