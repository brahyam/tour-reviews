package com.dvipersquad.tourreviews.data.source;

import com.dvipersquad.tourreviews.data.Review;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReviewsRepositoryTests {

    private static final Integer DEFAULT_PAGE = 1;
    private static final int DEFAULT_TOTAL_PAGES = 2;

    private static List<Review> REVIEWS = new ArrayList<Review>() {{
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

    private ReviewsRepository reviewsRepository;

    @Mock
    private ReviewsDataSource reviewsRemoteDataSource;

    @Mock
    private ReviewsDataSource reviewsLocalDataSource;

    @Mock
    private ReviewsDataSource.LoadReviewsCallback loadReviewsCallback;

    @Captor
    private ArgumentCaptor<ReviewsDataSource.LoadReviewsCallback> loadReviewsCallbackCaptor;

    @Before
    public void setupReviewsRepository() {
        MockitoAnnotations.initMocks(this);
        reviewsRepository = new ReviewsRepository(reviewsRemoteDataSource, reviewsLocalDataSource);
    }

    @Test
    public void getReviews_repositoryCachesAfterFirstApiCall() {
        // send 2 calls to repo
        twoReviewsLoadCallsToRepository(loadReviewsCallback);

        // Then reviews were only requested once from Service API
        verify(reviewsRemoteDataSource).getReviews(eq(DEFAULT_PAGE), any(ReviewsDataSource.LoadReviewsCallback.class));
    }

    @Test
    public void getReviews_requestsAllReviewsFromLocalDataSource() {
        // When reviews are requested from the reviews repository
        reviewsRepository.getReviews(DEFAULT_PAGE, loadReviewsCallback);

        // Then reviews are loaded from the local data source
        verify(reviewsLocalDataSource).getReviews(eq(DEFAULT_PAGE), any(ReviewsDataSource.LoadReviewsCallback.class));
    }

    @Test
    public void saveReview_savesReviewToLocalDB() {
        Review review = REVIEWS.get(0);

        // When a review is saved
        reviewsRepository.saveReview(review);

        // Then review is saved locally and cache is updated
        verify(reviewsLocalDataSource).saveReview(review);
        assertThat(reviewsRepository.cachedReviews.size(), is(1));
    }

    @Test
    public void deleteAllReviews_deleteReviewsFromLocalDBAndUpdatesCache() {
        Review review1 = REVIEWS.get(0);
        reviewsRepository.saveReview(review1);

        Review review2 = REVIEWS.get(1);
        reviewsRepository.saveReview(review2);

        Review review3 = REVIEWS.get(2);
        reviewsRepository.saveReview(review3);

        // When all reviews are deleted to the reviews repository
        reviewsRepository.deleteAllReviews();

        // Verify the data sources were called
        verify(reviewsLocalDataSource).deleteAllReviews();
        // And cache is erased
        assertThat(reviewsRepository.cachedReviews.size(), is(0));
    }

    @Test
    public void deleteReview_deleteReviewFromLocalDBAndRemovedFromCache() {
        // Given a review in the repository
        Review review = REVIEWS.get(0);
        reviewsRepository.saveReview(review);
        assertThat(reviewsRepository.cachedReviews.containsKey(review.getId()), is(true));

        // When deleted
        reviewsRepository.deleteReview(review.getId());

        // Verify the local data source is called
        verify(reviewsLocalDataSource).deleteReview(review.getId());

        // Verify it's removed from cache
        assertThat(reviewsRepository.cachedReviews.containsKey(review.getId()), is(false));
    }

    @Test
    public void getReviewsWithDirtyCache_reviewsAreRetrievedFromRemote() {
        // Request reviews with dirty cache
        reviewsRepository.refreshReviews();
        reviewsRepository.getReviews(DEFAULT_PAGE, loadReviewsCallback);

        // And the remote data source has data available
        setReviewsAvailable(reviewsRemoteDataSource, REVIEWS);

        // Verify the reviews from the remote data source are returned, not the local
        verify(reviewsLocalDataSource, never()).getReviews(DEFAULT_PAGE, loadReviewsCallback);
        verify(loadReviewsCallback).onReviewsLoaded(REVIEWS, DEFAULT_PAGE, DEFAULT_TOTAL_PAGES);
    }

    @Test
    public void getReviewsWithLocalDataSourceUnavailable_reviewsAreRetrievedFromRemote() {
        // Request reviews
        reviewsRepository.getReviews(DEFAULT_PAGE, loadReviewsCallback);

        // And the local data source has no data available
        setReviewsNotAvailable(reviewsLocalDataSource);

        // And the remote data source has data available
        setReviewsAvailable(reviewsRemoteDataSource, REVIEWS);

        // Verify the reviews from the local data source are returned
        verify(loadReviewsCallback).onReviewsLoaded(REVIEWS, DEFAULT_PAGE, DEFAULT_TOTAL_PAGES);
    }

    @Test
    public void getReviewsWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // Request reviews
        reviewsRepository.getReviews(DEFAULT_PAGE, loadReviewsCallback);

        // data not available locally
        setReviewsNotAvailable(reviewsLocalDataSource);

        // data not available remotely
        setReviewsNotAvailable(reviewsRemoteDataSource);

        // Verify no data is returned
        verify(loadReviewsCallback).onDataNotAvailable();
    }

    @Test
    public void getReviews_refreshesLocalDataSource() {
        // Force remote reload
        reviewsRepository.refreshReviews();

        // Request reviews
        reviewsRepository.getReviews(DEFAULT_PAGE, loadReviewsCallback);

        // Fake remote data source response
        setReviewsAvailable(reviewsRemoteDataSource, REVIEWS);

        // Verify data fetched was saved in DB.
        verify(reviewsLocalDataSource, times(REVIEWS.size())).saveReview(any(Review.class));
    }

    /**
     * Sends two calls to the reviews repository
     */
    private void twoReviewsLoadCallsToRepository(ReviewsRepository.LoadReviewsCallback callback) {
        // reviews are requested
        reviewsRepository.getReviews(DEFAULT_PAGE, callback); // First call to API

        // capture the callback
        verify(reviewsLocalDataSource).getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());

        // Local data source doesn't have data yet
        loadReviewsCallbackCaptor.getValue().onDataNotAvailable();

        // Verify the remote data source is queried
        verify(reviewsRemoteDataSource).getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());

        // Trigger callback so reviews are cached
        loadReviewsCallbackCaptor.getValue().onReviewsLoaded(REVIEWS, DEFAULT_PAGE, DEFAULT_TOTAL_PAGES);

        reviewsRepository.getReviews(DEFAULT_PAGE, callback); // Second call to API
    }

    private void setReviewsNotAvailable(ReviewsDataSource dataSource) {
        verify(dataSource).getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());
        loadReviewsCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setReviewsAvailable(ReviewsDataSource dataSource, List<Review> reviews) {
        verify(dataSource).getReviews(eq(DEFAULT_PAGE), loadReviewsCallbackCaptor.capture());
        loadReviewsCallbackCaptor.getValue().onReviewsLoaded(reviews, DEFAULT_PAGE, DEFAULT_TOTAL_PAGES);
    }
}
