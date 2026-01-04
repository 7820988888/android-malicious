package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maliciousurldetector.R;
import com.google.firebase.auth.FirebaseAuth;

public class ChildLoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.childEmail);
        password = findViewById(R.id.childPassword);
        loginBtn = findViewById(R.id.childLoginBtn);

        loginBtn.setOnClickListener(v -> {
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();
            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Enter credentials", Toast.LENGTH_SHORT).show();
                return;
            }
            auth.signInWithEmailAndPassword(e, p).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(this, ChildModeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Login failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
