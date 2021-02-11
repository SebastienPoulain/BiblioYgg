package com.annequinpoulain.biblioygg;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GestionLivresFragment extends Fragment {

    public GestionLivresFragment() {
        // Required empty public constructor
    }


    private RecyclerView recyclerView;
    private AdapterListe adapter;
    private List<Livre> listeL;
    FloatingActionButton btAdd;
    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_gestion_livres, container, false);

        SingletonSession.Instance().setActivateRecyclerButtons(true);

        recyclerView = RootView.findViewById(R.id.livreListe);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(RootView.getContext(),LinearLayoutManager.VERTICAL,false));

        listeL = SingletonSession.Instance().getLivresByUserEmail(SingletonSession.Instance().getUser().getEmail());

        if (!listeL.isEmpty()) {
            adapter = new AdapterListe(listeL);
            recyclerView.setAdapter(adapter);
        }

        btAdd = RootView.findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent (view.getContext(), UserLivres.class);
                startActivityForResult(intent, 11);
            }
        });

        // Inflate the layout for this fragment
        return RootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11)
        {
            refreshFrag();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        refreshFrag();
    }

    public void refreshFrag(){
        SingletonSession.Instance().setPosModif(-1);
        SingletonSession.Instance().setActivateRecyclerButtons(true);
        this.listeL = SingletonSession.Instance().getLivresByUserEmail(SingletonSession.Instance().getUser().getEmail());

        if (!listeL.isEmpty()) {
            this.adapter = new AdapterListe(listeL);
            this.recyclerView.setAdapter(adapter);
            this.adapter.onActivityResult(11,1);
        }

    }

}
