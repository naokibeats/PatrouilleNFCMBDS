package com.mbds.material.PatrouilleNFC;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mbds.material.PatrouilleNFC.Fragments.FragmentScan;
import com.mbds.material.PatrouilleNFC.Help.DefaultHelp;
import com.mbds.material.PatrouilleNFC.Utils.Nfc.NFCManager;
import com.mbds.material.PatrouilleNFC.Utils.PicassoTransform.CircleTransformWhite;
import com.mbds.material.PatrouilleNFC.Utils.PnToast;
import com.mbds.material.PatrouilleNFC.Utils.SessionManager;
import com.mbds.material.PatrouilleNFC.Fragments.FragmentSearch;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewAdapters.DrawerAdapter;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses.DrawerItem;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewUtils.ItemClickSupport;
import com.mbds.material.PatrouilleNFC.Utils.JsonParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class MainActivity extends ActionBarActivity {
    final Context context = this;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    SharedPreferences sharedPreferences;
    ScrollView scrollView;
    ActionBarDrawerToggle mDrawerToggle;
    int theme, scrollPositionX = 0, scrollPositionY = -100;
    Intent intent;
    FrameLayout statusBar;
    SharedPreferences.Editor editor;
    ActivityOptions options;
    TextView textViewName, textViewTagId;
    ImageView imageViewToogle, imageViewCover, imageViewPicture;
    ToggleButton toggleButtonDrawer;
    RelativeLayout relativeLayoutHelp , relativeLayoutSettings, relativeLayoutLogout, relativeLayoutVoirProfil;
    LinearLayout linearLayoutMain, linearLayoutSecond;
    String urlName = "", urlPost = "";
    String urlProfile = "https://graph.facebook.com/" + urlName;
    String urlPicture = "https://graph.facebook.com/" + urlName + "picture?type=large&redirect=false";
    String urlCover = "https://graph.facebook.com/" + urlName + "cover";
    String facebookID, name, tagId, cover, picture;
    Bitmap bitmapPicture, bitmapCover;
    Drawable drawablePicture, drawableCover;
    File file, folder;
    Boolean downloaded, error = false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    String[] newsTitle, newsExcerpt, newsImage, newsContent, drawerArray;
    RecyclerView recyclerViewDrawer;
    int postNumber = 99;
    JSONObject jsonObjectNewsPosts;
    JSONArray jsonArrayNewsContent;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.Adapter adapterDrawer;
    private RecyclerView.LayoutManager layoutManagerDrawer;
    private boolean mActionBarAutoHideEnabled = false;
    private int mActionBarAutoHideSensivity = 0;
    private int mActionBarAutoHideMinY = 0;
    private int mActionBarAutoHideSignal = 0;
    private boolean mActionBarShown = true;
    private String tagenCours = "";
    NFCManager managernfc;
    String privilege;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        theme();

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        HashMap<String, String> user = session.getUserDetails();
        String fullname = user.get(SessionManager.KEY_NAME);
        name=fullname;
        String tag = user.get(SessionManager.KEY_MATR);
        tagId= tag;
         privilege = user.get(SessionManager.KEY_PRIVILEGE);
        if(privilege != null && privilege.equals("civil")){
            intent = new Intent(MainActivity.this, ProfilDetailActivity.class);
            intent.putExtra("TAG_SCAN_ID",tagId);
            startActivity(intent);
        }
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(fullname);
        textViewTagId = (TextView) findViewById(R.id.textViewTagId);
        textViewTagId.setText(tagId);
        toolbarStatusBar();
        setFragment(sharedPreferences.getInt("FRAGMENT", 0));
        navigationDrawer();
        toogleButtonDrawer();
        openSettings();
        openHelp();
        logOut();


        managernfc = new NFCManager(this);
        managernfc.onActivityCreate();
        managernfc.setOnTagReadListener(new NFCManager.TagReadListener() {
            @Override
            public void onTagRead(String tagRead) {
                if(tagRead.contains(("en"))){
                    tagRead = tagRead.split("en")[1];
                }
                new TestPersonTask().execute(tagRead);
            }
        });
    }
    class TestPersonTask extends AsyncTask<String, Void, Boolean> {
        boolean errorconnexion = false;
        @Override
        protected Boolean doInBackground(String... params) {
            try{
                if(Utilitaire.checkValidPerson(params[0])){
                    tagenCours = params[0];
                    return true;
                }
                return false;
            }
            catch(IOException e){
                errorconnexion = true;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if(errorconnexion){
                PnToast.show(context, "Erreur de connexion", true);
            }
            else if(true){
                intent = new Intent(MainActivity.this, ProfilDetailActivity.class);
                intent.putExtra("TAG_SCAN_ID",tagenCours);
                startActivity(intent);
            }
            else{
                PnToast.show(context, "Numero de Tag non valide", true);
            }
        }
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
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.about_dialog);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("DOWNLOAD", false);
        editor.apply();

        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }

    public void navigationDrawer() {

        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
        facebookID = sharedPreferences.getString("FACEBOOKID", "javiersegoviacordoba");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        downloaded = sharedPreferences.getBoolean("DOWNLOAD", false);

        View drawer = findViewById(R.id.scrimInsetsFrameLayout);
        ViewGroup.LayoutParams layoutParams = drawer.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.width = displayMetrics.widthPixels - (56 * Math.round(displayMetrics.density));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = displayMetrics.widthPixels + (20 * Math.round(displayMetrics.density)) - displayMetrics.widthPixels / 2;
        }


        file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/picture.png");
        if (file.length() != 0) {
            imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
            imageViewPicture.setImageDrawable(Drawable.createFromPath(file.toString()));
        }
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/cover.png");
        if (file.length() != 0) {
            imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
            imageViewCover.setImageDrawable(Drawable.createFromPath(file.toString()));
        }

        if (!downloaded) {

            new AsyncTaskFacebookParseJson().execute(facebookID);

        } else {
           // Toast.makeText(MainActivity.this, downloaded.toString(), Toast.LENGTH_SHORT).show();
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        TypedValue typedValueStatusBarColor = new TypedValue();
        MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueStatusBarColor, true);
        final int colorStatusBar = typedValueStatusBarColor.data;
        mDrawerLayout.setStatusBarBackgroundColor(colorStatusBar);

        recyclerViewDrawer = (RecyclerView) findViewById(R.id.recyclerViewDrawer);
        recyclerViewDrawer.setHasFixedSize(true);
        recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        final String[] drawerTitles = getResources().getStringArray(R.array.drawer);
        final TypedArray drawerIcons = getResources().obtainTypedArray(R.array.drawerIcons);
        int nbloop = drawerTitles.length;
        if(privilege !=null && privilege.equals("controleur")){
           nbloop = 1;

        }
        for (int i = 0; i < nbloop; i++) {

            drawerItems.add(new DrawerItem(drawerTitles[i], drawerIcons.getDrawable(i)));
        }
        drawerIcons.recycle();
        adapterDrawer = new DrawerAdapter(drawerItems);
        recyclerViewDrawer.setAdapter(adapterDrawer);
        final TypedValue typedValue = new TypedValue();
        MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.data;


        recyclerViewDrawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int nbloop = drawerTitles.length;
                if(privilege !=null && privilege.equals("controleur")){
                    nbloop = 1;

                }

                for (int i = 0; i < nbloop; i++) {
                    if (i == sharedPreferences.getInt("FRAGMENT", 0)) {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                    } else {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }
                }

                recyclerViewDrawer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerViewDrawer);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, final int position, long id) {

                int nbloop = drawerTitles.length;
                if(privilege !=null && privilege.equals("controleur")){
                    nbloop = 1;

                }
                for (int i = 0; i < nbloop; i++) {
                    if (i == position) {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                    } else {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setFragment(position);
                    }
                }, 250);
                mDrawerLayout.closeDrawers();
            }
        });
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

    public void toogleButtonDrawer() {
        imageViewToogle = (ImageView) findViewById(R.id.imageViewToggle);
        toggleButtonDrawer = (ToggleButton) findViewById(R.id.toggleButtonDrawer);
        linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayoutMain);
        linearLayoutSecond = (LinearLayout) findViewById(R.id.linearLayoutSecond);
        toggleButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggleButtonDrawer.isChecked()) {
                    toggleButtonDrawer.setChecked(false);
                    imageViewToogle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_drop_down));
                    linearLayoutMain.setVisibility(View.VISIBLE);
                    linearLayoutSecond.setVisibility(View.GONE);
                }
                if (toggleButtonDrawer.isChecked()) {
                    toggleButtonDrawer.setChecked(true);
                    imageViewToogle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_drop_up));
                    linearLayoutMain.setVisibility(View.GONE);
                    linearLayoutSecond.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("DOWNLOAD", false);
        editor.apply();

    }

    private void openSettings() {
        relativeLayoutSettings = (RelativeLayout) findViewById(R.id.relativeLayoutSettings);
        relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
            }
        });
    }

    private void openHelp(){
        relativeLayoutHelp = (RelativeLayout) findViewById(R.id.relativeLayoutHelp);
        relativeLayoutHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, DefaultHelp.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
            }
        });    }
    private void logOut() {

        relativeLayoutLogout = (RelativeLayout) findViewById(R.id.relativeLayoutLogout);
        relativeLayoutVoirProfil = (RelativeLayout) findViewById(R.id.voirmoncompte);
        relativeLayoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
            }
        });
        relativeLayoutVoirProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ProfilDetailActivity.class);
                intent.putExtra("TAG_SCAN_ID",tagId);
                startActivity(intent);
            }
        });

    }

    public class AsyncTaskFacebookParseJson extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... url) {

            urlName = url[0];
            urlProfile = "https://graph.facebook.com/" + urlName;
            urlPicture = "https://graph.facebook.com/" + urlName + "/picture?type=large&redirect=false";
            urlCover = "https://graph.facebook.com/" + urlName + "/?fields=cover";

            JSONObject jsonObjectProfile, jsonObjectPicture, jsonObjectCover;
            try {
                jsonObjectProfile = JsonParser.readJsonFromUrl(urlProfile);
                jsonObjectPicture = JsonParser.readJsonFromUrl(urlPicture);
                jsonObjectCover = JsonParser.readJsonFromUrl(urlCover);

                // Storing each json item_post in variable
               // name = jsonObjectProfile.getString("name");
                //username = "Facebook ID: " + jsonObjectProfile.getString("username");
                //picture = jsonObjectPicture.getJSONObject("data").getString("url");
                //cover = jsonObjectCover.getJSONObject("cover").getString("source");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            textViewName = (TextView) findViewById(R.id.textViewName);
            textViewName.setText(name);
            textViewTagId = (TextView) findViewById(R.id.textViewTagId);
            textViewTagId.setText(tagId);

            imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
            imageViewCover = (ImageView) findViewById(R.id.imageViewCover);

            sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("NAME", name);
            editor.putString("TAGID", tagId);
            editor.apply();

            folder = new File(Environment.getExternalStorageDirectory() + "/MaterialDesignApp");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Target targetPicture = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/picture.png");
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                                ostream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            Target targetCover = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/cover.png");
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                                ostream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            Picasso.with(context).load(picture).transform(new CircleTransformWhite()).into(targetPicture);
            Picasso.with(context).load(cover).into(targetCover);

            imageViewPicture.setTag(targetPicture);
            imageViewCover.setTag(targetCover);

            Picasso.with(context).load(picture).placeholder(imageViewPicture.getDrawable()).transform(new CircleTransformWhite()).into(imageViewPicture);
            Picasso.with(context).load(cover).placeholder(imageViewCover.getDrawable()).into(imageViewCover);

            sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean("DOWNLOAD", true);
            editor.apply();
        }
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                sharedPreferences.edit().putInt("FRAGMENT", 0).apply();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentScan fragmentScan = new FragmentScan();
                fragmentTransaction.replace(R.id.fragment, fragmentScan);
                fragmentTransaction.commit();
                break;
            case 1:
                sharedPreferences.edit().putInt("FRAGMENT", 1).apply();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSearch fragmentSearch = new FragmentSearch();
                fragmentTransaction.replace(R.id.fragment, fragmentSearch);
                fragmentTransaction.commit();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        managernfc.onActivityResume();
    }

    @Override
    public void onPause() {
        managernfc.onActivityPause();
        super.onPause();
    }


    public void onNewIntent(Intent intent){
        managernfc.onActivityNewIntent(intent);
    }

}
