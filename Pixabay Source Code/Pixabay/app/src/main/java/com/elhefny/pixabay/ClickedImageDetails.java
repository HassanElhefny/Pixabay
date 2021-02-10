package com.elhefny.pixabay;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.elhefny.pixabay.TargetImages.Hit;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.button.MaterialButton;

public class ClickedImageDetails extends AppCompatActivity {
    TextView tv_comments, tv_imageSize, tv_views, tv_likes, tv_downloads;
    MaterialButton btn_download;
    ImageView image;
    Hit imageHit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_image_details);
        initializeViews();
    }

    private void initializeViews() {
        tv_comments = findViewById(R.id.clicked_image_details_number_of_comments_tv);
        tv_imageSize = findViewById(R.id.clicked_image_details_image_size_tv);
        tv_downloads = findViewById(R.id.clicked_image_details_number_of_downloads_tv);
        tv_views = findViewById(R.id.clicked_image_details_number_of_views_tv);
        tv_likes = findViewById(R.id.clicked_image_details_number_of_likes_tv);
        btn_download = findViewById(R.id.clicked_image_details_btn_download);
        image = findViewById(R.id.clicked_image_details_image);
        imageHit = (Hit) getIntent().getSerializableExtra(getString(R.string.Clicked_Image_Tag));
        LoadImage();
        PutDataIntoTextViews();
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadProcess();
            }
        });
    }

    private void downloadProcess() {
        if (!NetworkUtil.getConnectivityStatusString(this).equals("No internet is available")) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                downloadImage();
            } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 123);
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                downloadImage();
            }
        } else {
            Toast.makeText(this, getString(R.string.NoInternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadImage() {
        Uri uri = Uri.parse(imageHit.getLargeImageURL());
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(imageHit.getTags());
        request.setDescription("The User Of This Image Is : " + imageHit.getUser());
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Pixabay/" + imageHit.getId() + ".jpg");
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }

    private void PutDataIntoTextViews() {
        tv_likes.setText("\' " + imageHit.getLikes() + " \' " + getString(R.string.like));
        tv_comments.setText("\' " + imageHit.getComments() + " \' " + getString(R.string.comment));
        tv_views.setText("\' " + imageHit.getViews() + " \' " + getString(R.string.view));
        tv_imageSize.setText("\' " + (Math.ceil(imageHit.getImageSize().doubleValue() / 1000)) + " \' KB");
        tv_downloads.setText("\' " + imageHit.getDownloads() + " \' " + getString(R.string.downloads));
    }

    private void LoadImage() {
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Glide.with(this)
                .load(imageHit.getWebformatURL())
                .placeholder(shimmerDrawable)
                .centerCrop()
                .error(R.drawable.ic_pixabay)
                .into(image);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null && grantResults.length > 0 && permissions.length > 0 && grantResults != null && requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadImage();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, getString(R.string.Permission_Required), Toast.LENGTH_SHORT).show();
            }
        }
    }
}