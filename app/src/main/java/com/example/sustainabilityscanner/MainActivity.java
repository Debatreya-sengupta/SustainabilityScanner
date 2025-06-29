package com.example.sustainabilityscanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.sustainabilityscanner.api.ApiService;
import com.example.sustainabilityscanner.api.RetrofitClient;
import com.example.sustainabilityscanner.model.ProductResponse;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.example.sustainabilityscanner.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private DecoratedBarcodeView barcodeScanner;
    private TextView productNameTextView, sustainabilityRatingTextView;
    private ProgressBar sustainabilityScoreBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        barcodeScanner = findViewById(R.id.barcode_scanner);
        productNameTextView = findViewById(R.id.product_name);
        sustainabilityRatingTextView = findViewById(R.id.sustainability_rating);
        sustainabilityScoreBar = findViewById(R.id.sustainability_score);

        // Check for camera permission
        checkCameraPermission();
    }

    // Check & request camera permission
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startBarcodeScanner(); // Permission already granted, start scanning
        }
    }

    // Start scanning barcodes
    private void startBarcodeScanner() {
        barcodeScanner.resume();
        barcodeScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null) {
                    barcodeScanner.pause(); // Stop scanning after detecting one barcode
                    fetchProductDetails(result.getText()); // Fetch product details from API
                }
            }
        });
    }

    // Fetch product details from API
    private void fetchProductDetails(String barcode) {
        Retrofit retrofit = RetrofitClient.getInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<ProductResponse> call = apiService.getProduct(barcode);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse productResponse = response.body();
                    Product product = productResponse.getProduct();

                    if (product != null) {
                        productNameTextView.setText(product.getName());

                        // Get sustainability grade (A to E)
                        String sustainabilityGrade = product.getSustainabilityInfo();
                        sustainabilityRatingTextView.setText("Sustainability: " + sustainabilityGrade.toUpperCase());

                        // Calculate sustainability score (dummy logic)
                        int sustainabilityScore = calculateSustainabilityScore(sustainabilityGrade);
                        sustainabilityScoreBar.setProgress(sustainabilityScore);

                        // Change progress bar color based on grade
                        changeProgressBarColor(sustainabilityGrade);
                    } else {
                        Toast.makeText(MainActivity.this, "Product data not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                }
                barcodeScanner.resume();
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching product data", Toast.LENGTH_SHORT).show();
                barcodeScanner.resume();
            }
        });
    }
    private void changeProgressBarColor(String grade) {
        int color;

        switch (grade.toUpperCase()) {
            case "A":
                color = ContextCompat.getColor(this, R.color.green); // Best sustainability
                break;
            case "B":
                color = ContextCompat.getColor(this, R.color.light_green);
                break;
            case "C":
                color = ContextCompat.getColor(this, R.color.yellow);
                break;
            case "D":
                color = ContextCompat.getColor(this, R.color.orange);
                break;
            case "E":
                color = ContextCompat.getColor(this, R.color.red); // Worst sustainability
                break;
            default:
                color = ContextCompat.getColor(this, R.color.gray); // Unknown sustainability
                break;
        }

        sustainabilityScoreBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
    }


    // Calculate sustainability score (dummy logic)
    private int calculateSustainabilityScore(String sustainabilityInfo) {
        switch (sustainabilityInfo) {
            case "A": return 90; // 🌱 Best sustainability
            case "B": return 75;
            case "C": return 50;
            case "D": return 25;
            case "E": return 10;  // ❌ Worst sustainability
            default: return 0;    // No data
        }
    }

    // Handle camera permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBarcodeScanner(); // Restart scanning after permission granted
            } else {
                Toast.makeText(this, "Camera permission is required to scan barcodes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Resume scanner when activity comes back to foreground
    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeScanner != null) {
            barcodeScanner.resume();
        }
    }

    // Pause scanner when activity goes to background
    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeScanner != null) {
            barcodeScanner.pause();
        }
    }
}
