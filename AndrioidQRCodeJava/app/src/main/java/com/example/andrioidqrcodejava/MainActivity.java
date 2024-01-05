package com.example.andrioidqrcodejava;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.andrioidqrcodejava.databinding.ActivityMainBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if(isGranted) {
                    showCamera();
                }
                else{
                    //Show why user need this permission
                }
            });

    // Premenná na uchovávanie výsledku skenovania
    //private String scannedResult = "";

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(),result ->{
        if(result.getContents()==null){
            Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show();
        } else {
            displayScannedResult(result.getContents());
        }
    });

    private void displayScannedResult(String result) {
        binding.textResult.setText(result);
        Toast.makeText(this, "Scanned: " + result, Toast.LENGTH_SHORT).show();
    }

    private void setResult(String contents) {
        binding.textResult.setText(contents);
    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE,ScanOptions.DATA_MATRIX);
        options.setPrompt("Scan QR code or Data Matrix");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();
    }

    private void initViews() {
        binding.fab.setOnClickListener(v -> {
            checkPermissionAndShowActivity(this);
        });
    }

    private void checkPermissionAndShowActivity(Context context) {
        if(ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context,"Camera permission required", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    /*// Metóda na získanie hodnoty výsledku skenovania
    public String getScannedResult() {
        return scannedResult;
    }*/
}