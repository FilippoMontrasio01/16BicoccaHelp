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
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentForgotPasswordBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;


public class ForgotPasswordFragment extends Fragment implements View.OnClickListener,
View.OnFocusChangeListener{


    private AuthRepository authRepository;
    private UserRepository userRepository;
    private FragmentForgotPasswordBinding binding;
    private NavController navController;
    private WelcomeViewModel welcomeViewModel;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = ServiceLocator.getInstance().getAuthRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();

        WelcomeViewModelFactory factory = new WelcomeViewModelFactory(authRepository,
                userRepository);

        welcomeViewModel = new ViewModelProvider(this, factory).get(WelcomeViewModel.class);

        welcomeViewModel.getEmailSent().observe(this, sent -> {
            if(sent){
                navController.navigate(R.id.action_from_forgot_password_to_login);
            }else{
                Snackbar.make(requireView(), getString(R.string.email_not_send),
                        Snackbar.LENGTH_SHORT).show();
            }


        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        welcomeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isloading -> {
            if(isloading){
                createAndStartProgressBar().setVisibility(View.VISIBLE);
                createAndStartProgressBar().playAnimation();
            }else{
                createAndStartProgressBar().cancelAnimation();
                createAndStartProgressBar().setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.sendEmailButton.getId()){
            this.onSenEmailClick();
        }
    }

    private void onSenEmailClick() {
        String email = Objects.requireNonNull(binding.forgotPasswordEditText.getText()).toString();

        if(email.length() == 0){
            Snackbar.make(requireView(), getString(R.string.insert_your_email),
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        welcomeViewModel.forgotPassword(email);
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
        String email = Objects.requireNonNull(binding.forgotPasswordEditText.getText()).toString();

        if(!InputValidator.isValidEmail(email)) {
            binding.forgotPasswordTextInputLayout.setError(getString(R.string.invalid_email));
            return false;
        }
        binding.forgotPasswordTextInputLayout.setError(null);
        return true;
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