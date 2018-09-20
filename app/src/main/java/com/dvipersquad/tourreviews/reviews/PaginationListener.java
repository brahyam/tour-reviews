package com.dvipersquad.tourreviews.reviews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class PaginationListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager linearLayoutManager;

    public PaginationListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleCount = linearLayoutManager.getChildCount();
        int totalCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleCount + firstVisibleItemPosition) >= totalCount && firstVisibleItemPosition >= 0) {
                loadPage();
            }
        }
    }

    protected abstract void loadPage();

    protected abstract int totalPageCount();

    protected abstract boolean isLastPage();

    protected abstract boolean isLoading();
}
