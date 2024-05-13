package com.example.bicoccahelp.ui.review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.review.CreateReviewRequest;
import com.example.bicoccahelp.data.review.ReviewModel;
import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.ui.lessonBooking.TutorViewModel;
import com.example.bicoccahelp.utils.ServiceLocator;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class ReviewViewModel extends ViewModel {

    public static class UiState {
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
    private final MutableLiveData<List<ReviewModel>> updateReviewList;
    private List<ReviewModel> originalReviewList;
    private final MutableLiveData<String> errorMessage;
    public final List<ReviewModel> reviewList;

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    private final Long limit = 80L;
    private boolean hasMore = true;
    private int currentPage = 0;

    public ReviewViewModel(ReviewRepository reviewRepository){
        this.errorMessage = new MutableLiveData<>(null);
        this.reviewList = new ArrayList<>();
        this.originalReviewList = new ArrayList<>();
        this.updateReviewList = new MutableLiveData<>(new ArrayList<>());
        this.reviewRepository = reviewRepository;
        this.userRepository = ServiceLocator.getInstance().getUserRepository();

        this.uiStateMutableLiveData = new MutableLiveData<>(new UiState(0, 0, -1));
    }


    public LiveData<UiState> getUiState() {
        return uiStateMutableLiveData;
    }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public boolean hasMore() {
        return hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public LiveData<List<ReviewModel>> getReviewList() {
        return updateReviewList;
    }

    public void getNextReviewPage(){

        String uidUser = userRepository.getCurrentUser().getUid();

        reviewRepository.listReviewedTutors(uidUser, limit, new Callback<List<ReviewModel>>() {
            @Override
            public void onSucces(List<ReviewModel> data) {
                currentPage += 1;
                if (data.size() < limit) {
                    hasMore = false;
                }

                if (data.size() == 0) {
                    return;
                }
                originalReviewList.addAll(data);

                int sizeBeforeFetched = reviewList.size();
                int fetched = data.size();


                UiState newUiState = new UiState(sizeBeforeFetched, fetched, -1);
                reviewList.addAll(data);

                uiStateMutableLiveData.postValue(newUiState);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }
    public void createReview(CreateReviewRequest request, Callback<ReviewModel> callback){
        reviewRepository.createReview(request, new Callback<ReviewModel>() {
            @Override
            public void onSucces(ReviewModel reviewModel) {
                UiState currUiState = uiStateMutableLiveData.getValue();

                int inserted = 0;

                UiState newUiState = new UiState(currUiState.sizeBeforeFetch, 0, inserted);
                reviewList.add(inserted, reviewModel);
                uiStateMutableLiveData.postValue(newUiState);
                callback.onSucces(reviewModel);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void refreshData() {

        currentPage = 0;
        reviewList.clear();
        originalReviewList.clear();


        getNextReviewPage();
    }





}
