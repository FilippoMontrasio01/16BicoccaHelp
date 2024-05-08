package com.example.bicoccahelp.ui.review;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.ui.lessonBooking.TutorViewModel;

public class ReviewViewModelFactory implements ViewModelProvider.Factory {
    private final ReviewRepository reviewRepository;

    public ReviewViewModelFactory(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReviewViewModel(reviewRepository);
    }
}
