package com.example.administrator.publiclibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private static final String TAG="Login";

    private TextView newuser;
    private Button btnlogin;
    private EditText email,password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Login mInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        email=(EditText) findViewById(R.id.txtemail);
        password=(EditText) findViewById(R.id.txtpassword);
        mAuth = FirebaseAuth.getInstance();
    newuser=(TextView) findViewById(R.id.txtnewuser);
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg=new Intent(Login.this,Registration.class);
                startActivity(reg);

            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnlogin=(Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singin();
            }
        });
    }



    public void Singin()
    {

        if(email.getText().toString().equals(""))
        {
            email.setError("Please Enter Email Address");
        }else
        if (!isValidEmail(email.getText().toString())) {
            email.setError("Invalid Email");
        }else if (password.getText().toString().equals(""))
        {
            password.setError("Please Enter password");
        }else if(password.getText().toString().length()<6)
        {
            password.setError("Password must be six characters");
        }
        else
        {
            final ProgressDialog progress = ProgressDialog.show(Login.this, "please wait", "Sign in....", true);
            mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progress.dismiss();

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {


                                Log.w(TAG, "signInWithEmail", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (email.getText().toString().equals("faroos1996@gmail.com") && password.getText().toString().equals("faroos123")) {
                                    Intent user = new Intent(Login.this, Admin_UI.class);
                                    startActivity(user);
                                    Toast.makeText(getApplicationContext(), "Sign in Successful please wait..", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent user2 = new Intent(Login.this, User_UI.class);
                                    startActivity(user2);
                                    Toast.makeText(getApplicationContext(), "Sign in Successful please wait..", Toast.LENGTH_SHORT).show();
                                }
                            }

                            // ...
                        }
                    });
        }
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
