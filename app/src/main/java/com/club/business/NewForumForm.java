package com.club.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.club.business.Adapters.StaticValues;
import com.club.business.Firebase.NewPost;
import com.club.business.Firebase.UserFirebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.view.View.VISIBLE;

public class NewForumForm extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton add_image;
    CardView image_card;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    EditText newforumpost,subject;
    ImageView imageupload;
    CircularImageView displaypicture;
    int imageaddedcode;
    FirebaseStorage storage;
    StorageReference storageReference;
    Button postnewforum;
    FirebaseDatabase mDatabase;
    public String imageurl;
    DatabaseReference myRef;

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forum_form);

        imageaddedcode = 5149;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Club");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        displaypicture = findViewById(R.id.activity_new_forum_form_img_profile);

        Picasso.get().load(firebaseUser.getPhotoUrl()).into(displaypicture);



      toolbar=findViewById(R.id.newforum_toolbar);
      toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(NewForumForm.this,HomePage.class);
                startActivity(intent);

            }
        });
      newforumpost = findViewById(R.id.activity_new_forum_form_edt_post);
      add_image = findViewById(R.id.activity_new_forum_form_add_image);
      image_card = findViewById(R.id.activity_new_forum_form_imageholder);
      imageupload= findViewById(R.id.activity_new_forum_form_final_upload_image);
      postnewforum = findViewById(R.id.activity_new_forum_form_btn_post);
      subject = findViewById(R.id.activity_new_forum_form_edt_subject);

        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());


        postnewforum.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              postnewforum.setEnabled(false);
              firebaseUser = mAuth.getCurrentUser();


              ProgressDialog pd = new ProgressDialog(NewForumForm.this);
              pd.setMessage("getting you live");
              pd.show();


//              myRef.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//
//                  @Override
//                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                      try {
//                          newPost.setName(dataSnapshot.child("name").getValue().toString());
//                          Toast.makeText(NewForumForm.this, ""+newPost.getName(), Toast.LENGTH_SHORT).show();
//                      }catch (Exception e){
//                          e.printStackTrace();
//                          Toast.makeText(NewForumForm.this, "Failed to post", Toast.LENGTH_SHORT).show();
//                      }
//                  }
//
//                  @Override
//                  public void onCancelled(@NonNull DatabaseError databaseError) {
//                      Toast.makeText(NewForumForm.this, "Check your Internet", Toast.LENGTH_SHORT).show();
//                  }
//              });

              myRef.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      final NewPost newPost = new NewPost();
                      newPost.setForumpost(newforumpost.getText().toString().trim());
                      newPost.setTimestamp(currentDate);
                      newPost.setSubject(subject.getText().toString().trim());
                      firebaseUser = mAuth.getCurrentUser();
                      newPost.setUid(firebaseUser.getUid());


                      newPost.setName(dataSnapshot.child("name").getValue().toString());

                      myRef.child("Forums").child(myRef.push().getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(final DataSnapshot dataSnapshot) {

                              if(imageaddedcode != 5149){
                                  final StorageReference ref = storageReference.child("forumimages/" + UUID.randomUUID().toString());
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
//                                              StaticValues.imageURL=downloadUri.toString();
                                              dataSnapshot.getRef().child("postDescription").setValue(newPost.getForumpost());
                                              dataSnapshot.getRef().child("timeStamp").setValue(newPost.getTimestamp());
                                              dataSnapshot.getRef().child("name").setValue(newPost.getName());
                                              dataSnapshot.getRef().child("subject").setValue(newPost.getSubject());
                                              dataSnapshot.getRef().child("uid").setValue(newPost.getUid());
                                              dataSnapshot.getRef().child("imageurl").setValue(downloadUri.toString());
                                          } else {
                                              // Handle failures
                                              // ...
                                          }
                                      }
                                  });
                              }

                          }
                          @Override
                          public void onCancelled(DatabaseError databaseError) {
                              Log.d("User", databaseError.getMessage());
                          }
                      });
                      Toast.makeText(NewForumForm.this, "Posted", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });

              //Toast.makeText(NewForumForm.this, "outside "+newPost.getName(), Toast.LENGTH_SHORT).show();

              Intent intent = new Intent(NewForumForm.this,HomePage.class);
              startActivity(intent);

          }
      });
      add_image.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {

              add_image.setEnabled(false);
              Intent intentgallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
              startActivityForResult(intentgallery,PICK_IMAGE);
          }
      });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            imageaddedcode = resultCode;
            Toast.makeText(this, ""+resultCode, Toast.LENGTH_SHORT).show();
            imageUri = data.getData();
            imageupload.setImageURI(imageUri);
            image_card.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            sendtoMain();
        }
    }

    public void sendtoMain(){
        Intent intent = new Intent(NewForumForm.this,Login.class);
        startActivity(intent);
        finish();

    }
}
