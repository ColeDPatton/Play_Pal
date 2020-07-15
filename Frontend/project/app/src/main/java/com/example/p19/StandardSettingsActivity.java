package com.example.p19;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static com.example.p19.LoadingActivity.READ_BLOCK_SIZE;

/**
 * This class allows the user to logout or delete their account
 */
public class StandardSettingsActivity extends AppCompatActivity {

    AlertDialog mDialog;
    RequestQueue mQueue;
    User currentUser;
    String url = "http://192.168.1.120:8080/users/deleteUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_standard_settings);
        loadUser();
        mQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        showUpgrade();

        findViewById(R.id.bLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                Intent intent = new Intent(StandardSettingsActivity.this, LoadingActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.bDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(currentUser.getId());
                logout();
                Intent intent = new Intent(StandardSettingsActivity.this, LoadingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }

    /**
     * This method makes a request to the server to delete the current account from the database
     */
    private void deleteAccount(long id){
        JSONObject acc = new JSONObject();
        try{
            acc.put("id", id);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest deleteReq = new JsonObjectRequest(Request.Method.POST, url, acc,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        logout();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(deleteReq);
    }

    public void loadUser(){
        try {
            FileInputStream fileIn=openFileInput("userData.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            currentUser = new User(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method logs the current user out of the system so that they won't be automatically signed in every time
     */
    private void logout(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profilepic.png");
        mypath.delete();
        File dir = getFilesDir();
        File file = new File(dir, "userData.txt");
        boolean deleted = file.delete();
        System.out.println("FILE DELETED: "+ deleted);
    }

    private void showUpgrade(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StandardSettingsActivity.this);
        mBuilder.setPositiveButton("Get Premium", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(StandardSettingsActivity.this, UpgradeUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBuilder.setMessage("Upgrade to premium to unlock the ability to filter searches!");
        mBuilder.setNegativeButton("Continue",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }) ;
        mDialog = mBuilder.create();
        mDialog.setTitle("Get Premium");
        mDialog.show();
        mDialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0x6a6a6a));
    }
}
