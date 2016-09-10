package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Engine
{
    public static boolean klasorKontroluYap()
    {
        File uygulamaKlasoru = uygulamaKlasoruKontrolEt();
        File xmlYedekKlasoru = xmlYedekKlasoruKontrolEt();

        if (uygulamaKlasoru != null && xmlYedekKlasoru != null)
        {
            String xmlYedekKlasorYolu = xmlYedekKlasoru.getAbsolutePath();

            if (uygulamaKlasoru.exists())
            {
                String xmlVeriDosyaYolu = uygulamaKlasoru + "/" + SabitYoneticisi.XML_DOSYA_ADI;
                String xmlAyarDosyaYolu = uygulamaKlasoru + "/" + SabitYoneticisi.XML_AYAR_DOSYA_ADI;

                XmlVeri xmlVeri = new XmlVeri(xmlVeriDosyaYolu);
                XmlAyar xmlAyar = new XmlAyar(xmlAyarDosyaYolu);


                if (xmlVeri.isXmlDosyasiDuzgnMu() && xmlAyar.isXmlDosyasiDuzgnMu())//xml dosyaları ile ilgili hata yoksa devam etsin, varsa uygulamayı sonlandırsın
                {
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

                    return true;
                }
                else
                {
                    HataYoneticisi.ekranaHataYazdir("45", "xml hatasi olustu");
                    return false;
                }
            }
            else
            {
                HataYoneticisi.ekranaHataYazdir("1", "uygulama klasoru yok");
                return false;
            }
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir("37", "klasor hatasi olustu");
            return false;
        }
    }

    //xml in duracagı klasoru olusturur
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
}
