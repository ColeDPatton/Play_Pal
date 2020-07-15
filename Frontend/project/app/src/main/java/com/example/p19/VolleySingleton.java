package com.example.p19;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class makes it so that there is only one instance of a RequestQueue throughout the project
 */
public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;

    /**
     * Constructor for VolleySingleton
     * @param context Context of where the queue is going to be added
     */
    private VolleySingleton(Context context){
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * This method either returns the singleton or creates one if it doesn't exist
     * @param context The context of where you want the queue
     * @return The singleton object from which the queue stems
     */
    public static synchronized VolleySingleton getInstance(Context context){
        if(mInstance == null)
            mInstance = new VolleySingleton(context);
        return mInstance;
    }

    /**
     * Returns the RequestQueue associated with the instance of the singleton
     * @return The RequestQueue associated with the instance of the singleton
     */
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
