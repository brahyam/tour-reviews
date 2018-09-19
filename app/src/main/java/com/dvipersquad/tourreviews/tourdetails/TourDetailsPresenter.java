package com.dvipersquad.tourreviews.tourdetails;

import android.support.annotation.Nullable;

import com.dvipersquad.tourreviews.data.Tour;

import java.util.ArrayList;

import javax.inject.Inject;

final class TourDetailsPresenter implements TourDetailsContract.Presenter {

    private Tour tour;

    @Nullable
    private TourDetailsContract.View tourDetailsView;

    @Inject
    TourDetailsPresenter() {
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
    }

    private void loadTour() {
        if (tourDetailsView != null) {
            tourDetailsView.setLoadingIndicator(true);
        }
        tourDetailsView.showTour(tour);
    }

    @Override
    public void takeView(TourDetailsContract.View view) {
        tourDetailsView = view;
        loadTour();
    }

    @Override
    public void dropView() {
        tourDetailsView = null;
    }
}
