package com.example.administrator.publiclibrary;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User_UI extends AppCompatActivity {
    private static final String TAG="User_UI";
    private Button btnSingout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private  int CAMERA_REQUEST_CODE = 2;
    private Firebase FireRef;
    private RecyclerView borrowedbooks;
private ImageView profile,profile2,profilpic;
    private FirebaseDatabase fdatabase;
    private DatabaseReference mdatabase;
    private FirebaseStorage mstorage;
    private FirebaseUser user;


    private Button searchbook,getbook,done,respass,profileinfo,update,btnreturn,uploadimg;
    private EditText locID,categoryid,categories,categoryid2,categories2;
    private EditText bookid,bookname,bookid2,bookname2,txtcategory,txtlocation,status;
    private Spinner bookcategory,booklocation,bookcategory2,booklocation2;
    private TextView tv;
    private DrawerLayout drawer,drawer2;
    private ActionBarDrawerToggle toggle,toggle2;
    private TabHost tab;
    private EditText uname,uemail,ugender,uaddress,udob,unic,uname2,uemail2,uaddress2,udob2,unic2;
    private RadioButton male2,female2;
    private DatePickerDialog.OnDateSetListener mdatelistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout ) findViewById(R.id.drawerlayout);
        toggle=new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      /*  done=(Button) findViewById(R.id.Btndone);*/
        respass=(Button) findViewById(R.id.btnupdateuser);

        respass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab.setCurrentTab(4);
            }
        });


        profileinfo=(Button) findViewById(R.id.btnprofile);
        profileinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab.setCurrentTab(2);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        update=(Button) findViewById(R.id.btnupdate2);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
