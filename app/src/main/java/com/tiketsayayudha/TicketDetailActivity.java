package com.tiketsayayudha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class TicketDetailActivity extends AppCompatActivity {

    TextView namaWisata, locationTicket, spotPhotos, spotsWifi, spotsFestival, placeDesc;
    ImageView headerTicketDetail;
    Button btnBuyTicket;
    LinearLayout btnBack;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        namaWisata = findViewById(R.id.nama_wisata);
        locationTicket = findViewById(R.id.location_ticket);
        spotPhotos = findViewById(R.id.spot_photos);
        spotsWifi = findViewById(R.id.spots_wifi);
        spotsFestival = findViewById(R.id.spots_festival);
        placeDesc = findViewById(R.id.place_desc);
        headerTicketDetail = findViewById(R.id.header_ticket_detail);
        btnBack = findViewById(R.id.btn_back);
        btnBuyTicket = findViewById(R.id.btn_buy_ticket);

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

       // Toast.makeText(getApplicationContext(), "Jenis Tiket" + bundle, Toast.LENGTH_SHORT).show();

        assert jenis_tiket_baru != null;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan yang baru
                namaWisata.setText(Objects.requireNonNull(dataSnapshot.child("nama_wisata").getValue()).toString());
                locationTicket.setText(Objects.requireNonNull(dataSnapshot.child("lokasi").getValue()).toString());
                spotPhotos.setText(Objects.requireNonNull(dataSnapshot.child("is_photo_spot").getValue()).toString());
                spotsWifi.setText(Objects.requireNonNull(dataSnapshot.child("is_wifi").getValue()).toString());
                spotsFestival.setText(Objects.requireNonNull(dataSnapshot.child("is_festival").getValue()).toString());
                placeDesc.setText(Objects.requireNonNull(dataSnapshot.child("short_desc").getValue()).toString());
                Picasso.with(TicketDetailActivity.this)
                        .load(Objects.requireNonNull(dataSnapshot.child("url_thumbnail").getValue())
                                .toString()).centerCrop().fit()
                        .into(headerTicketDetail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoback = new Intent(TicketDetailActivity.this, HomeActivity.class);
                startActivity(gotoback);
            }
        });

        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotocheckout = new Intent(TicketDetailActivity.this, TicketCheckOutActivity.class);
                gotocheckout.putExtra("jenis_tiket", jenis_tiket_baru);
                startActivity(gotocheckout);

            }
        });
    }
}