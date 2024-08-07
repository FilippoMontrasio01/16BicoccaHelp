package com.example.bicoccahelp.ui.profile.lessons;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.ui.home.HomeViewModel;

public class LessonViewModelFactory implements ViewModelProvider.Factory{

    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;


    public LessonViewModelFactory(UserRepository userRepository, TutorRepository tutorRepository) {
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new LessonViewModel(userRepository, tutorRepository);
    }

}
