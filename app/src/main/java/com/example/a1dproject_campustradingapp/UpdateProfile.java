package com.example.a1dproject_campustradingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {
    private EditText changename;
    private EditText changeemail;
    private EditText changeid;
    private TextView textViewChangePhoto;
    private Button savebtn;
    private ImageView profileImageView;
    private Uri profileImageUri;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        changename=findViewById(R.id.changename);
        changeemail=findViewById(R.id.changeemail);
        changeid=findViewById(R.id.changeid);
        savebtn=findViewById(R.id.savebtn);
        textViewChangePhoto = findViewById(R.id.textview_profile_photo);
        profileImageView = findViewById(R.id.imageView_profile);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                changename.setText(userProfile.getName());
                changeemail.setText(userProfile.getEmail());
                changeid.setText(userProfile.getID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfile.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=changename.getText().toString();
                String email=changeemail.getText().toString();
                String id=changeid.getText().toString();
                UserProfile userProfile=new UserProfile(name,email,id);
                databaseReference.setValue(userProfile);
                finish();
                startActivity(new Intent(UpdateProfile.this,Home.class));
            }
        });

        textViewChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //to find the data we get back, identify the data by PICK_IMAGE_REQUEST
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //TODO this method will be called when we pick the file
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check for image request, if the user actually pick the image
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null){
            profileImageUri = data.getData();   //get back the uri of the image we pick

            //use Picasso to load the image picked
            Picasso.get().load(profileImageUri).into(profileImageView);
            //another way not using Picasso
            //mImageView.setImageURI(mImageUri);
        }
    }

    private void openImagesActivity(){
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }
}
