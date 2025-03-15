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

public class HomeActivity extends AppCompatActivity implements INavbar {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navbar_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navbar_book) {
                selectedFragment = new BookingFragment();
            } else if (item.getItemId() == R.id.navbar_manage) {
                //add fragment here
            } else if (item.getItemId() == R.id.navbar_alert) {
                selectedFragment = new AlertsFragment();
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
                //add fragment here
            } else if (click.getItemId() == R.id.menu_alerts) {
                selectedFragment = new AlertsFragment();
            } else if (click.getItemId() == R.id.menu_account) {
                //add account fragment here
            } else if (click.getItemId() == R.id.menu_contact) {
                //add contact fragment here
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
}
