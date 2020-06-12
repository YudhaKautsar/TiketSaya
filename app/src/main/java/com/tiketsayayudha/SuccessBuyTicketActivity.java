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

public class SuccessBuyTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        //register Animation
        Animation ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        Animation btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        Animation appSplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        //register id element
        ImageView iconSuccessTicket = findViewById(R.id.icon_success_ticket);
        TextView successTitle = findViewById(R.id.success_title);
        TextView successSubtitle = findViewById(R.id.success_subtitle);
        Button btnViewTicket = findViewById(R.id.btn_view_ticket);
        Button btnDashboard = findViewById(R.id.btn_dashboard);

        //run animation
        iconSuccessTicket.startAnimation(appSplash);
        successTitle.startAnimation(ttb);
        successSubtitle.startAnimation(ttb);
        btnViewTicket.startAnimation(btt);
        btnDashboard.startAnimation(btt);

        btnViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(SuccessBuyTicketActivity.this, ProfileActivity.class);
                startActivity(gotoprofile);
            }
        });

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(SuccessBuyTicketActivity.this, HomeActivity.class);
                startActivity(gotohome);
            }
        });

    }
}