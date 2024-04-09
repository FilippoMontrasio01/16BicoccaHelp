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

import com.airbnb.lottie.LottieAnimationView;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentVerifyEmailBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class VerifyEmailFragment extends Fragment implements View.OnClickListener{

    private NavController navController;
    private UserRepository userRepository;
    private FragmentVerifyEmailBinding binding;

    public VerifyEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVerifyEmailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.continueButton.setOnClickListener(this);
        binding.resendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.continueButton.getId()){
            this.continueOnclick(v);
            return;
        }

        if(v.getId() == binding.resendButton.getId()){
            this.resendOnClick(v);
        }
    }

    private void resendOnClick(View v) {


        userRepository.reload(new Callback<UserModel>() {


            @Override
            public void onSucces(UserModel userModel) {
                if(userModel.emailVerified){

                   Snackbar.make(getView(), "LA MAIL È GIA STATA VERIFICATA", Snackbar.LENGTH_SHORT).show();
                   navController.navigate(R.id.action_from_verify_email_fragment_to_main);
                   requireActivity().finish();
                }

                userRepository.sendEmailVerification(new Callback<Void>() {
                    @Override
                    public void onSucces(Void unused) {
                        Snackbar.make(getView(), "EMAIL INVIATA", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Snackbar.make(getView(), "EMAIL NON INVIATA", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(), "ERRORE", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void continueOnclick(View v) {

        createAndStartProgressBar().setVisibility(View.VISIBLE);
        createAndStartProgressBar().playAnimation();
        userRepository.reload(new Callback<UserModel>() {
            @Override
            public void onSucces(UserModel userModel) {
                if(!userModel.emailVerified){
                    createAndStartProgressBar().cancelAnimation();
                    Snackbar.make(getView(), "UTENTE NON VERIFICATO", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                navController.navigate(R.id.action_from_verify_email_fragment_to_main);
                requireActivity().finish();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(), "DEVI VERIFICARE LA TUA MAIL", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public LottieAnimationView createAndStartProgressBar(){
        LottieAnimationView animationView = binding.lottieAnimationView;
        animationView.setAnimation("switch_loaders.json");

        return animationView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}