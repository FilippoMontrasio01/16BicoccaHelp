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
import com.example.bicoccahelp.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment implements View.OnClickListener{

    private NavController navController;
    private FragmentLoginBinding binding;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

       binding.loginButtonRegister.setOnClickListener(this);
       binding.buttonLoginForgotPassword.setOnClickListener(this);



    }



    public void onClick(@NonNull View view) {
        if (view.getId() == binding.loginButtonRegister.getId()) {
            this.onRegisterClick();
            return;
        }

        if (view.getId() == binding.buttonLoginForgotPassword.getId()) {
            this.onForgotPasswordClick();
            return;
        }


    }

    public void onRegisterClick() {
        navController.navigate(R.id.action_from_login_to_registration);
    }

    public void onForgotPasswordClick() {
        navController.navigate(R.id.action_from_login_to_forgot_password);
    }




}