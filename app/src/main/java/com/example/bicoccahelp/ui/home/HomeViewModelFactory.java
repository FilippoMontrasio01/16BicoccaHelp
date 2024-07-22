package com.example.bicoccahelp.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.example.bicoccahelp.data.user.UserRepository;


public class HomeViewModelFactory implements ViewModelProvider.Factory{


    private final UserRepository userRepository;


    public HomeViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new HomeViewModel(userRepository);
    }
}
