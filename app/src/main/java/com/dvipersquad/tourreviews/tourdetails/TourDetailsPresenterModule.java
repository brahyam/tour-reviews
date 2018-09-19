package com.dvipersquad.tourreviews.tourdetails;

import com.dvipersquad.tourreviews.di.ActivityScoped;
import com.dvipersquad.tourreviews.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TourDetailsPresenterModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract TourDetailsFragment tourDetailsFragment();

    @ActivityScoped
    @Binds
    abstract TourDetailsContract.Presenter detailsPresenter(TourDetailsPresenter presenter);

}
