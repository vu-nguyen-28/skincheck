package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PredictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        // Get the prediction result from the intent
        Intent intent = getIntent();
        String prediction = intent.getStringExtra("prediction");

        // Set the prediction result to TextView
        TextView textViewResult = findViewById(R.id.textViewResult);
        textViewResult.setText(prediction);
    }
}
