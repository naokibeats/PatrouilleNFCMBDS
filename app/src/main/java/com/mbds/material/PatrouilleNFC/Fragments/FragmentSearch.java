package com.mbds.material.PatrouilleNFC.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.mbds.material.PatrouilleNFC.MainActivity;
import com.mbds.material.PatrouilleNFC.Model.Personne;

import com.mbds.material.PatrouilleNFC.ResultatRechercheActivity;

import com.mbds.material.PatrouilleNFC.Utils.PnToast;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FragmentSearch extends Fragment {


    SharedPreferences sharedPreferences;
    View view;
    Toolbar toolbar;
    FrameLayout statusBar;
    Spinner spinner;
    String sexeencours;
    Intent intent;
    TextView nom;
    TextView prenom;
    TextView numerotag;
    TextView cin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.recherche_multi, container, false);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        statusBar = (FrameLayout) getActivity().findViewById(R.id.statusBar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Recherche");


        spinner = (Spinner) view.findViewById(R.id.spinnerrecherche);


        List<String> sexe = new ArrayList<String>();
        sexe.add("");
        sexe.add("Homme");
        sexe.add("Femme");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(container.getContext(), R.layout.spinner_layout, sexe);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        sexeencours = parent.getItemAtPosition(position).toString();

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        spinner.setAdapter(dataAdapter);
        nom = (TextView) view.findViewById(R.id.nomrecherche);
        prenom = (TextView) view.findViewById(R.id.prenomrecherche);
        numerotag = (TextView) view.findViewById(R.id.tagrecherche);
        cin = (TextView) view.findViewById(R.id.cinrecherche);
        FloatingActionButton btn=(FloatingActionButton) view.findViewById(R.id.rechercher);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Personne critere = new Personne();

                critere.setNom(nom.getText().toString());
                critere.setPrenom(prenom.getText().toString());
                critere.setSexecritere(sexeencours);
                critere.setNumero_tag(numerotag.getText().toString());
                critere.setImmatriculation(cin.getText().toString());

                try {
                    new RecherchePersonneTask().execute(critere).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    class RecherchePersonneTask extends AsyncTask<Personne, Void, ArrayList<String>>{
        boolean errorconnexion;
        @Override
        protected ArrayList<String> doInBackground(Personne... personnes) {


            try {
                return Utilitaire.rechercheMulticritere(personnes[0]);
            }
            catch (IOException e){
                errorconnexion = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<String> result){{
            if(errorconnexion){
                PnToast.show(view.getContext(), "Erreur de connexion", true);
            }else{
                intent = new Intent(view.getContext(), ResultatRechercheActivity.class);
                intent.putStringArrayListExtra("LISTE_RECHERCHE",result);
                startActivity(intent);
            }


        }}
    }


}