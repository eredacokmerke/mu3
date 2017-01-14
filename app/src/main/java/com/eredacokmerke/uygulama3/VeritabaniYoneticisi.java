package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.List;

/**
 * Created by ekcdr on 11/5/16.
 */
public class VeritabaniYoneticisi extends SQLiteOpenHelper
{
    private MainActivity ma;
    private VeritabaniKayit vtKayit;
    private String VT_ISIM;//veritabani dosyasinin ismi
    private String VT_DOSYA_YOLU;//veritabani dosyasinin yolu
    private SQLiteDatabase VT;//veritabani islemleri (crud) yapabilmek icin veritabani nesnesi

    public VeritabaniYoneticisi(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, String dosyaYolu)
    {
        super(context, name, factory, version, errorHandler);

        if (dosyaYolu != null)//veritabani yonetici bir veritabani dosyasi acmayacagi icin parametreler null geliyor
        {
            setVT_ISIM(name);
            setVT_DOSYA_YOLU(dosyaYolu);
            init();
        }
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
        if (!veritabaniAc())//veritabani dosyasi yoksa olusturup tablolari ve ontanimli verileri yazar, varsa sadece acar
        {
            tablolariOlustur();
            ontaminliVerileriYaz();
        }
        veritabaniKapat();
    }

    /**
     * * veritabanini crud islemleri icin acar
     *
     * @return : veritabani dosyasi varsa true, yoksa false doner
     */
    public boolean veritabaniAc()
    {
        File dbfile = new File(getVT_DOSYA_YOLU());

        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db;

        try//veritabani dosyasi var, sadece acilacak
        {
            db = SQLiteDatabase.openDatabase(getVT_DOSYA_YOLU(), null, SQLiteDatabase.OPEN_READWRITE);
            setVT(db);

            return true;
        }
        catch (SQLiteException e)//veritabani dosyasi yok, olusturulacak
        {
            db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            setVT(db);

            return false;
        }
    }

    /**
     * veritabanini kapatir
     */
    public void veritabaniKapat()
    {
        getVT().close();
    }

    public void kayitVeritabaniniAc(String vtDosyaIsmi, String vtDosyaYolu)
    {
        vtKayit = new VeritabaniKayit(getMa().getApplicationContext(), vtDosyaIsmi, vtDosyaYolu, ma);
        setVtKayit(vtKayit);
    }

    // TODO: 11/27/16 : farkli veritabani dosyalari oldugu zaman burada hepsi nasil cagrilacak
    public void dosyaKontroluYap(File uygulamaKlasoru)
    {
        String vtDosyaIsmi = "kayit.db";
        String vtDosyaYolu = uygulamaKlasoru + "/" + vtDosyaIsmi;

        kayitVeritabaniniAc(vtDosyaIsmi, vtDosyaYolu);
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

    /**
     * yeniFragment icin gerekli bilgileri veritabanindan alir
     *
     * @return
     */
    public List<String> yeniKayitFragmentVeriTurleriniVeritabanindanAl()
    {
        if (veritabaniAcikDegilseAc())
        {
            List<String> listVeriler = getVtKayit().yeniKayitFragmentVeriTurleriniGetir();
            getVtKayit().veritabaniKapat();

            return listVeriler;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "9", "veritabani baglantisi acilamadi");

            return null;
        }
    }

