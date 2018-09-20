package com.dvipersquad.tourreviews.data.source;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.dvipersquad.tourreviews.data.source.local.ReviewsDao;
import com.dvipersquad.tourreviews.data.source.local.ReviewsDatabase;
import com.dvipersquad.tourreviews.data.source.local.ReviewsLocalDataSource;
import com.dvipersquad.tourreviews.data.source.remote.ReviewsRemoteDataSource;
import com.dvipersquad.tourreviews.utils.AppExecutors;
import com.dvipersquad.tourreviews.utils.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ReviewsRepositoryModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Provides
    @Local
    ReviewsDataSource provideReviewsLocalDataSource(ReviewsDao reviewsDao, AppExecutors executors) {
        return new ReviewsLocalDataSource(reviewsDao, executors);
    }

    @Singleton
    @Provides
    @Remote
    ReviewsDataSource provideReviewsRemoteDataSource() {
        return new ReviewsRemoteDataSource();
    }

    @Singleton
    @Provides
    ReviewsDatabase provideDB(Application context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                ReviewsDatabase.class, "Reviews.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    ReviewsDao provideReviewsDao(ReviewsDatabase database) {
        return database.reviewsDao();
    }

    @Singleton
    @Provides
    AppExecutors provideAppExecutors() {
        return new AppExecutors(
                new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
