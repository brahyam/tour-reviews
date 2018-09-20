package com.dvipersquad.tourreviews.reviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dvipersquad.tourreviews.R;
import com.dvipersquad.tourreviews.data.Review;
import com.dvipersquad.tourreviews.di.ActivityScoped;
import com.dvipersquad.tourreviews.tourdetails.ReviewsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class ReviewsFragment extends DaggerFragment implements ReviewsContract.View {

    private static final String TAG = ReviewsFragment.class.getSimpleName();

    private static final Integer DEFAULT_PAGE = 1;

    @Inject
    ReviewsContract.Presenter presenter;

    private RecyclerView recyclerReviews;

    private ReviewsAdapter adapter;

    private ProgressBar progressBarReviews;

    private boolean isLoading;

    private boolean isLastPage = false;

    private int currentPage = 1;

    private int totalPages = 0;

    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private EndlessRecyclerViewScrollListener paginationListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            Log.d(TAG, "RecyclerView requested load page:" + page);
            if (!isLoading) {
                int newPage = page + 1;
                presenter.loadReviews(false, newPage);
            }
        }
    };

    @Inject
    public ReviewsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        presenter.loadReviews(false, DEFAULT_PAGE);
        isLoading = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.reviews_fragment, container, false);
        adapter = new ReviewsAdapter(new ArrayList<Review>(), null);
        adapter.setMessageExpandable(true);
        recyclerReviews = root.findViewById(R.id.recyclerReviews);
        recyclerReviews.setHasFixedSize(true);
        recyclerReviews.setLayoutManager(linearLayoutManager);
        recyclerReviews.addOnScrollListener(paginationListener);
        recyclerReviews.setAdapter(adapter);
        progressBarReviews = root.findViewById(R.id.progressBarReviews);
        setHasOptionsMenu(true);
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
    public void showReviews(List<Review> reviews, Integer pages) {
        totalPages = pages;
        adapter.addData(reviews);
        setLoadingIndicator(false);
        isLoading = false;
        if (currentPage == totalPages) {
            isLastPage = true;
        }
    }

    @Override
    public void showReviewsNextPage(List<Review> reviews) {
        adapter.addData(reviews);
        setLoadingIndicator(false);
        isLoading = false;
        currentPage++;
    }

    @Override
    public void showReviewsLoadingError() {
        Log.d(TAG, "Show error");
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
