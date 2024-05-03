package com.example.bicoccahelp.data.Lesson;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class LessonModel {

    private @NonNull String uid_Student;
    private @NonNull String uid_tutor;
    private @NonNull int duration;
    private  @NonNull Timestamp lessonDate;

    private @NonNull String description;



    private @NonNull String id;

    public LessonModel(@NonNull String id, @NonNull String uid_Student, @NonNull String uid_tutor,
                       int duration, @NonNull Timestamp lessonDate, @NonNull String description) {

        this.id = id;
        this.uid_Student = uid_Student;
        this.uid_tutor = uid_tutor;
        this.duration = duration;
        this.lessonDate = lessonDate;
        this.description = description;

    }

    public void setUid_Student(@NonNull String uid_Student) {
        this.uid_Student = uid_Student;
    }

    public void setUid_tutor(@NonNull String uid_tutor) {
        this.uid_tutor = uid_tutor;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setlessonDate(@NonNull Timestamp date) {
        lessonDate = date;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setLessonDate(@NonNull Timestamp lessonDate) {
        this.lessonDate = lessonDate;
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

    public int getDuration() {
        return duration;
    }

    @NonNull
    public Timestamp getLessonDate() {
        return lessonDate;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
