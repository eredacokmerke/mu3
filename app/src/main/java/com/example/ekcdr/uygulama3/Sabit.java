package com.example.ekcdr.uygulama3;

public class Sabit
{
    public static final String UYGULAMA_ADI = "uygulama3";
    public static final String YEDEK_KLASORU_ADI = "backup";
    public static final String FRAGMENT_TAG = "fragment_tag";
    public static final String TIK_UNICODE = "\u2714";
    public static final String UC_NOKTA = "/.../";

    //xml dosya adlari
    public static final String XML_DOSYA_UZANTISI = "xml";
    public static final String XML_DOSYA_ADI = "new." + XML_DOSYA_UZANTISI;
    public static final String XML_AYAR_DOSYA_ADI = "opt." + XML_DOSYA_UZANTISI;

    //xml etiketleri
    public static final String XML_PARCA = "parca";
    public static final String XML_RENK = "renk";
    public static final String XML_BASLIK = "baslik";
    public static final String XML_YAZILAR = "yazilar";
    public static final String XML_YAZI = "yazi";
    public static final String XML_ROOT = "root";
    public static final String XML_KAYIT = "kayit";
    public static final String XML_ALTPARCA = "altparca";
    public static final String XML_ID = "id";
    public static final String XML_DURUM = "durum";
    public static final String XML_AYAR = "ayar";

    //renkler
    public static final String RENK_SIYAH = "#000000";
    public static final String RENK_BEYAZ = "#FFFFFF";
    public static final String RENK_BEYAZ2 = "#E9E9E9";
    public static final String RENK_ACIK_GRI = "#D8D8D8";
    public static final String RENK_KAPALI_GRI = "#404040";
    public static final String RENK_MAVI = "#779ECB";
    public static final String RENK_YESIL = "#03C03C";
    public static final String RENK_SARI = "#FDFD96";
    public static final String RENK_TURUNCU = "#FFB347";
    public static final String RENK_MOR = "#966FD6";
    public static final String RENK_TURKUAZ = "#00FFFF";

    //icine girilen elemanin turu
    public static final int ELEMAN_TUR_KAYIT = 0;
    public static final int ELEMAN_TUR_KATEGORI = 1;
    public static final int ELEMAN_TUR_YEDEK = 2;
    public static final int ELEMAN_TUR_AYARLAR = 3;
    public static final int ELEMAN_TUR_YENI_KAYIT = 4;
    public static final int ELEMAN_TUR_YENI_KATEGORI = 5;

    //acilan fragmentlerin turu
    public static final int FRAGMENT_KATEGORI_EKRANI = 0;
    public static final int FRAGMENT_KAYIT_EKRANI = 1;
    public static final int FRAGMENT_YEDEK_EKRANI = 2;
    public static final int FRAGMENT_AYAR_EKRANI = 3;
    public static final int FRAGMENT_YENI_KAYIT_EKRANI = 4;
    public static final int FRAGMENT_YENI_KATEGORI_EKRANI = 5;

    //actionbar turu
    public static final int ACTIONBAR_EKLE = 0;
    //public static final int ACTIONBAR_ONAY = 1;
    public static final int ACTIONBAR_SECIM = 2;
    public static final int ACTIONBAR_DEGISTIR = 3;
    public static final int ACTIONBAR_YEDEK = 4;
    public static final int ACTIONBAR_AYAR = 5;
    public static final int ACTIONBAR_YENI_KAYIT = 6;
    public static final int ACTIONBAR_YENI_KATEGORI = 7;

    //elemana tıklandigi zaman yapilacak islemler
    public static final int OLAY_ICINE_GIR = 0;//tıklama elemanın içine girer
    public static final int OLAY_SECIM_YAP = 1;//tıklama elemanı seçer

    //elemanlarin durumlari
    public static final String DURUM_YENI = "0";
    public static final String DURUM_TAMAMLANDI = "1";

    //elemanlari secme islemleri
    public static final int SECIM_YAPILDI = 1;
    public static final int SECIM_IPTAL_EDILDI = 0;

    //ontanimli renkler
    public static final String KATEGORI_ONTANIMLI_RENK = "#000000";
    public static final String KAYIT_ONTANIMLI_RENK = RENK_YESIL;
    public static final String ACTIONBAR_ARKAPLAN_SECILI = "#FF2222";

