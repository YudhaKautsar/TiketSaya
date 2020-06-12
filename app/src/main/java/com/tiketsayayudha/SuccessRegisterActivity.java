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

 public class SuccessRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        //register Animation
        Animation ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        Animation btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        Animation appSplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        //register id element
        ImageView iconSuccess = findViewById(R.id.icon_success);
        TextView appTitle = findViewById(R.id.app_title);
        TextView appSubtitle = findViewById(R.id.app_subtitle);
        Button btnExplore = findViewById(R.id.btn_explore);

        //start Animation
        iconSuccess.startAnimation(appSplash);
        appTitle.startAnimation(ttb);
        appSubtitle.startAnimation(ttb);
        btnExplore.startAnimation(btt);

        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotohome = new Intent(SuccessRegisterActivity.this, HomeActivity.class);
                startActivity(gotohome);

            }
        });
    }
}