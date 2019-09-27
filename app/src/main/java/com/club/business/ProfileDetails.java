package com.club.business;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.club.business.Firebase.UserFirebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.view.View.VISIBLE;

public class ProfileDetails extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    int imageaddedcode;
    Button update;
    EditText pd_name,pd_email,pd_contact,b_name,b_gstin,b_city,b_haves,b_wants;
    Spinner b_industry,b_scale,b_nature;
    FirebaseDatabase mDatabase;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    CircularImageView display_picure;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Club");
        mAuth = FirebaseAuth.getInstance();
        imageaddedcode = 5149;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        update= findViewById(R.id.profile_details_btn_update);
        pd_name=findViewById(R.id.et_username);
        pd_email=findViewById(R.id.et_email);
        pd_contact=findViewById(R.id.et_phone);
        b_name=findViewById(R.id.profile_details_business_name);
        b_gstin=findViewById(R.id.profile_details_business_GSTIN_number);
        b_city=findViewById(R.id.profile_details_business_city);
        b_haves=findViewById(R.id.profile_details_haves);
        b_wants=findViewById(R.id.profile_details_wants);
        b_industry=findViewById(R.id.profile_details_business_type);
        b_scale=findViewById(R.id.profile_details_business_scale);
        b_nature=findViewById(R.id.profile_details_business_nature);
        display_picure = findViewById(R.id.activity_profile_profile_display_picture);


        pd_name.setEnabled(false);
        pd_name.setFocusableInTouchMode(false);
        pd_name.setInputType(InputType.TYPE_NULL);

        pd_email.setEnabled(false);
        pd_email.setFocusableInTouchMode(false);
        pd_email.setInputType(InputType.TYPE_NULL);


        pd_contact.setEnabled(false);
        pd_contact.setFocusableInTouchMode(false);
        pd_contact.setInputType(InputType.TYPE_NULL);


        firebaseUser = mAuth.getCurrentUser();
        myRef.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    pd_name.setText(dataSnapshot.child("username").getValue().toString());
                    pd_email.setText(dataSnapshot.child("email").getValue().toString());
                    pd_contact.setText(dataSnapshot.child("contact").getValue().toString());
                    b_name.setText(dataSnapshot.child("BusinessName").getValue().toString());
                    b_gstin.setText(dataSnapshot.child("GSTIN").getValue().toString());
//                            b_industry.select(dataSnapshot.child("contact").getValue().toString());
//                            b_scale.setText(dataSnapshot.child("contact").getValue().toString());
//                            b_nature.setText(dataSnapshot.child("contact").getValue().toString());
                    b_city.setText(dataSnapshot.child("City").getValue().toString());
                    b_haves.setText(dataSnapshot.child("Have").getValue().toString());
                    b_wants.setText(dataSnapshot.child("Want").getValue().toString());


                    firebaseUser = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(dataSnapshot.child("displayurl").getValue().toString()))
                            .build();

                    firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileDetails.this, "User Updated!", Toast.LENGTH_SHORT).show();
                                        //Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });




                    Toast.makeText(getApplicationContext(), "Profile Details Generated", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });


        display_picure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(ProfileDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ProfileDetails.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(ProfileDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else {
                        Intent dp = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(dp,PICK_IMAGE);

                    }
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog pd = new ProgressDialog(ProfileDetails.this);
                pd.setMessage("Updating Profile..");
                pd.show();

                final UserFirebase user=new UserFirebase();

                user.setBusinessName(b_name.getText().toString().trim());
                user.setGSTIN(b_gstin.getText().toString().trim());
                user.setIndustry(b_industry.getSelectedItem().toString().trim());
                user.setScale(b_scale.getSelectedItem().toString().trim());
                user.setNature(b_nature.getSelectedItem().toString().trim());
                user.setCity(b_city.getText().toString().trim() + ", India");
                user.setHaves(b_haves.getText().toString().trim());
                user.setWants(b_wants.getText().toString().trim());

                firebaseUser = mAuth.getCurrentUser();

                myRef.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {


                        if(imageaddedcode != 5149){
                            final StorageReference ref = storageReference.child("displayPicture/" + UUID.randomUUID().toString());
                            UploadTask uploadTask = ref.putFile(imageUri);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        final Uri downloadUri = task.getResult();
                                             //StaticValues.imageURL=downloadUri.toString();
                                        dataSnapshot.getRef().child("Username").setValue(user.getUsername());
                                        dataSnapshot.getRef().child("BusinessName").setValue(user.getBusinessName());
                                        dataSnapshot.getRef().child("GSTIN").setValue(user.getGSTIN());
                                        dataSnapshot.getRef().child("Industry").setValue(user.getIndustry());
                                        dataSnapshot.getRef().child("Scale").setValue(user.getScale());
                                        dataSnapshot.getRef().child("Nature").setValue(user.getNature());
                                        dataSnapshot.getRef().child("City").setValue(user.getCity());
                                        dataSnapshot.getRef().child("Have").setValue(user.getHaves());
                                        dataSnapshot.getRef().child("Want").setValue(user.getWants());
                                        dataSnapshot.getRef().child("displayurl").setValue(downloadUri.toString());
                                    } else {
                                        // Handle failures
                                        // ...
                                    }

                                }
                            });
                        }
                        Toast.makeText(ProfileDetails.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });

                startActivity(new Intent(ProfileDetails.this,HomePage.class));
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            imageaddedcode = resultCode;
            Toast.makeText(this, ""+resultCode, Toast.LENGTH_SHORT).show();
            imageUri = data.getData();
            display_picure.setImageURI(imageUri);
        }
    }

    public void sendtoMain(){
        Intent intent = new Intent(ProfileDetails.this,Login.class);
        startActivity(intent);
        finish();

    }
}
