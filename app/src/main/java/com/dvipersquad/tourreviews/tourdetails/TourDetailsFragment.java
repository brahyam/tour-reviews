package com.dvipersquad.tourreviews.tourdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dvipersquad.tourreviews.R;
import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.data.Tour;
import com.dvipersquad.tourreviews.di.ActivityScoped;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * View for Tour Details
 */
@ActivityScoped
public class TourDetailsFragment extends DaggerFragment implements TourDetailsContract.View {

    private static final String TAG = TourDetailsFragment.class.getSimpleName();
    private ScrollView scrollMainPanel;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView imgTourPicture;
    private TextView txtTourTitle;
    private TextView txtRating;
    private TextView txtPrice;
    private TextView txtTourDescription;
    private ProgressBar progressBarReviews;
    private RecyclerView recyclerReviewsPreview;
    private ReviewsAdapter adapter;

    @Inject
    TourDetailsContract.Presenter presenter;

    @Inject
    public TourDetailsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        presenter.loadTour();
        presenter.loadReviews();
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
        progressBarReviews = root.findViewById(R.id.progressBarReviews);
        // init recycler
        adapter = new ReviewsAdapter(new ArrayList<Review>(), null);
        recyclerReviewsPreview = root.findViewById(R.id.recyclerReviewsPreview);
        recyclerReviewsPreview.setHasFixedSize(true);
        recyclerReviewsPreview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReviewsPreview.setAdapter(adapter);
        recyclerReviewsPreview.setClickable(false);
        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            progressBarReviews.setVisibility(View.VISIBLE);
        } else {
            progressBarReviews.setVisibility(View.GONE);
        }
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
                    .placeholder(R.drawable.tour_placeholder)
                    .into(imgTourPicture);
        }
        bottomSheetBehavior.setPeekHeight(imgTourPicture.getBaseline());
    }

    @Override
    public void showReviews(List<Review> reviews) {
        adapter.addData(reviews);
    }

    @Override
    public void showAllReviewsUI() {
        Log.d(TAG, "Called open all reviews");
    }

    @Override
    public void showLoadingError() {
        if (getView() != null) {
            Snackbar.make(getView(), getString(R.string.error_loading_reviews), Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
