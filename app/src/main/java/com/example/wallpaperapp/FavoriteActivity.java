package com.example.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.example.wallpaperapp.adapter.FavoriteAdapter;
import com.example.wallpaperapp.model.WallpaperModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    FavoriteAdapter adapter;
    Gson gson;
    private RecyclerView recyclerView;
    List<WallpaperModel> bookmarkList;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String BOOKMARK_PREF = "bookmarkPrefs";
    public static final String BOOKMARK_LIST = "bookmarkList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        innit();

        getImages();

        GridLayoutManager layoutManager = new GridLayoutManager(this , 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter= new FavoriteAdapter(bookmarkList, this);

        recyclerView.setAdapter(adapter);

        onDateHandle();

    }

    private void onDateHandle(){
        adapter.OnImageRemoved(new FavoriteAdapter.OnImageRemoved() {
            @Override
            public void onImageRemoved(int position) {

                bookmarkList.remove(position);

                adapter.notifyDataSetChanged();

            }
        });
    }
    private void storeImage(){
        String json = gson.toJson(bookmarkList);
        editor = preferences.edit();
        editor.putString(BOOKMARK_LIST, json);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeImage();
    }
    private void innit(){
        recyclerView = findViewById(R.id.recyclerView);
        preferences = getSharedPreferences(BOOKMARK_PREF, MODE_PRIVATE);
        gson = new Gson();
    }
    private void getImages(){
        String json = preferences.getString(BOOKMARK_LIST, "");

        Type type = new TypeToken<List<WallpaperModel>>(){

        }.getType();

        bookmarkList = gson.fromJson(json, type);

        if (bookmarkList == null){
            bookmarkList = new ArrayList<>();
        }
    }
}