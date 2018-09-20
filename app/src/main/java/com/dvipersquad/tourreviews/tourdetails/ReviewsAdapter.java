package com.dvipersquad.tourreviews.tourdetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dvipersquad.tourreviews.R;
import com.dvipersquad.tourreviews.data.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviews;
    ReviewListener reviewListener;

    public ReviewsAdapter(List<Review> reviews, ReviewListener reviewListener) {
        this.reviews = reviews;
        this.reviewListener = reviewListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.reviews_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.txtReviewerName.setText(review.getReviewerName());
        holder.txtReviewRating.setText(String.format(Locale.getDefault(), "%s", review.getRating()));
        holder.txtReviewDate.setText(review.getDate().toString());
        holder.txtReviewTitle.setText(review.getTitle());
        holder.txtReviewMessage.setText(review.getMessage());

        if (reviewListener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reviewListener.onItemClickListener(review);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addData(List<Review> reviews) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View layout;
        TextView txtReviewerName;
        TextView txtReviewRating;
        TextView txtReviewDate;
        TextView txtReviewTitle;
        TextView txtReviewMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtReviewerName = itemView.findViewById(R.id.txtReviewerName);
            txtReviewRating = itemView.findViewById(R.id.txtReviewRating);
            txtReviewDate = itemView.findViewById(R.id.txtReviewDate);
            txtReviewTitle = itemView.findViewById(R.id.txtReviewTitle);
            txtReviewMessage = itemView.findViewById(R.id.txtReviewMessage);
        }
    }

    public interface ReviewListener {
        void onItemClickListener(Review review);
    }
}
