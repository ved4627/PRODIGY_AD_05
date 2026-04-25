package com.example.prodigy_ad_05;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button btnScan;
    private TextView tvScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        tvScanResult = findViewById(R.id.tvScanResult);

        btnScan.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Align QR Code inside the frame");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scannedData = result.getContents();


            if (scannedData.startsWith("upi://pay")) {
                Uri uri = Uri.parse(scannedData);
                String name = uri.getQueryParameter("pn");
                String upi = uri.getQueryParameter("pa");
                tvScanResult.setText("💸 Payee: " + (name != null ? name : "Unknown") + "\n🆔 UPI ID: " + upi);
            } else if (scannedData.startsWith("http")) {
                tvScanResult.setText("🔗 Link: " + scannedData);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(scannedData)));
            } else {
                tvScanResult.setText(scannedData);
            }

            Toast.makeText(this, "Scan Complete", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}