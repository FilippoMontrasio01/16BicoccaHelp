package com.example.bicoccahelp.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.airbnb.lottie.LottieAnimationView;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentLoginBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class LoginFragment extends Fragment implements View.OnClickListener{

    private NavController navController;
    private FragmentLoginBinding binding;
    private AuthRepository authRepository;
    private UserRepository userRepository;

    public LoginFragment() {
        // Required empty public constructor
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

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

       binding.loginButtonRegister.setOnClickListener(this);
       binding.buttonLoginForgotPassword.setOnClickListener(this);
       binding.loginButton.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == binding.loginButtonRegister.getId()) {
            this.onRegisterClick();
            return;
        }

        if (view.getId() == binding.buttonLoginForgotPassword.getId()) {
            this.onForgotPasswordClick();
        }

        if(view.getId() == binding.loginButton.getId()){
            this.onLoginClick();
        }

    }

    public void onRegisterClick() {
        navController.navigate(R.id.action_from_login_to_registration);
    }

    public void onForgotPasswordClick() {
        navController.navigate(R.id.action_from_login_to_forgot_password);
    }

    private void onLoginClick() {
        String email = binding.loginEmailEditText.getText().toString();
        String password = binding.loginPasswordEditText.getText().toString();

        if(email.length() == 0 || password.length() == 0){
            return;
        }


        createAndStartProgressBar().setVisibility(View.VISIBLE);
        createAndStartProgressBar().playAnimation();
        authRepository.login(email, password, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                createAndStartProgressBar().cancelAnimation();
                userRepository.reload(new Callback<UserModel>() {
                    @Override
                    public void onSucces(UserModel userModel) {
                        if(!userModel.emailVerified){
                            navController.navigate(R.id.action_from_login_to_verify_email);
                            return;
                        }

                        navController.navigate(R.id.action_from_login_to_profile);
                        requireActivity().finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        createAndStartProgressBar().cancelAnimation();
                        Snackbar.make(getView(), getString(R.string.login_fail), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                createAndStartProgressBar().setVisibility(View.GONE);
                createAndStartProgressBar().cancelAnimation();
                Snackbar.make(getView(),getString(R.string.invalid_email_and_password), Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public LottieAnimationView createAndStartProgressBar(){
        LottieAnimationView animationView = binding.lottieAnimationView;
        animationView.setAnimation(getString(R.string.switch_loaders_json));

        return animationView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(userRepository.getCurrentUser() != null){
            navController.navigate(R.id.action_from_login_to_profile);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}