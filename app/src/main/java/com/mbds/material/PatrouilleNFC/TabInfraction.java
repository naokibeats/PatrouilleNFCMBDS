package com.mbds.material.PatrouilleNFC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mbds.material.PatrouilleNFC.Model.InfoPersonne;
import com.mbds.material.PatrouilleNFC.Model.Infraction;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;

import java.util.List;

/**
 * Created by Safidimahefa on 14/04/2016.
 */
public class TabInfraction extends Activity {
    ListView mListView;
    Context ctx;

    String[] erreur = new String[]{
            "Aucun résultat trouvé"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_infraction);
        ctx=this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent i = getIntent();
            InfoPersonne pers = (InfoPersonne)i.getSerializableExtra("PERSONNE_INFO");
            if(pers.getInfractions() !=null) {
                List<Infraction> sesinfractions = pers.getInfractions();
                if (sesinfractions != null && sesinfractions.size() > 0) {
                    String[] listes = new String[sesinfractions.size()];


                    for (int cursor = 0; cursor < sesinfractions.size(); cursor++) {
                        int nb = cursor + 1;

                        String etat = (sesinfractions.get(cursor).getEtat() == 0) ? " NON PAYÉ" : " PAYÉ";

                        listes[cursor] = nb + ". " + sesinfractions.get(cursor).getLibelle() + " le " + Utilitaire.formatterDateString(sesinfractions.get(cursor).getDate_infraction()) +etat;

                    }
                    mListView = (ListView) findViewById(R.id.listinfraction);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TabInfraction.this,
                            android.R.layout.simple_list_item_1, listes);
                    mListView.setAdapter(adapter);
                } else {
                    mListView = (ListView) findViewById(R.id.listinfraction);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TabInfraction.this,
                            android.R.layout.simple_list_item_1, erreur);
                    mListView.setAdapter(adapter);
                }
            }
        }



    }
}
