package com.example.bicoccahelp.ui.lessonBooking;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.user.tutor.TutorRepository;

public class TutorViewModelFactory implements ViewModelProvider.Factory {
    private final TutorRepository tutorRepository;

    public TutorViewModelFactory(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TutorViewModel(tutorRepository);
    }
}