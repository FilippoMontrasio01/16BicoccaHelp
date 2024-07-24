package com.example.bicoccahelp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    public static class UiState{
        public final int sizeBeforeFetch;
        public final int fetched;
        public final int inserted;

        public UiState(int sizeBeforeFetch, int fetched, int inserted) {
            this.sizeBeforeFetch = sizeBeforeFetch;
            this.fetched = fetched;
            this.inserted = inserted;
        }


    }

    private final MutableLiveData<UiState> uiStateMutableLiveData;
    private final MutableLiveData<List<TutorModel>> updateTutorList;
    private List<TutorModel> originalTutorList;
    public final List<TutorModel> tutorList;
    private final MutableLiveData<List<TutorModel>> list;
    private final TutorRepository tutorRepository;
    private final Long limit = 10L;
    private boolean hasMore = true;
    private int currentPage = 0;

    private final UserRepository userRepository;

    private final MutableLiveData<String> errorMessage;


    public HomeViewModel(UserRepository userRepository, TutorRepository tutorRepository){
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.errorMessage = new MutableLiveData<>();

        this.originalTutorList = new ArrayList<>();
        this.updateTutorList = new MutableLiveData<>(new ArrayList<>());
        this.tutorList = new ArrayList<>();
        this.list = new MutableLiveData<>();
        this.uiStateMutableLiveData = new MutableLiveData<>(new UiState(0, 0, -1));
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<TutorModel>> getList() {
        return list;
    }

    public LiveData<UiState> getUiState() {
        return uiStateMutableLiveData;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public LiveData<List<TutorModel>> getTutorList() {
        return updateTutorList;
    }

    public void getNextTutorsPage() {
        if (!hasMore) {
            return;
        }

        tutorRepository.listTutorOrderReview(limit, new Callback<List<TutorModel>>() {
            @Override
            public void onSucces(List<TutorModel> data) {
                currentPage += 1;
                if (data.size() < limit) {
                    hasMore = false;
                }

                if (data.size() == 0) {
                    return;
                }



                int sizeBeforeFetch = tutorList.size();
                int fetch = data.size();

                UiState newUiState = new UiState(sizeBeforeFetch, fetch, -1);
                tutorList.addAll(data);
                originalTutorList.addAll(data);
                list.setValue(data);
                uiStateMutableLiveData.postValue(newUiState);

            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public void restoreOriginalList() {
        tutorList.clear();
        tutorList.addAll(originalTutorList);
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
