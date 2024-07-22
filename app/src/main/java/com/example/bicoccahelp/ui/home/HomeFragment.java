package com.example.bicoccahelp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentHomeBinding;
import com.example.bicoccahelp.ui.lessonBooking.TutorFragmentDirections;
import com.example.bicoccahelp.utils.ServiceLocator;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NavController navController;


    private UserRepository userRepository;
    private TutorRepository tutorRepository;
    private HomeViewModel homeViewModel;




    public HomeFragment() {

    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userRepository = ServiceLocator.getInstance().getUserRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();

        HomeViewModelFactory factory = new HomeViewModelFactory(userRepository);
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
    }
}