package com.example.bicoccahelp.data.review;

import androidx.annotation.NonNull;

public class CreateReviewRequest {
    private final @NonNull String uidTutor;
    private final @NonNull String uidStudent;
    private final @NonNull double stars;



    public CreateReviewRequest(@NonNull String uidTutor, @NonNull String uidStudent,
                               double stars) {
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

    public double getStars() {
        return stars;
    }


}
