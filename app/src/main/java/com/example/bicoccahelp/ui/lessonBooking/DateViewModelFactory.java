package com.example.bicoccahelp.ui.lessonBooking;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bicoccahelp.data.date.DateRepository;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.google.firebase.firestore.auth.User;

public class DateViewModelFactory implements ViewModelProvider.Factory {

    private final DateRepository dateRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;

    private final StudentRepository studentRepository;
    public DateViewModelFactory(DateRepository dateRepository, LessonRepository lessonRepository,
                                UserRepository userRepository, TutorRepository tutorRepository,
                                StudentRepository studentRepository) {
        this.dateRepository = dateRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DateViewModel(dateRepository, lessonRepository, userRepository, tutorRepository, studentRepository);
    }
}
