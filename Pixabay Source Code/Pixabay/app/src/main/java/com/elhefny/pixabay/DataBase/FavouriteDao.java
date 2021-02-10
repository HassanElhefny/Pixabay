package com.elhefny.pixabay.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Insert
    void addImageToFavourite(UserFavouriteImagesEntity newImage);

    @Query("SELECT * FROM favourite_images WHERE user_name = :userName")
    List<UserFavouriteImagesEntity> getAllImagesInFavourite(String userName);

    @Query("DELETE FROM favourite_images WHERE image_id = :image_id")
    void deleteImageFromFavourite(int image_id);

}
