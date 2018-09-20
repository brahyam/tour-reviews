package com.dvipersquad.tourreviews.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dvipersquad.tourreviews.data.Review;

import java.util.List;

@Dao
public interface ReviewsDao {

    /**
     * Select all reviews
     *
     * @return all Reviews
     */
    @Query("SELECT * FROM Reviews")
    List<Review> getReviews();

    /**
     * Select a review by id
     *
     * @param reviewId the review id
     * @return review with reviewId if found, null otherwise.
     */
    @Query("SELECT * FROM Reviews WHERE id = :reviewId")
    Review getReviewById(Integer reviewId);

    /**
     * Store review in DB (Replace if exist previously)
     *
     * @param review the review to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReview(Review review);

    /**
     * Updates review in DB
     *
     * @param review the review to be updated
     */
    @Update
    void updateReview(Review review);

    /**
     * Deletes a review by id
     *
     * @param reviewId the review id to be deleted
     * @return number of Reviews deleted. 0 if not found / 1 if found.
     */
    @Query("DELETE FROM Reviews WHERE id = :reviewId")
    int deleteReviewById(Integer reviewId);

    /**
     * Deletes all Reviews
     */
    @Query("DELETE FROM Reviews")
    void deleteReviews();
}
