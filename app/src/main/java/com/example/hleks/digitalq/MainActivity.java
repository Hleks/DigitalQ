package com.example.hleks.digitalq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button login, register;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManagement(MainActivity.this);

        if (session.isLoggedIn()){
            Intent transitionTo = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(transitionTo);
            finish();
        }

        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionTo = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(transitionTo);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionTo = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(transitionTo);
                finish();
            }
        });
    }
}
