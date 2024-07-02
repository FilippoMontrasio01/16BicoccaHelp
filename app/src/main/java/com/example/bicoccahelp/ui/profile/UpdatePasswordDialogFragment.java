package com.example.bicoccahelp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentUpdatePasswordDialogBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class UpdatePasswordDialogFragment extends DialogFragment implements  View.OnClickListener{


    private UserRepository userRepository;
    private AuthRepository authRepository;
    private NavController navController;
    private FragmentUpdatePasswordDialogBinding binding;
    private ProfileViewModel profileViewModel;


    public UpdatePasswordDialogFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        authRepository = ServiceLocator.getInstance().getAuthRepository();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);

        ProfileViewModelFactory factory = new ProfileViewModelFactory(authRepository, userRepository);

        profileViewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);

        profileViewModel.getPasswordUpdated().observe(this, passwordUpdated -> {
            if(passwordUpdated){
                navController.navigate(R.id.action_from_update_password_dialog_to_welcome_Activity);
                requireActivity().finish();
            }else{
                Snackbar.make(requireView(), getString(R.string.password_update_error),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordDialogBinding
                .inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this.getParentFragment());

        binding.updatePasswordButtonConfirm.setOnClickListener(this);
        binding.updatePasswordButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.updatePasswordButtonConfirm.getId()) {
            this.onClickConfirm();
            return;
        }

        if (v.getId() == binding.updatePasswordButtonCancel.getId()) {
            this.onClickCancel();
        }
    }

    private void onClickConfirm() {
        String oldPassword = Objects.requireNonNull(binding.updatePasswordOldEditText.getText())
                .toString();
        String newPassword = Objects.requireNonNull(binding.updatePasswordNewEditText.getText())
                .toString();

        if(oldPassword.isEmpty()){
            binding.updatePasswordOldTextInputLayout.setError(getString(R.string.old_password));
            return;
        }

        if(newPassword.equals(oldPassword)){
            binding.updatePasswordNewTextInputLayout.setError(
                    getString(R.string.different_password));
            return;
        }

        if(!InputValidator.isValidPassword(newPassword)){
            binding.updatePasswordNewTextInputLayout.setError(getString(R.string.invalid_password));
            return;
        }

        profileViewModel.updatePassword(oldPassword, newPassword);
    }

    private void onClickCancel() {
        Objects.requireNonNull(getDialog()).cancel();
    }

    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }


}