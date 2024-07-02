package com.example.bicoccahelp.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.ui.welcome.WelcomeViewModel;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    public ProfileViewModelFactory(AuthRepository authRepository, UserRepository userRepository,
                                   StudentRepository studentRepository,
                                   TutorRepository tutorRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
    }

    public ProfileViewModelFactory(UserRepository userRepository,
                            StudentRepository studentRepository,
                            TutorRepository tutorRepository) {
        this(null, userRepository, studentRepository, tutorRepository);
    }

    public ProfileViewModelFactory(AuthRepository authRepository, UserRepository userRepository){
        this(authRepository, userRepository, null, null);
    }

    public ProfileViewModelFactory(AuthRepository authRepository){
        this(authRepository, null, null, null);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProfileViewModel(authRepository, userRepository,
                studentRepository, tutorRepository);
    }
}
