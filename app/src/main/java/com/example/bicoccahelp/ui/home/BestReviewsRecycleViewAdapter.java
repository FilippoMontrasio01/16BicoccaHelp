package com.example.bicoccahelp.ui.home;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.ServiceLocator;
import java.util.List;

public class BestReviewsRecycleViewAdapter extends RecyclerView.Adapter<
        BestReviewsRecycleViewAdapter.ReviewsViewHolder> {

    private final List<TutorModel> tutorList;
    private final Application application;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onTutorItemClick(TutorModel tutorModel);
    }
    public BestReviewsRecycleViewAdapter(List<TutorModel> tutorList, Application application, OnItemClickListener listener){
        this.tutorList = tutorList;
        this.application = application;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public void aggiornaDati(List<TutorModel> nuoviDati) {
        this.tutorList.clear();
        this.tutorList.addAll(nuoviDati);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_best_reviews,
                parent, false);

        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.bind(tutorList.get(position), listener);
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{
        private final ImageView userPhoto;
        private final TextView tutorName;
        private final TextView tutorEmail;
        private final TextView averageReview;
        private final ImageView starIcon;
        private final Button bookLessonButton;
        private ReviewRepository reviewRepository;

        public ReviewsViewHolder(@NonNull View view){
            super(view);

            userPhoto = view.findViewById(R.id.TutorImage);
            tutorName = view.findViewById(R.id.TutorNameTextView);
            tutorEmail = view.findViewById(R.id.TutorEmailTextView);
            averageReview = view.findViewById(R.id.TutorReviewTextView);
            starIcon = view.findViewById(R.id.TutorReviewImageView);
            bookLessonButton = view.findViewById(R.id.BookLessonButton);

            reviewRepository = ServiceLocator.getInstance().getReviewRepository();
        }


        public void bind(TutorModel tutorModel, OnItemClickListener listener){
            tutorName.setText(tutorModel.getName());
            tutorEmail.setText(tutorModel.getEmail());
            getAverageReview(tutorModel.getUid());
            //itemView.setOnClickListener(v -> listener.onTutorItemClick(tutorModel));
            bookLessonButton.setOnClickListener(v -> listener.onTutorItemClick(tutorModel));
            if(tutorModel.getPhotoUri() == null || TextUtils.isEmpty(tutorModel
                    .getPhotoUri().toString()) ){
                userPhoto.setImageResource(R.drawable.profile_icon);
            }else{
                Glide.with(application.getApplicationContext())
                        .load(GlideLoadModel.get(tutorModel.getPhotoUri().toString()))
                        .into(userPhoto);
            }


        }


        private void getAverageReview(String uidTutor){
            reviewRepository.getAverageReview(uidTutor, new Callback<Double>() {
                @Override
                public void onSucces(Double average) {
                    if(average != null){
                        averageReview.setText(String.valueOf(average));
                        starIcon.setImageResource(R.drawable.star_review);
                    }else{
                        starIcon.setImageDrawable(null);
                    }

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

    }




}
