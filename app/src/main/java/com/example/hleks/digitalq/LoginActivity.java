package com.example.hleks.digitalq;

import android.app.ProgressDialog;
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

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText txtUsername, txtPassword;
    private TextView txtRegister;
    private ProgressDialog pDialog;

    private RequestQueue requestQueue;
    private SessionManagement session;
    private HashMap<String, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtUsername = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        pDialog = new ProgressDialog(this);

        session = new SessionManagement(this.getApplicationContext());

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, session.BASE_URL+"login", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        try {
                            JSONObject js = new JSONObject(response);
                            if (js.getString("status").equals("true")){
                                session.userSession("admin@admin");
                                Intent transitionTo = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(transitionTo);
                                finish();
                            }
                            else
                                Toast.makeText(LoginActivity.this, "Invalid user response=" + response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(LoginActivity.this, "Response error", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }}) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String username = txtUsername.getText().toString();
                        String password = txtPassword.getText().toString();
                        data.put("user_name", username);
                        data.put("password", password);
                        data.put("remember", "false");
                        return data;
                    }
                };

                if (txtUsername.getText().length() == 0 || txtPassword.getText().length() == 0){
                    Toast.makeText(LoginActivity.this, "Please fill in everything", Toast.LENGTH_LONG).show();
                }
                else {
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionTo = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(transitionTo);
            }
        });
    }
}
