package com.eredacokmerke.uygulama3;

import java.io.File;

public class Engine
{
    public static String xmlYedekKlasorYolu;
    public static File uygulamaKlasoru;
    public static File xmlYedekKlasoru;
    private static String xmlDosyaYolu;
    private static String xmlAyarDosyaYolu;

    public static boolean klasorKontroluYap()
    {
        if (uygulamaKlasoru != null && xmlYedekKlasoru != null)
        {
            xmlYedekKlasorYolu = xmlYedekKlasoru.getAbsolutePath();

            if (uygulamaKlasoru.exists())
            {
                xmlDosyaYolu = uygulamaKlasoru + "/" + SabitYoneticisi.XML_DOSYA_ADI;
                xmlAyarDosyaYolu = uygulamaKlasoru + "/" + SabitYoneticisi.XML_AYAR_DOSYA_ADI;

                if (xmlDosyasiKontrolEt() && xmlAyarDosyasiKontrolEt())//xml dosyaları ile ilgili hata yoksa devam etsin, varsa uygulamayı sonlandırsın
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

    //xml dosyası var mı diye kontrol ediyor. yoksa oluşturuyor ve en buyuk xml id sini buluyor
    public static boolean xmlDosyasiKontrolEt()
    {
        if (new File(xmlDosyaYolu).exists())//xml dosyası var mı
        {
            /*
            document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);
            if (document == null)
            {
                ekranaHataYazdir("4", cnt.getString(R.string.xml_document_olusamadi));
                return false;
            }
            else
            {
                xmlEnBuyukID = enBuyukIDyiBul();
                if (xmlEnBuyukID == -1)
                {
                    ekranaHataYazdir("5", cnt.getString(R.string.xml_okunamadi));
                    return false;
                }
                else
                {
                    return true;
                }
            }
            */
        }
        else
        {
            /*
            xmlDosyasiOlustur();
            xmlEnBuyukID = 0;
            document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);
            return true;
            */
        }

        return true;
    }

    //uygulama baslarken sistemde ayar dosyası var mı diye bakar. varsa ayarları alır. yoksa öntanımlı ayarla ile dosyayı olusturur
    public static boolean xmlAyarDosyasiKontrolEt()
    {
        if (new File(xmlAyarDosyaYolu).exists())//xml ayar dosyası var mı
        {
            /*
            documentAyar = xmlDocumentNesnesiOlustur(xmlAyarDosyaYolu);
            if (documentAyar == null)
            {
                ekranaHataYazdir("6", cnt.getString(R.string.xml_ayar_document_olusamadi));
                return false;
            }
            else
            {
                yeniAyarVarsaEkle();
                ayarlariOku();
                return true;
            }
            */
        }
        else
        {
            /*
            xmlAyarDosyasiOlustur();
            documentAyar = xmlDocumentNesnesiOlustur(xmlAyarDosyaYolu);
            yeniAyarVarsaEkle();
            ayarlariOku();
            return true;
            */
        }

        return true;
    }
}
