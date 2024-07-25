package com.example.bicoccahelp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.utils.ServiceLocator;

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
    private final MutableLiveData<List<LessonModel>> updateClassList;
    private List<TutorModel> originalTutorList;
    private List<LessonModel> originalClassList;
    public final List<TutorModel> tutorList;
    public final List<LessonModel> classList;
    private final MutableLiveData<List<TutorModel>> listT;
    private final MutableLiveData<List<LessonModel>> listC;
    private final TutorRepository tutorRepository;
    private final Long limit = 10L;
    private boolean hasMore = true;
    private int currentPage = 0;

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    private final MutableLiveData<String> errorMessage;


    public HomeViewModel(UserRepository userRepository, TutorRepository tutorRepository){
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.lessonRepository = ServiceLocator.getInstance().getLessonRepository();
        this.errorMessage = new MutableLiveData<>();

        this.originalTutorList = new ArrayList<>();
        this.originalClassList = new ArrayList<>();
        this.updateTutorList = new MutableLiveData<>(new ArrayList<>());
        this.updateClassList = new MutableLiveData<>(new ArrayList<>());
        this.tutorList = new ArrayList<>();
        this.listT = new MutableLiveData<>();
        this.classList = new ArrayList<>();
        this.listC = new MutableLiveData<>();
        this.uiStateMutableLiveData = new MutableLiveData<>(new UiState(0, 0, -1));
    }

    public String getUid(){
        return userRepository.getCurrentUser().getUid();
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<TutorModel>> getListT() {
        return listT;
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
                listT.setValue(data);
                uiStateMutableLiveData.postValue(newUiState);

            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public void getNextClassesPage(String uidStudent) {
        if (!hasMore) {
            return;
        }

        lessonRepository.listLessonsByStudent(uidStudent, limit, new Callback<List<LessonModel>>() {
            @Override
            public void onSucces(List<LessonModel> data) {
                currentPage += 1;
                if (data.size() < limit) {
                    hasMore = false;
                }

                if (data.size() == 0) {
                    return;
                }

                int sizeBeforeFetch = classList.size();
                int fetch = data.size();

                UiState newUiState = new UiState(sizeBeforeFetch, fetch, -1);
                classList.addAll(data);
                originalClassList.addAll(data);
                listC.setValue(data);
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
        classList.clear();
        tutorList.addAll(originalTutorList);
        classList.addAll(originalClassList);
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
