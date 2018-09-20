package com.dvipersquad.tourreviews.reviews;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dvipersquad.tourreviews.R;
import com.dvipersquad.tourreviews.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class ReviewsActivity extends DaggerAppCompatActivity {

    @Inject
    ReviewsPresenter reviewsPresenter;

    @Inject
    Lazy<ReviewsFragment> reviewsFragmentProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews_activity);

        ReviewsFragment reviewsFragment = (ReviewsFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (reviewsFragment == null) {
            reviewsFragment = reviewsFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    reviewsFragment,
                    R.id.contentFrame);
        }
    }
}
