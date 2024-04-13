package com.example.bicoccahelp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

public class UpdatePasswordDialogFragment extends DialogFragment implements  View.OnClickListener{


    private UserRepository userRepository;
    private AuthRepository authRepository;
    private NavController navController;
    private FragmentUpdatePasswordDialogBinding binding;


    public UpdatePasswordDialogFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        authRepository = ServiceLocator.getInstance().getAuthRepository();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this.getParentFragment());

        binding.updatePasswordButtonConfirm.setOnClickListener(this::onClick);
        binding.updatePasswordButtonCancel.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.updatePasswordButtonConfirm.getId()) {
            this.onClickConfirm(v);
            return;
        }

        if (v.getId() == binding.updatePasswordButtonCancel.getId()) {
            this.onClickCancel(v);
        }
    }

    private void onClickConfirm(View v) {
        String oldPassword = binding.updatePasswordOldEditText.getText().toString();
        String newPassword = binding.updatePasswordNewEditText.getText().toString();

        if(oldPassword.isEmpty()){
            binding.updatePasswordOldTextInputLayout.setError("INSERISCI LA VECCHIA PASSWORD");
            return;
        }

        if(newPassword.equals(oldPassword)){
            binding.updatePasswordNewTextInputLayout.setError("LA PASSWORD DEVE ESSERE DIVERSA" +
                    "DA QUELLA VECCHIA");
            return;
        }

        if(!InputValidator.isValidPassword(newPassword)){
            binding.updatePasswordNewTextInputLayout.setError("INSERISCI UNA PASSWORD VALIDA");
            return;
        }

        reAuthAndUpdatePsw(oldPassword, newPassword);
    }

    private void onClickCancel(View v) {
        getDialog().cancel();
    }


    public void reAuthAndUpdatePsw(String oldPassword, String newPassword){
        userRepository.reauthenticate(oldPassword, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                updatePsw(newPassword);
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(), "L'UTENTE NON SI È RIAUTENTICATO CORRETTAMENTE", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePsw(String newPassword){
        userRepository.updatePassword(newPassword, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                authRepository.logout();
                navController.navigate(R.id.action_from_update_password_dialog_to_welcome_Activity);
                requireActivity().finish();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(), "LA PASSWORD NON È STATA AGGIORNATA", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }


}