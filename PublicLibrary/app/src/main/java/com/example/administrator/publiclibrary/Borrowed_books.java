package com.example.administrator.publiclibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

public class Borrowed_books extends AppCompatActivity {
    private static final String TAG="Borrowed_books";
    private Button btnSingout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  int CAMERA_REQUEST_CODE = 2;
    private Firebase FireRef;
    private RecyclerView borrowedbooks;


    private DatabaseReference mdatabase,ref;
    private FirebaseStorage mstorage;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books);


       // borrowedbooks=(RecyclerView)findViewById(R.id.borrowed_book);
       // borrowedbooks.setHasFixedSize(true);
       // borrowedbooks.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
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
    }


 /*   @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        mdatabase= FirebaseDatabase.getInstance().getReference();
        ref=mdatabase.child("BorrowedBooks").child("B001").child(mAuth.getCurrentUser().getUid());
        FirebaseRecyclerAdapter<my_books,blogviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<my_books, blogviewholder>(
                my_books.class,
                R.layout.activity_borrowed_books,
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
        borrowedbooks.setAdapter(firebaseRecyclerAdapter);
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

        }

        public void setBook_name(String name)
        {
            EditText b_ID=(EditText) mview.findViewById(R.id.txtmybookname);
            b_ID.setText(name);
        }

        public void setBook_category(String cate)
        {
            EditText b_ID=(EditText) mview.findViewById(R.id.txtmybookcategory);
            b_ID.setText(cate);
        }

        public void setBorrowed_date(String BD)
        {
            EditText b_ID=(EditText) mview.findViewById(R.id.txtmybookBD);
            b_ID.setText(BD);
        }
        public void setReturn_date(String RD)
        {
            EditText b_ID=(EditText) mview.findViewById(R.id.txtmybookRD);
            b_ID.setText(RD);
        }

    }
    */
}
