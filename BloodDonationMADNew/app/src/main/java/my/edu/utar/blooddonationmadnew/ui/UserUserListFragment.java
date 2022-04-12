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

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.databinding.FragmentUserUserListBinding;

public class UserUserListFragment extends Fragment {
    public final static String TAG = UserUserListFragment.class.getSimpleName();

    private FragmentUserUserListBinding binding;

    private AutoCompleteTextView state_txt;
    private AutoCompleteTextView bloodType_txt;


    private Button submit_btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserUserListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind Java Objects to XML Element
        state_txt = binding.stateAutoComplete;
        bloodType_txt = binding.bloodTypeAutoComplete;

        submit_btn = binding.submitBtn;

        // Choices
        // Should Import from String Values
        String[] state_arr = getResources().getStringArray(R.array.state_arr);
        ArrayAdapter<String> stateAdapter= new ArrayAdapter<>(this.getContext(), R.layout.dropdownmenu_listitem, state_arr);
        state_txt.setAdapter(stateAdapter);

        String[] blood_type_arr = getResources().getStringArray(R.array.blood_type_arr);
        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this.getContext(), R.layout.dropdownmenu_listitem, blood_type_arr);
        bloodType_txt.setAdapter(bloodTypeAdapter);

        submit_btn.setOnClickListener(v -> submitBtn());

        return root;
    }

    public void submitBtn(){
        String state = state_txt.getText().toString().trim();
        String blood_type = bloodType_txt.getText().toString().trim();

        // Fire base logic
        Intent intent = new Intent(this.getContext(), UserSearchResultActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("blood_type", blood_type);
        startActivity(intent);

    }
}
