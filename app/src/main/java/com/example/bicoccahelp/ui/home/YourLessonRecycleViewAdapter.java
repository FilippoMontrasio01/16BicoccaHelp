package com.example.bicoccahelp.ui.home;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;

import org.checkerframework.checker.units.qual.N;

import java.util.List;

public class YourLessonRecycleViewAdapter extends RecyclerView.Adapter<
        YourLessonRecycleViewAdapter.YourLessonViewHolder> {

    private final List<LessonModel> classList;
    private final Application application;
    private final OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClassItemClick(LessonModel lessonModel);
    }

    public YourLessonRecycleViewAdapter(List<LessonModel> classList, Application application,
                                        OnItemClickListener listener){
        this.classList = classList;
        this.application = application;
        this.listener = listener;
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

            if(lessonModel.getDescription() != null){
                lessonDescription.setText("Class with: ");
            }else{
                lessonDescription.setText("Class with: "+lessonModel.getUid_tutor());
            }

            dateTitle.setText(InputValidator.formatDate(lessonModel.getData()));
            hourTitle.setText(lessonModel.getOra());

            itemView.setOnClickListener(v -> listener.onClassItemClick(lessonModel));
        }

    }

}
