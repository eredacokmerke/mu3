package com.eredacokmerke.uygulama3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener, YeniKayitFragment.OnFragmentInteractionListener, YeniKlasorFragment.OnFragmentInteractionListener
{
    //private Context cnt;
    //private static Bundle _savedInstanceState;
    private Engine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //_savedInstanceState = savedInstanceState;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SabitYoneticisi.IZIN_WRITE_EXTERNAL_STORAGE);

        engine = new Engine();
        engine.init(MainActivity.this);
    }

    /**
     * uygulama acildigi zaman ilk yapilacak islemler
     *
     * @return hata olusursa false yoksa true doner
     */
    public boolean init()
    {
        //uygulama klasorleri ve dosyalari var mi diye kontrol ediyor
        if (!(Engine.klasorKontroluYap() && Engine.dosyaKontroluYap()))
        {
            return false;//hata durumunda uygulamadan cikilsin
        }

        return true;
    }

    /**
     * uygulamanin izin istegine kullanicinin verdigi cevabi dondurur
     *
     * @param requestCode  : izin kodu
     * @param permissions  : istenen izin
     * @param grantResults : kullanici cevabi. izin verildi yada verilmedi
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            //sd karta yazma izni istendi
            case SabitYoneticisi.IZIN_WRITE_EXTERNAL_STORAGE:
            {
                //izin kullanici tarafindan verildi
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (init())//init adimi basarili ise ui nesneleri ekrana yerlestirilsin
                    {
                        UIYukle();
                    }
                    else//init adiminda hata olustuysa uygulama kapansin
                    {
                        finish();
                    }
                }
                else//izin verilmezse uygulama kapansin
                {
                    Toast.makeText(MainActivity.this, R.string.harici_alana_yazman_izni_gerekli, Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
            default:
                HataYoneticisi.ekranaHataYazdir(getApplicationContext(), "4", getApplicationContext().getString(R.string.hatali_izin_id) + " : " + requestCode);
        }
    }

    /**
     * mainFragmenti acar
     */
    public void UIYukle()
    {
        //activity icinde ilk nesil kayitlari gosterecek fragment aciliyor
        getEngine().mainFragmentAc(1, Engine.HAREKET.ILERI, getApplicationContext().getString(R.string.app_name));

        //fab lar olusturuluyor
        getEngine().initFABYoneticisi();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * actionBar basligini degistirir
     *
     * @param baslik : actionBar da yazilacak baslik
     */
    public void toolbarBaslikGuncelle(String baslik)
    {
        TextView toolbarBaslik = (TextView) findViewById(R.id.toolbar_title);
        toolbarBaslik.setText(baslik);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
    }


    /////getter & setter/////


    public Engine getEngine()
    {
        return engine;
    }

    public void setEngine(Engine engine)
    {
        this.engine = engine;
    }
}