//Notification




        uname=(EditText) findViewById(R.id.txtname1);
        uemail=(EditText) findViewById(R.id.txtmail1);
        ugender=(EditText) findViewById(R.id.txtgender1);
        uaddress=(EditText) findViewById(R.id.txtaddress1);
        udob=(EditText) findViewById(R.id.txtdob1);
        unic=(EditText) findViewById(R.id.txtnic1);

        profile=(ImageView) findViewById(R.id.profilepic);
        profile2=(ImageView) findViewById(R.id.imgprofile1);
        searchbook=(Button) findViewById(R.id.btnusearchbook);
        getbook=(Button) findViewById(R.id.btngetbook);
        tv=(TextView)findViewById(R.id.textView16);

        getbook.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);

        bookid=(EditText) findViewById(R.id.txtubookid);
        bookname=(EditText) findViewById(R.id.txtubookname);
        txtcategory=(EditText) findViewById(R.id.txtubookcategory);
        txtlocation=(EditText) findViewById(R.id.txtubooklocation);
        status=(EditText) findViewById(R.id.txtstatus);

        uemail2=(EditText) findViewById(R.id.txtmail);

        //update member
        uname2=(EditText) findViewById(R.id.txtname3);
        uaddress2=(EditText) findViewById(R.id.txtaddress3);
        unic2=(EditText) findViewById(R.id.txtnic3);
        udob2=(EditText) findViewById(R.id.txtdob3);


        udob2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(User_UI.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mdatelistener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mdatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.e(TAG, "onDateSet dd/mm/yyyy:" + day + "/" + month + "/" + year);
                String Date = day + "/" + month + "/" + year;
                udob2.setText(Date);
            }
        };

        searchbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_book();
            }
        });

        getbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrow_books();
            }
        });

        uploadimg=(Button) findViewById(R.id.btnuploadimg);
        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadnewimage();
            }
        });
        fdatabase= FirebaseDatabase.getInstance();
        mstorage=FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnSingout=(Button) findViewById(R.id.btnsignout);
                btnSingout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(),"Sign Out...",Toast.LENGTH_LONG).show();
                Intent login=new Intent(User_UI.this,Login.class);
                startActivity(login);
            }
        });



        tab=(TabHost) findViewById(R.id.tabhost1);
        tab.setup();

        TabHost.TabSpec spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_queue_black_24dp));
        spec1.setContent(R.id.tab1);
        tab.addTab(spec1);


       spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_view_list_black_24dp));
        spec1.setContent(R.id.tab2);
       tab.addTab(spec1);

        spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_person_black_24dp));
        spec1.setContent(R.id.tab3);
        tab.addTab(spec1);

        spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_edit_black_24dp));
        spec1.setContent(R.id.tab4);
        tab.addTab(spec1);

        FirebaseUser user = mAuth.getCurrentUser();
        profilpic=(ImageView) findViewById(R.id.imgprofile3);
        profilpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img=new Intent();
                img.setType("image/*");
                img.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(img,"select an image"),CAMERA_REQUEST_CODE);

            }
        });

    }


    private Uri selectedImageUri=null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(resultCode, requestCode, data);
        if (resultCode == RESULT_OK && requestCode== CAMERA_REQUEST_CODE && data!=null)
        {

            selectedImageUri =data.getData();
            Picasso.with(User_UI.this).load(selectedImageUri).fit().centerCrop().into(profilpic);


        }

    }
    public String id,name,cate,locate,stat;
    public void search_book()
    {
        if(bookid.getText().toString().equals(""))
        {
            bookid.setError("Enter Book ID");
        }
        else {
            id = bookid.getText().toString().trim();


            mdatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference child = mdatabase.child("Books");
            DatabaseReference UserDB = child.child(id);

            UserDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            name = dataSnapshot.child("Book Name").getValue().toString();
                            cate = dataSnapshot.child("Book Category").getValue().toString();
                            locate = dataSnapshot.child("Book Location").getValue().toString();
                            stat = dataSnapshot.child("Status").getValue().toString();

                            bookname.setText(name);
                            txtcategory.setText(cate);
                            txtlocation.setText(locate);
                            status.setText(stat);

                            bookname.setEnabled(false);
                            txtcategory.setEnabled(false);
                            txtlocation.setEnabled(false);
                            status.setEnabled(false);


                            if (stat.equals("IN")) {
                                getbook.setVisibility(View.VISIBLE);
                                tv.setVisibility(View.INVISIBLE);
                            } else {
                                tv.setVisibility(View.VISIBLE);
                                getbook.setVisibility(View.INVISIBLE);
                            }
                            Toast.makeText(getApplicationContext(), "Book ID Found", Toast.LENGTH_SHORT).show();
                        }
                    else
                        {
                            Toast.makeText(getApplicationContext(), "Book ID not Found", Toast.LENGTH_SHORT).show();

                        }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Book ID not Found", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void borrow_books()
    {



            mdatabase= FirebaseDatabase.getInstance().getReference();
            DatabaseReference child=mdatabase.child("BorrowedBooks");
            DatabaseReference UserDB=child.child(user.getUid()).child("Book").child(id);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate1 = df1.format(c.getTime());
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.MONTH, 1);
       String retur=df1.format(c2.getTime());
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DATE, 27);
        String notify=df1.format(c3.getTime());




            UserDB.child("Book_ID").setValue(id);
            UserDB.child("Book_Name").setValue(name);
            UserDB.child("Book_Category").setValue(cate);
            UserDB.child("Book_Location").setValue(locate);
            UserDB.child("Status").setValue("OUT");
            UserDB.child("Borrowed_Date").setValue(formattedDate1);
            UserDB.child("Return_Date").setValue(retur);
            UserDB.child("Notify_Date").setValue(notify);
             UserDB.child("User").setValue(user.getUid());

        DatabaseReference child1=mdatabase.child("Adminlist");
        DatabaseReference UserDB1=child1.child(id);
        UserDB1.child("Book_ID").setValue(id);
        UserDB1.child("Book_Name").setValue(name);
        UserDB1.child("Book_Category").setValue(cate);
        UserDB1.child("Book_Location").setValue(locate);
        UserDB1.child("Borrowed_Date").setValue(formattedDate1);
        UserDB1.child("Return_Date").setValue(retur);
        UserDB1.child("Notify_Date").setValue(notify);
        UserDB1.child("User").setValue(user.getUid());


            update_books();
            Toast.makeText(getApplicationContext(),"Book added successful",Toast.LENGTH_SHORT).show();
            bookname.setText("");




    }
    public void update_books()
    {

        mdatabase= FirebaseDatabase.getInstance().getReference();
        DatabaseReference child=mdatabase.child("Books");
        DatabaseReference UserDB=child.child(id);


        UserDB.child("Status").setValue("OUT");


        Toast.makeText(getApplicationContext(),"Book added successful",Toast.LENGTH_SHORT).show();
        bookname.setText("");



    }
    String imageuri;

    public void DP()
    {
        DatabaseReference  mdata = FirebaseDatabase.getInstance().getReference().child("Member");
        mdata.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            uname.setText(dataSnapshot.child("Name").getValue().toString());
                            uemail.setText(user.getEmail().toString());
                            ugender.setText(dataSnapshot.child("Gender").getValue().toString());
                            uaddress.setText(dataSnapshot.child("Address").getValue().toString());
                            udob.setText(dataSnapshot.child("DOB").getValue().toString());
                            unic.setText(dataSnapshot.child("NIC NO").getValue().toString());
                            imageuri=dataSnapshot.child("Image").getValue().toString();

                            uname2.setText(dataSnapshot.child("Name").getValue().toString());
                            uaddress2.setText(dataSnapshot.child("Address").getValue().toString());
                            udob2.setText(dataSnapshot.child("DOB").getValue().toString());
                            unic2.setText(dataSnapshot.child("NIC NO").getValue().toString());
                            Glide.with(User_UI.this).load(dataSnapshot.child("Image").getValue().toString()).centerCrop().into(profile);
                            Glide.with(User_UI.this).load(dataSnapshot.child("Image").getValue().toString()).centerCrop().into(profile2);
                            Glide.with(User_UI.this).load(dataSnapshot.child("Image").getValue().toString()).centerCrop().into(profilpic);


                            uname.setEnabled(false);
                            uemail.setEnabled(false);
                            ugender.setEnabled(false);
                            uaddress.setEnabled(false);
                            udob.setEnabled(false);
                            unic.setEnabled(false);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


    }

   @Override
   public void onStart() {
        super.onStart();
       DP();
       my_borrowed_book();

       NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
       Notification notify=new Notification.Builder(this).setContentTitle("Public Library")
               .setSmallIcon(R.mipmap.icon_launcher)
               .setStyle(new Notification.BigTextStyle().bigText("If you Have Any Library Books Please Return On the Date"))
               .setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
               .setVibrate(new long[] {1, 1, 1}).setPriority(Notification.PRIORITY_MAX).build();
            /*   (getApplicationContext()).setContentTitle("Public Library").setContentText("If you Have Any Library Books Please Return On the Date").
               setContentTitle("Public Library ").setSmallIcon(R.mipmap.icon_launcher).build();*/



       notify.flags |= Notification.FLAG_AUTO_CANCEL;
       notif.notify(0, notify);




   }

    public void my_borrowed_book()
    {

        borrowedbooks=(RecyclerView)findViewById(R.id.borrowed_book);
        borrowedbooks.setHasFixedSize(true);
        borrowedbooks.setLayoutManager(new LinearLayoutManager(this));

        mdatabase= FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref=mdatabase.child("BorrowedBooks").child(user.getUid()).child("Book");
        FirebaseRecyclerAdapter<my_books,blogviewholder> RecyclerAdapter=new FirebaseRecyclerAdapter<my_books, blogviewholder>(
                my_books.class,
                R.layout.my_books,
                blogviewholder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(blogviewholder viewHolder, my_books model, int position) {

                viewHolder.setBook_ID(model.getBook_ID());
                viewHolder.setBook_name(model.getBook_Name());
                viewHolder.setBook_category(model.getBook_Category());
                viewHolder.setBorrowed_date(model.getBorrowed_Date());
                viewHolder.setReturn_date(model.getReturn_Date());


            }

        };


        borrowedbooks.setAdapter(RecyclerAdapter);




    }



    public static class blogviewholder extends RecyclerView.ViewHolder
    {
        View mview;
        public blogviewholder(View itemView) {
            super(itemView);
            mview=itemView;




        }

        public void setBook_ID(String id)
        {
            EditText b_ID=(EditText) mview.findViewById(R.id.txtmybookid);
            b_ID.setText(id);
            b_ID.setEnabled(false);
        }

        public void setBook_name(String name)
        {
            EditText b_Name=(EditText) mview.findViewById(R.id.txtmybookname);
            b_Name.setText(name);
            b_Name.setEnabled(false);
        }

        public void setBook_category(String cate)
        {
            EditText b_Cate=(EditText) mview.findViewById(R.id.txtmybookcategory);
            b_Cate.setText(cate);
            b_Cate.setEnabled(false);
        }

        public void setBorrowed_date(String BD)
        {
            EditText b_BD=(EditText) mview.findViewById(R.id.txtmybookBD);
            b_BD.setText(BD);
            b_BD.setEnabled(false);
        }
        public void setReturn_date(String RD)
        {
            EditText b_RD=(EditText) mview.findViewById(R.id.txtmybookRD);
            b_RD.setText(RD);
            b_RD.setEnabled(false);
        }

    }
