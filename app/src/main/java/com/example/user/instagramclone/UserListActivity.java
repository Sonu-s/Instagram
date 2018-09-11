package com.example.user.instagramclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private ListView listView;

    private DatabaseReference mDatabaseRef;

    private List<FriendsName> friendsNames;

    private ImageAdapter mAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = findViewById(R.id.listView);

        friendsNames = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userName");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                friendsNames.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.getKey().equals(mAuth.getUid())) {
                        // Log.i("_Uid", ds.getKey());
                        continue;

                    }

                    for (DataSnapshot ds1 : ds.getChildren()) {

                        FriendsName name = ds1.getValue(FriendsName.class);

                        friendsNames.add(name);
                        // Log.i("_Name",friendsNames.toString());
                    }

                }

                //Log.i("_UserNAme", friendsNames.toString());

                mAdapter = new ImageAdapter(getApplicationContext(), friendsNames);

                listView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), UserFeedActivity.class);
                startActivity(intent);

                // Toast.makeText(getApplicationContext(), "I'm at position" + i + 1, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logoutId) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;

        }
        if (item.getItemId() == R.id.shareId) {

            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            startActivity(intent);
            return true;

        }
        return false;
    }
}
