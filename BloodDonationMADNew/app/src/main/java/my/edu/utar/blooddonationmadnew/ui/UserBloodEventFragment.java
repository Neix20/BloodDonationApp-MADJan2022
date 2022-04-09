package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import my.edu.utar.blooddonationmadnew.databinding.FragmentUserBloodEventBinding;

public class UserBloodEventFragment extends Fragment {

    public final static String TAG = UserBloodEventFragment.class.getSimpleName();

    private FragmentUserBloodEventBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserBloodEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}
