package com.example.hleks.digitalq;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Hleks on 2018/06/14.
 */

public class MySingleton {
    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context cntxt;

    private MySingleton(Context context){
        cntxt = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context context){
        if (instance == null){
            instance = new MySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(cntxt.getApplicationContext());
        }
        return requestQueue;
    }

    public<T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
