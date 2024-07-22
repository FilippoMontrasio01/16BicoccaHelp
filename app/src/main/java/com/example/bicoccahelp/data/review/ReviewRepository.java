package com.example.bicoccahelp.data.review;

import com.example.bicoccahelp.data.Callback;

import java.util.List;

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

    public void listReviewedTutors(String uidStudent, Long limit, Callback<List<ReviewModel>> callback){
        reviewRemoteDataSource.listReviewsByStudent(uidStudent, limit, callback);
    }

    public void getAverageReview(String uidTutor, Callback<Double> callback){
        reviewRemoteDataSource.getAverageReview(uidTutor, callback);
    }

    public void getAverageReviewOrder(String uidTutor, Callback<Double> callback){
        reviewRemoteDataSource.getAverageReviewOrder(uidTutor, callback);
    }

    public void listReviews(String uidTutor, int limit, Callback<List<ReviewModel>> callback){
        reviewRemoteDataSource.listReviews(uidTutor, limit, callback);
    }

    public void getReview(String studentUid, String tutorUid, Callback<Float> callback){
        reviewRemoteDataSource.getReview(studentUid, tutorUid, callback);
    }
}
