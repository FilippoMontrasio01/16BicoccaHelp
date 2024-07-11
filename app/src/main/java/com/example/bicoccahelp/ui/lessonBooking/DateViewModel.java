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
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.auth.User;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

    private final MutableLiveData<UiState> uiState;
    private final MutableLiveData<String> errorMessage;
    private MutableLiveData<String> tutorId;
    public final ArrayList<String> dateList;
    public final DateRepository dateRepository;
    public final LessonRepository lessonRepository;
    public final TutorRepository tutorRepository;
    public final UserRepository userRepository;
    private MutableLiveData<Boolean> lessonCreate;
    private final MutableLiveData<Boolean> lessonBookingAllowed;

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
        this.lessonBookingAllowed = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLessonBookingAllowed() {
        return lessonBookingAllowed;
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

    public void createLessonWithTutorName(String tutorName, String uidStudent,
                                          Timestamp selectedDate, String selectedOrario,
                                          String description) {
        tutorRepository.getTutorUid(tutorName, new Callback<String>() {
            @Override
            public void onSucces(String uidTutor) {
                CreateLessonRequest request = new CreateLessonRequest(uidStudent, uidTutor,
                        selectedDate, selectedOrario, description);
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
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("The lesson has not been confirmed " + e.getMessage());
            }
        });
    }

    public void resetLessonCreate() {
        lessonCreate.setValue(false);
    }

    public void resetOraUpdated() {
        oraUpdated.setValue(false);
    }


    public void updateOrario(String uidTutor, Timestamp date, String orario) {
        dateRepository.updateOrario(uidTutor, date, orario, new Callback<Void>() {
            @Override
            public void onSucces(Void result) {
                oraUpdated.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue("Unable to update the schedule: " + e.getMessage());
            }
        });
    }

    private void countDay(String uidStudent, Timestamp day, Callback<Integer> callback) {
        lessonRepository.countLesson(uidStudent, day, new Callback<Integer>() {
            @Override
            public void onSucces(Integer count) {
                callback.onSucces(count);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void checkHour(String uidStudent, Timestamp day, String hour, Callback<Boolean> callback) {
        lessonRepository.checkHourPerDay(uidStudent, day, hour, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exists) {
                callback.onSucces(exists);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void bookLesson(String uidStudent, Timestamp selectedDate, String selectedOrario,
                           String tutorName, String description) {

        String selectedDayOfWeek = getDayOfWeekString(selectedDate);

        tutorRepository.getTutorUid(tutorName, new Callback<String>() {
            @Override
            public void onSucces(String tutorId) {
                handleTutorIdRetrieved(tutorId, uidStudent, selectedDate, selectedDayOfWeek, selectedOrario, description);
            }

            @Override
            public void onFailure(Exception e) {
                handleError(e.getMessage());
            }
        });
    }

    private void handleTutorIdRetrieved(String tutorId, String uidStudent, Timestamp selectedDate,
                                        String selectedDayOfWeek, String selectedOrario, String description) {
        tutorRepository.getTutorModelById(tutorId, new Callback<TutorModel>() {
            @Override
            public void onSucces(TutorModel tutorModel) {
                handleTutorModelRetrieved(tutorModel, uidStudent, selectedDate, selectedDayOfWeek, selectedOrario, description);
            }

            @Override
            public void onFailure(Exception e) {
                handleError(e.getMessage());
            }
        });
    }

    private void handleTutorModelRetrieved(TutorModel tutorModel, String uidStudent, Timestamp selectedDate,
                                           String selectedDayOfWeek, String selectedOrario, String description) {
        if (tutorModel != null) {
            Boolean tutorAvailable = tutorModel.getDisponibilitaGiorni().get(selectedDayOfWeek);
            if (tutorAvailable != null && tutorAvailable) {
                countDay(uidStudent, selectedDate, new Callback<Integer>() {
                    @Override
                    public void onSucces(Integer count) {
                        handleCountDayResult(count, uidStudent, selectedDate, selectedOrario, description, tutorModel);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handleError(e.getMessage());
                    }
                });
            } else {
                handleError("The tutor is not available on the selected day.");
            }
        } else {
            handleError("Unable to verify the tutor's availability.");
        }
    }

    private void handleCountDayResult(Integer count, String uidStudent, Timestamp selectedDate,
                                      String selectedOrario, String description, TutorModel tutorModel) {
        if (count >= 3) {
            handleError("You have reached the daily lesson limit.");
        } else {
            checkHour(uidStudent, selectedDate, selectedOrario, new Callback<Boolean>() {
                @Override
                public void onSucces(Boolean lessonExists) {
                    handleCheckHourResult(lessonExists, uidStudent, selectedDate, selectedOrario, description, tutorModel);
                }

                @Override
                public void onFailure(Exception e) {
                    handleError(e.getMessage());
                }
            });
        }
    }

    private void handleCheckHourResult(Boolean lessonExists, String uidStudent, Timestamp selectedDate,
                                       String selectedOrario, String description, TutorModel tutorModel) {
        if (lessonExists) {
            handleError("You already have a booking for the selected day and time.");
        } else {
            createLessonWithTutorName(tutorModel.getName(), uidStudent, selectedDate, selectedOrario, description);
            lessonBookingAllowed.setValue(true);
        }
    }

    private void handleError(String message) {
        errorMessage.setValue(message);
        lessonBookingAllowed.setValue(false);
    }

    public String getDayOfWeekString(Timestamp selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate.getSeconds() * 1000);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        Map<Integer, String> daysOfWeek = new HashMap<>();
        daysOfWeek.put(Calendar.SUNDAY, "Sunday");
        daysOfWeek.put(Calendar.MONDAY, "Monday");
        daysOfWeek.put(Calendar.TUESDAY, "Tuesday");
        daysOfWeek.put(Calendar.WEDNESDAY, "Wednesday");
        daysOfWeek.put(Calendar.THURSDAY, "Thursday");
        daysOfWeek.put(Calendar.FRIDAY, "Friday");
        daysOfWeek.put(Calendar.SATURDAY,"Saturday");


        return daysOfWeek.get(dayOfWeek);
    }
}

