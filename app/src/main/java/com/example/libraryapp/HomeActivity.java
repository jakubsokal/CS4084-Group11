package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.example.libraryapp.db.DatabaseHelper;

public class HomeActivity extends AppCompatActivity implements INavbar {
    private BottomNavigationView bottomNav;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        
        String userEmail = getIntent().getStringExtra("email");
        dbHelper = new DatabaseHelper(this);
        userId = dbHelper.getUserIdByEmail(userEmail);
        
        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navbar_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navbar_book) {
                selectedFragment = new BookingFragment();
            } else if (item.getItemId() == R.id.navbar_manage) {
                selectedFragment = new ManageFragment();
            } else if (item.getItemId() == R.id.navbar_alert) {
                AlertsFragment alertsFragment = new AlertsFragment();
                Bundle args = new Bundle();
                args.putInt("userId", userId);
                alertsFragment.setArguments(args);
                selectedFragment = alertsFragment;
            }

            if(selectedFragment != null) {
                selectFragment(selectedFragment);
            }

            return true;
        });

        DrawerLayout drawerLayout = findViewById(R.id.navigation_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HomeActivity.this, drawerLayout, toolbar,
                R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });

        NavigationView burger = findViewById(R.id.nav_view);
        burger.setNavigationItemSelectedListener( click -> {
            Fragment selectedFragment = null;
            if (click.getItemId() == R.id.menu_home) {
                selectedFragment = new HomeFragment();
            } else if (click.getItemId() == R.id.menu_book) {
                selectedFragment = new BookingFragment();
            } else if (click.getItemId() == R.id.menu_manage) {
                selectedFragment = new ManageFragment();
            } else if (click.getItemId() == R.id.menu_alerts) {
                AlertsFragment alertsFragment = new AlertsFragment();
                Bundle args = new Bundle();
                args.putInt("userId", userId);
                alertsFragment.setArguments(args);
                selectedFragment = alertsFragment;
            } else if (click.getItemId() == R.id.menu_contact) {
                selectedFragment = new ContactFragment();
            } else {
                //goes back to main screen.
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            if(selectedFragment != null) {
                selectFragment(selectedFragment);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        selectFragment(new HomeFragment());
    }

    private void selectFragment(Fragment selectedFragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, selectedFragment)
                .commit();
    }

    @Override
    public void bottomNavItemSelected(int itemId) {
        bottomNav.setSelectedItemId(itemId);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
