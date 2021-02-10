package com.elhefny.pixabay.DataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_images")
public class UserFavouriteImagesEntity {
    @PrimaryKey
    private int image_id;
    private int user_id;
    private String user_name;
    private String image_url;
    private int image_comments;
    private int images_likes;
    private int images_views;
    private int images_downloads;
    private String image_name;

    public UserFavouriteImagesEntity(int image_id, int user_id, String user_name, String image_url, int image_comments, int images_likes, int images_views, int images_downloads, String image_name) {
        this.image_id = image_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.image_url = image_url;
        this.image_comments = image_comments;
        this.images_likes = images_likes;
        this.images_views = images_views;
        this.images_downloads = images_downloads;
        this.image_name = image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getImage_comments() {
        return image_comments;
    }

    public void setImage_comments(int image_comments) {
        this.image_comments = image_comments;
    }

    public int getImages_likes() {
        return images_likes;
    }

    public void setImages_likes(int images_likes) {
        this.images_likes = images_likes;
    }

    public int getImages_views() {
        return images_views;
    }

    public void setImages_views(int images_views) {
        this.images_views = images_views;
    }

    public int getImages_downloads() {
        return images_downloads;
    }

    public void setImages_downloads(int images_downloads) {
        this.images_downloads = images_downloads;
    }

    public String getImage_name() {
        return image_name;
    }

}
