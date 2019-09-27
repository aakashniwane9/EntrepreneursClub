package com.club.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.club.business.Firebase.UserFirebase;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView link;
    Button register;
    FirebaseUser firebaseUser;
    EditText username,fullname,contact,email,password,cnfpassword;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Club");
        mAuth = FirebaseAuth.getInstance();


        link = (TextView)findViewById(R.id.activity_register_login_text);
        register = findViewById(R.id.acivity_register_btn_register);
        username =findViewById(R.id.activity_register_et_username);
        email = findViewById(R.id.activity_register_et_email);
        fullname = findViewById(R.id.activity_register_et_fullname);
        contact = findViewById(R.id.activity_register_et_phone);
        password = findViewById(R.id.activity_register_et_password);
        cnfpassword = findViewById(R.id.activity_register_et_confirmpassword);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Register.this,Login.class);
                startActivity(i1);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(fullname.getText().toString().trim().isEmpty() ||  username.getText().toString().trim().isEmpty() ||
//                        password.getText().toString().trim().isEmpty() || contact.getText().toString().trim().isEmpty()
//                || email.getText().toString().trim().isEmpty()){
//                    Toast.makeText(Register.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
//                }
//
//
//               else if((password.getText().toString().trim()).equals(cnfpassword.getText().toString().trim()))
//                {
//                    UserFirebase user=new UserFirebase();
//                    user.setFullname(fullname.getText().toString().trim());
//                    user.setUsername(username.getText().toString().trim());
//                    user.setEmail(email.getText().toString().trim());
//                    user.setPassword(password.getText().toString().trim());
//                    user.setContactnumber(contact.getText().toString().trim());
//
//
//                    Map<String, Object> userValues = user.toMap();
//                    Map<String, Object> childUpdates = new HashMap<>();
//
//                    childUpdates.put(contact.getText().toString() , userValues);
//                    myRef.child("users/").updateChildren(childUpdates);
//
//
//                    Intent intent = new Intent(Register.this,Login.class);
//                    startActivity(intent);
//                    finish();
//                }
//                else{
//                    Toast.makeText(Register.this,"Passwords Do Not Match!!!",Toast.LENGTH_SHORT).show();
//                }

                ProgressDialog pd = new ProgressDialog(Register.this);
                pd.setMessage("Loading... ");
                pd.show();


                if(!TextUtils.isEmpty(email.getText().toString()) &&
                        !TextUtils.isEmpty(password.getText().toString()) &
                                !TextUtils.isEmpty(cnfpassword.getText().toString()) ){

                    if (password.getText().toString().equals(cnfpassword.getText().toString())){

                        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),
                                password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    myRef.child("users").child(contact.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                               UserFirebase user=new UserFirebase();
                                                user.setFullname(fullname.getText().toString().trim());
                                                user.setUsername(username.getText().toString().trim());
                                                user.setEmail(email.getText().toString().trim());
                                                user.setPassword(password.getText().toString().trim());
                                                user.setContactnumber(contact.getText().toString().trim());

                                                Map<String, Object> userValues = user.toMap();
                                                Map<String, Object> childUpdates = new HashMap<>();

                                                firebaseUser = mAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(fullname.getText().toString().trim())
                                                    .build();

                                            firebaseUser.updateProfile(profileUpdates);
                                                childUpdates.put(firebaseUser.getUid() , userValues);
                                                myRef.child("users/").updateChildren(childUpdates);

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.d("User", databaseError.getMessage());
                                        }
                                    });


                                    sendtoMain();

                                }else{
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Register.this, "Error: "+errorMessage, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }else {

                        Toast.makeText(Register.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }

                }
//
//                UserFirebase user=new UserFirebase();
//                    user.setFullname(fullname.getText().toString().trim());
//                    user.setUsername(username.getText().toString().trim());
//                    user.setEmail(email.getText().toString().trim());
//                    user.setPassword(password.getText().toString().trim());
//                    user.setContactnumber(contact.getText().toString().trim());
//
//
//                    Map<String, Object> userValues = user.toMap();
//                    Map<String, Object> childUpdates = new HashMap<>();
//
//                    childUpdates.put(contact.getText().toString() , userValues);
//                    myRef.child("users/").updateChildren(childUpdates);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            sendtoMain();
        }
    }

    public void sendtoMain(){
        Intent intent = new Intent(Register.this,Login.class);
        startActivity(intent);
        finish();
    }
}

