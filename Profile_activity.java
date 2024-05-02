package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText editTextEmail = findViewById(R.id.editTextEmail); // Updated ID
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        TextView textViewProfileInfo = findViewById(R.id.textViewProfileInfo);
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password, textViewProfileInfo);
            } else {
                Toast.makeText(Profile_activity.this, "Email and password fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        buttonSignUp.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim(); // Make sure this is your email input
            String password = editTextPassword.getText().toString().trim(); // And this is your password input
            if (!email.isEmpty() && !password.isEmpty()) {
                registerUser(email, password);
            } else {
                Toast.makeText(this, "Email and password fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password, TextView textViewProfileInfo) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        textViewProfileInfo.setText("User logged in. Profile ID: " + userId);
                        textViewProfileInfo.setVisibility(View.VISIBLE);
                        goToHomeActivity();  // Navigate to home or user profile activity
                    } else {
                        Toast.makeText(Profile_activity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void registerUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return; // Exit the method if validations fail
        }

        // Continue with Firebase registration
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        String userId = mAuth.getCurrentUser().getUid();
                        goToAdditionalInfoActivity(userId, email); // Navigate or handle the success scenario
                    } else {
                        // Registration failed
                        Toast.makeText(Profile_activity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void goToHomeActivity() {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToAdditionalInfoActivity(String userId, String email) {
        Intent intent = new Intent(this, AdditionalInfoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("email", email);
        startActivity(intent);
        finish(); // Optional: call finish() if you do not want users to return to the previous activity
    }

    public static class User {
        public String email;

        public User(String email) {
            this.email = email;
        }
    }
}
