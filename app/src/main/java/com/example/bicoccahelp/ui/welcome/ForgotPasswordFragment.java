package com.example.bicoccahelp.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.databinding.FragmentForgotPasswordBinding;
import com.example.bicoccahelp.databinding.FragmentRegistrationBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class ForgotPasswordFragment extends Fragment implements View.OnClickListener,
View.OnFocusChangeListener{


    private AuthRepository authRepository;
    private FragmentForgotPasswordBinding binding;
    private NavController navController;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = ServiceLocator.getInstance().getAuthRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.sendEmailButton.setOnClickListener(this);
        binding.forgotPasswordEditText.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.sendEmailButton.getId()){
            this.onSenEmailClick();
        }
    }

    private void onSenEmailClick() {
        String email = binding.forgotPasswordEditText.getText().toString();

        if(email.length() == 0){
            return;
        }

        authRepository.forgotPassword(email, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                navController.navigate(R.id.action_from_forgot_password_to_login);
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(), "mail non inviata", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            return;
        }

        if(v.getId() == binding.forgotPasswordEditText.getId()){
            this.validateEmail();
        }
    }

    private boolean validateEmail() {
        String email = binding.forgotPasswordEditText.getText().toString();

        if(!InputValidator.isValidEmail(email)) {
            binding.forgotPasswordTextInputLayout.setError("Email non valida");
            return false;
        }
        binding.forgotPasswordTextInputLayout.setError(null);
        return true;
    }
}