package com.dvipersquad.tourreviews.di;

import com.dvipersquad.tourreviews.reviews.ReviewsActivity;
import com.dvipersquad.tourreviews.reviews.ReviewsPresenterModule;
import com.dvipersquad.tourreviews.tourdetails.TourDetailsActivity;
import com.dvipersquad.tourreviews.tourdetails.TourDetailsPresenterModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = TourDetailsPresenterModule.class)
    abstract TourDetailsActivity tourDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ReviewsPresenterModule.class)
    abstract ReviewsActivity reviewsActivity();
}
