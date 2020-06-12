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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    Button btn_sign_in;

    DatabaseReference databaseReference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final EditText xusername = findViewById(R.id.xusername);
        final EditText xpassword = findViewById(R.id.xpassword);
        TextView btn_create_account = findViewById(R.id.btn_create_account);
        btn_sign_in = findViewById(R.id.btn_sign_in);


        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregister = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(gotoregister);

            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading ...");

                String username = xusername.getText().toString();
                final String password = xpassword .getText().toString();

               if (username.isEmpty()) {
                   Toast.makeText(getApplicationContext(), "Username kosong", Toast.LENGTH_SHORT).show();
                   btn_sign_in.setEnabled(true);
                   btn_sign_in.setText("Sign In");
               }else if(password.isEmpty()){
                   Toast.makeText(getApplicationContext(), "Password kosong", Toast.LENGTH_SHORT).show();
                   btn_sign_in.setEnabled(true);
                   btn_sign_in.setText("Sign In");
               }else{
                   databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(xusername.getText().toString());

                   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.exists()) {

                               //ambil data dari firebase
                               String passwordFromFirebase =  Objects.requireNonNull(dataSnapshot.child("password").getValue()).toString();

                               //validasi password dengan password firebase
                               if (password.equals(passwordFromFirebase)) {

                                   //simpan username key ke local
                                   SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                   @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString(username_key, xusername.getText().toString());
                                   editor.apply();

                                   Intent gotohome = new Intent(SignInActivity.this, HomeActivity.class);
                                   startActivity(gotohome);

                               }
                               else {
                                   Toast.makeText(getApplicationContext(), "Password salah", Toast.LENGTH_SHORT).show();
                                   btn_sign_in.setEnabled(true);
                                   btn_sign_in.setText("Sign In");
                               }
                           }
                           else {
                               Toast.makeText(getApplicationContext(), "Username belum terdaftar", Toast.LENGTH_SHORT).show();
                               btn_sign_in.setEnabled(true);
                               btn_sign_in.setText("Sign In");
                           }
                       }


                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               }
            }
        });

    }
}