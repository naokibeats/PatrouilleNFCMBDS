package com.mbds.material.PatrouilleNFC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mbds.material.PatrouilleNFC.Model.InfoPersonne;
import com.mbds.material.PatrouilleNFC.Utils.PnToast;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by Safidimahefa on 13/04/2016.
 */
public class TabProfil  extends Activity
{

    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.info_profil);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent i = getIntent();
            InfoPersonne pers = (InfoPersonne)i.getSerializableExtra("PERSONNE_INFO");
            String datenaissance = pers.getDate_naiss();
            TextView datenaiss = (TextView) findViewById(R.id.datenaissance);
            datenaiss.setText(Html.fromHtml("<b><u>Date naissance:</u> </b>" + Utilitaire.formatterDateString(datenaissance)));
            TextView sexe = (TextView) findViewById(R.id.sexe);
            sexe.setText(Html.fromHtml("<b><u>Sexe:</u> </b>"+pers.getSexe()));
            TextView adresse= (TextView) findViewById(R.id.adresse);
            adresse.setText(Html.fromHtml("<b><u>Adresse:</u> </b>"+pers.getAdresse()));
            TextView lieunaissance = (TextView) findViewById(R.id.lieu);
            lieunaissance.setText(Html.fromHtml("<b><u>Lieu de naissance:</u> </b>"+pers.getLieu_naiss()));
            TextView situationmat = (TextView) findViewById(R.id.situation);
            situationmat.setText(Html.fromHtml("<b><u>Situation matrimoniale:</u> </b>"+pers.getSituation_mat()));
            TextView pere = (TextView) findViewById(R.id.nompere);
            pere.setText(Html.fromHtml("<b><u>Nom père:</u> </b>" +pers.getNom_pere()));
            TextView mere = (TextView) findViewById(R.id.nommere);
            mere.setText(Html.fromHtml("<b><u>Nom mère:</u> </b>" +pers.getNom_mere()));
            TextView cin = (TextView) findViewById(R.id.cin);
            cin.setText(Html.fromHtml("<b><u>CIN:</u> </b>" +pers.getImmatriculation()));
        }

    }

}
