package com.mbds.material.PatrouilleNFC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;


import com.mbds.material.PatrouilleNFC.Model.InfoPersonne;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;



/**
 * Created by Safidimahefa on 13/04/2016.
 */
public class TabPermis  extends Activity
{
    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_permis);
        ctx = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent i = getIntent();
            InfoPersonne pers = (InfoPersonne)i.getSerializableExtra("PERSONNE_INFO");
            TextView datepermis = (TextView) findViewById(R.id.datepermis);
            if(pers.getDate_permis() !=null) {
                datepermis.setText(Html.fromHtml("<b><u>Date permis:</u> </b>" + Utilitaire.formatterDateString(pers.getDate_permis())));

                TextView categA = (TextView) findViewById(R.id.cata);
                String categorie = (pers.getCategorie_a() == 1) ? "<font color=\"#00b359\">OK</font>" : "<font color=\"red\">NON</font>";
                categA.setText(Html.fromHtml("<b><u>Catégorie A:</u> </b>" + categorie));

                TextView categB = (TextView) findViewById(R.id.catb);
                categorie = (pers.getCategorie_b() == 1) ? "<font color=\"#00b359\">OK</font>" : "<font color=\"red\">NON</font>";
                categB.setText(Html.fromHtml("<b><u>Catégorie B:</u> </b>" + categorie));

                TextView categC = (TextView) findViewById(R.id.catc);
                categorie = (pers.getCategorie_c() == 1) ? "<font color=\"#00b359\">OK</font>" : "<font color=\"red\">NON</font>";
                categC.setText(Html.fromHtml("<b><u>Catégorie C:</u> </b>" + categorie));


                TextView categD = (TextView) findViewById(R.id.catd);
                categorie = (pers.getCategorie_d() == 1) ? "<font color=\"#00b359\">OK</font>" : "<font color=\"red\">NON</font>";
                categD.setText(Html.fromHtml("<b><u>Catégorie D:</u> </b>" + categorie));


                TextView categE = (TextView) findViewById(R.id.cate);
                categorie = (pers.getCategorie_e() == 1) ? "<font color=\"#00b359\">OK</font>" : "<font color=\"red\">NON</font>";
                categE.setText(Html.fromHtml("<b><u>Catégorie E:</u> </b>" + categorie));


                TextView categF = (TextView) findViewById(R.id.catf);
                categorie = (pers.getCategorie_f() == 1) ? "<font color=\"#00b359\">OK</font>" : "<font color=\"red\">NON</font>";
                categF.setText(Html.fromHtml("<b><u>Catégorie F:</u> </b>" + categorie));
            }




        }
    }
}
