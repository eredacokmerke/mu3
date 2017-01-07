package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;


public class Engine
{
    //private static XmlVeri xmlVeri;//veri xml dosyasi
    //private static VeritabaniKayit vtKayit;//veri veritabani nesnesi
    //private static XmlAyar xmlAyar;//ayar xml dosyasi
    private static File uygulamaKlasoru;//uygulama dosyalarinin bulundugu klasor
    private static MainActivity ma;
    private static VeritabaniYoneticisi vty;
    private static XmlYoneticisi xmly;
    private FragmentYoneticisi fry;

    //yeni acilan fragment layout a tiklanarak acilirsa ILERI
    //geri tusuna basilarak acilirsa GERI
    public enum HAREKET
    {
        ILERI,
        GERI
    }

    public Engine()
    {

    }

    /**
     * yoneticiler olusturuluyor
     *
     * @param mainActivity : mainActivity nesnesi
     */
    public void init(MainActivity mainActivity)
    {
        setMa(mainActivity);

        vty = new VeritabaniYoneticisi(getMa().getApplicationContext(), null, null, 1, null, null);
        vty.setMa(mainActivity);

        xmly = new XmlYoneticisi();
        xmly.setMa(mainActivity);

        fry = new FragmentYoneticisi();
        fry.setMa(mainActivity);
    }