String gender=null;

    public void update() {

        if(uname2.getText().toString().equals(""))
        {
            uname2.setError("Enter Your Name");
        }else if (uaddress2.getText().toString().equals(""))
        {
            uaddress2.setError("Enter Your Address");
        }else if (gender.equals(""))
        {
            Toast.makeText(getApplicationContext(),"select a Gender",Toast.LENGTH_LONG).show();
        }else if (udob2.getText().toString().equals(""))
        {
            udob2.setError("Pick a Date");
        }else if (unic2.getText().toString().equals(""))
        {
            unic2.setError("Enter NIC Number");
        }else if(unic2.getText().toString().length()!=10){
            unic2.setError("Enter your Correct 10 charector NIC Number ");
        }else {
            male2 = (RadioButton) findViewById(R.id.rbmale3);
            female2 = (RadioButton) findViewById(R.id.rbfemale3);
            if (male2.isChecked() == true) {
                gender = "Male";
            } else
                gender = "Female";

            FirebaseUser user = mAuth.getCurrentUser();
            mdatabase = FirebaseDatabase.getInstance().getReference().child("Member");
            DatabaseReference UserDB = mdatabase.child(user.getUid());

            UserDB.child("Name").setValue(uname2.getText().toString().trim());
            UserDB.child("Address").setValue(uaddress2.getText().toString().trim());
            UserDB.child("Gender").setValue(gender.toString().trim());
            UserDB.child("DOB").setValue(udob2.getText().toString().trim());
            UserDB.child("NIC NO").setValue(unic2.getText().toString().trim());
            DP();

            Toast.makeText(User_UI.this, "Details updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
public void uploadnewimage()
{
    final ProgressDialog progress= ProgressDialog.show(User_UI.this,"please wait....","uploading",true);
    StorageReference filepath = mstorage.getReference().child("photos").child(user.getUid());
    filepath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Uri uri2 = taskSnapshot.getDownloadUrl();
            mdatabase = FirebaseDatabase.getInstance().getReference().child("Member");
            DatabaseReference UserDB = mdatabase.child(user.getUid());

                UserDB.child("Image").setValue(uri2.toString());


            DP();
        progress.dismiss();
        }
    });
    Toast.makeText(User_UI.this, "picture changed successfully ", Toast.LENGTH_SHORT).show();
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
        if(toggle.onOptionsItemSelected(item))
        {

    return true;
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
