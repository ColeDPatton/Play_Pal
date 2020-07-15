package com.example.p19;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter2;
    private ArrayList<User> userList;
    private User admin,badUser;
    private JSONObject adminJA;
    private JSONObject tempr;
    private JSONArray adminization;
    private JSONArray reports;
private JSONObject USER;

String url =  "http://192.168.1.120:8080/users/getReports";
    String url2 ="http://192.168.1.120:8080/users/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin);

        tempr= new JSONObject();
        USER = new JSONObject();
        adminization = null;
        reports = null;
        userList = new ArrayList<>();
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        admin = new User(getIntent().getStringExtra("admin"));
        getUserReport();
    }


    private void getUserReport(){

        //Parses user ID as JSON object
        try{
            adminJA = new JSONObject("{\"id\" : \"" + admin.getId() + "\"}");
            adminization = new JSONArray();
            adminization.put(adminJA);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, url, adminization,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        reports = response;
                        parseUserList(reports);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(arrayRequest);

    }
    private void getUserById(long id) {


        try{
            tempr = new JSONObject("{\"id\" : \"" + id + "\"}");

        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2+""+id, tempr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        badUser = new User(response);
                        userList.add(badUser);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(jsonObjectRequest);
    }


    private void parseUserList(JSONArray list){
        for(int i=0; i<list.length(); i++){
            try{
                long userID = list.getJSONObject(i).getLong("reported");
                getUserById(userID);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        recyclerView = findViewById(R.id.recycler_view2);
        adapter2 = new AdminAdapter(userList, getApplicationContext());
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
