package com.example.oneshot;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.oneshot.fragments.CategoryPage;
import com.example.oneshot.fragments.HomePage;
import com.example.oneshot.fragments.MyListPage;
import com.example.oneshot.fragments.SearchPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        Fragment selectedFragment = new HomePage();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                .commit();
    }
    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();

        Fragment selectedFragment = null;

        if(itemId == R.id.nav_home){
            selectedFragment = new HomePage();
        }else if(itemId == R.id.nav_my_list){
            selectedFragment = new MyListPage();
        }else if(itemId == R.id.nav_search){
            selectedFragment = new SearchPage();
        }else if(itemId == R.id.nav_category){
            selectedFragment = new CategoryPage();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                .commit();

        return true;
    };
}