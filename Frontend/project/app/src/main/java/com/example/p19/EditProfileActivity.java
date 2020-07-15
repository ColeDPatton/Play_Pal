package com.example.p19;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.p19.CreateAccountActivity.validEmail;

/**
 * This class is very similar to the CreateAccountActivity in its functions and fields. The only difference
 * is that this class causes the user to change settings for a pre-existing account
 */
public class EditProfileActivity extends AppCompatActivity {

    private Spinner Gender, Console, sFortniteSkill, sMinecraftSkill, sGta5Skill,
            sLeagueOfLegendsSkill, sRainbowSixSkill, sOverwatchSkill, sPubgSkill, sRocketLeagueSkill;
    private EditText Fname, Email, Birth, Gamertag, Password, PasswordConfirm;
    public User oldUser;
    public ImageButton bBack;
    public Button bSaveChanges;
    static final int READ_BLOCK_SIZE = 1000;
    private RequestQueue mQueue;
    private Bitmap userImage;
    private Button bUpload;
    private ImageView ivEditPIC;

    String url= "http://192.168.1.120:8080/users/editUser";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_edit_profile);
        setUser();
        initFields();
        loadOldData();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }


        findViewById(R.id.bIMAGEADD2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

    }

    /**
     * This method loads the current user from a file within the internal storage
     */
    public void setUser() {
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
            oldUser = new User(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads
     */
    private void loadOldData(){
        loadProfilePicFromStorage();
        Fname.setText(oldUser.getName());
        Email.setText(oldUser.getEmail());
        Birth.setText(oldUser.getDateofbirth());
        Gamertag.setText(oldUser.getGamertag());
        Password.setText(oldUser.getPassword());
        PasswordConfirm.setText(oldUser.getPassword());
        sFortniteSkill.setSelection(oldUser.getFortnite());
        sMinecraftSkill.setSelection(oldUser.getMinecraft());
        sGta5Skill.setSelection(oldUser.getGta5());
        sLeagueOfLegendsSkill.setSelection(oldUser.getLeagueoflegends());
        sRainbowSixSkill.setSelection(oldUser.getRainbowsixsiege());
        sOverwatchSkill.setSelection(oldUser.getOverwatch());
        sPubgSkill.setSelection(oldUser.getPubg());
        sRocketLeagueSkill.setSelection(oldUser.getRocketleague());

        if(oldUser.getSex().equals("Male")){
            Gender.setSelection(0);
        }
        else if(oldUser.getSex().equals("Female")){
            Gender.setSelection(1);
        }
        else if(oldUser.getSex().equals("Other")){
            Gender.setSelection(2);
        }

        if(oldUser.getConsole().equals("Ps4")){
            Console.setSelection(0);
        }
        else if(oldUser.getConsole().equals("Xbox One")){
            Console.setSelection(1);
        }
        else if(oldUser.getConsole().equals("PC")){
            Console.setSelection(2);
        }
    }

    /**
     * This method handles the initialization of the data fields. Simply used to clean up the onCreate() method
     */
    private void initFields(){
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        bUpload = findViewById(R.id.bIMAGEADD2);
        ivEditPIC = findViewById(R.id.ivEditPic);
        Fname = findViewById(R.id.etFNAME);
        Email = findViewById(R.id.etEMAIL);
        Birth = findViewById(R.id.etDOB);
        Gamertag = findViewById(R.id.etGAMERTAG);
        Password = findViewById(R.id.etPASSWORD);
        PasswordConfirm = findViewById(R.id.etPASSWORD2);
        Gender = findViewById(R.id.sGENDER);
        Console = findViewById(R.id.sCONSOLE);
        bBack = findViewById(R.id.btBack);
        bSaveChanges = findViewById(R.id.bCREATEACC);
        sFortniteSkill = findViewById(R.id.sFortniteSkill);
        sMinecraftSkill = findViewById(R.id.sMinecraftSkill);
        sGta5Skill = findViewById(R.id.sGta5Skill);
        sLeagueOfLegendsSkill = findViewById(R.id.sLeagueOfLegendsSkill);
        sRainbowSixSkill = findViewById(R.id.sRainbowSixSiegeSkill);
        sOverwatchSkill = findViewById(R.id.sOverwatchSkill);
        sPubgSkill = findViewById(R.id.sPubgSkill);
        sRocketLeagueSkill = findViewById(R.id.sRocketLeagueSkill);
        ivEditPIC.setImageResource(R.drawable.controller);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                intent.putExtra("user", oldUser.toJsonString());
                startActivity(intent);
            }
        });

        bSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateEmptyFields();
                if(validUser() && changesMade()){
                    updateProfile();

                }
                if(!(ivEditPIC.getDrawable()+"").equals("android.graphics.drawable.BitmapDrawable@9fb6df9")){
                    uploadBitmap(userImage);
                }
                if(!changesMade()){
                    Toast noChangesMade = Toast.makeText(getApplicationContext(),"No changes were made",Toast.LENGTH_SHORT);
                    noChangesMade.show();
                    refreshFields();
                }
                if(!validUser()){
                    Toast invalidUser = Toast.makeText(getApplicationContext(),"No valid changes made",Toast.LENGTH_SHORT);
                    invalidUser.show();
                    refreshFields();
                }
            }
        });

        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,genders);
        Gender.setAdapter(genderAdapter);

        ArrayList<String> consoles = new ArrayList<>();
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
    }

    /**
     * Checks to see whether or not the formatting for the user is correct and if any fields are just spaces.
     * @return true if the user data creates a valid user and false if not
     */
    private boolean validUser(){
        if(Fname.getText().toString().contains(" ") ||
                Email.getText().toString().contains(" ") ||
                Birth.getText().toString().contains(" ") ||
                Gamertag.getText().toString().contains(" ") ||
                Password.getText().toString().contains(" ") ||
                PasswordConfirm.getText().toString().contains(" ") ||
                Gender.getSelectedItem().toString().contains(" ")){
            Toast noSpaces = Toast.makeText(getApplicationContext(),"No spaces allowed in any field",Toast.LENGTH_SHORT);
            noSpaces.show();
            return false;
        }
        if(!validFormatting())
            return false;
        return true;
    }

    /**
     * This method populates and empty fields with the old users data. This method gets called when a request to update
     * is made. It then ensures that no empty fields are submitted to the server which would result in data loss.
     */
    private void populateEmptyFields(){
        if(Fname.getText().toString().trim().equals(""))
            Fname.setText(oldUser.getName());
        if(Email.getText().toString().trim().equals(""))
            Email.setText(oldUser.getEmail());
        if(Birth.getText().toString().trim().equals(""))
            Birth.setText(oldUser.getDateofbirth());
        if(Gamertag.getText().toString().trim().equals(""))
            Gamertag.setText(oldUser.getGamertag());
        if(Password.getText().toString().trim().equals(""))
            Password.setText(oldUser.getPassword());
        if(PasswordConfirm.getText().toString().trim().equals(""))
            PasswordConfirm.setText(oldUser.getPassword());
    }

    /**
     * Helper method to ensure proper formatting of the data
     * @return true if valid formatting and false if invalid
     */
    private boolean validFormatting(){
        String dob = Birth.getText().toString();
        String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dob);

        Toast diffPassword = Toast.makeText(getApplicationContext(),"Passwords don't match",Toast.LENGTH_SHORT);
        Toast dobFormat = Toast.makeText(getApplicationContext(),"Please put in a valid date in the format dd/mm/yyyy",Toast.LENGTH_SHORT);
        Toast invalidEmail = Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT);

        if(!Password.getText().toString().equals(PasswordConfirm.getText().toString())){
            diffPassword.show();
            return false;
        }
        else if(!matcher.matches()){
            dobFormat.show();
            return false;
        }
        else if(!validEmail(Email.getText().toString())){
            invalidEmail.show();
            return false;
        }

        return true;
    }

    /**
     * Checks for differences between the old data and any new changes
     * @return true if modifications were made and false if not
     */
    private boolean changesMade(){
        if(!Fname.getText().toString().equals(oldUser.getName()))
            return true;
        if(!Email.getText().toString().equals(oldUser.getEmail()))
            return true;
        if(!Birth.getText().toString().equals(oldUser.getDateofbirth()))
            return true;
        if(!Gamertag.getText().toString().equals(oldUser.getGamertag()))
            return true;
        if(!Password.getText().toString().equals(oldUser.getPassword()))
            return true;
        if(!Gender.getSelectedItem().toString().equals(oldUser.getSex()))
            return true;
        if(!Console.getSelectedItem().toString().equals(oldUser.getConsole()))
            return true;
        if(sFortniteSkill.getSelectedItemPosition()!=oldUser.getFortnite())
            return true;
        if(sMinecraftSkill.getSelectedItemPosition()!=oldUser.getMinecraft())
            return true;
        if(sGta5Skill.getSelectedItemPosition()!=oldUser.getGta5())
            return true;
        if(sLeagueOfLegendsSkill.getSelectedItemPosition()!=oldUser.getLeagueoflegends())
            return true;
        if(sRainbowSixSkill.getSelectedItemPosition()!=oldUser.getRainbowsixsiege())
            return true;
        if(sOverwatchSkill.getSelectedItemPosition()!=oldUser.getOverwatch())
            return true;
        if(sPubgSkill.getSelectedItemPosition()!=oldUser.getPubg())
            return true;
        if(sRocketLeagueSkill.getSelectedItemPosition()!=oldUser.getRocketleague())
            return true;
        return false;
    }

    /**
     * This method sends the update request. If it's successful then it saves the new data as the original user,
     * saves the data to the file, and refreshes all the EditTexts with the new data. If it failed, it is either
     * because the new email is already taken or there was an error.
     */
    private void updateProfile(){
        final Toast emailExists = Toast.makeText(getApplicationContext(),"This email is already being used",Toast.LENGTH_SHORT);
        final Toast successfulUpdate = Toast.makeText(getApplicationContext(),"Profile updated successfully",Toast.LENGTH_SHORT);
        final JSONObject update = new JSONObject();
        try{
            update.put("id", oldUser.getId());
            update.put("name",  Fname.getText().toString());
            update.put("dateofbirth",  Birth.getText().toString());
            update.put("sex",  Gender.getSelectedItem().toString());
            update.put("gamertag",  Gamertag.getText().toString());
            update.put("console",  Console.getSelectedItem().toString());
            update.put("password",  PasswordConfirm.getText().toString());
            update.put("fortnite", sFortniteSkill.getSelectedItemPosition());
            update.put("minecraft", sMinecraftSkill.getSelectedItemPosition());
            update.put("gta5", sGta5Skill.getSelectedItemPosition());
            update.put("leagueoflegends", sLeagueOfLegendsSkill.getSelectedItemPosition());
            update.put("rainbowsixsiege", sRainbowSixSkill.getSelectedItemPosition());
            update.put("overwatch", sOverwatchSkill.getSelectedItemPosition());
            update.put("pubg", sPubgSkill.getSelectedItemPosition());
            update.put("rocketleague", sRocketLeagueSkill.getSelectedItemPosition());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, update,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getBoolean("b")==true){
                                successfulUpdate.show();
                                transferNewToOld(update);
                                storeUserAsJsonString();
                                try{
                                    update.put("email",  Email.getText().toString());
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                refreshFields();
                            }
                            else{
                                emailExists.show();
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
     * This method loads all data stored in oldUser to the EditTexts
     */
    public void refreshFields(){
        Fname.setText(oldUser.getName());
        Email.setText(oldUser.getEmail());
        Birth.setText(oldUser.getDateofbirth());
        Gamertag.setText(oldUser.getGamertag());
        Password.setText(oldUser.getPassword());
        PasswordConfirm.setText(oldUser.getPassword());
        sFortniteSkill.setSelection(oldUser.getFortnite());
        sMinecraftSkill.setSelection(oldUser.getMinecraft());
        sGta5Skill.setSelection(oldUser.getGta5());
        sLeagueOfLegendsSkill.setSelection(oldUser.getLeagueoflegends());
        sRainbowSixSkill.setSelection(oldUser.getRainbowsixsiege());
        sOverwatchSkill.setSelection(oldUser.getOverwatch());
        sPubgSkill.setSelection(oldUser.getPubg());
        sRocketLeagueSkill.setSelection(oldUser.getRocketleague());

        if(oldUser.getSex().equals("Male")){
            Gender.setSelection(0);
        }
        else if(oldUser.getSex().equals("Female")){
            Gender.setSelection(1);
        }
        else if(oldUser.getSex().equals("Other")){
            Gender.setSelection(2);
        }

        if(oldUser.getConsole().equals("Ps4")){
            Console.setSelection(0);
        }
        else if(oldUser.getConsole().equals("Xbox One")){
            Console.setSelection(1);
        }
        else if(oldUser.getConsole().equals("PC")){
            Console.setSelection(2);
        }
    }

    /**
     * This method assigns all oldUser values to the newData fields. This only happens after a successful update
     * @param newData The new user data to be used
     */
    public void transferNewToOld(JSONObject newData){
        try {
            oldUser.setName(newData.get("name").toString());
            oldUser.setDateofbirth(newData.get("dateofbirth").toString());
            oldUser.setSex(newData.get("sex").toString());
            oldUser.setGamertag(newData.get("gamertag").toString());
            oldUser.setConsole(newData.get("console").toString());
            oldUser.setEmail(newData.get("email").toString());
            oldUser.setPassword(newData.get("password").toString());
            oldUser.setFortnite(newData.getInt("fortnite"));
            oldUser.setMinecraft(newData.getInt("minecraft"));
            oldUser.setGta5(newData.getInt("gta5"));
            oldUser.setLeagueoflegends(newData.getInt("leagueoflegends"));
            oldUser.setRainbowsixsiege(newData.getInt("rainbowsixsiege"));
            oldUser.setOverwatch(newData.getInt("overwatch"));
            oldUser.setPubg(newData.getInt("pubg"));
            oldUser.setRocketleague(newData.getInt("rocketleague"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Helper method that stores the user to a file in Internal storage as a JSON
     */
    public void storeUserAsJsonString(){
        try {
            FileOutputStream fileout = openFileOutput("userData.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            //outputWriter.write(getIntent().getStringExtra("user"));
            outputWriter.write(oldUser.toJsonString());
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                userImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ivEditPIC.setImageBitmap(userImage);
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
        String url2= "http://192.168.1.120:8080/uploadFile";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url2,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        saveToInternalStorage(bitmap);
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",""+oldUser.getId());
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

    private void loadProfilePicFromStorage(){
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "profilepic.png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ivEditPIC.setImageBitmap(b);
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


}
