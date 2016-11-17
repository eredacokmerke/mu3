package com.eredacokmerke.uygulama3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class FragmentYoneticisi extends Fragment
{
    private static View fragmentRootView;//acilan fragmentin root view i

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
            ft.commit();//java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState hatasindan kurtulmak icin alttaki metodu kullandim
            //ft.commitAllowingStateLoss();
        }
    }

    /**
     * fragment onStart metodundan Engine sinifina gecis yapabilmek icin
     */
    public static void fragmentBasladi()
    {
        Engine.mainFragmentVerileriEkranaGetir();
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
}
