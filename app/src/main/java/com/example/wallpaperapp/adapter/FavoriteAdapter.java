package com.example.wallpaperapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import static com.example.wallpaperapp.FavoriteActivity.BOOKMARK_PREF;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wallpaperapp.R;
import com.example.wallpaperapp.model.WallpaperModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {
    private List<WallpaperModel> list;
    private Context context;

    public FavoriteAdapter(List<WallpaperModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private OnImageRemoved imageRemoved;


    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_items, parent , false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.FavoriteHolder holder, final int position) {
        Random random = new Random();

        int color = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getImage())
                .timeout(7500)
                .placeholder(new ColorDrawable(color))
                .into(holder.imageView);

        if (holder.imageMatched(position)){
            holder.actionButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favorite));
        }else {
            holder.actionButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border));
        }

        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.imageMatched(position)){
                    imageRemoved.onImageRemoved(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private FloatingActionButton actionButton;
        SharedPreferences preferences;
        Gson gson;
        int matchedPosition;
        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            actionButton = itemView.findViewById(R.id.actionBtn);

            preferences = context.getSharedPreferences(BOOKMARK_PREF, MODE_PRIVATE);
            gson = new Gson();

        }

        private boolean imageMatched(int currentPosition){

            int i = 0;
            boolean matched = false;

            for (WallpaperModel model : list){
                if (model.getId().equals(list.get(currentPosition).getId())
                        && model.getImage().equals(list.get(currentPosition).getImage())){
                    matched = true;
                    matchedPosition = i;
                }
                i++;
            }
            return matched;
        }


    }

    public interface OnImageRemoved {
        void onImageRemoved(int position);
    }

    public void OnImageRemoved(OnImageRemoved imageRemoved){
        this.imageRemoved = imageRemoved;
    }


}

