package com.eredacokmerke.uygulama3;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class FragmentYoneticisi extends Fragment
{

    /**
     * yeni fragment i ekranda gosterir
     *
     * @param fy : gosterilecek fragment
     * @param ma : activity nesnesi
     */
    public static void fragmentAc(Fragment fy, MainActivity ma)
    {
        FragmentTransaction ft = ma.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fy);
        //ft.commit();//java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState hatasindan kurtulmak icin alttaki metodu kullandim
        ft.commitAllowingStateLoss();
    }
}
