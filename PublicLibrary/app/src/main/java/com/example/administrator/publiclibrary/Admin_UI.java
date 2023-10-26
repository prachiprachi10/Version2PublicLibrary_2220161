package com.example.administrator.publiclibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Admin_UI extends AppCompatActivity {
    private static final String Tag="Admin_UI";
    private  int CAMERA_REQUEST_CODE = 2;
    private Firebase FireRef;
    private List<String> nomeConsulta = new ArrayList<String>();
    private ArrayAdapter<String> dataAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mdatabase,mdatabase2;
    private FirebaseStorage mstorage;
    private FirebaseUser user;

    private Spinner rackloc,shelfloc,Location1;
    private Button addbook_location,add_category,search_category,update_categor,delete_category;
    private Button insertbook,searchbook,updatebook,deletebook,searchborrow,returnb;
    private EditText locID,categoryid,categories,categoryid2,categories2;
    private EditText bookid,bookname,bookid2,bookname2,txtcategory,txtlocation;
    private Spinner bookcategory,booklocation,bookcategory2,booklocation2;
    private TextView tv;
    private RecyclerView borrowedbooks2;
    private DrawerLayout drawer;
    private EditText sbib,sname,saddress,sgender,snic,sbd,srd;
private String userid;
    private ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout) findViewById(R.id.drawer2);

        mAuth=FirebaseAuth.getInstance();

        updatebook=(Button) findViewById(R.id.btnupdatebook);
        deletebook=(Button) findViewById(R.id.btndeletebook);
        tv=(TextView) findViewById(R.id.tv1);

        tv.setVisibility(View.INVISIBLE);
        updatebook.setVisibility(View.INVISIBLE);
        deletebook.setVisibility(View.INVISIBLE);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatebook.setVisibility(View.VISIBLE);
                deletebook.setVisibility(View.VISIBLE);
                txtlocation.setVisibility(View.INVISIBLE);
                txtcategory.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
            }
        });



/*Including tab items*/
        TabHost tab=(TabHost) findViewById(R.id.tabhost2);
        tab.setup();

        TabHost.TabSpec spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_queue_black_24dp));
        spec1.setContent(R.id.Atab1);
        tab.addTab(spec1);


        spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_edit_black_24dp));
        spec1.setContent(R.id.Atab2);
        tab.addTab(spec1);

        spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_view_list_black_24dp));
        spec1.setContent(R.id.Atab3);
        tab.addTab(spec1);

        spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_edit_location_black_24dp));
        spec1.setContent(R.id.Atab4);
        tab.addTab(spec1);

        spec1=tab.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_search_black_24dp));
        spec1.setContent(R.id.Atab5);
        tab.addTab(spec1);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(Tag, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {

                    Log.d(Tag, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //search borrowed books
        sbib=(EditText) findViewById(R.id.txtsbookid);
        sname=(EditText) findViewById(R.id.txtsname);

        saddress=(EditText) findViewById(R.id.txtsaddress);
        sgender=(EditText) findViewById(R.id.txtsgender);
        snic=(EditText) findViewById(R.id.txtsnic);
        sbd=(EditText) findViewById(R.id.txtsBD);
        srd=(EditText) findViewById(R.id.txtsRD);

        searchborrow=(Button) findViewById(R.id.btnsearchborrow);

        searchborrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sbib.getText().toString().equals("")) {
                    sbib.setError("Enter Book ID");
                } else {

                    try {
                        String SBID = sbib.getText().toString().trim();
                        mdatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference child = mdatabase.child("Adminlist");


                        child.child(SBID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    sbd.setText(dataSnapshot.child("Borrowed_Date").getValue().toString());
                                    srd.setText(dataSnapshot.child("Return_Date").getValue().toString());
                                    userid = dataSnapshot.child("User").getValue().toString();
                                    memberdetails();

                                }
                                else
                                {
                                    sbib.setError("This Book Not Borrowed");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Book was not borrowed", Toast.LENGTH_SHORT).show();

                            }
                        });


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Book Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        returnb=(Button) findViewById(R.id.btnsreturn);
        returnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sbib.getText().toString().equals("")) {
                    sbib.setError("Enter Book ID");

                } else {


                    String SBID = sbib.getText().toString().trim();
                    mdatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference child = mdatabase.child("Adminlist");
                    child.child(SBID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getApplicationContext(), "Book Returned successfully", Toast.LENGTH_LONG).show();
                        }
                    });


                    mdatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference child2 = mdatabase.child("BorrowedBooks").child(userid);
                    DatabaseReference DB = child2.child("Book");
                    DB.child(SBID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Book Returned successfully", Toast.LENGTH_LONG).show();
                        }
                    });

                    DatabaseReference child3 = mdatabase.child("Books");
                    DatabaseReference UserDB = child3.child(SBID);


                    UserDB.child("Status").setValue("IN");

                    sbib.setText("");
                    sname.setText("");
                    saddress.setText("");
                    sgender.setText("");
                    snic.setText("");
                    sbd.setText("");
                    srd.setText("");
                }
            }
        });



        //add category declaration

        categoryid=(EditText) findViewById(R.id.txtcateid);
        categories=(EditText) findViewById(R.id.txtcate);

        categoryid2=(EditText) findViewById(R.id.txtcateid2);
        categories2=(EditText) findViewById(R.id.txtcate2);


        add_category=(Button) findViewById(R.id.btnaddcategory);
        search_category=(Button) findViewById(R.id.btnsearchcate);
        update_categor=(Button) findViewById(R.id.btnupdatecate);
        delete_category=(Button) findViewById(R.id.btndeletecate);

        //add category
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_category();
                get_cetagoryid();
            }
        });
        //search category
        search_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_category();
            }
        });

        //update category
        update_categor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_category();
            }
        });

        //delete Category
        delete_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              delete_category();
               get_cetagoryid();
            }
        });



        //Add book location Declaration
        rackloc=(Spinner) findViewById(R.id.spinB_rack);
       shelfloc=(Spinner) findViewById(R.id.spinB_Shelf);
        locID=(EditText) findViewById(R.id.txtlocid);
        addbook_location=(Button) findViewById(R.id.btnaddBlocation);
       addbook_location.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

              String rack=rackloc.getSelectedItem().toString().trim();
               String shelf=shelfloc.getSelectedItem().toString().trim();
              String locid=locID.getText().toString().trim();
               String location=rack+" / "+shelf;


               mdatabase= FirebaseDatabase.getInstance().getReference();
               DatabaseReference child=mdatabase.child("Book Location");
               DatabaseReference UserDB=child.child(locid);

               UserDB.child("Locations").setValue(location.toString().trim());

               Toast.makeText(getApplicationContext(),"added successful",Toast.LENGTH_SHORT).show();


           }
       });
