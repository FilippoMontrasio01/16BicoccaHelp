package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Map;

public class CreateTutorRequest {
    public final @NonNull String corsoDiStudi;
    public final @NonNull Map<String, Boolean> disponibilities;

    public CreateTutorRequest(@NonNull String corsoDiStudi, @NonNull Map<String, Boolean> disponibilities) {
        this.corsoDiStudi = corsoDiStudi;
        this.disponibilities = disponibilities;
    }
}
