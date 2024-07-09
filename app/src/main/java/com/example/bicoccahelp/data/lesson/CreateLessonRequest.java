package com.example.bicoccahelp.data.lesson;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class CreateLessonRequest {
    private @NonNull String uid_Student;
    private @NonNull String uid_tutor;

    private final @NonNull Timestamp data;
    private @NonNull int ora;
    private @NonNull String description;

    public CreateLessonRequest(@NonNull String uid_Student, @NonNull String uid_tutor,
                               @NonNull Timestamp data,
                               @NonNull int ora,
                               @NonNull String description) {
        this.uid_Student = uid_Student;
        this.uid_tutor = uid_tutor;
        this.data = data;
        this.ora = ora;
        this.description = description;
    }

    @NonNull
    public String getUid_Student() {
        return uid_Student;
    }

    @NonNull
    public String getUid_tutor() {
        return uid_tutor;
    }

    public int getOra() {
        return ora;
    }

    @NonNull
    public Timestamp getData() {
        return data;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setUid_Student(@NonNull String uid_Student) {
        this.uid_Student = uid_Student;
    }

    public void setUid_tutor(@NonNull String uid_tutor) {
        this.uid_tutor = uid_tutor;
    }

    public void setLessonDate(@NonNull String idLesson) {
        idLesson = idLesson;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

}
