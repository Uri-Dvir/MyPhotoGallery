package com.example.myphotogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Bundle b = getIntent().getExtras();
        String image = b.getString("image");

        ImageView fullImage = findViewById(R.id.fullImage);
        Glide.with(this).load(image).into(fullImage);

    }
}