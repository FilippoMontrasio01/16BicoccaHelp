package com.example.bicoccahelp.data.lesson;

import android.util.Log;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                             Callback<LessonModel> callback) {

        Map<String, Object> data = new HashMap<>();

        data.put(UID_STUDENT, request.getUid_Student());
        data.put(UID_TUTOR, request.getUid_tutor());
        data.put(DESCRIPTION, request.getDescription());
        data.put(LESSON_DATE, request.getData());
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

    public void countLesson(String uidStudent, Timestamp day, Callback<Integer> callback) {
        Query query = lesson
                .whereEqualTo(UID_STUDENT, uidStudent)
                .whereEqualTo(LESSON_DATE, day);
        AggregateQuery countQuery = query.count();

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                long count = snapshot.getCount();
                callback.onSucces((int) count);
            } else {

            }
        }).addOnFailureListener(callback::onFailure);
    }

    public void checkHourPerDay(String uidStudent, Timestamp day, String hour, Callback<Boolean> callback) {
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
    public void listLessonsByStudentASC(String uidStudent, Long limit, Callback<List<LessonModel>> callback) {
        Query query = lesson.whereEqualTo(UID_STUDENT, uidStudent)
                .orderBy(LESSON_DATE, Query.Direction.ASCENDING)
                .limit(limit);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<LessonModel> lessons = new ArrayList<>();
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        LessonModel lessonModel = new LessonModel(
                                document.getId(),
                                document.getString(UID_STUDENT),
                                document.getString(UID_TUTOR),
                                document.getTimestamp(LESSON_DATE),
                                document.getString(ORA),
                                document.getString(DESCRIPTION)
                        );
                        lessons.add(lessonModel);
                    }
                    callback.onSucces(lessons);
                } else {

                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public void listLessonsByStudentDES(String uidStudent, Long limit, Callback<List<LessonModel>> callback) {
        Query query = lesson.whereEqualTo(UID_STUDENT, uidStudent)
                .orderBy(LESSON_DATE, Query.Direction.DESCENDING)
                .limit(limit);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<LessonModel> lessons = new ArrayList<>();
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        LessonModel lessonModel = new LessonModel(
                                document.getId(),
                                document.getString(UID_STUDENT),
                                document.getString(UID_TUTOR),
                                document.getTimestamp(LESSON_DATE),
                                document.getString(ORA),
                                document.getString(DESCRIPTION)
                        );
                        lessons.add(lessonModel);
                    }
                    callback.onSucces(lessons);
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public void listLessonsByTutorDES(String uidStudent, String uidTutor, Long limit, Callback<List<LessonModel>> callback) {
        // Inizia la query
        Query query = lesson.whereEqualTo(UID_STUDENT, uidStudent)
                .whereEqualTo(UID_TUTOR, uidTutor)
                .orderBy(LESSON_DATE, Query.Direction.DESCENDING)
                .limit(limit);

        Log.d("lezione", "Inizio query: UID_STUDENT = " + uidStudent + ", UID_TUTOR = " + uidTutor);

        // Esegui la query
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<LessonModel> lessons = new ArrayList<>();
                QuerySnapshot snapshot = task.getResult();

                // Se la query restituisce documenti
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        // Log dei dati grezzi del documento
                        Log.d("lezione", "Documento trovato: " + document.getData());

                        // Creazione dell'oggetto LessonModel dai dati del documento
                        LessonModel lessonModel = new LessonModel(
                                document.getId(),
                                document.getString(UID_STUDENT),
                                document.getString(UID_TUTOR),
                                document.getTimestamp(LESSON_DATE),
                                document.getString(ORA),
                                document.getString(DESCRIPTION)
                        );
                        lessons.add(lessonModel);

                        // Log dell'UID dello studente e del tutor per confermare che sono corretti
                        Log.d("lezione", "UID TUTOR: " + lessonModel.getUid_tutor() +
                                " | UID STUDENT: " + lessonModel.getUid_Student() +
                                " | UID LEZIONE: " + lessonModel.getId());
                    }
                    // Passa i risultati alla callback
                    callback.onSucces(lessons);
                } else {
                    Log.d("lezione", "Nessun documento trovato che corrisponde alla query.");
                    callback.onSucces(new ArrayList<>()); // Restituisce una lista vuota
                }
            } else {
                Log.d("lezione", "Query fallita: " + task.getException());
                callback.onFailure(task.getException());
            }
        });
    }




    public void deleteLesson(String lessonUid, Callback<Void> callback){
        lesson.document(lessonUid)
                .delete()
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);
    }
}