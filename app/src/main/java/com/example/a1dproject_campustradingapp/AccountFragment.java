package com.example.a1dproject_campustradingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private AccountSellingAdapter mAdapter;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Upload> mUploads;
    private ImageView imageview;
    private TextView nametext;
    private TextView idtext;
    private TextView emailtext;
    private Button editbtn, logoutbtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    Activity acccontext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        acccontext = getActivity();
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        imageview=view.findViewById(R.id.imageview);
        nametext=view.findViewById(R.id.nametext);
        idtext=view.findViewById(R.id.idtext);
        emailtext=view.findViewById(R.id.emailtext);
        editbtn=view.findViewById(R.id.editbtn);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                nametext.setText("Name:  "+userProfile.getName());
                idtext.setText("ID:  "+userProfile.getID());
                emailtext.setText("Email:  "+userProfile.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(acccontext,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
        /*
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(acccontext,UpdateProfile.class));
            }
        });

         */
        mRecyclerView = view.findViewById(R.id.account_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(acccontext));

//        mAdapter.setOnItemClickListener(ImagesActivity.this);


        mUploads = new ArrayList<>();

        mAdapter = new AccountSellingAdapter(acccontext, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.setOnItemClickListener(AccountSellingActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                //dataSnapshot is a list which represent our data at the mDatabaseRef
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setmKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter = new AccountSellingAdapter(acccontext, mUploads);
                //mAdapter.notifyDataSetChanged();

                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //this will be called when there is an error
                Toast.makeText(acccontext, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



//        Intent intent = new Intent(acccontext, AccountSellingActivity.class);
//        startActivity(intent);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();

        Button btn = (Button)acccontext.findViewById(R.id.editbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acccontext.finish();
                startActivity(new Intent(acccontext,UpdateProfile.class));
            }
        });
        Button btn1 = (Button)acccontext.findViewById(R.id.btnLogout);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                acccontext.finish();
                startActivity((new Intent(acccontext,LoginActivity.class)));
            }
        });

    }
}