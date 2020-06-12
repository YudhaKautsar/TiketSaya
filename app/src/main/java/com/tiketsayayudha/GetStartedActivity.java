package com.tiketsayayudha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //register Animation
        Animation btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        Animation ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        //register id element
        ImageView emblemApp = findViewById(R.id.emblem_app);
        TextView appTagline2 = findViewById(R.id.app_tagline2);
        Button btn_sign_in = findViewById(R.id.btn_sign_in);
        Button btn_new_account = findViewById(R.id.btn_new_account);

        //run Animation
        emblemApp.startAnimation(ttb);
        appTagline2.startAnimation(ttb);
        btn_sign_in.startAnimation(btt);
        btn_new_account.startAnimation(btt);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosignin = new Intent(GetStartedActivity.this, SignInActivity.class);
                startActivity(gotosignin);

            }
        });

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregister = new Intent(GetStartedActivity.this, RegisterActivity.class);
                startActivity(gotoregister);

            }
        });

    }
}