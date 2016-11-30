package com.eredacokmerke.uygulama3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.List;

public class FragmentYoneticisi extends Fragment
{
    private static View fragmentRootView;//acilan fragmentin root view i
    private static FragmentYoneticisi acikFragment;//acilan fragmentin nesnesi
    private boolean fragmentAcikMi = false;
    private static int fragmentKlasorID = 0;

    /**
     * yeni fragment i ekranda gosterir
     *
     * @param fy       : gosterilecek fragment
     * @param ma       : activity nesnesi
     * @param klasorID : fragment te gosterilecek klasorun id si
     */
    public static void fragmentAc(Fragment fy, MainActivity ma, Bundle b, int klasorID)
    {
        if (b == null)
        {
            setFragmentKlasorID(klasorID);

            FragmentTransaction ft = ma.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fy);
            //ft.commit();//java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState hatasindan kurtulmak icin alttaki metodu kullandim
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * fragment onStart metodunda yapilacaklar
     */
    public void fragmentBasladi()
    {
        UIYukle();
    }

    /**
     * fragment ui nesnelerini yukler
     */
    public void UIYukle()
    {
        //override
    }

    /**
     * fragment basladi
     */
    @Override
    public void onStart()
    {
        super.onStart();

        if (!getAcikFragment().isFragmentAcikMi())//eger fragment aciksa tekrardan islem yapmasin
        {
            fragmentBasladi();
        }
    }

    /**
     * fragment arkaplandan onplana getirildi
     */
    @Override
    public void onResume()
    {
        super.onResume();
    }

    /**
     * fragment arkaplana atildi
     */
    @Override
    public void onPause()
    {
        super.onPause();
        //override
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }


    public static List<KayitLayout> mainFragmentVerileriVeritabanindanAl()
    {
        return Engine.mainFragmentVerileriVeritabanindanAl();
    }

    /**
     * yeniFragment te spinner da secili nesneyi dondurur
     */
    public static int yeniFragmentSpinnerSeciliNesneyiGetir()
    {
        YeniFragment yf = (YeniFragment) getAcikFragment();
        int seciliID = yf.spinnerSeciliNesneyiGetir();
        yf = null;

        return seciliID;
    }

    public static String yeniFragmentBaslikGetir()
    {
        YeniFragment yf = (YeniFragment) getAcikFragment();
        String baslik = yf.baslikGetir();
        yf = null;

        return baslik;
    }

    public static String yeniFragmentIcerikGetir()
    {
        YeniFragment yf = (YeniFragment) getAcikFragment();
        String baslik = yf.icerikGetir();
        yf = null;

        return baslik;
    }

    /**
     * spinner a doldurulacak verileri veritabanindan alir
     *
     * @return
     */
    public static List<String> yeniFragmentVeriTurleriniVeritabanindanAl()
    {
        return Engine.yeniFragmentVeriTurleriniVeritabanindanAl();
    }


    /////getter & setter/////


    public static View getFragmentRootView()
    {
        return fragmentRootView;
    }

    public static void setFragmentRootView(View fragmentRootView)
    {
        FragmentYoneticisi.fragmentRootView = fragmentRootView;
    }

    public static FragmentYoneticisi getAcikFragment()
    {
        return acikFragment;
    }

    public static void setAcikFragment(FragmentYoneticisi acikFragment)
    {
        FragmentYoneticisi.acikFragment = acikFragment;
    }

    public boolean isFragmentAcikMi()
    {
        return fragmentAcikMi;
    }

    public void setFragmentAcikMi(boolean fragmentAcikMi)
    {
        this.fragmentAcikMi = fragmentAcikMi;
    }

    public static int getFragmentKlasorID()
    {
        return fragmentKlasorID;
    }

    public static void setFragmentKlasorID(int fragmentKlasorID)
    {
        FragmentYoneticisi.fragmentKlasorID = fragmentKlasorID;
    }
}
