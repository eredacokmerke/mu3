package com.eredacokmerke.uygulama3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class FragmentYoneticisi extends Fragment
{
    private static View fragmentRootView;//acilan fragmentin root view i
    private static boolean fragmentAcikMi = false;//fragment arka tarafa atilip tekrar on plana alindiginda acik olup olmadigini anlamak icin

    /**
     * yeni fragment i ekranda gosterir
     *
     * @param fy : gosterilecek fragment
     * @param ma : activity nesnesi
     */
    public static void fragmentAc(Fragment fy, MainActivity ma, Bundle b)
    {
        if (b == null)
        {
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
        //override
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (!isFragmentAcikMi())//eger fragment aciksa tekrardan islem yapmasin
        {
            //setFragmentRootView(rootView);
            fragmentBasladi();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        //fragment arkaplana atildi. onplana geldigi zaman onStart() daki islemlerin tekrar etmemesi icin flah tutuyorum
        setFragmentAcikMi(true);
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

    public static boolean isFragmentAcikMi()
    {
        return fragmentAcikMi;
    }

    public static void setFragmentAcikMi(boolean fragmentAcikMi)
    {
        FragmentYoneticisi.fragmentAcikMi = fragmentAcikMi;
    }
}
