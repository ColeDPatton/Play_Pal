package com.example.p19;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static com.example.p19.LoadingActivity.READ_BLOCK_SIZE;

public class UpgradeUserActivity extends AppCompatActivity {

    EditText etCardNum, etSecurityNum, etExpirationDate, etCardName, etZipCode;
    Button bPay, bCancel;
    User user;
    RequestQueue mQueue;
    String url = "http://192.168.1.120:8080/users/editUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_upgrade_user);

        etCardNum = findViewById(R.id.etCardNum);
        etSecurityNum = findViewById(R.id.etSecurity);
        etExpirationDate = findViewById(R.id.etExp);
        etCardName = findViewById(R.id.etCardName);
        etZipCode = findViewById(R.id.etZip);
        bCancel = findViewById(R.id.bCancel);
        bPay = findViewById(R.id.bUpgrade);

        user = loadUser();
        mQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        if(user==null){
            Toast toast = Toast.makeText(getApplicationContext(),"Error communicating with server please retry",Toast.LENGTH_SHORT);
            toast.show();
        }

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpgradeUserActivity.this, StandardSettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()){
                    user.setUsertype(2);
                    updateUser(user);
                    storeUserAsJsonString();
                    user = loadUser();
                    System.out.println("Did this get set?");
                    System.out.println(user.getUsertype());
                    Intent intent = new Intent(UpgradeUserActivity.this, PremiumSettingsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Invalid Fields",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    public boolean checkData(){
        if(etCardName.getText().toString().trim().isEmpty()||
                etCardNum.getText().toString().trim().isEmpty()||
                etSecurityNum.getText().toString().trim().isEmpty()||
                etExpirationDate.getText().toString().trim().isEmpty()||
                etZipCode.getText().toString().trim().isEmpty())
            return false;
        else
            return true;
    }

    private User loadUser(){
        try {
            FileInputStream fileIn = openFileInput("userData.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
            return new User(s);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateUser(User data){

        JSONObject update = new JSONObject();
        try{
            update = new JSONObject(data.toJsonString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, update,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getBoolean("b")==true) {
                                storeUserAsJsonString();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    /**
     * Helper method that stores the user to a file in Internal storage as a JSON
     */
    public void storeUserAsJsonString(){
        try {
            FileOutputStream fileout = openFileOutput("userData.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            //outputWriter.write(getIntent().getStringExtra("user"));
            outputWriter.write(user.toJsonString());
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
