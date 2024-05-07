package com.example.bicoccahelp.ui.lessonBooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.date.DateModel;
import com.example.bicoccahelp.date.DateRepository;
import com.google.firebase.Timestamp;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DateViewModel extends ViewModel {

    public static class UiState {
        public final int sizeBeforeFetch;
        public final int fetched;


        public UiState(int sizeBeforeFetch, int fetched) {
            this.sizeBeforeFetch = sizeBeforeFetch;
            this.fetched = fetched;
        }
    }

    private final MutableLiveData<TutorViewModel.UiState> uiState;
    //private final MutableLiveData<List<TutorModel>> updateTutorList;
    //private List<TutorModel> originalTutorList;
    private final MutableLiveData<String> errorMessage;
    public final ArrayList<String> dateList;
    public final DateRepository dateRepository;


    private final Long limit = 80L;
    private boolean hasMore = true;
    private int currentPage = 0;


    public DateViewModel(DateRepository dateRepository) {
        this.uiState = new MutableLiveData<>(new TutorViewModel.UiState(0, 0));;
        this.errorMessage = new MutableLiveData<>(null);;
        this.dateRepository = dateRepository;
        this.dateList = new ArrayList<>();
    }

    public LiveData<TutorViewModel.UiState> getUiState() {
        return uiState;
    }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public boolean hasMore() {
        return hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void resetCurrentPage() {
        currentPage = 0;
    }

    public void getNextHourPage(String uidTutor, String uidStudent, Timestamp data) {


        dateRepository.listOrari(uidTutor, uidStudent, data, limit, new Callback<List<String>>() {
            @Override
            public void onSucces(List<String> strings) {
                currentPage += 1;
                if (strings.size() < limit) {
                    hasMore = false;
                }

                if (strings.size() == 0) {
                    return;
                }

                int initialSize = dateList.size();
                int newItemsCount = strings.size();
                dateList.addAll(strings);
                uiState.postValue(new TutorViewModel.UiState(initialSize, newItemsCount));
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });





    }

}
