package com.example.a1dproject_campustradingapp;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Favourites> mFavourites;
    private OnItemClickListener mListener;
    private DatabaseReference uploadTable;

    public ImageAdapter(Context context, List<Favourites> favourites){
        mContext = context;
        mFavourites = favourites;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    //get data from upload item and
    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final Favourites favoriteCurrent = mFavourites.get(position);
        holder.textViewName.setText(favoriteCurrent.getfName());

        //Glide.with(mContext).load(uploadCurrent.getmImageUrl()).into(holder.imageView);
        Picasso.get().load(favoriteCurrent.getfImageURL())
                .placeholder(R.mipmap.ic_launcher).centerInside()
                .fit().into(holder.imageView);

        uploadTable = FirebaseDatabase.getInstance().getReference().child("uploads");
        uploadTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> imageUrlList = new ArrayList<String>();
                for (DataSnapshot mdataSnapshot: dataSnapshot.getChildren()) {
                    Upload mupload = mdataSnapshot.getValue(Upload.class);
                    imageUrlList.add(mupload.getmImageUrl());
                }

                if (imageUrlList.contains(favoriteCurrent.getfImageURL())) {
                    holder.statusView.setText("In Stock");
                } else {
                    holder.statusView.setText("Sold Out");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Admin", "Database error");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFavourites.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName; //display the name
        public ImageView imageView;
        public TextView statusView;

        public ImageViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            statusView = itemView.findViewById(R.id.stock_status);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");

            MenuItem delete = menu.add(Menu.NONE, 1,1,"Delete");

            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    //this interface will forward our click to the images_activity
    public interface OnItemClickListener{
        void onItemClick(int position);

        //void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
