package com.dvipersquad.tourreviews.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.data.source.ReviewsApiResponse;
import com.dvipersquad.tourreviews.data.source.ReviewsDataSource;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

@Singleton
public class ReviewsRemoteDataSource implements ReviewsDataSource {

    private final static String API_URL = "https://www.getyourguide.com/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/";
    private final static int REVIEW_AMOUNT_PER_FETCH = 20;
    private final static String TAG = ReviewsRemoteDataSource.class.getSimpleName();
    private ReviewsApi reviewsApi;

    public ReviewsRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        reviewsApi = retrofit.create(ReviewsApi.class);
    }

    @Override
    public void getReviews(@NonNull final Integer page, @NonNull final LoadReviewsCallback callback) {
        Call<ReviewsApiResponse> call = reviewsApi.getReviews(REVIEW_AMOUNT_PER_FETCH, page);
        call.enqueue(new Callback<ReviewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsApiResponse> call, @NonNull Response<ReviewsApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onReviewsLoaded(response.body().getData(), page, response.body().getTotalReviews() / REVIEW_AMOUNT_PER_FETCH);
                } else {
                    Log.e(TAG, "response unsuccessful or with empty body ");
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<ReviewsApiResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                callback.onDataNotAvailable();
            }
        });
    }

    public void saveReview(@NonNull Review review) {
        // Not needed here, only saved locally
        throw new UnsupportedOperationException();
    }

    @Override
    public void refreshReviews() {
        // Not needed here, handled in ReviewsRepository
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllReviews() {
        // Not needed here, handled only locally
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteReview(@NonNull Integer id) {
        // Not needed here, handled in ReviewsRepository
        throw new UnsupportedOperationException();
    }

    public interface ReviewsApi {
        @Headers("User-Agent: GetYourGuide")
        @GET("reviews.json")
        Call<ReviewsApiResponse> getReviews(@Query("count") int count, @Query("page") int page);
    }
}