package com.example.bicoccahelp.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.databinding.FragmentProfileBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class ProfileFragment extends Fragment implements View.OnClickListener
         {

    private FragmentProfileBinding binding;
    private NavController navController;
    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private StudentViewModel studentViewModel;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        studentViewModel = new ViewModelProvider(requireActivity(),
                new StudentViewModelFactory(studentRepository, userRepository))
                .get(StudentViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);


        studentViewModel.getStudentExists().observe(getViewLifecycleOwner(), exist -> {
            if (exist) {
                binding.completeStudentTextView.setText(getString(R.string.update_student));
            } else {
                binding.completeStudentTextView.setText(getString(R.string.complete_student));
            }
        });

        studentViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
        });

        studentViewModel.checkStudentExists();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserModel user = userRepository.getCurrentUser();

        Fragment profilePhotoFragment = new ProfilePhotoFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.profile_profilePhoto, profilePhotoFragment).commit();



        navController = Navigation.findNavController(view);
        binding.signOutItem.setOnClickListener(this);
        binding.deleteProfileItem.setOnClickListener(this);
        binding.updatePasswordItem.setOnClickListener(this);
        binding.updateNameItem.setOnClickListener(this);
        binding.displayEmailTextView.setText(user.getEmail());
        binding.displayNameTextView.setText(user.getName());
        binding.completeStudentItem.setOnClickListener(this);
        binding.becomeATutorItem.setOnClickListener(this);
        binding.reviewItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {



        if(v.getId() == binding.signOutItem.getId()){
            showConfirmSignOutDialog();
            return;
        }

        if(v.getId() == binding.deleteProfileItem.getId()){
            showConfirmDeleteDialog();
            return;
        }

        if(v.getId() == binding.updatePasswordItem.getId()){
            showUpdatePasswordDialog();
            return;
        }

        if(v.getId() == binding.updateNameItem.getId()){
            showUpdateNameDialog();
            return;
        }

        if(v.getId() == binding.completeStudentItem.getId()){
            completeStudentOnClick();
            return;
        }

        if(v.getId() == binding.becomeATutorItem.getId()){
            completeTutorOnclick();
            return;
        }

        if(v.getId() == binding.reviewItem.getId()){
            reviewOnclick();
        }
    }



    private void completeTutorOnclick() {

        String uid = userRepository.getCurrentUser().getUid();

        studentRepository.studentExist(uid, new Callback<Boolean>()  {
            @Override
            public void onSucces(Boolean exist) {
                if(exist){
                    navController.navigate(R.id.action_from_profile_to_complete_tutor_fragment);
                }else{
                    Snackbar.make(requireView(), getString(R.string.add_at_least_a_skill),
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error),
                        Snackbar.LENGTH_SHORT).show();
            }
        });



    }

    private void completeStudentOnClick() {
        navController.navigate(R.id.action_from_profile_to_complete_student_fragment);
    }

    private void showConfirmSignOutDialog() {
        navController.navigate(R.id.action_from_profile_to_confirm_sign_out);
    }

    private void showConfirmDeleteDialog() {
        navController.navigate(R.id.action_from_profile_to_delete_dialog);
    }

    private void showUpdatePasswordDialog() {
        navController.navigate(R.id.action_from_profile_to_update_password_dialog);
    }

    private void showUpdateNameDialog() {
        navController.navigate(R.id.action_from_profile_to_update_name_dialog);
    }

    private void reviewOnclick() {
        navController.navigate(R.id.action_from_profile_to_review_fragment);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}