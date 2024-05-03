package com.example.bicoccahelp.ui.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentProfileBinding;
import com.example.bicoccahelp.databinding.FragmentProfilePhotoBinding;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class ProfilePhotoFragment extends Fragment implements View.OnClickListener{

    private FragmentProfilePhotoBinding binding;
    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private TutorRepository tutorRepository;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
               if(uri != null){
                   updatePhoto(uri);
               }
            });

    public ProfilePhotoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfilePhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserModel user = userRepository.getCurrentUser();

        if(user.getPhotoUri() != null){
            loadPhoto(user.getPhotoUri(), false);
        }

        binding.userPhoto.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == binding.userPhoto.getId()) {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());



        }



    }

    public void updatePhoto(Uri photoUri) {
        userRepository.updatePhoto(photoUri, new Callback<Void>() {
            @Override
            public void onSucces(Void unused) {
                UserModel user = userRepository.getCurrentUser();
                loadPhoto(user.getPhotoUri(), true);

                studentRepository.isTutor(user.getUid(), true, new Callback<Boolean>() {
                    @Override
                    public void onSucces(Boolean exist) {
                        if(exist){
                            tutorRepository.updateTutorPhoto(user.getUid(), user.getPhotoUri());
                        }

                        studentRepository.updateStudentPhoto(user.getUid(), user.getPhotoUri());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Snackbar.make(requireView(), getString(R.string.generic_error),Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Fragment parent = getParentFragment();
                        if(parent != null){
                            Snackbar.make(requireView(),getString(R.string.load_photo_error),
                                    Snackbar.LENGTH_SHORT).show();
                        }
            }
        });
    }

    public void loadPhoto(Uri photoUri, boolean skipCache) {
        if (photoUri.getScheme() != null && photoUri.getScheme()
                .equals(getString(R.string.https))) {

            Glide.with(requireActivity().getApplicationContext())
                    .load(photoUri)
                    .into(binding.userPhoto);
            return;
        }
        Glide.with(requireActivity().getApplicationContext())
                .load(GlideLoadModel.get(photoUri.toString()))
                .skipMemoryCache(skipCache)
                .diskCacheStrategy(skipCache ? DiskCacheStrategy.NONE : DiskCacheStrategy.AUTOMATIC)
                .into(binding.userPhoto);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}