package com.example.myphotogallery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<String> images;

    public ImageAdapter()
    {
        images = new ArrayList<String>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                   item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           images.add(uri.toString());
                           notifyDataSetChanged();
                       }
                   });
                }
            }
        });
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String image = images.get(position);
        Glide.with(holder.image.getContext()).load(image).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),FullImageActivity.class);
                i.putExtra("image",image);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(),
                                holder.image,
                                "imageTrasnition"
                        );
                view.getContext().startActivity(i,options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addImage(String path) {
        images.add(path);
        notifyDataSetChanged();
        Uri file = Uri.fromFile(new File(path));
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(file.getLastPathSegment()).putFile(file);
    }
}
