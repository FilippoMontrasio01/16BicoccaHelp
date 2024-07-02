package com.example.bicoccahelp.ui.welcome;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.parser.ColorParser;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentRegistrationBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.C;

import java.util.Objects;


public class RegistrationFragment extends Fragment implements View.OnClickListener,
View.OnFocusChangeListener{

    private NavController navController;
    private FragmentRegistrationBinding binding;
    private AuthRepository authRepository;
    private UserRepository userRepository;
    private WelcomeViewModel welcomeViewModel;




    public RegistrationFragment() {

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
                navController.navigate(R.id.action_from_registration_to_verify_email);
            }else{
                Snackbar.make(requireView(), getString(R.string.email_not_send),
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



        String email = Objects.requireNonNull(binding.createAccountEmailEditText
                .getText()).toString();
        String psw = Objects.requireNonNull(binding.createAccountPasswordEditText
                .getText()).toString();
        String name = Objects.requireNonNull(binding.createAccountNameEditText
                .getText()).toString();

        welcomeViewModel.register(email, psw, name);

    }

    private boolean checkPassword(){

        String password = Objects.requireNonNull(binding.createAccountPasswordEditText
                .getText()).toString();
        String rePassword = Objects.requireNonNull(binding.createAccountRepeatPasswordEditText
                .getText()).toString();

        if(!rePassword.equals(password)){
            binding.createAccountRepswTextInputLayout
                    .setError(getString(R.string.repeate_password_fail));

            return false;
        }





        binding.createAccountRepswTextInputLayout.setError(null);
        return true;
    }

    @SuppressLint("ResourceType")
    private boolean validateName(){
        String name = Objects.requireNonNull(binding.createAccountNameEditText.getText())
                .toString();

        if(name.length() >= 3){
            binding.nameVerificationCard1.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.light_green)));
        }else{
            binding.nameVerificationCard1.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
        }

        if(!name.matches("(.*[0-9].*)")){
            binding.nameVerificationCard2.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.light_green)));
        }else{
            binding.nameVerificationCard2.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
        }

        if(!name.matches("^(?=.*[@#$%^&+=!]).*$")){
            binding.nameVerificationCard3.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.light_green)));
        }else{
            binding.nameVerificationCard3.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
        }

        if(!InputValidator.isValidName(name)){
            binding.createAccountNameTextInputLayout.setError(getString(R.string.invalid_name));
            return false;
        }



        binding.createAccountNameTextInputLayout.setError(null);
        return true;
    }


    @SuppressLint("ResourceType")
    private boolean validateEmail(){
        String email = Objects.requireNonNull(binding.createAccountEmailEditText.getText())
                .toString();

        if(!InputValidator.isValidEmail(email) ){
            binding.createAccountEmailTextInputLayout.setError(getString(R.string.invalid_email));
            binding.emailVerificationCard.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
            return false;
        }else{
            binding.emailVerificationCard.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.light_green)));
        }



        binding.createAccountEmailTextInputLayout.setError(null);
        return true;
    }

    @SuppressLint("ResourceType")
    private boolean validatePassword(){
        String psw = Objects.requireNonNull(binding.createAccountPasswordEditText.getText())
                .toString();


        if(psw.length() >= 8){
            binding.cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.light_green)));
        }else{
            binding.cardOne.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
        }

        if(psw.matches("(.*[A-Z].*)")){
            binding.cardFour.setCardBackgroundColor(Color
                    .parseColor((getString(R.color.light_green))));
        }else{
            binding.cardFour.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
        }

        if(psw.matches("(.*[0-9].*)")){
            binding.cardTwo.setCardBackgroundColor(Color.parseColor((getString(R.color.light_green))));
        }else{
            binding.cardTwo.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
        }

        if(psw.matches("^(?=.*[@#$%^&+=!]).*$")){
            binding.cardThree.setCardBackgroundColor(Color
                    .parseColor((getString(R.color.light_green))));
        }else{
            binding.cardThree.setCardBackgroundColor(Color
                    .parseColor(getString(R.color.cranberry_red)));
        }

        if(!InputValidator.isValidPassword(psw)){
            binding.createAccountPswTextInputLayout.setError(getString(R.string.invalid_password));
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