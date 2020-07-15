package com.example.p19;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Pattern;

/**
 * This activity prompts the user for credentials and handles all actions pertaining to logging a user into
 * their account
 */
public class LoginActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private EditText etEmail;
    private EditText etPassword;
    private Toast here;
    private Toast connectionErr;
    private Toast invalidEmail;
    private Toast emptyPassword;
    private Toast invalidCreds;
    private int loginStatus;
    private JSONObject user;
    private long userID;
    private int userType;


    private String url = "http://192.168.1.120:8080/users";
    private String url2 = "http://192.168.1.120:8080/users/loginCheck";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_login);
        Button blogin = findViewById(R.id.bLogin);
        user = new JSONObject();
        connectionErr = Toast.makeText(getApplicationContext(),"Error connecting to server please try again",Toast.LENGTH_SHORT);
        invalidEmail = Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT);
        emptyPassword = Toast.makeText(getApplicationContext(),"Empty password",Toast.LENGTH_SHORT);
        invalidCreds = Toast.makeText(getApplicationContext(),"Incorrect email/password",Toast.LENGTH_SHORT);
        here = Toast.makeText(getApplicationContext(),"reeee",Toast.LENGTH_SHORT);
        userID = -1;
        loginStatus = 0;
        etEmail = findViewById(R.id.etEMAIL);
        etPassword = findViewById(R.id.etPASSWORD);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();

        blogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getLoginStatus();
            }
        });

    }


    /**
     * This method checks for valid formatting first. If there is valid formatting, then the credentials are sent up to
     * the server and if they are valid for some user, initLogin() gets called. If the credentials are not valid, the method
     * creates a toast telling the user that the credentials are incorrect
     */
    private void getLoginStatus(){

        if(validEmail(etEmail.getText().toString())==false){
            invalidEmail.show();
        }
        else if(TextUtils.isEmpty(etPassword.getText())){
            emptyPassword.show();
        }
        else {
            JSONObject login = new JSONObject();
            String data = "{\"email\" : \"" + etEmail.getText().toString() + "\",\"password\" : \"" + etPassword.getText().toString() + "\"}";
            try {
                login = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
                connectionErr.show();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, login,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("b") == true)
                                    initLogin();
                                else
                                    invalidCreds.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                connectionErr.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    here.show();
                }
            });

            mQueue.add(jsonObjectRequest);
        }
    }

    /**
     * Helper method that checks the @param email to see if it's a valid email according to our regex.
     * @param email - the email that is to be checked
     * @return - true if valid and false if invalid
     */
    public boolean validEmail(String email)
    {
        if (email == null)
            return false;
        String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }


    /**
     * This method retrieves all the data for the user from the server including the id. This then modifies the user in the class,
     * Once this happens, the finishLogin() method is called to verify that it was successful.
     */
    private void initLogin(){
        JSONObject login = new JSONObject();
        String data = "{\"email\" : \"" + etEmail.getText().toString() +  "\",\"password\" : \"" + etPassword.getText().toString() + "\"}";
        try{
            login = new JSONObject(data);
        }
        catch(JSONException e){
            e.printStackTrace();
            connectionErr.show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url+"/login", login,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = response;
                            userID = response.getLong("id");
                            userType= response.getInt("usertype");
                            user.put("id",response.getLong("id"));
                            finishLogin();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            connectionErr.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionErr.show();
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    /**
     * This method is the last step of the login process. It creates a new Intent and sends the
     * user through. If the userID is -1 this indicates that there was some sort of server side error and
     * as a result, the login fails. This is mostly just a safeguard for our own code.
     */
    private void finishLogin(){
        if(userID!=-1 && (userType == 1 || userType ==2)){
            Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
            intent.putExtra("user", user.toString());
            startActivity(intent);
        }
        else if(userType == 3){
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            User temp = new User(user.toString());
            Log.i("user",temp.toString());
            intent.putExtra("admin", temp.toJsonString());
            startActivity(intent);
        }
    }

}
