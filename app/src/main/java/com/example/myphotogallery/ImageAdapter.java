package com.example.myphotogallery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<Image> images;
    GoogleSignInAccount account;

    public ImageAdapter() {
        images = new ArrayList<Image>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    images = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        try {
                            Image i = new Image(
                                    document.getId(),
                                    document.get("URL").toString()
                            );
                            images.add(i);
                        } catch (Exception e) {
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });
        db.collection("Images").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                images = new ArrayList<>();
                for (DocumentSnapshot document : value.getDocuments())
                {
                    try {
                        Image i = new Image(
                                document.getId(),
                                document.get("URL").toString()
                        );
                        images.add(i);
                    } catch (Exception e) {
                    }
                }
                notifyDataSetChanged();
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
        Image image = images.get(position);
        Glide.with(holder.image.getContext()).load(image.URL).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),FullImageActivity.class);
                i.putExtra("image",image);
                i.putExtra("user",account);
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
        Uri file = Uri.fromFile(new File(path));
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(file.getLastPathSegment()).putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.child(file.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> image = new HashMap<>();
                        image.put("URL", uri.toString());
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Images").document(String.valueOf(System.currentTimeMillis()))
                                .set(image);
                    }
                });
            }
        });
    }

    public void setAccount(GoogleSignInAccount account) {
        this.account = account;
    }
}
