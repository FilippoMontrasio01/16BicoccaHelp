package com.example.bicoccahelp.ui.review;

import android.net.Uri;
import android.telecom.Call;

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
    private final MutableLiveData<String> tutorId;
    private final MutableLiveData<Boolean> reviewExist;
    public final List<ReviewModel> reviewList;

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final TutorRepository tutorRepository;

    private final Long limit = 80L;
    private boolean hasMore = true;
    private int currentPage = 0;

    public ReviewViewModel(ReviewRepository reviewRepository, TutorRepository tutorRepository, UserRepository userRepository) {
        this.errorMessage = new MutableLiveData<>(null);
        this.reviewList = new ArrayList<>();
        this.originalReviewList = new ArrayList<>();
        this.updateReviewList = new MutableLiveData<>(new ArrayList<>());
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.uiStateMutableLiveData = new MutableLiveData<>(new UiState(0, 0, -1));
        this.tutorId = new MutableLiveData<>();
        this.reviewExist = new MutableLiveData<>();
    }

    public LiveData<UiState> getUiState() {
        return uiStateMutableLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getTutor() {
        return tutorId;
    }

    public LiveData<Boolean> getReviewExist() {
        return reviewExist;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public LiveData<List<ReviewModel>> getReviewList() {
        return updateReviewList;
    }

    public void getNextReviewPage() {
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

    public void findTutor(String tutorName) {
        tutorRepository.getTutorUid(tutorName, new Callback<String>() {
            @Override
            public void onSucces(String uidTutor) {
                if (!uidTutor.equals(userRepository.getCurrentUser().getUid())) {
                    tutorId.postValue(uidTutor);
                } else {
                    errorMessage.postValue("You cannot review yourself");
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue("Tutor not found");
            }
        });
    }

    public void verifyMultipleReview(String uidTutor) {
        String uidStudent = userRepository.getCurrentUser().getUid();

        reviewRepository.checkReview(uidStudent, uidTutor, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean notExist) {
                if (!notExist) {
                    reviewExist.postValue(true);
                } else {
                    reviewExist.postValue(false);
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue("Firestore Database Error");
            }
        });
    }

    public void createReview(CreateReviewRequest request, Callback<ReviewModel> callback) {
        reviewRepository.createReview(request, new Callback<ReviewModel>() {
            @Override
            public void onSucces(ReviewModel reviewModel) {
                UiState currUiState = uiStateMutableLiveData.getValue();
                int inserted = currUiState.sizeBeforeFetch;

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

    public String getCurrentUserUid() {
        return userRepository.getCurrentUser().getUid();
    }

    public void getTutorName(String uidTutor, Callback<String> callback){
        tutorRepository.getTutorName(uidTutor, new Callback<String>() {
            @Override
            public void onSucces(String tutorName) {
                callback.onSucces(tutorName);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Tutor name not found");
            }
        });
    }

    public void getPhotoUri(String uidTutor, Callback<Uri> callback){
        tutorRepository.getTutorPhotoUri(uidTutor, new Callback<Uri>() {
            @Override
            public void onSucces(Uri photoUri) {
                callback.onSucces(photoUri);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Tutor photo not found");
            }
        });
    }

    public void getTutorEmail(String uidTutor, Callback<String> callback){
        tutorRepository.getTutorEmail(uidTutor, new Callback<String>() {
            @Override
            public void onSucces(String email) {
                callback.onSucces(email);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Tutor email not found");
            }
        });
    }
}
