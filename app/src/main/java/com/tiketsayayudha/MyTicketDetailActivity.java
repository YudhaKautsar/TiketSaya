package com.tiketsayayudha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.Objects;

public class MyTicketDetailActivity extends AppCompatActivity {

    TextView xTicketName, xTicketLocation, xDateTicket, xTimeTicket, xKetentuan;
    LinearLayout btnBack;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        //mengambil data dari intent
        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        //register id
        xTicketName = findViewById(R.id.ticket_name);
        xTicketLocation = findViewById(R.id.ticket_location);
        xDateTicket = findViewById(R.id.ticket_date);
        xTimeTicket = findViewById(R.id.ticket_time);
        xKetentuan = findViewById(R.id.ketentuan);
        btnBack = findViewById(R.id.btn_back);

        assert nama_wisata_baru != null;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xTicketName.setText(Objects.requireNonNull(dataSnapshot.child("nama_wisata").getValue()).toString());
                xTicketLocation.setText(Objects.requireNonNull(dataSnapshot.child("lokasi").getValue()).toString());
                xDateTicket.setText(Objects.requireNonNull(dataSnapshot.child("date_wisata").getValue()).toString());
                xTimeTicket.setText(Objects.requireNonNull(dataSnapshot.child("time_wisata").getValue()).toString());
                xKetentuan.setText(Objects.requireNonNull(dataSnapshot.child("ketentuan").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(MyTicketDetailActivity.this, ProfileActivity.class);
                startActivity(gotoprofile);
            }
        });
    }
}