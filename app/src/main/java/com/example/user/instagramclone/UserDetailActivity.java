package com.example.user.instagramclone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private TextView userName1;
    private DatabaseReference mDatabaseRef1;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private List<FriendsName> friendsNames;
    private String userName;
    private ImageView backgroundImage;
    private ImageView userImage;
    private Uri mUri;
    private List<AllImages> mUpload;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("_WriteExternalStorage", "tue");
            }
            if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                takePhoto();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        backgroundImage = findViewById(R.id.backgroundImage);
        userImage = findViewById(R.id.userImage);

        userName1 = findViewById(R.id.userName1);

        mAuth = FirebaseAuth.getInstance();
        mUpload = new ArrayList<>();
        friendsNames = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userProfileImages").child(mAuth.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference("userProfileImages").child(mAuth.getUid());

        //for user name.....

        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("userName").child(mAuth.getUid());
        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                friendsNames.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    FriendsName name = ds.getValue(FriendsName.class);
                    friendsNames.add(name);

                    userName = name.getName();

                    userName1.setText(userName);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // for profile picture..

        databaseReference = FirebaseDatabase.getInstance().getReference("userProfileImages").child(mAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUpload.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    AllImages allImages = ds.getValue(AllImages.class);

                   // Log.i("_All",allImages.toString());

                    mUpload.add(allImages);

                    Picasso.with(UserDetailActivity.this)
                            .load(allImages.getmImageUri())
                            .placeholder(R.drawable.ic_account_circle_black_24dp)
                            .fit()
                            .into(userImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       /* Picasso.with(UserDetailActivity.this)
                .load(UserFeedActivity.uri)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .fit()
                .into(userImage);
                */

        getSupportActionBar().hide();

        // Window w = getWindow();

        // w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }


    public void editProfile(View view) {

        if (Build.VERSION.SDK_INT > 23) {

            if (((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) &&
                    (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            } else {

                takePhoto();
            }

        } else {
            takePhoto();
        }


    }

    public void takePhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 7);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK && data != null) {

            mUri = data.getData();
           // userImage.setImageURI(mUri);
            uploadImage();
        }
    }



    private String getFileExtension(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return (mime.getExtensionFromMimeType(cr.getType(uri)));
    }


    private void uploadImage() {

        if (mUri != null) {

            StorageReference fileStorageRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mUri));

            fileStorageRef.putFile(mUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(UserDetailActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                            AllImages friendsName = new AllImages(
                                    taskSnapshot.getDownloadUrl().toString(),userName);

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(friendsName);
                            //  Log.i("_upId",uploadId);
                            // Log.i("_uri",friendsName.toString());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(UserDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        } else {

            Toast.makeText(getApplicationContext(), "Please Choose a photo", Toast.LENGTH_SHORT).show();
        }
    }




}
