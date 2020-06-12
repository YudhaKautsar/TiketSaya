package com.tiketsayayudha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    EditText xfullname, xusername, xemailAddress, xpassword, xuserNumber;
    CircleImageView photoUserEdit;
    Button btnAddPicture, btnSave;
    LinearLayout btnBack;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    Uri photo_location;
    Integer photo_max = 1;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUsernameLocal();

        xfullname = findViewById(R.id.fullname);
        xusername = findViewById(R.id.username);
        xemailAddress = findViewById(R.id.email_address);
        xpassword = findViewById(R.id.password);
        xuserNumber = findViewById(R.id.user_number);
        photoUserEdit = findViewById(R.id.photo_user_edit);
        btnAddPicture = findViewById(R.id.btn_add_picture);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        storageReference = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                xfullname.setText(Objects.requireNonNull(dataSnapshot.child("nama_lengkap").getValue()).toString());
                xusername.setText(Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString());
                xemailAddress.setText(Objects.requireNonNull(dataSnapshot.child("email_address").getValue()).toString());
                xpassword.setText(Objects.requireNonNull(dataSnapshot.child("password").getValue()).toString());
                xuserNumber.setText(Objects.requireNonNull(dataSnapshot.child("number_telephone").getValue()).toString());
                Picasso.with(EditProfileActivity.this)
                        .load(Objects.requireNonNull(dataSnapshot.child("url_photo_profile")
                                .getValue()).toString()).centerCrop().fit()
                        .into(photoUserEdit);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                btnSave.setEnabled(false);
                btnSave.setText("Loading ...");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("nama_lengkap").setValue(xfullname.getText().toString());
                        dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(xemailAddress.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                        dataSnapshot.getRef().child("number_telephone").setValue(xuserNumber.getText().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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

                                    Intent gotosuccessreg = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                    startActivity(gotosuccessreg);
                                }
                            });

                        }
                    });
                }else {
                    Intent gotosuccessregis = new Intent(EditProfileActivity.this, ProfileActivity.class);
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
                    .into(photoUserEdit);

        }

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
