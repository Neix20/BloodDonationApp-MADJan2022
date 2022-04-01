package my.edu.utar.blooddonationmad.admin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import my.edu.utar.blooddonationmad.databinding.FragmentAdminDashboardBinding;

public class AdminDashboardFragment extends Fragment {

    public final static String TAG = AdminDashboardFragment.class.toString();

    private FragmentAdminDashboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

}
