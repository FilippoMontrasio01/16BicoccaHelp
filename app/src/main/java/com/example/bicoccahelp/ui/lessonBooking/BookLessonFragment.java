package com.example.bicoccahelp.ui.lessonBooking;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.bicoccahelp.data.lesson.CreateLessonRequest;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.example.bicoccahelp.data.user.UserRepository;
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
    private TutorViewModel tutorViewModel;

    private NavController navController;

    private DateRecycleViewAdapter dateRecycleViewAdapter;
    private DateViewModel dateViewModel;
    private boolean isLoading = false;

    private static final String ARG_TUTOR_NAME = "tutorName";
    private static final String ARG_TUTOR_EMAIL = "tutorEmail";
    private static final String ARG_TUTOR_LOGO_URI = "tutorLogoUri";
    private static final String ARG_TUTOR_UID = "tutorUid";

    private String tutorName;
    private String tutorEmail;
    private String tutorLogoUri;
    private String tutorUid;

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
        userRepository = ServiceLocator.INSTANCE.getUserRepository();
        dateRepository = ServiceLocator.getInstance().getDateRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        tutorViewModel = new ViewModelProvider(requireActivity()).get(TutorViewModel.class);
        lessonRepository = ServiceLocator.getInstance().getLessonRepository();

        if (getArguments() != null) {
            tutorName = getArguments().getString(ARG_TUTOR_NAME);
            tutorEmail = getArguments().getString(ARG_TUTOR_EMAIL);
            tutorLogoUri = getArguments().getString(ARG_TUTOR_LOGO_URI);
            tutorUid = getArguments().getString(ARG_TUTOR_UID);
        }

        dateViewModel = new ViewModelProvider(requireActivity(),
                new DateViewModelFactory(dateRepository, lessonRepository, userRepository,
                        tutorRepository)).get(DateViewModel.class);
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
        binding.lessonCard.tutorListItemName.setText(tutorName);
        binding.lessonCard.tutorListItemEmail.setText(tutorEmail);
        Glide.with(requireActivity().getApplicationContext())
                .load(GlideLoadModel.get(tutorLogoUri))
                .into(binding.lessonCard.tutorListItemLogo);
        changeTutor(tutorUid);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.lessonCard.selectDayButton.getId()) {
            openCalendar();
        } else if (v.getId() == binding.lessonCard.bookLessonButton.getId()) {
            bookLesson();
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
                R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth1) {
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
            }
        }, year, month, day);

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
        String uidStudent = dateViewModel.getStudentId();
        String description = Objects.
                requireNonNull(binding.lessonCard.textInputEditTextDescription.getText()).toString();

        String selectedOrario = dateRecycleViewAdapter.getSelectedToggleButtonText();

        dateViewModel.bookLesson(uidStudent, selectedDate, selectedOrario, tutorName, description);

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
                        dateViewModel.resetOraUpdated();
                    }
                });
            } else {
                // Avvisa l'utente che non può prenotare per qualche motivo
                dateViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
                    Snackbar.make(requireActivity().findViewById(R.id.fragment_tutor),
                            errorMessage, Snackbar.LENGTH_SHORT).show();
                });
            }
        });
    }
}
