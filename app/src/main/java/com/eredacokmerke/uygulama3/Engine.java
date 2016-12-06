package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;


public class Engine
{
    //private static XmlVeri xmlVeri;//veri xml dosyasi
    //private static VeritabaniKayit vtKayit;//veri veritabani nesnesi
    private static XmlAyar xmlAyar;//ayar xml dosyasi
    private static File uygulamaKlasoru;//uygulama dosyalarinin bulundugu klasor

    /**
     * uygulama icin gerekli dosyalar olusturulur
     *
     * @return hata olusursa false yoksa true doner
     */
    public static boolean dosyaKontroluYap()
    {
        //String xmlVeriDosyaYolu = getUygulamaKlasoru() + "/" + SabitYoneticisi.XML_DOSYA_ADI;
        String xmlAyarDosyaYolu = getUygulamaKlasoru() + "/" + SabitYoneticisi.XML_AYAR_DOSYA_ADI;
        //String vtDosyaIsmi = "kayit.db";
        //String vtDosyaYolu = getUygulamaKlasoru() + "/" + vtDosyaIsmi;

        //veritaani dosyalari kontrol ediliyor, veritabani nesneleri dolduruluyor
        VeritabaniYoneticisi.dosyaKontroluYap(getUygulamaKlasoru());

        ////setXmlVeri(new XmlVeri(xmlVeriDosyaYolu));
        setXmlAyar(new XmlAyar(xmlAyarDosyaYolu));
        //setVtKayit(new VeritabaniKayit(vtDosyaIsmi, vtDosyaYolu));

        if ((getXmlAyar().getDocument() != null))//xml dosyaları ile ilgili hata yoksa devam etsin, varsa uygulamayı sonlandırsın
        {
            return true;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir("1", "xml hatasi olustu");
            return false;
        }

        //if ((getXmlVeri().getDocument() != null) && (getXmlAyar() != null))//xml dosyaları ile ilgili hata yoksa devam etsin, varsa uygulamayı sonlandırsın
        //{
                    /*
                    org.w3c.dom.Element element = document.getElementById("0");
                    if (savedInstanceState == null)
                    {
                        String renk = etiketBilgisiniGetir(element, Sabit.XML_RENK);
                        String baslik = etiketBilgisiniGetir(element, Sabit.XML_BASLIK);
                        String durum = etiketBilgisiniGetir(element, Sabit.XML_DURUM);

                        CustomRelativeLayout crl = new CustomRelativeLayout(cnt, baslik, renk, durum, 0, -1);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                        getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstanceKategori(crl), Sabit.FRAGMENT_TAG).commit();
                    }
                    if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        String renk = etiketBilgisiniGetir(element, Sabit.XML_RENK);
                        actionBarArkaPlanDegistir(renk);
                    }
                    else
                    {
                        actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
                    }
                    getSupportActionBar().setDisplayUseLogoEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);

                    actionBarBoyUzunlugunuGetir();
                    elemanEniniHesapla();
                    elemanBoyunuHesapla();

                    listeRenkler = new ArrayList<>();
                    listeRenkler.add(Sabit.RENK_SIYAH);
                    listeRenkler.add(Sabit.RENK_BEYAZ);
                    listeRenkler.add(Sabit.RENK_ACIK_GRI);
                    listeRenkler.add(Sabit.RENK_KAPALI_GRI);
                    listeRenkler.add(Sabit.RENK_MAVI);
                    listeRenkler.add(Sabit.RENK_MOR);
                    listeRenkler.add(Sabit.RENK_SARI);
                    listeRenkler.add(Sabit.RENK_KAHVE);
                    listeRenkler.add(Sabit.RENK_TURUNCU);
                    listeRenkler.add(Sabit.RENK_YESIL);

                    //geriSimgesiniEkle();
                    */

        //return true;
        //}
        //else
        // {
        //   HataYoneticisi.ekranaHataYazdir("1", "xml hatasi olustu");
        //   return false;
        // }
    }

    /**
     * uygulama icin gerekli klasorler olusturulur
     *
     * @return hata olusursa false yoksa true doner
     */
    public static boolean klasorKontroluYap()
    {
        setUygulamaKlasoru(uygulamaKlasoruKontrolEt());
        File xmlYedekKlasoru = xmlYedekKlasoruKontrolEt();

        if (getUygulamaKlasoru() != null && xmlYedekKlasoru != null)
        {
            //String xmlYedekKlasorYolu = xmlYedekKlasoru.getAbsolutePath();

            //uygulama klasoru varsa
            if (getUygulamaKlasoru().exists())
            {
                return true;
            }
            else
            {
                HataYoneticisi.ekranaHataYazdir("2", "uygulama klasoru yok");
                return false;
            }
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir("3", "klasor hatasi olustu");
            return false;
        }
    }

    /**
     * uygulama dosyalarinin duracagi klasorun olup olmadigina bakar, yoksa olusturur
     *
     * @return hata olusursa false yoksa true doner
     */
    public static File uygulamaKlasoruKontrolEt()
    {
        File uygKlasoru;

        if (hariciAlanVarMi())//sdcard var
        {
            uygKlasoru = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SabitYoneticisi.UYGULAMA_ADI);
        }
        else//sdcard yok
        {
            uygKlasoru = MainActivity.getCnt().getDir(SabitYoneticisi.UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!uygKlasoru.exists())
        {
            if (!uygKlasoru.mkdirs())
            {
                HataYoneticisi.ekranaHataYazdir("17", MainActivity.getCnt().getString(R.string.uygulama_klasoru_olusturulamadi) + ", " + MainActivity.getCnt().getString(R.string.klasor) + " : " + uygKlasoru);
                return null;
            }
        }

        return uygKlasoru;
    }

