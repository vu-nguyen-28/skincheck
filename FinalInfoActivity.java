package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FinalInfoActivity extends AppCompatActivity {

    private EditText editTextDOB, editTextGender;
    private Button buttonCompleteSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_info);

        editTextDOB = findViewById(R.id.editTextDOB);
        editTextGender = findViewById(R.id.editTextGender);
        buttonCompleteSignup = findViewById(R.id.buttonCompleteSignup);

        // Get the full name passed from the previous activity
        String fullName = getIntent().getStringExtra("fullName");

        buttonCompleteSignup.setOnClickListener(v -> {
            String dob = editTextDOB.getText().toString().trim();
            String gender = editTextGender.getText().toString().trim();

            if (!dob.isEmpty() && !gender.isEmpty()) {
                Intent intent = new Intent(this, UserHomeActivity.class);
                intent.putExtra("fullName", fullName);
                intent.putExtra("dob", dob);
                intent.putExtra("gender", gender);
                startActivity(intent);
                finish();
            } else {
                if (dob.isEmpty()) editTextDOB.setError("DOB is required!");
                if (gender.isEmpty()) editTextGender.setError("Gender is required!");
            }
        });
    }
}
