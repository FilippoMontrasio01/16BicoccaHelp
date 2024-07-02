package com.example.bicoccahelp.ui.welcome;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserRepository;

public class WelcomeViewModelFactory implements ViewModelProvider.Factory {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    public WelcomeViewModelFactory(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WelcomeViewModel(authRepository, userRepository);
    }
}
