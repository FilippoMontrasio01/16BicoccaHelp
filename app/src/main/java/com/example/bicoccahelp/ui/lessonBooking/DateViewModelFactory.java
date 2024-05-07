package com.example.bicoccahelp.ui.lessonBooking;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.date.DateRepository;

public class DateViewModelFactory implements ViewModelProvider.Factory {

    private final DateRepository dateRepository;

    public DateViewModelFactory(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DateViewModel(dateRepository);
    }
}
