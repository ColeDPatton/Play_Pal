package com.example.p19;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static com.example.p19.LoadingActivity.READ_BLOCK_SIZE;

public class PremiumSettingsActivity extends AppCompatActivity {

    Button bPrefs, bLogout, bDelete;
    Spinner sPrefs, sGamePrefs;
    User currentUser;
    RequestQueue mQueue;
    String url = "http://192.168.1.120:8080/users/deleteUser";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_premium_settings);

        bPrefs = findViewById(R.id.bPrefs);
        bLogout = findViewById(R.id.bLogout);
        bDelete = findViewById(R.id.bDelete);
        sPrefs = findViewById(R.id.sPrefs);
        sGamePrefs = findViewById(R.id.sGamePrefs);
        mQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        ArrayList<String> prefs = new ArrayList<>();
        prefs.add("Random");
        prefs.add("Console");
        prefs.add("Game");
        ArrayAdapter<String> prefAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,prefs);
        sPrefs.setAdapter(prefAdapter);

        ArrayList<String> gameprefs = new ArrayList<>();
        gameprefs.add("Fortnite");
        gameprefs.add("Minecraft");
        gameprefs.add("GTA V");
        gameprefs.add("League of Legends");
        gameprefs.add("Rainbow Six Siege");
        gameprefs.add("Overwatch");
        gameprefs.add("PubG");
        gameprefs.add("Rocket League");
        ArrayAdapter<String> gameAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,gameprefs);
        sGamePrefs.setAdapter(gameAdapter);

        loadUser();

        if(currentUser.getSearchPref()==1){
            sPrefs.setSelection(0);
        }
        else if(currentUser.getSearchPref()==2){
            sPrefs.setSelection(1);
        }
        else{
            sPrefs.setSelection(2);
        }

        sGamePrefs.setSelection(0);

        bPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sPrefs.getSelectedItem().toString().equals("Random"))
                    currentUser.setSearchPref(1);
                else if(sPrefs.getSelectedItem().toString().equals("Console"))
                    currentUser.setSearchPref(2);
                else{
                    if(currentUser.getDefaultGamepref()!=null){
                        currentUser.setSearchPref(3);
                        setGamePref();
                    }

                }
                storeUserAsJsonString();
                finish();
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(currentUser.getId());
                logout();
                Intent intent = new Intent(PremiumSettingsActivity.this, LoadingActivity.class);
                startActivity(intent);
            }
        });
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

    public void deleteAccount(long id){
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

    public void logout(){
        File dir = getFilesDir();
        File file = new File(dir, "userData.txt");
        boolean deleted = file.delete();
        System.out.println("FILE DELETED: "+ deleted);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profilepic.png");
        System.out.println("FILE DELETED: " + mypath.delete());
        Intent intent = new Intent(PremiumSettingsActivity.this, LoadingActivity.class);
        startActivity(intent);
    }

    /**
     * This method stores the user into a file as soon as the activity starts
     */
    public void storeUserAsJsonString(){
        try {
            FileOutputStream fileout = openFileOutput("userData.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            //outputWriter.write(getIntent().getStringExtra("user"));
            outputWriter.write(currentUser.toJsonString());
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGamePref(){
        if(sGamePrefs.getSelectedItem().toString().equals("Fortnite"))
            currentUser.setGamepref("fortnite");
        if(sGamePrefs.getSelectedItem().toString().equals("Minecraft"))
            currentUser.setGamepref("minecraft");
        if(sGamePrefs.getSelectedItem().toString().equals("GTA V"))
            currentUser.setGamepref("gta5");
        if(sGamePrefs.getSelectedItem().toString().equals("League of Legends"))
            currentUser.setGamepref("leagueoflegends");
        if(sGamePrefs.getSelectedItem().toString().equals("Rainbow Six Siege"))
            currentUser.setGamepref("rainbowsixsiege");
        if(sGamePrefs.getSelectedItem().toString().equals("Overwatch"))
            currentUser.setGamepref("overwatch");
        if(sGamePrefs.getSelectedItem().toString().equals("PubG"))
            currentUser.setGamepref("pubg");
        if(sGamePrefs.getSelectedItem().toString().equals("Rocket League"))
            currentUser.setGamepref("rocketleague");
    }
}
