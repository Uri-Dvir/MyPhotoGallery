package com.example.myphotogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Bundle b = getIntent().getExtras();
        int image = b.getInt("image");

        ImageView fullImage = findViewById(R.id.fullImage);
        fullImage.setImageResource(image);

    }
}