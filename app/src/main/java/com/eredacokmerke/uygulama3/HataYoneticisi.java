package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.widget.Toast;

public class HataYoneticisi
{
    public static void ekranaHataYazdir(Context cnt, String id, String hata)
    {
        Toast.makeText(cnt, "hata [" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
    }
}
