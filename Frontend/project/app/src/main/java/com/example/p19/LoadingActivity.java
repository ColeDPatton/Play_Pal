package com.example.p19;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadingActivity extends AppCompatActivity {

    static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        checkForSavedUser();
    }

    private void checkForSavedUser(){
        File file = new File(getApplicationContext().getFilesDir(),"userData.txt");
        if(file.exists()){
            System.out.println("THE FILE EXISTS");
        }
        else{
            System.out.println("THE FILE DOESNT EXIST");
        }
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
            User loggedInUser = new User(s);
            if(!(loggedInUser.getSearchPref() == 1 || loggedInUser.getSearchPref() == 2 || loggedInUser.getSearchPref() == 3))
                loggedInUser.setSearchPref(1);
            Intent intent = new Intent(LoadingActivity.this, BufferActivity.class);
            intent.putExtra("user", loggedInUser.toJsonString());
            startActivity(intent);

        } catch (FileNotFoundException e) {
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            startActivity(intent);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
