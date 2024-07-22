package com.example.bicoccahelp.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;

public class HomeViewModel extends ViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<String> errorMessage;


    public HomeViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
        this.errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void getUserName(Callback<String> callback){
        UserModel userModel = userRepository.getCurrentUser();

        if(userModel != null){
            callback.onSucces(userModel.getName());
        }else{
            errorMessage.setValue("No name found");
        }
    }
}
