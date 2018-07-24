package com.example.hleks.digitalq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class CheckinActivity extends AppCompatActivity {

    private DrawerLayout drawerLay;
    private NavigationView navView;
    private NavDrawerHelper navDrawerHelp;
    private Toolbar toolbar;
    private Button btnCheckIn;
    private ProgressDialog pDialog;

    private EditText txtIllness;
    private RadioButton disYes;
    private RadioButton disNo;

    private RequestQueue requestQueue;
    private SessionManagement session;

    JSONObject json = null;
    HashMap<String, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        btnCheckIn = (Button) findViewById(R.id.btnCheckin);
        txtIllness = (EditText) findViewById(R.id.txtIllness);
        disYes = (RadioButton) findViewById(R.id.rdbYes);
        disNo = (RadioButton) findViewById(R.id.rdbNo);
        pDialog = new ProgressDialog(this);

        disYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disNo.setChecked(false);
            }
        });
        disNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disYes.setChecked(false);
            }
        });

        initNavDrawer();

        session = new SessionManagement(this.getApplicationContext());
        session.loggedIn();

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, session.BASE_URL+"joinQueue", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        try {
                            JSONObject js = new JSONObject(response);
                            if (js.getString("status").equals("true")){
                                Intent transitionTo = new Intent(CheckinActivity.this, QueueActivity.class);
                                transitionTo.putExtra("callingNumber", js.getString("calling_num"));
                                transitionTo.putExtra("department", getIntent().getExtras().getString("department"));

                                startActivity(transitionTo);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(CheckinActivity.this, "Failed to add you in the queue. Please try again later.", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }}) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String illness = txtIllness.getText().toString();
                        String disability;
                        if (disYes.isChecked())
                            disability = disYes.getText().toString();
                        else
                            disability = disNo.getText().toString();

                        data.put("department", "1");
                        data.put("priority", "1");
                        data.put("user_id", "1");
                        data.put("illness", illness);
                        data.put("disability", disability);
                        return data;
                    }
                };

                if (txtIllness.getText().length() == 0)
                    Toast.makeText(CheckinActivity.this, "Please fill in your illness", Toast.LENGTH_LONG).show();
                else if (!disNo.isChecked() && !disYes.isChecked())
                    Toast.makeText(CheckinActivity.this, "Please disability status", Toast.LENGTH_LONG).show();
                else {
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    MySingleton.getInstance(CheckinActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.toString().equals("Logout")){
                    session.logout();
                    finish();
                }
                Toast.makeText(CheckinActivity.this, "you have chosen "+item.toString(), Toast.LENGTH_LONG).show();
                //Close the drawer after selecting an option
                drawerLay.closeDrawers();
                return false;
            }
        });
    }

    public void initNavDrawer(){
        navView = (NavigationView) findViewById(R.id.navigationDrawer);
        drawerLay = (DrawerLayout) findViewById(R.id.activity_checkin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Check In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        navDrawerHelp = new NavDrawerHelper(this);
        navDrawerHelp.initialize(drawerLay, navView, toolbar, false);
    }
}
