package com.example.bicoccahelp.data.review;

import androidx.annotation.NonNull;

import java.util.Objects;

public class CreateReviewRequest {
    private final @NonNull String uidTutor;
    private final @NonNull String uidStudent;
    private @NonNull double stars;



    public CreateReviewRequest(@NonNull String uidTutor, @NonNull String uidStudent,
                               @NonNull double stars) {
        this.uidTutor = Objects.requireNonNull(uidTutor, "uidTutor cannot be null");
        this.uidStudent = Objects.requireNonNull(uidStudent, "uidStudent cannot be null");
        this.stars = Objects.requireNonNull(stars, "stars cannot be null");

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

    public void setStars(@NonNull  double stars) {

        if (stars < 0.0 || stars > 5.0) {
            throw new IllegalArgumentException("Stars must be between 0.0 and 5.0");
        }

        this.stars = Objects.requireNonNull(stars, "stars cannot be null");
    }
}
