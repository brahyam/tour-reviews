package com.dvipersquad.tourreviews.tourdetails;

import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.data.Tour;
import com.dvipersquad.tourreviews.data.source.ReviewsDataSource;
import com.dvipersquad.tourreviews.data.source.ReviewsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TourDetailsPresenterTests {

    private static List<Review> REVIEWS;

    private static final Integer DEFAULT_PAGE = 1;

    private static final int DEFAULT_TOTAL_PAGES = 2;

    @Mock
    private ReviewsRepository reviewsRepository;

    @Mock
    private TourDetailsContract.View tourDetailsView;

    private TourDetailsPresenter tourDetailsPresenter;

    @Captor
    private ArgumentCaptor<ReviewsDataSource.LoadReviewsCallback> loadReviewsCallbackCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        tourDetailsPresenter = new TourDetailsPresenter(reviewsRepository);
        tourDetailsPresenter.takeView(tourDetailsView);

        // Presenter needs active view
        when(tourDetailsView.isActive()).thenReturn(true);

        REVIEWS = new ArrayList<Review>() {{
            add(new Review(1, 1.0, "Test Title 1", "Test Message 1",
                    "September 1st, 2018", "en", "couple",
                    "John Doe", "USA"));
            add(new Review(2, 2.5, "Test Title 2", "Test Message 2",
                    "August 4th, 2018", "de", "friends",
                    "Teresa May", "USA"));
            add(new Review(3, 5.0, "Test Title 3", "Test Message 3",
                    "April 10th, 2018", "es", "single",
                    "Varsha Jahaaa", "USA"));
        }};
    }

    @Test
    public void LoadTourIntoView() {
        // As current tour is hardcoded we just verify its contents are loaded into the view
        tourDetailsPresenter.loadTour();
        verify(tourDetailsView).showTour(any(Tour.class));
    }

    @Test
    public void clickOnSeeAllReviews_ShowsReviewsScreen() {
        // When button is clicked
        tourDetailsPresenter.openAllReviews();
        // Then reviews screen is shown
        verify(tourDetailsView).showAllReviewsUI();
    }

    @Test
    public void loadAllReviewsFromRepositoryAndLoadIntoView() {
        // Request reviews load
        tourDetailsPresenter.loadReviews();

        // Called 2 times. On fragment added/ On load requested
        verify(reviewsRepository, times(1))
                .getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());
        loadReviewsCallbackCaptor.getValue().onReviewsLoaded(REVIEWS, DEFAULT_PAGE, DEFAULT_TOTAL_PAGES);

        // Progress indicator is shown
        verify(tourDetailsView, times(1)).setLoadingIndicator(true);
        // Progress indicator is hidden and reviews are shown
        verify(tourDetailsView, times(1)).setLoadingIndicator(false);
        ArgumentCaptor<List> showReviewsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(tourDetailsView).showReviews(showReviewsArgumentCaptor.capture());
        assertTrue(showReviewsArgumentCaptor.getValue().size() == 3);
    }


    @Test
    public void unavailableTasks_ShowsError() {
        // When reviews are loaded
        tourDetailsPresenter.loadReviews();

        // And the reviews aren't available in the repository
        verify(reviewsRepository, times(1)).getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());
        loadReviewsCallbackCaptor.getValue().onDataNotAvailable();

        // Then an error message is shown
        verify(tourDetailsView).showLoadingError();
    }
}
