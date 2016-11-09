package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by ekcdr on 11/5/16.
 */
public class VeritabaniYoneticisi extends SQLiteOpenHelper
{
    private String VT_ISIM;//veritabani dosyasinin ismi
    private String VT_DOSYA_YOLU;//veritabani dosyasinin yolu
    private SQLiteDatabase VT;//veritabani islemleri (crud) yapabilmek icin veritabani nesnesi

    public VeritabaniYoneticisi(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, String dosyaYolu)
    {
        super(context, name, factory, version, errorHandler);
        setVT_ISIM(name);
        setVT_DOSYA_YOLU(dosyaYolu);
        init();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
    }

    /**
     * veritabani ilk olusturuldugu zaman tablolar ve ontanimli veriler veritabanina yaziliyor
     */
    public void init()
    {
        veritabaniAc(getVT_DOSYA_YOLU());
        tablolariOlustur();
        ontaminliVerileriYaz();
        veritabaniKapat();
    }

    /**
     * veritabanini crud islemleri icin acar
     */
    public void veritabaniAc(String vtDosyaYolu)
    {
        //File dbfile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SabitYoneticisi.UYGULAMA_ADI + "/" + getVT_ISIM());
        File dbfile = new File(vtDosyaYolu);

        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        setVT(db);

        // TODO: 11/8/16 hata durumlarini burada yakala
    }

    /**
     * veritabanini kapatir
     */
    public void veritabaniKapat()
    {
        getVT().close();
    }

    /**
     * veritabani ilk olusturuldugunda ontanimli verileri veritabanina yazar
     */
    public void ontaminliVerileriYaz()
    {
        //override
    }

    /**
     * veritabani ilk olusturuldugu zaman tablolari olusturur
     */
    public void tablolariOlustur()
    {
        //override
    }

    /////getter & setter/////

    public String getVT_ISIM()
    {
        return VT_ISIM;
    }

    public void setVT_ISIM(String VT_ISIM)
    {
        this.VT_ISIM = VT_ISIM;
    }

    public SQLiteDatabase getVT()
    {
        return VT;
    }

    public void setVT(SQLiteDatabase VT)
    {
        this.VT = VT;
    }

    public String getVT_DOSYA_YOLU()
    {
        return VT_DOSYA_YOLU;
    }

    public void setVT_DOSYA_YOLU(String VT_DOSYA_YOLU)
    {
        this.VT_DOSYA_YOLU = VT_DOSYA_YOLU;
    }
}
