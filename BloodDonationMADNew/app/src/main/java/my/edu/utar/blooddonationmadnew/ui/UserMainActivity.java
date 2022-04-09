package my.edu.utar.blooddonationmadnew.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.databinding.ActivityUserMainBinding;

public class UserMainActivity extends AppCompatActivity {

    public final static String TAG = UserMainActivity.class.getSimpleName();

    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView bottomNavigationView;

    private ActivityUserMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Objects to XML element
        bottomNavigationView = binding.bottomNavView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_user_dashboard, R.id.nav_user_blood_event, R.id.nav_user_user_list, R.id.nav_user_profile
        ).build();

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