    /**
     * klasorFragment ta gosterilecek kayitlarin verilerini veritabanindan alir
     *
     * @return
     */
    public List<KayitLayout> mainFragmentKayitlariVeritabanindanAl()
    {
        if (veritabaniAcikDegilseAc())
        {
            List<KayitLayout> listVeriler = getVtKayit().mainFragmentKayitlariGetir(getMa().getEngine().getFragmentKlasorID());
            getVtKayit().veritabaniKapat();

            return listVeriler;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "15", "veritabani baglantisi acilamadi");

            return null;
        }
    }

    /**
     * klasorFragment ta gosterilecek klasorlerin verilerini veritabanindan alir
     *
     * @return
     */
    public List<KlasorLayout> mainFragmentKlasorleriVeritabanindanAl()
    {
        if (veritabaniAcikDegilseAc())
        {
            List<KlasorLayout> listVeriler = getVtKayit().mainFragmentKlasorleriGetir(getMa().getEngine().getFragmentKlasorID());
            getVtKayit().veritabaniKapat();

            return listVeriler;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "19", "veritabani baglantisi acilamadi");

            return null;
        }
    }

    /**
     * kayitFragment ta gosterilecek icerigi veritabanindan alir
     *
     * @return
     */
    public String kayitFragmentKayitAyrintisiniVeritabanindanAl()
    {

        if (veritabaniAcikDegilseAc())
        {
            String icerik = getVtKayit().kayitFragmentKayitAyrintisiniGetir(getMa().getEngine().getFragmentKayitID());
            getVtKayit().veritabaniKapat();

            return icerik;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "25", "veritabani baglantisi acilamadi");

            return null;
        }
    }

    /**
     * yeniKayitFragment ekraninda kaydet e tiklayinca verileri veritabanina kaydeder
     *
     * @param seciliIcerikTuruID : secili veri turunun id si
     * @param baslik             : ekranda girilen baslik
     * @param icerik             : ekranda girilen veri
     */
    public void yeniKayitFragmentVeritabaninaKaydet(int seciliIcerikTuruID, String baslik, String icerik)
    {
        if (veritabaniAcikDegilseAc())
        {
            getVtKayit().yeniKayitFragmentVerileriKaydet(seciliIcerikTuruID, baslik, icerik);
            getVtKayit().veritabaniKapat();
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "16", "veritabani baglantisi acilamadi");
        }
    }

    /**
     * yeniKlasorFragment ekraninda kaydet tusuna basildiginda verileri veritabanina kaydeder
     *
     * @param baslik : klasorun basligi
     * @param parentKlasorID : klasorun parentinin id si
     */
    public void yeniKlasorFragmentVeritabaninaKaydet(String baslik, int parentKlasorID)
    {
        if (veritabaniAcikDegilseAc())
        {
            getVtKayit().yeniKlasorFragmentVerileriKaydet(baslik, parentKlasorID);
            getVtKayit().veritabaniKapat();
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "21", "veritabani baglantisi acilamadi");
        }
    }

    /**
     * fragment te acik olan klasorun id sini getirir
     *
     * @return : klasor id si
     */
    public int getFragmentKlasorID()
    {
        return getMa().getEngine().getFragmentKlasorID();
    }

    /**
     * id si verilen klasorun parentinin id sini doner
     *
     * @param klasorID : parent i alinacacak klasorun id si
     * @return : parent klasorun id si
     */
    public int parentKlasorIDyiVeritabanindanGetir(int klasorID)
    {
        if (veritabaniAcikDegilseAc())
        {
            int parentKlasorID = getVtKayit().parentKlasorIDyiGetir(klasorID);
            getVtKayit().veritabaniKapat();

            if (parentKlasorID == -1)
            {
                HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "22", "veritabanindan veri alinamadi");
            }

            return parentKlasorID;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "23", "veritabani baglantisi acilamadi");

            return -1;
        }
    }

    /**
     * veritabanindan verilen id li klasorun basligini getitir
     *
     * @param klasorID : basligi alinacak klasor
     * @return :baslik
     */
    public String klasorBasliginiVeritabanindanGetir(int klasorID)
    {
        if (veritabaniAcikDegilseAc())
        {
            String baslik = getVtKayit().klasorBasliginiGetir(klasorID);
            getVtKayit().veritabaniKapat();

            return baslik;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "24", "veritabani baglantisi acilamadi");

            return "";
        }
    }

    /**
     * veritabani baglantisini acar
     */
    public boolean veritabaniAcikDegilseAc()
    {
        if (!(getVtKayit().getVT().isOpen()))//veritabani kapaliysa acalim
        {
            return getVtKayit().veritabaniAc();
        }

        return true;
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

    public VeritabaniKayit getVtKayit()
    {
        return vtKayit;
    }

    public void setVtKayit(VeritabaniKayit vtKayit)
    {
        this.vtKayit = vtKayit;
    }

    public MainActivity getMa()
    {
        return ma;
    }

    public void setMa(MainActivity ma)
    {
        this.ma = ma;
    }
}
