package com.example.bicoccahelp.data.lesson;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.Objects;
import com.google.firebase.Timestamp;

public class CreateLessonRequest {
    private @NonNull String uid_Student;
    private @NonNull String uid_tutor;
    private @NonNull Timestamp data;
    private @NonNull String ora;
    private @NonNull String description;

    public CreateLessonRequest(@NonNull String uid_Student, @NonNull String uid_tutor,
                               @NonNull Timestamp data, @NonNull String ora,
                               @NonNull String description) {
        this.uid_Student = Objects.requireNonNull(uid_Student, "uid_Student cannot be null");
        this.uid_tutor = Objects.requireNonNull(uid_tutor, "uid_tutor cannot be null");
        this.data = Objects.requireNonNull(data, "data cannot be null");
        this.ora = Objects.requireNonNull(ora, "ora cannot be null");
        this.description = Objects.requireNonNull(description, "description cannot be null");
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
    public String getOra() {
        return ora;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setUid_Student(@NonNull String uid_Student) {
        this.uid_Student = Objects.requireNonNull(uid_Student, "uid_Student cannot be null");
    }

    public void setUid_tutor(@NonNull String uid_tutor) {
        this.uid_tutor = Objects.requireNonNull(uid_tutor, "uid_tutor cannot be null");
    }

    public void setData(@NonNull Timestamp data) {
        this.data = Objects.requireNonNull(data, "data cannot be null");
    }

    public void setOra(@NonNull String ora) {
        this.ora = Objects.requireNonNull(ora, "ora cannot be null");
    }

    public void setDescription(@NonNull String description) {
        this.description = Objects.requireNonNull(description, "description cannot be null");
    }
}
