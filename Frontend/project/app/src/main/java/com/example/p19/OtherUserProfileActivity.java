package com.example.p19;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;


public class OtherUserProfileActivity extends AppCompatActivity {

    private ImageView ivOtherUser;
    private TextView tvName,tvGT,tvAge,tvGender;
   private User tempUser;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        ivOtherUser = findViewById(R.id.ivOtherUserPICC);
        tvName = findViewById(R.id.tvOtherUserName);
        tvGT = findViewById(R.id.tvOtherUserGT);
        tvGender = findViewById(R.id.tvOtherUserGender);
        tvAge = findViewById(R.id.tvOtherUserAge);
        Intent stuffFromMessageing = getIntent();
//Log.i("intent",stuffFromMessageing.getStringExtra("otherUser"));
     tempUser = new User(getIntent().getStringExtra("otherUser"));
        downloadImage(ivOtherUser, tempUser.getId(), "profilepic");






        tvName.setText(tempUser.getName());
        tvGT.setText(tempUser.getGamertag());
        tvGender.setText(tempUser.getSex());
        tvAge.setText(SwipeActivity.calculateAge(tempUser.getDateofbirth()));

    }

    public void downloadImage(final ImageView mImageView, long id, String picname){

        CustomImageRequest sr = new CustomImageRequest("http://192.168.1.120:8080/downloadFileFromGame?id="+id+"&game="+picname,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
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
