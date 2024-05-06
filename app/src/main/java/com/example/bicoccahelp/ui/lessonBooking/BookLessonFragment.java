package com.example.bicoccahelp.ui.lessonBooking;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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
import com.example.bicoccahelp.date.CreateDateRequest;
import com.example.bicoccahelp.date.DateRepository;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Calendar;

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

    private Timestamp giorno;
    private String tutorName;
    private String tutorEmail;
    private String tutorLogoUri;
    private String tutorUid;

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
        this.loadFirstPage();
        this.addOnScrollListener();




        binding.lessonCard.selectDayButton.setOnClickListener(this);
        binding.lessonCard.tutorListItemName.setText(tutorName);
        binding.lessonCard.tutorListItemEmail.setText(tutorEmail);
        Glide.with(requireActivity().getApplicationContext())
                .load(GlideLoadModel.get(tutorLogoUri))
                .into(binding.lessonCard.tutorListItemLogo);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.lessonCard.selectDayButton.getId()){
            selectDayOnClik();
        }
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

        // Crea un nuovo DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("", "DAY: "+dayOfMonth + "MONTH: "+month);


                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // Creare un oggetto Timestamp da un oggetto Date
                giorno = new Timestamp(calendar.getTime());


                int finalMonth = month + 1;
                binding.lessonCard.selectDayButton.setText(dayOfMonth + "/" + finalMonth + "/" + year);



            }
        }, year, month, day);


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

        if (giorno!= null && dateViewModel.getCurrentPage() == 0 && dateViewModel.hasMore()) {
            dateViewModel.getNextHourPage(tutorUid, giorno);
        }
    }
    private void addOnScrollListener() {


        binding.lessonCard.HoourRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (giorno!= null && !isLoading && dateViewModel.hasMore() && dy > 0) {
                    dateViewModel.getNextHourPage(tutorUid, giorno);
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
}