package com.eredacokmerke.uygulama3;

import android.content.Context;
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
    private Context cnt;

    public VeritabaniKayit(Context cnt, String vtDosyaIsmi, String vtDosyaYolu)
    {
        super(cnt, vtDosyaIsmi, null, DATABASE_VERSION, handler, vtDosyaYolu);
        this.cnt = cnt;
    }

    @Override
    public void ontaminliVerileriYaz()
    {
        getVT().execSQL("INSERT INTO KLASOR (KLASOR_ISIM, UST_KLASOR_ID, KLASOR_RENK_KODU_ID) VALUES ('ANA', 0, 1);");

        getVT().execSQL("INSERT INTO RENK (RENK_ISIM) VALUES ('#118580');");
        getVT().execSQL("INSERT INTO RENK (RENK_ISIM) VALUES ('#162e1b');");
        getVT().execSQL("INSERT INTO RENK (RENK_ISIM) VALUES ('#abc123');");

        getVT().execSQL("INSERT INTO TUR (TUR_ISIM) VALUES ('duz_not');");
        getVT().execSQL("INSERT INTO TUR (TUR_ISIM) VALUES ('calar_saat');");
        getVT().execSQL("INSERT INTO TUR (TUR_ISIM) VALUES ('yapilacaklar_listesi');");

        getVT().execSQL("INSERT INTO KAYIT (ICERIK_TURU_ID, RENK_KODU_ID, KLASOR_ID, BASLIK, ICERIK) VALUES (1, 1, 0, 'baslik1', 'icerik1');");
        getVT().execSQL("INSERT INTO KAYIT (ICERIK_TURU_ID, RENK_KODU_ID, KLASOR_ID, BASLIK, ICERIK) VALUES (1, 2, 0, 'baslik2', 'icerik2');");
        getVT().execSQL("INSERT INTO KAYIT (ICERIK_TURU_ID, RENK_KODU_ID, KLASOR_ID, BASLIK, ICERIK) VALUES (1, 3, 0, 'baslik3', 'icerik3');");
    }

    @Override
    public void tablolariOlustur()
    {
        getVT().execSQL("CREATE TABLE KLASOR (ID INTEGER PRIMARY KEY, KLASOR_ISIM TEXT, UST_KLASOR_ID VARCHAR(20), KLASOR_RENK_KODU_ID INTEGER, FOREIGN KEY(KLASOR_RENK_KODU_ID) REFERENCES RENK(ID));");
        getVT().execSQL("CREATE TABLE TUR (ID INTEGER PRIMARY KEY, TUR_ISIM TEXT);");
        getVT().execSQL("CREATE TABLE RENK (ID INTEGER PRIMARY KEY, RENK_ISIM VARCHAR(20));");
        getVT().execSQL("CREATE TABLE KAYIT (ID INTEGER PRIMARY KEY, ICERIK_TURU_ID INTEGER, RENK_KODU_ID INTEGER, KLASOR_ID INTEGER,BASLIK TEXT, ICERIK TEXT, FOREIGN KEY(ICERIK_TURU_ID) REFERENCES TUR(ID), FOREIGN KEY(RENK_KODU_ID) REFERENCES RENK(ID), FOREIGN KEY(KLASOR_ID) REFERENCES KLASOR(ID));");
    }

    /**
     * mainFragment de gosterilecek kayitlari veritabanindan alir
     *
     * @param klasorID : ici gosterilecek klasorun id si
     * @return : kayit listesi
     */
    public List<KayitLayout> mainFragmentKayitlariGetir(int klasorID)
    {
        String selectQuery = "select R.RENK_ISIM, BASLIK, ICERIK, ICERIK_TURU_ID from KAYIT as K, RENK as R where K.RENK_KODU_ID = R.ID and K.KLASOR_ID='" + klasorID + "';";

        Cursor cursor = getVT().rawQuery(selectQuery, null);
        List<KayitLayout> listeVeri = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            do
            {
                String veriKayitRenk = cursor.getString(0);
                String veriKayitBaslik = cursor.getString(1);
                String veriKayitIcerik = cursor.getString(2);
                String veriKayitIcerikTuruID = cursor.getString(3);

                listeVeri.add(new KayitLayout(cnt, veriKayitBaslik, veriKayitIcerik, veriKayitRenk, veriKayitIcerikTuruID));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeVeri;
    }

    /**
     * mainFragment de gosterilecek klasorleri veritabanindan alir
     *
     * @param klasorID : klasorlerin icinde yer aldigi klasorun id si
     * @return : klasor listesi
     */
    public List<KayitLayout> mainFragmentKlasorleriGetir(int klasorID)
    {
        String selectQuery = "select K.KLASOR_ISIM, R.RENK_ISIM from KLASOR as K, RENK as R where K.KLASOR_RENK_KODU_ID=R.ID and UST_KLASOR_ID='" + klasorID + "';";

        Cursor cursor = getVT().rawQuery(selectQuery, null);
        List<KayitLayout> listeVeri = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            do
            {
                String veriKlasorIsim = cursor.getString(0);
                String veriKlasorRenk = cursor.getString(1);

                listeVeri.add(new KayitLayout(cnt, veriKlasorIsim, veriKlasorRenk, veriKlasorID));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeVeri;
    }

    /**
     * yeniKayitFragment icin gerekli olan verileri veritabanindan alir
     *
     * @return : veri listesi
     */
    public List<String> yeniKayitFragmentVeriTurleriniGetir()
    {
        String selectQuery = "select TUR_ISIM from TUR;";
        Cursor cursor = getVT().rawQuery(selectQuery, null);
        List<String> listeVeri = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            do
            {
                String veriTurIsim = cursor.getString(0);

                listeVeri.add(veriTurIsim);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listeVeri;
    }

    /**
     * yeniKayitFragment ekraninda kaydet tusuna basildiginda verileri veritabanina kaydeder
     *
     * @param seciliIcerikTuruID : secili veri turunun id si
     * @param baslik             : ekranda girilen baslik
     * @param icerik             : ekranda girilen veri
     */
    public void yeniKayitFragmentVerileriKaydet(int seciliIcerikTuruID, String baslik, String icerik)
    {
        String insertQuery = "INSERT INTO KAYIT (ICERIK_TURU_ID, RENK_KODU_ID, KLASOR_ID, BASLIK, ICERIK) VALUES (" + seciliIcerikTuruID + ", 1," + VeritabaniYoneticisi.getFragmentKlasorID() + " ,'" + baslik + "', '" + icerik + "');";
        getVT().execSQL(insertQuery);
    }

    /**
     * yeniKlasorFragment ekraninda kaydet tusuna basildiginda verileri veritabanina kaydeder
     *
     * @param baslik    :klasor ismi
     * @param klasorID: klasorun icinde olusturulacagi klasorun id si
     */
    public void yeniKlasorFragmentVerileriKaydet(String baslik, int klasorID)
    {
        String insertQuery = "INSERT INTO KLASOR (KLASOR_ISIM, UST_KLASOR_ID, KLASOR_RENK_KODU_ID) VALUES ('" + baslik + "', " + klasorID + ", 2);";
        getVT().execSQL(insertQuery);
    }
}
