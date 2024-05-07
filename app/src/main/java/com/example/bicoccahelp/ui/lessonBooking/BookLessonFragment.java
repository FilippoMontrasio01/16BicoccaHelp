package com.example.bicoccahelp.ui.lessonBooking;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentBookLessonBinding;
import com.example.bicoccahelp.data.date.DateRepository;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Objects;

public class BookLessonFragment extends DialogFragment implements View.OnClickListener  {

    private FragmentBookLessonBinding binding;
    private TutorRepository tutorRepository;
    private DateRepository dateRepository;
    private UserRepository userRepository;
    private TutorViewModel tutorViewModel;

    private DateRecycleViewAdapter dateRecycleViewAdapter;
    private DateViewModel dateViewModel;
    private boolean isLoading = false;

    private static final String ARG_TUTOR_NAME = "tutorName";
    private static final String ARG_TUTOR_EMAIL = "tutorEmail";
    private static final String ARG_TUTOR_LOGO_URI = "tutorLogoUri";
    private static final String ARG_TUTOR_UID = "tutorUid";
    private Timestamp newDate;
    private Timestamp today;
    private String tutorName;
    private String tutorEmail;
    private String tutorLogoUri;

    private String lastLoadedTutorUid = null;
    private String lastLoadedTutorUidForScrollListener = null;

    private String tutorUid;
    private boolean isFirstLoad = true;


    String oggi = "oggi";


    public BookLessonFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        userRepository = ServiceLocator.INSTANCE.getUserRepository();
        dateRepository = ServiceLocator.getInstance().getDateRepository();
        tutorViewModel = new ViewModelProvider(requireActivity()).get(TutorViewModel.class);

        if (getArguments() != null) {
            tutorName = getArguments().getString(ARG_TUTOR_NAME);
            tutorEmail = getArguments().getString(ARG_TUTOR_EMAIL);
            tutorLogoUri = getArguments().getString(ARG_TUTOR_LOGO_URI);
            tutorUid = getArguments().getString(ARG_TUTOR_UID);
        }

        dateViewModel = new ViewModelProvider(requireActivity(),
                new DateViewModelFactory(dateRepository)).get(DateViewModel.class);
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
        this.configureRecyclerView();
        this.observeUiState();
        //this.loadFirstPage();
        this.observeErrorMessage(view);

        this.addOnScrollListener();
        binding.lessonCard.selectDayButton.setOnClickListener(this);
        binding.lessonCard.tutorListItemName.setText(tutorName);
        binding.lessonCard.tutorListItemEmail.setText(tutorEmail);
        Glide.with(requireActivity().getApplicationContext())
                .load(GlideLoadModel.get(tutorLogoUri))
                .into(binding.lessonCard.tutorListItemLogo);
        changeTutor(tutorUid);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.lessonCard.selectDayButton.getId()){
            selectDayOnClik();
            return;
        }

        /*if(v.getId() == binding.lessonCard.selectDayButton.getId()){
            backOnClick();
        }*/
    }

    private void selectDayOnClik() {
        openCalendar();
    }

    private void openCalendar() {
        // Ottieni la data corrente
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        today = new Timestamp(c.getTime());

        // Crea un nuovo DatePickerDialog

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth1) {
                Log.d("", "DAY: "+dayOfMonth1 + "MONTH: "+month1);


                Calendar calendar = Calendar.getInstance();
                calendar.set(year1, month1, dayOfMonth1, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // Creare un oggetto Timestamp da un oggetto Date
                newDate = new Timestamp(calendar.getTime());

                int finalMonth = month + 1;
                dateRepository.updateDate(tutorUid, userRepository.getCurrentUser().getUid(), today, newDate, new Callback<Void>() {
                    @Override
                    public void onSucces(Void unused) {
                        binding.lessonCard.selectDayButton.setText(dayOfMonth1 + "/" + finalMonth + "/" + year);
                        updateRecyclerView();
                        Log.d("", "LA DATA È STATA AGGIORANTA CON SUCCESSO: "+ dayOfMonth1 + "/" + finalMonth + "/" + year);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("", "LA DATA NON È STATA AGGIORANTA CON SUCCESSO: "+ dayOfMonth1 + "/" + finalMonth + "/" + year);
                    }
                });

            }
        }, year, month, day);

        today = newDate;

        // Mostra il dialog
        dialog.show();
    }


    private void configureRecyclerView() {
        RecyclerView dateRecycleView = binding.lessonCard.HoourRecyclerView;

        dateRecycleViewAdapter = new DateRecycleViewAdapter(dateViewModel.dateList);
        dateRecycleView.setAdapter(dateRecycleViewAdapter);

        LinearLayoutManager layoutManager = (LinearLayoutManager) dateRecycleView
                .getLayoutManager();
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

    private void observeErrorMessage(View view) {
        dateViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Snackbar.make(view, getString(R.string.db_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFirstPage() {

        if (newDate == null) {
            // Se la data è nulla, impostiamo la data attuale
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            newDate = new Timestamp(calendar.getTime());
        }

        if (!Objects.equals(lastLoadedTutorUidForScrollListener, tutorUid)) {
            // Il tutor è cambiato
            Log.d("", "IL TUTOR E: " + tutorUid);

            // Svuota il RecyclerView
            clearRecyclerView();

            // Resetta la pagina corrente
            dateViewModel.resetCurrentPage();

            // Carica la prima pagina di dati per il nuovo tutor
            dateViewModel.getNextHourPage(tutorUid, userRepository.getCurrentUser().getUid(), newDate);

            // Aggiorna lastLoadedTutorUidForScrollListener
            lastLoadedTutorUidForScrollListener = tutorUid;
        } else if (dateViewModel.getCurrentPage() == 0 && dateViewModel.hasMore()) {
            // Il tutor non è cambiato, quindi carica la prima pagina come al solito
            dateViewModel.getNextHourPage(tutorUid, userRepository.getCurrentUser().getUid(), newDate);
        }
    }
    private void addOnScrollListener() {

        if (newDate == null) {
            // Se la data è nulla, impostiamo la data attuale
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            newDate = new Timestamp(calendar.getTime());
        }

        binding.lessonCard.HoourRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Objects.equals(lastLoadedTutorUidForScrollListener, tutorUid) && !isLoading && dateViewModel.hasMore() && dy > 0) {
                    dateViewModel.getNextHourPage(tutorUid, userRepository.getCurrentUser().getUid(), newDate);
                    lastLoadedTutorUidForScrollListener = tutorUid;
                }
            }
        });
    }

    private void updateRecyclerView() {
        this.configureRecyclerView();
        this.observeUiState();
        this.loadFirstPage();
        this.addOnScrollListener();
    }

    private void clearRecyclerView() {
        // Svuota l'elenco di dati
        dateViewModel.dateList.clear();

        // Notifica all'adapter che i dati sono cambiati
        dateRecycleViewAdapter.notifyDataSetChanged();
    }

    private void changeTutor(String newTutorUid) {

        tutorUid = newTutorUid;
        // Resetta il RecyclerView e carica i dati relativi al nuovo tutor
        dateRecycleViewAdapter.clearData();
    }

    public void onStart() {
        super.onStart();
        loadFirstPage();
    }





}