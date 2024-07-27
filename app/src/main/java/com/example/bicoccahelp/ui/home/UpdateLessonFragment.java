package com.example.bicoccahelp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.databinding.FragmentUpdateLessonBinding;
import com.example.bicoccahelp.utils.GlideLoadModel;


public class UpdateLessonFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_TUTOR_NAME = "tutorName";
    private static final String ARG_TUTOR_EMAIL = "tutorEmail";
    private static final String ARG_TUTOR_LOGO_URI = "tutorLogoUri";
    private static final String ARG_TUTOR_UID = "tutorUid";

    private FragmentUpdateLessonBinding binding;
    private NavController navController;

    private String tutorName;
    private String tutorEmail;
    private String tutorLogoUri;
    private String tutorUid;


    public UpdateLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tutorName = getArguments().getString(ARG_TUTOR_NAME);
            tutorEmail = getArguments().getString(ARG_TUTOR_EMAIL);
            tutorLogoUri = getArguments().getString(ARG_TUTOR_LOGO_URI);
            tutorUid = getArguments().getString(ARG_TUTOR_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUpdateLessonBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        binding.updateLessonCard.selectDayButton.setOnClickListener(this);
        binding.updateLessonCard.bookLessonButton.setOnClickListener(this);
        binding.updateLessonCard.tutorListItemName.setText(tutorName);
        binding.updateLessonCard.tutorListItemEmail.setText(tutorEmail);
        Glide.with(requireActivity().getApplicationContext())
                .load(GlideLoadModel.get(tutorLogoUri))
                .into(binding.updateLessonCard.tutorListItemLogo);
    }

    @Override
    public void onClick(View v) {

    }
}