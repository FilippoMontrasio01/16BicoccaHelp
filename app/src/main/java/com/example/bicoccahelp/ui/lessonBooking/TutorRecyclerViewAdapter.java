package com.example.bicoccahelp.ui.lessonBooking;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.utils.GlideLoadModel;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TutorRecyclerViewAdapter extends RecyclerView.Adapter<
        TutorRecyclerViewAdapter.TutorViewHolder> {
    public interface OnItemClickListener {
        void onTutorItemClick(TutorModel tutorModel);
    }

    private final List<TutorModel> tutorList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;
    private final TutorViewModel tutorViewModel;

    public TutorRecyclerViewAdapter(List<TutorModel> tutorList, Application application,
                                       OnItemClickListener onItemClickListener,
                                    TutorViewModel tutorViewModel) {
        this.tutorList = tutorList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
        this.tutorViewModel = tutorViewModel;
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
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_list_item,
                parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        holder.bind(tutorList.get(position), onItemClickListener);
    }

    public class TutorViewHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView tutorName;
        private final ImageView areaIcon;
        private final TextView corso;
        private final TextView averageReview;

        private final ImageView starIcon;
        private StudentRepository studentRepository;
        private CorsoDiStudiRepository corsoDiStudiRepository;
        private ReviewRepository reviewRepository;
        private UserRepository userRepository;

        public TutorViewHolder(@NonNull View view) {
            super(view);
            photo = view.findViewById(R.id.tutorListItemLogo);
            tutorName = view.findViewById(R.id.tutorListItemName);
            areaIcon = view.findViewById(R.id.areaIcon);
            corso = view.findViewById(R.id.tutorListItemCorsoDiStudi);
            averageReview = view.findViewById(R.id.tutorListReview);
            starIcon = view.findViewById(R.id.star_image_review);


            studentRepository = ServiceLocator.getInstance().getStudentRepository();
            corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();
            reviewRepository = ServiceLocator.getInstance().getReviewRepository();
            userRepository = ServiceLocator.getInstance().getUserRepository();

        }

        public void bind(TutorModel tutorModel, OnItemClickListener listener) {

            String currentUid = tutorViewModel.getUserId();

            if (!tutorModel.getUid().equals(currentUid)) {
                tutorName.setText(tutorModel.getName());
                itemView.setOnClickListener(view -> listener.onTutorItemClick(tutorModel));
                getCorsoDiStudi(tutorModel.getUid());


                tutorViewModel.getAverageReview(tutorModel.getUid(), new Callback<Double>() {
                    @Override
                    public void onSucces(Double average) {
                        if (average != null) {
                            averageReview.setText(String.valueOf(average));
                            starIcon.setImageResource(R.drawable.star_review);
                        } else {
                            averageReview.setText("0.0");
                            starIcon.setImageDrawable(null);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

                if(tutorModel.getPhotoUri() == null || TextUtils.isEmpty(tutorModel
                        .getPhotoUri().toString()) ){
                    photo.setImageResource(R.drawable.profile_icon);
                }else{
                    Glide.with(application.getApplicationContext())
                            .load(GlideLoadModel.get(tutorModel.getPhotoUri().toString()))
                            .into(photo);
                }

            }
        }

        private void getCorsoDiStudi(String idTutor){
            tutorViewModel.getCorsoDiStudi(idTutor, new Callback<String>() {
                @Override
                public void onSucces(String idCorso) {
                    getNomeCorso(idCorso);
                    getAreaCorso(idCorso);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

        private void getNomeCorso(String idCorso){

            tutorViewModel.getNomeCorso(idCorso, new Callback<String>() {
                @Override
                public void onSucces(String nomeCorsoDiStudi) {
                    if(nomeCorsoDiStudi != null){
                        nomeCorsoDiStudi = InputValidator.capitalizeFirstLetter(nomeCorsoDiStudi);
                        corso.setText(nomeCorsoDiStudi);
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

        private void getAreaCorso(String idCorso){


            tutorViewModel.getAreaCorso(idCorso, new Callback<String>() {
                @Override
                public void onSucces(String areaCorsoDiStudi) {
                    if(areaCorsoDiStudi != null){
                        switch (areaCorsoDiStudi){
                            case "Scientifica":
                                areaIcon.setImageResource(R.drawable.scienze);
                                break;
                            case "Medica":
                                areaIcon.setImageResource(R.drawable.medicina);
                                break;
                            case "Economico statistica":
                                areaIcon.setImageResource(R.drawable.economia);
                                break;
                            case "Sociologica":
                                areaIcon.setImageResource(R.drawable.sociologia);
                                break;
                            case "Psicologica":
                                areaIcon.setImageResource(R.drawable.psicologia);
                                break;
                            case "Formazione":
                                areaIcon.setImageResource(R.drawable.educazione);
                                break;
                            case "Giuridica":
                                areaIcon.setImageResource(R.drawable.giurisprudenza);
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }


            });
        }

    }

}