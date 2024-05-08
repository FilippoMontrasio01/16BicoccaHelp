package com.example.bicoccahelp.data.review;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewRemoteDataSource {

    private static final String FIELD_UID_STUDENT = "uid Student";
    private static final String FIELD_UID_TUTOR = "uid Tutor";
    private static final String FIELD_STARS = "stars";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference review = db.collection("review");
    private DocumentSnapshot lastDocument;

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

    public void getReview(String uidStudent, String uidTutor, Callback<Float> callback){
        review.whereEqualTo(FIELD_UID_STUDENT, uidStudent)
                .whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .get()
                .addOnSuccessListener(task -> {

                    if(!task.getDocuments().isEmpty()){
                        DocumentSnapshot documentSnapshot = task.getDocuments().get(0);
                        float stars = Float.parseFloat(documentSnapshot.getString(FIELD_STARS));
                        callback.onSucces(stars);
                    }else{
                        callback.onFailure(new Exception("No review found with the given uidStudent and uidTutor"));
                    }


                })
                .addOnFailureListener(callback::onFailure);
    }

    public void listReviewsByStudent(String uidStudent, Long limit, Callback<List<ReviewModel>> callback) {
        Query query = review.whereEqualTo(FIELD_UID_STUDENT, uidStudent)
                .orderBy(FIELD_STARS, Query.Direction.DESCENDING)
                .limit(limit);
        if (lastDocument != null) {
            query = query.startAfter(lastDocument);
        }
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    if (documents.size() > 0) {
                        lastDocument = documents.get(documents.size() - 1);
                    }
                    List<ReviewModel> reviewList = new ArrayList<>();
                    for (DocumentSnapshot document: documents) {
                        String uidTutor = document.getString(FIELD_UID_TUTOR) != null ? document.getString(FIELD_UID_TUTOR) : "";
                        float stars = document.getDouble(FIELD_STARS) != null ? document.getDouble(FIELD_STARS).floatValue() : 0;
                        ReviewModel reviewModel = new ReviewModel(uidTutor, uidStudent, stars);
                        reviewList.add(reviewModel);
                    }
                    callback.onSucces(reviewList);
                })
                .addOnFailureListener(callback::onFailure);
    }




}
