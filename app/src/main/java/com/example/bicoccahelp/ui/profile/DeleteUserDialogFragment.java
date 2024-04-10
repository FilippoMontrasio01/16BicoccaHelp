package com.example.bicoccahelp.ui.profile;

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
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentDeleteUserDialogBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class DeleteUserDialogFragment extends DialogFragment{

    private NavController navController;
    private FragmentDeleteUserDialogBinding binding;
    private UserRepository userRepository;

    public DeleteUserDialogFragment() {
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
        binding = FragmentDeleteUserDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.deleteAccountButton.setOnClickListener(this::onClick);
        navController = NavHostFragment.findNavController(getParentFragment());
    }


    private void onClick(View view) {
        if(view.getId() == binding.deleteAccountButton.getId()){
            this.onDeleteClick(view);
        }
    }

    private void onDeleteClick(@NonNull View view) {
        String password = Objects.requireNonNull(binding.deleteAccountPasswordEditText.getText()).toString();

        if (password.isEmpty()) {
            binding.deleteAccountPasswordTextInputLayout.setError(getString(R.string.insert_your_password));
            return;
        }

        userRepository.reauthenticate(password, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                userRepository.deleteUser(new Callback<Void>() {
                    @Override
                    public void onSucces(Void unused) {
                        navController.navigate(R.id.action_from_delete_dialog_to_welcome_activity);
                        requireActivity().finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Snackbar.make(view,"Errore nell'eliminazione", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(view,"Errore nell'eliminazione", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}