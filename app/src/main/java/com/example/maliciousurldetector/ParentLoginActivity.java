package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ParentLoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    TextView goToRegister;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.parentEmail);
        password = findViewById(R.id.parentPassword);
        loginBtn = findViewById(R.id.parentLoginBtn);
        goToRegister = findViewById(R.id.goToRegister);

        loginBtn.setOnClickListener(v -> loginUser());

        // ⭐ Add this for Register Activity Navigation
        goToRegister.setOnClickListener(v -> {
            Intent i = new Intent(ParentLoginActivity.this, ParentRegisterActivity.class);
            startActivity(i);
        });
    }

    private void loginUser() {
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (e.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, ParentDashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
