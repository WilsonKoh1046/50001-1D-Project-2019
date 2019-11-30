package com.example.a1dproject_campustradingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView.LayoutManager linearLayoutManager;
    private ArrayList<Uploaded> list;    // * TODO
    private RecyclerView recyclerView;
    private homeFragmentAdapter adapter;
    private Activity context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView itemCount;
    private EditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity(); // get host activity
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.re_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Uploaded"); // * TODO
        list = new ArrayList<Uploaded>(); // list of the items we want to display           // * TODO
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        searchInput = (EditText) view.findViewById(R.id.search_input);

        if (databaseReference != null) { // check if our database is null
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list.clear(); //  important, to prevent duplicated entries
                        for (DataSnapshot snapShot: dataSnapshot.getChildren()) {
                            Uploaded items = snapShot.getValue(Uploaded.class); // * TODO
                            list.add(items);
                        }
                        Collections.reverse(list); // sort the items starting from most recent
                        adapter = new homeFragmentAdapter(list);
                        recyclerView.setAdapter(adapter);
                        itemCount = (TextView) view.findViewById(R.id.item_count);
                        itemCount.setText(getString(R.string.on_sales_string, Integer.toString(adapter.getItemCount())));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("Admin", "Database error");
                }
            });
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // swipe to refresh function
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Update();
                Toast.makeText(context, "News Feed Updated", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });

        // query function
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void search(String s) {
        ArrayList<Uploaded> result = new ArrayList<Uploaded>(); // * TODO
        for (Uploaded items: list) {                            // * TODO
            if (items.getName().toLowerCase().contains(s.toLowerCase())) { // match by keywords contained // * TODO
                result.add(items);
            }
        }
        adapter.filter(result);
        // Update in stock number of the search products
        itemCount.setText(getString(R.string.on_sales_string, Integer.toString(adapter.getItemCount())));
    }

    // update layout upon refreshing
    private void Update() {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.nav_home);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }
    }

    public class homeFragmentAdapter extends RecyclerView.Adapter {

        private ArrayList<Uploaded> items_list; // * TODO

        public homeFragmentAdapter(ArrayList<Uploaded> data) {  // * TODO
            this.items_list = data;
        }

        public class homeViewHolder extends RecyclerView.ViewHolder {
            private ImageView image;
            private TextView name;
            private TextView price;
            // * TODO: add item tag here

            public homeViewHolder(View view) {
                super(view);
                this.image = (ImageView) view.findViewById(R.id.thumbnail);
                this.name = (TextView) view.findViewById(R.id.pro_title);
                this.price = (TextView) view.findViewById(R.id.pro_price);
            }

            public void bindHolder(int position) {
                Picasso.get().load(items_list.get(position).getImageURL()).into(this.image);   // * TODO
                this.name.setText(items_list.get(position).getName());             // * TODO
                this.price.setText(items_list.get(position).getPrice());            // * TODO
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new homeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((homeViewHolder) holder).bindHolder(position);
        }

        @Override
        public int getItemCount() {
            return items_list.size();
        }

        public void filter(ArrayList<Uploaded> list) {  // * TODO
            items_list = list;
            notifyDataSetChanged();
        }
    }
}


