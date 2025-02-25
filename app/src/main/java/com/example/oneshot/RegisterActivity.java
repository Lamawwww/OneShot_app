package com.example.oneshot;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailET, usernameET, passwordET, cPasswordET;
    private Button registerBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.EmailET);
        usernameET = findViewById(R.id.UsernameET);
        passwordET = findViewById(R.id.PasswordET);
        cPasswordET = findViewById(R.id.CPasswordET);
        registerBtn = findViewById(R.id.RegisterBtn);

        registerBtn.setOnClickListener(v -> registerUser());

        TextView signInTxt = findViewById(R.id.SignInTxt);
        String text = "Already Have An Account? Sign In";
        SpannableString spannable = new SpannableString(text);

        int start = text.indexOf("Sign In");
        int end = start + "Sign In".length();

// Make "Sign In" bold and blue
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle the "Sign In" click (e.g., navigate to SignInActivity)
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#6475F7")); // Ensures color stays #6475F7
                ds.setTypeface(Typeface.DEFAULT_BOLD); // Keeps it bold
                ds.setUnderlineText(false); // Removes underline
            }
        };

        spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        signInTxt.setText(spannable);
        signInTxt.setMovementMethod(LinkMovementMethod.getInstance()); // Make the span clickable
    }

    private void registerUser() {
        String email = emailET.getText().toString().trim();
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String cPassword = cPasswordET.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            emailET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            usernameET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            passwordET.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
            passwordET.requestFocus();
            return;
        }

        if (!password.equals(cPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            cPasswordET.requestFocus();
            return;
        }

        checkUsernameAvailability(username, email, password);
    }

    private void checkUsernameAvailability(String username, String email, String password) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username is already taken
                    Toast.makeText(RegisterActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                    usernameET.requestFocus();
                } else {
                    // Username is available, proceed with registration
                    createUser(email, username, password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(String email, String username, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();

                        // Create a HashMap to store user data
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("uid", uid);
                        hashMap.put("email", email);
                        hashMap.put("username", username);
                        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

                        // Get database reference
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        ref.child(uid).setValue(hashMap).addOnSuccessListener(aVoid -> {
                            // Data successfully written to the database
                            Toast.makeText(RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                            // Redirect the user to LoginActivity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Close RegisterActivity
                        }).addOnFailureListener(e -> {
                            // Failed to write data to the database
                            Toast.makeText(RegisterActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}