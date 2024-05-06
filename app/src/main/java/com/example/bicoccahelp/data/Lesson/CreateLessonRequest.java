package com.example.bicoccahelp.data.Lesson;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class CreateLessonRequest {
    private @NonNull String uid_Student;
    private @NonNull String uid_tutor;
    private @NonNull int duration;

    private  @NonNull String idLesson;
    private @NonNull String description;

    public CreateLessonRequest(@NonNull String uid_Student, @NonNull String uid_tutor,
                               int duration, @NonNull String idLesson,
                               @NonNull String description) {
        this.uid_Student = uid_Student;
        this.uid_tutor = uid_tutor;
        this.duration = duration;
        this.idLesson = idLesson;
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

    public int getDuration() {
        return duration;
    }

    @NonNull
    public String getIdLesson() {
        return idLesson;
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

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLessonDate(@NonNull String idLesson) {
        idLesson = idLesson;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

}
