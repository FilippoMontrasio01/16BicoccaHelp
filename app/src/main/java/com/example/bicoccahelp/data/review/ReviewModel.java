package com.example.bicoccahelp.data.review;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ReviewModel {

    private @NonNull String uidTutor;
    private @NonNull String uidStudent;
    private @NonNull double stars;


    public ReviewModel(@NonNull String uidTutor,
                       @NonNull String uidStudent, double stars) {
        this.uidTutor = uidTutor;
        this.uidStudent = uidStudent;
        this.stars = stars;
    }

    public void setUidTutor(@NonNull String uidTutor) {
        this.uidTutor = Objects.requireNonNull(uidTutor, "uidTutor cannot be null");
        this.uidStudent = Objects.requireNonNull(uidStudent, "uidStudent cannot be null");
    }

    public void setUidStudent(@NonNull String uidStudent) {

        this.uidStudent = Objects.requireNonNull(uidStudent, "uidStudent cannot be null");
    }

    public void setStars(double stars) {

        if (stars < 0.0 || stars > 5.0) {
            throw new IllegalArgumentException("Stars must be between 0.0 and 5.0");
        }

        this.stars = stars;
    }

    @NonNull
    public String getUidTutor() {
        return uidTutor;
    }

    @NonNull
    public String getUidStudent() {
        return uidStudent;
    }

    public double getStars() {
        return stars;
    }


}
