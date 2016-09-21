package com.eredacokmerke.uygulama3;

import android.Manifest;
import android.content.Context;
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
import android.widget.Toast;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener, YeniFragment.OnFragmentInteractionListener
{
    private static Context cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                SabitYoneticisi.IZIN_WRITE_EXTERNAL_STORAGE);

        //global context olusturuluyor
        cnt = getApplicationContext();
    }

    /**
     * uygulama acildigi zaman ilk yapilacak islemler
     *
     * @return hata olusursa false yoksa true doner
     */
    public boolean init()
    {
        //uygulama klasorler var mi diye kontrol ediyor
        if (!Engine.klasorKontroluYap())
        {
            return false;
        }

        //activity icinde ilk nesil kayitlari gosterecek fragment aciliyor
        FragmentYoneticisi.fragmentAc(MainFragment.newInstance(Engine.getXmlVeri().getXmlEnBuyukID(), "2"), MainActivity.this);

        //fab lar olusturuluyor
        new FABYoneticisi(MainActivity.this);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case SabitYoneticisi.IZIN_WRITE_EXTERNAL_STORAGE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //init adiminda hata olustuysa uygulama kapansin
                    if (!init())
                    {
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, R.string.harici_alana_yazman_izni_gerekli, Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
            default:
                HataYoneticisi.ekranaHataYazdir("4", MainActivity.getCnt().getString(R.string.hatali_izin_id) + " : " + requestCode);
        }
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


    public static Context getCnt()
    {
        return cnt;
    }
}
