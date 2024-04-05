package com.example.bicoccahelp.ui.welcome;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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


public class RegistrationFragment extends Fragment implements View.OnClickListener{

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
        //binding.createAccountNameEditText.setOnFocusChangeListener(this);
        //binding.createAccountEmailEditText.setOnFocusChangeListener(this);
        //binding.createAccountPasswordEditText.setOnFocusChangeListener(this);
        binding.createAccountButton.setOnClickListener(this);
    }

    /*public void onFocusChange(@NonNull View view, boolean hasFocus){
        if(hasFocus){
            return;
        }

        if(view.getId() == binding.createAccountEmailEditText.getId()){
            this.validateEmail();
            return;
        }

        if(view.getId() == binding.createAccountPasswordEditText.getId()){
            this.validatePassword();
        }


    }*/

    @Override
    public void onClick(@NonNull View v) {
        if(v.getId() == binding.createAccountButton.getId()){
            this.onRegisterClick(v);
        }
    }

    public void onRegisterClick(@NonNull View view){
        /*boolean isValidEmail = this.validateEmail();
        boolean isValidPsw = this.validatePassword();

        if(!isValidEmail || isValidPsw){
            return;
        }
        */


        String email = binding.createAccountEmailEditText.getText().toString();
        String psw = binding.createAccountPasswordEditText.getText().toString();

        authRepository.register(email, psw, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                navController.navigate(R.id.action_from_registration_to_login);
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(view,"ACCOUNT NON CREATO", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private boolean validateEmail(){
        String email = binding.createAccountEmailEditText.getText().toString();

        if(!InputValidator.isValidEmail(email)){
            binding.createAccountEmailEditText.setError("ERROR");
            return false;
        }

        return true;
    }

    private boolean validatePassword(){
        String psw = binding.createAccountPasswordEditText.getText().toString();

        if(!InputValidator.isValidPassword(psw)){
            binding.createAccountPasswordEditText.setError("ERROR");
            return false;
        }

        return true;
    }*/

    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }
}