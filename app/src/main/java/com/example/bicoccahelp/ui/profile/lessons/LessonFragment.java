package com.example.bicoccahelp.ui.profile.lessons;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentLessonBinding;
import com.example.bicoccahelp.databinding.FragmentProfileBinding;
import com.example.bicoccahelp.ui.home.HomeViewModel;
import com.example.bicoccahelp.utils.ServiceLocator;

public class LessonFragment extends Fragment {

    private NavController navController;
    private FragmentLessonBinding binding;
    private boolean isLoading = false;
    private LessonRecycleViewAdapter lessonRecycleViewAdapter;
    private UserRepository userRepository;
    private TutorRepository tutorRepository;
    private LessonViewModel lessonViewModel;


    public LessonFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRepository = ServiceLocator.getInstance().getUserRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();

        LessonViewModelFactory factory = new LessonViewModelFactory(userRepository, tutorRepository);
        lessonViewModel = new ViewModelProvider(this, factory).get(LessonViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLessonBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        this.configureRecycleView();
        this.observeUiState();
        this.loadFirstPage();
        this.addOnScrollListener();
    }

    private void configureRecycleView(){
        RecyclerView lessonRecycleView = binding.lessonRecycleView;

        lessonRecycleViewAdapter = new LessonRecycleViewAdapter(
                lessonViewModel.classList,
                requireActivity().getApplication(),
                lessonViewModel
        );

        lessonRecycleView.setAdapter(lessonRecycleViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
        lessonRecycleView.setLayoutManager(layoutManager);
    }

    private void observeUiState(){
        lessonViewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            stopLoading();

            if(uiState == null){
                return;
            }

            if(uiState.fetched > 0){
                lessonRecycleViewAdapter.notifyItemRangeInserted(uiState.sizeBeforeFetch, uiState.fetched);

            }

            if(uiState.inserted > -1){
                lessonRecycleViewAdapter.notifyItemInserted(uiState.inserted);
            }
        });
    }

    public void loadFirstPage(){
        if(lessonViewModel.getCurrentPage() == 0 && lessonViewModel.hasMore()){
            startLoading();
            lessonViewModel.getNextClassPage(lessonViewModel.getUid());
        }
    }

    private void addOnScrollListener() {
        binding.lessonRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && lessonViewModel.hasMore() && dy > 0){
                    startLoading();
                    lessonViewModel.getLessonList();
                }
            }
        });
    }

    private void startLoading(){
        isLoading = true;
    }

    private void stopLoading(){
        isLoading = false;
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}