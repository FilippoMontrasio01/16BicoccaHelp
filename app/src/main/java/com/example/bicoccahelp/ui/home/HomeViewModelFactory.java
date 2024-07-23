package com.example.bicoccahelp.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;


public class HomeViewModelFactory implements ViewModelProvider.Factory{


    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;


    public HomeViewModelFactory(UserRepository userRepository, TutorRepository tutorRepository) {
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new HomeViewModel(userRepository, tutorRepository);
    }
}
