package com.example.bicoccahelp.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;

public class CompleteProfileVIewModelFactory implements ViewModelProvider.Factory{

    private final CorsoDiStudiRepository corsoDiStudiRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    public CompleteProfileVIewModelFactory(CorsoDiStudiRepository corsoDiStudiRepository,
                                           UserRepository userRepository,
                                           StudentRepository studentRepository,
                                           TutorRepository tutorRepository) {

        this.corsoDiStudiRepository = corsoDiStudiRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new CompleteProfileViewModel(corsoDiStudiRepository,
                userRepository, studentRepository, tutorRepository);
    }
}
