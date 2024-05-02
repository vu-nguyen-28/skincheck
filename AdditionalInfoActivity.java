package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AdditionalInfoActivity extends AppCompatActivity {
    private EditText editTextFullName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        userId = getIntent().getStringExtra("userId");
        editTextFullName = findViewById(R.id.editTextFullName);
        Button buttonNext = findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(v -> {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
            String fullName = editTextFullName.getText().toString().trim();
            if (!fullName.isEmpty()) {
                saveAdditionalInfoAndContinue(fullName);
            } else {
                Toast.makeText(this, "Full name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAdditionalInfoAndContinue(String fullName) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userId).child("fullName").setValue(fullName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        goToFinalInfoActivity(); // Correct navigation to FinalInfoActivity
                    } else {
                        Toast.makeText(this, "Failed to save additional info: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToFinalInfoActivity() {
        Intent intent = new Intent(this, FinalInfoActivity.class);
        intent.putExtra("userId", userId); // Ensure the userId is passed correctly
        startActivity(intent);
        finish();
    }
}
