package com.dvipersquad.tourreviews.reviews;

import com.dvipersquad.tourreviews.di.ActivityScoped;
import com.dvipersquad.tourreviews.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ReviewsPresenterModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract ReviewsFragment reviewsFragment();

    @ActivityScoped
    @Binds
    abstract ReviewsContract.Presenter reviewsPresenter(ReviewsPresenter presenter);
}
