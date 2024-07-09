package com.example.bicoccahelp.data.lesson;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class LessonModel {

    private @NonNull String uid_Student;
    private @NonNull String uid_tutor;
    private final @NonNull Timestamp data;
    private @NonNull int ora;

    private @NonNull String description;



    private @NonNull String id;

    public LessonModel(@NonNull String id, @NonNull String uid_Student, @NonNull String uid_tutor,
                       @NonNull Timestamp data, @NonNull int ora, @NonNull String description) {

        this.id = id;
        this.uid_Student = uid_Student;
        this.uid_tutor = uid_tutor;
        this.data = data;
        this.ora = ora;
        this.description = description;

    }

    public void setUid_Student(@NonNull String uid_Student) {
        this.uid_Student = uid_Student;
    }

    public void setUid_tutor(@NonNull String uid_tutor) {
        this.uid_tutor = uid_tutor;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public int getOra() {
        return ora;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getUid_Student() {
        return uid_Student;
    }

    @NonNull
    public String getUid_tutor() {
        return uid_tutor;
    }

    @NonNull
    public Timestamp getData() {
        return data;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
