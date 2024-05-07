package com.example.bicoccahelp.ui.lessonBooking;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RadioButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicoccahelp.R;
import com.google.android.material.snackbar.Snackbar;


import java.util.List;

public class DateRecycleViewAdapter extends RecyclerView.Adapter<
        DateRecycleViewAdapter.DateViewHolder>{

    private final List<String> disponibilitaOrari;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

    public DateRecycleViewAdapter(List<String> disponibilitaOrari) {
        this.disponibilitaOrari = disponibilitaOrari;
    }

    @Override
    public int getItemCount() {
        return disponibilitaOrari.size();
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_list_item, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String orario = disponibilitaOrari.get(position);
        holder.bind(orario, position);
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        private final ToggleButton toggleButton;

        public DateViewHolder(@NonNull View view) {
            super(view);
            toggleButton = view.findViewById(R.id.hourRadio);

            toggleButton.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (!itemStateArray.get(adapterPosition, false)) {
                    // Abilita il ToggleButton cliccato
                    toggleButton.setChecked(true);
                    itemStateArray.put(adapterPosition, true);

                    // Disabilita gli altri ToggleButton
                    for (int i = 0; i < itemStateArray.size(); i++) {
                        if (i != adapterPosition) {
                            itemStateArray.put(i, false);
                        }
                    }
                    // Notifica l'adattatore dei cambiamenti
                    notifyDataSetChanged();

                }
            });
        }

        public void bind(String orario, int position) {
            toggleButton.setText(orario);
            toggleButton.setTextOn(orario);
            toggleButton.setTextOff(orario);
            // Utilizza lo stato salvato se presente
            toggleButton.setChecked(itemStateArray.get(position, false));
        }
    }

    public void clearData() {
        // Rimuovi tutti gli elementi dalla lista
        disponibilitaOrari.clear();

        // Notifica all'adapter che i dati sono cambiati
        notifyDataSetChanged();
    }

}
