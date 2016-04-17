package com.mbds.material.PatrouilleNFC;

import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.*;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.mbds.material.PatrouilleNFC.Model.InfoPersonne;
import com.mbds.material.PatrouilleNFC.Utils.PnToast;
import com.mbds.material.PatrouilleNFC.Utils.SessionManager;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by Safidimahefa on 12/04/2016.
 */
public class ProfilDetailActivity extends AppCompatActivity {
    final Context context = this;
    Toolbar toolbar;
    Intent intent;
    FrameLayout statusBar;
    SharedPreferences sharedPreferences;
    int theme, scrollPositionX = 0, scrollPositionY = -100;
    String tagID;
    SessionManager session;
    InfoPersonne info;
    LinearLayout backutilisateur;
    @Override
        protected void onCreate(Bundle savedInstanceState) {

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
            super.onCreate(savedInstanceState);
            theme();
            setContentView(R.layout.activity_detail);

            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                tagID = extras.getString("TAG_SCAN_ID");
                try {
                     info=  new FindPersonneTask().execute(tagID).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        backutilisateur = (LinearLayout)findViewById(R.id.layoututilisateur);
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();

        drawable.addFrame(new ColorDrawable(Color.RED), 400);
        drawable.addFrame(new ColorDrawable(Color.BLUE), 400);
        drawable.setOneShot(false);
        if(info.getAvis_recherche() == 1) {
            String message= "Cette personne est recherchée";
            final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);
            dialogBuilder
                    .withTitle("Attention")
                    .withTitleColor("#FFFFFF")
                    .withDividerColor("#11000000")
                    .withMessage(message)
                    .withMessageColor("#FFFFFFFF")
                    .withDialogColor("#FFE74C3C")
                    .withIcon(getResources().getDrawable(R.mipmap.ic_launcher1))
                    .withDuration(700)
                    .withEffect(Effectstype.Shake)
                    .withButton1Text("OK")
                    .isCancelableOnTouchOutside(false)
                    .setCustomView(R.layout.alert_dialog,this)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                dialogBuilder.dismiss();
                            } else {
                                Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                dialogBuilder.dismiss();
                            }
                        }
                    })
                    .show();
            backutilisateur.setBackgroundDrawable(drawable);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawable.start();
                }
            }, 10);
        }
            TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
            TabHost.TabSpec tab1 = tabHost.newTabSpec("Information");
            TabHost.TabSpec tab2 = tabHost.newTabSpec("Permis");
            TabHost.TabSpec tab3 = tabHost.newTabSpec("Infraction");

            tab1.setIndicator("Info");
            Intent tabfirst = new Intent(this,TabProfil.class);
            tabfirst.putExtra("PERSONNE_INFO",info);
            tab1.setContent(tabfirst);


            tab2.setIndicator("Permis");
            Intent tabsecond = new Intent(this, TabPermis.class);
            tabsecond.putExtra("PERSONNE_INFO",info);
            tab2.setContent(tabsecond);


            tab3.setIndicator("Infraction");
            Intent tabthird = new Intent(this, TabInfraction.class);
            tabthird.putExtra("PERSONNE_INFO", info);
            tab3.setContent(tabthird);


            LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
            mLocalActivityManager.dispatchCreate(savedInstanceState);
            tabHost.setup(mLocalActivityManager);
            tabHost.addTab(tab1);
                tabHost.addTab(tab2);
                tabHost.addTab(tab3);
                toolbarStatusBar();
                navigationBarStatusBar();
        FloatingActionButton imageView=(FloatingActionButton)findViewById(R.id.fab);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ProfilDetailActivity.this, SaisieInfraction.class);
                intent.putExtra("ID_PERSONNE", ""+info.getId_personne());
                startActivity(intent);

            }
        });


    }
    class FindPersonneTask extends AsyncTask<String, Void, InfoPersonne> {
        boolean errorconnexion = false;
        @Override
        protected InfoPersonne doInBackground(String... params) {
            try{

                InfoPersonne infoPersonnep = Utilitaire.bindInfoPersonneByTag(tagID);
                return infoPersonnep;
            }

            catch(IOException e){
                errorconnexion = true;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(InfoPersonne result) {
            if(errorconnexion){
                PnToast.show(context, "Erreur de connexion", true);
            }
            else if(result != null){
                TextView profilname = (TextView) findViewById(R.id.user_profile_name);
                profilname.setText(result.getNom() + " " + result.getPrenom());
                TextView professsion = (TextView) findViewById(R.id.user_profile_short_bio);
                professsion.setText(result.getProfession());
            }
            else{
                PnToast.show(context, "Profil introuvable", true);
            }
        }
    }
    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
    @Override
    public void onBackPressed() {
        HashMap<String, String> user = session.getUserDetails();
        String privilege = user.get(SessionManager.KEY_PRIVILEGE);
        if(privilege != null && privilege.equals("civil")){
            session.logoutUser();
        }
        else{
            intent = new Intent(ProfilDetailActivity.this, MainActivity.class);

        }
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Dialog dialog = new Dialog(ProfilDetailActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.about_dialog);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        HashMap<String, String> user = session.getUserDetails();
        String privilege = user.get(SessionManager.KEY_PRIVILEGE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Information");
        if(privilege != null && privilege.equals("civil")) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

        }
        else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }
    public void navigationBarStatusBar() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ProfilDetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                ProfilDetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ProfilDetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                ProfilDetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
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
