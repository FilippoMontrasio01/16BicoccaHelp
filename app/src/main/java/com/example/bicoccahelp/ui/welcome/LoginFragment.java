package com.example.bicoccahelp.ui.welcome;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import java.util.Objects;


public class LoginFragment extends Fragment implements View.OnClickListener{

    private NavController navController;
    private FragmentLoginBinding binding;

    private WelcomeViewModel welcomeViewModel;
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

        WelcomeViewModelFactory factory = new WelcomeViewModelFactory(
                authRepository,
                userRepository
        );

        welcomeViewModel = new ViewModelProvider(this, factory).get(WelcomeViewModel.class);

        welcomeViewModel.getLoginSuccess().observe(this, success -> {
            if(success){
                handleAuthUser(userRepository.getCurrentUser());
            }else {
                Snackbar.make(requireView(), getString(R.string.login_fail),
                        Snackbar.LENGTH_SHORT).show();
            }


        });


        welcomeViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        UserModel user = userRepository.getCurrentUser();

        if (user != null) {
            this.handleAuthUser(user);
            return;
        }

        welcomeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isloading -> {
            if(isloading){
                createAndStartProgressBar().setVisibility(View.VISIBLE);
                createAndStartProgressBar().playAnimation();
            }else{
                createAndStartProgressBar().cancelAnimation();
                createAndStartProgressBar().setVisibility(View.GONE);
            }
        });


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
        String email = Objects.requireNonNull(binding.loginEmailEditText.getText()).toString();
        String password = Objects.requireNonNull(binding.loginPasswordEditText.getText())
                .toString();

        if(email.length() == 0 || password.length() == 0){
            return;
        }

        welcomeViewModel.login(email, password);
    }



    private void handleAuthUser(@NonNull UserModel user){
        if (!user.isEmailVerified()) {
            navController.navigate(R.id.action_from_login_to_verify_email);
            return;
        }

        requireActivity().finish();
        navController.navigate(R.id.action_from_login_to_main);

    }


    public LottieAnimationView createAndStartProgressBar(){
        LottieAnimationView animationView = binding.lottieAnimationView;
        animationView.setAnimation(getString(R.string.switch_loaders_json));

        return animationView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}