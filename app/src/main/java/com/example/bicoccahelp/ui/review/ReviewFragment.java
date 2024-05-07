package com.example.bicoccahelp.ui.review;

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
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.review.CreateReviewRequest;
import com.example.bicoccahelp.data.review.ReviewModel;
import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentReviewBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.C;

import io.grpc.Server;


public class ReviewFragment extends Fragment implements View.OnClickListener{

    private ReviewRepository reviewRepository;
    private TutorRepository tutorRepository;
    private UserRepository userRepository;
    private NavController navController;
    private FragmentReviewBinding binding;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewRepository = ServiceLocator.getInstance().getReviewRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.reviewButton.setOnClickListener(this);
        binding.backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.reviewButton.getId()){
            reviewOnClick();
            return;
        }

        if(v.getId() == binding.backButton.getId()){
            backOnClick();
            return;
        }
    }

    private void backOnClick() {
        navController.navigate(R.id.action_from_review_to_profile);
    }

    private void reviewOnClick() {
        String tutor = binding.reviewStudentEditText.getText().toString();

        if(tutor.isEmpty()){
            binding.reviewStudentTextInputLayout.setError(getText(R.string.tutor_to_review));
            return;
        }

        binding.reviewStudentTextInputLayout.setError(null);


        findTutor(tutor);
    }

    private void findTutor(String tutorName){

        tutorRepository.getTutorUid(tutorName, new Callback<String>() {
            @Override
            public void onSucces(String uidTutor) {

                if(!uidTutor.equals(userRepository.getCurrentUser().getUid())){
                    verifyMultipleReview(uidTutor);
                }else{
                    Snackbar.make(requireView(), getString(R.string.auto_review),
                            Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.tutor_to_review),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyMultipleReview(String uidTutor) {
        String uidStudent = userRepository.getCurrentUser().getUid();

        reviewRepository.checkReview(uidStudent, uidTutor, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean notExist) {
                if(!notExist){
                    makeReview(uidTutor, uidStudent);
                }else{
                    Snackbar.make(requireView(), getString(R.string.already_review),
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.db_error),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void makeReview(String tutorUid, String uidStudent){

        float stars = binding.ratingBar.getRating();
        CreateReviewRequest createReviewRequest = new CreateReviewRequest(tutorUid,
                uidStudent, stars);

        reviewRepository.createReview(createReviewRequest, new Callback<ReviewModel>() {
            @Override
            public void onSucces(ReviewModel reviewModel) {
                Snackbar.make(requireView(), getString(R.string.review_submit),
                        Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.review_not_submit),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}