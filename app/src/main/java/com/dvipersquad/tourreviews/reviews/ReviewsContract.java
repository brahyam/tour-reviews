package com.dvipersquad.tourreviews.reviews;

import com.dvipersquad.tourreviews.BasePresenter;
import com.dvipersquad.tourreviews.BaseView;
import com.dvipersquad.tourreviews.data.Review;

import java.util.List;

public interface ReviewsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showReviews(List<Review> reviews, Integer pages);

        void showReviewsNextPage(List<Review> reviews);

        void showReviewsLoadingError();

        boolean isActive();

    }

    interface Presenter extends BasePresenter<View> {

        void loadReviews(boolean forceUpdate, Integer page);

    }
}
