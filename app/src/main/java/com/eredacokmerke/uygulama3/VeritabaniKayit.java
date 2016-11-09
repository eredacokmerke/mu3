package com.eredacokmerke.uygulama3;

import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;

/**
 * Created by ekcdr on 11/5/16.
 */
public class VeritabaniKayit extends VeritabaniYoneticisi
{
    private static final int DATABASE_VERSION = 1;
    private static DatabaseErrorHandler handler = new DefaultDatabaseErrorHandler();

    public VeritabaniKayit(String vtDosyaIsmi, String vtDosyaYolu)
    {
        super(MainActivity.getCnt(), vtDosyaIsmi, null, DATABASE_VERSION, handler, vtDosyaYolu);
    }

    @Override
    public void ontaminliVerileriYaz()
    {
        getVT().execSQL("INSERT INTO TUR (ISIM) VALUES ('asdfghjkl');");
        getVT().execSQL("INSERT INTO TUR (ISIM) VALUES ('vvvvvvvv');");

        getVT().execSQL("INSERT INTO KAYIT (VERI_TURU, BASLIK, RENK, VERI) VALUES (2, 'Fred', 'Flinstone', '10.0');");
        getVT().execSQL("INSERT INTO KAYIT (VERI_TURU, BASLIK, RENK, VERI) VALUES (1, 'vvv', 'Flinvvvvstone', '10.0');");
    }

    @Override
    public void tablolariOlustur()
    {
        getVT().execSQL("CREATE TABLE KAYIT ( ID INTEGER PRIMARY KEY, VERI_TURU INTEGER, BASLIK TEXT, RENK TEXT, VERI TEXT, FOREIGN KEY(VERI_TURU) REFERENCES TUR(ID) );");
        getVT().execSQL("CREATE TABLE TUR (ID INTEGER PRIMARY KEY, ISIM TEXT)");
    }
}
