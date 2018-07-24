package com.example.hleks.digitalq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.HashMap;

public class DepartmentsActivity extends AppCompatActivity {

    private DrawerLayout drawerLay;
    private NavigationView navView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerTrigger;
    private ListView depList;
    private TextView txtPolyQueue, txtPayQueue, txtPhaQueue, txtPolyTime, txtPayTime, txtPhaTime;

    private SessionManagement session;
    private String[] deps;
    private RequestQueue requestQueue;
    private HashMap<String, String> data = new HashMap<>();

    Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
    Network network = new BasicNetwork(new HurlStack());


    public DepartmentsActivity() {
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        txtPolyQueue = findViewById(R.id.txtPolyQueue);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, session.BASE_URL+"departments", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject js = new JSONObject(response);
                    if (js.getString("status").equals("true")){
                        txtPolyQueue.setText(js.getString("poly_queue"));
                        txtPayQueue.setText(js.getString("pay_Queue"));
                        txtPhaQueue.setText(js.getString("pha_Queue"));
                        txtPolyTime.setText(js.getString("poly_Time"));
                        txtPayTime.setText(js.getString("pay_Time"));
                        txtPhaTime.setText(js.getString("pha_Time"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }});

        MySingleton.getInstance(DepartmentsActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        /*txtPolyQueue = findViewById(R.id.txtPolyQueue);
        txtPolyTime = findViewById(R.id.txtPolyTime);
        txtPayQueue = findViewById(R.id.txtPayQueue);
        txtPayTime = findViewById(R.id.txtPayTime);
        txtPhaQueue = findViewById(R.id.txtPhaQueue);
        txtPhaTime = findViewById(R.id.txtPhaTime);*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Departments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView =  findViewById(R.id.navigationDrawer);
        drawerLay = findViewById(R.id.activity_departments);
        drawerTrigger = new ActionBarDrawerToggle(this, drawerLay, R.string.drawer_open, R.string.drawer_close);

        drawerLay.addDrawerListener(drawerTrigger);
        drawerTrigger.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManagement(this.getApplicationContext());

        session.loggedIn();

        deps = getResources().getStringArray(R.array.list_departments);
        depList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deps));
        depList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Go to the departments page
                Intent transitionTo = new Intent(DepartmentsActivity.this, CheckinActivity.class);
                transitionTo.putExtra("department", deps[position]);
                transitionTo.putExtra("dep_id", position);
                startActivity(transitionTo);
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.toString().equals("Logout")){
                    session.logout();
                    finish();
                }
                Toast.makeText(DepartmentsActivity.this, "you have chosen "+item.toString(), Toast.LENGTH_LONG).show();
                //Close the drawer after selecting an option
                drawerLay.closeDrawers();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerTrigger.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
