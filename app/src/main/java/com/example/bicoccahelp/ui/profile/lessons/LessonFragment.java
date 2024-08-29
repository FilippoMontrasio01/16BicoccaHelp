package com.example.bicoccahelp.ui.profile.lessons;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentLessonBinding;
import com.example.bicoccahelp.databinding.FragmentProfileBinding;
import com.example.bicoccahelp.ui.home.HomeViewModel;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Objects;

public class LessonFragment extends Fragment implements View.OnClickListener{

    private NavController navController;
    private FragmentLessonBinding binding;
    private boolean isLoading = false;
    private LessonRecycleViewAdapter lessonRecycleViewAdapter;
    private UserRepository userRepository;
    private TutorRepository tutorRepository;
    private LessonViewModel lessonViewModel;
    private Timestamp selectedDate;

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

        binding.upcomingToggle.setOnClickListener(this);
        binding.TerminatedToggle.setOnClickListener(this);
        binding.filterNameButton.setOnClickListener(this);
        binding.dateButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {

        lessonViewModel.getLessonList().observe(getViewLifecycleOwner(),
                lessons -> lessonRecycleViewAdapter.aggiornaDati(lessons));

        if(v.getId() == binding.filterNameButton.getId()){
            filterNameOnClick();
            return;
        }

        if(v.getId() == binding.TerminatedToggle.getId()){
            filterCompletedOnClick();
            return;
        }

        if(v.getId() == binding.upcomingToggle.getId()) {
            filterUpcomingOnClick();
            return;
        }

        if(v.getId() == binding.dateButton.getId()){
            filterDateOnClick();
        }

    }

    private void filterCompletedOnClick(){
        lessonViewModel.getNextTerminatedClassesPage(lessonViewModel.getUid());
    }

    private void filterUpcomingOnClick(){
        lessonViewModel.getNextUpcomingClassesPage(lessonViewModel.getUid());
    }

    private void filterDateOnClick() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                R.style.MyDatePickerDialogTheme, (view, year1, month1, dayOfMonth1) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year1, month1, dayOfMonth1, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // Crea un Timestamp dalla data selezionata
            selectedDate = new Timestamp(calendar.getTime());
            binding.dateButton.setText(InputValidator.formatDate(selectedDate));

            // Reset della lista e chiamata al ViewModel per caricare le lezioni della nuova data
            lessonViewModel.resetClassList();  // Metodo per svuotare la lista corrente
            lessonViewModel.getNextDateClassesPage(selectedDate, lessonViewModel.getUid());

        }, year, month, day);

        dialog.show();
    }

    private void filterNameOnClick(){

        Log.d("PROVA", "IL BOTTONE VA");


        String tutorName = binding.searchTutorEditText.getText().toString();

        if(tutorName.isEmpty()){
            lessonViewModel.restoreOriginalList();
            Log.d("PROVA", "IL BOTTONE È VUOTO");

        }else{
            lessonViewModel.getTutorId(tutorName);
        }

        binding.dateButton.setText(R.string.date);

    }



    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}