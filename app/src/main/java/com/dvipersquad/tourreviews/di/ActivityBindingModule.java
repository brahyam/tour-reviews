package com.dvipersquad.tourreviews.di;

import com.dvipersquad.tourreviews.tourdetails.TourDetailsActivity;
import com.dvipersquad.tourreviews.tourdetails.TourDetailsPresenterModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = TourDetailsPresenterModule.class)
    abstract TourDetailsActivity tourDetailsActivity();
}
