package com.dvipersquad.tourreviews.tourdetails;

import android.os.Bundle;

import com.dvipersquad.tourreviews.R;
import com.dvipersquad.tourreviews.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Displays tour details screen
 */
public class TourDetailsActivity extends DaggerAppCompatActivity {

    @Inject
    TourDetailsFragment injectedTourDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourdetails_act);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TourDetailsFragment tourDetailsFragment =
                (TourDetailsFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);

        if (tourDetailsFragment == null) {
            tourDetailsFragment = this.injectedTourDetailsFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    tourDetailsFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
