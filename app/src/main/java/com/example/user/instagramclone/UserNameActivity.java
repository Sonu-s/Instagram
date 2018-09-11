package com.example.user.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserNameActivity extends AppCompatActivity {

    private Button continueButtonId;
    private EditText nameText;

    private DatabaseReference mDatabaseRef;

     private String name;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        nameText = findViewById(R.id.nameText);
        continueButtonId = findViewById(R.id.continueButtonId);
        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userName").child(mAuth.getUid());

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.length() > 0) {

                    continueButtonId.setVisibility(View.VISIBLE);


                } else {

                    continueButtonId.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void continueButton(View view) {

        name = nameText.getText().toString().trim();

        FriendsName friendsName = new FriendsName();

        friendsName.setName(name);

        mDatabaseRef.push().setValue(friendsName);

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);

    }
}
