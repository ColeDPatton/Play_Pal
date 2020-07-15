package com.example.p19;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class shows the user all the users that they have matched with. A "match" just means that both users swiped right
 * on each other
 */
public class MatchedUsersActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String url = "http://192.168.1.120:8080/users/matchedUsers";

    private ArrayList<User> userList;
    static final int READ_BLOCK_SIZE = 100;
    private User currentUser;
    private JSONArray request;
    private JSONArray matches;
    private JSONObject requester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_matched_users);
        matches = null;
        requester = null;

        userList = new ArrayList<>();
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        loadCurrentUser();
        setMatches();

    }

    /**
     * This method loads the current user from a file stored on the internal storage of the device
     */
    public void loadCurrentUser(){
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
    public boolean loadCurrentUser2(){
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method gets the list of users matched with the user currently logged in and sends it to
     * parseUserList()
     */
    private void setMatches(){

        //Parses user ID as JSON object
        try{
            requester = new JSONObject("{\"id\" : \"" + currentUser.getId() + "\"}");
            request = new JSONArray();
            request.put(requester);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        matches = response;
                        parseUserList(matches);
                    }
                }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
        });

        mQueue.add(arrayRequest);

    }

    /**
     * This method parses the JSONArray and binds all the data to the newly created RecyclerView
     * @param list The list of users that the current user has matched with
     */
    private void parseUserList(JSONArray list){
        for(int i=0; i<list.length(); i++){
            try{
                userList.add(new User(list.getJSONObject(i)));
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        recyclerView = findViewById(R.id.recycler_view2);
        adapter = new MatchedUserAdapter(userList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

}
