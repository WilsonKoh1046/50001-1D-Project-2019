package com.example.a1dproject_campustradingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Product extends AppCompatActivity {

    private String key;
    private ImageView mImageView;
    private TextView nameView;
    private TextView priceView;
    private TextView descriptionView;
    private TextView contactView;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mImageView = (ImageView) findViewById(R.id.detail_image);
        nameView = (TextView) findViewById(R.id.detail_name);
        priceView = (TextView) findViewById(R.id.detail_price);
        descriptionView = (TextView) findViewById(R.id.detail_description);
        contactView = (TextView) findViewById(R.id.detail_contact);

        if (bundle != null) {
            key = (String) bundle.get("key"); // receive the item key from the card
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("uploads").child(key); // look for the specific entry in database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Upload upload = dataSnapshot.getValue(Upload.class);
                Log.i("Name", upload.getmName());
                Log.i("Price", upload.getmPrice());
                Log.i("Description", upload.getmDescription());
                Log.i("Contact Info", upload.getmContactInfo());

                Picasso.get().load(upload.getmImageUrl()).placeholder(R.drawable.ic_insert_photo_black_24dp).fit().centerCrop().into(mImageView);
                nameView.setText(String.valueOf(upload.getmName()));
                priceView.setText(String.valueOf(upload.getmPrice()));
                descriptionView.setText(String.valueOf(upload.getmDescription()));
                contactView.setText(String.valueOf(upload.getmContactInfo()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("admin", "Database error");
            }
        });
    }
}



