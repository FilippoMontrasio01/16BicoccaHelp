package com.example.bicoccahelp.data.review;

import com.example.bicoccahelp.data.Callback;

public class ReviewRepository {

    private ReviewRemoteDataSource reviewRemoteDataSource;

    public ReviewRepository(ReviewRemoteDataSource reviewRemoteDataSource) {
        this.reviewRemoteDataSource = reviewRemoteDataSource;
    }

    public void createReview(CreateReviewRequest request, Callback<ReviewModel> callback){
        reviewRemoteDataSource.createReview(request, callback);
    }

    public void checkReview(String uidStudent, String uidTutor, Callback<Boolean> callback){
        reviewRemoteDataSource.checkReview(uidStudent, uidTutor, callback);
    }
}
