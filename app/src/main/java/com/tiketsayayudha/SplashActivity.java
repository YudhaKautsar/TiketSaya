package com.tiketsayayudha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation app_splash, btt;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //load Animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        //load id element
        ImageView appLogo = findViewById(R.id.app_logo);
        TextView appTagline = findViewById(R.id.app_tagline);

        //run Animation
        appLogo.startAnimation(app_splash);
        appTagline.startAnimation(btt) ;

        getUsernameLocal();

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
        Handler handler = new Handler();
        if (username_key_new.isEmpty()){
            //SETTING TIMER
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //PINDAH ACTIVTY KE GET STARTED
                    Intent gotogetstarted = new Intent(SplashActivity.this, GetStartedActivity.class);
                    startActivity(gotogetstarted);
                    finish();

                }
            }, 2000);
        }
        else {
            //SETTING TIMER
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //PINDAH ACTIVTY KE HOME
                    Intent gotohome = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(gotohome);
                    finish();

                }
            }, 2000);
        }
    }

}