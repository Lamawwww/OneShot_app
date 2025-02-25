package com.example.oneshot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.oneshot.LoginActivity;
import com.example.oneshot.MainActivity;
import com.example.oneshot.R;
import com.example.oneshot.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePage extends Fragment {
    private Button logoutBtn;
    private FirebaseAuth mAuth;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_profile_page, container, false);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getContext(), "Please login to access this page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        logoutBtn = view.findViewById(R.id.LogoutBtn);

        logoutBtn.setOnClickListener(view1 -> {
            mAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });




        return view;

    }
}