package com.dvipersquad.tourreviews.tourdetails;

import android.support.annotation.Nullable;
import android.util.Log;

import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.data.Tour;
import com.dvipersquad.tourreviews.data.source.ReviewsDataSource;
import com.dvipersquad.tourreviews.data.source.ReviewsRepository;
import com.dvipersquad.tourreviews.di.ActivityScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ActivityScoped
final class TourDetailsPresenter implements TourDetailsContract.Presenter {

    private static final String TAG = TourDetailsPresenter.class.getSimpleName();

    private static final Integer FIRST_PAGE = 1;

    private static final int REVIEWS_PREVIEW_COUNT = 4;

    private Tour tour;

    private boolean firstLoad = true;

    private final ReviewsRepository reviewsRepository;

    @Nullable
    private TourDetailsContract.View tourView;

    @Inject
    TourDetailsPresenter(ReviewsRepository reviewsRepository) {
        // Dummy data to show
        tour = new Tour(1,
                "Berlin Tempelhof Airport: The Legend of Tempelhof",
                4.7,
                "Embark on a 2-hour tour of the historic Berlin-Tempelhof airport " +
                        "building with explanations about its peculiar history and architecture. " +
                        "Have a look at the remnants of Nazi architecture, and discover the " +
                        "importance of Tempelhof throughout history.",
                16.50,
                120);

        tour.setImageUrls(new ArrayList<String>() {{
            add("https://cdn.getyourguide.com/img/tour_img-435583-145.jpg");
        }});

        this.reviewsRepository = reviewsRepository;
    }

    @Override
    public void loadTour() {
        if (tourView != null) {
            tourView.setLoadingIndicator(true);
        }
        tourView.showTour(tour);
    }

    @Override
    public void loadReviews() {
        if (tourView != null) {
            tourView.setLoadingIndicator(true);
        }

        if (firstLoad) {
            reviewsRepository.refreshReviews();
            firstLoad = false;
        }

        reviewsRepository.getReviews(FIRST_PAGE, new ReviewsDataSource.LoadReviewsCallback() {

            @Override
            public void onReviewsLoaded(List<Review> reviews, int loadedPage, int totalPages) {
                if (tourView == null || !tourView.isActive()) {
                    return;
                }
                tourView.setLoadingIndicator(false);
                int resultCount = reviews.size();
                if (REVIEWS_PREVIEW_COUNT < resultCount) {
                    resultCount = REVIEWS_PREVIEW_COUNT;
                }
                tourView.showReviews(reviews.subList(0, resultCount));
            }

            @Override
            public void onDataNotAvailable() {
                if (tourView == null || !tourView.isActive()) {
                    return;
                }
                tourView.showLoadingError();
            }
        });
    }

    @Override
    public void openAllReviews() {
        if (tourView != null && tourView.isActive()) {
            tourView.showAllReviewsUI();
        }
    }

    @Override
    public void takeView(TourDetailsContract.View view) {
        tourView = view;
    }

    @Override
    public void dropView() {
        tourView = null;
    }
}
