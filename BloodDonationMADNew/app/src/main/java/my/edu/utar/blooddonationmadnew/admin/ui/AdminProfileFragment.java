package my.edu.utar.blooddonationmadnew.admin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminProfileBinding;

public class AdminProfileFragment extends Fragment {

    public final static String TAG = AdminProfileFragment.class.toString();

    private FragmentAdminProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

}
