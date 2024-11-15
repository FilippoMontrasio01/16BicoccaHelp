package com.example.bicoccahelp.ui.lessonBooking;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.date.CreateDateRequest;
import com.example.bicoccahelp.data.date.DateModel;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentBookLessonBinding;
import com.example.bicoccahelp.data.date.DateRepository;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BookLessonFragment extends DialogFragment implements View.OnClickListener {

    private FragmentBookLessonBinding binding;
    private TutorRepository tutorRepository;
    private DateRepository dateRepository;
    private UserRepository userRepository;
    private LessonRepository lessonRepository;
    private StudentRepository studentRepository;
    private TutorViewModel tutorViewModel;

    private NavController navController;

    private DateRecycleViewAdapter dateRecycleViewAdapter;
    private DateViewModel dateViewModel;
    private boolean isLoading = false;

    private static final String ARG_TUTOR_NAME = "tutorName";
    private static final String ARG_TUTOR_EMAIL = "tutorEmail";
    private static final String ARG_TUTOR_LOGO_URI = "tutorLogoUri";
    private static final String ARG_TUTOR_UID = "tutorUid";
    private static final String ARG_LESSON_ID = "lessonId";
    private static final String ARG_LESSON_DATE = "lessonDate";

    private static final String ARG_LESSON_HOUR = "lessonHour";
    private String tutorName;
    private String tutorEmail;
    private String tutorLogoUri;
    private String tutorUid;
    private String lessonId;

    private Timestamp lessonDate;
    private String lessonHour;

    private Timestamp selectedDate;
    private boolean isDateSelected = false;

    private String lastLoadedTutorUid = null;
    private String lastLoadedTutorUidForScrollListener = null;

    public BookLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        dateRepository = ServiceLocator.getInstance().getDateRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();


        TutorViewModelFactory factory = new TutorViewModelFactory(tutorRepository);
        tutorViewModel = new ViewModelProvider(requireActivity(), factory).get(TutorViewModel.class);
        lessonRepository = ServiceLocator.getInstance().getLessonRepository();

        if (getArguments() != null) {
            tutorName = getArguments().getString(ARG_TUTOR_NAME);
            tutorEmail = getArguments().getString(ARG_TUTOR_EMAIL);
            tutorLogoUri = getArguments().getString(ARG_TUTOR_LOGO_URI);
            tutorUid = getArguments().getString(ARG_TUTOR_UID);
            lessonId = getArguments().getString(ARG_LESSON_ID);
            lessonDate = (Timestamp) getArguments().get(ARG_LESSON_DATE);
            lessonHour = getArguments().getString(ARG_LESSON_HOUR);
        }

        dateViewModel = new ViewModelProvider(requireActivity(),
                new DateViewModelFactory(dateRepository, lessonRepository, userRepository,
                        tutorRepository, studentRepository)).get(DateViewModel.class);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookLessonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        binding.lessonCard.selectDayButton.setOnClickListener(this);
        binding.lessonCard.bookLessonButton.setOnClickListener(this);
        binding.lessonCard.deleteLessonButton.setOnClickListener(this);
        binding.lessonCard.UpdateLessonButton.setOnClickListener(this);
        binding.lessonCard.tutorListItemName.setText(tutorName);
        binding.lessonCard.tutorListItemEmail.setText(tutorEmail);
        Glide.with(requireActivity().getApplicationContext())
                .load(GlideLoadModel.get(tutorLogoUri))
                .into(binding.lessonCard.tutorListItemLogo);
        changeTutor(tutorUid);

        BookLessonFragmentArgs args = BookLessonFragmentArgs.fromBundle(getArguments());
        String navigationSource = args.getNavigationSource();

        dateViewModel.checkStudentExist(dateViewModel.getStudentId());

        if ("desiredSource".equals(navigationSource)) {
            binding.lessonCard.UpdateClassButtonLinearLayout.setVisibility(View.VISIBLE);
            binding.lessonCard.BookClassButtonLinearLayout.setVisibility(View.GONE);
        } else {
            binding.lessonCard.UpdateClassButtonLinearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.lessonCard.selectDayButton.getId()) {
            openCalendar();

        } else if (v.getId() == binding.lessonCard.bookLessonButton.getId()) {
            bookLesson();
        }else if (v.getId() == binding.lessonCard.deleteLessonButton.getId()){
            deleteLesson(lessonId);
        }else if(v.getId() == binding.lessonCard.UpdateLessonButton.getId()){
            updateLesson(lessonId);
        }
    }



    private void openCalendar() {
        // Ottieni la data corrente
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Crea un nuovo DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                R.style.MyDatePickerDialogTheme, (view, year1, month1, dayOfMonth1) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year1, month1, dayOfMonth1, 0, 0, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    // Creare un oggetto Timestamp da un oggetto Date
                    selectedDate = new Timestamp(calendar.getTime());
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);



                    Log.d("DayOfWeek", "Giorno della settimana selezionato: " + dayOfWeek);

                    // Verifica se è stata già selezionata una data
                    if (!isDateSelected || !Objects.equals(binding.lessonCard.selectDayButton.getText()
                            .toString(), InputValidator.formatDate(selectedDate))) {
                        binding.lessonCard.selectDayButton.
                                setText(InputValidator.formatDate(selectedDate));


                        insertOrUpdateDate(selectedDate);
                    }
                }, year, month, day);

        Calendar minDate = Calendar.getInstance();
        dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        dialog.show();
    }

    private void insertOrUpdateDate(Timestamp selectedDate) {
        // Verifica se è già presente un'istanza nel database per questa data e tutorUid
        dateRepository.listOrari(tutorUid, selectedDate, 1L, new Callback<List<String>>() {
            @Override
            public void onSucces(List<String> strings) {
                if (strings.isEmpty()) {
                    // Non c'è ancora un'istanza nel database, creala
                    createDateInstance(selectedDate);
                } else {
                    // Già presente nel database, aggiorna solo se la data è cambiata
                    updateDateInstance(selectedDate);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Gestione dell'errore
                Log.e("BookLessonFragment", "Errore nel recuperare i dati dal repository: " + e.getMessage());
            }
        });
    }

    private void createDateInstance(Timestamp selectedDate) {
        dateRepository.createDate(new CreateDateRequest(new HashMap<>(), selectedDate, tutorUid),
                new Callback<DateModel>() {
            @Override
            public void onSucces(DateModel dateModel) {
                isDateSelected = true;
                binding.lessonCard.selectDayButton.setText(InputValidator.formatDate(selectedDate));
                // Aggiorna subito gli orari disponibili dopo aver creato la data nel database
                loadAvailableTimes(selectedDate);
            }

            @Override
            public void onFailure(Exception e) {
                // Gestione dell'errore nella creazione dell'istanza nel database
                Log.e("BookLessonFragment", "Errore nella creazione dell'istanza nel database: " + e.getMessage());
            }
        });
    }

    private void updateDateInstance(Timestamp selectedDate) {
        dateRepository.updateDate(tutorUid, selectedDate, selectedDate, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                isDateSelected = true;
                binding.lessonCard.selectDayButton.setText(InputValidator.formatDate(selectedDate));
                // Aggiorna subito gli orari disponibili dopo aver aggiornato la data nel database
                loadAvailableTimes(selectedDate);
            }

            @Override
            public void onFailure(Exception e) {
                // Gestione dell'errore nell'aggiornamento dell'istanza nel database
                Log.e("BookLessonFragment", "Errore nell'aggiornamento dell'istanza nel database: " + e.getMessage());
            }
        });
    }


    private void loadAvailableTimes(Timestamp selectedDate) {
        isLoading = true; // Imposta lo stato di caricamento
        configureRecyclerView();
        observeUiState();
        loadFirstPage(selectedDate);
        addOnScrollListener();
    }

    private void configureRecyclerView() {
        RecyclerView dateRecycleView = binding.lessonCard.HourRecyclerView;

        dateRecycleViewAdapter = new DateRecycleViewAdapter(dateViewModel.dateList, dateRecycleView);
        dateRecycleView.setAdapter(dateRecycleViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        dateRecycleView.setLayoutManager(layoutManager);  // LinearLayoutManager ORIZZONTALE

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                dateRecycleView.getContext(), layoutManager.getOrientation());
        dateRecycleView.addItemDecoration(dividerItemDecoration);
    }

    private void observeUiState() {
        dateViewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            if (uiState.fetched == 0) {
                return;
            }
            dateRecycleViewAdapter.notifyItemRangeInserted(uiState.sizeBeforeFetch,
                    uiState.fetched);
        });
    }

    private void loadFirstPage(Timestamp selectedDate) {
        clearRecyclerView();
        dateViewModel.resetCurrentPage();
        dateViewModel.getNextHourPage(tutorUid, selectedDate);
        lastLoadedTutorUidForScrollListener = tutorUid;
    }

    private void addOnScrollListener() {
        binding.lessonCard.HourRecyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Objects.equals(lastLoadedTutorUidForScrollListener, tutorUid) && !isLoading
                        && dateViewModel.hasMore() && dy > 0) {

                    dateViewModel.getNextHourPage(tutorUid, selectedDate);
                    lastLoadedTutorUidForScrollListener = tutorUid;
                }
            }
        });
    }

    private void clearRecyclerView() {
        if (dateViewModel != null) {
            dateViewModel.dateList.clear();
        }
        if (dateRecycleViewAdapter != null) {
            dateRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    private void changeTutor(String newTutorUid) {
        if (!Objects.equals(this.tutorUid, newTutorUid)) {
            this.tutorUid = newTutorUid;
            clearRecyclerView();
            isDateSelected = false;
        }
    }

    public void onStart() {
        super.onStart();
        if (isDateSelected) {
            loadFirstPage(selectedDate);
        }
    }

    private void bookLesson() {

        if (!isDateSelected || dateRecycleViewAdapter.getSelectedOrario() == null) {
            // Mostra la Snackbar se la data o l'orario non sono stati selezionati
            Snackbar.make(requireView(), getString(R.string.select_day_and_time), Snackbar.LENGTH_SHORT).show();
            return;
        }

        String uidStudent = dateViewModel.getStudentId();
        String description = Objects.
                requireNonNull(binding.lessonCard.textInputEditTextDescription.getText()).toString();

        String selectedOrario = dateRecycleViewAdapter.getSelectedToggleButtonText();


        dateViewModel.getStudentExist().observe(getViewLifecycleOwner(), StudentExist -> {
            if(StudentExist){
                dateViewModel.bookLesson(uidStudent, selectedDate, selectedOrario, tutorName, description);
            }else {
                Snackbar.make(requireView(), "Complete Student Profile before booking a lesson", Snackbar.LENGTH_SHORT).show();
            }
        });



        dateViewModel.getLessonBookingAllowed().observe(getViewLifecycleOwner(), bookingAllowed -> {
            if (bookingAllowed) {
                dateViewModel.getLessonCreate().observe(getViewLifecycleOwner(), lessonCreated -> {
                    if (lessonCreated) {
                        dateViewModel.updateOrario(tutorUid, selectedDate, selectedOrario);
                        dateViewModel.resetLessonCreate();
                    }
                });

                dateViewModel.getOraUpdated().observe(getViewLifecycleOwner(), oraUpdated -> {
                    if (oraUpdated) {
                        clearRecyclerView();
                        navController.navigate(R.id.action_from_book_dialog_to_home);
                        dateViewModel.setOra();
                    }
                });
            } else {
                dateViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
                    Snackbar.make(requireActivity().findViewById(R.id.fragment_tutor),
                            errorMessage, Snackbar.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void deleteLesson(String lessonId){

        int checkDate = InputValidator.calculateDaysDifference(lessonDate);

        if(!lessonId.isEmpty() && !lessonHour.isEmpty() && lessonDate != null){

            if(checkDate > 1){
                dateViewModel.deleteLesson(lessonId, lessonDate, lessonHour, tutorUid );
                navController.navigate(R.id.action_from_book_dialog_to_home);
            }else{
                Snackbar.make(getView(), "Impossible to delete your lesson. It is scheduled for the next day.", Snackbar.LENGTH_SHORT).show();
            }

        }else{
            Snackbar.make(getView(), "Impossible to delete your lesson", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void updateLesson(String lessonId){

        int checkDate = InputValidator.calculateDaysDifference(lessonDate);

        if(!lessonId.isEmpty() && !lessonHour.isEmpty() && lessonDate != null){
            if(checkDate > 1){
                dateViewModel.deleteLesson(lessonId, lessonDate, lessonHour, tutorUid );
                bookLesson();
            }else{
                Snackbar.make(getView(), "Impossible to replan your lesson. It is scheduled for the next day.", Snackbar.LENGTH_SHORT).show();
            }

        }else{
            Snackbar.make(getView(), "Impossible to update your lesson", Snackbar.LENGTH_SHORT).show();
        }
    }
}
