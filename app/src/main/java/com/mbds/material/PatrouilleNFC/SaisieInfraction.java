package com.mbds.material.PatrouilleNFC;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.clans.fab.FloatingActionButton;
import com.mbds.material.PatrouilleNFC.Dialogs.ColorChooserDialog;
import com.mbds.material.PatrouilleNFC.Model.Infraction;
import com.mbds.material.PatrouilleNFC.Model.TypeInfraction;
import com.mbds.material.PatrouilleNFC.Utils.PnToast;
import com.mbds.material.PatrouilleNFC.Utils.SessionManager;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Safidimahefa on 14/04/2016.
 */
public class SaisieInfraction extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    final Context context = this;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ScrollView scrollView;
    ActionBarDrawerToggle mDrawerToggle;
    int theme, scrollPositionX = 0, scrollPositionY = -100;
    Intent intent;
    ActivityOptions options;
    TextView textViewName, textViewLink;
    EditText editTextFacebookID;
    ImageView imageViewToogle, imageViewCover, imageViewPicture, imageViewSend;
    ToggleButton toggleButtonDrawer;
    FrameLayout statusBar, frameLayoutSwitch, frameLayoutCheckBox, frameLayoutRadioButton;
    RelativeLayout relativeLayoutDrawerTexts, relativeLayoutChooseTheme;
    LinearLayout linearLayoutMain, linearLayoutSecond, linearLayoutSettings;
    String name, link, cover, picture, facebookID;
    Dialog dialog;
    Boolean homeButton = false, themeChanged;
    ViewGroup.LayoutParams layoutParamsStatusBar;
    SwitchCompat switchCompat;
    CheckedTextView checkBox;
    JSONArray jsonArrayNewsPost;
    Spinner spinner;
    List<String> listeDeroulante;
    TextView id;
    TextView remarque;
    String idpersonne;
    String deroulanteValue;
    SessionManager session;
    String idtagpersonne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        super.onCreate(savedInstanceState);

        theme();

        setContentView(R.layout.saisie_infraction);

        toolbarStatusBar();


        navigationBarStatusBar();

        themeChanged();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idpersonne = extras.getString("ID_PERSONNE");
        }
        id = (TextView) findViewById(R.id.identifiantsaisie);
        id.setText(idpersonne);
        checkBox = (CheckedTextView)findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.toggle();
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);


        try {
            List<TypeInfraction> liste=  new FindAllTypeInfraction().execute().get();
            if(liste !=null){
                listeDeroulante = new ArrayList<String>();
                for(int cursor = 0; cursor < liste.size(); cursor++){
                    String temp = liste.get(cursor).getId_type_infraction() + " - " +liste.get(cursor).getLibelle();
                    if(cursor==0) {
                        deroulanteValue = temp;
                    }
                    listeDeroulante.add(temp);
                }

            }else{
                PnToast.show(context, "Liste type infraction introuvable", true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, listeDeroulante);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        FloatingActionButton btn=(FloatingActionButton)findViewById(R.id.validatesanction);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int detention = (checkBox.isChecked()) ? 1 : 0;
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    idpersonne = extras.getString("ID_PERSONNE");

                    int idpers = Integer.parseInt(idpersonne);
                    if(idpersonne == null || idpersonne.isEmpty()){
                        PnToast.show(context, "ID PERSONNE VIDE", true);
                        return;
                    }
                    remarque = (TextView) findViewById(R.id.remarque);
                    String rmq = remarque.getText().toString();
                    int idderoulante = Integer.parseInt(deroulanteValue.split("-")[0].trim());
                    HashMap<String, String> user = session.getUserDetails();
                    String utilisateur = user.get(SessionManager.KEY_USER);
                    int idutilisateur = Integer.parseInt(utilisateur);
                    Infraction inf = new Infraction(idpers,idutilisateur,idderoulante,rmq,idutilisateur);
                    try{
                        new InsertSanctionTask().execute(inf).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
    class FindAllTypeInfraction extends AsyncTask<Void, Void, List<TypeInfraction>>{
        boolean errorconnexion = false;

        @Override
        protected  List<TypeInfraction> doInBackground(Void... voids) {
            try{
                List<TypeInfraction> listeinfraction = Utilitaire.getAllTypeInfraction();
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    idpersonne = extras.getString("ID_PERSONNE");
                }
                idtagpersonne = Utilitaire.getPersonneTagById(Integer.parseInt(idpersonne));
                return listeinfraction;
            }
            catch(IOException e){
                errorconnexion = true;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute( List<TypeInfraction> result){
            if(errorconnexion) {
                PnToast.show(context, "Erreur de connexion", true);
            }
        }
    }

    class InsertSanctionTask extends AsyncTask<Infraction, Void, Boolean>{
        boolean errorconnexion = false;

        @Override
        protected Boolean doInBackground(Infraction... infractions) {
            try{
                return Utilitaire.insertInfraction(infractions[0]);
            }
            catch (IOException e){
                errorconnexion = true;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Boolean result){
            if(errorconnexion){
                PnToast.show(context, "Erreur de connexion", true);
            }
            if(true){
                PnToast.show(context, "Sanction insérée", true);
                intent = new Intent(SaisieInfraction.this, ProfilDetailActivity.class);
                intent.putExtra("TAG_SCAN_ID",idtagpersonne);
                startActivity(intent);
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            intent = new Intent(SaisieInfraction.this, ProfilDetailActivity.class);
            intent.putExtra("TAG_SCAN_ID",idtagpersonne);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Dialog dialog = new Dialog(SaisieInfraction.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.about_dialog);
            dialog.show();
            return true;
        }
        if (id == android.R.id.home) {
            if (!homeButton) {
                NavUtils.navigateUpFromSameTask(SaisieInfraction.this);
            }
            if (homeButton) {
                if (!themeChanged) {
                    editor = sharedPreferences.edit();
                    editor.putBoolean("DOWNLOAD", false);
                    editor.apply();
                }
                intent = new Intent(SaisieInfraction.this, MainActivity.class);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(SaisieInfraction.this, ProfilDetailActivity.class);
        startActivity(intent);
    }

    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    private void themeChanged() {
        themeChanged = sharedPreferences.getBoolean("THEMECHANGED",false);
        homeButton = true;
    }



    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saisie infraction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void fixBooleanDownload() {

        editor = sharedPreferences.edit();
        editor.putBoolean("DOWNLOAD", true);
        editor.apply();
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
                SaisieInfraction.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                SaisieInfraction.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                SaisieInfraction.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                SaisieInfraction.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         deroulanteValue = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