    /**
     * uygulama icin gerekli dosyalar olusturulur
     *
     * @return hata olusursa false yoksa true doner
     */
    public static boolean dosyaKontroluYap()
    {
        //String xmlVeriDosyaYolu = getUygulamaKlasoru() + "/" + SabitYoneticisi.XML_DOSYA_ADI;
        String xmlAyarDosyaYolu = getUygulamaKlasoru() + "/" + SabitYoneticisi.XML_AYAR_DOSYA_ADI;

        //veritabani dosyalari kontrol ediliyor, veritabani nesneleri dolduruluyor
        getVty().dosyaKontroluYap(getUygulamaKlasoru());

        //xmlAyar olusturuluyor
        getXmly().xmlAyarOlustur(xmlAyarDosyaYolu);

        if ((getXmly().getXmlAyar().getDocument() != null))//xml dosyaları ile ilgili hata yoksa devam etsin, varsa uygulamayı sonlandırsın
        {
            return true;
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "1", getMa().getApplicationContext().getString(R.string.xml_hata_olustu));
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
                HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "2", "uygulama klasoru yok");
                return false;
            }
        }
        else
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "3", "klasor hatasi olustu");
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
            uygKlasoru = getMa().getApplicationContext().getDir(SabitYoneticisi.UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!uygKlasoru.exists())
        {
            if (!uygKlasoru.mkdirs())
            {
                HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "17", getMa().getApplicationContext().getString(R.string.uygulama_klasoru_olusturulamadi) + ", " + getMa().getApplicationContext().getString(R.string.klasor) + " : " + uygKlasoru);
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
            xmlYedekKlasoru = getMa().getApplicationContext().getDir(SabitYoneticisi.UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!xmlYedekKlasoru.exists())
        {
            if (!xmlYedekKlasoru.mkdirs())
            {
                HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "18", getMa().getApplicationContext().getString(R.string.yedek_klasoru_olusturulumadi) + ", " + getMa().getApplicationContext().getString(R.string.klasor) + " : " + xmlYedekKlasoru);
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
        return getVty().mainFragmentKayitlariVeritabanindanAl();
    }

    /**
     * veritabanindan klasorleri alir
     */
    public static List<KayitLayout> mainFragmentKlasorleriVeritabanindanAl()
    {
        return getVty().mainFragmentKlasorleriVeritabanindanAl();
    }

    /**
     * yeniKayitFragment te kaydet tusuna basilinca ekranadaki verileri veritabanina kaydeder
     */
    public void yeniKayitFragmentKaydet()
    {
        int seciliIcerikTuru = getFry().yeniKayitFragmentSpinnerSeciliNesneyiGetir();
        String baslik = getFry().yeniKayitFragmentBaslikGetir();
        String icerik = getFry().yeniKayitFragmentIcerikGetir();

        getVty().yeniKayitFragmentVeritabaninaKaydet(seciliIcerikTuru, baslik, icerik);
    }

    /**
     * yeniKlasorFragment te kaydet tusuna basilinca ekrandaki verileri veritabanina kaydeder
     */
    public void yeniKlasorFragmentKaydet()
    {
        String baslik = getFry().yeniKlasorFragmentBaslikGetir();

        getVty().yeniKlasorFragmentVeritabaninaKaydet(baslik, FragmentYoneticisi.getFragmentKlasorID());
    }

    /**
     * KlasorFragment acar
     *
     * @param klasorID : acilan klasorun id si
     * @param hareket  : fragment geri tusuna basilarak mi acildi yoksa kayitLayout a tiklanarak mi
     */
    public void klasorAc(int klasorID, HAREKET hareket, String baslik)
    {
        KlasorFragment fr = KlasorFragment.newInstance(getMa());

        Bundle args = new Bundle();
        args.putString(SabitYoneticisi.BILGI_KLASORFRAGMENT_BASLIK, baslik);
        args.putInt(SabitYoneticisi.BILGI_KLASORFRAGMENT_KLASOR_ID, klasorID);
        args.putSerializable(SabitYoneticisi.BILGI_KLASORFRAGMENT_HAREKET, hareket);
        fr.setArguments(args);

        FragmentYoneticisi.fragmentAc(fr, getMa());
    }

    }

    /**
     * id si verilen klasorun parentinin id sini doner
     *
     * @param klasorID : parent i alinacacak klasorun id si
     * @return : parent klasorun id si
     */
    public int parentKlasorIDyiGetir(int klasorID)
    {
        int parentID = getVty().parentKlasorIDyiVeritabanindanGetir(klasorID);
        if (parentID == -1)
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "22", "hatali parent id");
        }

        return parentID;
    }

    /**
     * veritabanindan verilen id li klasorun basligini getitir
     *
     * @param klasorID : basligi alinacak klasor
     * @return :baslik
     */
    public String klasorBaslikGetir(int klasorID)
    {
        return getVty().klasorBasliginiVeritabanindanGetir(klasorID);
    }

    /**
     * ekran klavyesini kapatir
     */
    public static void klavyeKapat(View view, MainActivity ma)
    {
        InputMethodManager imm = (InputMethodManager) ma.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * floating action button yoneticisini baslatir. ekranda fab i gosterir
     */
    public void initFABYoneticisi()
    {
        new FABYoneticisi(getMa(), this);
    }

    /**
     * yeniFragment spinner da gosterilecek icerik turlerini veritabanindan alir
     *
     * @return : icerik turleri listesi
     */
    public static List<String> yeniFragmentVeriTurleriniVeritabanindanAl()
    {
        return getVty().yeniKayitFragmentVeriTurleriniVeritabanindanAl();
    }

    /**
     * fragment te acik olan klasorun id sini getirir
     *
     * @return : klasor id si
     */
    public int getFragmentKlasorID()
    {
        return FragmentYoneticisi.getFragmentKlasorID();
    }


    /////getter & setter/////


    public static File getUygulamaKlasoru()
    {
        return uygulamaKlasoru;
    }

    public static void setUygulamaKlasoru(File uygulamaKlasoru)
    {
        Engine.uygulamaKlasoru = uygulamaKlasoru;
    }

    public static MainActivity getMa()
    {
        return ma;
    }

    public static void setMa(MainActivity ma)
    {
        Engine.ma = ma;
    }

    public static VeritabaniYoneticisi getVty()
    {
        return vty;
    }

    public static XmlYoneticisi getXmly()
    {
        return xmly;
    }

    public FragmentYoneticisi getFry()
    {
        return fry;
    }

    public void setFry(FragmentYoneticisi fry)
    {
        this.fry = fry;
    }
}
