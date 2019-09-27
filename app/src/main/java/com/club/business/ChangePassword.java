package com.club.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.club.business.Firebase.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

public class ChangePassword extends AppCompatActivity {
    EditText old_pwd,new_pwd,confirm_pwd;
    Button submit;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    ProgressDialog dialog;
    //int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Club");
        mAuth = FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);


        old_pwd = (EditText) findViewById(R.id.cp_old_password);
        new_pwd = (EditText) findViewById(R.id.cp_new_password);
        confirm_pwd = (EditText) findViewById(R.id.cp_cnf_new_password);
        submit = (Button) findViewById(R.id.cp_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((new_pwd.getText().toString().trim()).isEmpty() || (old_pwd.getText().toString().trim()).isEmpty() || (confirm_pwd.getText().toString().trim()).isEmpty()){
                    Toast.makeText(ChangePassword.this,"All fields are mandatory",Toast.LENGTH_SHORT).show();
                }
                else if(  !(new_pwd.getText().toString().trim()).equals((confirm_pwd.getText().toString().trim()))  ){
                    Toast.makeText(ChangePassword.this,"Passwords Do Not Match",Toast.LENGTH_SHORT).show();
                }
                else{
                    final String newPassword= new_pwd.getText().toString();


                    firebaseUser = mAuth.getCurrentUser();



                    myRef.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot){
                            String check=dataSnapshot.child("password").getValue().toString();
                            AuthCredential credential = EmailAuthProvider.getCredential(dataSnapshot.child("email").getValue().toString(), check);
                            firebaseUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //Toast.makeText(ChangePassword.this,"User Reauthenticated" , Toast.LENGTH_LONG).show();
                                        }
                                    });
                            if(newPassword.equals(check)){
                                Toast.makeText(ChangePassword.this,"Enter a different password \n(New Password same as Old password)" , Toast.LENGTH_LONG).show();
                            }
                            else if((old_pwd.getText().toString().trim()).equals(check)){

                                dataSnapshot.getRef().child("password").setValue(check);
                                if(firebaseUser!=null){
                                    dialog.setMessage("Changing Password,Please Wait!!!");
                                    dialog.show();

                                    firebaseUser.updatePassword(newPassword.trim())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        dataSnapshot.getRef().child("password").setValue(newPassword.trim());
                                                        dialog.dismiss();
                                                        Toast.makeText(ChangePassword.this,"Password Changed!" , Toast.LENGTH_LONG).show();
                                                        mAuth.signOut();
                                                        finish();
                                                        startActivity(new Intent(ChangePassword.this,Login.class));
                                                    }
                                                    else{
                                                        dialog.dismiss();
                                                        Toast.makeText(ChangePassword.this,"Error Encountered!" , Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            }
                            else{
                                Toast.makeText(ChangePassword.this,"Authentication Denied!" , Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("User", databaseError.getMessage());
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            finish();
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }
    }
}