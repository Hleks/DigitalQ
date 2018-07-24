package com.example.hleks.digitalq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText txtName, txtSurname, txtDOB, txtEmail, txtPassword, txtConfirmPass, txtPhone;
    private TextView lblLogin, lblRegister;

    private SessionManagement session;
    HashMap<String, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.btnRegister);
        txtName = findViewById(R.id.txtName);
        txtSurname = findViewById(R.id.txtSurname);
        txtDOB = findViewById(R.id.txtDOB);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);
        lblLogin = findViewById(R.id.txtLogin);
        lblRegister = findViewById(R.id.txtRegister);

        session = new SessionManagement(this.getApplicationContext());

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, session.BASE_URL+"login", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject js = new JSONObject(response);
                            if (js.getString("status").equals("true")){
                                session.userSession("admin@admin");
                                Intent transitionTo = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(transitionTo);
                                finish();
                            }
                            else
                                Toast.makeText(getBaseContext(), "Invalid user response=" + response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Response error", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }}) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        data.put("first_name", txtName.getText().toString());
                        data.put("last_name", txtSurname.getText().toString());
                        data.put("date_of_birth", txtDOB.getText().toString());
                        data.put("email", txtEmail.getText().toString());
                        data.put("password", txtPassword.getText().toString());
                        data.put("phone", txtPhone.getText().toString());
                        return data;
                    }
                };

                if (txtName.getText().length() == 0 || txtSurname.getText().length() == 0 || txtDOB.getText().length() == 0
                        || txtEmail.getText().length() == 0 || txtPassword.getText().length() == 0 || txtConfirmPass.getText().length() == 0){
                    Toast.makeText(RegisterActivity.this, "Please fill in everything", Toast.LENGTH_LONG).show();
                }
                else if (!txtPassword.getText().equals(txtConfirmPass.getText())){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                }
                else {
                    MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });

        lblLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionTo = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(transitionTo);
                finish();
            }
        });
    }
}
