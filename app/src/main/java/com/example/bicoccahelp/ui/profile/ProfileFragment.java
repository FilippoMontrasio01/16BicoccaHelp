package com.example.bicoccahelp.ui.profile;

import android.app.AlertDialog;
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
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentProfileBinding;
import com.example.bicoccahelp.utils.ServiceLocator;


public class ProfileFragment extends Fragment implements View.OnClickListener{


    private AuthRepository authRepository;
    private FragmentProfileBinding binding;
    private NavController navController;
    private UserRepository userRepository;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = ServiceLocator.getInstance().getAuthRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserModel user = userRepository.getCurrentUser();


        navController = Navigation.findNavController(view);
        binding.signOutItem.setOnClickListener(this);
        binding.deleteProfileItem.setOnClickListener(this);
        binding.displayNameTextView.setText(user.name);
        binding.displayEmailTextView.setText(user.email);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.signOutItem.getId()){
            showConfirmSignOutDialog();
            return;
        }

        if(v.getId() == binding.deleteProfileItem.getId()){
            showConfirmDeleteDialog();
        }
    }

    private void onSignOutClick() {
        authRepository.logout();
        navController.navigate(R.id.action_from_profile_to_welcome_activity);
        requireActivity().finish();
    }

    private void showConfirmSignOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sign_out)
                .setMessage("Sei sicuro di voler uscire?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    onSignOutClick();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showConfirmDeleteDialog() {;
        navController.navigate(R.id.action_from_profile_to_delete_dialog);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}