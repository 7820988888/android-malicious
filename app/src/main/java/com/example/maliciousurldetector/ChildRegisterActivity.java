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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ChildRegisterActivity extends AppCompatActivity {

    EditText etEmail, etPass, etParentCode;
    Button btnRegister;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_register); // etEmail childEmail, etc.

        etEmail = findViewById(R.id.childEmail);
        etPass = findViewById(R.id.childPassword);
        etParentCode = findViewById(R.id.parentCode);
        btnRegister = findViewById(R.id.childRegisterBtn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> registerChild());
    }

    private void registerChild() {
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String code = etParentCode.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        // Lookup parentId by code
        db.collection("codes").document(code).get().addOnSuccessListener((DocumentSnapshot ds) -> {
            if (!ds.exists()) {
                btnRegister.setEnabled(true);
                Toast.makeText(this, "Invalid parent code", Toast.LENGTH_SHORT).show();
                return;
            }
            String parentId = ds.getString("parentId");
            if (parentId == null) {
                btnRegister.setEnabled(true);
                Toast.makeText(this, "Invalid parent mapping", Toast.LENGTH_SHORT).show();
                return;
            }
            // create child auth account
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                btnRegister.setEnabled(true);
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        // add child doc
                        db.collection("children").document(user.getUid()).set(new HashMap<String, Object>() {{
                            put("email", user.getEmail());
                            put("linkedParent", parentId);
                            put("mode", "child");
                            put("lastHeartbeat", System.currentTimeMillis());
                        }}).addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Child registered and linked", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, ChildModeActivity.class));
                            finish();
                        });
                    }
                } else {
                    Toast.makeText(this, "Register failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener(e -> {
            btnRegister.setEnabled(true);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
