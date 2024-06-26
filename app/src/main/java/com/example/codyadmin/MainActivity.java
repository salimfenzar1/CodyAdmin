package com.example.codyadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.codyadmin.Model.Statement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 2;

    private EditText editTextDescription;
    private EditText editTextPictureName;

    private ProgressBar progressBar;


    private Spinner spinnerCategory, spinnerIntensity;
    private CheckBox checkBoxIsActive;
    private Button buttonAddStatement, buttonChooseImage;
    private FirebaseFirestore db;
    private Uri selectedImageUri;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiseer de ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Andere initialisaties
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextPictureName = findViewById(R.id.editTextPictureName);
        spinnerIntensity = findViewById(R.id.spinnerIntensity);
        buttonAddStatement = findViewById(R.id.buttonAddStatement);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        FloatingActionButton fabViewStatements = findViewById(R.id.fabViewStatements);
        db = FirebaseFirestore.getInstance();

        // Initialiseer de spinners
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> intensityAdapter = ArrayAdapter.createFromResource(this,
                R.array.intensities_array, android.R.layout.simple_spinner_item);
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIntensity.setAdapter(intensityAdapter);

        buttonChooseImage.setOnClickListener(v -> {
            Log.d("MainActivity", "Choose image button clicked");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                        Toast.makeText(this, "Toestemming is nodig om afbeeldingen te kiezen", Toast.LENGTH_LONG).show();
                    }
                    Log.d("MainActivity", "Requesting permission");
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                            REQUEST_CODE_PERMISSIONS);
                } else {
                    Log.d("MainActivity", "Permission already granted");
                    chooseImage();
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "Toestemming is nodig om afbeeldingen te kiezen", Toast.LENGTH_LONG).show();
                    }
                    Log.d("MainActivity", "Requesting permission");
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSIONS);
                } else {
                    Log.d("MainActivity", "Permission already granted");
                    chooseImage();
                }
            }
        });

        fabViewStatements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewStatementsActivity.class);
                startActivity(intent);
            }
        });

        buttonAddStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStatement(v);
            }
        });
    }

    private void chooseImage() {
        Log.d("MainActivity", "chooseImage called");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", "onRequestPermissionsResult called");
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Permission granted");
                chooseImage();
            } else {
                Log.d("MainActivity", "Permission denied");
                Toast.makeText(this, "Toestemming is nodig om afbeeldingen te kiezen", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "onActivityResult called");
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Log.d("MainActivity", "Image selected: " + selectedImageUri.toString());
        }
    }

    private void addStatement(View view) {
        buttonAddStatement.setEnabled(false); // Disable the button to prevent multiple clicks
        progressBar.setVisibility(View.VISIBLE); // Show the progress bar

        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String pictureName = editTextPictureName.getText().toString().trim(); // Haal de naam van de afbeelding op
        String intensity = spinnerIntensity.getSelectedItem().toString();
        boolean isActive = true;

        int intensityLevel = 1; // Default waarde
        switch (intensity) {
            case "Matig":
                intensityLevel = 2;
                break;
            case "Intens":
                intensityLevel = 3;
                break;
        }

        if (!description.isEmpty() && !category.isEmpty() && selectedImageUri != null) {
            uploadImageToFirebase(selectedImageUri, description, category, pictureName, isActive, intensityLevel, view);
        } else {
            Snackbar.make(view, "Vul alle velden in en kies een afbeelding", Snackbar.LENGTH_LONG).show();
            buttonAddStatement.setEnabled(true); // Enable the button again
            progressBar.setVisibility(View.GONE); // Hide the progress bar
        }
    }

    private void uploadImageToFirebase(Uri uri, String description, String category, String pictureName, boolean isActive, int intensityLevel, View view) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Comprimeer de bitmap naar een byte-array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            // Upload naar Firebase Storage
            String sanitizedPictureName = pictureName.replaceAll("[^a-zA-Z0-9]", "_");
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + sanitizedPictureName + ".jpg");
            UploadTask uploadTask = storageReference.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("MainActivity", "Image upload failed", exception);
                    Snackbar.make(view, "Afbeelding uploaden mislukt", Snackbar.LENGTH_LONG).show();
                    buttonAddStatement.setEnabled(true); // Enable the button again
                    progressBar.setVisibility(View.GONE); // Hide the progress bar
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Haal de URL van de geüploade afbeelding op
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            Statement statement = new Statement(description, category, imageUrl, isActive, pictureName, intensityLevel);

                            db.collection("statements")
                                    .add(statement)
                                    .addOnSuccessListener(documentReference -> {
                                        editTextDescription.setText("");
                                        editTextPictureName.setText(""); // Maak het invoerveld leeg
                                        Snackbar.make(view, "Statement toegevoegd", Snackbar.LENGTH_LONG).show();
                                        buttonAddStatement.setEnabled(true); // Enable the button again
                                        progressBar.setVisibility(View.GONE); // Hide the progress bar
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("MainActivity", "Error adding statement", e);
                                        buttonAddStatement.setEnabled(true); // Enable the button again
                                        progressBar.setVisibility(View.GONE); // Hide the progress bar
                                    });
                        }
                    });
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Snackbar.make(view, "Afbeelding niet gevonden", Snackbar.LENGTH_LONG).show();
            buttonAddStatement.setEnabled(true); // Enable the button again
            progressBar.setVisibility(View.GONE); // Hide the progress bar
        }
    }
}