package com.example.bicoccahelp.ui.welcome;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.auth.authException.EmailVerificationException;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentLoginBinding;
import com.example.bicoccahelp.databinding.FragmentRegistrationBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class RegistrationFragment extends Fragment implements View.OnClickListener,
View.OnFocusChangeListener{

    private NavController navController;
    private FragmentRegistrationBinding binding;
    private AuthRepository authRepository;
    private UserRepository userRepository;




    public RegistrationFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = ServiceLocator.getInstance().getAuthRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.createAccountEmailEditText.setOnFocusChangeListener(this);
        binding.createAccountPasswordEditText.setOnFocusChangeListener(this);
        binding.createAccountNameEditText.setOnFocusChangeListener(this);
        binding.createAccountButton.setOnClickListener(this);
        binding.createAccountRepeatPasswordEditText.setOnFocusChangeListener(this);
    }

    public void onFocusChange(@NonNull View view, boolean hasFocus){
        if(hasFocus){
            return;
        }

        if(view.getId() == binding.createAccountEmailEditText.getId()){
            this.validateEmail();
            return;
        }

        if(view.getId() == binding.createAccountPasswordEditText.getId()){
            this.validatePassword();
            return;
        }

        if(view.getId() == binding.createAccountNameEditText.getId()){
            this.validateName();
            return;
        }

        if(view.getId() == binding.createAccountRepeatPasswordEditText.getId()){
            this.checkPassword();
        }

    }

    @Override
    public void onClick(@NonNull View v) {
        if(v.getId() == binding.createAccountButton.getId()){
            this.onRegisterClick(v);
        }
    }

    public void onRegisterClick(@NonNull View view){
        boolean isValidEmail = this.validateEmail();
        boolean isValidPsw = this.validatePassword();
        boolean isValiName = this.validateName();
        boolean isCheckPsw = this.checkPassword();

        if(!isValidEmail || !isValidPsw || !isCheckPsw || !isValiName){
            return;
        }



        String email = binding.createAccountEmailEditText.getText().toString();
        String psw = binding.createAccountPasswordEditText.getText().toString();

        authRepository.register(email, psw, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {

                userRepository.sendEmailVerification(new Callback<Void>() {
                    @Override
                    public void onSucces(Void unused) {
                        navController.navigate(R.id.action_from_registration_to_login);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Snackbar.make(view,"ACCOUNT NON CREATO, MAIL NON INVIATA", Snackbar.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(view,"ACCOUNT NON CREATO", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkPassword(){

        String password = binding.createAccountPasswordEditText.getText().toString();
        String rePassword = binding.createAccountRepeatPasswordEditText.getText().toString();

        if(!rePassword.equals(password)){
            binding.createAccountRepswTextInputLayout.setError("LA PASSWORD È DIVERSA");
            return false;
        }

        binding.createAccountRepswTextInputLayout.setError(null);
        return true;
    }

    private boolean validateName(){
        String name = binding.createAccountNameEditText.getText().toString();

        if(!InputValidator.isValidName(name)){
            binding.createAccountNameTextInputLayout.setError("NOME NON VALIDO");
            return false;
        }

        binding.createAccountNameTextInputLayout.setError(null);
        return true;
    }


    private boolean validateEmail(){
        String email = binding.createAccountEmailEditText.getText().toString();

        if(!InputValidator.isValidEmail(email) ){
            binding.createAccountEmailTextInputLayout.setError("EMAIL NON VALIDA");
            return false;
        }

        binding.createAccountEmailTextInputLayout.setError(null);
        return true;
    }

    private boolean validatePassword(){
        String psw = binding.createAccountPasswordEditText.getText().toString();

        if(!InputValidator.isValidPassword(psw)){
            binding.createAccountPswTextInputLayout.setError("PASSWORD NON VALIDA");
            return false;
        }



        binding.createAccountPswTextInputLayout.setError(null);
        return true;
    }

    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }
}