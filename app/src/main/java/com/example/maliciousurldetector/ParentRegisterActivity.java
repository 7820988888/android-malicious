package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ParentRegisterActivity extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnRegister;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register); // etEmail, etPass, btnRegister

        etEmail = findViewById(R.id.parentEmail);
        etPass = findViewById(R.id.parentPassword);
        btnRegister = findViewById(R.id.parentRegisterBtn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> registerParent());
    }

    private void registerParent() {
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            btnRegister.setEnabled(true);
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String code = generate6DigitCode();
                    Map<String, Object> data = new HashMap<>();
                    data.put("email", user.getEmail());
                    data.put("code", code);
                    data.put("createdAt", System.currentTimeMillis());

                    db.collection("parents").document(user.getUid()).set(data)
                            .addOnSuccessListener(aVoid -> {
                                // also store code->parentId mapping for easy lookup
                                db.collection("codes").document(code).set(new HashMap<String, Object>() {{
                                    put("parentId", user.getUid());
                                }});
                                Toast.makeText(this, "Registered. Share code: " + code, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, ParentDashboardActivity.class));
                                finish();
                            }).addOnFailureListener(e -> Toast.makeText(this, "DB error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(this, "Register failed: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generate6DigitCode() {
        Random r = new Random();
        int code = 100000 + r.nextInt(900000);
        return String.valueOf(code);
    }
}
