package com.example.bicoccahelp.data.review;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReviewRemoteDataSource {

    private static final String FIELD_UID_STUDENT = "uid Student";
    private static final String FIELD_UID_TUTOR = "uid Tutor";
    private static final String FIELD_STARS = "stars";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference review = db.collection("review");

    public void createReview(CreateReviewRequest request, Callback<ReviewModel> callback){

        Map<String, Object> data = new HashMap<>();

        data.put(FIELD_UID_TUTOR, request.getUidTutor());
        data.put(FIELD_UID_STUDENT, request.getUidStudent());
        data.put(FIELD_STARS, request.getStars());


        review.add(data)
                .addOnSuccessListener(documentReference -> {
                    ReviewModel reviewModel = new ReviewModel(
                            request.getUidTutor(), request.getUidStudent(),
                            request.getStars());
                    callback.onSucces(reviewModel);
                })
                .addOnFailureListener(callback::onFailure);

    }

    public void checkReview(String uidStudent, String uidTutor, Callback<Boolean> callback){
        review.whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .whereEqualTo(FIELD_UID_STUDENT, uidStudent)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        callback.onSucces(true);
                    }else {
                        callback.onSucces(false);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


}
