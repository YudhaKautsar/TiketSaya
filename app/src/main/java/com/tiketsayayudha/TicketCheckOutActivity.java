package com.tiketsayayudha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Objects;
import java.util.Random;

public class TicketCheckOutActivity extends AppCompatActivity {

    Integer valueJumlahTiket = 1, myBalance = 0, valueTotalHarga = 0, valueHargaTiket = 0, sisa_balance = 0;
    TextView tvJumlahTiket, tvMyBalance, tvTotalHarga, namaWisata, locationTicket, ketentuan;
    ImageView noticeUang;
    Button btnMinus, btnPlus, btnPay;
    LinearLayout btnBack;
    //Generate nomer secara random
    Integer nomer_transaksi = new Random().nextInt();

    DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String dateWisata = "";
    String timeWisata = "";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_check_out);

        getUsernaemLocal();

        //mengambil data dari intent
        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //register id elemenet
        btnMinus = findViewById(R.id.btn_minus);
        tvJumlahTiket = findViewById(R.id.tv_jumlah_ticket);
        tvTotalHarga = findViewById(R.id.tv_total_harga);
        tvMyBalance = findViewById(R.id.tv_my_balance);
        noticeUang = findViewById(R.id.notice_uang);
        namaWisata = findViewById(R.id.nama_wisata);
        locationTicket = findViewById(R.id.location_ticket);
        ketentuan = findViewById(R.id.ketentuan);
        btnPlus = findViewById(R.id.btn_plus);
        btnBack = findViewById(R.id.btn_back);
        btnPay = findViewById(R.id.btn_pay);

        //setting baru beberapa komponen
        tvJumlahTiket.setText(valueJumlahTiket.toString());
        tvMyBalance.setText("US$ "+ myBalance + "");
        valueTotalHarga = valueHargaTiket * valueJumlahTiket;
        tvTotalHarga.setText("US$ " + valueTotalHarga + "");
        btnMinus.animate().alpha(0).setDuration(200).start();
        btnMinus.setEnabled(false);
        noticeUang.setVisibility(View.GONE);

        //mengambil data user dari firebase
        tvMyBalance.setText("US$ "+ myBalance + "");
        databaseReference2 =FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myBalance = Integer.valueOf(Objects.requireNonNull(dataSnapshot.child("user_balance").getValue()).toString());
                tvMyBalance.setText("US$ "+ myBalance + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        assert jenis_tiket_baru != null;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan yang baru
                namaWisata.setText(Objects.requireNonNull(dataSnapshot.child("nama_wisata").getValue()).toString());
                locationTicket.setText(Objects.requireNonNull(dataSnapshot.child("lokasi").getValue()).toString());
                ketentuan.setText(Objects.requireNonNull(dataSnapshot.child("ketentuan").getValue()).toString());
                dateWisata = (Objects.requireNonNull(dataSnapshot.child("date_wisata").getValue())).toString();
                timeWisata = (Objects.requireNonNull(dataSnapshot.child("time_wisata").getValue()).toString());
                valueHargaTiket = Integer.valueOf(Objects.requireNonNull(dataSnapshot.child("harga_tiket").getValue()).toString());
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                tvTotalHarga.setText("US$ " + valueTotalHarga + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTiket-=1;
                tvJumlahTiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket < 2) {
                    btnMinus.animate().alpha(0).setDuration(200).start();
                    btnMinus.setEnabled(false);
                }
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                tvTotalHarga.setText("US$ " + valueTotalHarga + "");
                if (valueTotalHarga < myBalance) {
                    btnPay.animate().translationY(0).alpha(1).setDuration(300);
                    btnPay.setEnabled(true);
                    tvMyBalance.setTextColor(Color.parseColor("#203DD1"));
                    noticeUang.setVisibility(View.GONE);
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                valueJumlahTiket+=1;
                tvJumlahTiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket > 1) {
                    btnMinus.animate().alpha(1).setDuration(200).start();
                    btnMinus.setEnabled(true);
                }
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                tvTotalHarga.setText("US$ " + valueTotalHarga + "");
                if (valueTotalHarga > myBalance) {
                    btnPay.animate().translationY(250).alpha(0).setDuration(300);
                    btnPay.setEnabled(false);
                    tvMyBalance.setTextColor(Color.parseColor("#D1206B"));
                    noticeUang.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoback = new Intent(TicketCheckOutActivity.this, TicketDetailActivity.class);
                startActivity(gotoback);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //menyimpan data user ke firebase dan membuat table baru "MyTickets"
                databaseReference3 =FirebaseDatabase.getInstance()
                        .getReference().child("MyTickets")
                        .child(username_key_new).child(namaWisata.getText().toString() + nomer_transaksi);
                databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        databaseReference3.getRef().child("id_ticket").setValue(namaWisata.getText().toString() + nomer_transaksi);
                        databaseReference3.getRef().child("nama_wisata").setValue(namaWisata.getText().toString());
                        databaseReference3.getRef().child("lokasi").setValue(locationTicket.getText().toString());
                        databaseReference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        databaseReference3.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());
                        databaseReference3.getRef().child("date_wisata").setValue(dateWisata);
                        databaseReference3.getRef().child("time_wisataa").setValue(timeWisata);

                        Intent gotosuccesbuy = new Intent(TicketCheckOutActivity.this, SuccessBuyTicketActivity.class);
                        startActivity(gotosuccesbuy);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = myBalance - valueHargaTiket;
                        databaseReference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void getUsernaemLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}