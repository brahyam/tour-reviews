package com.dvipersquad.tourreviews.data.source;

import com.dvipersquad.tourreviews.data.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsApiResponse {

    private boolean status;

    @SerializedName("total_reviews_comments")
    private int totalReviews;

    private List<Review> data;

    public ReviewsApiResponse(boolean status, int totalReviews, List<Review> data) {
        this.status = status;
        this.totalReviews = totalReviews;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public List<Review> getData() {
        return data;
    }

    public void setData(List<Review> data) {
        this.data = data;
    }
}
