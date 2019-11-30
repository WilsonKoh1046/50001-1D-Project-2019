package com.example.a1dproject_campustradingapp;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class UploadFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextProductName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private EditText mEditTextPrice;
    private EditText mEditTextContactInfo;
    private EditText mEditTextDescription;

    private Uri mImageUri;  //reference to the image

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_user;

    private StorageTask mUploadTask;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        mButtonChooseImage = view.findViewById(R.id.button_choose_image);
        mButtonUpload = view.findViewById(R.id.button_upload);
        mTextViewShowUploads = view.findViewById(R.id.textview_show_uploads);
        mEditTextProductName = view.findViewById(R.id.edit_text_file_name);
        mImageView = view.findViewById(R.id.image_view);
        mProgressBar = view.findViewById(R.id.progreess_bar);
        mEditTextPrice = view.findViewById(R.id.edit_text_price);
        mEditTextDescription = view.findViewById(R.id.edit_text_description);
        mEditTextContactInfo = view.findViewById(R.id.edit_text_contact_info);

        //TODO supply the spinner with the array using an instance of ArrayAdapter
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner_type);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.category_array,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

                //save in a folder called "uploads" in the storage
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if upload is in progress, we unable upload
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(context, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                }
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
        return view;
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
            mImageUri = data.getData();   //get back the uri of the image we pick

            //use Picasso to load the image picked
            Picasso.get().load(mImageUri).into(mImageView);
            //another way not using Picasso
            //mImageView.setImageURI(mImageUri);
        }
    }

    //TODO get the file extention of an image
    private String getFileExtension(Uri uri){
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(){
        //check if user pick an image
        //if the user picked an image, store it in storage
        if (mImageUri != null){
            //create a child with the unique name
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            //use mUploadTask to see if there is upload that currently running
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //user see the progress bar pressing in 5 seconds
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500); //delay the progress bar to see the processing

                            Toast.makeText(context, "Upload Successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(mEditTextProductName.getText().toString().trim(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(),
                                    mEditTextPrice.getText().toString().trim(), mEditTextContactInfo.getText().toString().trim(),
                                    mEditTextDescription.getText().toString().trim());
                            String uploadId = mDatabaseRef.push().getKey(); //create a new entry in database with unique Id
                            mDatabaseRef.child(uploadId).setValue(upload);  //set the Id's data as upload which contains the name and the imageUrl

                            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mDatabaseRef_user = FirebaseDatabase.getInstance().getReference(currentUser);
                            mDatabaseRef_user.child(upload.getmName()).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //show a Toast message of error
                            Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //update the progress bar with the current progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    /taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int)progress);
                        }
                    });

        }
        else{
            //if the user didn't pick an image, show a toast
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void openImagesActivity(){
        Intent intent = new Intent(context, ImagesActivity.class);
        startActivity(intent);
    }
}

