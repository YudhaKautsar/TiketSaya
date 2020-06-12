package com.tiketsayayudha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    EditText fullname, username, emailAddress, password, userNumber;
    Button btnAddPicture, btn_register;
    CircleImageView photoUser;
    LinearLayout btn_back;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    Uri photo_location;
    Integer photo_max = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //register id
        fullname = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        emailAddress = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        userNumber = findViewById(R.id.user_number);
        btn_back = findViewById(R.id.btn_back);
        btn_register = findViewById(R.id.btn_register);
        btnAddPicture = findViewById(R.id.btn_add_picture);
        photoUser = findViewById(R.id.photo_user);

        btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findPhoto();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivity(goback);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
                btn_register.setEnabled(false);
                btn_register.setText("Loading ...");

                //menyimapan data kepada local storage (handphone)
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, username.getText().toString());
                editor.apply();

                //simpan ke database
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username.getText().toString());
                storageReference = FirebaseStorage.getInstance().getReference().child("photo_users").child(username.getText().toString());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("nama_lengkap").setValue(fullname.getText().toString());
                        dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(emailAddress.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                        dataSnapshot.getRef().child("number_telephone").setValue(userNumber.getText().toString());
                        dataSnapshot.getRef().child("user_balance").setValue(2000);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent gotosuccessreg = new Intent(RegisterActivity.this, SuccessRegisterActivity.class);
                startActivity(gotosuccessreg);

                if (photo_location != null) {
                    final StorageReference storageReference1 = storageReference
                            .child(System.currentTimeMillis()
                                    + "." + getFileExtension(photo_location));

                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    databaseReference.getRef().child("url_photo_profile").setValue(uri.toString());

                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Intent gotosuccessreg = new Intent(RegisterActivity.this, SuccessRegisterActivity.class);
                                    startActivity(gotosuccessreg);
                                }
                            });

                        }
                    });
                }else {
                    Intent gotosuccessregis = new Intent(RegisterActivity.this, SuccessRegisterActivity.class);
                    startActivity(gotosuccessregis);
                }
                
            }
        });
    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            photo_location = data.getData();
            Picasso.with(this)
                    .load(photo_location)
                    .centerCrop()
                    .fit()
                    .into(photoUser);

        }

    }
}