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
import com.elhefny.pixabay.R;
import com.elhefny.pixabay.TargetImages.Hit;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.List;

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.imageViewHolder> {
    Context context;
    List<Hit> imageHits;
    onImageClicked listener;

    public RecyclerImageAdapter(Context context, List<Hit> imageHits, onImageClicked listener) {
        this.context = context;
        this.imageHits = imageHits;
        this.listener = listener;
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_design, parent, false);
        return new imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        Hit hit = imageHits.get(position);
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
                .load(hit.getWebformatURL())
                .placeholder(shimmerDrawable)
                .centerCrop()
                .error(R.drawable.ic_pixabay)
                .into(holder.iv_image_content);
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openImageDetails(hit);
            }
        });
        holder.iv_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addImageToFavourite(hit);
            }
        });
        holder.iv_favourite.setImageResource(hit.getImageFavouriteResc());
        holder.tv_number_of_downloads.setText(hit.getDownloads() + "");
        holder.tv_number_of_views.setText(hit.getViews() + "");

        holder.tv_number_of_views.setAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
        holder.tv_number_of_downloads.setAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));


    }

    @Override
    public int getItemCount() {
        return imageHits.size();
    }

    public class imageViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image_content, iv_favourite;
        TextView tv_number_of_downloads, tv_number_of_views;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_content = itemView.findViewById(R.id.recycler_design_iv);
            iv_favourite = itemView.findViewById(R.id.recycler_design_iv_favourite);

            tv_number_of_views = itemView.findViewById(R.id.recycler_design_tv_number_of_views);
            tv_number_of_downloads = itemView.findViewById(R.id.recycler_design_tv_number_of_downloads);
        }
    }

    public interface onImageClicked {
        void addImageToFavourite(Hit imagesHit);


        void openImageDetails(Hit itemsHit);
    }
}
