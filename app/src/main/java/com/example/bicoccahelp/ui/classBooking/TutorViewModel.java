package com.example.bicoccahelp.ui.classBooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;

import java.util.ArrayList;
import java.util.List;

public class TutorViewModel extends ViewModel {

    public static class UiState {
        public final int sizeBeforeFetch;
        public final int fetched;


        public UiState(int sizeBeforeFetch, int fetched) {
            this.sizeBeforeFetch = sizeBeforeFetch;
            this.fetched = fetched;
        }
    }

    private final MutableLiveData<UiState> uiState;
    private final MutableLiveData<String> errorMessage;
    public final List<TutorModel> tutorList;
    private final TutorRepository tutorRepository;
    private final Long limit = 25L;
    private boolean hasMore = true;
    private int currentPage = 0;

    public TutorViewModel(TutorRepository tutorRepository) {
        this.uiState = new MutableLiveData<>(new UiState(0, 0));
        this.errorMessage = new MutableLiveData<>(null);
        this.tutorList = new ArrayList<>();
        this.tutorRepository = tutorRepository;
    }

    public LiveData<UiState> getUiState() {
        return uiState;
    }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public boolean hasMore() {
        return hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void getNextServicesPage() {
        if (!hasMore) {
            return;
        }

        tutorRepository.listTutors(limit, new Callback<List<TutorModel>>() {
            @Override
            public void onSucces(List<TutorModel> data) {
                currentPage += 1;

                if (data.size() < limit) {
                    hasMore = false;
                }

                if (data.size() == 0) {
                    return;
                }

                int initialSize = tutorList.size();
                int newItemsCount = data.size();
                tutorList.addAll(data);
                uiState.postValue(new UiState(initialSize, newItemsCount));
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }





}
