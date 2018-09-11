package com.example.user.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    //my-project-instagram-a0b98

    private EditText emailText;
    private EditText passwordText;
    private Button signUpButton;
    private TextView loginText;
    private Boolean signUpModeActive = true;
    private RelativeLayout backgroundRelativeLayout;
    private ImageView logoImageView;
    // private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String password;
    private ProgressBar mProgressBarCircle;
    private ImageView visibilityOn;
    private ImageView visibilityOff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setTitle("Instagram");
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        signUpButton = findViewById(R.id.signUpButton);
        loginText = findViewById(R.id.loginText);
        backgroundRelativeLayout = findViewById(R.id.backgroundRelativeLayout);
        logoImageView = findViewById(R.id.logoimageView);
        mProgressBarCircle = findViewById(R.id.progress_circle);
        visibilityOn = findViewById(R.id.visibilityOn);
        visibilityOff = findViewById(R.id.visibilityOff);

        passwordText.setOnKeyListener(this);
        loginText.setOnClickListener(this);
        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null){

            Intent intent = new Intent(this,UserListActivity.class);
            startActivity(intent);
        }

    }




    public void onClick(View view1) {

        if (view1.getId() == R.id.loginText) {
            if (signUpModeActive) {

                signUpButton.setText("Login");
                loginText.setText("Or, SignUp");
                // loginText.setTag(0);
                signUpModeActive = false;
            } else {

                signUpButton.setText("SignUp");
                loginText.setText("Or, Login");
                //loginText.setTag(1);
                signUpModeActive = true;
            }
        } else if (view1.getId() == R.id.backgroundRelativeLayout || view1.getId() == R.id.logoimageView) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }

    public void signUpProcess(View view) {
        mProgressBarCircle.setVisibility(View.VISIBLE);

        String email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            //Email is empty..
            Toast.makeText(getApplicationContext(), "Please enter a email!", Toast.LENGTH_SHORT).show();
            //Stop the function execution further
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //password is empty..
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
            //stop the function execution further
            return;
        }
        //  progressDialog.setMessage("Registering User..");
        //  progressDialog.show();

        if (signUpModeActive) {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //progressDialog.dismiss();
                            mProgressBarCircle.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()) {

                                Intent intent = new Intent(getApplicationContext(),UserNameActivity.class);
                                startActivity(intent);


                            } else {

                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        } else {

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // progressDialog.dismiss();
                            mProgressBarCircle.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()) {

                                Log.i("SignUp", "Login successful");
                                Intent intent = new Intent(MainActivity.this,UserListActivity.class);
                                startActivity(intent);


                            } else {

                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }


    public void viewPassword(View view){

        if (view.getId() == R.id.visibilityOn){

            passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            visibilityOn.setVisibility(View.INVISIBLE);
            visibilityOff.setVisibility(View.VISIBLE);
            passwordText.setSelection(passwordText.length());

        }else{

            passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            visibilityOn.setVisibility(View.VISIBLE);
            visibilityOff.setVisibility(View.INVISIBLE);
            passwordText.setSelection(passwordText.length());
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {

            signUpProcess(view);
        }
        return false;
    }
}
