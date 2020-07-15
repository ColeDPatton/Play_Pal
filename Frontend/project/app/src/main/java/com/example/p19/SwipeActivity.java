package com.example.p19;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This is the core activity of the app where the users view information on other users and swipe
 * indicating whether or not they want to play together with this person
 */
public class SwipeActivity extends AppCompatActivity {

    private ImageButton bMoreInfo, bPrevInfo, bYes, bNo, bReport;
    private TextView tvNameGender,tvPlatformAge, tvGameName, tvUserName;
    private RequestQueue mQueue;
    private ImageView ivImage;
    private int infoSet;
    private ArrayList gamesPlayed;
    private User swipingOnUser;
    private User swipingUser;
    private JSONObject swipe;
    private JSONObject getNextReq;
    private Bitmap mGuestBitmap;
    String swipeUrl = "http://192.168.1.120:8080/users/swipe";
    String consoleUrl = "http://192.168.1.120:8080/users/searchByConsole";
    String randomUrl = "http://192.168.1.120:8080/users/searchByRandom";
    String gameUrl = "http://192.168.1.120:8080/users/searchByGame";
    static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_swipe);
        bMoreInfo = findViewById(R.id.bMoreInfo);
        bPrevInfo = findViewById(R.id.bPrevInfo);
        bYes = findViewById(R.id.bYes);
        bNo =  findViewById(R.id.bNo);
        bReport = findViewById(R.id.bReport);
        tvNameGender = findViewById(R.id.tvNameGender);
        tvPlatformAge = findViewById(R.id.tvPlatformAge);
        tvGameName = findViewById(R.id.tvGameName);
        tvUserName = findViewById(R.id.tvUserName);
        ivImage = findViewById(R.id.ivImage);

        infoSet = 0;
        gamesPlayed = new ArrayList<String>();
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        swipingOnUser = null;
        mGuestBitmap = null;
        userInit();
        bMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInfo(1);
                // display next set of info
            }
        });

        bPrevInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInfo(-1);
                // display previous set of info
            }
        });

        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swipingOnUser.getId()!=-1) {
                    swipe(1);
                    mGuestBitmap = null;
                }
                // send data saying there was a swipe to match
            }
        });

        bNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swipingOnUser.getId()!=-1) {
                    swipe(0);
                    mGuestBitmap = null;
                }
                // send data to say there was no interest
            }
        });

        bReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwipeActivity.this, ReportUserActivity.class);
                intent.putExtra("reportedid", swipingOnUser.getId());
                intent.putExtra("reporterid", swipingUser.getId());
                intent.putExtra("reportedname", swipingOnUser.getName());
                startActivity(intent);
            }
        });

    }

    /**
     * This method cyles through data based on the direction the @param direction indicates.
     * @param direction The direction to progress the data displayed
     */
    private void setInfo(int direction){
        if(gamesPlayed.size()>0){
            if(direction>0){
                infoSet++;
                if(infoSet>gamesPlayed.size()){
                    infoSet = 0;
                }
            }
            else if(direction<0){
                infoSet--;
                if(infoSet<0){
                    infoSet = gamesPlayed.size();
                }
            }
            displayInfo();
        }
        if(gamesPlayed.size()==0){
            Toast noMoreInfo = Toast.makeText(getApplicationContext(), "No more info on this user!", Toast.LENGTH_SHORT);
            noMoreInfo.show();
        }
    }

    /**
     * This method displays all the data from the user that the setInfo() method indicates should be
     * displayed
     */
    private void displayInfo(){
        tvUserName.setText(swipingOnUser.getName());
        if(infoSet==0){
            // set profile picture to correct picture once we figure that out
            tvGameName.setText("Basic Info");
            if(mGuestBitmap != null)
                ivImage.setImageBitmap(mGuestBitmap);
            else
                ivImage.setImageResource(R.drawable.controller);
            tvNameGender.setText("Sex:\n"+swipingOnUser.getSex());
            tvPlatformAge.setText("Age:\n"+calculateAge(swipingOnUser.getDateofbirth()));
        }
        else if(infoSet>0 && gamesPlayed.size()>0){
            if(gamesPlayed.get(infoSet-1).equals("fortnite")){
                tvGameName.setText("Fortnite");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("fortnite"));
                ivImage.setImageResource(R.drawable.fornitedefault);
            }
            if(gamesPlayed.get(infoSet-1).equals("minecraft")){
                tvGameName.setText("Minecraft");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("minecraft"));
                ivImage.setImageResource(R.drawable.minecraftdefault);
            }
            if(gamesPlayed.get(infoSet-1).equals("gta5")){
                tvGameName.setText("Grand Theft Auto V");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("gta5"));
                ivImage.setImageResource(R.drawable.gta5default);
            }
            if(gamesPlayed.get(infoSet-1).equals("leagueoflegends")){
                tvGameName.setText("League of Legends");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("leagueoflegends"));
                ivImage.setImageResource(R.drawable.leagueoflegendsdefault);
            }
            if(gamesPlayed.get(infoSet-1).equals("rainbowsixsiege")){
                tvGameName.setText("Rainbow Six Siege");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("rainbowsixsiege"));
                ivImage.setImageResource(R.drawable.rainbowsixdefault);
            }
            if(gamesPlayed.get(infoSet-1).equals("overwatch")){
                tvGameName.setText("Overwatch");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("overwatch"));
                ivImage.setImageResource(R.drawable.overwatchdefault);
            }
            if(gamesPlayed.get(infoSet-1).equals("pubg")){
                tvGameName.setText("PlayerUnknown's\nBattlegrounds");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("pubg"));
                ivImage.setImageResource(R.drawable.pubgdeafult);
            }
            if(gamesPlayed.get(infoSet-1).equals("rocketleague")){
                tvGameName.setText("Rocket League");
                tvNameGender.setText("Console: \n"+swipingOnUser.getConsole());
                tvPlatformAge.setText("Skill: \n"+swipingOnUser.getGameSkill("rocketleague"));
                ivImage.setImageResource(R.drawable.rocketleaguedefault);
            }
        }
    }

    /**
     * This method is called initially on startup. It both loads the current user from the file and
     * gets the first user from the database for the user to swipe on
     */
    private void userInit(){
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
            swipingUser = new User(s);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            getNextReq = new JSONObject("{\"id\" : \"" + swipingUser.getId() + "\"}");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        setNextUser();

    }

    /**
     * This mathod allows the current user to swipe 'yes' or 'no' on a user to indicate whether or not they
     * have an interest in matching with them.
     * @param liked Represents a like where 1 implies interest and 0 implies no interest
     */
    private void swipe(int liked){
        infoSet=0;
        swipe = new JSONObject();
        String data = "{\"swipingUserId\" : \"" + swipingUser.getId() +
                        "\",\"swipedOnId\" : \"" + swipingOnUser.getId() +
                        "\", \"liked\" : \"" + liked + "\"}";
        try{
            swipe = new JSONObject(data);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, swipeUrl, swipe,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getBoolean("b"))
                                setNextUser();
                        }
                        catch (Exception e){
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
     * This method makes a request to the server for the next person to swipe on and sets the
     * required fields on the display
     */
    public void setNextUser(){
        String url = "";
        if(!(swipingUser.getSearchPref() == 1 || swipingUser.getSearchPref() == 2 || swipingUser.getSearchPref() == 3))
            swipingUser.setSearchPref(1);
        ivImage.setImageResource(R.drawable.controller);
        if(swipingUser.getSearchPref()!=2)
            url = randomUrl;
        if(swipingUser.getSearchPref()==2)
            url = consoleUrl;
        if(swipingUser.getSearchPref()==3){
            try {
                getNextReq.put("game", swipingUser.getGamepref());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            url = gameUrl;
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, getNextReq,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response.put("searchpref", 1);
                            swipingOnUser = new User(response);
                            if(swipingOnUser.getId()==-1)
                                noMoreUsers();
                            else{
                                infoSet=0;
                                initGamesPlayed();
                                downloadImage(ivImage, swipingOnUser.getId(), "profilepic");
                                displayInfo();
                            }

                        }
                        catch (Exception e){
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
     * This method gets called and sets the user data so that it indicates there's no more users to swipe on
     */
    public void noMoreUsers(){
        tvPlatformAge.setText("");
        tvNameGender.setText("");
        tvGameName.setText("No more users!");
        tvUserName.setText("Try again later");
    }

    public void initGamesPlayed(){
        if(swipingOnUser.getFortnite()>0)
            gamesPlayed.add("fortnite");
        if(swipingOnUser.getMinecraft()>0)
            gamesPlayed.add("minecraft");
        if(swipingOnUser.getGta5()>0)
            gamesPlayed.add("gta5");
        if(swipingOnUser.getLeagueoflegends()>0)
            gamesPlayed.add("leagueoflegends");
        if(swipingOnUser.getRainbowsixsiege()>0)
            gamesPlayed.add("rainbowsixsiege");
        if(swipingOnUser.getOverwatch()>0)
            gamesPlayed.add("overwatch");
        if(swipingOnUser.getPubg()>0)
            gamesPlayed.add("pubg");
        if(swipingOnUser.getRocketleague()>0)
            gamesPlayed.add("rocketleague");
    }

    public static int calculateAge(String birthdayString){
        int age;
        String birth[] = birthdayString.split("/");
        Calendar rightNow = Calendar.getInstance();
        int birthYear = Integer.parseInt(birth[2]);
        int birthMonth = Integer.parseInt(birth[1]);
        int birthDay = Integer.parseInt(birth[0]);
        int currentYear = rightNow.get(Calendar.YEAR);
        int currentMonth = rightNow.get(Calendar.MONTH);
        int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
        age = currentYear - birthYear;
        if(currentMonth<birthMonth)
            age--;
        if(currentMonth==birthMonth){
            if(currentDay<birthDay)
                age--;
        }

        return age;
    }

    /**
     * This method downloads an image Bitmap from the serve and sets it to the ImageView for the profile picture
     * @param mImageView ImageView where the actual image is to be displayed
     * @param id The id of the user that any given image is tied to
     * @param picname The name of the picture you're requesting
     */
    public void downloadImage(final ImageView mImageView, long id, String picname){

        CustomImageRequest sr = new CustomImageRequest("http://192.168.1.120:8080/downloadFileFromGame?id="+id+"&game="+picname,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mImageView.setImageBitmap(response);
                        mGuestBitmap = response;
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_INSIDE,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(sr);
    }


}

