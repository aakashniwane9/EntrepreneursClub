package com.club.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.club.business.Adapters.ForumList;
import com.club.business.Adapters.MyAdapater;
import com.club.business.Firebase.UserFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class ProfileFragment extends Fragment {

    Button update;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ForumList> listItems;
    CircularImageView displaypicture;
    FirebaseDatabase mDatabase;
    TextView postcount;
    ForumList listItem;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    TextView username,name,businessname,city;


    DatabaseReference myRef;
    int intpostcount;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.fragment_profile,container,false);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Club");
        mAuth = FirebaseAuth.getInstance();

        postcount = v.findViewById(R.id.profile_card_data_post_count);

        username = v.findViewById(R.id.fragment_profile_username);
        name = v.findViewById(R.id.fragment_profile_fullname);
        businessname = v.findViewById(R.id.fragment_profile_business_name);
        city = v.findViewById(R.id.fragment_profile_location);
        String val = postcount.getText().toString().trim();
        displaypicture = v.findViewById(R.id.fragment_profile_image);




        firebaseUser = mAuth.getCurrentUser();
        try{
            Picasso.get().load(firebaseUser.getPhotoUrl()).into(displaypicture);
        }catch (Exception e){

            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
        }

        myRef.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    username.setText(dataSnapshot.child("username").getValue().toString());
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    businessname.setText(dataSnapshot.child("BusinessName").getValue().toString());
                    city.setText(dataSnapshot.child("City").getValue().toString());

                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });


        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        myRef.child("Forums").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if((ds.child("name").getValue()).equals(firebaseUser.getDisplayName())){
                        intpostcount++;
                    }
                }
                postcount.setText("" + intpostcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });




        update=v.findViewById(R.id.activity_profile_profile_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ProfileDetails.class));
            }
        });


        return v;
    }

}
