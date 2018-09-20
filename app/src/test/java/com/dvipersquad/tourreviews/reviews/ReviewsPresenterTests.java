package com.dvipersquad.tourreviews.reviews;

import com.dvipersquad.tourreviews.data.Review;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewsPresenterTests {

    private static List<Review> REVIEWS;

    private static final Integer DEFAULT_PAGE = 0;

    private static final int DEFAULT_TOTAL_PAGES = 2;

    @Mock
    private ReviewsRepository reviewsRepository;

    @Mock
    private ReviewsContract.View reviewsView;

    @Captor
    private ArgumentCaptor<ReviewsDataSource.LoadReviewsCallback> loadReviewsCallbackCaptor;

    private ReviewsPresenter reviewsPresenter;

    @Before
    public void setupReviewsPresenter() {
        MockitoAnnotations.initMocks(this);

        reviewsPresenter = new ReviewsPresenter(reviewsRepository);
        reviewsPresenter.takeView(reviewsView);

        when(reviewsView.isActive()).thenReturn(true);

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
    public void loadAllReviewsFromRepositoryAndLoadIntoView() {
        // Request reviews load
        reviewsPresenter.loadReviews(true, eq(DEFAULT_PAGE));

        // Called 2 times. On fragment added/ On load requested
        verify(reviewsRepository, times(1))
                .getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());
        loadReviewsCallbackCaptor.getValue().onReviewsLoaded(REVIEWS, DEFAULT_PAGE, DEFAULT_TOTAL_PAGES);

        // Progress indicator is shown
        verify(reviewsView, times(1)).setLoadingIndicator(true);
        // Progress indicator is hidden and reviews are shown
        verify(reviewsView, times(1)).setLoadingIndicator(false);
        ArgumentCaptor<List> showReviewsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(reviewsView).showReviews(showReviewsArgumentCaptor.capture(), eq(DEFAULT_TOTAL_PAGES));
        assertTrue(showReviewsArgumentCaptor.getValue().size() == 3);
    }


    @Test
    public void unavailableTasks_ShowsError() {
        // When reviews are loaded
        reviewsPresenter.loadReviews(true, eq(DEFAULT_PAGE));

        // And the reviews aren't available in the repository
        verify(reviewsRepository, times(1)).getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());
        loadReviewsCallbackCaptor.getValue().onDataNotAvailable();

        // Then an error message is shown
        verify(reviewsView).showReviewsLoadingError();
    }
}
