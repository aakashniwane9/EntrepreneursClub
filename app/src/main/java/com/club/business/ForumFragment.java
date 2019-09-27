package com.club.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.club.business.Adapters.ForumList;
import com.club.business.Adapters.MyAdapater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ForumFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ForumList> listItems;
    ForumList listItem;
    FloatingActionButton addForum;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference("Club");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View v= inflater.inflate(R.layout.fragment_forum,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recylcer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        addForum=(FloatingActionButton) v.findViewById(R.id.fragforum_add_forum);
        addForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewForumForm.class));
                Toast.makeText(getContext(), ""+user.getUid(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), ""+user.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        });


        listItems=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        myRef.child("Forums").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    listItem= new ForumList("" + ds.child("name").getValue(),"" + ds.child("subject").getValue(),"" + ds.child("timeStamp").getValue(),""+ds.getKey(),""+ds.child("imageurl").getValue());
                    listItems.add(listItem);
                }
                adapter=new MyAdapater(listItems, getContext());
                recyclerView.setAdapter(adapter);


//                String datausername = "" + dataSnapshot.child(username).child("contact").getValue();
//                String datapassword = "" +dataSnapshot.child(username).child("password").getValue();
//                if (username.equals(datausername) && password.equals(datapassword)) {
//
//                    Intent intent = new Intent(Login.this, HomePage.class);
//                    startActivity(intent);
//                    finish();
//                }else
//                    Toast.makeText(Login.this, "Credentials Wrong...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
