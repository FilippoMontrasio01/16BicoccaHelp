package com.example.bicoccahelp.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentVerifyEmailBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class VerifyEmailFragment extends Fragment implements View.OnClickListener{

    private NavController navController;
    private UserRepository userRepository;
    private AuthRepository authRepository;
    private FragmentVerifyEmailBinding binding;
    private WelcomeViewModel welcomeViewModel;

    public VerifyEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        authRepository = ServiceLocator.getInstance().getAuthRepository();

        WelcomeViewModelFactory factory = new WelcomeViewModelFactory(authRepository,
                userRepository);

        welcomeViewModel = new ViewModelProvider(this, factory).get(WelcomeViewModel.class);

        welcomeViewModel.getEmailVerified().observe(this, verified -> {
            if(verified){
                requireActivity().finish();
                navController.navigate(R.id.action_from_verify_email_to_main);
            }else{
                Snackbar.make(requireView(), getString(R.string.email_not_verified),
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
        binding = FragmentVerifyEmailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this.getParentFragment());
        binding.continueButton.setOnClickListener(this);
        binding.resendButton.setOnClickListener(this);

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
        if(v.getId() == binding.continueButton.getId()){
            welcomeViewModel.verifyEmail();
            return;
        }

        if(v.getId() == binding.resendButton.getId()){
            welcomeViewModel.resendEmail();
        }
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