package com.example.administrator.publiclibrary;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
   private static final String Tag="Regstration";
   final private  int CAMERA_REQUEST_CODE = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email,password,cpassword,name,address,dob,nic,answer1,answer2;
    private Button signup;
    private DatabaseReference mdatabase;
    private FirebaseDatabase fdatabase;
    private FirebaseStorage mstorage;
    private ImageView profilpic;
    private String gender;
    private RadioButton male,female;
    private DatePickerDialog.OnDateSetListener mdatelistener;
private TextView txtpp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        email=(EditText) findViewById(R.id.txtmail);
        password=(EditText) findViewById(R.id.txtpassword);
        cpassword=(EditText) findViewById(R.id.txtcpass);
        name=(EditText) findViewById(R.id.txtname);
        address=(EditText) findViewById(R.id.txtaddress);
        nic=(EditText) findViewById(R.id.txtnic);
        answer1=(EditText) findViewById(R.id.txtanswer1);
        answer2=(EditText) findViewById(R.id.txtanswer2);
        dob=(EditText) findViewById(R.id.txtdob);
        txtpp=(TextView) findViewById(R.id.txtvpp);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Registration.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mdatelistener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mdatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.e(Tag, "onDateSet dd/mm/yyyy:" + day + "/" + month + "/" + year);
                String Date = day + "/" + month + "/" + year;
                dob.setText(Date);
            }
        };
        mstorage=FirebaseStorage.getInstance();
        signup = (Button) findViewById(R.id.btnsingup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sing_up();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(Tag, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(Tag, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        FirebaseUser user = mAuth.getCurrentUser();
        profilpic=(ImageView) findViewById(R.id.imgprofile);
        profilpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img=new Intent();
               img.setType("image/*");
                img.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(img,"select an image"),CAMERA_REQUEST_CODE);
                txtpp.setVisibility(View.INVISIBLE);
            }
        });




    }


    private Uri selectedImageUri=null;
    private File imgFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
            super.onActivityResult(resultCode, requestCode, data);
        if (resultCode == RESULT_OK && requestCode== CAMERA_REQUEST_CODE && data!=null)
        {

             selectedImageUri =data.getData();
            imgFile = new File(selectedImageUri.getPath());
            Picasso.with(Registration.this).load(selectedImageUri).fit().centerCrop().into(profilpic);


            }

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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void sing_up() {
        male = (RadioButton) findViewById(R.id.rbmale);
        female = (RadioButton) findViewById(R.id.rbfemale);
        if(imgFile==null)
        {
            Toast.makeText(getApplicationContext(), "select a profile image", Toast.LENGTH_LONG).show();
        }else
        if (name.getText().toString().equals("")) {
            name.setError("Enter Your Name");
        } else if (email.getText().toString().equals("")) {
            email.setError("Enter Email Address");
        } else if (!isValidEmail(email.getText().toString())) {
            email.setError("Invalid Email Address");
        } else if (password.getText().toString().equals("")) {
            password.setError("Please Enter password");
        } else if (password.getText().toString().length() < 6) {
            password.setError("Password must be 6 characters");
        } else if(!cpassword.getText().toString().equals(password.getText().toString()))
        {
            cpassword.setError("password is not matching");
        }
        else if (address.getText().toString().equals("")) {
            address.setError("Enter Your Address");
        } else if (male.isChecked()==false && female.isChecked()==false) {
            Toast.makeText(getApplicationContext(), "select a Gender", Toast.LENGTH_LONG).show();
        } else if (dob.getText().toString().equals("")) {
            dob.setError("Pick a Date");
        } else if (nic.getText().toString().equals("")) {
            nic.setError("Enter NIC Number");
        } else if (nic.getText().toString().length() != 10) {
            nic.setError("Enter your Correct 10 charector NIC Number ");
        } else if (answer1.getText().toString().equals("")) {
            answer1.setError("Choose a question and give the Answer");
        } else if (answer2.getText().toString().equals("")) {
            answer2.setError("Choose a question and give the Answer");
        } else

        {


            if (male.isChecked() == true) {
                gender = "Male";
            } else {
                gender = "Female";
            }
                final ProgressDialog progress = ProgressDialog.show(Registration.this, "please wait....", "Registering", true);
                mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.dismiss();

                                if (task.isSuccessful()) {

                                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString());

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    StorageReference filepath = mstorage.getReference().child("photos").child(user.getUid());
                                    filepath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Uri uri2 = taskSnapshot.getDownloadUrl();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            mdatabase = FirebaseDatabase.getInstance().getReference().child("Member");
                                            DatabaseReference UserDB = mdatabase.child(user.getUid());

                                            UserDB.child("Name").setValue(name.getText().toString().trim());
                                            UserDB.child("Address").setValue(address.getText().toString().trim());
                                            UserDB.child("Gender").setValue(gender.toString().trim());
                                            UserDB.child("DOB").setValue(dob.getText().toString().trim());
                                            UserDB.child("NIC NO").setValue(nic.getText().toString().trim());
                                            UserDB.child("Answer").setValue(answer1.getText().toString().trim());
                                            UserDB.child("Answer2").setValue(answer2.getText().toString().trim());
                                            UserDB.child("Image").setValue(uri2.toString());


                                        }
                                    });
                                    Toast.makeText(Registration.this, "Registered Successful", Toast.LENGTH_SHORT).show();

                                    Intent user1 = new Intent(Registration.this, Login.class);
                                    startActivity(user1);


                                } else {

                                    try
                                    {
                                        throw task.getException();
                                    }

                                    // if user enters wrong password.
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {

                                        email.setError("Email already in the database");

                                    }
                                    catch (Exception e)
                                    {
                                        Log.d(Tag, "onComplete: " + e.getMessage());
                                    }

                                }


                            }


                        });

        }
    }
}
