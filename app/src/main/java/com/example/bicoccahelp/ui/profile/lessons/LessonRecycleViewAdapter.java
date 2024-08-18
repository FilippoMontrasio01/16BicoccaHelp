package com.example.bicoccahelp.ui.profile.lessons;

import android.app.Application;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.ui.home.HomeViewModel;
import com.example.bicoccahelp.ui.home.YourLessonRecycleViewAdapter;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.InputValidator;



import java.util.List;

public class LessonRecycleViewAdapter extends RecyclerView.Adapter<
        LessonRecycleViewAdapter.YourLessonViewHolder> {

    private final List<LessonModel> classList;
    private final Application application;
    private final LessonViewModel lessonViewModel;

    private interface OnItemClickListener{
        void onClassItemClick(LessonModel lessonModel);
    }

    public LessonRecycleViewAdapter(List<LessonModel> classList, Application application,
                                    LessonViewModel lessonViewModel
                                    ){
        this.classList = classList;
        this.application = application;
        this.lessonViewModel = lessonViewModel;
    }

    public int getItemCount(){
        return classList.size();
    }

    public void aggiornaDati(List<LessonModel> nuoviDati) {

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
        holder.bind(classList.get(position));
    }

    public class YourLessonViewHolder extends RecyclerView.ViewHolder{
        private final TextView lessonDescription;
        private final TextView dateTitle;
        private final TextView hourTitle;
        private final ImageView userPhoto;
        private final TextView tutor_Name;
        private final ViewFlipper lessonViewFlipper;

        public YourLessonViewHolder(@NonNull View view){
            super(view);
            lessonDescription = view.findViewById(R.id.lessonDescription);
            dateTitle = view.findViewById(R.id.DateTitle);
            hourTitle = view.findViewById(R.id.HourTitle);
            userPhoto = view.findViewById(R.id.TutorImage);
            tutor_Name = view.findViewById(R.id.TutorName);
            lessonViewFlipper = view.findViewById(R.id.LessonViewFlipper);
        }

        public void bind(LessonModel lessonModel){
            hourTitle.setText(lessonModel.getOra());
            dateTitle.setText(InputValidator.formatDate(lessonModel.getData()));


            if(lessonModel.getDescription().isEmpty()){
                lessonViewModel.getTutorName(lessonModel.getUid_tutor(), new Callback<String>() {
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

            lessonViewModel.getTutorDetails(lessonModel.getUid_tutor(), new Callback<TutorModel>() {
                @Override
                public void onSucces(TutorModel tutorModel) {
                    tutor_Name.setText(tutorModel.getName());
                    if (tutorModel.getPhotoUri() == null || TextUtils.isEmpty(tutorModel
                            .getPhotoUri().toString())) {
                        userPhoto.setImageResource(R.drawable.profile_icon);
                    } else {
                        Glide.with(application.getApplicationContext())
                                .load(GlideLoadModel.get(tutorModel.getPhotoUri().toString()))
                                .into(userPhoto);
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });

            if(InputValidator.calculateDaysDifference(lessonModel.getData()) > 0){
                lessonViewFlipper.setDisplayedChild(0);
            }else{
                lessonViewFlipper.setDisplayedChild(1);
            }
        }
    }
}
