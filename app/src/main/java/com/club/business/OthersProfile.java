package com.club.business;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class OthersProfile extends AppCompatActivity {

    Button viewbusinessdetails;
    TableLayout table;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference("Club");
    FirebaseUser Firebaseuser;
    TextView name,businessname,city,email,ask,have,GSTIN,industry,natureofbusiness,businesstype;
    String getUser;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        Firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        viewbusinessdetails = findViewById(R.id.others_profile_view_details);
        table = findViewById(R.id.others_profile_business_detail_table);

        key = getIntent().getStringExtra("key");
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        name = findViewById(R.id.profile_fullname);
        businessname = findViewById(R.id.profile_business_name);
        city = findViewById(R.id.profile_location);
        email = findViewById(R.id.activity_others_profile_email);
        ask = findViewById(R.id.activity_others_profile_iask);
        have = findViewById(R.id.activity_others_profile_ihave);
        GSTIN = findViewById(R.id.activity_others_profile_gstin);
        industry = findViewById(R.id.activity_others_profile_industry);
        natureofbusiness = findViewById(R.id.activity_others_profile_natue_of_business);
        businesstype = findViewById(R.id.activity_others_profile_business_type);

        myRef.child("Forums").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getUser= dataSnapshot.child("uid").getValue().toString();
                Toast.makeText(OthersProfile.this, ""+getUser, Toast.LENGTH_SHORT).show();
                //Nested Call
                myRef.child("users").child(getUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            //FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                            name.setText(dataSnapshot.child("name").getValue().toString());
                            businessname.setText(dataSnapshot.child("BusinessName").getValue().toString());
                            city.setText(dataSnapshot.child("City").getValue().toString());
                            email.setText(dataSnapshot.child("email").getValue().toString());
                            ask.setText(dataSnapshot.child("Want").getValue().toString());
                            have.setText(dataSnapshot.child("Have").getValue().toString());
                            GSTIN.setText(dataSnapshot.child("GSTIN").getValue().toString());
                            industry.setText(dataSnapshot.child("Industry").getValue().toString());
                            natureofbusiness.setText(dataSnapshot.child("Nature").getValue().toString());
                            businesstype.setText(dataSnapshot.child("Scale").getValue().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(OthersProfile.this, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OthersProfile.this, "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        viewbusinessdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.setVisibility(View.VISIBLE);
            }
        });
    }
}