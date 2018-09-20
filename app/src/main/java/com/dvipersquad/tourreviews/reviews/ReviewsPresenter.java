package com.dvipersquad.tourreviews.reviews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.data.source.ReviewsDataSource;
import com.dvipersquad.tourreviews.data.source.ReviewsRepository;
import com.dvipersquad.tourreviews.di.ActivityScoped;

import java.util.List;

import javax.inject.Inject;

@ActivityScoped
public class ReviewsPresenter implements ReviewsContract.Presenter {

    private static final String TAG = ReviewsPresenter.class.getSimpleName();
    private final ReviewsRepository reviewsRepository;

    @Nullable
    private ReviewsContract.View reviewsView;

    private boolean firstLoad = true;

    @Inject
    public ReviewsPresenter(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    @Override
    public void loadReviews(boolean forceUpdate, final Integer page) {
        if (reviewsView != null) {
            reviewsView.setLoadingIndicator(true);
        }

        if (forceUpdate || firstLoad) {
            reviewsRepository.refreshReviews();
        }

        reviewsRepository.getReviews(page, new ReviewsDataSource.LoadReviewsCallback() {

            @Override
            public void onReviewsLoaded(List<Review> reviews, int loadedPage, int totalPages) {
                if (reviewsView == null || !reviewsView.isActive()) {
                    return;
                }
                reviewsView.setLoadingIndicator(false);
                if (firstLoad) {
                    reviewsView.showReviews(reviews, totalPages);
                    firstLoad = false;
                } else {
                    reviewsView.showReviewsNextPage(reviews);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (reviewsView == null || !reviewsView.isActive()) {
                    return;
                }
                reviewsView.showReviewsLoadingError();
            }
        });
    }

    @Override
    public void takeView(ReviewsContract.View view) {
        this.reviewsView = view;
    }

    @Override
    public void dropView() {
        this.reviewsView = null;
    }

}
