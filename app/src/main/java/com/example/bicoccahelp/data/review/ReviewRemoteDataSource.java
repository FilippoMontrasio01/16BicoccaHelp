package com.example.bicoccahelp.data.review;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateField;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReviewRemoteDataSource {

        private static final String FIELD_UID_STUDENT = "uid_Student";
        private static final String FIELD_UID_TUTOR = "uid_Tutor";
        private static final String FIELD_STARS = "stars";

        private static final String AVERAGE_REVIEW = "average review";

        private final FirebaseFirestore db = FirebaseFirestore.getInstance();
        private final CollectionReference review = db.collection("Review");

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

    public void getAverageReview(String uidTutor, Callback<Double> callback) {

        CollectionReference tutors = db.collection("Tutor");

        review.whereEqualTo(FIELD_UID_TUTOR, uidTutor).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double totalStars = 0.0;
                        int count = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Double stars = document.getDouble(FIELD_STARS);
                            if (stars != null) {
                                totalStars += stars;
                                count++;
                            }
                        }

                        if(count != 0){
                            double averageStars = totalStars / count;
                            double roundedAverageStars = Math.round(averageStars * 10.0) / 10.0;

                            // Aggiornamento del campo del tutor con la media calcolata
                            tutors.document(uidTutor).update(AVERAGE_REVIEW, roundedAverageStars)
                                    .addOnSuccessListener(aVoid -> {
                                        callback.onSucces(roundedAverageStars);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Firestore Error", "Error updating averageReview", e);
                                        callback.onFailure(e);
                                    });
                        } else {
                            callback.onSucces(null);
                        }
                    } else {
                        Log.d("Firestore Error", "Error getting documents: ", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void listReviews(String uidTutor, int limit, Callback<List<ReviewModel>> callback) {
        review.whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .orderBy(FIELD_STARS, Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<ReviewModel> reviewList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String uidStudent = document.getString(FIELD_UID_STUDENT);
                        double stars = document.getDouble(FIELD_STARS);
                        ReviewModel reviewModel = new ReviewModel(uidTutor, uidStudent, (float) stars);
                        reviewList.add(reviewModel);
                    }
                    callback.onSucces(reviewList);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
