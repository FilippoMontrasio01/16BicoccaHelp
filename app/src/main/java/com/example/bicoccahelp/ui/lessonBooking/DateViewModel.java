package com.example.bicoccahelp.ui.lessonBooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.date.DateRepository;
import com.google.firebase.Timestamp;


import java.util.ArrayList;
import java.util.List;

public class DateViewModel extends ViewModel {

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
    public final ArrayList<String> dateList;
    public final DateRepository dateRepository;

    private final Long limit = 80L;
    private boolean hasMore = true;
    private int currentPage = 0;

    public DateViewModel(DateRepository dateRepository) {
        this.uiState = new MutableLiveData<>(new UiState(0, 0));;
        this.errorMessage = new MutableLiveData<>(null);;
        this.dateRepository = dateRepository;
        this.dateList = new ArrayList<>();
    }

    public LiveData<UiState> getUiState() {
        return uiState;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void resetCurrentPage() {
        currentPage = 0;
    }

    public void getNextHourPage(String uidTutor,Timestamp data) {
        if (currentPage == 0) {
            hasMore = true;
        }

        dateRepository.listOrari(uidTutor,data, limit, new Callback<List<String>>() {
            @Override
            public void onSucces(List<String> strings) {
                if (currentPage == 0) {
                    dateList.clear();  // Clear the list when loading the first page
                }
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
                uiState.postValue(new UiState(initialSize, newItemsCount));
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }
}

