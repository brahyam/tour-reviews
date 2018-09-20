package com.dvipersquad.tourreviews.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dvipersquad.tourreviews.data.Review;

@Database(entities = {Review.class}, version = 2, exportSchema = false)
public abstract class ReviewsDatabase extends RoomDatabase {
    public abstract ReviewsDao reviewsDao();
}