// add book declare
        bookid=(EditText) findViewById(R.id.txtbookID);
        bookname=(EditText) findViewById(R.id.txtbookname);
        bookcategory=(Spinner) findViewById(R.id.spincategory);
        booklocation=(Spinner) findViewById(R.id.spinlocation);

        insertbook=(Button) findViewById(R.id.btnaddbook);
        insertbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_books();
            }
        });

        //search book
        bookid2=(EditText) findViewById(R.id.txtbookid2);
        bookname2=(EditText) findViewById(R.id.txtbookname2);
        txtcategory=(EditText) findViewById(R.id.txtcategory);
        txtlocation=(EditText) findViewById(R.id.txtlocation);

        bookcategory2=(Spinner) findViewById(R.id.spincategory2);
        booklocation2=(Spinner) findViewById(R.id.spinlocation2);

        searchbook=(Button) findViewById(R.id.btnsearchbook);
        searchbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_book();

            }
        });

        updatebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_book();
            }
        });

        deletebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_book();
            }
        });

//Getting book locations from database
        get_locid();
        get_cetagoryid();
        get_bookid();






}

    public void memberdetails()
    {

        profile=(ImageView) findViewById(R.id.imgprofile3);
        DatabaseReference  mdata = FirebaseDatabase.getInstance().getReference().child("Member");
        mdata.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sname.setText(dataSnapshot.child("Name").getValue().toString());
                sgender.setText(dataSnapshot.child("Gender").getValue().toString());
                saddress.setText(dataSnapshot.child("Address").getValue().toString());
                snic.setText(dataSnapshot.child("NIC NO").getValue().toString());
                String imageuri=dataSnapshot.child("Image").getValue().toString();

                Glide.with(Admin_UI.this).load(dataSnapshot.child("Image").getValue().toString()).centerCrop().into(profile);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






    public void get_spinner_location()
    {

   /*     dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeConsulta);
      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Location1 = (Spinner)findViewById(R.id.spinlocation);


        mdatabase = FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Book Location").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Book_location  data= snapshot.child("Location").getValue(Book_location.class);
                    nomeConsulta.add(data.getbooklocation());
                }
                dataAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Location1.setAdapter(dataAdapter);*/
    }


    public void get_locid()

    {
        final EditText txtlid=(EditText) findViewById(R.id.txtlocid);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference child=mdatabase.child("Book Location");
        child.child("Location").getParent().orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Get the node from the datasnapshot

               txtlid.setEnabled(false);

                String EID = null;

                    EID = dataSnapshot.getKey().toString();

                EID = EID.trim();
                String x = EID.substring(1);
                int ID = Integer.parseInt(x);

                String IDS=null ;

                if(ID>0 && ID<9)
                {
                    ID=ID+1;
                    IDS="L00"+ID;
                }

                else if(ID>=9 && ID<99)
                {
                    ID=ID+1;
                    IDS="L0"+ID;
                }

                else if(ID >=99)
                {
                    ID=ID+1;
                    IDS="L"+ID;
                }

                txtlid.setText(IDS);



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void add_category()
    {
        if(categories.getText().toString().equals(""))
        {
            categories.setError("Enter Category");

        }
        else {
            String cateid = categoryid.getText().toString().trim();
            String cate = categories.getText().toString().trim();


            mdatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference child = mdatabase.child("Book Category");
            DatabaseReference UserDB = child.child(cateid);

            UserDB.child("Category").setValue(cate);

            Toast.makeText(getApplicationContext(), "Category added successful", Toast.LENGTH_SHORT).show();
            categories.setText("");
        }
    }

    public void search_category()
    {
        if(categoryid2.getText().toString().equals(""))
        {
            categoryid2.setError("Enter Category ID");

        }
        else {
            String cateid2 = categoryid2.getText().toString().trim();

            mdatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference child = mdatabase.child("Book Category");
            DatabaseReference UserDB = child.child(cateid2);

            UserDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String catego = dataSnapshot.child("Category").getValue().toString();
                        categories2.setText(catego);
                        Toast.makeText(getApplicationContext(), "Category ID Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Category ID Not Found", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Category ID not Found", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void update_category()
    {
        if(categoryid2.getText().toString().equals(""))
        {
            categoryid2.setError("Enter Category ID");

        }else
        if(categories2.getText().toString().equals(""))
        {
            categories2.setError("Enter Category");
        }else {
            try {
                String cateid2 = categoryid2.getText().toString().trim();
                String cate2 = categories2.getText().toString().trim();


                mdatabase = FirebaseDatabase.getInstance().getReference();
                DatabaseReference child = mdatabase.child("Book Category");
                DatabaseReference UserDB = child.child(cateid2);

                UserDB.child("Category").setValue(cate2);

                Toast.makeText(getApplicationContext(), "Category updated successful", Toast.LENGTH_SHORT).show();
                categories2.setText("");
                categoryid2.setText("");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Category updating failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void delete_category()
    {
        if(categoryid2.getText().toString().equals(""))
        {
            categoryid2.setError("Enter Category ID");

        }else {
            String cateid2 = categoryid2.getText().toString().trim();
            String cate2 = categories2.getText().toString().trim();

            mdatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref = mdatabase.child("Book Category");

            ref.child(cateid2).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Category Removed successfully", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Category Removed Failed ", Toast.LENGTH_SHORT).show();

                }
            });
        }


}

    public void get_cetagoryid()
    {
        mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference child=mdatabase.child("Book Category");
        child.child("Category").getParent().orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Get the node from the datasnapshot

                categoryid.setEnabled(false);

                String EID = null;

                EID = dataSnapshot.getKey().toString();

                EID = EID.trim();
                String x = EID.substring(1);
                int ID = Integer.parseInt(x);

                String IDS=null ;

                if(ID>0 && ID<9)
                {
                    ID=ID+1;
                    IDS="C00"+ID;
                }

                else if(ID>=9 && ID<99)
                {
                    ID=ID+1;
                    IDS="C0"+ID;
                }

                else if(ID >=99)
                {
                    ID=ID+1;
                    IDS="C"+ID;
                }

                categoryid.setText(IDS);



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void add_books()
    {
        if(bookid.getText().toString().equals(""))
        {
            bookid.setError("Enter Book ID");
        }
        else if (bookname.getText().toString().equals(""))
        {
            bookname.setError("Enter Book Name");
        }else {
            String id = bookid.getText().toString().trim();
            String name = bookname.getText().toString().trim();
            String Category = bookcategory.getSelectedItem().toString().trim();
            String locat = booklocation.getSelectedItem().toString().trim();


            mdatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference child = mdatabase.child("Books");
            DatabaseReference UserDB = child.child(id);


            UserDB.child("Book ID").setValue(id);
            UserDB.child("Book Name").setValue(name);
            UserDB.child("Book Category").setValue(Category);
            UserDB.child("Book Location").setValue(locat);
            UserDB.child("Status").setValue("IN");


            Toast.makeText(getApplicationContext(), "Book added successful", Toast.LENGTH_SHORT).show();
            bookname.setText("");
        }


    }

    public void get_bookid()
    {
        mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference child=mdatabase.child("Books");
        child.child("Category").getParent().orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Get the node from the datasnapshot

                bookid.setEnabled(false);

                String EID = null;

                EID = dataSnapshot.getKey().toString();

                EID = EID.trim();
                String x = EID.substring(1);
                int ID = Integer.parseInt(x);

                String IDS=null ;

                if(ID>0 && ID<9)
                {
                    ID=ID+1;
                    IDS="B00"+ID;
                }

                else if(ID>=9 && ID<99)
                {
                    ID=ID+1;
                    IDS="B0"+ID;
                }

                else if(ID >=99)
                {
                    ID=ID+1;
                    IDS="B"+ID;
                }

                bookid.setText(IDS);



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void search_book()
    {
        if(bookid2.getText().toString().equals(""))
        {
            bookid2.setError("Enter Book ID");
        }
        else {
            String id2 = bookid2.getText().toString().trim();
            String name2 = bookname2.getText().toString().trim();
            String cate2 = txtcategory.getText().toString().trim();
            String locate2 = txtlocation.getText().toString().trim();


            mdatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference child = mdatabase.child("Books");
            DatabaseReference UserDB = child.child(id2);

            UserDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        bookname2.setText(dataSnapshot.child("Book Name").getValue().toString());
                        txtcategory.setText(dataSnapshot.child("Book Category").getValue().toString());
                        txtlocation.setText(dataSnapshot.child("Book Location").getValue().toString());


                        Toast.makeText(getApplicationContext(), "Book ID Found", Toast.LENGTH_SHORT).show();
                        tv.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        bookid2.setError("Book ID Not Found");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Book ID not Found", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void update_book()
    {
        if(bookid2.getText().toString().equals(""))
        {
            bookid2.setError("Enter Book ID");
        }else if(bookname2.getText().toString().equals("")) {
            bookname2.setError("Enter Book Name");

        }
        else {
            String id = bookid2.getText().toString().trim();
            String name = bookname2.getText().toString().trim();
            String Category = bookcategory2.getSelectedItem().toString().trim();
            String locat = booklocation2.getSelectedItem().toString().trim();


            mdatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference child = mdatabase.child("Books");
            DatabaseReference UserDB = child.child(id);

            UserDB.child("Book Name").setValue(name);
            UserDB.child("Book Category").setValue(Category);
            UserDB.child("Book Location").setValue(locat);


            Toast.makeText(getApplicationContext(), "Book added successful", Toast.LENGTH_SHORT).show();

            bookid2.setText("");
            bookname2.setText("");
            txtcategory.setText("");
            txtlocation.setText("");
            tv.setVisibility(View.INVISIBLE);
            updatebook.setVisibility(View.INVISIBLE);
            deletebook.setVisibility(View.INVISIBLE);
            txtlocation.setVisibility(View.VISIBLE);
            txtcategory.setVisibility(View.VISIBLE);
        }
    }
 public void delete_book()
 {

         String id = bookid2.getText().toString().trim();


         mdatabase = FirebaseDatabase.getInstance().getReference();
         DatabaseReference child = mdatabase.child("Books");
         DatabaseReference UserDB = child.child(id);
         UserDB.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Toast.makeText(getApplicationContext(), "Book Deleted successful", Toast.LENGTH_SHORT).show();
                 bookid2.setText("");
                 bookname2.setText("");
                 txtcategory.setText("");
                 txtlocation.setText("");
                 tv.setVisibility(View.INVISIBLE);
                 updatebook.setVisibility(View.INVISIBLE);
                 deletebook.setVisibility(View.INVISIBLE);
                 txtlocation.setVisibility(View.VISIBLE);
                 txtcategory.setVisibility(View.VISIBLE);
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getApplicationContext(), "Book Deleted Failed", Toast.LENGTH_SHORT).show();

             }
         });

 }

    @Override
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
        get_spinner_location();
       /// my_borrowed_book();
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
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logout2) {
            mAuth.signOut();
            Toast.makeText(getApplicationContext(),"Sign Out...",Toast.LENGTH_LONG).show();
            Intent login=new Intent(Admin_UI.this,Login.class);
            startActivity(login);
            return true;
        }

            //noinspection SimplifiableIfStatement



        return super.onOptionsItemSelected(item);
    }
}
