package com.example.bicoccahelp.ui.home;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;

import org.checkerframework.checker.units.qual.N;

import java.util.List;

public class YourLessonRecycleViewAdapter extends RecyclerView.Adapter<
        YourLessonRecycleViewAdapter.YourLessonViewHolder> {

    private final List<LessonModel> classList;
    private final Application application;
    private final OnItemClickListener listener;
    private final HomeViewModel homeViewModel;
    public interface OnItemClickListener{
        void onClassItemClick(LessonModel lessonModel, TutorModel tutorModel);
    }

    public YourLessonRecycleViewAdapter(List<LessonModel> classList, Application application,
                                        OnItemClickListener listener, HomeViewModel homeViewModel){
        this.classList = classList;
        this.application = application;
        this.listener = listener;
        this.homeViewModel = homeViewModel;
    }

    public int getItemCount(){
        return classList.size();
    }

    public void aggiornaDati(List<LessonModel> nuoviDati){
        this.classList.clear();
        this.classList.addAll(nuoviDati);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public YourLessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_your_lesson_card,
                parent, false);

        return new YourLessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourLessonViewHolder holder, int  position) {
        holder.bind(classList.get(position), listener);
    }

    public class YourLessonViewHolder extends RecyclerView.ViewHolder{

        private final TextView lessonDescription;
        private final TextView dateTitle;
        private final TextView hourTitle;


        public YourLessonViewHolder(@NonNull View view){
            super(view);
            lessonDescription = view.findViewById(R.id.lessonDescription1);
            dateTitle = view.findViewById(R.id.DateTitle1);
            hourTitle = view.findViewById(R.id.HourTitle1);


        }

        public void bind(LessonModel lessonModel, OnItemClickListener listener){

            if(lessonModel.getDescription().isEmpty()){
                homeViewModel.getTutorName(lessonModel.getUid_tutor(), new Callback<String>() {
                    @Override
                    public void onSucces(String tutorName) {
                        lessonDescription.setText("Class with: "+tutorName);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }else{
                lessonDescription.setText(lessonModel.getDescription());
            }

            dateTitle.setText(InputValidator.formatDate(lessonModel.getData()));
            hourTitle.setText(lessonModel.getOra());


            homeViewModel.getTutorDetails(lessonModel.getUid_tutor(), new Callback<TutorModel>() {
                @Override
                public void onSucces(TutorModel tutorModel) {
                    itemView.setOnClickListener(v -> listener.onClassItemClick(lessonModel, tutorModel));
                }

                @Override
                public void onFailure(Exception e) {
                    // Gestione errore
                }
            });
        }

    }

}