    /**
     * xml dosyalarinin yedeklenecegi klasorun olup olmadigina bakar, yoksa olusturur
     *
     * @return hata olusursa false yoksa true doner
     */
    public static File xmlYedekKlasoruKontrolEt()
    {
        File xmlYedekKlasoru;

        if (hariciAlanVarMi())//sdcard var
        {
            xmlYedekKlasoru = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SabitYoneticisi.UYGULAMA_ADI + "/" + SabitYoneticisi.YEDEK_KLASORU_ADI);
        }
        else//sdcard yok
        {
            xmlYedekKlasoru = MainActivity.getCnt().getDir(SabitYoneticisi.UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!xmlYedekKlasoru.exists())
        {
            if (!xmlYedekKlasoru.mkdirs())
            {
                HataYoneticisi.ekranaHataYazdir("18", MainActivity.getCnt().getString(R.string.yedek_klasoru_olusturulumadi) + ", " + MainActivity.getCnt().getString(R.string.klasor) + " : " + xmlYedekKlasoru);
                return null;
            }
        }

        return xmlYedekKlasoru;
    }

    /**
     * sdcard ın olup olmadığını kontrol ediyor
     */
    public static boolean hariciAlanVarMi()
    {
        String state = Environment.getExternalStorageState();
        switch (state)
        {
            case Environment.MEDIA_MOUNTED:
                return true;

            case Environment.MEDIA_MOUNTED_READ_ONLY:
                return false;

            default:
                return false;
        }
    }

    /**
     * veritabanindan kayitlari alir
     */
    public static List<KayitLayout> mainFragmentKayitlariVeritabanindanAl()
    {
        return VeritabaniYoneticisi.mainFragmentKayitlariVeritabanindanAl();
    }

    /**
     * veritabanindan klasorleri alir
     */
    public static List<KayitLayout> mainFragmentKlasorleriVeritabanindanAl()
    {
        return VeritabaniYoneticisi.mainFragmentKlasorleriVeritabanindanAl();
    }

    /**
     * yeniKayitFragment te kaydet tusuna basilinca ekranadaki verileri veritabanina kaydeder
     */
    public static void yeniKayitFragmentKaydet()
    {
        int seciliIcerikTuru = FragmentYoneticisi.yeniKayitFragmentSpinnerSeciliNesneyiGetir();
        String baslik = FragmentYoneticisi.yeniKayitFragmentBaslikGetir();
        String icerik = FragmentYoneticisi.yeniKayitFragmentIcerikGetir();

        VeritabaniYoneticisi.yeniKayitFragmentVeritabaninaKaydet(seciliIcerikTuru, baslik, icerik);
    }

    /**
     * yeniKlasorFragment te kaydet tusuna basilinca ekrandaki verileri veritabanina kaydeder
     */
    public static void yeniKlasorFragmentKaydet()
    {
        String baslik = FragmentYoneticisi.yeniFragmentKlasorBaslikGetir();

        VeritabaniYoneticisi.yeniKlasorFragmentVeritabaninaKaydet(baslik, FragmentYoneticisi.getFragmentKlasorID());
    }

    /**
     * mainFragment acar
     *
     * @param ma       : mainActivity nesnesi
     * @param b
     * @param klasorID : acilan klasorun id si
     */
    public static void mainFragmentAc(MainActivity ma, Bundle b, int klasorID)
    {
        //mainFragment ta etkin ekran kayit ekrani
        SabitYoneticisi.setEtkinEkran(SabitYoneticisi.EKRAN_KAYIT);

        FragmentYoneticisi.fragmentAc(MainFragment.newInstance(1, "2"), ma, b, klasorID);
    }

    /**
     * ekran klavyesini kapatir
     */
    public static void klavyeKapat(MainActivity ma, View view)
    {
        InputMethodManager imm = (InputMethodManager) ma.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * floating action button yoneticisini baslatir. ekranda fab i gosterir
     *
     * @param ma : main activity nesnesi
     */
    public static void initFABYoneticisi(MainActivity ma)
    {
        new FABYoneticisi(ma);
    }

    /**
     * yeniFragment spinner da gosterilecek icerik turlerini veritabanindan alir
     *
     * @return : icerik turleri listesi
     */
    public static List<String> yeniFragmentVeriTurleriniVeritabanindanAl()
    {
        return VeritabaniYoneticisi.yeniKayitFragmentVeriTurleriniVeritabanindanAl();
    }

    /**
     * fragment te acik olan klasorun id sini getirir
     *
     * @return : klasor id si
     */
    public static int getFragmentKlasorID()
    {
        return FragmentYoneticisi.getFragmentKlasorID();
    }

    /////getter & setter/////


    public static XmlAyar getXmlAyar()
    {
        return xmlAyar;
    }

    public static void setXmlAyar(XmlAyar xmlAyar)
    {
        Engine.xmlAyar = xmlAyar;
    }

    public static File getUygulamaKlasoru()
    {
        return uygulamaKlasoru;
    }

    public static void setUygulamaKlasoru(File uygulamaKlasoru)
    {
        Engine.uygulamaKlasoru = uygulamaKlasoru;
    }
}
