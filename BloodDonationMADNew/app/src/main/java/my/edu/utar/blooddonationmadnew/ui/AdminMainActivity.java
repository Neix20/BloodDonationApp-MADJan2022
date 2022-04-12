package my.edu.utar.blooddonationmadnew.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {

    public static final String TAG = AdminMainActivity.class.toString();

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminMainBinding binding;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind Java Object to XML Element
        bottomNavigationView = binding.bottomNavView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_dashboard, R.id.nav_admin_blood_event, R.id.nav_admin_map, R.id.nav_admin_user_list, R.id.nav_admin_profile
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_blood_donor, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_more) {
            Intent intent = new Intent(this, AdminAddBloodDonorRecordActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}