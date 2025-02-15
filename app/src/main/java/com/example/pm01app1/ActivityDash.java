package com.example.pm01app1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityDash extends AppCompatActivity {

    Button btnClient, btnList, btnCapturePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash);

        btnClient = findViewById(R.id.btnAdd);
        btnList = findViewById(R.id.btnList);



        btnClient.setOnClickListener(v -> {
            startActivity(new Intent(ActivityDash.this, ActivityPrincipal.class));

        });

        btnList.setOnClickListener(v -> {
            startActivity(new Intent(ActivityDash.this, ActivityLists.class));
        });



    }
}