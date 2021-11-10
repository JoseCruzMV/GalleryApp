package com.example.galleryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    public final String IMAGE_PATH_LABEL = "imagePath";
    private final Context context;
    private final ArrayList<String> mImagePathArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<String> mImagePathArrayList) {
        this.context = context;
        this.mImagePathArrayList = mImagePathArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        File imageFile = new File(mImagePathArrayList.get(position));

        if (imageFile.exists()) {
            Picasso.get().load(imageFile).placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageDetailActivity.class);
                    intent.putExtra(IMAGE_PATH_LABEL, mImagePathArrayList.get(holder.getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImagePathArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
