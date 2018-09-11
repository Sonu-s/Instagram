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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button uploadButtonId;
    private Uri mUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef1;
    private FirebaseAuth mAuth;
    // private ImageAdapter mAuthName;
    private String userName;

    private List<FriendsName> friendsNames;


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
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        uploadButtonId = findViewById(R.id.uploadButtonId);

        mAuth = FirebaseAuth.getInstance();

        friendsNames = new ArrayList<>();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userNameImages").child(mAuth.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference("userNameImages").child(mAuth.getUid());

        if (Build.VERSION.SDK_INT > 23) {

            if (((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) &&
                    (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            } else {

                takePhoto();
            }

        } else {
            takePhoto();
        }

        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("userName").child(mAuth.getUid());
        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                friendsNames.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    FriendsName name = ds.getValue(FriendsName.class);
                    friendsNames.add(name);

                    userName = name.getName();
                    // Log.i("_NAme",userName);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadButton(View view) {

        uploadImage();
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
        finish();
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
            imageView.setImageURI(mUri);
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

                            Toast.makeText(CameraActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                            AllImages friendsName = new AllImages(
                                    taskSnapshot.getDownloadUrl().toString(), userName);

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(friendsName);
                            //  Log.i("_upId",uploadId);
                            // Log.i("_uri",friendsName.toString());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(CameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        } else {

            Toast.makeText(getApplicationContext(), "Please Choose a photo", Toast.LENGTH_SHORT).show();
        }
    }
}
