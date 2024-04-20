package com.example.bicoccahelp.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.OnUpdateListener;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentProfileBinding;
import com.example.bicoccahelp.utils.ServiceLocator;


public class ProfileFragment extends Fragment implements View.OnClickListener
         {

    private FragmentProfileBinding binding;
    private NavController navController;
    private UserRepository userRepository;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();




    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserModel user = userRepository.getCurrentUser();

        Fragment profilePhotoFragment = new ProfilePhotoFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.profile_profilePhoto, profilePhotoFragment).commit();



        navController = NavHostFragment.findNavController(this);
        binding.signOutItem.setOnClickListener(this);
        binding.deleteProfileItem.setOnClickListener(this);
        binding.updatePasswordItem.setOnClickListener(this);
        binding.updateNameItem.setOnClickListener(this);
        binding.displayEmailTextView.setText(user.email);
        binding.displayNameTextView.setText(user.name);
        binding.completeStudentItem.setOnClickListener(this);
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
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}