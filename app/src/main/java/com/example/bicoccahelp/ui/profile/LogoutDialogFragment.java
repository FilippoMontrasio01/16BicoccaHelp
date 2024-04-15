package com.example.bicoccahelp.ui.profile;

import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.databinding.FragmentLogoutDialogBinding;
import com.example.bicoccahelp.utils.ServiceLocator;


public class LogoutDialogFragment extends DialogFragment implements  View.OnClickListener{
    private AuthRepository authRepository;
    private FragmentLogoutDialogBinding binding;
    private NavController navController;

    public LogoutDialogFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = ServiceLocator.getInstance().getAuthRepository();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogoutDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this.getParentFragment());
        binding.signOutButtonConfirm.setOnClickListener(this);
        binding.signOutButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.signOutButtonConfirm.getId()){
            this.onClickConfirm(v);
            return;
        }

        if(v.getId() == binding.signOutButtonCancel.getId()){
            this.onClickCancel(v);
        }
    }

    private void onClickCancel(View v) {
        getDialog().cancel();
    }

    private void onClickConfirm(View v) {
        authRepository.logout();
        navController.navigate(R.id.action_from_SignOut_to_welcome_activity);
        requireActivity().finish();
    }
}