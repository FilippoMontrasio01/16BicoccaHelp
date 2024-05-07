package com.example.bicoccahelp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.databinding.ActivityMainBinding;

import com.example.bicoccahelp.ui.home.HomeFragment;
import com.example.bicoccahelp.ui.lessonBooking.TutorFragment;
import com.example.bicoccahelp.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment = binding.navHostFragment.getFragment();
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = binding.bottomNavView;
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

}
