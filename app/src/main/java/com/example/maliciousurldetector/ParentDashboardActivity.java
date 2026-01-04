package com.example.maliciousurldetector;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentDashboardActivity extends AppCompatActivity {

    TextView appsUsed, urlsVisited, alertsList;
    FirebaseFirestore db;
    String parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        db = FirebaseFirestore.getInstance();
        parentId = FirebaseAuth.getInstance().getUid();

        appsUsed = findViewById(R.id.appsUsed);
        urlsVisited = findViewById(R.id.urlsVisited);
        alertsList = findViewById(R.id.alertsList);

        listenChildUpdates();
    }

    private void listenChildUpdates() {
        DocumentReference ref = db.collection("parents").document(parentId);

        ref.addSnapshotListener((snapshot, error) -> {
            if (snapshot != null && snapshot.exists()) {
                appsUsed.setText(snapshot.getString("appsUsed"));
                urlsVisited.setText(snapshot.getString("urlsVisited"));
                alertsList.setText(snapshot.getString("alerts"));
            }
        });
    }
}
