package com.example.bicoccahelp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.CreateTutorRequest;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentCompleteTutorProfileBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;


public class CompleteTutorProfileFragment extends Fragment implements View.OnClickListener{

    private FragmentCompleteTutorProfileBinding binding;
    private TutorRepository tutorRepository;
    private NavController navController;
    private CorsoDiStudiRepository corsoDiStudiRepository;
    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private String livello = "Triennale";

    public CompleteTutorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCompleteTutorProfileBinding.inflate(inflater,
                container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.createTutorButton.setOnClickListener(this);
        binding.tutorBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == binding.createTutorButton.getId()){
            createTutorOnClick();
            return;
        }

        if(v.getId() == binding.tutorBackButton.getId()){
            backOnclick();
        }

    }

    private void backOnclick() {
        navController.navigate(R.id.action_from_complete_tutor_to_profile_fragment);
    }

    private void createTutorOnClick() {
        String studyProgram = binding.createTutorEditText.getText().toString();

        if(binding.livelloCheckbox.isChecked()){
            livello = getString(R.string.magistrale);
        }

        InputValidator.isValidStudyProgram(studyProgram, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {

                if(exist){
                    getCorsoAndCreateStudent(studyProgram, livello);
                }else{
                    binding.createTutorTextInputLayout.setError(getString(R.string.insert_a_valid_studyProgram));
                }

            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error),Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void setDisponibilities(Map<String, Boolean> map){
        CheckBox[] checkBoxes = new CheckBox[]{
                binding.mondayCheckbox,
                binding.tuesdayCheckbox,
                binding.wednesdayCheckbox,
                binding.thursdayCheckbox,
                binding.fridayCheckbox,
                binding.saturdayCheckbox,
                binding.sundayCheckbox
        };

        for (CheckBox checkBox : checkBoxes) {
            addListenerAndUpdateMap(checkBox, map);
        }
    }


    private void addListenerAndUpdateMap(CheckBox checkBox, Map<String, Boolean> map) {

        map.put(checkBox.getText().toString(), checkBox.isChecked());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                map.put(buttonView.getText().toString(), isChecked));
    }

    private void createTutor(CreateTutorRequest request){

        String uid = userRepository.getCurrentUser().uid;
        tutorRepository.createTutor(request, new Callback<TutorModel>() {
            @Override
            public void onSucces(TutorModel tutorModel) {

                studentRepository.studentExist(uid, new Callback<Boolean>() {
                    @Override
                    public void onSucces(Boolean exist) {
                        if(exist){
                            studentRepository.updateiSTutor(uid, true);
                        }

                        navController.popBackStack();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getCorsoAndCreateStudent(String studyProgram, String livello){


        Map<String, Boolean> disponibilitaGiorni = new HashMap<>();
        setDisponibilities(disponibilitaGiorni);



        corsoDiStudiRepository.getCorsoDiStudiIdByName(
                studyProgram, livello, new Callback<String>() {
                    @Override
                    public void onSucces(String idCorso) {
                        CreateTutorRequest request = new CreateTutorRequest(idCorso,
                                disponibilitaGiorni);
                        createTutor(request);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}