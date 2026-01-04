package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private String planName;
    private int planAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        planName = getIntent().getStringExtra("SUBSCRIPTION_TYPE");
        planAmount = getIntent().getIntExtra("SUBSCRIPTION_PRICE", 0);

        // Open Razorpay Checkout
        startPayment();
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_XXXXXXXX"); // Replace with your Razorpay Test/Live Key

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Malicious URL Detector");
            options.put("description", planName);
            options.put("currency", "INR");
            options.put("amount", planAmount * 100); // Convert to paise

            // Optional prefill
            JSONObject preFill = new JSONObject();
            preFill.put("email", "customer@example.com");
            preFill.put("contact", "9999999999");

            options.put("prefill", preFill);

            checkout.open(PaymentActivity.this, options);

        } catch (Exception e) {
            Toast.makeText(this, "Payment Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Payment success callback
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();

        // Navigate to MainActivity and unlock premium features
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        intent.putExtra("SUBSCRIPTION_TYPE", planName);
        intent.putExtra("PREMIUM_ACTIVE", true);
        startActivity(intent);
        finish();
    }

    // Payment failed callback
    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed! Try Again", Toast.LENGTH_LONG).show();
        finish();
    }
}
