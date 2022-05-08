package com.example.myphotogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Bundle b = getIntent().getExtras();
        Image image = (Image) b.getSerializable("image");
        GoogleSignInAccount account = (GoogleSignInAccount) b.get("user");

        ImageView fullImage = findViewById(R.id.fullImage);
        Glide.with(this).load(image.URL).into(fullImage);

        FloatingActionButton btn = findViewById(R.id.comments);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FullImageActivity.this,CommentsActivity.class);
                i.putExtra("image",image);
                i.putExtra("user",account);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(),
                                btn,
                                "comm"
                        );
                view.getContext().startActivity(i,options.toBundle());
            }
        });

    }
}