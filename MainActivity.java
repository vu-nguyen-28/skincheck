package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int IMAGE_PICKER_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private TFLiteClassifier tfliteClassifier;  // Your TFLite classifier
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Button uploadImageButton = findViewById(R.id.buttonUploadImage);
        Button scanNowButton = findViewById(R.id.buttonScanNow);
        Button goToProfileButton = findViewById(R.id.goToProfileButton); // Button to navigate to Profile

        uploadImageButton.setOnClickListener(v -> openImagePicker());
        scanNowButton.setOnClickListener(v -> startScanning());
        goToProfileButton.setOnClickListener(v -> goToProfile()); // Set up the listener to navigate

        tfliteClassifier = new TFLiteClassifier(this);  // Initialize your classifier
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICKER_REQUEST);
    }

    private void startScanning() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }


    private void goToProfile() {
        Intent intent = new Intent(this, Profile_activity.class);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is necessary to take pictures", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Handle the captured image
            classifyImage(imageBitmap);
        }
    }

    private void classifyImage(Bitmap bitmap) {
        if (tfliteClassifier != null) {
            String prediction = tfliteClassifier.classify(bitmap);
            navigateToPredictionActivity(prediction);
        } else {
            Toast.makeText(this, "Model is not loaded properly!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToPredictionActivity(String prediction) {
        Intent intent = new Intent(this, PredictionActivity.class);
        intent.putExtra("prediction", prediction);
        startActivity(intent);
    }
}
