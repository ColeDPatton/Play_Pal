package com.example.p19;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static com.example.p19.LoadingActivity.READ_BLOCK_SIZE;

/**
 * This class is responsible for being a sort of dashboard for the user. It allows them to view all their
 * current data as well as navigate to most of the other activities
 */
public class UserProfileActivity extends AppCompatActivity implements LifecycleObserver {

    private TextView tvGamertag, tvBirthday, tvUserName, tvConsole, tvSex;
    public User currentUser;
    private Button bGoToSwipe, bGoToMatches;
    private ImageButton bSettings, bEditProf;
    private ImageView ivProfile;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_user_profile);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        tvGamertag = findViewById(R.id.tvGamertag);
        tvBirthday = findViewById(R.id.tvDOB);
        tvConsole = findViewById(R.id.tvConsole);
        tvUserName = findViewById(R.id.tvUserName);
        tvSex = findViewById(R.id.tvSex);
        ivProfile = findViewById(R.id.ivProfilePic);
        ivProfile.setImageResource(R.drawable.controller);
        currentUser = new User(getIntent().getStringExtra("user"));
        storeUserAsJsonString();
        tvGamertag.setText(currentUser.getGamertag());
        tvBirthday.setText("Birthday:\n\t\t\t\t\t"+ currentUser.getDateofbirth());
        tvConsole.setText("Console:\n\t\t\t\t\t"+ currentUser.getConsole());
        tvSex.setText("Sex:\n\t\t\t\t\t"+ currentUser.getSex());
        tvUserName.setText(currentUser.getName());
        Log.d("id",""+currentUser.getId());
        downloadImage(ivProfile, currentUser.getId(), "profilepic");
        loadProfilePicFromStorage();
        bGoToSwipe = findViewById(R.id.btSwipe);
        bGoToMatches = findViewById(R.id.btMatches);
        bSettings = findViewById(R.id.btSettings);
        bEditProf = findViewById(R.id.btEditProf);



        bGoToSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SwipeActivity.class);
                startActivity(intent);
            }
        });

        bGoToMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, MatchedUsersActivity.class);
                startActivity(intent);
            }
        });

        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser.getUsertype()!=2){
                    Intent intent = new Intent(UserProfileActivity.this, StandardSettingsActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(UserProfileActivity.this, PremiumSettingsActivity.class);
                    startActivity(intent);
                }

            }
        });

        bEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onResume() {

        super.onResume();
        loadProfilePicFromStorage();
        currentUser = loadUser();

    }

    @Override
    public void onRestart() {

        super.onRestart();
        loadProfilePicFromStorage();
        currentUser = loadUser();

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
    public boolean storeUserAsJsonString2(User user){
        try {
            FileOutputStream fileout = openFileOutput("userData.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(user.toString());
            outputWriter.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
                saveToInternalStorage(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ivProfile.setImageResource(R.drawable.controller);
                error.printStackTrace();
            }
        });
        mQueue.add(sr);
    }

    private void loadProfilePicFromStorage(){
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "profilepic.png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ivProfile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("There was no stored file");
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profilepic.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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
}
