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
import java.util.HashMap;

import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView.LayoutManager linearLayoutManager;
    private ArrayList<Upload> list;
    private RecyclerView recyclerView;
    private homeFragmentAdapter adapter;
    private Activity context;
    private SwipeRefreshLayout swipeRefreshLayout;
    // private TextView itemCount;
    private EditText searchInput;
    private HashMap<String, Upload> mHashMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity(); // get host activity
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.re_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads");
        list = new ArrayList<Upload>(); // list of the items we want to display
        // itemCount = (TextView) view.findViewById(R.id.item_count);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        searchInput = (EditText) view.findViewById(R.id.search_input);
        mHashMap = new HashMap<String, Upload>();

        if (databaseReference != null) { // check if our database is null
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list.clear(); //  important, to prevent duplicated entries
                        for (DataSnapshot snapShot: dataSnapshot.getChildren()) {
                            Upload items = snapShot.getValue(Upload.class);
                            String key = snapShot.getKey();
                            list.add(items);
                            mHashMap.put(key, items);
                        }
                        Collections.reverse(list); // sort the items starting from most recent
                        adapter = new homeFragmentAdapter(list, mHashMap);
                        recyclerView.setAdapter(adapter);
                        // itemCount.setText(getString(R.string.on_sales_string, Integer.toString(adapter.getItemCount())));
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
        ArrayList<Upload> result = new ArrayList<Upload>();
        for (Upload items: list) {
            if (items.getmName().toLowerCase().contains(s.toLowerCase())) { // match by keywords contained
                result.add(items);
            }
        }
        adapter.filter(result);
        // Update in stock number of the search products
        // itemCount.setText(getString(R.string.on_sales_string, Integer.toString(adapter.getItemCount())));
    }

    // update layout upon refreshing
    private void Update() {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_section);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }
    }

    public class homeFragmentAdapter extends RecyclerView.Adapter<homeFragmentAdapter.homeViewHolder> {

        private ArrayList<Upload> items_list;
        private HashMap<String, Upload> key_values;

        public homeFragmentAdapter(ArrayList<Upload> data, HashMap<String, Upload> keyList) {
            this.items_list = data;
            this.key_values = keyList;
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
        }

        @NonNull
        @Override
        public homeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new homeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull homeViewHolder holder, int position) {
            final Upload upload = items_list.get(position);
            Picasso.get().load(upload.getmImageUrl()).placeholder(R.drawable.ic_insert_photo_black_24dp).fit().centerCrop().into(holder.image);
            holder.name.setText(upload.getmName());
            holder.price.setText(upload.getmPrice());

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dataKey = "";
                    Intent intent = new Intent(v.getContext(), Product.class);
                    for (String key: key_values.keySet()) {
                        if (key_values.get(key) == upload) { // get the key of the current item
                            dataKey += key;
                        }
                    }
                    intent.putExtra("key", dataKey); // pass this key to the individual product page
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items_list.size();
        }

        public void filter(ArrayList<Upload> list) {
            items_list = list;
            notifyDataSetChanged();
        }
    }
}


