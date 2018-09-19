package com.dvipersquad.tourreviews.tourdetails;

import com.dvipersquad.tourreviews.BasePresenter;
import com.dvipersquad.tourreviews.BaseView;
import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.data.Tour;

import java.util.List;

/**
 * Specifies the contract between the view and the presenter
 */
public interface TourDetailsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTour(Tour tour);

        boolean isActive();
    }

    interface Presenter extends BasePresenter<View> {

        void takeView(TourDetailsContract.View hotelDetailsFragment);

        void dropView();
    }

}
