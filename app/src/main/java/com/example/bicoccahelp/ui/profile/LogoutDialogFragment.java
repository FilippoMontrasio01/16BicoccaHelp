package com.example.bicoccahelp.ui.profile;

import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.CreateStudentRequest;
import com.example.bicoccahelp.data.user.student.StudentModel;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.CreateTutorRequest;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentLogoutDialogBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;


public class LogoutDialogFragment extends DialogFragment implements  View.OnClickListener{
    private AuthRepository authRepository;
    private FragmentLogoutDialogBinding binding;
    private NavController navController;
    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private TutorRepository tutorRepository;

    public LogoutDialogFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = ServiceLocator.getInstance().getAuthRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogoutDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this.getParentFragment());
        binding.signOutButtonConfirm.setOnClickListener(this);
        binding.signOutButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.signOutButtonConfirm.getId()){
            this.onClickConfirm(v);
            return;
        }

        if(v.getId() == binding.signOutButtonCancel.getId()){
            this.onClickCancel(v);
        }
    }

    private void onClickCancel(View v) {
        getDialog().cancel();
    }

    private void onClickConfirm(View v) {

        Map<String, Boolean> disponibilities = new HashMap<>();
        disponibilities.put("lunedì", true);
        disponibilities.put("martedì", false);
        disponibilities.put("mercoledi", true);
        disponibilities.put("giovedi", false);
        disponibilities.put("venerdi", true);

        UserModel user = userRepository.getCurrentUser();
        CreateTutorRequest request = new CreateTutorRequest("Informatica", disponibilities);

        tutorRepository.createTutor(request, new Callback<TutorModel>() {
            @Override
            public void onSucces(TutorModel tutorModel) {
                dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(v, "ERRORE", Snackbar.LENGTH_SHORT).show();
            }
        });



       //UserModel user = userRepository.getCurrentUser();
       CreateStudentRequest srequest = new CreateStudentRequest("informatica", true);

        studentRepository.createStudent(srequest, new Callback<StudentModel>() {
            @Override
            public void onSucces(StudentModel studentModel) {
                dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(v, "ERRORE", Snackbar.LENGTH_SHORT).show();
            }
        });


       // authRepository.logout();
       // navController.navigate(R.id.action_from_SignOut_to_welcome_activity);
       // requireActivity().finish();
    }
}