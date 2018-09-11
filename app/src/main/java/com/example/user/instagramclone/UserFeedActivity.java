package com.example.user.instagramclone;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    private ListView listView;
    private FeedAdapter adapter;
    private DatabaseReference mDatabaseRef;
    private List<AllImages> mUpload;
    private List<AllImages> mUpload1;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ImageView menuIcon;
    public static String uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        setTitle("Home");
        getSupportActionBar().hide();

        //  setTitleColor(Color.WHITE);

        listView = findViewById(R.id.listView);
        menuIcon = findViewById(R.id.menuIcon);


        // Log.i("_IMG",mUri.downloadUri());


        mUpload = new ArrayList<>();
        mUpload1 = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userNameImages");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUpload.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    for (DataSnapshot ds1 : ds.getChildren()) {

                        AllImages allImages = ds1.getValue(AllImages.class);

                        // Log.i("_Uri",allImages.getmImageUri());

                        mUpload.add(allImages);
                    }

                }
                adapter = new FeedAdapter(getApplicationContext(), mUpload);

                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.i("_cancle", databaseError.getDetails());


            }
        });

        // Profile pic retrieve....


        databaseReference = FirebaseDatabase.getInstance().getReference("userProfileImages").child(mAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUpload1.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // uri = null;
                    AllImages allImages = ds.getValue(AllImages.class);

                    // Log.i("_All",allImages.toString());

                    mUpload1.add(allImages);
                    Picasso.with(UserFeedActivity.this)
                            .load(allImages.getmImageUri())
                            .placeholder(R.drawable.ic_account_circle_black_24dp)
                            .fit()
                            .into(menuIcon);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void gotoProfile(View view) {

        Intent intent = new Intent(getApplicationContext(), UserDetailActivity.class);
        startActivity(intent);

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_menu,menu);


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.profileDetail){

            Intent intent = new Intent(getApplicationContext(),UserDetailActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
    */


}
