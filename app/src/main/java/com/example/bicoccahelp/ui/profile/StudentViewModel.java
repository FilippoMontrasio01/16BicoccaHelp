package com.example.bicoccahelp.ui.profile;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;

public class StudentViewModel extends ViewModel {
    private final MutableLiveData<Boolean> studentExistsLiveData;
    private final MutableLiveData<String> errorMessage;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentViewModel(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.studentExistsLiveData = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
    }

    public LiveData<Boolean> getStudentExists() {
        return studentExistsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void checkStudentExists() {
        String uid = userRepository.getCurrentUser().uid;

        studentRepository.studentExist(uid, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                studentExistsLiveData.postValue(exist);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }
}