package com.example.projectuddd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    //private ActionBar toolbar;
    int selectedMenuItem;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        addControl();
        navigationControl();

    }

    private void addControl() {
        bottomNav = findViewById(R.id.bottom_navigation);
        //toolbar = getSupportActionBar();
        //toolbar.setTitle("Home");
        loadFragment(new HomeFragment());

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void navigationControl () {           //sự kiện click bottom navigation
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectFragment(menuItem);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;

            }
        });

    }
    private void selectFragment(MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home :
                //toolbar.setTitle("Home");
                selectedFragment = new HomeFragment();
                loadFragment(selectedFragment);
                break;
            case R.id.nav_find :
                //toolbar.setTitle("Find");
                selectedFragment= new FindFragment();
                loadFragment(selectedFragment);
                break;
            case R.id.nav_bookmark :
               // toolbar.setTitle("Bookmark");
                selectedFragment = new BookmarkFragment();
                loadFragment(selectedFragment);
                break;
            case R.id.nav_user :
               // toolbar.setTitle("Your Account");
                selectedFragment= new UserFragment();
                loadFragment(selectedFragment);
                break;
        }
        selectedMenuItem= menuItem.getItemId();
    }

    @Override
    public void onBackPressed() {
        MenuItem item=bottomNav.getMenu().getItem(0);
        if(selectedMenuItem!=item.getItemId()) {
            selectFragment(item);
            bottomNav.setSelectedItemId(item.getItemId());
        }
        if(selectedMenuItem==0)
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null) {
            finish();
            startActivity(new Intent(MainActivity.this,SignInActivity.class));
        }
    }
}
