package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {

    private TextView textViewUserInfo;
    private Button buttonLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        mAuth = FirebaseAuth.getInstance();

        textViewUserInfo = findViewById(R.id.textViewUserInfo);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Check if the user is logged in and update UI accordingly
        if (mAuth.getCurrentUser() != null) {
            textViewUserInfo.setText("Welcome, " + mAuth.getCurrentUser().getEmail());
        } else {
            textViewUserInfo.setText("No user logged in.");
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserHomeActivity.this, Profile_activity.class));
                finish();
            }
        });
    }
}
