package com.example.bicoccahelp.ui.profile.lessons;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.utils.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class LessonViewModel  extends ViewModel {
    public static class UiState{
        public final int sizeBeforeFetch;
        public final int fetched;
        public final int inserted;

        public UiState(int sizeBeforeFetch, int fetched, int inserted){
            this.sizeBeforeFetch = sizeBeforeFetch;
            this.fetched = fetched;
            this.inserted = inserted;
        }
    }

    private final MutableLiveData<UiState> uiStateMutableLiveData;
    private final MutableLiveData<List<LessonModel>> updateClassList;
    private List<LessonModel> originalClassList;
    public List<LessonModel> classList;
    private final MutableLiveData<List<LessonModel>> listC;
    private final Long limit = 50L;
    private boolean hasMore = true;
    private int currentPage = 0;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final TutorRepository tutorRepository;
    private final MutableLiveData<String> errorMessage;

    public LessonViewModel(UserRepository userRepository, TutorRepository tutorRepository){
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.lessonRepository = ServiceLocator.getInstance().getLessonRepository();
        this.errorMessage = new MutableLiveData<>();
        this.originalClassList = new ArrayList<>();
        this.updateClassList = new MutableLiveData<>(new ArrayList<>());
        this.classList = new ArrayList<>();
        this.listC = new MutableLiveData<>();
        this.uiStateMutableLiveData = new MutableLiveData<>(new UiState(0, 0, -1));

    }

    public String getUid(){
        return userRepository.getCurrentUser().getUid();
    }

    public MutableLiveData<String> getErrorMessage(){
        return errorMessage;
    }

    public LiveData<List<LessonModel>> getListC(){
        return listC;
    }

    public LiveData<UiState> getUiState(){
        return uiStateMutableLiveData;
    }

    public boolean hasMore(){
        return hasMore;
    }

    public int getCurrentPage(){
        return currentPage;
    }

    public LiveData<List<LessonModel>> getLessonList(){
        return updateClassList;
    }

    public void getNextClassPage(String uidStudent){
        if(!hasMore){
            return;
        }

        lessonRepository.listLessonsByStudentDES(uidStudent, limit, new Callback<List<LessonModel>>() {
            @Override
            public void onSucces(List<LessonModel> data) {
                if(data.size() < limit){
                    hasMore = false;
                }

                if(data.size() == 0){
                    return;
                }

                int sizeBeforeFetch = classList.size();
                int fetched = data.size();

                UiState newUiState = new UiState(sizeBeforeFetch, fetched, -1);
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

    public void getNextTutorNameClassPage(String uidStudent, String uidTutor) {
        Log.d("lezione", "PROVA");


        Log.d("lezione", "PROVA 2");

        lessonRepository.listLessonsByTutorDES(uidStudent, uidTutor, limit, new Callback<List<LessonModel>>() {
            @Override
            public void onSucces(List<LessonModel> data) {
                if (data.size() < limit) {
                    hasMore = false;
                }

                if (data.size() == 0) {
                    return;
                }

                int sizeBeforeFetch = classList.size();
                int fetched = data.size();

                UiState newUiState = new UiState(sizeBeforeFetch, fetched, -1);
                classList.addAll(data);
                listC.setValue(data);
                uiStateMutableLiveData.postValue(newUiState);
                Log.d("lezione", "È ENTRATA");

            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
                Log.d("lezione", "NON ENTRA");

            }
        });
    }

    public void restoreOriginalList(){
        classList.clear();
        classList.addAll(originalClassList);
    }

    public void getTutorName(String uidTutor, Callback<String> callback){
        tutorRepository.getTutorName(uidTutor, new Callback<String>() {
            @Override
            public void onSucces(String name) {
                callback.onSucces(name);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void getTutorDetails(String uidTutor, Callback<TutorModel> callback) {
        tutorRepository.getTutorModelById(uidTutor, new Callback<TutorModel>() {
            @Override
            public void onSucces(TutorModel tutorModel) {
                callback.onSucces(tutorModel);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public void getTutorId(String tutorName){

        String userId = userRepository.getCurrentUser().getUid();

        tutorRepository.getTutorUid(tutorName, new Callback<String>() {
            @Override
            public void onSucces(String tutorId) {
                Log.d("lezione", "UID TUTOR È: "+ tutorId  + "USER UID È: "+ userId);
                getNextTutorNameClassPage(userId, tutorId);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("tutor not found");
            }
        });
    }


}