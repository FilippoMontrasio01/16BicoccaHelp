package com.example.bicoccahelp.ui.lessonBooking;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentBookLessonBinding;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.ServiceLocator;

import java.util.Calendar;

public class BookLessonFragment extends DialogFragment implements View.OnClickListener  {

    private FragmentBookLessonBinding binding;
    private TutorRepository tutorRepository;
    private UserRepository userRepository;
    private TutorViewModel tutorViewModel;

    private static final String ARG_TUTOR_NAME = "tutorName";
    private static final String ARG_TUTOR_EMAIL = "tutorEmail";
    private static final String ARG_TUTOR_LOGO_URI = "tutorLogoUri";
    private String tutorName;
    private String tutorEmail;
    private String tutorLogoUri;


    public BookLessonFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        userRepository = ServiceLocator.INSTANCE.getUserRepository();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_BicoccaHelp_Light_FullscreenDialog);
        tutorViewModel = new ViewModelProvider(requireActivity()).get(TutorViewModel.class);

        if (getArguments() != null) {
            tutorName = getArguments().getString(ARG_TUTOR_NAME);
            tutorEmail = getArguments().getString(ARG_TUTOR_EMAIL);
            tutorLogoUri = getArguments().getString(ARG_TUTOR_LOGO_URI);
        }
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
            }
        }, year, month, day);

        // Mostra il dialog
        dialog.show();
    }
}