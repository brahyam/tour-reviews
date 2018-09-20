package com.dvipersquad.tourreviews.data.source.local;

import android.support.annotation.NonNull;

import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.data.source.ReviewsDataSource;
import com.dvipersquad.tourreviews.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ReviewsLocalDataSource implements ReviewsDataSource {

    private final ReviewsDao reviewsDao;

    private final AppExecutors appExecutors;

    @Inject
    public ReviewsLocalDataSource(ReviewsDao reviewsDao, AppExecutors appExecutors) {
        this.reviewsDao = reviewsDao;
        this.appExecutors = appExecutors;
    }

    @Override
    public void getReviews(@NonNull Integer page, @NonNull final LoadReviewsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Review> reviews = reviewsDao.getReviews();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (reviews.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onReviewsLoaded(reviews, 1, 1);
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    public void saveReview(@NonNull final Review review) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                reviewsDao.insertReview(review);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshReviews() {
        // Not needed here, handled in ReviewsRepository
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllReviews() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                reviewsDao.deleteReviews();
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteReview(@NonNull final Integer id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                reviewsDao.deleteReviewById(id);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }
}
