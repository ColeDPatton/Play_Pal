package com.example.p19;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class is used to allow communication between two users via a websocket
 */
public class MessagingActivity extends AppCompatActivity {
    static final int READ_BLOCK_SIZE = 100;
    private RequestQueue mQueue;
    ImageButton bSendMessage,bReportUser,bBack;
    EditText e1,e2,etMessage;
    TextView tvChat,tvUserName, tvGamertag;
    User currentUser, tempUser;
    private ImageView ivOtherUserPic;
    private Bitmap otherPic;

    private WebSocketClient cc;
    //TODO - find way to access each individual IP address and need to append unique userID to the end
    String uri = "http://192.168.1.120:8080/chat/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_messaging);

        //load current user and temp user(the user that the message is getting sent to)
        tempUser = new User(getIntent().getStringExtra("user"));
        loadCurrentUser();

        tvChat = findViewById(R.id.tvChat);
        tvUserName = findViewById(R.id.tvUserName);

        //checking all data loads correctly
        //String testing = tempUser.toString() + "\n" + currentUser.toString();
        //tvChat.setText(testing);

        tvGamertag = findViewById(R.id.tvGamertag);
        tvGamertag.setText(tempUser.getGamertag());
        bSendMessage = findViewById(R.id.bSend);
        bBack = findViewById(R.id.bBack);
        bReportUser = findViewById(R.id.bReportUser);
        etMessage = findViewById(R.id.etMessage);
        tvUserName.setText(tempUser.getName());






        initWebsocket();


        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessagingActivity.this, OtherUserProfileActivity.class);
                Log.i("user",tempUser.toString());
                intent.putExtra("otherUser", tempUser.toJsonString());
                startActivity(intent);
            }
        });


        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                sendMessage(message);
            }
        });

        bReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessagingActivity.this, ReportUserActivity.class);
                //intent.putExtra("reportedid", tempUser.getId());
               // intent.putExtra("reporterid", currentUser.getId());
                intent.putExtra("tempUser",tempUser.toJsonString());
                intent.putExtra("currentUser",currentUser.toJsonString());

                intent.putExtra("reportedname", tempUser.getName());
                startActivity(intent);
            }
        });

    }

    /**
     * This method initializes the websocket and allows for communication between two users
     */
    public void initWebsocket(){
        Draft[] drafts = {new Draft_6455()};

            /**
             * If running this on an android device, make sure it is on the same network as your
             * computer, and change the ip address to that of your computer.
             * If running on the emulator, you can use http://192.168.1.120.
             */
            try {
                Log.d("Socket:", "Trying socket");
                cc = new WebSocketClient(new URI(uri + currentUser.getId() + "/" + tempUser.getId()), drafts[0]) {
                    @Override
                    public void onMessage(String message) {
                        Log.d("", "run() returned: " + message);
                        tvChat.append("\n" + message);
                    }

                    @Override
                    public void onOpen(ServerHandshake handshake) {
                        Log.d("OPEN", "run() returned: " + "is connecting");
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.d("CLOSE", "onClose() returned: " + reason);
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Log.d("Exception:", e.toString());
                    }
                };
            }
            catch (URISyntaxException e) {
                Log.d("Exception:", e.getMessage());
                e.printStackTrace();
            }
            cc.connect();

    }

    /**
     * Sends a message through the websocket
     * @param message The message to be sent
     */
    public void sendMessage(String message){
        //send message
        try{
            cc.send(currentUser.getName()+ ": " +message);
            etMessage.setText("");
        }
        catch (WebsocketNotConnectedException e){
            System.out.println("\n\n\n\n\n Not Connected To Websocket \n\n\n\n\n");
            e.printStackTrace();
        }

    }

    /**
     * This method loads the current user from the file in Internal Storage
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

    public void downloadImage(final ImageView mImageView, long id, String picname){

        CustomImageRequest sr = new CustomImageRequest("http://192.168.1.120:8080/downloadFileFromGame?id="+id+"&game="+picname,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        otherPic = response;
                        mImageView.setImageBitmap(response);

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

