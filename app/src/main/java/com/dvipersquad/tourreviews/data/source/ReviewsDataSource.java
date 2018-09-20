package com.dvipersquad.tourreviews.data.source;

import android.support.annotation.NonNull;

import com.dvipersquad.tourreviews.data.Review;

import java.util.List;

/**
 * Defines the behavior of a reviews data source
 */
public interface ReviewsDataSource {

    interface LoadReviewsCallback {
        void onReviewsLoaded(List<Review> reviews, int loadedPage, int totalPages);

        void onDataNotAvailable();
    }

    void getReviews(@NonNull Integer page, @NonNull LoadReviewsCallback callback);

    void saveReview(@NonNull Review review);

    void refreshReviews();

    void deleteAllReviews();

    void deleteReview(@NonNull Integer id);

}
