package com.example.a1dproject_campustradingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class Product extends AppCompatActivity {

    private String key;
    private ImageView mImageView;
    private TextView nameView;
    private TextView priceView;
    private TextView descriptionView;
    private TextView contactView;
    private TextView CategoryView;
    private Button addButton;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentUser;
    private DatabaseReference lookForUser;
    private DatabaseReference mFavourites;
    private Boolean checkAdded;

    public void setCheckAdded() {
        checkAdded = false;
    }

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
        CategoryView = (TextView)findViewById(R.id.detail_category);
        addButton = (Button) findViewById(R.id.favourite_btn);
        addButton.setText("Add to cart");
        checkAdded = true;

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
                priceView.setText(String.valueOf(upload.getmPrice())+"sgd");
                descriptionView.setText(String.valueOf(upload.getmDescription()));
                contactView.setText(String.valueOf(upload.getmContactInfo()));
                CategoryView .setText(String.valueOf(upload.getmCategory()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("admin", "Database error");
            }
        });

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser(); // look for the current user
        String userKey = mCurrentUser.getUid();
        lookForUser = FirebaseDatabase.getInstance().getReference().child(userKey);
        Log.i("Current User: ", String.valueOf(lookForUser.getKey()));

        mFavourites = FirebaseDatabase.getInstance().getReference().child(userKey).child("favourites"); // create a new node for favourite items
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFavourites != null) {
                    final Favourites favourites = new Favourites();
                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // make copy of the item's information
                            Upload fUpload = dataSnapshot.getValue(Upload.class);
                            favourites.setfImageURL(fUpload.getmImageUrl());
                            favourites.setfName(fUpload.getmName());
                            favourites.setfPrice(fUpload.getmPrice());
                            favourites.setfCategory(fUpload.getmCategory());
                            favourites.setfContact(fUpload.getmContactInfo());
                            favourites.setfDescription(fUpload.getmDescription());

                            Log.i("favourites", String.valueOf(favourites.getfName()));
                            mFavourites.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) { // check if this newly copied item already in the favourites list
                                        Favourites checkFavourite = dataSnapshot1.getValue(Favourites.class);
                                        if (String.valueOf(checkFavourite.getfImageURL()).equals(favourites.getfImageURL())) {
                                            setCheckAdded();
                                            addButton.setText("Item already in cart");
                                            break;
                                        }
                                    }
                                    if (checkAdded) {
                                        mFavourites.push().setValue(favourites);
                                        Toast.makeText(Product.this, "Item added to cart", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.i("Admin", "Database error");
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("Admin", "Database error");
                        }
                    });

                } else {
                    Log.i("Admin", "No node found");
                }
            }
        });

    }
}



