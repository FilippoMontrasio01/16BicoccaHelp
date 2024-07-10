package com.example.bicoccahelp.ui.lessonBooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.date.DateRepository;
import com.example.bicoccahelp.data.lesson.CreateLessonRequest;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.auth.User;


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
    private MutableLiveData<String> tutorId;
    public final ArrayList<String> dateList;
    public final DateRepository dateRepository;
    public final LessonRepository lessonRepository;
    public final TutorRepository tutorRepository;
    public final UserRepository userRepository;
    private MutableLiveData<Boolean> lessonCreate;

    private final Long limit = 80L;
    private boolean hasMore = true;
    private int currentPage = 0;
    private MutableLiveData<Boolean> oraUpdated;

    public DateViewModel(DateRepository dateRepository, LessonRepository lessonRepository,
                         UserRepository userRepository, TutorRepository tutorRepository) {
        this.uiState = new MutableLiveData<>(new UiState(0, 0));;
        this.errorMessage = new MutableLiveData<>(null);;
        this.dateRepository = dateRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.dateList = new ArrayList<>();
        this.lessonCreate = new MutableLiveData<>();
        this.tutorId = new MutableLiveData<>();
        this.oraUpdated = new MutableLiveData<>();
    }


    public MutableLiveData<Boolean> getOraUpdated() {
        return oraUpdated;
    }

    public void setOraUpdated(MutableLiveData<Boolean> oraUpdated) {
        this.oraUpdated = oraUpdated;
    }

    public LiveData<Boolean> getLessonCreate() {
        return Transformations.map(lessonCreate, input -> {
            if (input) {
                lessonCreate.setValue(false); // Reset lessonCreate to false after observing true
            }
            return input;
        });
    }

    public LiveData<String> getTutorId(){
        return tutorId;
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

    public void createLessonWithTutorName(String tutorName, String uidStudent, Timestamp selectedDate, String selectedOrario, String description) {
        tutorRepository.getTutorUid(tutorName, new Callback<String>() {
            @Override
            public void onSucces(String uidTutor) {
                CreateLessonRequest request = new CreateLessonRequest(uidStudent, uidTutor, selectedDate, selectedOrario, description);
                createLessonAndUpdateOrario(request);
            }

            @Override
            public void onFailure(Exception e) {
                // Gestisci l'errore qui
            }
        });
    }
    public String getStudentId(){
        return userRepository.getCurrentUser().getUid();
    }
    public void createLessonAndUpdateOrario(CreateLessonRequest request){
        lessonRepository.createLesson(request, new Callback<LessonModel>() {
            @Override
            public void onSucces(LessonModel lessonModel) {
                lessonCreate.setValue(true);
                updateOrario(lessonModel.getUid_tutor(), lessonModel.getData(), lessonModel.getOra());
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("La lezione non è stata confermata: " + e.getMessage());
            }
        });
    }

    public void updateOrario(String uidTutor, Timestamp date, String orario) {
        dateRepository.updateOrario(uidTutor, date, orario, new Callback<Void>() {
            @Override
            public void onSucces(Void result) {
                oraUpdated.setValue(true);  // Imposta oraUpdated su true dopo il successo
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue("Impossibile aggiornare l'orario: " + e.getMessage());
            }
        });
    }
}

