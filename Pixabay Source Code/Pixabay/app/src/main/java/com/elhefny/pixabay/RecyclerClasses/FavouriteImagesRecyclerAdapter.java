package com.elhefny.pixabay.RecyclerClasses;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elhefny.pixabay.DataBase.UserFavouriteImagesEntity;
import com.elhefny.pixabay.R;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavouriteImagesRecyclerAdapter extends RecyclerView.Adapter<FavouriteImagesRecyclerAdapter.FavouriteImagesViewHolder> {
    List<UserFavouriteImagesEntity> favouriteImagesList;
    Context context;
    OnImageFavouriteClicked listener;

    public FavouriteImagesRecyclerAdapter(List<UserFavouriteImagesEntity> favouriteImagesList, Context context, OnImageFavouriteClicked listener) {
        this.favouriteImagesList = favouriteImagesList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavouriteImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteImagesViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteImagesViewHolder holder, int position) {
        UserFavouriteImagesEntity favouriteImage = favouriteImagesList.get(position);
        holder.iv_favourite.setImageResource(R.drawable.colored_favourite);
        holder.iv_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteImage(favouriteImage.getImage_id(), favouriteImagesList.indexOf(favouriteImage));
            }
        });
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Glide.with(context)
                .load(favouriteImage.getImage_url())
                .placeholder(shimmerDrawable)
                .centerCrop()
                .error(R.drawable.ic_pixabay)
                .into(holder.iv_image_content);
        holder.tv_number_of_downloads.setText(favouriteImage.getImages_downloads() + "");
        holder.tv_number_of_views.setText(favouriteImage.getImages_views() + "");


        holder.tv_number_of_views.setAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
        holder.tv_number_of_downloads.setAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(holder.itemView, favouriteImage.getImage_name(), 2000);
                snackbar.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteImagesList.size();
    }

    public class FavouriteImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image_content, iv_favourite;
        TextView tv_number_of_downloads, tv_number_of_views;

        public FavouriteImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_content = itemView.findViewById(R.id.recycler_design_iv);
            iv_favourite = itemView.findViewById(R.id.recycler_design_iv_favourite);

            tv_number_of_views = itemView.findViewById(R.id.recycler_design_tv_number_of_views);
            tv_number_of_downloads = itemView.findViewById(R.id.recycler_design_tv_number_of_downloads);
        }
    }

    public interface OnImageFavouriteClicked {
        void deleteImage(int imageId, int imagePosition);
    }
}
