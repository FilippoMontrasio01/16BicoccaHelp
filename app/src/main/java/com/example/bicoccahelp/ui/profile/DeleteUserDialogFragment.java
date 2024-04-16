package com.example.bicoccahelp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

public class DeleteUserDialogFragment extends DialogFragment implements View.OnClickListener{


    private UserRepository userRepository;
    private FragmentDeleteUserDialogBinding binding;
    private NavController navController;
    public DeleteUserDialogFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeleteUserDialogBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert this.getParentFragment() != null;
        navController = NavHostFragment.findNavController(this.getParentFragment());
        binding.deleteAccountButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == binding.deleteAccountButton.getId()){
            this.onDeleteClick(v);
        }
    }

    private void onDeleteClick(View v) {
        String password = Objects.requireNonNull(binding.deleteAccountPasswordEditText.getText()).toString();

        if (password.isEmpty()) {
            binding.deleteAccountPasswordTextInputLayout.setError(getString(R.string.insert_your_password));
            return;
        }

        reAuthAndDelete(password);
    }


    public void reAuthAndDelete(String password){
        userRepository.reauthenticate(password, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                deleteUser();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(),getString(R.string.reauth_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteUser(){
        userRepository.deleteUser(new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                navController.navigate(R.id.action_from_delete_dialog_to_welcome_activity);
                requireActivity().finish();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(getView(),getString(R.string.delete_account_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}