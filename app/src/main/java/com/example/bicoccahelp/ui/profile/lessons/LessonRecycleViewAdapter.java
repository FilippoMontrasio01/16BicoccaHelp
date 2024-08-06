package com.example.bicoccahelp.ui.profile.lessons;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.ui.home.HomeViewModel;
import com.example.bicoccahelp.ui.home.YourLessonRecycleViewAdapter;
import com.example.bicoccahelp.utils.InputValidator;



import java.util.List;

public class LessonRecycleViewAdapter extends RecyclerView.Adapter<
        LessonRecycleViewAdapter.YourLessonViewHolder> {

    private final List<LessonModel> classList;
    private final Application application;
    private final OnItemClickListener listener;
    private final LessonViewModel lessonViewModel;
    private final HomeViewModel homeViewModel;

    private interface OnItemClickListener{
        void onClassItemClick(LessonModel lessonModel, TutorModel tutorModel);
    }

    public LessonRecycleViewAdapter(List<LessonModel> classList, Application application,
                                    OnItemClickListener listener, LessonViewModel lessonViewModel,
                                    HomeViewModel homeViewModel){
        this.classList = classList;
        this.application = application;
        this.listener = listener;
        this.lessonViewModel = lessonViewModel;
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

    public YourLessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_your_lesson,
                parent, false);

        return new YourLessonViewHolder(view);
    }

    public void onBindViewHolder(@NonNull YourLessonViewHolder holder, int position){
        holder.bind(classList.get(position), listener);
    }

    public class YourLessonViewHolder extends RecyclerView.ViewHolder{
        private final TextView lessonDescription;
        private final TextView dateTitle;
        private final TextView hourTitle;

        public YourLessonViewHolder(@NonNull View view){
            super(view);
            lessonDescription = view.findViewById(R.id.lessonDescription);
            dateTitle = view.findViewById(R.id.DateTitle);
            hourTitle = view.findViewById(R.id.HourTitle);
        }

        public void bind(LessonModel lessonModel, OnItemClickListener listener){
            hourTitle.setText(lessonModel.getOra());
            dateTitle.setText(InputValidator.formatDate(lessonModel.getData()));

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
            }else {
                lessonDescription.setText(lessonModel.getDescription());
            }


            homeViewModel.getTutorDetails(lessonModel.getUid_tutor(), new Callback<TutorModel>() {
                @Override
                public void onSucces(TutorModel tutorModel) {
                    itemView.setOnClickListener(v -> listener.onClassItemClick(lessonModel,
                            tutorModel));
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }
}
