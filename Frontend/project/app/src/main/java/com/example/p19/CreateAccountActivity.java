package com.example.p19;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to parse data for a potential user creation. The data is checked
 * to make sure it agrees with certain formatting parameters. It handles both the collection
 * of data as well as sends the request to the server.
 */

public class CreateAccountActivity extends AppCompatActivity {

    private ImageView ivProfilePic;
    private Spinner Gender, Console, sFortniteSkill, sMinecraftSkill, sGta5Skill,
            sLeagueOfLegendsSkill, sRainbowSixSkill, sOverwatchSkill, sPubgSkill, sRocketLeagueSkill;
    private EditText Fname, Email, Birth, Gamertag, Password, PasswordConfirm;
    private Toast emailExists, connectError, invalidEmail;
    private Button btImage, bCreateAcc;
    private long userId;
    public JSONObject newuser;
    private Bitmap userImage;


    ImageView imageView;

    private RequestQueue mQueue;
    String url = "http://192.168.1.120:8080/users/addUser";
    String url2= "http://192.168.1.120:8080/uploadFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_create_account);
        initFields();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        findViewById(R.id.bIMAGEADD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

    }

    /**
     * Creates JSONObject from data in XML fields and attempts to create new user in server from info.
     * If the email is already being used or an error arises, the method returns an error or toast.
     * On successful creation, the method calls the creationSuccess() method to finish up the process
     */
    private void attemptCreation(){
        String data =   "{\"name\" : \"" + Fname.getText().toString() +
                        "\",\"gamertag\" : \"" + Gamertag.getText().toString() +
                        "\", \"dateofbirth\" : \"" + Birth.getText().toString()+
                        "\",\"password\" : \"" + Password.getText().toString() +
                        "\",\"sex\" : \"" + Gender.getSelectedItem().toString() +
                        "\", \"email\" : \"" + Email.getText().toString() +
                        "\",\"console\" : \"" + Console.getSelectedItem().toString() +
                        "\", \"fortnite\" : \"" + sFortniteSkill.getSelectedItemPosition() +
                        "\", \"minecraft\" : \"" + sMinecraftSkill.getSelectedItemPosition() +
                        "\", \"gta5\" : \"" + sGta5Skill.getSelectedItemPosition() +
                        "\", \"leagueoflegends\" : \"" + sLeagueOfLegendsSkill.getSelectedItemPosition() +
                        "\", \"rainbowsixsiege\" : \"" + sRainbowSixSkill.getSelectedItemPosition() +
                        "\", \"overwatch\" : \"" + sOverwatchSkill.getSelectedItemPosition() +
                        "\", \"pubg\" : \"" + sPubgSkill.getSelectedItemPosition() +
                        "\", \"rocketleague\" : \"" + sRocketLeagueSkill.getSelectedItemPosition() +
                        "\", \"searchpref\" : \"" + 1 +
                        "\", \"gamepref\" : \"" + null +"\"}";
        try{
            newuser = new JSONObject(data);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, newuser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getBoolean("b")==true){
                                newuser.put("id",response.getLong("id"));
                                newuser.put("searchpref", 1);
                                userId = newuser.getLong("id");
                                Log.d("id:",""+userId);
                                creationSuccess();
                            }
                            else{
                                emailExists.show();
                            }
                        }
                        catch (JSONException e){
                            connectError.show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectError.show();
            }
        });
        mQueue.add(jsonObjectRequest);

    }

    /**
     * This is a helper method that checks to make sure all fields in the XML have been filled out according
     * to our formatting specifications. It also uses a regex to make sure the date is valid.
     * @return boolean - true for valid formating and false for invalid formatting
     */
    public boolean validCreation(){

        String dob = Birth.getText().toString();
        String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dob);

        Toast diffPassword = Toast.makeText(getApplicationContext(),"Passwords don't match",Toast.LENGTH_SHORT);
        Toast selectGender = Toast.makeText(getApplicationContext(),"Please select a gender",Toast.LENGTH_SHORT);
        Toast selectConsole = Toast.makeText(getApplicationContext(),"Please select a platform",Toast.LENGTH_SHORT);
        Toast dobFormat = Toast.makeText(getApplicationContext(),"Please put in a valid date in the format dd/mm/yyyy",Toast.LENGTH_SHORT);


        if(!Password.getText().toString().equals(PasswordConfirm.getText().toString())){
            diffPassword.show();
            return false;
        }
        else if(!matcher.matches()){
            dobFormat.show();
            return false;
        }
        else if(Gender.getSelectedItem().toString().equals("Choose gender")){
            selectGender.show();
            return false;
        }
        else if(Console.getSelectedItem().toString().equals("Choose platform")){
            selectConsole.show();
            return false;
        }
        else if(!validEmail(Email.getText().toString())){
            invalidEmail.show();
            return false;
        }

        return true;

    }

    /**
     * Helper method that checks the @param email to see if it's a valid email according to our regex.
     * @param email - the email that is to be checked
     * @return - true if valid and false if invalid
     */
    public static boolean validEmail(String email)
    {
        if (email == null)
            return false;
        String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    /**
     * This method is called on a response from the server indicating a new user has been created. This
     * method then puts this user into an intent and starts the UserProfileActivity
     */
    public void creationSuccess(){
        Intent intent = new Intent(CreateAccountActivity.this, UserProfileActivity.class);
        try{
            newuser.put("usertype", 1);
            newuser.put("searchpref", 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        intent.putExtra("user", newuser.toString());
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                 userImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                 imageView.setImageBitmap(userImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes a compressed version of specified bitmap to outputstream
     * @param bitmap Takes a bitmap
     * @return Byte array of the outputsteam
     */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Uses VolleyMultipartRequest to handle uploading the image, sends the image as compressed byteArray with two params: id and game.
     * @param bitmap Specified bitmap to be uploaded to the server
     */
    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url2,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",""+userId);
                params.put("game", "profilepic");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        mQueue.add(volleyMultipartRequest);
    }

    private void initFields(){
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        emailExists = Toast.makeText(getApplicationContext(),"This email is already being used",Toast.LENGTH_SHORT);
        connectError = Toast.makeText(getApplicationContext(),"Error connecting to server, please try again",Toast.LENGTH_SHORT);
        invalidEmail = Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT);
        Gender = findViewById(R.id.sGENDER);
        Console = findViewById(R.id.sCONSOLE);
        sFortniteSkill = findViewById(R.id.sFortniteSkill);
        sMinecraftSkill = findViewById(R.id.sMinecraftSkill);
        sGta5Skill = findViewById(R.id.sGta5Skill);
        sLeagueOfLegendsSkill = findViewById(R.id.sLeagueOfLegendsSkill);
        sRainbowSixSkill = findViewById(R.id.sRainbowSixSiegeSkill);
        sOverwatchSkill = findViewById(R.id.sOverwatchSkill);
        sPubgSkill = findViewById(R.id.sPubgSkill);
        sRocketLeagueSkill = findViewById(R.id.sRocketLeagueSkill);
        Fname = findViewById(R.id.etFNAME);
        Email = findViewById(R.id.etEMAIL);
        Birth = findViewById(R.id.etDOB);
        Gamertag = findViewById(R.id.etGAMERTAG);
        Password = findViewById(R.id.etPASSWORD);
        PasswordConfirm = findViewById(R.id.etPASSWORD2);
        ivProfilePic = findViewById(R.id.ivPROFILE);
        btImage = findViewById(R.id.bIMAGEADD);
        imageView =  findViewById(R.id.ivPROFILE);
        newuser = null;

        ArrayList<String> genders = new ArrayList<>();

        genders.add("Choose gender");
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,genders);
        Gender.setAdapter(genderAdapter);

        ArrayList<String> consoles = new ArrayList<>();

        consoles.add("Choose platform");
        consoles.add("Ps4");
        consoles.add("Xbox One");
        consoles.add("PC");
        ArrayAdapter<String> consoleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,consoles);
        Console.setAdapter(consoleAdapter);

        ArrayList<String> skills = new ArrayList<>();

        skills.add("Don't play this");
        skills.add("Beginner");
        skills.add("Intermediate");
        skills.add("Expert");
        ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,skills);
        sFortniteSkill.setAdapter(skillAdapter);
        sMinecraftSkill.setAdapter(skillAdapter);
        sGta5Skill.setAdapter(skillAdapter);
        sLeagueOfLegendsSkill.setAdapter(skillAdapter);
        sRainbowSixSkill.setAdapter(skillAdapter);
        sOverwatchSkill.setAdapter(skillAdapter);
        sPubgSkill.setAdapter(skillAdapter);
        sRocketLeagueSkill.setAdapter(skillAdapter);

        ivProfilePic.setImageResource(R.drawable.controller);


        bCreateAcc = findViewById(R.id.bCREATEACC);

        bCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validCreation()) {
                    attemptCreation();
                    if(!(ivProfilePic.getDrawable()+"").equals("android.graphics.drawable.BitmapDrawable@9fb6df9"))
                        saveToInternalStorage(userImage);
                        uploadBitmap(userImage);
                }
            }
        });
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




}
