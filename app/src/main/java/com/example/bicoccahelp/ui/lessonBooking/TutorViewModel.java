package com.example.bicoccahelp.ui.lessonBooking;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.utils.ServiceLocator;

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

    private final MutableLiveData<UiState> uiStateMutableLiveData;
    private final MutableLiveData<List<TutorModel>> updateTutorList;
    private List<TutorModel> originalTutorList;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> nomeCorso;
    private final MutableLiveData<String> areaCorso;
    public final List<TutorModel> tutorList;
    public final CorsoDiStudiRepository corsoDiStudiRepository;
    public final UserRepository userRepository;
    public final ReviewRepository reviewRepository;
    public final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final Long limit = 80L;
    private boolean hasMore = true;
    private int currentPage = 0;

    public TutorViewModel(TutorRepository tutorRepository) {
        this.uiStateMutableLiveData = new MutableLiveData<>(new UiState(0,0));
        this.errorMessage = new MutableLiveData<>(null);
        this.tutorList = new ArrayList<>();
        this.originalTutorList = new ArrayList<>();
        this.updateTutorList = new MutableLiveData<>(new ArrayList<>());
        this.tutorRepository = tutorRepository;
        this.corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();
        this.userRepository = ServiceLocator.getInstance().getUserRepository();
        this.reviewRepository = ServiceLocator.getInstance().getReviewRepository();
        this.studentRepository = ServiceLocator.getInstance().getStudentRepository();
        this.nomeCorso = new MutableLiveData<>();
        this.areaCorso = new MutableLiveData<>();
    }

    public LiveData<UiState> getUiState() {
        return uiStateMutableLiveData;
    }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public MutableLiveData<String> getNomeC() {
        return nomeCorso;
    }

    public MutableLiveData<String> getAreaC() {
        return areaCorso;
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



                int sizeBeforeFetch = tutorList.size();
                int fetch = data.size();
                UiState newUiState = new UiState(sizeBeforeFetch, fetch);
                tutorList.addAll(data);
                originalTutorList.addAll(data);
                uiStateMutableLiveData.postValue(newUiState);

            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }


    public void getTutorNamePage(String name) {


        tutorRepository.listTutorName(name, limit, new Callback<List<TutorModel>>() {
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
                UiState newUiState = new UiState(sizeBeforeFetch, fetch);
                tutorList.addAll(data);
                originalTutorList.addAll(data);
                uiStateMutableLiveData.postValue(newUiState);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public void getTutorCorsoDiStudiPage(String corsoDiStudi){
        corsoDiStudiRepository.getCorsoId(corsoDiStudi, new Callback<String>() {

            @Override
            public void onSucces(String idCorso) {
                tutorRepository.listTutorsCorsodiStudi(idCorso, limit,
                        new Callback<List<TutorModel>>() {
                    @Override
                    public void onSucces(List<TutorModel> data) {
                        currentPage += 1;
                        if (data.size() < limit) {
                            hasMore = false;
                        }

                        if (data.size() == 0) {
                            return;
                        }

                        Log.d("", data.toString());


                        int sizeBeforeFetch = tutorList.size();
                        int fetch = data.size();
                        UiState newUiState = new UiState(sizeBeforeFetch, fetch);
                        tutorList.addAll(data);
                        originalTutorList.addAll(data);
                        uiStateMutableLiveData.postValue(newUiState);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        errorMessage.postValue(e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public void getTutorSkillPage(String skill){
        tutorRepository.listTutorSkill(skill, limit, new Callback<List<TutorModel>>() {
            @Override
            public void onSucces(List<TutorModel> data) {
                currentPage += 1;
                if (data.size() < limit) {
                    hasMore = false;
                }

                if (data.size() == 0) {
                    return;
                }

                Log.d("", data.toString());


                int sizeBeforeFetch = tutorList.size();
                int fetch = data.size();
                UiState newUiState = new UiState(sizeBeforeFetch, fetch);
                tutorList.addAll(data);
                originalTutorList.addAll(data);
                uiStateMutableLiveData.postValue(newUiState);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public void getTutorDisponibilityPage(String day){
        tutorRepository.listTutorDisponibility(day, limit, new Callback<List<TutorModel>>() {
            @Override
            public void onSucces(List<TutorModel> data) {
                currentPage += 1;
                if (data.size() < limit) {
                    hasMore = false;
                }

                if (data.size() == 0) {
                    return;
                }

                Log.d("", data.toString());


                int sizeBeforeFetch = tutorList.size();
                int fetch = data.size();
                UiState newUiState = new UiState(sizeBeforeFetch, fetch);
                tutorList.addAll(data);
                originalTutorList.addAll(data);
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

    public String getUserId(){
        return userRepository.getCurrentUser().getUid();
    }

    public void getAverageReview(String uidTutor, Callback<Double> callback){
        reviewRepository.getAverageReview(uidTutor, new Callback<Double>() {
            @Override
            public void onSucces(Double averageReview) {
                callback.onSucces(averageReview);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Impossible to calculate the average review.");
            }
        });
    }

    public void getCorsoDiStudi(String studentUid, Callback<String> callback){
        studentRepository.getCorsoDiStudi(studentUid, new Callback<String>() {
            @Override
            public void onSucces(String corsoId) {
                callback.onSucces(corsoId);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Study Program not found");
            }
        });
    }


    public void getNomeCorso(String idCorso, Callback<String> callback){
        corsoDiStudiRepository.getCorsodiStudiName(idCorso, new Callback<String>() {
            @Override
            public void onSucces(String nomeC) {
                callback.onSucces(nomeC);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Name not found");
            }
        });
    }

    public void getAreaCorso(String idCorso, Callback<String> callback){
        corsoDiStudiRepository.getArea(idCorso, new Callback<String>() {
            @Override
            public void onSucces(String area) {
                callback.onSucces(area);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Study Program area not found");
            }
        });
    }







}
