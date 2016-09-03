package com.eredacokmerke.uygulama3;

import android.widget.Toast;

public class HataYoneticisi
{
    public static void ekranaHataYazdir(String id, String hata)
    {
        Toast.makeText(MainActivity.getCnt(), "hata [" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
    }
}
