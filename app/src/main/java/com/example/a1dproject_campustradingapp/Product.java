package com.example.a1dproject_campustradingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Product extends AppCompatActivity {
    private LinearLayout mGallery;
    private int[] mImgIds;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mInflater = LayoutInflater.from(this);
        initData();
        initView();
    }

    private void initData()
    {
        mImgIds = new int[] { R.drawable.product_book1, R.drawable.product_book2, R.drawable.product_book3
        };
    }

    private void initView()
    {
        mGallery = (LinearLayout) findViewById(R.id.linear);

        for (int i = 0; i < mImgIds.length; i++)
        {

            View view = mInflater.inflate(R.layout.product_item,
                    mGallery, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.id_index_gallery_item_image);
            img.setImageResource(mImgIds[i]);
            mGallery.addView(view);
        }
    }



    }



