package com.club.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.club.business.Firebase.UserFirebase;
//import com.club.business.Session.SessionValue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button register,login;
    EditText login_contact_no,login_password;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    ProgressBar progressBar;
    DatabaseReference myRef = mDatabase.getReference("Club");
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        register = (Button)findViewById(R.id.btn_register);
        login = (Button)findViewById(R.id.btn_signin);
        login_contact_no = findViewById(R.id.activity_login_email);
        login_password = findViewById(R.id.activity_login_password);
        progressBar = findViewById(R.id.activity_login_progress_horizontal);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProgressDialog pd = new ProgressDialog(Login.this);
                pd.setMessage("Welcome back");
                pd.show();

                String username = login_contact_no.getText().toString();
                String password = login_password.getText().toString();



                
                if (!TextUtils.isEmpty(username) &&
                        !TextUtils.isEmpty(password)) // && TextUtils.isEmpty(password))
                     {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            
                            if (task.isSuccessful()){
                                sendToMain();
                            }else{

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error: "+errorMessage, Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,Register.class));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            sendToMain();
        }
    }

    public void sendToMain(){

        Intent intent = new Intent(Login.this,HomePage.class);
        startActivity(intent);
        finish();
    }
}
