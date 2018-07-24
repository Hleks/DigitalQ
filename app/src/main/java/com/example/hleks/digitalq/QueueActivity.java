package com.example.hleks.digitalq;

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
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QueueActivity extends AppCompatActivity {

    private DrawerLayout drawerLay;
    private NavigationView navView;
    private NavDrawerHelper navDrawerHelp;
    private Toolbar toolbar;
    private TextView txtDepartment, txtNumber, txtDate, txtTime, txtQSize;
    private Button btnOk, btnExit;

    private String callingResult, departResult;
    private SessionManagement session;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        //Set up the navigation drawer
        initNavDrawer();

        //Fetch tools from the layout interface
        txtDepartment = findViewById(R.id.lblDepartment);
        txtNumber = findViewById(R.id.lblNumber);
        txtDate = findViewById(R.id.lblDate);
        txtTime = findViewById(R.id.lblTime);
        txtQSize = findViewById(R.id.txtQueueSize);
        btnOk = findViewById(R.id.btnOk);
        btnExit = findViewById(R.id.btnExit);

        //Get data from the calling activity
        Bundle data = getIntent().getExtras();
        callingResult = data.getString("callingNumber");
        departResult = data.getString("department");
        txtDepartment.setText(departResult);
        txtNumber.setText(callingResult);
        txtQSize.append("1");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        txtTime.setText(mdformat.format(calendar.getTime()));
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        session = new SessionManagement(this.getApplicationContext());
        session.loggedIn();

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transitionTo = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(transitionTo);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, session.BASE_URL+"exitQueue?id="+callingResult, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject js = new JSONObject(response);
                            if (js.getString("status").equals("true")){
                                Intent transitionTo = new Intent(QueueActivity.this, HomeActivity.class);
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
                        Toast.makeText(QueueActivity.this, "Failed to exit the queue. Please try again.", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }});

                MySingleton.getInstance(QueueActivity.this).addToRequestQueue(stringRequest);
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.toString().equals("Logout")){
                    session.logout();
                    finish();
                }
                Toast.makeText(QueueActivity.this, "you have chosen "+item.toString(), Toast.LENGTH_LONG).show();
                //Close the drawer after selecting an option
                drawerLay.closeDrawers();
                return false;
            }
        });
    }

    public void initNavDrawer(){
        navView = findViewById(R.id.navigationDrawer);
        drawerLay = findViewById(R.id.activity_queue);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Queue Status");

        navDrawerHelp = new NavDrawerHelper(this);
        navDrawerHelp.initialize(drawerLay, navView, toolbar, false);
    }
}
