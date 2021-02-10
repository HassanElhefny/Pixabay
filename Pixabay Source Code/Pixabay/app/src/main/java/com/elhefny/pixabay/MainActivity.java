package com.elhefny.pixabay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.elhefny.pixabay.DataBase.AppExecutor;
import com.elhefny.pixabay.DataBase.FavouriteDatabase;
import com.elhefny.pixabay.DataBase.UserFavouriteImagesEntity;
import com.elhefny.pixabay.RecyclerClasses.RecyclerImageAdapter;
import com.elhefny.pixabay.TargetImages.Hit;
import com.elhefny.pixabay.TargetImages.Images;
import com.elhefny.pixabay.TargetImages.RetrofitClient;
import com.elhefny.pixabay.TargetImages.jsonInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    jsonInterface api_;
    RecyclerView rv_images;
    RecyclerImageAdapter adapter;
    List<Hit> imageHits;
    FloatingActionButton btn_find;
    TextInputEditText et_search;
    int page = 1, max_limit = 10;
    AppExecutor appExecutor = new AppExecutor();
    FavouriteDatabase favouriteDatabase;
    SharedPreferences sp;
    LottieAnimationView animation_no_image;
    MaterialButton btn_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        if (NetworkUtil.getConnectivityStatusString(this).equals("No internet is available")) {
            rv_images.setVisibility(View.GONE);
            animation_no_image.setVisibility(View.VISIBLE);
        } else {
            getImagesFromAPI(null, page, max_limit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.favourite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favourite_menu) {
            Intent i = new Intent(MainActivity.this, ShowFavouriteImages.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildRecycler() {
        adapter = new RecyclerImageAdapter(this, imageHits, new RecyclerImageAdapter.onImageClicked() {
            @Override
            public void addImageToFavourite(Hit imagesHit) {
                favouriteDatabase = FavouriteDatabase.getInstance(MainActivity.this);
                if (imagesHit.getImageFavouriteResc() == R.drawable.uncolored_favourite) {
                    imagesHit.setImageFavouriteResc(R.drawable.colored_favourite);
                    appExecutor.getmDisk_IO().execute(new Runnable() {
                        @Override
                        public void run() {
                            favouriteDatabase.favouriteDao().addImageToFavourite(new UserFavouriteImagesEntity(
                                    imagesHit.getId(),
                                    imagesHit.getUserId(),
                                    sp.getString(getString(R.string.SharedPreferencesUserName), "User 1"),
                                    imagesHit.getWebformatURL(),
                                    imagesHit.getComments(),
                                    imagesHit.getLikes(),
                                    imagesHit.getViews(),
                                    imagesHit.getDownloads(),
                                    imagesHit.getTags()
                            ));
                        }
                    });
                } else {
                    imagesHit.setImageFavouriteResc(R.drawable.uncolored_favourite);
                    appExecutor.getmDisk_IO().execute(new Runnable() {
                        @Override
                        public void run() {
                            favouriteDatabase.favouriteDao().deleteImageFromFavourite(imagesHit.getId());
                        }
                    });
                }
                adapter.notifyItemChanged(imageHits.indexOf(imagesHit));
            }

            @Override
            public void openImageDetails(Hit itemsHit) {
                Intent i = new Intent(MainActivity.this, ClickedImageDetails.class);
                i.putExtra(getString(R.string.Clicked_Image_Tag), itemsHit);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        rv_images.setAdapter(adapter);
        rv_images.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getImagesFromAPI(String SearchItemType, int pageNumber, int numberOfImagesPerPage) {
        WaitDialogFragment waitDialogFragment = WaitDialogFragment.newInstance(getString(R.string.loading));
        waitDialogFragment.show(getSupportFragmentManager(), null);
        ErrorFragment errorFragment = ErrorFragment.newInstance();
        api_ = RetrofitClient.getService();
        api_.getTargetImages(RetrofitClient.key, SearchItemType, pageNumber, numberOfImagesPerPage, (((int) Math.random() * 2) == 0) ? "popular" : "latest").enqueue(new Callback<Images>() {
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                if (response.isSuccessful()) {
                    imageHits.addAll(response.body().getHits());
                    if (imageHits.size() == 0) {
                        if (waitDialogFragment.isVisible()) {
                            waitDialogFragment.dismiss();
                        }
                        errorFragment.show(getSupportFragmentManager(), null);
                    }
                    Log.e("TAG", "onResponse: " + imageHits.size());
                    buildRecycler();
                    adapter.notifyDataSetChanged();
                    if (waitDialogFragment.isVisible()) {
                        rv_images.post(new Runnable() {
                            @Override
                            public void run() {
                                waitDialogFragment.dismiss();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Images> call, Throwable t) {

            }
        });
    }

    private void initializeViews() {
        btn_more = findViewById(R.id.main_btn_more);
        sp = getSharedPreferences(getString(R.string.SharedPreferencesName), MODE_PRIVATE);
        rv_images = findViewById(R.id.main_recycler_view);
        animation_no_image = findViewById(R.id.main_error_animation);
        et_search = findViewById(R.id.main_et_search);
        btn_find = findViewById(R.id.main_btn_find);
        imageHits = new ArrayList<>();
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText().toString().trim().length() == 0) {
                    et_search.setError("Required");
                    et_search.requestFocus();
                } else {
                    imageHits.clear();
                    getImagesFromAPI(et_search.getText().toString().trim(), page, max_limit);
                }
            }
        });
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                imageHits.clear();
                getImagesFromAPI(et_search.getText().length() == 0 ? null : et_search.getText().toString().trim(), page, max_limit);
            }
        });
    }
}