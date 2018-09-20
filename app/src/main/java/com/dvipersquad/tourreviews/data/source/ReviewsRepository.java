package com.dvipersquad.tourreviews.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dvipersquad.tourreviews.data.Review;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ReviewsRepository implements ReviewsDataSource {

    private static final String TAG = ReviewsRepository.class.getSimpleName();
    private final ReviewsDataSource reviewsRemoteDataSource;
    private final ReviewsDataSource reviewsLocalDataSource;

    private int totalPages = 0;
    private int currentPage = -1;
    Map<Integer, Review> cachedReviews;

    boolean cacheIsDirty = false;

    @Inject
    public ReviewsRepository(@Remote ReviewsDataSource reviewsRemoteDataSource, @Local ReviewsDataSource reviewsLocalDataSource) {
        this.reviewsRemoteDataSource = reviewsRemoteDataSource;
        this.reviewsLocalDataSource = reviewsLocalDataSource;
    }

    @Override
    public void getReviews(@NonNull final Integer page, @NonNull final LoadReviewsCallback callback) {
        if (cachedReviews != null && !cacheIsDirty && page <= currentPage) {
            callback.onReviewsLoaded(new ArrayList<>(cachedReviews.values()), currentPage, totalPages);
        }

        if (cacheIsDirty || page > 1) { // fetch from repo all pages greater than 1
            getReviewsFromRemoteDataSource(page, callback);
        } else {
            reviewsLocalDataSource.getReviews(page, new LoadReviewsCallback() {

                @Override
                public void onReviewsLoaded(List<Review> reviews, int loadedPage, int totalPages) {
                    refreshCache(reviews);
                    callback.onReviewsLoaded(reviews, loadedPage, totalPages);
                }

                @Override
                public void onDataNotAvailable() {
                    getReviewsFromRemoteDataSource(page, callback);
                }
            });
        }
    }

    private void getReviewsFromRemoteDataSource(final Integer page, final LoadReviewsCallback callback) {
        reviewsRemoteDataSource.getReviews(page, new LoadReviewsCallback() {

            @Override
            public void onReviewsLoaded(List<Review> reviews, int loadedPage, int totalPages) {
                refreshCache(reviews);
                refreshLocalDataSource(reviews);
                currentPage = loadedPage;
                callback.onReviewsLoaded(reviews, loadedPage, totalPages);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Review> reviews) {
        if (cachedReviews == null) {
            cachedReviews = new LinkedHashMap<>();
        }
        if (cacheIsDirty) {
            cachedReviews.clear();
        }
        for (Review review : reviews) {
            cachedReviews.put(review.getId(), review);
        }
        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Review> reviews) {
        if (cacheIsDirty) {
            reviewsLocalDataSource.deleteAllReviews();
        }
        for (Review review : reviews) {
            reviewsLocalDataSource.saveReview(review);
        }
    }

    public void saveReview(@NonNull Review review) {
        reviewsLocalDataSource.saveReview(review);
        if (cachedReviews == null) {
            cachedReviews = new LinkedHashMap<>();
        }
        cachedReviews.put(review.getId(), review);
    }

    @Override
    public void refreshReviews() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllReviews() {
        reviewsLocalDataSource.deleteAllReviews();
        if (cachedReviews == null) {
            cachedReviews = new LinkedHashMap<>();
        }
        cachedReviews.clear();
    }

    @Override
    public void deleteReview(@NonNull Integer id) {
        reviewsLocalDataSource.deleteReview(id);
        if (cachedReviews == null) {
            cachedReviews = new LinkedHashMap<>();
        }
        cachedReviews.remove(id);
    }
}
