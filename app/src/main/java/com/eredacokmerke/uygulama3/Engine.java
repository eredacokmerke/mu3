package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;


public class Engine
{
    //private static XmlVeri xmlVeri;//veri xml dosyasi
    private static VeritabaniKayit vtKayit;//veri veritabani dosyasi
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
        String vtDosyaIsmi = "kayit.db";
        String vtDosyaYolu = getUygulamaKlasoru() + "/" + vtDosyaIsmi;

        //setXmlVeri(new XmlVeri(xmlVeriDosyaYolu));
        setXmlAyar(new XmlAyar(xmlAyarDosyaYolu));
        setVtKayit(new VeritabaniKayit(vtDosyaIsmi, vtDosyaYolu));

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
                HataYoneticisi.ekranaHataYazdir("2", MainActivity.getCnt().getString(R.string.uygulama_klasoru_olusturulamadi) + ", " + MainActivity.getCnt().getString(R.string.klasor) + " : " + uygKlasoru);
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
                HataYoneticisi.ekranaHataYazdir("3", MainActivity.getCnt().getString(R.string.yedek_klasoru_olusturulumadi) + ", " + MainActivity.getCnt().getString(R.string.klasor) + " : " + xmlYedekKlasoru);
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
     * verilen rengin koyu,açık bilgisini döndürür
     *
     * @param renk renk kodu
     * @return acik ise false koyu ise true doner
     */
    public static boolean renkKoyuMu(String renk)
    {
        int irenk = Color.parseColor(renk);
        double darkness = 1 - (0.299 * Color.red(irenk) + 0.587 * Color.green(irenk) + 0.114 * Color.blue(irenk)) / 255;
        if (darkness < 0.5)
        {
            return false; //açık renk
        }
        else
        {
            return true; //koyu renk
        }
    }

    /**
     * main fragment icin verileri veritabanindan alir ve ekrana yerlestirir
     */
    public static void mainFragmentVerileriEkranaGetir()
    {
        List<KayitLayout> listeVeriler = mainFragmentVerileriVeritabanindanAl();
        mainFragmentVerileriEkranaYerlestir(listeVeriler);//veriler ekrana yerlestiriliyor
    }

    /**
     * veritabanindan verileri alir
     */
    public static List<KayitLayout> mainFragmentVerileriVeritabanindanAl()
    {
        if (!(getVtKayit().getVT().isOpen()))//veritabani kapaliysa acalim
        {
            getVtKayit().veritabaniAc();
        }
        List<KayitLayout> listVeriler = getVtKayit().verileriGetir();
        getVtKayit().veritabaniKapat();

        return listVeriler;
    }

    /**
     * veritabaindan alinan verileri fragment e yerlestirir
     *
     * @param liste : veritabanindan gelen veriler
     */
    public static void mainFragmentVerileriEkranaYerlestir(List<KayitLayout> liste)
    {
        for (int i = 0; i < liste.size(); i++)
        {
            View v = FragmentYoneticisi.getFragmentRootView();
            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.mainFragmentRelativeLayout);

            //KayitLayout kl = new KayitLayout(MainActivity.getCnt());
            KayitLayout kl = liste.get(i);
            kl.setId(i + 100000);
            //kl.setBackgroundColor(Color.RED);
            kl.setPadding(20, 20, 20, 20);

            RelativeLayout.LayoutParams kl_lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            kl_lp.setMargins(20, 20, 20, 0);

            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(10);
            shape.setColor(Color.parseColor(kl.getRenk()));

            kl.setBackground(shape);
            ViewCompat.setElevation(kl, 5);

            if (i != 0)
            {
                kl_lp.addRule(RelativeLayout.BELOW, i - 1 + 100000);
            }
            if (i == liste.size() - 1)
            {
                kl_lp.setMargins(20, 20, 20, 20);
            }
            kl.setLayoutParams(kl_lp);


            TextView tv = new TextView(MainActivity.getCnt());
            tv.setText(kl.getBaslik());
            tv.setTextColor(Color.YELLOW);

            kl.addView(tv);

            rl.addView(kl);
        }
    }

    /**
     * yeni fragment acar
     *
     * @param ma : main activity nesnesi
     */
    public static void fragmentAc(MainActivity ma, Bundle b)
    {
        FragmentYoneticisi.fragmentAc(MainFragment.newInstance(1, "2"), ma, b);
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


    /////getter & setter/////


    public static VeritabaniKayit getVtKayit()
    {
        return vtKayit;
    }

    public static void setVtKayit(VeritabaniKayit vtKayit)
    {
        Engine.vtKayit = vtKayit;
    }

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
