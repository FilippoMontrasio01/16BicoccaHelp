package com.example.bicoccahelp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.OnUpdateListener;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.databinding.FragmentUpdateNameDialogBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

public class UpdateNameDialogFragment extends DialogFragment implements View.OnClickListener{

    private UserRepository userRepository;
    private FragmentUpdateNameDialogBinding binding;
    private NavController navController;



    public UpdateNameDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateNameDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this.getParentFragment());

        binding.updateNameButtonConfirm.setOnClickListener(this::onClick);
        binding.updateNameButtonCancel.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.updateNameButtonConfirm.getId()){
            this.onClickConfirm(v);
            return;
        }

        if(v.getId() == binding.updateNameButtonCancel.getId()){
            this.onClickCancel(v);
        }


    }

    private void onClickCancel(View v) {
        getDialog().cancel();
    }

    private void onClickConfirm(View v) {
        String newName = binding.updateNameEditText.getText().toString();

        if(newName.isEmpty() || !InputValidator.isValidName(newName)){
            binding.updateNameTextInputLayout.setError("IL NOME INSERITO NON È VALIDO");
            return;
        }

        binding.updateNameTextInputLayout.setError(null);

        updateName(newName);
    }

    private void updateName(String newName) {
        userRepository.updateUsername(newName, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                navController.navigate(R.id.action_from_update_name_dialog_to_profile_fragment);
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(), "IL NOME NON È STATO AGGIORNATO", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}