package com.example.bicoccahelp.ui.lessonBooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentTutorBinding;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.annotation.Nullable;

public class TutorFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = TutorFragment.class.getSimpleName();
    private FragmentTutorBinding binding;
    private TutorRecyclerViewAdapter tutorRecyclerViewAdapter;
    private TutorViewModel tutorViewModel;
    private NavController navController;
    private StudentRepository studentRepository;
    private CorsoDiStudiRepository corsoDiStudiRepository;
    private TutorRepository tutorRepository;
    private boolean isLoading = false;

    public TutorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();

        tutorViewModel = new ViewModelProvider(requireActivity(),
                new TutorViewModelFactory(tutorRepository)).get(TutorViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTutorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        this.configureRecyclerView();
        this.observeUiState();
        this.observeErrorMessage(view);
        this.loadFirstPage();
        this.addOnScrollListener();
        binding.filterNameButton.setOnClickListener(this);
    }


    private void configureRecyclerView() {
        RecyclerView tutorRecyclerView = binding.tutorRecyclerView;


        TutorRecyclerViewAdapter.OnItemClickListener listener = tutor -> {
            TutorFragmentDirections.ActionToLessonCard action = TutorFragmentDirections.actionToLessonCard(
                    tutor.getName(),tutor.getEmail(), tutor.getPhotoUri().toString(),
                    tutor.getUid());
            navController.navigate(action);
        };




        tutorRecyclerViewAdapter = new TutorRecyclerViewAdapter(
                tutorViewModel.tutorList,
                requireActivity().getApplication(), listener);
        tutorRecyclerView.setAdapter(tutorRecyclerViewAdapter);

        LinearLayoutManager layoutManager = (LinearLayoutManager) tutorRecyclerView
                .getLayoutManager();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                tutorRecyclerView.getContext(), layoutManager.getOrientation());
        tutorRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void observeUiState() {
        tutorViewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            stopLoading();

            if(uiState == null){
                return;
            }


            if(uiState.fetched > 0){
                tutorRecyclerViewAdapter.notifyItemRangeInserted(uiState.sizeBeforeFetch,
                        uiState.fetched);
            }

        });
    }

    private void observeErrorMessage(View view) {
        tutorViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Snackbar.make(view, getString(R.string.db_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFirstPage() {
        if (tutorViewModel.getCurrentPage() == 0 && tutorViewModel.hasMore()) {
            tutorViewModel.getNextTutorsPage();
        }
    }
    private void addOnScrollListener() {
        binding.tutorRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && tutorViewModel.hasMore() && dy > 0) {
                    tutorViewModel.getNextTutorsPage();
                }
            }
        });
    }

    private void startLoading() {
        isLoading = true;
    }

    private void stopLoading() {
        isLoading = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        tutorViewModel.restoreOriginalList();
        binding.searchTutorEditText.setText("");
    }

    @Override
    public void onClick(View v) {

        tutorViewModel.getTutorList().observe(getViewLifecycleOwner(),
                tutors -> tutorRecyclerViewAdapter.aggiornaDati(tutors));

        if(v.getId() == binding.filterNameButton.getId()){
            filterNameOnClick();
        }
    }

    private void filterNameOnClick() {
       String filter = binding.searchTutorEditText.getText().toString();


        if (filter.isEmpty()) {
            tutorViewModel.restoreOriginalList();
        } else if(binding.skillsToggle.isChecked()) {
            tutorViewModel.getTutorSkillPage(filter);
        }else if(binding.corsoToggle.isChecked()){
            tutorViewModel.getTutorCorsoDiStudiPage(filter);
        }else if(binding.dispoToggle.isChecked()){
            tutorViewModel.getTutorDisponibilityPage(filter);
        }else{
            tutorViewModel.getTutorNamePage(filter);
        }



    }


}

