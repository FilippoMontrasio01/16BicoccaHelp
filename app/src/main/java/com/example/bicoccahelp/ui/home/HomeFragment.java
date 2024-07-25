package com.example.bicoccahelp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentHomeBinding;
import com.example.bicoccahelp.ui.lessonBooking.TutorFragmentDirections;
import com.example.bicoccahelp.utils.ServiceLocator;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private FragmentHomeBinding binding;
    private NavController navController;
    private boolean isLoading = false;
    private BestReviewsRecycleViewAdapter bestReviewsRecycleViewAdapter;
    private YourLessonRecycleViewAdapter yourLessonRecycleViewAdapter;


    private UserRepository userRepository;
    private TutorRepository tutorRepository;
    private LessonRepository lessonRepository;
    private HomeViewModel homeViewModel;




    public HomeFragment() {

    }



    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userRepository = ServiceLocator.getInstance().getUserRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        lessonRepository = ServiceLocator.getInstance().getLessonRepository();

        HomeViewModelFactory factory = new HomeViewModelFactory(userRepository, tutorRepository);
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.SeeAllReviewsButton.setOnClickListener(this);

        homeViewModel.getUserName(new Callback<String>() {
            @Override
            public void onSucces(String username) {
                binding.UsernameTextview.setText(username);
            }

            @Override
            public void onFailure(Exception e) {
                binding.UsernameTextview.setText(homeViewModel.getErrorMessage().toString());
            }
        });

        this.configureRecyclerView();
        this.observeUiState();
        this.loadFirstPage();
        this.addOnScrollListener();

    }

    private void configureRecyclerView(){
        bestReviewsRecycleView();
        yourLessonRecycleView();
    }

    private void yourLessonRecycleView(){
        RecyclerView yourLessonRecycleView = binding.LessonRecycleView;

        YourLessonRecycleViewAdapter.OnItemClickListener listener = lesson -> {
            if(lesson != null){
                navController.navigate(R.id.action_from_home_to_book_lesson);
            }
        };

        yourLessonRecycleViewAdapter = new YourLessonRecycleViewAdapter(
                homeViewModel.classList,
                requireActivity().getApplication(),
                listener,
                homeViewModel
        );

        yourLessonRecycleView.setAdapter(yourLessonRecycleViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
        yourLessonRecycleView.setLayoutManager(layoutManager);

    }

    private void bestReviewsRecycleView(){
        RecyclerView bestReviewsRecycleView = binding.BestReviewRecycleView;

        BestReviewsRecycleViewAdapter.OnItemClickListener listener = tutor -> {
            if (tutor != null) { // Aggiungi questo controllo di nullità
                HomeFragmentDirections.ActionToLessonCardFromHome action = HomeFragmentDirections.actionToLessonCardFromHome(
                        tutor.getName(), tutor.getEmail(), tutor.getPhotoUri().toString(), tutor.getUid());
                navController.navigate(action);
            } else {
                Log.e("HomeFragment", "TutorModel is null in OnItemClickListener");
            }
        };

        bestReviewsRecycleViewAdapter = new BestReviewsRecycleViewAdapter(
                homeViewModel.tutorList,
                requireActivity().getApplication(),
                listener);

        bestReviewsRecycleView.setAdapter(bestReviewsRecycleViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        bestReviewsRecycleView.setLayoutManager(layoutManager);

    }

    private void observeUiState(){
        homeViewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            stopLoading();
            if(uiState.fetched == 0){
                binding.EmptyBestReviewRecycleViewTextView.setVisibility(View.VISIBLE);
                binding.BestReviewRecycleView.setVisibility(View.GONE);
                binding.EmptyLessonRecycleViewTextView.setVisibility(View.VISIBLE);
                binding.LessonRecycleView.setVisibility(View.GONE);
                return;
            }else{
                binding.EmptyBestReviewRecycleViewTextView.setVisibility(View.GONE);
                binding.EmptyLessonRecycleViewTextView.setVisibility(View.GONE);
                binding.BestReviewRecycleView.setVisibility(View.VISIBLE);
                binding.LessonRecycleView.setVisibility(View.VISIBLE);
            }

            bestReviewsRecycleViewAdapter.notifyItemRangeInserted(uiState.sizeBeforeFetch,
                    uiState.fetched);

            yourLessonRecycleViewAdapter.notifyItemRangeInserted(uiState.sizeBeforeFetch,
                    uiState.fetched);
        });
    }

    private void loadFirstPage() {
        if (homeViewModel.getCurrentPage() == 0 && homeViewModel.hasMore()) {
            homeViewModel.getNextTutorsPage();
            homeViewModel.getNextClassesPage(homeViewModel.getUid());
        }
    }

    private void addOnScrollListener(){
        binding.BestReviewRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading && homeViewModel.hasMore() && dy > 0){
                    homeViewModel.getNextTutorsPage();
                }
            }
        });

        binding.LessonRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading && homeViewModel.hasMore() && dy > 0){
                    homeViewModel.getNextClassesPage(homeViewModel.getUid());
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
    public void onClick(View v) {
        if(v.getId() == binding.SeeAllReviewsButton.getId()){
            navController.navigate(R.id.action_from_home_to_book_lesson, null, new NavOptions.Builder()
                    .setPopUpTo(R.id.home_fragment, true)  // Rimuove il fragment di origine e tutto ciò che è sopra di esso nello stack
                    .build());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}