package com.example.bicoccahelp.ui.profile;



import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;



public class StudentViewModelFactory implements ViewModelProvider.Factory {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentViewModelFactory(StudentRepository studentRepository,
                                   UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new StudentViewModel(studentRepository, userRepository);
    }
}
