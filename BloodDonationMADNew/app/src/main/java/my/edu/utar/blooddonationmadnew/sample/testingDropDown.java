package my.edu.utar.blooddonationmadnew.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.databinding.TestingBinding;

public class testingDropDown extends AppCompatActivity {

    private TestingBinding binding;

    AutoCompleteTextView elem;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = TestingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        elem = binding.autoCompleteTextView2;

        ArrayList<String> items = new ArrayList<>(Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4"));

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        elem.setAdapter(adapter);


    }
}
