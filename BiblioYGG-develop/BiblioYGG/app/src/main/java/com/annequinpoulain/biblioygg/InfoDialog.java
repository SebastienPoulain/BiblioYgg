package com.annequinpoulain.biblioygg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class InfoDialog extends AppCompatDialogFragment {

    private TextView tvNomInfo, tvAuteur, tvDesc, tvCat, tvVendeurInfo, tvStat, tvPrixInfo;
    //private InfoDialog.InfoDialogListener listener;
    AlertDialog.Builder builder;
    String[] args;
    RecyclerView r;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.info_dialog, null);

        args = getArguments().getStringArray("infos");

        tvNomInfo = view.findViewById(R.id.tvNomInfo);
        tvAuteur = view.findViewById(R.id.tvAuteur);
        tvDesc = view.findViewById(R.id.tvDesc);
        tvCat = view.findViewById(R.id.tvCat);
        tvVendeurInfo = view.findViewById(R.id.tvVendeurInfo);
        tvStat = view.findViewById(R.id.tvStat);
        tvPrixInfo = view.findViewById(R.id.tvPrixInfo);


        tvNomInfo.setText("Titre: "+args[0]);
        tvAuteur.setText("Auteur: "+args[1]);
        tvDesc.setText("Description: "+args[2]);
        tvCat.setText("Catégorie: "+args[3]);
        tvVendeurInfo.setText("Vendeur: "+args[4]);
        tvStat.setText("Statut: "+args[5]);
        tvPrixInfo.setText("Prix :$"+args[6]);


        builder.setView(view)
                .setTitle("Informations du livre")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setNeutralButton("Réserver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Réservation réussie", Toast.LENGTH_LONG).show();
                        SingletonSession.Instance().modLivre(Integer.parseInt(args[7]), args[0], Double.parseDouble(args[6]), args[1], args[3], args[2], 'R', args[0]);
                        dismiss();
                    }
                });

        return builder.create();
    }

    /*@Override
    public void onResume()
    {
        super.onResume();
    }*/

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (InfoDialog.InfoDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " doit implementer LoginDialogListener");
        }
    }*/

   /* public interface InfoDialogListener{
        void setInfo(String nomLivre, String auteur, String desc, String cat, String vendeur, String stat, String prix);
    }*/

    /*public void setTvNomInfo(String tvNomInfo) {
        this.tvNomInfo.setText(tvNomInfo);
    }

    public void setTvAuteur(String tvAuteur) {
        this.tvAuteur.setText(tvAuteur);
    }

    public void setTvDesc(String tvDesc) {
        this.tvDesc.setText(tvDesc);
    }

    public void setTvCat(String tvCat) {
        this.tvCat.setText(tvCat);
    }

    public void setTvVendeurInfo(String tvVendeurInfo) {
        this.tvVendeurInfo.setText(tvVendeurInfo);
    }

    public void setTvStat(String tvStat) {
        this.tvStat.setText(tvStat);
    }

    public void setTvPrixInfo(String tvPrixInfo) {
        this.tvPrixInfo.setText(tvPrixInfo);
    }*/
}

