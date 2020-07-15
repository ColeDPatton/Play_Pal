package com.example.p19;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;

/**
 * This activity allows a user to submit a report form to the server for any offense
 */
public class ReportUserActivity extends AppCompatActivity {

    public User user;
    RequestQueue mQueue;
    CheckBox cbProfilePic, cbLanguage, cbSpam, cbFakeAccount;
    EditText etDescription;
    TextView tvUsername;
    Spinner sReason;
    User currentUser,tempUser;
    String url = "http://192.168.1.120:8080/users/reportUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_report_user);

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        sReason = findViewById(R.id.sReason);
        etDescription = findViewById(R.id.etDesc);
        tvUsername = findViewById(R.id.tvUsername);

        tvUsername.setText(getIntent().getStringExtra("reportedname"));

        findViewById(R.id.bCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.bReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport();
                finish();
            }
        });

        currentUser = new User(getIntent().getStringExtra("currentUser"));
        tempUser = new User(getIntent().getStringExtra("tempUser"));

        ArrayList<String> reasons = new ArrayList<>();

        reasons.add("Abusive Language");
        reasons.add("Inappropriate Profile Picture");
        reasons.add("Fake User");
        reasons.add("Spam Account");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,reasons);
        sReason.setAdapter(genderAdapter);
    }

    public void sendReport(){

        JSONObject report = new JSONObject();
        try{
            report.put("reason", sReason.getSelectedItem().toString());
            report.put("description", etDescription.getText().toString());
            report.put("reported", tempUser.getId());

            report.put("reporter", currentUser.getId());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, report,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(jsonObjectRequest);
    }



}
