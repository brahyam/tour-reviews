package com.dvipersquad.tourreviews.tourdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dvipersquad.tourreviews.R;
import com.dvipersquad.tourreviews.data.Tour;
import com.dvipersquad.tourreviews.di.ActivityScoped;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * View for Tour Details
 */
@ActivityScoped
public class TourDetailsFragment extends DaggerFragment implements TourDetailsContract.View {

    private ScrollView scrollMainPanel;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView imgTourPicture;
    private TextView txtTourTitle;
    private TextView txtRating;
    private TextView txtPrice;
    private TextView txtTourDescription;

    @Inject
    TourDetailsContract.Presenter presenter;

    @Inject
    public TourDetailsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tourdetails_frag, container, false);
        scrollMainPanel = root.findViewById(R.id.scrollMainPanel);
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(scrollMainPanel);
        imgTourPicture = root.findViewById(R.id.imgTourPicture);
        txtTourTitle = root.findViewById(R.id.txtTourTitle);
        txtRating = root.findViewById(R.id.txtRating);
        txtPrice = root.findViewById(R.id.txtPrice);
        txtTourDescription = root.findViewById(R.id.txtTourDescription);
        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
    }

    @Override
    public void showTour(Tour tour) {
        txtTourTitle.setText(tour.getTitle());
        txtRating.setText(String.valueOf(tour.getRating()));
        txtPrice.setText(String.format(Locale.getDefault(), "%s %.2f", "$", tour.getPrice()));
        txtTourDescription.setText(tour.getDescription());
        if (tour.getImageUrls() != null && !tour.getImageUrls().isEmpty()) {
            Picasso.get()
                    .load(tour.getImageUrls().get(0))
                    .placeholder(R.drawable.placeholder)
                    .into(imgTourPicture);
        }
        bottomSheetBehavior.setPeekHeight(imgTourPicture.getBaseline());
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
