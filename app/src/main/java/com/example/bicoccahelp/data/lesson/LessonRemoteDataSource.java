package com.example.bicoccahelp.data.lesson;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.Callback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LessonRemoteDataSource {

    private static final String UID_STUDENT = "uid Student";
    private static final String UID_TUTOR = "uid Tutor";
    private static final String DESCRIPTION = "description";
    private static final String LESSON_DATE = "lesson Date";
    private static final String ORA = "hour";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference lesson = db.collection("Class");


    public void createLesson(CreateLessonRequest request,
                             Callback<LessonModel> callback){

        Map<String, Object> data = new HashMap<>();

        data.put(UID_STUDENT , request.getUid_Student());
        data.put(UID_TUTOR, request.getUid_tutor());
        data.put(DESCRIPTION , request.getDescription());
        data.put(LESSON_DATE , request.getData());
        data.put(ORA, request.getOra());

        lesson.add(data)
                .addOnSuccessListener(documentReference -> {

                    LessonModel lessonModel = new LessonModel(
                            lesson.getId(), request.getUid_Student(), request.getUid_tutor(),
                            request.getData(),
                            request.getOra(),
                            request.getDescription());

                    callback.onSucces(lessonModel);

                })
                .addOnFailureListener(callback::onFailure);
    }

    public void countLesson(String uidStudent, Timestamp day, Callback<Integer> callback){
        Query query = lesson
                .whereEqualTo(UID_STUDENT, uidStudent)
                .whereEqualTo(LESSON_DATE, day);
        AggregateQuery countQuery = query.count();

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               AggregateQuerySnapshot snapshot = task.getResult();
               long count = snapshot.getCount();
               callback.onSucces((int) count);
           } else {

           }
        }).addOnFailureListener(callback::onFailure);
    }

    public void checkHourPerDay(String uidStudent, Timestamp day, String hour, Callback<Boolean> callback){
        Query query = lesson
                .whereEqualTo(UID_STUDENT, uidStudent)
                .whereEqualTo(LESSON_DATE, day)
                .whereEqualTo(ORA, hour);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    callback.onSucces(true);
                } else {

                    callback.onSucces(false);
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}
