package com.example.bicoccahelp.ui.review;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.ui.lessonBooking.TutorViewModel;

public class ReviewViewModelFactory implements ViewModelProvider.Factory {
    private ReviewRepository reviewRepository;
    private TutorRepository tutorRepository;
    private UserRepository userRepository;

    public ReviewViewModelFactory(ReviewRepository reviewRepository,TutorRepository tutorRepository,  UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.tutorRepository = tutorRepository;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReviewViewModel(reviewRepository, tutorRepository, userRepository);
    }
}