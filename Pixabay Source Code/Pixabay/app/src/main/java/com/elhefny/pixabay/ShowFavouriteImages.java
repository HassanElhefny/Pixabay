package com.elhefny.pixabay;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.elhefny.pixabay.DataBase.AppExecutor;
import com.elhefny.pixabay.DataBase.FavouriteDatabase;
import com.elhefny.pixabay.DataBase.UserFavouriteImagesEntity;
import com.elhefny.pixabay.RecyclerClasses.FavouriteImagesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowFavouriteImages extends AppCompatActivity {
    AppExecutor appExecutor = new AppExecutor();
    FavouriteDatabase favouriteDatabase;
    List<UserFavouriteImagesEntity> favouriteImagesEntityList = new ArrayList<>();
    FavouriteImagesRecyclerAdapter adapter;
    RecyclerView rv_images;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favourite_images);
        initializeViews();
        buildRecycler();
        getDataFromDatabase();
        if (NetworkUtil.getConnectivityStatusString(this).equals("No internet is available")) {
            Toast.makeText(this, getString(R.string.NoInternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    private void buildRecycler() {
        adapter = new FavouriteImagesRecyclerAdapter(favouriteImagesEntityList, this, new FavouriteImagesRecyclerAdapter.OnImageFavouriteClicked() {
            @Override
            public void deleteImage(int imageId, int imagePosition) {
                appExecutor.getmDisk_IO().execute(new Runnable() {
                    @Override
                    public void run() {
                        favouriteDatabase.favouriteDao().deleteImageFromFavourite(imageId);
                        rv_images.post(new Runnable() {
                            @Override
                            public void run() {
                                if (favouriteImagesEntityList.size() == 0) {
                                    ErrorFragment errorFragment = ErrorFragment.newInstance();
                                    errorFragment.show(getSupportFragmentManager(), null);
                                } else {
                                    adapter.notifyItemRemoved(imagePosition);
                                    favouriteImagesEntityList.remove(imagePosition);
                                    if (favouriteImagesEntityList.size() == 0) {
                                        ErrorFragment errorFragment = ErrorFragment.newInstance();
                                        errorFragment.show(getSupportFragmentManager(), null);
                                    }
                                }
                            }
                        });
                    }
                });

            }
        });
        rv_images.setLayoutManager(new LinearLayoutManager(this));
        rv_images.setAdapter(adapter);
    }

    private void initializeViews() {
        sp = getSharedPreferences(getString(R.string.SharedPreferencesName), MODE_PRIVATE);
        rv_images = findViewById(R.id.show_favourite_images_recycler_view);
    }

    private void getDataFromDatabase() {
        favouriteDatabase = FavouriteDatabase.getInstance(this);
        appExecutor.getmDisk_IO().execute(new Runnable() {
            @Override
            public void run() {
                favouriteImagesEntityList.addAll(favouriteDatabase.favouriteDao().getAllImagesInFavourite(sp.getString(getString(R.string.SharedPreferencesUserName), "User 1")));
                rv_images.post(new Runnable() {
                    @Override
                    public void run() {
                        if (favouriteImagesEntityList.size() == 0) {
                            ErrorFragment errorFragment = ErrorFragment.newInstance();
                            errorFragment.show(getSupportFragmentManager(), null);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                Log.e("TAG", "run: " + favouriteImagesEntityList.size());
                Log.e("TAG", "run: " + sp.getString(getString(R.string.SharedPreferencesUserName), "User 1"));
            }
        });
    }
}