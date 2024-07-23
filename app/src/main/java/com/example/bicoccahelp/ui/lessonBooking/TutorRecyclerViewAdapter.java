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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.review.ReviewRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
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

    public TutorRecyclerViewAdapter(List<TutorModel> tutorList, Application application,
                                       OnItemClickListener onItemClickListener) {
        this.tutorList = tutorList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
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
        private final TextView review;

        private final ImageView starIcon;
        private StudentRepository studentRepository;
        private CorsoDiStudiRepository corsoDiStudiRepository;
        private ReviewRepository reviewRepository;

        public TutorViewHolder(@NonNull View view) {
            super(view);
            photo = view.findViewById(R.id.tutorListItemLogo);
            tutorName = view.findViewById(R.id.tutorListItemName);
            areaIcon = view.findViewById(R.id.areaIcon);
            corso = view.findViewById(R.id.tutorListItemCorsoDiStudi);
            review = view.findViewById(R.id.tutorListReview);
            starIcon = view.findViewById(R.id.star_image_review);


            studentRepository = ServiceLocator.getInstance().getStudentRepository();
            corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();
            reviewRepository = ServiceLocator.getInstance().getReviewRepository();

        }

        public void bind(TutorModel tutorModel, OnItemClickListener listener) {
            tutorName.setText(tutorModel.getName());
            itemView.setOnClickListener(view -> listener.onTutorItemClick(tutorModel));
            getCorsoId(tutorModel.getUid());
            getAverageReview(tutorModel.getUid());


            if(tutorModel.getPhotoUri() == null || TextUtils.isEmpty(tutorModel
                    .getPhotoUri().toString()) ){
                photo.setImageResource(R.drawable.profile_icon);
            }else{
                Glide.with(application.getApplicationContext())
                        .load(GlideLoadModel.get(tutorModel.getPhotoUri().toString()))
                        .into(photo);
            }

        }

        private void getCorsoId(String uid){

            studentRepository.getCorsoDiStudi(uid, new Callback<String>() {
                @Override
                public void onSucces(String idCorso) {
                    getCorsoArea(idCorso);
                    getNomeCorso(idCorso);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

        private void getNomeCorso(String idCorso){
            corsoDiStudiRepository.getCorsodiStudiName(idCorso, new Callback<String>() {
                @Override
                public void onSucces(String nomeCorso) {
                    nomeCorso = InputValidator.capitalizeFirstLetter(nomeCorso);
                    corso.setText(nomeCorso);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

        private void getCorsoArea(String idCorso){
            corsoDiStudiRepository.getArea(idCorso, new Callback<String>() {
                @Override
                public void onSucces(String areaCorso) {
                    setAreaLogo(areaCorso);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }



        private void setAreaLogo(String area){
            switch (area){
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

        private void getAverageReview(String uidTutor){
            reviewRepository.getAverageReview(uidTutor, new Callback<Double>() {
                @Override
                public void onSucces(Double average) {
                    if(average != null){
                        review.setText(String.valueOf(average));
                        starIcon.setImageResource(R.drawable.star_review);
                    }else{
                        review.setText("No Reviews");
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