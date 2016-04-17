package com.mbds.material.PatrouilleNFC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.mbds.material.PatrouilleNFC.Adapters.SearchCardAdapter;
import com.mbds.material.PatrouilleNFC.Model.Personne;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewUtils.ItemClickSupport;
import com.mbds.material.PatrouilleNFC.Utils.SessionManager;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Safidimahefa on 17/04/2016.
 */
public class ResultatRechercheActivity extends ActionBarActivity {
    SessionManager session;
    SharedPreferences.Editor editor;
    ScrollView scrollView;
    int theme, scrollPositionX = 0, scrollPositionY = -100;
    SharedPreferences sharedPreferences;
    Intent intent;
    Boolean homeButton = false, themeChanged;
    FrameLayout statusBar, frameLayoutSwitch, frameLayoutCheckBox, frameLayoutRadioButton;
    Toolbar toolbar;
    private ListView listView;
    SearchCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        setContentView(R.layout.recherche_resultat);

        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));
        adapter = new SearchCardAdapter(getApplicationContext(), R.layout.list_view);
        ArrayList<String> listeRecherche = getIntent().getStringArrayListExtra("LISTE_RECHERCHE");
        if(listeRecherche == null){
            Personne personne = new Personne ("Aucun", "Résultat trouvé", "0 résultat");
            adapter.add(personne);
        }
        else{
            List<Personne> liste = Utilitaire.decoderStringPersonne(listeRecherche);
            for(int i = 0; i< liste.size(); i++){
                adapter.add(liste.get(i));
            }
        }
        listView.setAdapter(adapter);
        if(listeRecherche!=null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Personne personne = (Personne) parent.getItemAtPosition(position);
                    Intent i = new Intent(ResultatRechercheActivity.this, ProfilDetailActivity.class);
                    i.putExtra("TAG_SCAN_ID", personne.getNumero_tag());
                    startActivity(i);
                }
            });
        }
        toolbarStatusBar();
        navigationBarStatusBar();
        themeChanged();
    }
    @Override
    public void onBackPressed() {
        intent = new Intent(ResultatRechercheActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void themeChanged() {
        themeChanged = sharedPreferences.getBoolean("THEMECHANGED",false);
        homeButton = true;
    }
    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Résultat recherche");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setThemeFragment(int theme) {
        switch (theme) {
            case 1:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 1).apply();
                break;
            case 2:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 2).apply();
                break;
            case 3:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 3).apply();
                break;
            case 4:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 4).apply();
                break;
            case 5:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 5).apply();
                break;
            case 6:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 6).apply();
                break;
            case 7:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 7).apply();
                break;
            case 8:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 8).apply();
                break;
            case 9:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 9).apply();
                break;
            case 10:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 10).apply();
                break;
        }
    }
    public void navigationBarStatusBar() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ResultatRechercheActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                ResultatRechercheActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ResultatRechercheActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                ResultatRechercheActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }
    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }
    public void settingTheme(int theme) {
        switch (theme) {
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            case 7:
                setTheme(R.style.AppTheme7);
                break;
            case 8:
                setTheme(R.style.AppTheme8);
                break;
            case 9:
                setTheme(R.style.AppTheme9);
                break;
            case 10:
                setTheme(R.style.AppTheme10);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
    }
}
