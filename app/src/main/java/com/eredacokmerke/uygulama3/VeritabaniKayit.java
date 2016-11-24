package com.eredacokmerke.uygulama3;

import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;

import java.util.ArrayList;
import java.util.List;

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
        getVT().execSQL("INSERT INTO RENK (RENK_ISIM) VALUES ('#118580');");
        getVT().execSQL("INSERT INTO RENK (RENK_ISIM) VALUES ('#162e1b');");
        getVT().execSQL("INSERT INTO RENK (RENK_ISIM) VALUES ('#abc123');");

        getVT().execSQL("INSERT INTO TUR (TUR_ISIM) VALUES ('duz_not');");
        getVT().execSQL("INSERT INTO TUR (TUR_ISIM) VALUES ('calar_saat');");
        getVT().execSQL("INSERT INTO TUR (TUR_ISIM) VALUES ('yapilacaklar_listesi');");

        getVT().execSQL("INSERT INTO KAYIT (VERI_TURU_ID, RENK_KODU_ID, BASLIK, VERI) VALUES (1, 1, 'baslik1', 'veri1');");
        getVT().execSQL("INSERT INTO KAYIT (VERI_TURU_ID, RENK_KODU_ID, BASLIK, VERI) VALUES (1, 2, 'baslik2', 'veri2');");
        getVT().execSQL("INSERT INTO KAYIT (VERI_TURU_ID, RENK_KODU_ID, BASLIK, VERI) VALUES (1, 3, 'baslik3', 'veri3');");
    }

    @Override
    public void tablolariOlustur()
    {
        getVT().execSQL("CREATE TABLE KAYIT (ID INTEGER PRIMARY KEY,VERI_TURU_ID INTEGER,RENK_KODU_ID INTEGER,BASLIK TEXT, VERI TEXT,FOREIGN KEY(VERI_TURU_ID) REFERENCES TUR(ID)FOREIGN KEY(RENK_KODU_ID) REFERENCES RENK(ID));");
        getVT().execSQL("CREATE TABLE TUR (ID INTEGER PRIMARY KEY,TUR_ISIM TEXT);");
        getVT().execSQL("CREATE TABLE RENK (ID INTEGER PRIMARY KEY,RENK_ISIM VARCHAR(20));");
    }

    @Override
    public List<KayitLayout> verileriGetir()
    {
        String selectQuery = "select R.RENK_ISIM, BASLIK, VERI, VERI_TURU_ID from KAYIT as K, RENK as R where K.RENK_KODU_ID = R.ID";
        Cursor cursor = getVT().rawQuery(selectQuery, null);
        List<KayitLayout> listeVeri = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            do
            {
                String veriRenkIsim = cursor.getString(0);
                String veriBaslik = cursor.getString(1);
                String veriIcerik = cursor.getString(2);
                String veriIcerikTuruID = cursor.getString(3);

                listeVeri.add(new KayitLayout(MainActivity.getCnt(), veriBaslik, veriIcerik, veriRenkIsim, veriIcerikTuruID));
            } while (cursor.moveToNext());
        }

        return listeVeri;
    }
}
