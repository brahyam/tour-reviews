package com.dvipersquad.tourreviews.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "reviews")
public class Review {

    @PrimaryKey
    @NonNull
    @SerializedName("review_id")
    private int id;

    private double rating;

    private String title;

    private String message;

    private String date;

    private String languageCode;

    @SerializedName("traveler_type")
    private String travelerType;

    private String reviewerName;

    private String reviewerCountry;

    public Review(int id, double rating, String title, String message, String date, String languageCode, String travelerType, String reviewerName, String reviewerCountry) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.message = message;
        this.date = date;
        this.languageCode = languageCode;
        this.travelerType = travelerType;
        this.reviewerName = reviewerName;
        this.reviewerCountry = reviewerCountry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTravelerType() {
        return travelerType;
    }

    public void setTravelerType(String travelerType) {
        this.travelerType = travelerType;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerCountry() {
        return reviewerCountry;
    }

    public void setReviewerCountry(String reviewerCountry) {
        this.reviewerCountry = reviewerCountry;
    }

    @Override
    public String toString() {
        return "Review{id:" + id + ", title:" + title + ", date:" + date + ", lang:" + languageCode + "}";
    }
}
