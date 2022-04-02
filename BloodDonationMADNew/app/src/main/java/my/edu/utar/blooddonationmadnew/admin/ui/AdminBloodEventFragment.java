package my.edu.utar.blooddonationmadnew.admin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminBloodEventBinding;
import my.edu.utar.blooddonationmadnew.databinding.FragmentAdminDashboardBinding;

public class AdminBloodEventFragment extends Fragment {

    public final static String TAG = AdminBloodEventFragment.class.toString();

    private FragmentAdminBloodEventBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminBloodEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

}
