package com.eredacokmerke.uygulama3;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class FragmentYoneticisi extends Fragment
{

    public static void fragmentAc(Fragment fy, MainActivity ma)
    {
        FragmentTransaction ft = ma.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fy);
        ft.commit();
    }

}
