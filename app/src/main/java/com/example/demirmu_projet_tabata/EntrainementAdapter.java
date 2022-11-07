package com.example.demirmu_projet_tabata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.demirmu_projet_tabata.db.Entrainement;

import java.util.List;

public class EntrainementAdapter extends ArrayAdapter<Entrainement> {

    public EntrainementAdapter(Context mCtx, List<Entrainement> taskList) {
        super(mCtx, R.layout.template_entrainement, taskList);
    }

    /**
     * Remplit une ligne de la listView avec les informations de la multiplication associée
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la multiplication
        final Entrainement entrainement = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_entrainement, parent, false);

        // Récupération des objets graphiques dans le template
        TextView textViewEntrainement = (TextView) rowView.findViewById(R.id.textViewEntrainement);
        TextView textViewDesc = (TextView) rowView.findViewById(R.id.textViewDesc);
        TextView textViewFinish = rowView.findViewById(R.id.textFinie);
        //
        textViewEntrainement.setText(entrainement.getLibelle());
        textViewDesc.setText(entrainement.getDescription());
        textViewFinish.setText(entrainement.getEntrainementFini());

        //
        return rowView;
    }
}
