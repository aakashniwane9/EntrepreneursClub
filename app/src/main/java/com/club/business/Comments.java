package com.club.business;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.club.business.Adapters.CommentAdapter;
import com.club.business.Adapters.CommentList;
import com.club.business.Firebase.CommentFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Comments extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference("Club");
    private List<CommentList> listItems;
    Button submit;
    CommentList listItem;
    EditText commentContent;

FirebaseAuth mAuth;
FirebaseUser Firebaseuser;

    TextView display_name,display_timestamp,display_desc;
    ImageView image;

String key;
String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        Firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar mtoolbar;
        mtoolbar = findViewById(R.id.activity_comment_toolbar);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Comments.this,HomePage.class);
                startActivity(intent);
                finish();
            }
        });

        display_name = findViewById(R.id.actvity_comment_name);
        display_desc = findViewById(R.id.activity_comment_desc);
        display_timestamp = findViewById(R.id.activity_comment_timestamp);
        image = findViewById(R.id.comment_forum_card_image);
        key=getIntent().getStringExtra("key");
        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        submit=(Button) findViewById(R.id.btnComment);
        listItems=new ArrayList<>();
        commentContent=(EditText)findViewById(R.id.activity_comments_enter_comment);

        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());


        myRef.child("Forums").child(key).addValueEventListener(new ValueEventListener() {
            String picassoImg;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    picassoImg = dataSnapshot.child("imageurl").getValue().toString();
                    Picasso.get().load(picassoImg).into(image);
                    name = dataSnapshot.child("name").getValue().toString();
                    display_desc.setText(dataSnapshot.child("postDescription").getValue().toString());
                    display_name.setText(dataSnapshot.child("name").getValue().toString());
                    display_timestamp.setText(dataSnapshot.child("timeStamp").getValue().toString());
                }
                catch (Exception e){
                    Toast.makeText(Comments.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });



        myRef.child("Forums").child(key).child("comments").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listItems.clear();


                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    listItem= new CommentList("" + ds.child("timestamp").getValue(),"" + ds.child("name").getValue(),"" + ds.child("message").getValue());
                    listItems.add(listItem);

                }
                adapter=new CommentAdapter(listItems, Comments.this);
                recyclerView.setAdapter(adapter);


//                display_desc.setText(dataSnapshot.child("name").getValue().toString());
//                display_name.setText(dataSnapshot.child("postDescription").getValue().toString());
//                display_timestamp.setText(dataSnapshot.child("timeStamp").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CommentFirebase commentFirebase = new CommentFirebase();
                commentFirebase.setComment(commentContent.getText().toString().trim());
                commentFirebase.setTimestamp(currentDate);
                commentFirebase.setName(Firebaseuser.getDisplayName());

                myRef.child("Forums").child(key).child("comments").child(myRef.push().getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("message").setValue(commentFirebase.getComment());
                        dataSnapshot.getRef().child("timestamp").setValue(commentFirebase.getTimestamp());
                        dataSnapshot.getRef().child("name").setValue(commentFirebase.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });
                commentContent.setText("");
            }
        });



//        for(int i=0;i<10;i++)
//        {
//            CommentList listItem= new CommentList("12 min ago" + i+1,"Omkar Raykar","He is very very very smart");
//            listItems.add(listItem);
//        }


    }
}
