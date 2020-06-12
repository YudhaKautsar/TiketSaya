package com.tiketsayayudha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    TextView fullName, userName, userBalance;
    CircleImageView userPhoto;

    DatabaseReference databaseReference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        LinearLayout btn_ticket_pisa = findViewById(R.id.btn_ticket_pisa);
        LinearLayout btn_ticket_torri = findViewById(R.id.btn_ticket_torri);
        LinearLayout btn_ticket_pagoda = findViewById(R.id.btn_ticket_pagoda);
        LinearLayout btn_ticket_candi = findViewById(R.id.btn_ticket_candi);
        LinearLayout btn_ticket_sphinx = findViewById(R.id.btn_ticket_sphinx);
        LinearLayout btn_ticket_monas = findViewById(R.id.btn_ticket_monas);
        userPhoto = findViewById(R.id.user_photo);
        fullName = findViewById(R.id.fullname);
        userName = findViewById(R.id.username);
        userBalance = findViewById(R.id.user_balance);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullName.setText(Objects.requireNonNull(dataSnapshot.child("nama_lengkap").getValue()).toString());
                userName.setText("@" + (Objects.requireNonNull(dataSnapshot.child("username").getValue())).toString());
                userBalance.setText("US$ " + (Objects.requireNonNull(dataSnapshot.child("user_balance").getValue())).toString());
                Picasso.with(HomeActivity.this)
                        .load(Objects.requireNonNull(dataSnapshot.child("url_photo_profile").getValue())
                                .toString()).centerCrop().fit()
                        .into(userPhoto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toprofile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(toprofile);

            }
        });

        btn_ticket_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gototicketdetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                gototicketdetail.putExtra("jenis_tiket", "Pisa");
                startActivity(gototicketdetail);

            }
        });

        btn_ticket_torri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gototicketdetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                gototicketdetail.putExtra("jenis_tiket", "Torri");
                startActivity(gototicketdetail);

            }
        });

        btn_ticket_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gototicketdetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                gototicketdetail.putExtra("jenis_tiket", "Pagoda");
                startActivity(gototicketdetail);

            }
        });

        btn_ticket_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gototicketdetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                gototicketdetail.putExtra("jenis_tiket", "Candi");
                startActivity(gototicketdetail);

            }
        });

        btn_ticket_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gototicketdetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                gototicketdetail.putExtra("jenis_tiket", "Sphinx");
                startActivity(gototicketdetail);

            }
        });

        btn_ticket_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gototicketdetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                gototicketdetail.putExtra("jenis_tiket", "Monas");
                startActivity(gototicketdetail);

            }
        });

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}