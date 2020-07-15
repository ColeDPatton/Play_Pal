package com.example.p19;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is the initial activity the user encounters. From here they either go to create an account or login
 */
public class MainActivity extends AppCompatActivity {

    Button bLogin, bCreateAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        bLogin = findViewById(R.id.bLogin);
        bCreateAcc = findViewById(R.id.bCreateAcc);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionsEnabled()) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                    mBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                            startActivity(intent);
                        }
                    });

                    mBuilder.setMessage("Please enable permissions to access external storage on the next screen by selecting Permissions > Storage");
                    mBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }) ;
                    AlertDialog alert = mBuilder.create();
                    alert.setTitle("Allow Permissions");
                    alert.show();
                    alert.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0x6a6a6a));
                    return;
                }
                else{
                    Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * This is a helper method that indicates whether or not a user has permitted use of the External Storage by our app.
     * @return true if permission has been granted and false if it has not
     */
    public boolean permissionsEnabled(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED);
    }



}
