package com.annequinpoulain.biblioygg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterListe extends RecyclerView.Adapter<AdapterListe.MonViewHolder> {
    private List<Livre> listeLivres;
    private boolean activate = false;

    public AdapterListe(List<Livre> liste){
        listeLivres = liste;
    }

    @NonNull
    @Override
    public AdapterListe.MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.liste,parent,false);
        return new MonViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MonViewHolder holder,int position){
        holder.tvNom.setText(listeLivres.get(position).getNom());
        holder.tvPrix.setText(String.valueOf(listeLivres.get(position).getPrix()));
        holder.tvVendeur.setText("Vendeur: " + SingletonSession.Instance().getUserByEmail(listeLivres.get(position).getEmail()).getPrenom() + " " + SingletonSession.Instance().getUserByEmail(listeLivres.get(position).getEmail()).getNom());

        Picasso.get()
                .load(RetrofitInstance.getInstance().baseUrl() + listeLivres.get(position).getImgpath())
                .resize(220, 220)
                .centerCrop()
                .into(holder.img);

        activate = SingletonSession.Instance().getActivateRecyclerButton();

        if (activate) {
            holder.btModif.setVisibility(View.VISIBLE);
            holder.btDel.setVisibility(View.VISIBLE);
        } else {
            holder.btModif.setVisibility(View.INVISIBLE);
            holder.btDel.setVisibility(View.INVISIBLE);
        }
    }

    public void filtrerListe(ArrayList<Livre> listeNom ){
        listeLivres = listeNom;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return listeLivres.size();
    }




    public class MonViewHolder extends RecyclerView.ViewHolder{
        TextView tvNom, tvNomInfo, tvPrix, tvPrixInfo, tvVendeur, tvVendeurInfo, tvAuteur, tvCat, tvDesc, tvStat, tvDol;
        ImageButton btModif, btDel;
        LinearLayout layout1;
        ImageView img;

        AlertDialog.Builder builder;
        AlertDialog.Builder builderInfo;



        public MonViewHolder(View view) {
            super(view);

            tvAuteur = view.findViewById(R.id.tvAuteur);
            tvCat = view.findViewById(R.id.tvCat);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvStat = view.findViewById(R.id.tvStat);
            tvNom = view.findViewById(R.id.tvNom);
            tvPrix = view.findViewById(R.id.tvPrix);
            tvVendeur = view.findViewById(R.id.tvVendeur);
            btModif = view.findViewById(R.id.modify);
            btDel = view.findViewById(R.id.delete);
            layout1 = view.findViewById(R.id.layout1);
            tvDol = view.findViewById(R.id.textView4);
            tvNomInfo = view.findViewById(R.id.tvNomInfo);
            tvPrixInfo = view.findViewById(R.id.tvPrixInfo);
            tvVendeurInfo = view.findViewById(R.id.tvVendeurInfo);

            img = view.findViewById(R.id.livreImg);
            builder = new AlertDialog.Builder(view.getContext());
            builderInfo = new AlertDialog.Builder(view.getContext());

            btModif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SingletonSession.Instance().setPosModif(listeLivres.get(getAdapterPosition()).getId());
                    Intent intent = new Intent(view.getContext(), UserLivres.class);
                    ((Activity)view.getContext()).startActivityForResult(intent, 11);
                }
            });

            btDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    builder.setTitle("Confirmer la suppression");
                    builder.setMessage("Voulez-vous vraiement supprimer le livre \"" + tvNom.getText().toString() + "\"");
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SingletonSession.Instance().rmLivre(listeLivres.get(getAdapterPosition()).getNom());
                            listeLivres.remove(getAdapterPosition());
                            notifyItemChanged(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyDataSetChanged();
                            Dialog dialog  = (Dialog) dialogInterface;
                            Context context = dialog.getContext();
                            Toast.makeText(context, "Supression réussie", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Dialog dialog  = (Dialog) dialogInterface;
                            Context context = dialog.getContext();
                            Toast.makeText(context, "Supression annulée", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();
                }
            });



            layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //List<String> infos = new ArrayList<String>();
                    String statut;

                    if (listeLivres.get(getAdapterPosition()).getStat() == 'R')
                        statut = "Réservé";
                    else
                        statut = "Disponible";

                    String[] infos = {listeLivres.get(getAdapterPosition()).getNom(), listeLivres.get(getAdapterPosition()).getAuteur(),
                            listeLivres.get(getAdapterPosition()).getDescription(), listeLivres.get(getAdapterPosition()).getCat(),
                            SingletonSession.Instance().getUserByEmail(listeLivres.get(getAdapterPosition()).getEmail()).getPrenom() + " " + SingletonSession.Instance().getUserByEmail(listeLivres.get(getAdapterPosition()).getEmail()).getNom(),
                    statut, String.valueOf(listeLivres.get(getAdapterPosition()).getPrix()), String.valueOf(listeLivres.get(getAdapterPosition()).getId())};

                    InfoDialog info = new InfoDialog();

                    Bundle args = new Bundle();
                    args.putStringArray("infos", infos);

                    info.setArguments(args);


                    info.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "Info Dialog");


                }
            });
            tvPrix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            tvDol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


    public void onActivityResult(int requestCode, int resultCode) {

        notifyItemRangeChanged(0, this.getItemCount());
        this.notifyDataSetChanged();
    }
}
