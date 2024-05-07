package com.example.bicoccahelp.data.review;

import androidx.annotation.NonNull;

public class ReviewModel {

    private final @NonNull String uidTutor;
    private final @NonNull String uidStudent;
    private final @NonNull float stars;


    public ReviewModel(@NonNull String uidTutor,
                       @NonNull String uidStudent, float stars) {
        this.uidTutor = uidTutor;
        this.uidStudent = uidStudent;
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

    public float getStars() {
        return stars;
    }


}
