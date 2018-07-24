package com.example.hleks.digitalq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private ImageButton btnDepartments, btnProfile, btnLocation, btnAbout;
    private Button btnLogout;

    private SessionManagement session;

    JSONObject json = null;
    HashMap<String, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnDepartments = findViewById(R.id.lyDepartments);
        btnLogout = findViewById(R.id.btnLogout);
        btnAbout = findViewById(R.id.imbAbout);
        btnLocation = findViewById(R.id.imbLocation);
        btnProfile = findViewById(R.id.imbPofile);

        session = new SessionManagement(this.getApplicationContext());

        session.loggedIn();

        btnDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionTo = new Intent(HomeActivity.this, DepartmentsActivity.class);
                startActivity(transitionTo);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionTo = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(transitionTo);
            }
        });
    }
}
