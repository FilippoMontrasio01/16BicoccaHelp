package com.example.bicoccahelp.data.Lesson;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LessonRemoteDataSource {

    private static final String UID_STUDENT = "uid Student";
    private static final String UID_TUTOR = "uid Tutor";
    private static final String DURATION = "duration";
    private static final String DESCRIPTION = "description";
    private static final String LESSON_DATE = "lesson Date";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference lesson = db.collection("Class");


    public void createLesson(CreateLessonRequest request,
                             Callback<LessonModel> callback){

        Map<String, Object> data = new HashMap<>();

        data.put(UID_STUDENT , request.getUid_Student());
        data.put(UID_TUTOR, request.getUid_tutor());
        data.put(DURATION , request.getDuration());
        data.put(DESCRIPTION , request.getDescription());
        data.put(LESSON_DATE , request.getLessonDate());

        lesson.add(data)
                .addOnSuccessListener(documentReference -> {

                    LessonModel lessonModel = new LessonModel(
                            lesson.getId(), request.getUid_Student(), request.getUid_tutor(),
                            request.getDuration(), request.getLessonDate(),
                            request.getDescription());

                    callback.onSucces(lessonModel);

                })
                .addOnFailureListener(callback::onFailure);
    }


}