    //xml documentleri
    public static final int DOCUMENT_ASIL = 0;
    public static final int DOCUMENT_AYAR = 1;

    //aletdialogda gosterilecek view
    public static final int ALERTDIALOG_EDITTEXT = 0;//alertdialog ta edittex cikacak
    public static final int ALERTDIALOG_TEXTVIEW = 1;//alertdialog ta textview cikacak
    public static final int ALERTDIALOG_CUSTOM_VIEW = 2;//alertdialog ta hazırlanmış view gosterilecek

    //ayarlar ekraninda ayar degerini girmek kullanilacak view
    public static final int SECENEK_EDITTEXT = 0;
    public static final int SECENEK_CHECKBOX = 1;
    public static final int SECENEK_BUTTON = 2;

    //actionbar basliginin actionbar boyutuna orani
    public static final double ORAN_DIKEY = 0.3;
    public static final double ORAN_YATAY = 0.5;

    //ayarlarin idleri ve ontanimli degerleri
    public static final int AYAR_ID_SATIR_BASINA_KAYIT_SAYISI = 1;
    public static final String ONTANIMLI_DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI = "1";
    public static final int AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN = 2;
    public static final String ONTANIMLI_DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN = "0";
    public static final int AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI = 3;
    public static final String ONTANIMLI_DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI = "5";
    public static final int AYAR_ID_SIMGE_RENGI = 4;
    public static final String ONTANIMLI_DEGER_AYAR_SIMGE_RENGI = RENK_TURUNCU;
    //public static final int AYAR_ID_YAZI_RENGI = 5;
    //public static final String ONTANIMLI_DEGER_AYAR_YAZI_RENGI = RENK_TURUNCU;
    public static final int AYAR_ID_CERCEVE_GOZUKSUN = 6;
    public static final String ONTANIMLI_DEGER_AYAR_CERCEVE_GOZUKSUN = "1";
    public static final int AYAR_ID_CERCEVE_RENGI = 7;
    public static final String ONTANIMLI_DEGER_AYAR_CERCEVE_RENGI = RENK_SIYAH;
    public static final int AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN = 8;
    public static final String ONTANIMLI_DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN = "0";
    public static final int AYAR_ID_ARKAPLAN_RENGI = 9;
    public static final String ONTANIMLI_DEGER_AYAR_ARKAPLAN_RENGI = RENK_BEYAZ;
    public static final int AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN = 10;
    public static final String ONTANIMLI_DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN = "0";
    public static final int AYAR_ID_ACTIONBAR_RENGI = 11;
    public static final String ONTANIMLI_DEGER_AYAR_ACTIONBAR_RENGI = RENK_BEYAZ;

    //actionbar tuslarinin yapacagi islemler
    public static final int ACTION_ANA_EKRAN_KATEGORI_EKLE = 0;
    public static final int ACTION_ANA_EKRAN_KAYIT_EKLE = 1;
    public static final int ACTION_ANA_EKRAN_RENK_DEGISTIR = 2;
    public static final int ACTION_ANA_EKRAN_YEDEKLE = 3;
    public static final int ACTION_ANA_EKRAN_YEDEKLERI_GOSTER = 4;
    public static final int ACTION_ANA_EKRAN_YEDEGI_YUKLE = 5;
    public static final int ACTION_ANA_EKRAN_AYARLAR = 6;
    public static final int ACTION_SECIM_SIL = 7;
    public static final int ACTION_SECIM_YENI = 8;
    public static final int ACTION_SECIM_TAMAM = 9;
    public static final int ACTION_DEGISTIR_RENK_DEGISTIR = 10;
    public static final int ACTION_DEGISTIR_KAYDET = 11;
    public static final int ACTION_DEGISTIR_YENI = 12;
    public static final int ACTION_DEGISTIR_TAMAM = 13;
    public static final int ACTION_DEGISTIR_SIL = 14;
    public static final int ACTION_YEDEK_SIL = 15;
    public static final int ACTION_AYAR_ONTANIMLI = 16;
    public static final int ACTION_AYAR_KAYDET = 17;
    public static final int ACTION_YENI_KAYIT_KAYDET = 18;
    public static final int ACTION_YENI_KAYIT_RENK_DEGISTIR = 19;
    public static final int ACTION_YENI_KATEGORI_KAYDET = 20;
    public static final int ACTION_YENI_KATEGORI_RENK_DEGISTIR = 21;
}
