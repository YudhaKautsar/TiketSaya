package com.tiketsayayudha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView fullName, userNumber;
    CircleImageView photoProfile;
    ImageView btnBack;
    RecyclerView rvMyTickets;
    ArrayList<MyTicket> myTicketArrayList;
    TicketAdapter ticketAdapter;

    DatabaseReference databaseReference, databaseReference2;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getUsernameLocal();

        fullName = findViewById(R.id.fullname);
        userNumber = findViewById(R.id.user_number);
        photoProfile = findViewById(R.id.photo_profile);
        btnBack = findViewById(R.id.btn_back);
        Button btn_edit_profile = findViewById(R.id.btn_edit_profile);
        Button btnSignOut = findViewById(R.id.btn_sign_out);
        rvMyTickets = findViewById(R.id.rv_mytickets);

        rvMyTickets.setLayoutManager(new LinearLayoutManager(this));
        myTicketArrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullName.setText(Objects.requireNonNull(dataSnapshot.child("nama_lengkap").getValue()).toString());
                userNumber.setText(Objects.requireNonNull(dataSnapshot.child("number_telephone").getValue()).toString());
                Picasso.with(ProfileActivity.this)
                        .load(Objects.requireNonNull(dataSnapshot.child("url_photo_profile").getValue())
                                .toString()).centerCrop().fit()
                        .into(photoProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(gotohome);
            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotoedit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(gotoedit);

            }
        });

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MyTicket myTicket = dataSnapshot1.getValue(MyTicket.class);
                    myTicketArrayList.add(myTicket);
                }
                ticketAdapter = new TicketAdapter(ProfileActivity.this, myTicketArrayList);
                rvMyTickets.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mengapus isi / nilai / value dari username lokal
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                //pindah activity
                Intent gotosignin = new Intent(ProfileActivity.this, SignInActivity.class);
                startActivity(gotosignin);
                finish();


            }
        });

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}