package com.example.bicoccahelp.ui.review;

import static android.provider.Settings.System.getString;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.review.ReviewModel;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.ui.lessonBooking.TutorRecyclerViewAdapter;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class ReviewRecycleViewAdapter extends RecyclerView.Adapter<
        ReviewRecycleViewAdapter.ReviewViewHolder> {

    private final List<ReviewModel> reviewList;
    private final Application application;
    private final TutorRepository tutorRepository;
    private final ReviewViewModel reviewViewModel;

    public ReviewRecycleViewAdapter(List<ReviewModel> reviewList, Application application,
                                    ReviewViewModel reviewViewModel) {
        this.reviewList = reviewList;
        this.application = application;
        this.reviewViewModel = reviewViewModel;
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void aggiornaDati(List<ReviewModel> nuoviDati) {
        this.reviewList.clear();
        this.reviewList.addAll(nuoviDati);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewRecycleViewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_list_item,
                parent, false);
        return new ReviewRecycleViewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviewList.get(position));
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photo;
        private final TextView tutorName;
        private final TextView stars;
        private final TextView email;


        public ReviewViewHolder(@NonNull View view) {
            super(view);
            photo = view.findViewById(R.id.tutorListItemLogo);
            tutorName = view.findViewById(R.id.tutorListItemName);
            email = view.findViewById(R.id.tutorListItemCorsoDiStudi);
            stars = view.findViewById(R.id.tutorListReview);
        }


        public void bind(ReviewModel reviewModel) {

            reviewViewModel.getTutorName(reviewModel.getUidTutor(), new Callback<String>() {
                @Override
                public void onSucces(String name) {
                    if(name != null){
                        tutorName.setText(name);
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });

            reviewViewModel.getPhotoUri(reviewModel.getUidTutor(), new Callback<Uri>() {
                @Override
                public void onSucces(Uri tutorPhotoUri) {
                    if(tutorPhotoUri == null || TextUtils.isEmpty(tutorPhotoUri.toString()) ){
                        photo.setImageResource(R.drawable.profile_icon);
                    }else{
                        Glide.with(application.getApplicationContext())
                                .load(GlideLoadModel.get(tutorPhotoUri.toString()))
                                .into(photo);
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });

            reviewViewModel.getTutorEmail(reviewModel.getUidTutor(), new Callback<String>() {
                @Override
                public void onSucces(String tutorEmail) {
                    if(tutorEmail.isEmpty() || (tutorEmail == null)){
                        email.setText("Email");
                    }else{
                        email.setText(tutorEmail);
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
            checkStar(reviewModel);

        }

        public void checkStar(ReviewModel reviewModel){
            double starsValue = reviewModel.getStars();
            if (starsValue == Math.floor(starsValue)) {
                stars.setText(String.valueOf((int) starsValue));
            } else {
                // Altrimenti, convertilo direttamente in una stringa
                stars.setText(String.valueOf(starsValue));
            }
        }





    }

}
