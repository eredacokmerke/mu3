package com.example.ekcdr.uygulama3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MainActivity extends ActionBarActivity
{
    public static String xmlYedekKlasorYolu;
    public static int tumEkranEnUzunlugu;
    public static int ekranEnUzunlugu;
    public static int elemanEnUzunluğu;
    public static int elemanBoyUzunluğu;
    public static String DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI;
    public static String DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN;
    public static String DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI;
    public static String DEGER_AYAR_SIMGE_RENGI;
    //public static String DEGER_AYAR_YAZI_RENGI;
    public static String DEGER_AYAR_CERCEVE_GOZUKSUN;
    public static String DEGER_AYAR_CERCEVE_RENGI;
    public static String DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN;
    public static String DEGER_AYAR_ARKAPLAN_RENGI;
    public static String DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN;
    public static String DEGER_AYAR_ACTIONBAR_RENGI;
    public static Resources mResources;
    public static Context cnt;
    private static String xmlDosyaYolu;
    private static String xmlAyarDosyaYolu;
    private static int ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;//actionbar turunu tutar. ture göre simgeler deiğişiyor
    private static int FRAGMENT_ETKIN_EKRAN;//uyuglama hangi fragment turunun içinde
    private static int TIKLAMA_OLAYI;//elemana tıklanıldığı zaman ne olacak
    private static String xmlParcaID = "0";//içinde olunan parçanın id si
    private static int xmlEnBuyukID;//eklenen kategori ve kayıtlara id verebilmek için
    private static View activityRootView;
    private static Document document;
    private static Document documentAyar;
    private static int actionBarBoy;
    private static List<String> listeRenkler;
    //private static String kategoriBasligi = "";//kayıt ekranındayken ekran dönerse baslik siliniyor. tekrar yazırabilmek için
    private static int llBaslikID = 1000;
    private static int etBaslikID = 1001;
    private static int etKayitID = 1002;
    private static PlaceholderFragment frag;
    private static ActionBarDrawerToggle mDrawerToggle;
    private static DrawerLayout mDrawer;//yan panel
    private static LinearLayout mDrawerLayout;//yan panel içindeki layout

    public static List<String> getListeRenkler()
    {
        return listeRenkler;
    }

    //xml i okumank için Document nesnesi olusturur
    public static Document xmlDocumentNesnesiOlustur(String xmlDosyaYolu)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

            return doc;
        }
        catch (ParserConfigurationException e)
        {
            ekranaHataYazdir("28", cnt.getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (FileNotFoundException e)
        {
            ekranaHataYazdir("29", cnt.getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (IOException e)
        {
            ekranaHataYazdir("30", cnt.getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (SAXException e)
        {
            ekranaHataYazdir("15", cnt.getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
    }

    //elemanların en uzunluğunu hesaplar
    public static void elemanEniniHesapla()
    {
        tumEkranEnUzunlugu = mResources.getDisplayMetrics().widthPixels;
        float fazlalık = (mResources.getDimension(R.dimen.activity_horizontal_margin) * 2);
        fazlalık = fazlalık + dpGetir(3) * ((Integer.valueOf(DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI)) - 1);
        ekranEnUzunlugu = (int) (tumEkranEnUzunlugu - fazlalık);

        elemanEnUzunluğu = ekranEnUzunlugu / Integer.valueOf(DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI);
    }

    //elemanların boy uzunluğunu hesaplar
    public static void elemanBoyunuHesapla()
    {
        int ekranBoyUzunluğu = mResources.getDisplayMetrics().heightPixels;
        float fazlalık = (mResources.getDimension(R.dimen.activity_horizontal_margin) * 2);
        fazlalık = fazlalık + actionBarBoy + dpGetir(30) + dpGetir(3) * ((Integer.valueOf(DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI)) - 1);
        elemanBoyUzunluğu = (int) ((ekranBoyUzunluğu - fazlalık) / Integer.valueOf(DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI));
    }

    //px birimini dp ye cevirir
    public static float dpGetir(int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, mResources.getDisplayMetrics());
    }

    public static void ekranaHataYazdir(String id, String hata)
    {
        Toast.makeText(cnt, cnt.getString(R.string.hata) + "[" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
    }

    /*
    //kategorinin renk degerinin dondurur
    public static String kategoriRenkBilgisiniGetir(String kategoriID)
    {
        Element element = document.getElementById(kategoriID);

        Node nodeRenk = etiketiGetir(element, Sabit.XML_RENK);
        if (nodeRenk == null)
        {
            ekranaHataYazdir("39", cnt.getString(R.string.xml_renk_etiketi_getirilemedi));
            return Sabit.KATEGORI_ONTANIMLI_RENK;
        }
        else
        {
            return nodeRenk.getTextContent();
        }
    }
    */

    public static String etiketBilgisiniGetir(Element element, String xmlEtiketi)
    {
        Node node = etiketiGetir(element, xmlEtiketi);
        if (node == null)
        {
            ekranaHataYazdir("39", cnt.getString(R.string.xml_etiket_okunamadi) + " : " + xmlEtiketi);
            return "";
        }
        else
        {
            return node.getTextContent();
        }
    }

    //etiket isminden etiketi bulur
    public static Node etiketiGetir(Element element, String etiketAdi)
    {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            if (nodeList.item(i).getNodeName().equals(etiketAdi))
            {
                return nodeList.item(i);
            }
        }
        return null;
    }

    public void yanEkraniOlustur()
    {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout = (LinearLayout) findViewById(R.id.left_drawer);

        Button btnYedekle = new Button(this);
        btnYedekle.setAllCaps(false);
        btnYedekle.setText(getString(R.string.yedekle));
        btnYedekle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                frag.xmlYedekle();
                mDrawer.closeDrawers();
            }
        });
        mDrawerLayout.addView(btnYedekle);

        Button btnYedegiYukle = new Button(this);
        btnYedegiYukle.setAllCaps(false);
        btnYedegiYukle.setText(getString(R.string.yedegi_yukle));
        mDrawerLayout.addView(btnYedegiYukle);
        btnYedegiYukle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                frag.xmlYedektenYukle();
                mDrawer.closeDrawers();
            }
        });

        Button btnYedekDosyalari = new Button(this);
        btnYedekDosyalari.setText(getString(R.string.yedek_dosyalari));
        btnYedekDosyalari.setAllCaps(false);
        mDrawerLayout.addView(btnYedekDosyalari);
        btnYedekDosyalari.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                frag.actionBarDegistir(Sabit.ACTIONBAR_YEDEK);
                frag.yedekDosyalariGoster();
                mDrawer.closeDrawers();
            }
        });

        Button btnAyarlar = new Button(this);
        btnAyarlar.setAllCaps(false);
        btnAyarlar.setText(getString(R.string.ayarlar));
        mDrawerLayout.addView(btnAyarlar);
        btnAyarlar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                frag.ayarEkraniniAc();
                mDrawer.closeDrawers();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawer,         /* DrawerLayout object */
                R.drawable.menu,  /* nav drawer icon to replace 'Up' caret */
                R.string.dosya,  /* "open drawer" description */
                R.string.kaydet  /* "close drawer" description */
        )
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                //getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                //getSupportActionBar().setTitle(mTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawer.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yanEkraniOlustur();

        File uygulamaKlasoru;
        File xmlYedekKlasoru;
        mResources = getResources();
        cnt = getApplicationContext();

        activityRootView = findViewById(R.id.container);
        uygulamaKlasoru = uygulamaKlasoruKontrolEt();
        xmlYedekKlasoru = xmlYedekKlasoruKontrolEt();
        if (uygulamaKlasoru != null && xmlYedekKlasoru != null)
        {
            xmlYedekKlasorYolu = xmlYedekKlasoru.getAbsolutePath();

            if (uygulamaKlasoru.exists())
            {
                xmlDosyaYolu = uygulamaKlasoru + "/" + Sabit.XML_DOSYA_ADI;
                xmlAyarDosyaYolu = uygulamaKlasoru + "/" + Sabit.XML_AYAR_DOSYA_ADI;

                if (xmlDosyasiKontrolEt() && xmlAyarDosyasiKontrolEt())//xml dosyaları ile ilgili hata yoksa devam etsin, varsa uygulamayı sonlandırsın
                {
                    Element element = document.getElementById("0");
                    if (savedInstanceState == null)
                    {
                        String renk = etiketBilgisiniGetir(element, Sabit.XML_RENK);
                        String baslik = etiketBilgisiniGetir(element, Sabit.XML_BASLIK);
                        String durum = etiketBilgisiniGetir(element, Sabit.XML_DURUM);

                        CustomRelativeLayout crl = new CustomRelativeLayout(cnt, baslik, renk, durum, 0);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
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
                }
                else
                {
                    ekranaHataYazdir("45", cnt.getString(R.string.xml_hata_olustu));
                    finish();
                }
            }
            else
            {
                ekranaHataYazdir("1", cnt.getString(R.string.uygulama_klasoru_yok) + uygulamaKlasoru);
                finish();
            }
        }
        else
        {
            ekranaHataYazdir("37", cnt.getString(R.string.klasor_hata_olustu));
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //actionbarın solundaki geri oku
    public void geriSimgesiniEkle(String renk)
    {
        final Drawable upArrow = getResources().getDrawable(R.drawable.geri);
        upArrow.setColorFilter(Color.parseColor(renk), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    //actionbarın solundaki geri oku
    public void geriSimgesiniEkle()
    {
        final Drawable upArrow = getResources().getDrawable(R.drawable.geri);
        upArrow.setColorFilter(Color.parseColor(DEGER_AYAR_SIMGE_RENGI), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void menuSimgesiEkle()
    {
        final Drawable upArrow = getResources().getDrawable(R.drawable.menu);
        upArrow.setColorFilter(Color.parseColor(DEGER_AYAR_SIMGE_RENGI), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void actionBarBoyUzunlugunuGetir()
    {
        TypedValue tv = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        actionBarBoy = getResources().getDimensionPixelSize(tv.resourceId);
    }

    //xml in duracagı klasoru olusturur
    public File uygulamaKlasoruKontrolEt()
    {
        File uygKlasoru;

        if (hariciAlanVarMi())//sdcard var
        {
            uygKlasoru = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Sabit.UYGULAMA_ADI);
        }
        else//sdcard yok
        {
            uygKlasoru = getDir(Sabit.UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!uygKlasoru.exists())
        {
            if (!uygKlasoru.mkdirs())
            {
                ekranaHataYazdir("2", cnt.getString(R.string.uygulama_klasoru_olusturulamadi) + ", " + cnt.getString(R.string.klasor) + " : " + uygKlasoru);
                return null;
            }
        }

        return uygKlasoru;
    }

    public File xmlYedekKlasoruKontrolEt()
    {
        File xmlYedekKlasoru;

        if (hariciAlanVarMi())//sdcard var
        {
            xmlYedekKlasoru = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Sabit.UYGULAMA_ADI + "/" + Sabit.YEDEK_KLASORU_ADI);
        }
        else//sdcard yok
        {
            xmlYedekKlasoru = getDir(Sabit.UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!xmlYedekKlasoru.exists())
        {
            if (!xmlYedekKlasoru.mkdirs())
            {
                ekranaHataYazdir("3", cnt.getString(R.string.yedek_klasoru_olusturulumadi) + ", " + cnt.getString(R.string.klasor) + " : " + xmlYedekKlasoru);
                return null;
            }
        }

        return xmlYedekKlasoru;
    }

    //xml dosyası var mı diye kontrol ediyor. yoksa oluşturuyor ve en buyuk xml id sini buluyor
    public boolean xmlDosyasiKontrolEt()
    {
        if (new File(xmlDosyaYolu).exists())//xml dosyası var mı
        {
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
        }
        else
        {
            xmlDosyasiOlustur();
            xmlEnBuyukID = 0;
            document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);
            return true;
        }
    }

    //uygulama baslarken sistemde ayar dosyası var mı diye bakar. varsa ayarları alır. yoksa öntanımlı ayarla ile dosyayı olusturur
    public boolean xmlAyarDosyasiKontrolEt()
    {
        if (new File(xmlAyarDosyaYolu).exists())//xml ayar dosyası var mı
        {
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
        }
        else
        {
            xmlAyarDosyasiOlustur();
            documentAyar = xmlDocumentNesnesiOlustur(xmlAyarDosyaYolu);
            yeniAyarVarsaEkle();
            ayarlariOku();
            return true;
        }
    }

    //yeni ayar varsa xml ayar dosyasına ekle
    private void yeniAyarVarsaEkle()
    {
        boolean eksikAyarVarMi = false;
        List<Integer> xmldekiAyarlar = new ArrayList<>();
        Element element = documentAyar.getDocumentElement();
        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node nodeAyar = nodeList.item(i);
            int ayarID = Integer.valueOf(nodeAyar.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue());

            xmldekiAyarlar.add(ayarID);
        }

        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);//ayar isimli etiket olşuturuluyor
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI));//ayar etiketine id veriliyor
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI);//ayar etiketinin icerigi yazılıyor
            element.appendChild(elementAyar);//root etiketine ayar etiketi ekleniyor
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_SIMGE_RENGI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_SIMGE_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_SIMGE_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        /*
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_YAZI_RENGI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_YAZI_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_YAZI_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        */
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_CERCEVE_GOZUKSUN))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_CERCEVE_GOZUKSUN));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_CERCEVE_GOZUKSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_CERCEVE_RENGI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_CERCEVE_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_CERCEVE_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_ARKAPLAN_RENGI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_ARKAPLAN_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_ARKAPLAN_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_ACTIONBAR_RENGI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_ACTIONBAR_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_ACTIONBAR_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }

        if (eksikAyarVarMi)
        {
            try
            {
                documentAyar.normalize();
                //document i string e çeviriyor
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(documentAyar), new StreamResult(writer));
                String output = "<?xml version=\"1.0\"?>" + writer.getBuffer().toString().replaceAll("\n|\r", "");
                ///////////////////////////////
                //string i xml dosyasına yazıyor
                BufferedWriter out = new BufferedWriter(new FileWriter(xmlAyarDosyaYolu));
                out.write(output);
                out.close();
                //////////////////////////////////
            }
            catch (TransformerConfigurationException e)
            {
                ekranaHataYazdir("31", cnt.getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
            catch (TransformerException e)
            {
                ekranaHataYazdir("32", cnt.getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
            catch (IOException e)
            {
                ekranaHataYazdir("33", cnt.getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
        }
    }

    //uygulama açılırken gecerli ayarları xml den okur
    private void ayarlariOku()
    {
        Element element = documentAyar.getDocumentElement();
        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node nodeAyar = nodeList.item(i);
            String ayarDeger = nodeAyar.getTextContent();
            int ayarID = Integer.valueOf(nodeAyar.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue());

            switch (ayarID)
            {
                case Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI:
                    DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI = ayarDeger;
                    break;

                case Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN:
                    DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN = ayarDeger;
                    break;

                case Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI:
                    DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI = ayarDeger;
                    break;

                case Sabit.AYAR_ID_SIMGE_RENGI:
                    DEGER_AYAR_SIMGE_RENGI = ayarDeger;
                    break;

                /*
                case Sabit.AYAR_ID_YAZI_RENGI:
                    DEGER_AYAR_YAZI_RENGI = ayarDeger;
                    break;
                */

                case Sabit.AYAR_ID_CERCEVE_GOZUKSUN:
                    DEGER_AYAR_CERCEVE_GOZUKSUN = ayarDeger;
                    break;

                case Sabit.AYAR_ID_CERCEVE_RENGI:
                    DEGER_AYAR_CERCEVE_RENGI = ayarDeger;
                    break;

                case Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN:
                    DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN = ayarDeger;
                    break;

                case Sabit.AYAR_ID_ARKAPLAN_RENGI:
                    DEGER_AYAR_ARKAPLAN_RENGI = ayarDeger;
                    break;

                case Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN:
                    DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN = ayarDeger;
                    break;

                case Sabit.AYAR_ID_ACTIONBAR_RENGI:
                    DEGER_AYAR_ACTIONBAR_RENGI = ayarDeger;
                    break;

                default:
                    ekranaHataYazdir("7", cnt.getString(R.string.hatali_ayar_id) + " : " + ayarID);
            }
        }
    }

    //sdcard ın olup olmadığını kontrol ediyor
    public boolean hariciAlanVarMi()
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

    //uygulama açıldığında xml dosyasındaki en büyük id yi buluyor ve id vermeye o sayıdan devam ediyor
    public int enBuyukIDyiBul()
    {
        int sonIDParca = 0;
        int sonIDKayit = 0;

        NodeList nodeListParca = document.getElementsByTagName(Sabit.XML_PARCA);
        NodeList nodeListKayit = document.getElementsByTagName(Sabit.XML_KAYIT);

        for (int i = 0; i < nodeListParca.getLength(); i++)
        {
            if (Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue()) > sonIDParca)
            {
                sonIDParca = Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue());
            }
        }
        for (int i = 0; i < nodeListKayit.getLength(); i++)
        {
            if (Integer.parseInt(nodeListKayit.item(i).getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue()) > sonIDKayit)
            {
                sonIDKayit = Integer.parseInt(nodeListKayit.item(i).getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue());
            }
        }

        if (sonIDKayit > sonIDParca)
        {
            return sonIDKayit;
        }
        else
        {
            return sonIDParca;
        }
    }

    //xml dosyası yoksa oluşturuyor
    public void xmlDosyasiOlustur()
    {
        try
        {
            //string i xml dosyasına yazıyor
            BufferedWriter out = new BufferedWriter(new FileWriter(xmlDosyaYolu));

            out.write("<?xml version=\"1.0\"?><" + Sabit.XML_ROOT + ">" +
                    "<" + Sabit.XML_PARCA + " " + Sabit.XML_ID + "=\"0\">" +
                    "<" + Sabit.XML_BASLIK + "/>" +
                    "<" + Sabit.XML_RENK + ">" + Sabit.KATEGORI_ONTANIMLI_RENK + "</" + Sabit.XML_RENK + ">" +
                    "<" + Sabit.XML_YAZILAR + "/>" +
                    "<" + Sabit.XML_DURUM + ">" + Sabit.DURUM_YENI + "</" + Sabit.XML_DURUM + ">" +
                    "<" + Sabit.XML_ALTPARCA + "/>" +
                    "</" + Sabit.XML_PARCA + ">" +
                    "</" + Sabit.XML_ROOT + ">");
            out.close();
        }
        catch (IOException e)
        {
            ekranaHataYazdir("8", cnt.getString(R.string.xml_olusturulamadi) + e.getMessage() + ", " + cnt.getString(R.string.dosya) + " : " + xmlDosyaYolu);
        }
    }

    public void xmlAyarDosyasiOlustur()
    {
        try
        {
            //string i xml dosyasına yazıyor
            BufferedWriter out = new BufferedWriter(new FileWriter(xmlAyarDosyaYolu));
            out.write("<?xml version=\"1.0\"?><root>" +
                    "<ayar id=\"" + Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI + "</ayar>" +
                    "<ayar id=\"" + Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN + "</ayar>" +
                    "<ayar id=\"" + Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI + "</ayar>" +
                    "<ayar id=\"" + Sabit.AYAR_ID_SIMGE_RENGI + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_SIMGE_RENGI + "</ayar>" +
                    //"<ayar id=\"" + Sabit.AYAR_ID_YAZI_RENGI + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_YAZI_RENGI + "</ayar>" +
                    "</root>");
            out.close();
        }
        catch (IOException e)
        {
            ekranaHataYazdir("9", cnt.getString(R.string.xml_olusturulamadi) + " : " + e.getMessage() + "," + cnt.getString(R.string.dosya) + " : " + xmlAyarDosyaYolu);
        }
    }

    //actionBar'ın arkaplan rengini degiştirir
    public void actionBarArkaPlanDegistir(String renk)
    {
        ColorDrawable actionBarArkaPlan = new ColorDrawable(Color.parseColor(renk));
        getSupportActionBar().setBackgroundDrawable(actionBarArkaPlan);
    }

    @Override
    public void onBackPressed()
    {
        PlaceholderFragment fr = (PlaceholderFragment) getFragmentManager().findFragmentByTag(Sabit.FRAGMENT_TAG);
        switch (FRAGMENT_ETKIN_EKRAN)
        {
            case Sabit.FRAGMENT_YEDEK_EKRANI:
            case Sabit.FRAGMENT_AYAR_EKRANI:
                fr.parseXml(xmlParcaID);
                fr.ustSeviyeyiGetir();
                break;

            case Sabit.FRAGMENT_KAYIT_EKRANI:
            case Sabit.FRAGMENT_YENI_KAYIT_EKRANI:
            case Sabit.FRAGMENT_YENI_KATEGORI_EKRANI:
                fr.ustSeviyeyiGetir();
                break;

            default:
                if (xmlParcaID.equals("0"))//en ust seviyede ise uygulamadan çıksın
                {
                    finish();
                }
                else
                {
                    fr.ustSeviyeyiGetir();
                }
        }
    }

    public static class PlaceholderFragment extends Fragment
    {
        public static List<YedekRelativeLayout> listSeciliYedek;//seçilen yedeklerin listesi
        private static List<String> listSeciliElemanDurumu;//seçilen elemanların durumlarının listesi
        private static List<CustomRelativeLayout> listSeciliCRL;//seçilen elemanların listesi
        private static CustomRelativeLayout seciliCRL;//içine girilen kaydın nesnesi
        private static Yerlesim globalYerlesim;
        private static MainActivity ma;//MainActivity fonksiyonlarını calistirabilmek için
        private static int elemanTuru;
        private static View fragmentRootView;//fragment icindeki layoutlara ulasabilmek için fragment view ı
        private static View fragmentRootViewYeniKayit;//fragment icindeki layoutlara ulasabilmek için fragment view ı
        private static RelativeLayout anaLinearLayout;//scrollview ın içine yerleşeceği linear layout
        private RelativeLayout anaRelativeLayout;//scrollview ın içinde viewların içine yerleşeceği relative layout
        private MenuInflater inflaterActionBar;
        private Menu menuActionBar;
        private EditText etDegisecek;//kayit degiştirmeye tıklandığı zaman olusan edittext
        private Activity fAct;
        private AlertDialog alertRenk;//renkleri soran alertDialog. renk dugmesine dokunuca alertDialog^u kapatabilmek için

        public PlaceholderFragment()
        {
            frag = this;
            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();
            listSeciliCRL = new ArrayList<>();
        }

        public static PlaceholderFragment newInstanceKategori(CustomRelativeLayout crl)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            //FRAGMENT_ETKIN_EKRAN = secim;
            FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_KATEGORI_EKRANI;

            xmlParcaID = String.valueOf(crl.getId());
            fragment.setHasOptionsMenu(true);

            ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;
            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;
            seciliCRL = crl;
            elemanTuru = Sabit.ELEMAN_TUR_KATEGORI;

            return fragment;
        }

        public static PlaceholderFragment newInstanceKategoriDuzenle()
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_KATEGORI_DUZENLE_EKRANI;

            //xmlParcaID = String.valueOf(crl.getId());
            fragment.setHasOptionsMenu(true);

            ACTIONBAR_TUR = Sabit.ACTIONBAR_KATEGORI_DEGISTIR;
            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;
            //seciliCRL = crl;
            elemanTuru = Sabit.ELEMAN_TUR_KATEGORI;

            return fragment;
        }

        public static PlaceholderFragment newInstanceKayit(CustomRelativeLayout crl)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_KAYIT_EKRANI;
            fragment.setHasOptionsMenu(true);

            ACTIONBAR_TUR = Sabit.ACTIONBAR_DEGISTIR;
            seciliCRL = crl;
            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;
            elemanTuru = Sabit.ELEMAN_TUR_KAYIT;

            return fragment;
        }

        public static PlaceholderFragment newInstanceYeni(int tur)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setHasOptionsMenu(true);
            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;

            switch (tur)
            {
                case Sabit.ELEMAN_TUR_YENI_KAYIT:
                    FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_YENI_KAYIT_EKRANI;
                    ACTIONBAR_TUR = Sabit.ACTIONBAR_YENI_KAYIT;
                    elemanTuru = Sabit.ELEMAN_TUR_YENI_KAYIT;
                    break;

                case Sabit.ELEMAN_TUR_YENI_KATEGORI:
                    FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_YENI_KATEGORI_EKRANI;
                    ACTIONBAR_TUR = Sabit.ACTIONBAR_YENI_KATEGORI;
                    elemanTuru = Sabit.ELEMAN_TUR_YENI_KATEGORI;
                    break;
            }


            /*
            PlaceholderFragment fragment = new PlaceholderFragment();
            FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_YENI_KAYIT_EKRANI;
            fragment.setHasOptionsMenu(true);

            ACTIONBAR_TUR = Sabit.ACTIONBAR_YENI_KAYIT;
            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;

            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();
            listSeciliCRL = new ArrayList<>();
            elemanTuru = Sabit.ELEMAN_TUR_YENI_KAYIT;
            */

            return fragment;
        }

        public static PlaceholderFragment newInstanceYedek()
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_YEDEK_EKRANI;
            fragment.setHasOptionsMenu(true);

            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;
            elemanTuru = Sabit.ELEMAN_TUR_YEDEK;

            return fragment;
        }

        public static PlaceholderFragment newInstanceAyarlar()
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            //Bundle args = new Bundle();
            FRAGMENT_ETKIN_EKRAN = Sabit.FRAGMENT_AYAR_EKRANI;
            //fragment.setArguments(args);
            fragment.setHasOptionsMenu(true);

            ACTIONBAR_TUR = Sabit.ACTIONBAR_AYAR;
            elemanTuru = Sabit.ELEMAN_TUR_AYARLAR;

            return fragment;
        }

        /*
        //kaydin renk degerinin dondurur
        public static String kayitRenkBilgisiniGetir(String kayitID)
        {
            Element element = document.getElementById(kayitID);

            Node nodeRenk = etiketiGetir(element, Sabit.XML_RENK);
            if (nodeRenk == null)
            {
                ekranaHataYazdir("48", cnt.getString(R.string.xml_renk_etiketi_getirilemedi));
                return Sabit.KAYIT_ONTANIMLI_RENK;
            }
            else
            {
                return nodeRenk.getTextContent();
            }
        }
        */

        //kutuların ekranda yerlesimini ayarlamak için yerlesim nesnesi
        public static void yeniYerlesimOlustur()
        {
            globalYerlesim = new Yerlesim(Integer.valueOf(DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI));
        }

        public static int getElemanTuru()
        {
            return elemanTuru;
        }

        //verilen renge göre ekran engini degistirir
        public static void ekranRenginiDegistir(String renk)
        {
            anaLinearLayout.setBackgroundColor(Color.parseColor(renk));
            //LinearLayout ll = (LinearLayout) fragmentRootView.findViewById(R.id.anaLinearLayout);
            //ll.setBackgroundColor(Color.parseColor(renk));
            //ll.getBackground().setAlpha(128);
        }

        public CustomRelativeLayout findCRLbyID(int id)
        {
            CustomRelativeLayout crl = (CustomRelativeLayout) anaRelativeLayout.findViewById(id);
            return crl;
        }

        //parca etiketinin altındaki yazi ve kategorileri ekrana basıyor
        public void parseXml(String parcaID)
        {
            Element element = document.getElementById(parcaID);
            NodeList nodeList = element.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeName().equals(Sabit.XML_YAZILAR))
                {
                    Node nodeYazilar = nodeList.item(i);
                    NodeList nodeListKayitlar = nodeYazilar.getChildNodes();

                    for (int j = 0; j < nodeListKayitlar.getLength(); j++)
                    {
                        Node nodeKayit = nodeListKayitlar.item(j);

                        String kayitBaslik = etiketBilgisiniGetir((Element) nodeKayit, Sabit.XML_BASLIK);
                        String kayitRenk = etiketBilgisiniGetir((Element) nodeKayit, Sabit.XML_RENK);
                        String kayitYazi = etiketBilgisiniGetir((Element) nodeKayit, Sabit.XML_YAZI);
                        String kayitDurum = etiketBilgisiniGetir((Element) nodeKayit, Sabit.XML_DURUM);

                        /*
                        String kayitBaslik = "";
                        String kayitYazi = "";
                        String kayitRenk = Sabit.KAYIT_ONTANIMLI_RENK;
                        Node nodeKayitBaslik = etiketiGetir((Element) nodeKayit, Sabit.XML_BASLIK);
                        if (nodeKayitBaslik == null)
                        {
                            ekranaHataYazdir("53", cnt.getString(R.string.xml_etiket_okunamadi));
                        }
                        else
                        {
                            kayitBaslik = nodeKayitBaslik.getTextContent();
                        }
                        Node nodeKayitYazi = etiketiGetir((Element) nodeKayit, Sabit.XML_YAZI);
                        if (nodeKayitYazi == null)
                        {
                            ekranaHataYazdir("40", cnt.getString(R.string.xml_etiket_okunamadi));
                        }
                        else
                        {
                            kayitYazi = nodeKayitYazi.getTextContent();
                        }
                        Node nodeRenk = etiketiGetir((Element) nodeKayit, Sabit.XML_RENK);
                        if (nodeRenk == null)
                        {
                            ekranaHataYazdir("47", cnt.getString(R.string.xml_etiket_okunamadi));
                        }
                        else
                        {
                            kayitRenk = nodeRenk.getTextContent();
                        }
                        String kayitDurum = nodeKayit.getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue();
                        */

                        String kayitID = nodeKayit.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue();

                        kayitlariAnaEkranaEkle(kayitBaslik, kayitYazi, Integer.parseInt(kayitID), kayitDurum, kayitRenk, globalYerlesim);
                    }
                }
                else if (nodeList.item(i).getNodeName().equals(Sabit.XML_ALTPARCA))
                {
                    Node nodeAltParca = nodeList.item(i);
                    NodeList nodeListParcalar = nodeAltParca.getChildNodes();

                    for (int j = 0; j < nodeListParcalar.getLength(); j++)
                    {
                        Node nodeParca = nodeListParcalar.item(j);

                        String kategoriBaslik = etiketBilgisiniGetir((Element) nodeParca, Sabit.XML_BASLIK);
                        String kategoriRenk = etiketBilgisiniGetir((Element) nodeParca, Sabit.XML_RENK);
                        String kategoriDurum = etiketBilgisiniGetir((Element) nodeParca, Sabit.XML_DURUM);


                        /*
                        Node nodeBaslik = etiketiGetir((Element) nodeParca, Sabit.XML_BASLIK);

                        String kategoriBaslik = "";
                        String kategoriRenk = Sabit.KATEGORI_ONTANIMLI_RENK;
                        if (nodeBaslik == null)
                        {
                            ekranaHataYazdir("41", cnt.getString(R.string.xml_etiket_okunamadi));
                        }
                        else
                        {
                            kategoriBaslik = nodeBaslik.getTextContent();
                        }

                        Node nodeRenk = etiketiGetir((Element) nodeParca, Sabit.XML_RENK);
                        if (nodeRenk == null)
                        {
                            ekranaHataYazdir("42", cnt.getString(R.string.xml_etiket_okunamadi));
                        }
                        else
                        {
                            kategoriRenk = nodeRenk.getTextContent();
                        }
                        */

                        //String kategoriDurum = nodeParca.getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue();
                        String kategoriID = nodeParca.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue();

                        kategoriyiAnaEkranaEkle(kategoriBaslik, Integer.parseInt(kategoriID), kategoriDurum, kategoriRenk, globalYerlesim);
                    }
                }
            }
        }

        //secilen elemanların durum bilgilerine göre actionBar daki simgeleri gizler, gösterir
        public void secimEkranindaDurumuKontrolEt(String durum, int eylem)
        {
            switch (eylem)
            {
                case Sabit.SECIM_YAPILDI:
                    listSeciliElemanDurumu.add(durum);
                    break;

                case Sabit.SECIM_IPTAL_EDILDI:
                    listSeciliElemanDurumu.remove(listSeciliElemanDurumu.indexOf(durum));
                    break;

                default:
                    ekranaHataYazdir("34", cnt.getString(R.string.hatali_secim_eylemi) + " : " + eylem);
            }

            if (listSeciliElemanDurumu.contains(Sabit.DURUM_YENI))
            {
                menuActionBar.findItem(Sabit.ACTION_SECIM_TAMAM).setVisible(true);
                // menuActionBar.findItem(R.id.Sabit.ACTION_SECIM_TAMAM).setVisible(true);
            }
            else
            {
                menuActionBar.findItem(Sabit.ACTION_SECIM_TAMAM).setVisible(false);
                //   menuActionBar.findItem(R.id.Sabit.ACTION_SECIM_TAMAM).setVisible(false);
            }
            if (listSeciliElemanDurumu.contains(Sabit.DURUM_TAMAMLANDI))
            {
                menuActionBar.findItem(Sabit.ACTION_SECIM_YENI).setVisible(true);
                // menuActionBar.findItem(R.id.action_secim_yeni).setVisible(true);
            }
            else
            {
                menuActionBar.findItem(Sabit.ACTION_SECIM_YENI).setVisible(false);
                // menuActionBar.findItem(R.id.action_secim_yeni).setVisible(false);
            }
        }

        //xml parse edildikten sonra kayitları ana ekrana ekler
        public void kayitlariAnaEkranaEkle(String baslik, String yazi, final int eklenenID, final String durum, String renk, Yerlesim ylsm)
        {
            final CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), baslik, yazi, Sabit.ELEMAN_TUR_KAYIT, eklenenID, durum, renk, this, ylsm);

            if (durum.equals(Sabit.DURUM_TAMAMLANDI))
            {
                crl.getTvTik().setText(Sabit.TIK_UNICODE);
            }
            else
            {
                crl.getTvTik().setText("");
            }
            crl.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if (!mDrawer.isDrawerOpen(mDrawerLayout))
                    {
                        if (!crl.isCrlSeciliMi())//secili eleman tekrar secilemesin
                        {
                            listSeciliCRL.add(crl);
                            crl.arkaplanSecili();
                            crl.setCrlSeciliMi(true);

                            if (TIKLAMA_OLAYI != Sabit.OLAY_SECIM_YAP)//ilk uzun basmada yapılacak işlemler
                            {
                                actionBarDegistir(Sabit.ACTIONBAR_SECIM);
                                TIKLAMA_OLAYI = Sabit.OLAY_SECIM_YAP;
                                ma.actionBarArkaPlanDegistir(Sabit.ACTIONBAR_ARKAPLAN_SECILI);
                                duzenleSimgesininGorunumunuDegistir(View.VISIBLE);
                                basligiDuzenleninYaninaAl();
                            }

                            secimEkranindaDurumuKontrolEt(crl.getDurum(), Sabit.SECIM_YAPILDI);
                        }


                    }

                    return true;
                }
            });
            crl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!mDrawer.isDrawerOpen(mDrawerLayout))
                    {
                        if (TIKLAMA_OLAYI == Sabit.OLAY_ICINE_GIR)
                        {
                            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKayit(crl)).addToBackStack(null).commit();
                        }
                        else if (TIKLAMA_OLAYI == Sabit.OLAY_SECIM_YAP)
                        {
                            if (crl.isCrlSeciliMi())
                            {
                                listSeciliCRL.remove(listSeciliCRL.indexOf(crl));
                                crl.arkaplanKayit();
                                crl.setCrlSeciliMi(false);

                                if (seciliElemanSayisi() == 0)
                                {
                                    seciliElemanListeleriniSifirla();
                                }
                                else
                                {
                                    secimEkranindaDurumuKontrolEt(crl.getDurum(), Sabit.SECIM_IPTAL_EDILDI);
                                }
                            }
                            else
                            {
                                listSeciliCRL.add(crl);
                                crl.arkaplanSecili();
                                crl.setCrlSeciliMi(true);

                                secimEkranindaDurumuKontrolEt(crl.getDurum(), Sabit.SECIM_YAPILDI);
                            }
                        }
                    }
                }
            });
            anaRelativeLayout.addView(crl);
        }

        //actionBar a yazılacak olan kategorinin yolunu getiriyor
        public String kategoriYolunuGetir(String kategoriID)
        {
            String baslik = "";
            Element elementParca = document.getElementById(kategoriID);
            baslik = elementParca.getFirstChild().getTextContent() + "/" + baslik;
            String asilBaslik = baslik;
            do
            {
                elementParca = (Element) elementParca.getParentNode().getParentNode();
                baslik = elementParca.getFirstChild().getTextContent() + "/" + baslik;
            }
            while (!elementParca.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue().equals("0"));

            //Rect bounds = new Rect();
            Paint textPaint = new Paint();
            //textPaint.getTextBounds(baslik, 0, baslik.length(), bounds);
            //int height = bounds.width();
            float height = textPaint.measureText(baslik);
            float dpWidth = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;

            float oran = height / dpWidth;
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                if (oran > Sabit.ORAN_DIKEY)
                {
                    int a = 1;//ilk / işaretini ararken ilk döngüde 1. basamaktan, sonra ise 6. basamaktan aramaya başlayacak
                    while (oran > Sabit.ORAN_DIKEY)
                    {
                        int yer = baslik.indexOf("/", a);
                        String atilacakKisim = baslik.substring(0, yer + 1);

                        baslik = baslik.replaceFirst(atilacakKisim, Sabit.UC_NOKTA);

                        //textPaint.getTextBounds(baslik, 0, baslik.length(), bounds);
                        //height = bounds.width();
                        height = textPaint.measureText(baslik);
                        oran = height / dpWidth;

                        a = 6;
                    }
                    if (baslik.equals(Sabit.UC_NOKTA))//baslik actionBar'a sığmıyor. sığdığı kadarı yazılacak
                    {
                        baslik = Sabit.UC_NOKTA + asilBaslik;
                    }
                }
            }
            else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                if (oran > Sabit.ORAN_YATAY)
                {
                    int a = 1;//ilk / işaretini ararken ilk döngüde 1. basamaktan, sonra ise 6. basamaktan aramaya başlayacak
                    while (oran > Sabit.ORAN_YATAY)
                    {
                        int yer = baslik.indexOf("/", a);
                        String atilacakKisim = baslik.substring(0, yer + 1);
                        baslik = baslik.replaceFirst(atilacakKisim, Sabit.UC_NOKTA);

                        //textPaint.getTextBounds(baslik, 0, baslik.length(), bounds);
                        //height = bounds.width();
                        height = textPaint.measureText(baslik);
                        oran = height / dpWidth;

                        a = 6;
                    }
                    if (baslik.equals(Sabit.UC_NOKTA))//baslik actionBar'a sığmıyor. sığdığı kadarı yazılacak
                    {
                        baslik = Sabit.UC_NOKTA + asilBaslik;
                    }
                }
            }

            //kategoriBasligi = baslik;

            return baslik;
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        public void kategoriyiAnaEkranaEkle(final String baslik, int kategoriID, final String durum, String renk, Yerlesim ylsm)
        {
            final CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), baslik, "", Sabit.ELEMAN_TUR_KATEGORI, kategoriID, durum, renk, this, ylsm);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran

            if (durum.equals(Sabit.DURUM_TAMAMLANDI))
            {
                crl.getTvTik().setText(Sabit.TIK_UNICODE);
            }
            else
            {
                crl.getTvTik().setText("");
            }
            crl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!mDrawer.isDrawerOpen(mDrawerLayout))
                    {
                        if (TIKLAMA_OLAYI == Sabit.OLAY_ICINE_GIR)
                        {
                            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(crl), Sabit.FRAGMENT_TAG).commit();
                        }
                        else if (TIKLAMA_OLAYI == Sabit.OLAY_SECIM_YAP)
                        {
                            if (crl.isCrlSeciliMi())
                            {
                                listSeciliCRL.remove(listSeciliCRL.indexOf(crl));
                                crl.arkaplanKategori();
                                crl.setCrlSeciliMi(false);

                                if (seciliElemanSayisi() == 0)
                                {
                                    seciliElemanListeleriniSifirla();
                                }
                                else
                                {
                                    secimEkranindaDurumuKontrolEt(crl.getDurum(), Sabit.SECIM_IPTAL_EDILDI);
                                }
                            }
                            else
                            {
                                listSeciliCRL.add(crl);
                                crl.arkaplanSecili();
                                crl.setCrlSeciliMi(true);

                                secimEkranindaDurumuKontrolEt(crl.getDurum(), Sabit.SECIM_YAPILDI);
                            }
                        }
                    }
                }
            });
            crl.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if (!mDrawer.isDrawerOpen(mDrawerLayout))
                    {
                        if (!crl.isCrlSeciliMi())//secili eleman tekrar secilemesin
                        {
                            listSeciliCRL.add(crl);
                            crl.arkaplanSecili();
                            crl.setCrlSeciliMi(true);

                            if (TIKLAMA_OLAYI != Sabit.OLAY_SECIM_YAP)//ilk uzun basmada yapılacak işlemler
                            {
                                actionBarDegistir(Sabit.ACTIONBAR_SECIM);
                                TIKLAMA_OLAYI = Sabit.OLAY_SECIM_YAP;
                                ma.actionBarArkaPlanDegistir(Sabit.ACTIONBAR_ARKAPLAN_SECILI);
                                duzenleSimgesininGorunumunuDegistir(View.VISIBLE);
                                basligiDuzenleninYaninaAl();
                            }
                            secimEkranindaDurumuKontrolEt(crl.getDurum(), Sabit.SECIM_YAPILDI);
                        }
                    }
                    return true;
                }
            });
            anaRelativeLayout.addView(crl);
        }

        //kategori secildigi zaman kategori basligini duzenle simgesini yanına alır
        public void basligiDuzenleninYaninaAl()
        {
            for (int i = 0; i < anaRelativeLayout.getChildCount(); i++)
            {
                CustomRelativeLayout c = (CustomRelativeLayout) anaRelativeLayout.getChildAt(i);
                if (c.getCrlTur() == Sabit.ELEMAN_TUR_KATEGORI)
                {
                    RelativeLayout.LayoutParams lpBaslik = (RelativeLayout.LayoutParams) c.getTvBaslik().getLayoutParams();
                    lpBaslik.addRule(RelativeLayout.LEFT_OF, c.getTvDuzenle().getId());
                    c.getTvBaslik().setLayoutParams(lpBaslik);
                }
            }
        }

        //kategori secildigi zaman kategori basligini duzenle simgesini yanına alır
        public void basligiEskiYerineAl()
        {
            for (int i = 0; i < anaRelativeLayout.getChildCount(); i++)
            {
                CustomRelativeLayout c = (CustomRelativeLayout) anaRelativeLayout.getChildAt(i);
                if (c.getCrlTur() == Sabit.ELEMAN_TUR_KATEGORI)
                {
                    RelativeLayout.LayoutParams lpBaslik = (RelativeLayout.LayoutParams) c.getTvBaslik().getLayoutParams();
                    lpBaslik.removeRule(RelativeLayout.LEFT_OF);
                    c.getTvBaslik().setLayoutParams(lpBaslik);
                }
            }
        }

        //kategori layout'undaki duzenle simgesini gösterir ve gizler
        public void duzenleSimgesininGorunumunuDegistir(int gorunum)
        {
            for (int i = 0; i < anaRelativeLayout.getChildCount(); i++)
            {
                CustomRelativeLayout crl = (CustomRelativeLayout) anaRelativeLayout.getChildAt(i);
                if (crl.getCrlTur() == Sabit.ELEMAN_TUR_KATEGORI)
                {
                    crl.getTvDuzenle().setVisibility(gorunum);
                }
            }
        }

        //Document nesnesini dosyaya yazıyor
        public void documentToFile(int tur)
        {
            try
            {
                Document doc = null;
                String dosyaYolu = "";
                switch (tur)
                {
                    case Sabit.DOCUMENT_ASIL:
                        doc = document;
                        dosyaYolu = xmlDosyaYolu;
                        break;

                    case Sabit.DOCUMENT_AYAR:
                        doc = documentAyar;
                        dosyaYolu = xmlAyarDosyaYolu;
                        break;

                    default:
                        ekranaHataYazdir("10", cnt.getString(R.string.hatali_xml_document_turu) + ", " + cnt.getString(R.string.tur) + " : " + tur);
                }

                if (doc != null)
                {
                    doc.normalize();
                    //document i string e çeviriyor
                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer transformer = tf.newTransformer();
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                    StringWriter writer = new StringWriter();
                    transformer.transform(new DOMSource(doc), new StreamResult(writer));
                    String output = "<?xml version=\"1.0\"?>" + writer.getBuffer().toString().replaceAll("\n|\r", "");
                    ///////////////////////////////
                    //string i xml dosyasına yazıyor
                    BufferedWriter out = new BufferedWriter(new FileWriter(dosyaYolu));

                    out.write(output);
                    out.close();
                    //////////////////////////////////
                }
            }
            catch (TransformerConfigurationException e)
            {
                ekranaHataYazdir("11", cnt.getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
            catch (TransformerException e)
            {
                ekranaHataYazdir("12", cnt.getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
            catch (IOException e)
            {
                ekranaHataYazdir("13", cnt.getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
        }

        //başta oluşturulan xmle yeni eklenen kategoriyi ekler ve en buyuk xml idsini döndürür
        public int xmlDosyasinaKategoriEkle(String baslik, String renk)
        {
            xmlEnBuyukID++;
            Node nodeMevcutParca = document.getElementById(String.valueOf(xmlParcaID));//içinde bulunulan parcaya giriyor
            NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
            for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
            {
                if (nodeParcaCocuklari.item(i).getNodeName().equals(Sabit.XML_ALTPARCA))//parcanın içindeki altparca etiketine ulaşılıyor
                {
                    Node nodeAltparca = nodeParcaCocuklari.item(i);//altparcaya giriliyor
                    Element yeniNodeParca = document.createElement(Sabit.XML_PARCA);//parca isimli etiket olşuturuluyor
                    yeniNodeParca.setAttribute(Sabit.XML_ID, String.valueOf(xmlEnBuyukID));//parca ya id özelliği ekleniyor
                    //yeniNodeParca.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);//parca ya id özelliği ekleniyor
                    nodeAltparca.appendChild(yeniNodeParca);//altparca etiketine parca ekleniyor

                    Node nodeParca = document.getElementById(String.valueOf(xmlEnBuyukID));//xmlid id sine sahip parca nın içine giriliyor. az önce oluşturulan parca

                    Element yeniNodeBaslik = document.createElement(Sabit.XML_BASLIK);//baslik etiketi oluşturuluyor
                    yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri giriliyor

                    Element yeniNodeRenk = document.createElement(Sabit.XML_RENK);//renk etiketi oluşturuluyor
                    yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri giriliyor

                    Element yeniNodeDurum = document.createElement(Sabit.XML_DURUM);//durum etiketi oluşturuluyor
                    yeniNodeDurum.setTextContent(Sabit.DURUM_YENI);//durum etiketine durum değeri giriliyor

                    Element yeniNodeYazi = document.createElement(Sabit.XML_YAZILAR);//yazi etiketi oluşturuluyor

                    Element yeniNodeAltparca = document.createElement(Sabit.XML_ALTPARCA);//altparca etiketi oluşturuluyor

                    nodeParca.appendChild(yeniNodeBaslik);//parca etiketine baslik etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeRenk);//parca etiketine renk etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeYazi);//parca etiketine yazi etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeDurum);//parca etiketine durum etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeAltparca);//parca etiketine altparca etiketi ekleniyor
                    break;
                }
            }
            documentToFile(Sabit.DOCUMENT_ASIL);

            return xmlEnBuyukID;
        }

        //listSeciliKategori ve listSeciliKayit'yi sıfırlar ve actionbar ı ilk haline döndürür
        public void seciliElemanListeleriniSifirla()
        {
            listSeciliCRL.clear();
            listSeciliElemanDurumu.clear();

            if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
            {
                Element element = document.getElementById(xmlParcaID);
                String renk = etiketBilgisiniGetir(element, Sabit.XML_RENK);

                ma.actionBarArkaPlanDegistir(renk);
            }
            else
            {
                ma.actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
            }
            actionBarDegistir(Sabit.ACTIONBAR_EKLE);
            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;
            duzenleSimgesininGorunumunuDegistir(View.INVISIBLE);
            basligiEskiYerineAl();
        }

        //yeni kategori fragmentini acar
        public void yeniKategoriEkranininAc()
        {
            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceYeni(Sabit.ELEMAN_TUR_YENI_KATEGORI), Sabit.FRAGMENT_TAG).commit();

            /*
            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.kategori_adi), cnt.getString(R.string.iptal), cnt.getString(R.string.tamam), "", Sabit.ALERTDIALOG_EDITTEXT);
            final AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();

            klavyeAc();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String kategoriAdi = builder.getAlertET().getText().toString();
                    if (kategoriAdi.isEmpty())//edittext boşken tamam'a tıklandı
                    {
                        Toast.makeText(getActivity(), cnt.getString(R.string.kategori_adi_bos_olamaz), Toast.LENGTH_LONG).show();
                    }
                    else//anaRelativeLayout'a yeni alan ekliyor
                    {
                        klavyeKapat();
                        alert.dismiss();
                        final int eklenenID = xmlDosyasinaKategoriEkle(kategoriAdi);

                        kategoriyiAnaEkranaEkle(kategoriAdi, eklenenID, Sabit.DURUM_YENI, Sabit.KATEGORI_ONTANIMLI_RENK, globalYerlesim);
                    }
                }
            });
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    klavyeKapat();
                    alert.dismiss();
                }
            });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener()//dialogun dışına tıklandı
            {
                @Override
                public void onCancel(DialogInterface dialogInterface)
                {
                    klavyeKapat();
                }
            });
            */
        }

        public int seciliElemanSayisi()
        {
            return listSeciliCRL.size();
        }

        //su anki etkin ekrana gore bir ust seviyenin ekran ve actionbar bilgilerini belirler
        public void ustSeviyeyiGetir()
        {
            switch (FRAGMENT_ETKIN_EKRAN)
            {
                case Sabit.FRAGMENT_KAYIT_EKRANI:
                case Sabit.FRAGMENT_YENI_KATEGORI_EKRANI:
                case Sabit.FRAGMENT_YENI_KAYIT_EKRANI:
                case Sabit.FRAGMENT_YEDEK_EKRANI:
                case Sabit.FRAGMENT_AYAR_EKRANI:
                case Sabit.FRAGMENT_KATEGORI_DUZENLE_EKRANI:
                {
                    Element element = document.getElementById(xmlParcaID);
                    String renk = etiketBilgisiniGetir(element, Sabit.XML_RENK);
                    String baslik = etiketBilgisiniGetir(element, Sabit.XML_BASLIK);
                    String durum = etiketBilgisiniGetir(element, Sabit.XML_DURUM);

                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), baslik, renk, durum, Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(crl), Sabit.FRAGMENT_TAG).commit();
                    //ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;
                    /*
                    FragmentManager fm = getFragmentManager();

                    if (fm.getBackStackEntryCount() > 0)
                    {
                        fm.popBackStackImmediate();
                    }
                    */

                    break;
                }

                case Sabit.FRAGMENT_KATEGORI_EKRANI:
                {
                    Element element = document.getElementById(xmlParcaID);
                    String ustSeviyeID = element.getParentNode().getParentNode().getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue();
                    xmlParcaID = ustSeviyeID;
                    element = document.getElementById(xmlParcaID);//ust seviye id yi alinca yeni element olusturuluyor

                    String renk = etiketBilgisiniGetir(element, Sabit.XML_RENK);
                    String baslik = etiketBilgisiniGetir(element, Sabit.XML_BASLIK);
                    String durum = etiketBilgisiniGetir(element, Sabit.XML_DURUM);

                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), baslik, renk, durum, Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    //CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(crl), Sabit.FRAGMENT_TAG).commit();
                    //ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                /*
                case Sabit.FRAGMENT_YEDEK_EKRANI:
                {
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    //ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                case Sabit.FRAGMENT_AYAR_EKRANI:
                {
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    //ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                case Sabit.FRAGMENT_YENI_KAYIT_EKRANI:
                {
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    //ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                case Sabit.FRAGMENT_YENI_KATEGORI_EKRANI:
                {
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    //ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                */
                default:
                    ekranaHataYazdir("14", cnt.getString(R.string.hatali_fragment_id) + " : " + FRAGMENT_ETKIN_EKRAN);
            }
        }

        //başta oluşturulan xmle yeni eklenen kaydı ekler ve en buyuk xml idsini döndürür
        public int xmlDosyasinaKayitEkle(String baslik, String yazi, String renk)
        {
            xmlEnBuyukID++;
            //kategoriDurumunuGuncelle(xmlParcaID, Sabit.DURUM_YENI);
            Element element = document.getElementById(String.valueOf(xmlParcaID));
            etiketiGuncelle(element, Sabit.DURUM_YENI, Sabit.XML_DURUM);

            Node nodeMevcutParca = document.getElementById(String.valueOf(xmlParcaID));//içinde bulunulan parcaya giriyor
            NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
            for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
            {
                if (nodeParcaCocuklari.item(i).getNodeName().equals(Sabit.XML_YAZILAR))//parcanın içindeki yazilar etiketine ulaşılıyor
                {
                    //int eklenenID = xmlEnBuyukID;
                    Element yeniNodeKayit = document.createElement(Sabit.XML_KAYIT);//kayıt etiketi olusturuyor
                    yeniNodeKayit.setAttribute(Sabit.XML_ID, String.valueOf(xmlEnBuyukID));//parca ya id özelliği ekleniyor
                    //yeniNodeKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);//parca ya durum özelliği ekleniyor

                    StringBuilder str = new StringBuilder(yazi);//alt satıra geçmeyi anlayabilmek için \n <br> ile değiştiriliyor
                    int sayac = 0;
                    for (int j = 0; j < yazi.length(); j++)//<br> eklendiği zaman stringin boyu 4 uzuyor onun için sayac tutuyoruz
                    {
                        if (yazi.charAt(j) == '\n')
                        {
                            str.insert(j + sayac, "<br>");
                            sayac = sayac + 4;
                        }
                    }

                    Element yeniNodeBaslik = document.createElement(Sabit.XML_BASLIK);//baslik etiketi oluşturuluyor
                    yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri giriliyor
                    yeniNodeKayit.appendChild(yeniNodeBaslik);

                    Element yeniNodeYazi = document.createElement(Sabit.XML_YAZI);//yazi etiketi oluşturuluyor
                    yeniNodeYazi.setTextContent(str.toString());//yazi etiketine yazi değeri giriliyor
                    yeniNodeKayit.appendChild(yeniNodeYazi);

                    Element yeniNodeRenk = document.createElement(Sabit.XML_RENK);//renk etiketi oluşturuluyor
                    yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri giriliyor
                    yeniNodeKayit.appendChild(yeniNodeRenk);

                    Element yeniNodeDurum = document.createElement(Sabit.XML_DURUM);//renk etiketi oluşturuluyor
                    yeniNodeDurum.setTextContent(Sabit.DURUM_YENI);//renk etiketine renk değeri giriliyor
                    yeniNodeKayit.appendChild(yeniNodeDurum);

                    nodeParcaCocuklari.item(i).appendChild(yeniNodeKayit);//yazilar etiketinin içine kayit etiketini ekliyor

                    break;
                }
            }
            documentToFile(Sabit.DOCUMENT_ASIL);

            return xmlEnBuyukID;
        }

        //yeni kayit fragmentini acar
        public void yeniKayitEkraniniAc()
        {
            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceYeni(Sabit.ELEMAN_TUR_YENI_KAYIT), Sabit.FRAGMENT_TAG).commit();

            /*
            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.kayit), cnt.getString(R.string.iptal), cnt.getString(R.string.tamam), "", Sabit.ALERTDIALOG_EDITTEXT);
            final AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();

            klavyeAc();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String kayitAdi = builder.getAlertET().getText().toString();
                    if (kayitAdi.isEmpty())//edittext boşken tamam'a tıklandı
                    {
                        Toast.makeText(getActivity(), cnt.getString(R.string.kayit_bos_olamaz), Toast.LENGTH_LONG).show();
                    }
                    else//anaRelativeLayout'a yeni alan ekliyor
                    {
                        klavyeKapat();
                        alert.dismiss();
                        final int eklenenID = xmlDosyasinaKayitEkle(kayitAdi);

                        kayitlariAnaEkranaEkle(kayitAdi, eklenenID, Sabit.DURUM_YENI, Sabit.KAYIT_ONTANIMLI_RENK, globalYerlesim);
                    }
                }
            });
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    klavyeKapat();
                    alert.dismiss();
                }
            });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener()//dialogun dışına tıklandı
            {
                @Override
                public void onCancel(DialogInterface dialogInterface)
                {
                    klavyeKapat();
                }
            });
            */
        }

        //sil tusuna basıldığı zaman secili elemanları siler
        public void seciliElemanlariSilDiyaloguOlustur()
        {
            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.onay), cnt.getString(R.string.iptal), cnt.getString(R.string.tamam), cnt.getString(R.string.secilen_kayitlar_silinsin_mi), Sabit.ALERTDIALOG_TEXTVIEW);
            final AlertDialog alert = builder.create();
            alert.show();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    seciliElemanlariSil(listSeciliCRL);
                    seciliElemanListeleriniSifirla();
                    alert.dismiss();
                }
            });
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                }
            });
        }

        public void kategoriSilDiyaloguOlustur()
        {
            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.onay), cnt.getString(R.string.iptal), cnt.getString(R.string.tamam), cnt.getString(R.string.secilen_kayitlar_silinsin_mi), Sabit.ALERTDIALOG_TEXTVIEW);
            final AlertDialog alert = builder.create();
            alert.show();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Element element = document.getElementById(String.valueOf(seciliCRL.getId()));

                    String ustSeviyeID = element.getParentNode().getParentNode().getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue();
                    xmlParcaID = ustSeviyeID;

                    element.getParentNode().removeChild(element);
                    documentToFile(Sabit.DOCUMENT_ASIL);

                    ustSeviyeyiGetir();

                    alert.dismiss();
                }
            });
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                }
            });
        }

        public void kayitSilDiyaloguOlustur()
        {
            if (seciliCRL != null)
            {
                final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.onay), cnt.getString(R.string.iptal), cnt.getString(R.string.tamam), cnt.getString(R.string.kayit_silinsin_mi), Sabit.ALERTDIALOG_TEXTVIEW);
                final AlertDialog alert = builder.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        kayitSil();
                        ustSeviyeyiGetir();

                        alert.dismiss();
                    }
                });
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alert.dismiss();
                    }
                });

                //klavyeKapat(etDegisecek.getWindowToken());
                klavyeKapat();
            }
            else
            {
                ekranaHataYazdir("16", cnt.getString(R.string.kayit_silinsin_mi));
            }
        }

        //kayit ekranındayken sile tıklandı. bir kayıt siliyor
        public void kayitSil()
        {
            Element element = document.getElementById(String.valueOf(seciliCRL.getId()));
            element.getParentNode().removeChild(element);
            documentToFile(Sabit.DOCUMENT_ASIL);

            seciliCRL = null;
        }

        //secili elemanlari siler
        public void seciliElemanlariSil(List<CustomRelativeLayout> listeSilinecek)
        {
            for (int i = 0; i < listeSilinecek.size(); i++)
            {
                Element element = document.getElementById(String.valueOf(listeSilinecek.get(i).getId()));
                element.getParentNode().removeChild(element);
                documentToFile(Sabit.DOCUMENT_ASIL);

                //CustomRelativeLayout crl = findCRLbyID(listeSilinecek.get(i).getId());
                //anaRelativeLayout.removeView(crl);
            }
            anaRelativeLayout.removeAllViews();
            yeniYerlesimOlustur();
            parseXml(xmlParcaID);
        }

        //secili elemanların durumunu yeni olarak isaretler
        public void seciliElemanlarYeni()
        {
            for (int i = 0; i < listSeciliCRL.size(); i++)
            {
                CustomRelativeLayout crl = listSeciliCRL.get(i);
                Element element = kayitDurumunuYeniYap(String.valueOf(crl.getId()));
                if (element != null)
                {
                    switch (crl.getCrlTur())
                    {
                        case Sabit.ELEMAN_TUR_KAYIT:
                            crl.arkaplanKayit();
                            break;

                        case Sabit.ELEMAN_TUR_KATEGORI:
                            kategoriCocuklariniDurumunuGuncelle(element, Sabit.DURUM_YENI);
                            crl.arkaplanKategori();
                            break;

                        default:
                            ekranaHataYazdir("17", cnt.getString(R.string.hatali_kayit_turu) + " : " + crl.getCrlTur());
                    }
                    crl.getTvTik().setText("");
                    crl.setCrlSeciliMi(false);
                }
            }
            if (!listSeciliCRL.isEmpty())
            {
                Element elementKayit = document.getElementById(String.valueOf(listSeciliCRL.get(0).getId()));//secilen butun kayıtlar aynı parca altında olduğu için 1 kez kontrol yeterli
                ustParcaDurumunuKontrolEtYeni(elementKayit);
            }

            seciliElemanListeleriniSifirla();
            documentToFile(Sabit.DOCUMENT_ASIL);
        }

        //secili kayıtları ve kategorilerin altındaki kayıtları tamamlandı olarak isaretler
        public void seciliElemanlariTamamla()
        {
            for (int i = 0; i < listSeciliCRL.size(); i++)
            {
                CustomRelativeLayout crl = listSeciliCRL.get(i);
                Element element = kayitDurumunuTamamlandiYap(String.valueOf(crl.getId()));
                if (element != null)
                {
                    switch (crl.getCrlTur())
                    {
                        case Sabit.ELEMAN_TUR_KATEGORI:
                            kategoriCocuklariniDurumunuGuncelle(element, Sabit.DURUM_TAMAMLANDI);
                            crl.arkaplanKategori();
                            break;

                        case Sabit.ELEMAN_TUR_KAYIT:
                            crl.arkaplanKayit();
                            break;

                        default:
                            ekranaHataYazdir("18", cnt.getString(R.string.hatali_kayit_turu) + " : " + crl.getCrlTur());
                    }
                    crl.getTvTik().setText(Sabit.TIK_UNICODE);
                    crl.setCrlSeciliMi(false);
                }
            }

            if (!listSeciliCRL.isEmpty())
            {
                Element elementKayit = document.getElementById(String.valueOf(listSeciliCRL.get(0).getId()));//secilen butun kayıtlar aynı parca altında olduğu için 1 kez kontrol yeterli
                ustParcaDurumunuKontrolEtTamamla(elementKayit);
            }

            seciliElemanListeleriniSifirla();
            documentToFile(Sabit.DOCUMENT_ASIL);
        }

        //kategori altındaki bilesenlerin durumlarını gunceller
        public void kategoriCocuklariniDurumunuGuncelle(Element elementKategori, String durum)
        {
            NodeList nodeList = elementKategori.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeName().equals(Sabit.XML_YAZILAR))
                {
                    NodeList nodeListKayitlar = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListKayitlar.getLength(); j++)
                    {
                        etiketiGuncelle((Element) nodeListKayitlar.item(j), durum, Sabit.XML_DURUM);
                        //nodeListKayitlar.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(durum);
                    }
                }
                else if (nodeList.item(i).getNodeName().equals(Sabit.XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        //nodeListAltParca.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(durum);
                        etiketiGuncelle((Element) nodeListAltParca.item(j), durum, Sabit.XML_DURUM);
                        kategoriCocuklariniDurumunuGuncelle((Element) nodeListAltParca.item(j), durum);
                    }
                }
            }
        }

        /*
        public void kategoriDurumunuGuncelle(String id, String durum)
        {
            Element elementKayit = document.getElementById(id);
            elementKayit.setAttribute(Sabit.XML_DURUM, durum);
        }
        */

        //parcanın yazilarini ve altparcalarını kontrol eder. hepsi tamamlanmiş ise parcayı tamamlandı olarak işaretler
        public boolean parcayiIsaretleTamamlandi(Node nodeParca)
        {
            String idKategori = nodeParca.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue();

            boolean sonucYazilar = true;
            boolean sonucAltParcalar = true;
            NodeList nodeListParca = nodeParca.getChildNodes();//parcanın alt etiketlerini tarıyor. yazılar etiketini bulmak için
            for (int i = 0; i < nodeListParca.getLength(); i++)
            {
                if (nodeListParca.item(i).getNodeName().equals(Sabit.XML_YAZILAR))
                {
                    Node nodeYazilar = nodeListParca.item(i);

                    NodeList nodeListKayit = nodeYazilar.getChildNodes();
                    for (int j = 0; j < nodeListKayit.getLength(); j++)
                    {
                        if (etiketiGetir((Element) nodeListKayit.item(j), Sabit.XML_DURUM).equals(Sabit.DURUM_YENI))
                        {
                            sonucYazilar = false;
                            break;
                        }

                        /*
                        if (nodeListKayit.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue().equals(Sabit.DURUM_YENI))
                        {
                            sonucYazilar = false;
                            break;
                        }
                        */
                    }
                }
                else if (nodeListParca.item(i).getNodeName().equals(Sabit.XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeListParca.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        if (etiketiGetir((Element) nodeListAltParca.item(j), Sabit.XML_DURUM).equals(Sabit.DURUM_YENI))
                        {
                            sonucAltParcalar = false;
                            break;
                        }

                        /*
                        if (nodeListAltParca.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue().equals(Sabit.DURUM_YENI))
                        {
                            sonucAltParcalar = false;
                            break;
                        }
                        */
                    }
                }
            }
            if (idKategori.equals("0"))
            {
                return false;
            }
            else
            {
                if (sonucYazilar && sonucAltParcalar)//butun kayitlar ve kategoriler tamamlandı olarak işaretlenmiş. parca da tamamlandı olarak işaretlenecek
                {
                    etiketiGuncelle((Element) nodeParca, Sabit.DURUM_TAMAMLANDI, Sabit.XML_DURUM);

                    //nodeParca.getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(Sabit.DURUM_TAMAMLANDI);
                    documentToFile(Sabit.DOCUMENT_ASIL);

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        //yeni olarak işaretlenen kaydin ust parcalarını da yeni olarak işaretler
        public boolean parcayiIsaretleYeni(Node nodeParca)
        {
            String idKategori = nodeParca.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue();
            if (idKategori.equals("0"))
            {
                return false;
            }
            else
            {
                etiketiGuncelle((Element) nodeParca, Sabit.DURUM_YENI, Sabit.XML_DURUM);

//                nodeParca.getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(Sabit.DURUM_YENI);
                documentToFile(Sabit.DOCUMENT_ASIL);

                return true;
            }
        }

        public void ustParcaDurumunuKontrolEtYeni(Element element)
        {
            boolean sonuc;
            Node nodeParca = element.getParentNode().getParentNode();
            sonuc = parcayiIsaretleYeni(nodeParca);

            while (sonuc)
            {
                nodeParca = nodeParca.getParentNode().getParentNode();
                sonuc = parcayiIsaretleYeni(nodeParca);
            }
        }

        //secilen kaydin durumunu yeni olarak değiştirir
        public Element kayitDurumunuYeniYap(String idd)
        {
            CustomRelativeLayout crl = findCRLbyID(Integer.valueOf(idd));
            crl.setDurum(Sabit.DURUM_YENI);

            Element elementKayit = document.getElementById(idd);
            //elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);

            etiketiGuncelle(elementKayit, Sabit.DURUM_YENI, Sabit.XML_DURUM);


            documentToFile(Sabit.DOCUMENT_ASIL);

            return elementKayit;
        }

        //secilen kaydin durumunu yeni olarak değiştirir
        public Element kayitDurumunuYeniYap()
        {
            if (seciliCRL != null)
            {
                seciliCRL.setDurum(Sabit.DURUM_YENI);
                Element elementKayit = document.getElementById(String.valueOf(seciliCRL.getId()));
                //elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);

                etiketiGuncelle(elementKayit, Sabit.DURUM_YENI, Sabit.XML_DURUM);

                documentToFile(Sabit.DOCUMENT_ASIL);

                return elementKayit;
            }
            else
            {
                return null;
            }
        }

        public void etiketiGuncelle(Element element, String yeniDeger, String xmlEtiketi)
        {
            Node node = etiketiGetir(element, xmlEtiketi);//node getiriliyor
            if (node == null)
            {
                ekranaHataYazdir("48", cnt.getString(R.string.xml_etiket_okunamadi) + " : " + xmlEtiketi);
            }
            else
            {
                node.setTextContent(yeniDeger);//node guncelleniyor
            }
        }

        //kayit degistime ekraninda kaydete basıldı
        public void kayitDegistirKaydet()
        {
            EditText etBaslik = (EditText) fragmentRootViewYeniKayit.findViewById(etBaslikID);//kayit degistirme ekranındaki baslik bilgisi
            EditText etYazi = (EditText) fragmentRootViewYeniKayit.findViewById(etKayitID);//kayit degistirme ekranındaki yazi bilgisi
            LinearLayout llBaslik2 = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
            String renkKodu = arkaplanRenginiGetir(llBaslik2);

            Element elementKayit = document.getElementById(String.valueOf(seciliCRL.getId()));//degisecek kayit nesnesi

            etiketiGuncelle(elementKayit, etBaslik.getText().toString(), Sabit.XML_BASLIK);
            etiketiGuncelle(elementKayit, etYazi.getText().toString(), Sabit.XML_YAZI);
            etiketiGuncelle(elementKayit, renkKodu, Sabit.XML_RENK);
            etiketiGuncelle(elementKayit, seciliCRL.getDurum(), Sabit.XML_DURUM);

            //final int eklenenID = xmlDosyasinaKayitEkle(etBaslik.getText().toString(), etKayit.getText().toString(), renkKodu2);
            //anaRelativeLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.anaRelativeLayout);
            //kayitlariAnaEkranaEkle(etBaslik.getText().toString(), etKayit.getText().toString(), eklenenID, Sabit.DURUM_YENI, Sabit.KAYIT_ONTANIMLI_RENK, globalYerlesim);

            documentToFile(Sabit.DOCUMENT_ASIL);

            ustSeviyeyiGetir();
        }

        public void kategoriDegistirKaydet()
        {
            EditText etBaslik = (EditText) fragmentRootViewYeniKayit.findViewById(etBaslikID);//kayit degistirme ekranındaki baslik bilgisi
            //EditText etYazi = (EditText) fragmentRootViewYeniKayit.findViewById(etKayitID);//kayit degistirme ekranındaki yazi bilgisi
            LinearLayout llBaslik2 = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
            String renkKodu = arkaplanRenginiGetir(llBaslik2);

            Element elementKategori = document.getElementById(String.valueOf(seciliCRL.getId()));//degisecek kayit nesnesi

            etiketiGuncelle(elementKategori, etBaslik.getText().toString(), Sabit.XML_BASLIK);
            //etiketiGuncelle(elementKategori, etYazi.getText().toString(), Sabit.XML_YAZI);
            etiketiGuncelle(elementKategori, renkKodu, Sabit.XML_RENK);
            etiketiGuncelle(elementKategori, seciliCRL.getDurum(), Sabit.XML_DURUM);

            //final int eklenenID = xmlDosyasinaKayitEkle(etBaslik.getText().toString(), etKayit.getText().toString(), renkKodu2);
            //anaRelativeLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.anaRelativeLayout);
            //kayitlariAnaEkranaEkle(etBaslik.getText().toString(), etKayit.getText().toString(), eklenenID, Sabit.DURUM_YENI, Sabit.KAYIT_ONTANIMLI_RENK, globalYerlesim);

            documentToFile(Sabit.DOCUMENT_ASIL);

            ustSeviyeyiGetir();
        }

        //ust parcaların kayıt ve kategorilerine bakar hepsi tamalandi durumunda ise parcayı tamamlandı olarak isaretler
        public void ustParcaDurumunuKontrolEtTamamla(Element element)
        {
            boolean sonuc;
            Node nodeParca = element.getParentNode().getParentNode();
            sonuc = parcayiIsaretleTamamlandi(nodeParca);

            while (sonuc)
            {
                nodeParca = nodeParca.getParentNode().getParentNode();
                sonuc = parcayiIsaretleTamamlandi(nodeParca);
            }
        }

        //secilen kaydin durumunu tamamlandı olarak değiştirir
        public Element kayitDurumunuTamamlandiYap(String idd)
        {
            CustomRelativeLayout crl = findCRLbyID(Integer.valueOf(idd));
            crl.setDurum(Sabit.DURUM_TAMAMLANDI);
            Element elementKayit = document.getElementById(idd);
            ////elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_TAMAMLANDI);

            //etiketiGuncelle(elementKayit, Sabit.DURUM_TAMAMLANDI, Sabit.XML_DURUM);

            //documentToFile(Sabit.DOCUMENT_ASIL);

            return elementKayit;
        }

        //secilen kaydin durumunu tamamlandı olarak değiştirir
        public Element kayitDurumunuTamamlandiYap()
        {
            if (seciliCRL != null)
            {
                seciliCRL.setDurum(Sabit.DURUM_TAMAMLANDI);
                Element elementKayit = document.getElementById(String.valueOf(seciliCRL.getId()));
                ////elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_TAMAMLANDI);

                //etiketiGuncelle(elementKayit, Sabit.DURUM_TAMAMLANDI, Sabit.XML_DURUM);

                //documentToFile(Sabit.DOCUMENT_ASIL);

                return elementKayit;
            }
            else
            {
                ekranaHataYazdir("35", cnt.getString(R.string.bos_nesne));
                return null;
            }
        }

        //duruma göre actionbarDegistirdeki simgeleri gösterir, gizler
        public void actionBarDurumSimgesiDegistirKayit(String durum)
        {
            switch (durum)
            {
                case Sabit.DURUM_TAMAMLANDI:
                    menuActionBar.findItem(Sabit.ACTION_KAYIT_DEGISTIR_TAMAM).setVisible(false);
                    menuActionBar.findItem(Sabit.ACTION_KAYIT_DEGISTIR_YENI).setVisible(true);
                    break;

                case Sabit.DURUM_YENI:
                    menuActionBar.findItem(Sabit.ACTION_KAYIT_DEGISTIR_TAMAM).setVisible(true);
                    menuActionBar.findItem(Sabit.ACTION_KAYIT_DEGISTIR_YENI).setVisible(false);
                    break;

                default:
                    ekranaHataYazdir("36", cnt.getString(R.string.hatali_kayit_durumu_turu) + " : " + durum);
            }
        }

        //duruma göre actionbarDegistirdeki simgeleri gösterir, gizler
        public void actionBarDurumSimgesiDegistirKategori(String durum)
        {
            switch (durum)
            {
                case Sabit.DURUM_TAMAMLANDI:
                    menuActionBar.findItem(Sabit.ACTION_KATEGORI_DEGISTIR_TAMAM).setVisible(false);
                    menuActionBar.findItem(Sabit.ACTION_KATEGORI_DEGISTIR_YENI).setVisible(true);
                    break;

                case Sabit.DURUM_YENI:
                    menuActionBar.findItem(Sabit.ACTION_KATEGORI_DEGISTIR_TAMAM).setVisible(true);
                    menuActionBar.findItem(Sabit.ACTION_KATEGORI_DEGISTIR_YENI).setVisible(false);
                    break;

                default:
                    ekranaHataYazdir("36", cnt.getString(R.string.hatali_kayit_durumu_turu) + " : " + durum);
            }
        }

        public void klavyeAc(Context c, EditText et)
        {
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (getActivity().getCurrentFocus() == null)
            {
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
            else
            {
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }

            //InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
            //imm.showSoftInput(et, 0);
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        public void klavyeKapat(IBinder windowToken)
        {
            InputMethodManager mgr = (InputMethodManager) fAct.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (getActivity() == null)
            {
                klavyeKapat();
            }
            else
            {
                mgr.hideSoftInputFromWindow(windowToken, 0);
            }

            //InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            //mgr.hideSoftInputFromWindow(windowToken, 0);
            //mgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }

        public void klavyeAc()
        {
            if (!klavyeAcikMi())
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }

        public void klavyeKapat()
        {
            if (klavyeAcikMi())
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }

        //kullanıcı klavyeyi kendi kapatmis mi diye kontrol etmek için
        public boolean klavyeAcikMi()
        {
            return activityRootView.getRootView().getHeight() / 2 > activityRootView.getHeight();
        }

        public void dosyaKopyala(String kaynak, String hedef)
        {
            try
            {
                InputStream in = new FileInputStream(kaynak);
                OutputStream out = new FileOutputStream(hedef);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            }
            catch (FileNotFoundException e)
            {
                ekranaHataYazdir("37", cnt.getString(R.string.yedek_dosyasi_kopyalanirken_hata_olustu) + " : " + e.getMessage() + ", " + cnt.getString(R.string.kaynak) + " : " + kaynak + ", " + cnt.getString(R.string.hedef) + " : " + hedef);
            }
            catch (IOException e)
            {
                ekranaHataYazdir("38", cnt.getString(R.string.yedek_dosyasi_kopyalanirken_hata_olustu) + " : " + e.getMessage() + ", " + cnt.getString(R.string.kaynak) + " : " + kaynak + ", " + cnt.getString(R.string.hedef) + " : " + hedef);
            }
        }

        //xml dosyasını yedek klasörüne kopyalar
        public void xmlYedekle()
        {
            String zaman = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), getString(R.string.onay), getString(R.string.iptal), getString(R.string.tamam), zaman, Sabit.ALERTDIALOG_EDITTEXT);
            final AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();

            klavyeAc();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String yedekAdi = builder.getAlertET().getText().toString();
                    if (yedekAdi.isEmpty())//edittext boşken tamam'a tıklandı
                    {
                        Toast.makeText(getActivity(), getString(R.string.yedek_adi_bos_olamaz), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        klavyeKapat();

                        final String hedefDosya = xmlYedekKlasorYolu + "/" + builder.getAlertET().getText() + "." + Sabit.XML_DOSYA_UZANTISI;
                        File fileHedef = new File(hedefDosya);
                        if (fileHedef.exists())
                        {
                            CustomAlertDialogBuilder builder2 = new CustomAlertDialogBuilder(getActivity(), getString(R.string.onay), getString(R.string.iptal), getString(R.string.tamam), getString(R.string.bu_isme_sahip_dosya_var_uzerine_yazilsin_mi), Sabit.ALERTDIALOG_TEXTVIEW);
                            final AlertDialog alert2 = builder2.create();
                            alert2.show();

                            alert2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    alert2.dismiss();
                                    alert.dismiss();
                                    dosyaKopyala(xmlDosyaYolu, hedefDosya);
                                }
                            });
                            alert2.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    alert2.dismiss();
                                }
                            });
                        }
                        else
                        {
                            alert.dismiss();
                            dosyaKopyala(xmlDosyaYolu, hedefDosya);
                        }
                    }
                }
            });
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    klavyeKapat();
                    alert.dismiss();
                }
            });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener()//dialogun dışına tıklandı
            {
                @Override
                public void onCancel(DialogInterface dialogInterface)
                {
                    klavyeKapat();
                }
            });
        }

        //xml yedeklerinin isinlerini liste olarak getirir
        public List<String> yedekDosyalariniGetir()
        {
            List<String> yedekler = new ArrayList<>();
            File f = new File(xmlYedekKlasorYolu);
            File file[] = f.listFiles();
            for (int i = 0; i < file.length; i++)
            {
                String uzanti = file[i].getName().substring(file[i].getName().length() - 4);
                if (uzanti.equals("." + Sabit.XML_DOSYA_UZANTISI))//yedek dosyalarının uzantıları xml olmalı
                {
                    String b = file[i].getName().substring(0, file[i].getName().length() - 4);
                    yedekler.add(b);
                }
                else
                {
                    ekranaHataYazdir("20", cnt.getString(R.string.hatali_dosya_uzantisi) + " : " + uzanti);
                }
            }

            return yedekler;
        }

        //yedek xml dosyasını ana xml dosyası yapar
        public void xmlYedektenYukle()
        {
            //alertdialog un içindeki ana LinearLayout
            LinearLayout alertLL = new LinearLayout(getActivity());
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            alertLL.setLayoutParams(pa);
            alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
            alertLL.setWeightSum(1f);

            ListView lv = new ListView(getActivity());
            ListView.LayoutParams paa = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
            lv.setLayoutParams(paa);
            alertLL.addView(lv);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(cnt.getString(R.string.yedek_adi));
            builder.setView(alertLL);
            builder.setNegativeButton(cnt.getString(R.string.iptal), null);
            final AlertDialog alert = builder.create();

            List<String> yedekler = yedekDosyalariniGetir();
            ArrayAdapter<String> veriAdaptoru = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, yedekler);
            lv.setAdapter(veriAdaptoru);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    final TextView vv = (TextView) view;
                    final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.onay), cnt.getString(R.string.iptal), cnt.getString(R.string.tamam), vv.getText() + cnt.getString(R.string.yedek_dosyasi_yuklensin_mi), Sabit.ALERTDIALOG_TEXTVIEW);
                    final AlertDialog alert2 = builder.create();
                    alert2.show();

                    alert2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            File xmlDosyasi = new File(xmlDosyaYolu);
                            if (xmlDosyasi.delete())
                            {
                                dosyaKopyala(xmlYedekKlasorYolu + "/" + vv.getText() + "." + Sabit.XML_DOSYA_UZANTISI, xmlDosyaYolu);

                                document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);

                                Element element = document.getElementById("0");
                                String renk = etiketBilgisiniGetir(element, Sabit.XML_RENK);
                                String baslik = etiketBilgisiniGetir(element, Sabit.XML_BASLIK);
                                String durum = etiketBilgisiniGetir(element, Sabit.XML_DURUM);

                                CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), baslik, renk, durum, 0);
                                getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(crl), Sabit.FRAGMENT_TAG).commit();

                                /*
                                anaRelativeLayout.removeAllViews();
                                document = xmlDocumentNesnesiOlustur(xmlDosyaYolu, (MainActivity) fAct);
                                yeniYerlesimOlustur();
                                //seciliCRL = new CustomRelativeLayout(getActivity(), Sabit.ELEMAN_TUR_KATEGORI, Integer.parseInt(xmlParcaID));
                                xmlParcaID = "0";
                                parseXml(xmlParcaID);
                                getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                                getActivity().getActionBar().setTitle("/");
                                */
                            }
                            else
                            {
                                ekranaHataYazdir("21", cnt.getString(R.string.xml_silinirken_hata_olustu) + " : " + xmlDosyasi);
                            }
                            alert2.dismiss();
                            alert.dismiss();
                        }
                    });
                    alert2.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            alert2.dismiss();
                            alert.dismiss();
                        }
                    });
                }
            });
            alert.show();
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                }
            });
        }

        //sistemdeki xml yedeklerini gosterir
        public void yedekDosyalariGoster()
        {
            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceYedek(), Sabit.FRAGMENT_TAG).commit();
        }

        public void seciliYedekDosyalariniSil()
        {
            if (!listSeciliYedek.isEmpty())
            {
                String soru = cnt.getString(R.string.yedek_dosyalari_silinsin_mi) + "\n";
                for (int i = 0; i < listSeciliYedek.size(); i++)
                {
                    soru = soru + "\n" + listSeciliYedek.get(i).getIsim();
                }

                final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.onay), cnt.getString(R.string.iptal), cnt.getString(R.string.tamam), soru, Sabit.ALERTDIALOG_TEXTVIEW);
                final AlertDialog alert = builder.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        for (int i = 0; i < listSeciliYedek.size(); i++)
                        {
                            String dosya = xmlYedekKlasorYolu + "/" + listSeciliYedek.get(i).getIsim() + "." + Sabit.XML_DOSYA_UZANTISI;
                            File yedekDosya = new File(dosya);

                            if (!yedekDosya.delete())
                            {
                                ekranaHataYazdir("22", cnt.getString(R.string.yedek_silinirken_hata_olustu) + " : " + yedekDosya);
                            }
                            anaRelativeLayout.removeView(listSeciliYedek.get(i));
                        }
                        listSeciliYedek.clear();
                        alert.dismiss();
                    }
                });
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        List<CheckBox> listeYedekCheckBox = new ArrayList<>();

                        for (int i = 0; i < listSeciliYedek.size(); i++)
                        {
                            listeYedekCheckBox.add(listSeciliYedek.get(i).getCb());
                        }

                        for (int i = 0; i < listeYedekCheckBox.size(); i++)
                        {
                            listeYedekCheckBox.get(i).setChecked(false);
                        }

                        listSeciliYedek.clear();
                        alert.dismiss();
                    }
                });
            }
        }

        public void actionBarDegistir(int actionBarTur)
        {
            if (ACTIONBAR_TUR != actionBarTur)
            {
                ACTIONBAR_TUR = actionBarTur;
                menuActionBar.clear();
                onCreateOptionsMenu(menuActionBar, inflaterActionBar);
            }
        }

        public void ayarEkraniniAc()
        {
            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceAyarlar()).addToBackStack(null).commit();
        }

        //ayar degerlerini öntanımlı değerler yapar
        public void ayarlariSifirla()
        {
            for (int i = 0; i < anaRelativeLayout.getChildCount(); i++)
            {
                AyarlarRelativeLayout arl = (AyarlarRelativeLayout) anaRelativeLayout.getChildAt(i);
                switch (arl.getAyarID())
                {
                    case Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI:
                        ((EditText) arl.getViewSecenek()).setText(Sabit.ONTANIMLI_DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI);
                        break;

                    case Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN:
                        if (Sabit.ONTANIMLI_DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN.equals("1"))
                        {
                            ((CheckBox) arl.getViewSecenek()).setChecked(true);
                        }
                        else
                        {
                            ((CheckBox) arl.getViewSecenek()).setChecked(false);
                        }
                        break;

                    case Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI:
                        ((EditText) arl.getViewSecenek()).setText(Sabit.ONTANIMLI_DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI);
                        break;

                    default:
                        ekranaHataYazdir("23", cnt.getString(R.string.hatali_ayar_id) + " : " + arl.getAyarID());
                }
            }
        }

        //ayar ekranina ayar metinlerini ekler
        public void ayarlariAyarEkraninaEkle()
        {
            AyarlarRelativeLayout arl1 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.sutun_sayisi), DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI, Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI, 0, Sabit.SECENEK_EDITTEXT);
            anaRelativeLayout.addView(arl1);

            AyarlarRelativeLayout arl2 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.satir_boyu_sabit_olsun), DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN, Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN, Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI, Sabit.SECENEK_CHECKBOX);
            anaRelativeLayout.addView(arl2);

            final AyarlarRelativeLayout arl3 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.satir_sayisi), DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI, Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI, Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN, Sabit.SECENEK_EDITTEXT);
            anaRelativeLayout.addView(arl3);
            if (DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN.equals("0"))//2. ayardaki checkbox secili değilse 3. ayar disable olsun
            {
                for (int i = 0; i < arl3.getChildCount(); i++)
                {
                    arl3.getChildAt(i).setEnabled(false);
                }
            }

            //3. ayar için önce 2. ayardaki checkbox'ın isaretlenmesi gerek
            ((CheckBox) arl2.getViewSecenek()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    for (int i = 0; i < arl3.getChildCount(); i++)
                    {
                        arl3.getChildAt(i).setEnabled(b);
                    }
                }
            });

            AyarlarRelativeLayout arl4 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.simge_rengi), DEGER_AYAR_SIMGE_RENGI, Sabit.AYAR_ID_SIMGE_RENGI, Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI, Sabit.SECENEK_BUTTON);
            anaRelativeLayout.addView(arl4);

            /*
            AyarlarRelativeLayout arl5 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.yazı_rengi), DEGER_AYAR_YAZI_RENGI, Sabit.AYAR_ID_YAZI_RENGI,Sabit.AYAR_ID_SIMGE_RENGI Sabit.SECENEK_BUTTON);
            anaRelativeLayout.addView(arl5);
            */

            AyarlarRelativeLayout arl6 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.cerceve_gozuksun), DEGER_AYAR_CERCEVE_GOZUKSUN, Sabit.AYAR_ID_CERCEVE_GOZUKSUN, Sabit.AYAR_ID_SIMGE_RENGI, Sabit.SECENEK_CHECKBOX);
            anaRelativeLayout.addView(arl6);

            final AyarlarRelativeLayout arl7 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.cerceve_rengi), DEGER_AYAR_CERCEVE_RENGI, Sabit.AYAR_ID_CERCEVE_RENGI, Sabit.AYAR_ID_CERCEVE_GOZUKSUN, Sabit.SECENEK_BUTTON);
            anaRelativeLayout.addView(arl7);
            if (DEGER_AYAR_CERCEVE_GOZUKSUN.equals("0"))//6. ayardaki checkbox secili değilse 7. ayar disable olsun
            {
                for (int i = 0; i < arl7.getChildCount(); i++)
                {
                    arl7.getChildAt(i).setEnabled(false);
                }
            }

            //7. ayar için önce 6. ayardaki checkbox'ın isaretlenmesi gerek
            ((CheckBox) arl6.getViewSecenek()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    for (int i = 0; i < arl7.getChildCount(); i++)
                    {
                        arl7.getChildAt(i).setEnabled(b);
                    }
                }
            });

            AyarlarRelativeLayout arl8 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.arkaplan_rengi_sabit_olsun), DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN, Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN, Sabit.AYAR_ID_CERCEVE_RENGI, Sabit.SECENEK_CHECKBOX);
            anaRelativeLayout.addView(arl8);

            final AyarlarRelativeLayout arl9 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.arkaplan_rengi), DEGER_AYAR_ARKAPLAN_RENGI, Sabit.AYAR_ID_ARKAPLAN_RENGI, Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN, Sabit.SECENEK_BUTTON);
            anaRelativeLayout.addView(arl9);
            if (DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN.equals("0"))//8. ayardaki checkbox secili değilse 9. ayar disable olsun
            {
                for (int i = 0; i < arl9.getChildCount(); i++)
                {
                    arl9.getChildAt(i).setEnabled(false);
                }
            }

            //9. ayar için önce 8. ayardaki checkbox'ın isaretlenmesi gerek
            ((CheckBox) arl8.getViewSecenek()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    for (int i = 0; i < arl9.getChildCount(); i++)
                    {
                        arl9.getChildAt(i).setEnabled(b);
                    }
                }
            });

            AyarlarRelativeLayout arl10 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.actionbar_rengi_sabit_olsun), DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN, Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN, Sabit.AYAR_ID_ARKAPLAN_RENGI, Sabit.SECENEK_CHECKBOX);
            anaRelativeLayout.addView(arl10);

            final AyarlarRelativeLayout arl11 = new AyarlarRelativeLayout(getActivity(), cnt.getString(R.string.actionbar_rengi), DEGER_AYAR_ACTIONBAR_RENGI, Sabit.AYAR_ID_ACTIONBAR_RENGI, Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN, Sabit.SECENEK_BUTTON);
            anaRelativeLayout.addView(arl11);
            if (DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN.equals("0"))//10. ayardaki checkbox secili değilse 11. ayar disable olsun
            {
                for (int i = 0; i < arl11.getChildCount(); i++)
                {
                    arl11.getChildAt(i).setEnabled(false);
                }
            }

            //11. ayar için önce 10. ayardaki checkbox'ın isaretlenmesi gerek
            ((CheckBox) arl10.getViewSecenek()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    for (int i = 0; i < arl11.getChildCount(); i++)
                    {
                        arl11.getChildAt(i).setEnabled(b);
                    }
                }
            });
        }

        //ayarlar ekranıda secilen ayarları döndurur
        public String ayarDegeriniGetir(int ayarID)
        {
            switch (ayarID)
            {
                case Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI:
                    AyarlarRelativeLayout arl1 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    String yeniDeger1 = ((EditText) arl1.getViewSecenek()).getText().toString();
                    return yeniDeger1;

                case Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN:
                    AyarlarRelativeLayout arl2 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    boolean yeniDeger2 = ((CheckBox) arl2.getViewSecenek()).isChecked();
                    if (yeniDeger2)
                    {
                        return "1";
                    }
                    else
                    {
                        return "0";
                    }

                case Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI:
                    AyarlarRelativeLayout arl3 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    String yeniDeger3 = ((EditText) arl3.getViewSecenek()).getText().toString();
                    return yeniDeger3;

                case Sabit.AYAR_ID_SIMGE_RENGI:
                    AyarlarRelativeLayout arl4 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    return arl4.getSecilenRenk();

                /*
                case Sabit.AYAR_ID_YAZI_RENGI:
                    AyarlarRelativeLayout arl5 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    return arl5.getSecilenRenk();
                */

                case Sabit.AYAR_ID_CERCEVE_GOZUKSUN:
                    AyarlarRelativeLayout arl6 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    boolean yeniDeger6 = ((CheckBox) arl6.getViewSecenek()).isChecked();
                    if (yeniDeger6)
                    {
                        return "1";
                    }
                    else
                    {
                        return "0";
                    }

                case Sabit.AYAR_ID_CERCEVE_RENGI:
                    AyarlarRelativeLayout arl7 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    return arl7.getSecilenRenk();

                case Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN:
                    AyarlarRelativeLayout arl8 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    boolean yeniDeger8 = ((CheckBox) arl8.getViewSecenek()).isChecked();
                    if (yeniDeger8)
                    {
                        return "1";
                    }
                    else
                    {
                        return "0";
                    }

                case Sabit.AYAR_ID_ARKAPLAN_RENGI:
                    AyarlarRelativeLayout arl9 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    return arl9.getSecilenRenk();

                case Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN:
                    AyarlarRelativeLayout arl10 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    boolean yeniDeger10 = ((CheckBox) arl10.getViewSecenek()).isChecked();
                    if (yeniDeger10)
                    {
                        return "1";
                    }
                    else
                    {
                        return "0";
                    }

                case Sabit.AYAR_ID_ACTIONBAR_RENGI:
                    AyarlarRelativeLayout arl11 = (AyarlarRelativeLayout) anaRelativeLayout.findViewById(ayarID);
                    return arl11.getSecilenRenk();

                default:
                    ekranaHataYazdir("52", cnt.getString(R.string.hatali_ayar_id) + " : " + ayarID);
                    return "-1";
            }
        }

        //girilen değerleri kontrol eder
        public boolean ayarlariKontrolEt()
        {
            boolean sonuc = true;
            Element element = documentAyar.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node nodeAyar = nodeList.item(i);
                int ayarID = Integer.valueOf(nodeAyar.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue());
                String yeniDeger = ayarDegeriniGetir(ayarID);
                if (!yeniDeger.equals("-1"))//ayarDegeriniGetir'de hata oluşursa girmesin
                {
                    switch (ayarID)
                    {
                        case Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI:
                            if (Integer.valueOf(yeniDeger) < 1)
                            {
                                Toast.makeText(getActivity(), cnt.getString(R.string.sutun_sayisi_1_den_kucuk_olamaz), Toast.LENGTH_SHORT).show();
                                sonuc = sonuc & false;
                            }
                            break;

                        case Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN:
                            //checkbox olduğu için kontrole gerek yok
                            break;

                        case Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI:
                            if (Integer.valueOf(yeniDeger) < 1)
                            {
                                Toast.makeText(getActivity(), cnt.getString(R.string.satir_sayisi_1_den_kucuk_olamaz), Toast.LENGTH_SHORT).show();
                                sonuc = sonuc & false;
                            }
                            break;

                        case Sabit.AYAR_ID_SIMGE_RENGI:
                            //önerilerden secildiği icin kontrol edecek birşey yok
                            break;

                        /*
                        case Sabit.AYAR_ID_YAZI_RENGI:
                            //önerilerden secildiği icin kontrol edecek birşey yok
                            break;
                        */

                        case Sabit.AYAR_ID_CERCEVE_GOZUKSUN:
                            //checkbox olduğu için kontrole gerek yok
                            break;

                        case Sabit.AYAR_ID_CERCEVE_RENGI:
                            //önerilerden secildiği icin kontrol edecek birşey yok
                            break;

                        case Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN:
                            //checkbox olduğu için kontrole gerek yok
                            break;

                        case Sabit.AYAR_ID_ARKAPLAN_RENGI:
                            //önerilerden secildiği icin kontrol edecek birşey yok
                            break;

                        case Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN:
                            //checkbox olduğu için kontrole gerek yok
                            break;

                        case Sabit.AYAR_ID_ACTIONBAR_RENGI:
                            //önerilerden secildiği icin kontrol edecek birşey yok
                            break;

                        default:
                            ekranaHataYazdir("24", cnt.getString(R.string.hatali_ayar_id) + " : " + ayarID);
                    }
                }
                else//ayar degeri getirilirken hata oluştu.
                {
                    return false;
                }
            }

            return sonuc;
        }

        public void ayarlariKaydet()
        {
            Element element = documentAyar.getDocumentElement();
            NodeList nodeList = element.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node nodeAyar = nodeList.item(i);
                String ayarDeger = nodeAyar.getTextContent();
                int ayarID = Integer.valueOf(nodeAyar.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue());
                String yeniDeger = ayarDegeriniGetir(ayarID);
                switch (ayarID)
                {
                    case Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI:
                        if (!ayarDeger.equals(yeniDeger))//deger degisti
                        {
                            nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                            DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI = yeniDeger;
                            elemanEniniHesapla();
                        }
                        break;

                    case Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN = yeniDeger;
                        break;

                    case Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI:
                        if (!ayarDeger.equals(yeniDeger))//deger degisti
                        {
                            nodeAyar.setTextContent(yeniDeger);
                            DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI = yeniDeger;
                            elemanBoyunuHesapla();
                        }
                        break;

                    case Sabit.AYAR_ID_SIMGE_RENGI:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_SIMGE_RENGI = yeniDeger;
                        ma.geriSimgesiniEkle();
                        break;

                    /*
                    case Sabit.AYAR_ID_YAZI_RENGI:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_YAZI_RENGI = yeniDeger;
                        break;
                    */

                    case Sabit.AYAR_ID_CERCEVE_GOZUKSUN:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_CERCEVE_GOZUKSUN = yeniDeger;
                        break;

                    case Sabit.AYAR_ID_CERCEVE_RENGI:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_CERCEVE_RENGI = yeniDeger;
                        break;

                    case Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN = yeniDeger;
                        break;

                    case Sabit.AYAR_ID_ARKAPLAN_RENGI:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_ARKAPLAN_RENGI = yeniDeger;
                        break;

                    case Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN = yeniDeger;
                        break;

                    case Sabit.AYAR_ID_ACTIONBAR_RENGI:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_ACTIONBAR_RENGI = yeniDeger;
                        break;

                    default:
                        ekranaHataYazdir("25", cnt.getString(R.string.hatali_ayar_id) + " : " + ayarID);
                }
            }
            documentToFile(Sabit.DOCUMENT_AYAR);
        }

        //actionBar daki kategori basligini yazar
        public void kategoriBasliginiYaz()
        {
            if (seciliCRL.getId() == 0)
            {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>/</font>"));
            }
            else
            {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>" + kategoriYolunuGetir(String.valueOf(seciliCRL.getId())) + "</font>"));
                //getActivity().getActionBar().setTitle(kategoriYolunuGetir(xmlParcaID));
            }
        }

        //kategori seciliyken duzenleye tıklanıldığı zaman girilen ismi xml'e kaydeder
        public void kategorininBaslikBilgisiniGuncelle(String baslik, int kategoriID)
        {
            Element element = document.getElementById(String.valueOf(kategoriID));
            Node nodeBaslik = element.getFirstChild();
            nodeBaslik.setTextContent(baslik);
            documentToFile(Sabit.DOCUMENT_ASIL);
        }

        //actionbar da renk tusuna dokunulduğu zaman ekrana gelen alertdialogu olusturur
        public void renkDialogunuOlustur()
        {
            RenkDugmeleri alertLL = new RenkDugmeleri(getActivity(), seciliCRL.getRenk(), listeRenkler);
            alertLL.setCagiranYer(alertLL.CAGIRAN_YER_FRAGMENT, this);
            CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.renk), cnt.getString(R.string.iptal), alertLL, Sabit.ALERTDIALOG_CUSTOM_VIEW);
            alertRenk = builder.create();
            alertRenk.setCanceledOnTouchOutside(true);
            alertRenk.show();
        }

        public void renkDialogunuOlustur(String renkKodu)
        {
            RenkDugmeleri alertLL = new RenkDugmeleri(getActivity(), renkKodu, listeRenkler);
            alertLL.setCagiranYer(alertLL.CAGIRAN_YER_FRAGMENT, this);
            CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), cnt.getString(R.string.renk), cnt.getString(R.string.iptal), alertLL, Sabit.ALERTDIALOG_CUSTOM_VIEW);
            alertRenk = builder.create();
            alertRenk.setCanceledOnTouchOutside(true);
            alertRenk.show();
        }

        //renk dialogunda renk secimi yapıldı
        public void renkSecimiYapildi(String secilenRenk)
        {
            switch (getElemanTuru())
            {
                case Sabit.ELEMAN_TUR_KATEGORI:
                {
                    LinearLayout llBaslik = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(secilenRenk));
                    alertRenk.dismiss();

                    //actionbar rengi sabit ise degistirilmesin
                    if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        ma.actionBarArkaPlanDegistir(secilenRenk);
                    }
                    else
                    {
                        ma.actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
                    }
                }

                    /*
                    alertRenk.dismiss();

                    Element element = document.getElementById(String.valueOf(xmlParcaID));
                    etiketiGuncelle(element, secilenRenk, Sabit.XML_RENK);
                    documentToFile(Sabit.DOCUMENT_ASIL);

                    //actionbar rengi sabit ise degistirilmesin
                    if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        ma.actionBarArkaPlanDegistir(secilenRenk);
                    }
                    else
                    {
                        ma.actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
                    }

                    //arkaplan rengi sabit ise degistirilmesin
                    if (DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        ekranRenginiDegistir();
                    }

                    seciliCRL.setRenk(secilenRenk);
                    */

                break;

                case Sabit.ELEMAN_TUR_KAYIT:
                {
                    LinearLayout llBaslik = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(secilenRenk));
                    alertRenk.dismiss();
                }
                    /*
                    kaydinRenkBilgisiniGuncelle(secilenRenk);
                    if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        ma.actionBarArkaPlanDegistir(secilenRenk);
                    }
                    else
                    {
                        ma.actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
                    }
                    seciliCRL.setRenk(secilenRenk);
                    ekranRenginiDegistir();
                    */
                break;

                case Sabit.ELEMAN_TUR_YENI_KAYIT:
                {
                    LinearLayout llBaslik = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(secilenRenk));
                    alertRenk.dismiss();
                }
                break;

                case Sabit.ELEMAN_TUR_YENI_KATEGORI:
                {
                    LinearLayout llBaslik = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(secilenRenk));
                    alertRenk.dismiss();
                }
                break;

                /* ayarlar AyarlarRelativeLayout içinde yakalanıyor
                case Sabit.ELEMAN_TUR_AYARLAR:
                    break;
                case Sabit.ELEMAN_TUR_YEDEK:
                    break;
                */

                default:
                    ekranaHataYazdir("49", cnt.getString(R.string.hatali_kayit_turu) + " : " + getElemanTuru());
            }
        }

        //icine girilen kategori ve kayıt rengine göre ekranın rengini degistirir
        public void ekranRenginiDegistir()
        {
            anaLinearLayout.setBackgroundColor(Color.parseColor(seciliCRL.getRenk()));
            //ll.getBackground().setAlpha(128);
        }

        //view'ın arka plan rengini getirir
        public String arkaplanRenginiGetir(View v)
        {
            String renkKodu = Integer.toHexString(((ColorDrawable) v.getBackground()).getColor()).toUpperCase();
            renkKodu = renkKodu.substring(2, 8);//alfa degerini siliyorum
            renkKodu = "#" + renkKodu;
            return renkKodu;
        }

        //ikonu actionBara ekler, renk bilgisini disaridan alır
        public void menuIkonEkle(Menu menu, Drawable drawable, int Action, String baslik, int id, String renk)
        {
            drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(renk), PorterDuff.Mode.SRC_IN));
            menu.add(0, id, 0, baslik).setIcon(drawable).setShowAsAction(Action);
        }

        //ikonu actionBara ekler
        public void menuIkonEkle(Menu menu, Drawable drawable, int Action, String baslik, int id)
        {
            drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(DEGER_AYAR_SIMGE_RENGI), PorterDuff.Mode.SRC_IN));
            menu.add(0, id, 0, baslik).setIcon(drawable).setShowAsAction(Action);
        }

        public void menuYeniKategori(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.renk_degistir), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.renk_degistir), Sabit.ACTION_YENI_KATEGORI_RENK_DEGISTIR);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kaydet), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.kaydet), Sabit.ACTION_YENI_KATEGORI_KAYDET);
        }

        public void menuYeniKayit(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.renk_degistir), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.renk_degistir), Sabit.ACTION_YENI_KAYIT_RENK_DEGISTIR);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kaydet), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.kaydet), Sabit.ACTION_YENI_KAYIT_KAYDET);
        }

        public void menuAnaEkran(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.duzenle), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.duzenle), Sabit.ACTION_KATEGORI_DUZENLE);
            //menuIkonEkle(menu, getResources().getDrawable(R.drawable.kategori_ekle), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.kategori_ekle), Sabit.ACTION_ANA_EKRAN_KATEGORI_EKLE);
            //menuIkonEkle(menu, getResources().getDrawable(R.drawable.kayit_ekle), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.kayit_ekle), Sabit.ACTION_ANA_EKRAN_KAYIT_EKLE);
            //menu.add(0, Sabit.ACTION_ANA_EKRAN_YEDEKLE, 0, cnt.getString(R.string.yedekle));
            //menu.add(0, Sabit.ACTION_ANA_EKRAN_YEDEGI_YUKLE, 0, cnt.getString(R.string.yedegi_yukle));
            //menu.add(0, Sabit.ACTION_ANA_EKRAN_YEDEKLERI_GOSTER, 0, cnt.getString(R.string.yedek_dosyalari));
            //menu.add(0, Sabit.ACTION_ANA_EKRAN_AYARLAR, 0, cnt.getString(R.string.ayarlar));
        }

        public void menuSecim(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.tamam), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.tamamla), Sabit.ACTION_SECIM_TAMAM);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.yeni), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.yeni), Sabit.ACTION_SECIM_YENI);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.sil), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.sil), Sabit.ACTION_SECIM_SIL);
        }

        public void menuKayitDegistir(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.renk_degistir), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.renk_degistir), Sabit.ACTION_KAYIT_DEGISTIR_RENK_DEGISTIR);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kaydet), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.kaydet), Sabit.ACTION_KAYIT_DEGISTIR_KAYDET);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.tamam), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.tamamla), Sabit.ACTION_KAYIT_DEGISTIR_TAMAM);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.yeni), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.yeni), Sabit.ACTION_KAYIT_DEGISTIR_YENI);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.sil), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.sil), Sabit.ACTION_KAYIT_DEGISTIR_SIL);
        }

        public void menuKategoriDegistir(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.renk_degistir), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.renk_degistir), Sabit.ACTION_KATEGORI_DEGISTIR_RENK_DEGISTIR);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kaydet), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.kaydet), Sabit.ACTION_KATEGORI_DEGISTIR_KAYDET);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.tamam), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.tamamla), Sabit.ACTION_KATEGORI_DEGISTIR_TAMAM);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.yeni), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.yeni), Sabit.ACTION_KATEGORI_DEGISTIR_YENI);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.sil), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.sil), Sabit.ACTION_KATEGORI_DEGISTIR_SIL);
        }

        public void menuYedek(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.sil), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.sil), Sabit.ACTION_YEDEK_SIL, Sabit.RENK_SIYAH);
        }

        public void menuAyar(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.ontanimli), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.sifirla), Sabit.ACTION_AYAR_ONTANIMLI, Sabit.RENK_SIYAH);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kaydet), MenuItem.SHOW_AS_ACTION_ALWAYS, cnt.getString(R.string.kaydet), Sabit.ACTION_AYAR_KAYDET, Sabit.RENK_SIYAH);
        }

        /*
        @Override
        public void onPrepareOptionsMenu(Menu menu)
        {
            try
            {
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem mi = menu.getItem(i);
                    //mi.setIcon(R.drawable.sil);
                    String title = mi.getTitle().toString();
                    Spannable spannable = new SpannableString(title);
                    spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mi.setTitle(spannable);
                }
            } catch (Exception ex) {
                Log.d("uyg3", "ex : "+ ex.getMessage());
            }
        }
        */

        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            fAct = activity;
            ma = (MainActivity) fAct;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
            super.onCreateOptionsMenu(menu, inflater);

            inflaterActionBar = inflater;
            menuActionBar = menu;

            switch (ACTIONBAR_TUR)
            {
                case Sabit.ACTIONBAR_YENI_KATEGORI:
                    menuYeniKategori(menu);
                    break;

                case Sabit.ACTIONBAR_YENI_KAYIT:
                    menuYeniKayit(menu);
                    break;

                case Sabit.ACTIONBAR_EKLE:
                    menuAnaEkran(menu);
                    break;

                case Sabit.ACTIONBAR_SECIM:
                    menuSecim(menu);
                    break;

                /*
                case ACTIONBAR_ONAY:
                    inflater.inflate(R.menu.menu_onay, menu);
                    break;
                */

                case Sabit.ACTIONBAR_DEGISTIR:
                    menuKayitDegistir(menu);
                    switch (seciliCRL.getDurum())
                    {
                        case Sabit.DURUM_TAMAMLANDI:
                            menu.findItem(Sabit.ACTION_KAYIT_DEGISTIR_TAMAM).setVisible(false);
                            break;

                        case Sabit.DURUM_YENI:
                            menu.findItem(Sabit.ACTION_KAYIT_DEGISTIR_YENI).setVisible(false);
                            break;

                        default:
                            ekranaHataYazdir("26", cnt.getString(R.string.hatali_kategori_durumu_turu) + " : " + seciliCRL.getDurum());
                    }
                    break;

                case Sabit.ACTIONBAR_KATEGORI_DEGISTIR:
                    menuKategoriDegistir(menu);
                    switch (seciliCRL.getDurum())
                    {
                        case Sabit.DURUM_TAMAMLANDI:
                            menu.findItem(Sabit.ACTION_KATEGORI_DEGISTIR_TAMAM).setVisible(false);
                            break;

                        case Sabit.DURUM_YENI:
                            menu.findItem(Sabit.ACTION_KATEGORI_DEGISTIR_YENI).setVisible(false);
                            break;

                        default:
                            ekranaHataYazdir("26", cnt.getString(R.string.hatali_kayit_durumu_turu) + " : " + seciliCRL.getDurum());
                    }
                    break;

                case Sabit.ACTIONBAR_YEDEK:
                    menuYedek(menu);
                    break;

                case Sabit.ACTIONBAR_AYAR:
                    menuAyar(menu);
                    break;

                default:
                    ekranaHataYazdir("27", cnt.getString(R.string.hatali_actionbar_turu) + " : " + ACTIONBAR_TUR);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                //ana ekran
                case Sabit.ACTION_ANA_EKRAN_KATEGORI_EKLE:
                    yeniKategoriEkranininAc();
                    return true;

                case Sabit.ACTION_ANA_EKRAN_KAYIT_EKLE:
                    yeniKayitEkraniniAc();
                    //actionBarDegistir(ACTIONBAR_ONAY);
                    //etEklenecek = yaziAlaniOlustur();
                    //klavyeAc(getActivity().getApplicationContext(), etEklenecek);
                    return true;

                case Sabit.ACTION_ANA_EKRAN_RENK_DEGISTIR:
                    renkDialogunuOlustur();
                    return true;

                case Sabit.ACTION_ANA_EKRAN_YEDEKLE:
                    xmlYedekle();
                    return true;

                case Sabit.ACTION_ANA_EKRAN_YEDEGI_YUKLE:
                    xmlYedektenYukle();
                    return true;

                case Sabit.ACTION_ANA_EKRAN_YEDEKLERI_GOSTER:
                    actionBarDegistir(Sabit.ACTIONBAR_YEDEK);
                    yedekDosyalariGoster();
                    return true;

                case Sabit.ACTION_ANA_EKRAN_AYARLAR:
                    actionBarDegistir(Sabit.ACTIONBAR_AYAR);
                    ayarEkraniniAc();
                    return true;

                case Sabit.ACTION_KATEGORI_DUZENLE:
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategoriDuzenle()).addToBackStack(null).commit();
                    return true;
                //ana ekran
                //secim
                case Sabit.ACTION_SECIM_TAMAM:
                    seciliElemanlariTamamla();
                    Toast.makeText(getActivity(), cnt.getString(R.string.secili_kayitlar_tamamlandi_olarak_isaretlendi), Toast.LENGTH_SHORT).show();
                    return true;

                case Sabit.ACTION_SECIM_YENI:
                    seciliElemanlarYeni();
                    Toast.makeText(getActivity(), cnt.getString(R.string.secili_kayitlar_yeni_olarak_isaretlendi), Toast.LENGTH_SHORT).show();
                    return true;

                case Sabit.ACTION_SECIM_SIL:
                    seciliElemanlariSilDiyaloguOlustur();
                    return true;
                //secim
                //kayit degistir
                case Sabit.ACTION_KAYIT_DEGISTIR_SIL:
                    kayitSilDiyaloguOlustur();
                    return true;

                case Sabit.ACTION_KAYIT_DEGISTIR_TAMAM:
                    Element elementKayitTamam = kayitDurumunuTamamlandiYap();
                    if (elementKayitTamam != null)
                    {
                        ustParcaDurumunuKontrolEtTamamla(elementKayitTamam);
                        actionBarDurumSimgesiDegistirKayit(Sabit.DURUM_TAMAMLANDI);
                        Toast.makeText(getActivity(), cnt.getString(R.string.kayit_tamamlandi_olarak_isaretlendi), Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case Sabit.ACTION_KAYIT_DEGISTIR_YENI:
                    Element elementKayitYeni = kayitDurumunuYeniYap();
                    if (elementKayitYeni != null)
                    {
                        ustParcaDurumunuKontrolEtYeni(elementKayitYeni);
                        actionBarDurumSimgesiDegistirKayit(Sabit.DURUM_YENI);
                        Toast.makeText(getActivity(), cnt.getString(R.string.kayit_yeni_olarak_isaretlendi), Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case Sabit.ACTION_KAYIT_DEGISTIR_KAYDET:
                    kayitDegistirKaydet();
                    klavyeKapat();
                    return true;

                case Sabit.ACTION_KAYIT_DEGISTIR_RENK_DEGISTIR:
                    renkDialogunuOlustur(arkaplanRenginiGetir(fragmentRootViewYeniKayit.findViewById(llBaslikID)));
                    return true;
                //kayit degistir
                //kategori degistir
                case Sabit.ACTION_KATEGORI_DEGISTIR_SIL:
                    kategoriSilDiyaloguOlustur();
                    return true;

                case Sabit.ACTION_KATEGORI_DEGISTIR_RENK_DEGISTIR:
                    renkDialogunuOlustur(arkaplanRenginiGetir(fragmentRootViewYeniKayit.findViewById(llBaslikID)));
                    return true;

                case Sabit.ACTION_KATEGORI_DEGISTIR_KAYDET:
                    kategoriDegistirKaydet();
                    klavyeKapat();
                    return true;

                case Sabit.ACTION_KATEGORI_DEGISTIR_TAMAM:
                    Element elementKategoriTamam = kayitDurumunuTamamlandiYap();
                    if (elementKategoriTamam != null)
                    {
                        ustParcaDurumunuKontrolEtTamamla(elementKategoriTamam);
                        kategoriCocuklariniDurumunuGuncelle(elementKategoriTamam, Sabit.DURUM_TAMAMLANDI);
                        actionBarDurumSimgesiDegistirKategori(Sabit.DURUM_TAMAMLANDI);
                        Toast.makeText(getActivity(), cnt.getString(R.string.kayit_tamamlandi_olarak_isaretlendi), Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case Sabit.ACTION_KATEGORI_DEGISTIR_YENI:
                    Element elementKategoriYeni = kayitDurumunuYeniYap();
                    if (elementKategoriYeni != null)
                    {
                        ustParcaDurumunuKontrolEtYeni(elementKategoriYeni);
                        kategoriCocuklariniDurumunuGuncelle(elementKategoriYeni, Sabit.DURUM_YENI);
                        actionBarDurumSimgesiDegistirKategori(Sabit.DURUM_YENI);
                        Toast.makeText(getActivity(), cnt.getString(R.string.kayit_yeni_olarak_isaretlendi), Toast.LENGTH_SHORT).show();
                    }
                    return true;

                //kategori degistir
                //yedek
                case Sabit.ACTION_YEDEK_SIL:
                    seciliYedekDosyalariniSil();
                    return true;
                //yedek
                //ayar
                case Sabit.ACTION_AYAR_KAYDET:
                    klavyeKapat();
                    if (ayarlariKontrolEt())//ayarlarda sorun yoksa kaydetsin
                    {
                        ayarlariKaydet();
                        ustSeviyeyiGetir();
                    }
                    return true;

                case Sabit.ACTION_AYAR_ONTANIMLI:
                    ayarlariSifirla();
                    return true;
                //ayar
                //yeni kayit
                case Sabit.ACTION_YENI_KAYIT_RENK_DEGISTIR:
                    renkDialogunuOlustur(arkaplanRenginiGetir(fragmentRootViewYeniKayit.findViewById(llBaslikID)));
                    return true;

                case Sabit.ACTION_YENI_KAYIT_KAYDET:
                {
                    klavyeKapat();
                    EditText etBaslik = (EditText) fragmentRootViewYeniKayit.findViewById(etBaslikID);
                    EditText etKayit = (EditText) fragmentRootViewYeniKayit.findViewById(etKayitID);
                    LinearLayout llBaslik = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
                    String renkKodu = arkaplanRenginiGetir(llBaslik);
                    int eklenenID = xmlDosyasinaKayitEkle(etBaslik.getText().toString(), etKayit.getText().toString(), renkKodu);
                    anaRelativeLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.anaRelativeLayout);
                    kayitlariAnaEkranaEkle(etBaslik.getText().toString(), etKayit.getText().toString(), eklenenID, Sabit.DURUM_YENI, Sabit.KAYIT_ONTANIMLI_RENK, globalYerlesim);
                    ustSeviyeyiGetir();
                }
                return true;
                //yeni kayit
                //yeni kategori
                case Sabit.ACTION_YENI_KATEGORI_RENK_DEGISTIR:
                    renkDialogunuOlustur(arkaplanRenginiGetir(fragmentRootViewYeniKayit.findViewById(llBaslikID)));
                    return true;

                case Sabit.ACTION_YENI_KATEGORI_KAYDET:
                {
                    klavyeKapat();
                    EditText etBaslik = (EditText) fragmentRootViewYeniKayit.findViewById(etBaslikID);
                    EditText etKayit = (EditText) fragmentRootViewYeniKayit.findViewById(etKayitID);
                    LinearLayout llBaslik = (LinearLayout) fragmentRootViewYeniKayit.findViewById(llBaslikID);
                    String renkKodu = arkaplanRenginiGetir(llBaslik);

                    int eklenenID = xmlDosyasinaKategoriEkle(etBaslik.getText().toString(), renkKodu);
                    anaRelativeLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.anaRelativeLayout);
                    kategoriyiAnaEkranaEkle(etBaslik.getText().toString(), eklenenID, Sabit.DURUM_YENI, Sabit.KAYIT_ONTANIMLI_RENK, globalYerlesim);
                    ustSeviyeyiGetir();
                }
                return true;
                //yeni kategori

                case android.R.id.home:
                    switch (FRAGMENT_ETKIN_EKRAN)
                    {
                        //case girdigi ekranlarda ustsol tus ust seviyeyi getirir, diger durumda yan paneli acar
                        case Sabit.FRAGMENT_YENI_KAYIT_EKRANI:
                        case Sabit.FRAGMENT_YENI_KATEGORI_EKRANI:
                        case Sabit.FRAGMENT_AYAR_EKRANI:
                        case Sabit.FRAGMENT_YEDEK_EKRANI:
                        case Sabit.FRAGMENT_KATEGORI_DUZENLE_EKRANI:
                            klavyeKapat();
                            ustSeviyeyiGetir();
                            break;

                        default:
                            mDrawerToggle.onOptionsItemSelected(item);
                    }
                    break;

                default:
                    ekranaHataYazdir("55", cnt.getString(R.string.hatali_action_id) + " : " + item.getItemId());


                /*
                case R.id.action_onay_kaydet:
                    //yeniKayitEkraniniAc(etEklenecek);
                    //actionBarDegistir(Sabit.ACTIONBAR_EKLE);
                    return true;
                case R.id.action_onay_iptal:
                    //klavyeKapat(etEklenecek.getWindowToken());
                    //anaRelativeLayout.removeView(etEklenecek);
                    //actionBarDegistir(Sabit.ACTIONBAR_EKLE);
                    return true;
                */
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            switch (FRAGMENT_ETKIN_EKRAN)
            {
                case Sabit.FRAGMENT_KATEGORI_DUZENLE_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_yeni_kayit, container, false);
                    fragmentRootViewYeniKayit = rootView;
                    RelativeLayout yeniKayitRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.yeniKayitRelativeLayout);

                    //klavye acilinca ekran yukarı dogru kaymasın sadece edittext oynasın
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    new YeniElemanLayout(yeniKayitRelativeLayout, seciliCRL, Sabit.FRAGMENT_KATEGORI_DUZENLE_EKRANI);

                    /*
                    //yeni kayıt fragmentinde baslik kısmı
                    LinearLayout llBaslik = new LinearLayout(getActivity());
                    llBaslik.setId(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(seciliCRL.getRenk()));
                    EditText etBaslik = new EditText(getActivity());
                    etBaslik.setId(etBaslikID);
                    etBaslik.setSingleLine(true);
                    etBaslik.setHint(cnt.getString(R.string.baslik));
                    etBaslik.setBackground(null);//edittext te altcizgi cikmasin
                    etBaslik.setText(seciliCRL.getBaslik());
                    LinearLayout.LayoutParams lpBaslik = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpBaslik.setMargins((int) dpGetir(20), (int) dpGetir(40), (int) dpGetir(20), (int) dpGetir(40));
                    etBaslik.setLayoutParams(lpBaslik);
                    llBaslik.addView(etBaslik);
                    RelativeLayout.LayoutParams lpBaslik2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpBaslik2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    llBaslik.setLayoutParams(lpBaslik2);

                    //yeni kayıt fragmentinde yazı kısmı
                    LinearLayout llKayit = new LinearLayout(getActivity());
                    EditText etKayit = new EditText(getActivity());
                    etKayit.setId(etKayitID);
                    etKayit.setHint(cnt.getString(R.string.not));
                    etKayit.setBackground(null);//edittext te altcigi cikmasin
                    LinearLayout.LayoutParams lpKayit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpKayit.setMargins((int) dpGetir(20), 0, (int) dpGetir(20), 0);
                    etKayit.setLayoutParams(lpKayit);
                    llKayit.addView(etKayit);
                    RelativeLayout.LayoutParams lpKayit2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpKayit2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lpKayit2.addRule(RelativeLayout.BELOW, llBaslik.getId());
                    llKayit.setLayoutParams(lpKayit2);

                    LinearLayout llYeniKayit = new LinearLayout(getActivity());
                    llYeniKayit.setOrientation(LinearLayout.VERTICAL);
                    RelativeLayout.LayoutParams lpYeniKayit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    llYeniKayit.setLayoutParams(lpYeniKayit);
                    yeniKayitRelativeLayout.addView(llBaslik);
                    yeniKayitRelativeLayout.addView(llKayit);
                    */

                    ma.geriSimgesiniEkle();

                    ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>" + seciliCRL.getBaslik() + "</font>"));

                    return rootView;
                }
                case Sabit.FRAGMENT_YENI_KATEGORI_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_yeni_kayit, container, false);
                    fragmentRootViewYeniKayit = rootView;
                    RelativeLayout yeniKayitRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.yeniKayitRelativeLayout);

                    //klavye acilinca ekran yukarı dogru kaymasın sadece edittext oynasın
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    new YeniElemanLayout(yeniKayitRelativeLayout, seciliCRL, Sabit.FRAGMENT_KATEGORI_DUZENLE_EKRANI);

                    /*
                    //yeni kayıt fragmentinde baslik kısmı
                    LinearLayout llBaslik = new LinearLayout(getActivity());
                    llBaslik.setId(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(Sabit.KAYIT_ONTANIMLI_RENK));
                    EditText etBaslik = new EditText(getActivity());
                    etBaslik.setId(etBaslikID);
                    etBaslik.setSingleLine(true);
                    etBaslik.setHint(cnt.getString(R.string.baslik));
                    etBaslik.setBackground(null);//edittext te altcigi cikmasin
                    LinearLayout.LayoutParams lpBaslik = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpBaslik.setMargins((int) dpGetir(20), (int) dpGetir(40), (int) dpGetir(20), (int) dpGetir(40));
                    etBaslik.setLayoutParams(lpBaslik);
                    llBaslik.addView(etBaslik);
                    RelativeLayout.LayoutParams lpBaslik2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpBaslik2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    llBaslik.setLayoutParams(lpBaslik2);

                    //yeni kayıt fragmentinde yazı kısmı
                    LinearLayout llKayit = new LinearLayout(getActivity());
                    EditText etKayit = new EditText(getActivity());
                    etKayit.setId(etKayitID);
                    etKayit.setHint(cnt.getString(R.string.not));
                    etKayit.setBackground(null);//edittext te altcigi cikmasin
                    LinearLayout.LayoutParams lpKayit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpKayit.setMargins((int) dpGetir(20), 0, (int) dpGetir(20), 0);
                    etKayit.setLayoutParams(lpKayit);
                    llKayit.addView(etKayit);
                    RelativeLayout.LayoutParams lpKayit2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpKayit2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lpKayit2.addRule(RelativeLayout.BELOW, llBaslik.getId());
                    llKayit.setLayoutParams(lpKayit2);

                    LinearLayout llYeniKayit = new LinearLayout(getActivity());
                    llYeniKayit.setOrientation(LinearLayout.VERTICAL);
                    RelativeLayout.LayoutParams lpYeniKayit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    llYeniKayit.setLayoutParams(lpYeniKayit);
                    yeniKayitRelativeLayout.addView(llBaslik);
                    yeniKayitRelativeLayout.addView(llKayit);
                    */

                    ma.geriSimgesiniEkle();

                    ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>" + cnt.getString(R.string.yeni_kategori) + "</font>"));

                    return rootView;
                }

                case Sabit.FRAGMENT_YENI_KAYIT_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_yeni_kayit, container, false);
                    fragmentRootViewYeniKayit = rootView;
                    RelativeLayout yeniKayitRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.yeniKayitRelativeLayout);

                    //klavye acilinca ekran yukarı dogru kaymasın sadece edittext oynasın
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    new YeniElemanLayout(yeniKayitRelativeLayout, seciliCRL, Sabit.FRAGMENT_YENI_KAYIT_EKRANI);

                    /*
                    //yeni kayıt fragmentinde baslik kısmı
                    LinearLayout llBaslik = new LinearLayout(getActivity());
                    llBaslik.setId(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(Sabit.KAYIT_ONTANIMLI_RENK));
                    EditText etBaslik = new EditText(getActivity());
                    etBaslik.setId(etBaslikID);
                    etBaslik.setSingleLine(true);
                    etBaslik.setHint(cnt.getString(R.string.baslik));
                    etBaslik.setBackground(null);//edittext te altcigi cikmasin
                    LinearLayout.LayoutParams lpBaslik = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpBaslik.setMargins((int) dpGetir(20), (int) dpGetir(40), (int) dpGetir(20), (int) dpGetir(40));
                    etBaslik.setLayoutParams(lpBaslik);
                    llBaslik.addView(etBaslik);
                    RelativeLayout.LayoutParams lpBaslik2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpBaslik2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    llBaslik.setLayoutParams(lpBaslik2);

                    //yeni kayıt fragmentinde yazı kısmı
                    LinearLayout llKayit = new LinearLayout(getActivity());
                    EditText etKayit = new EditText(getActivity());
                    etKayit.setId(etKayitID);
                    etKayit.setHint(cnt.getString(R.string.not));
                    etKayit.setBackground(null);//edittext te altcigi cikmasin
                    LinearLayout.LayoutParams lpKayit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpKayit.setMargins((int) dpGetir(20), 0, (int) dpGetir(20), 0);
                    etKayit.setLayoutParams(lpKayit);
                    llKayit.addView(etKayit);
                    RelativeLayout.LayoutParams lpKayit2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpKayit2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lpKayit2.addRule(RelativeLayout.BELOW, llBaslik.getId());
                    llKayit.setLayoutParams(lpKayit2);

                    LinearLayout llYeniKayit = new LinearLayout(getActivity());
                    llYeniKayit.setOrientation(LinearLayout.VERTICAL);
                    RelativeLayout.LayoutParams lpYeniKayit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    llYeniKayit.setLayoutParams(lpYeniKayit);
                    yeniKayitRelativeLayout.addView(llBaslik);
                    yeniKayitRelativeLayout.addView(llKayit);
                    */

                    ma.geriSimgesiniEkle();
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>" + cnt.getString(R.string.yeni_not) + "</font>"));

                    return rootView;
                }
                case Sabit.FRAGMENT_KATEGORI_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    fragmentRootView = rootView;
                    anaLinearLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.anaLinearLayout);
                    anaRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.anaRelativeLayout);

                    if (ma != null)//ilk açılışta null geliyor. o zaman activity'nin içinde actionbar rengini değiştiriyorum
                    {
                        if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
                        {
                            ma.actionBarArkaPlanDegistir(seciliCRL.getRenk());
                        }
                        else
                        {
                            ma.actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
                        }
                    }
                    if (DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        ekranRenginiDegistir();
                    }
                    else
                    {
                        ekranRenginiDegistir(DEGER_AYAR_ARKAPLAN_RENGI);
                    }
                    ma.menuSimgesiEkle();

                    if (xmlEnBuyukID > 0)//xml de kayıt varsa ekrana eklesin
                    {
                        yeniYerlesimOlustur();
                        parseXml(String.valueOf(seciliCRL.getId()));
                        kategoriBasliginiYaz();
                    }
                    else//xml'de kayıt yok
                    {
                        yeniYerlesimOlustur();
                        kategoriBasliginiYaz();
                    }

                    new FloatingActionButton(fAct, anaLinearLayout, this);//ekrana floatinActionButton ekleniyopr

                    return rootView;
                }

                case Sabit.FRAGMENT_KAYIT_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_yeni_kayit, container, false);
                    fragmentRootViewYeniKayit = rootView;
                    RelativeLayout yeniKayitRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.yeniKayitRelativeLayout);

                    new YeniElemanLayout(yeniKayitRelativeLayout, seciliCRL, Sabit.FRAGMENT_KAYIT_EKRANI);

                    /*
                    //kayıt fragmentinde baslik kısmı
                    LinearLayout llBaslik = new LinearLayout(getActivity());
                    llBaslik.setId(llBaslikID);
                    llBaslik.setBackgroundColor(Color.parseColor(seciliCRL.getRenk()));
                    EditText etBaslik = new EditText(getActivity());
                    etBaslik.setId(etBaslikID);
                    etBaslik.setSingleLine(true);
                    etBaslik.setHint(cnt.getString(R.string.baslik));
                    etBaslik.setBackground(null);//edittext te altcizgi cikmasin
                    etBaslik.setText(seciliCRL.getTvBaslik().getText());
                    LinearLayout.LayoutParams lpBaslik = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpBaslik.setMargins((int) dpGetir(20), (int) dpGetir(40), (int) dpGetir(20), (int) dpGetir(40));
                    etBaslik.setLayoutParams(lpBaslik);
                    llBaslik.addView(etBaslik);
                    RelativeLayout.LayoutParams lpBaslik2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpBaslik2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    llBaslik.setLayoutParams(lpBaslik2);

                    //kayıt fragmentinde yazı kısmı
                    LinearLayout llKayit = new LinearLayout(getActivity());
                    EditText etKayit = new EditText(getActivity());
                    etKayit.setId(etKayitID);
                    etKayit.setHint(cnt.getString(R.string.not));
                    etKayit.setBackground(null);//edittext te altcizgi cikmasin
                    etKayit.setText(seciliCRL.getTvYazi().getText());
                    LinearLayout.LayoutParams lpKayit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lpKayit.setMargins((int) dpGetir(20), 0, (int) dpGetir(20), 0);
                    etKayit.setLayoutParams(lpKayit);
                    llKayit.addView(etKayit);
                    RelativeLayout.LayoutParams lpKayit2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpKayit2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lpKayit2.addRule(RelativeLayout.BELOW, llBaslik.getId());
                    llKayit.setLayoutParams(lpKayit2);

                    LinearLayout llYeniKayit = new LinearLayout(getActivity());
                    llYeniKayit.setOrientation(LinearLayout.VERTICAL);
                    RelativeLayout.LayoutParams lpYeniKayit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    llYeniKayit.setLayoutParams(lpYeniKayit);
                    yeniKayitRelativeLayout.addView(llBaslik);
                    yeniKayitRelativeLayout.addView(llKayit);
                    */

                    ma.menuSimgesiEkle();
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    /*
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    fragmentRootView = rootView;
                    anaLinearLayout = (LinearLayout) fragmentRootView.findViewById(R.id.anaLinearLayout);
                    anaRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.anaRelativeLayout);

                    if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        ma.actionBarArkaPlanDegistir(kayitRenkBilgisiniGetir(String.valueOf(seciliCRL.getId())));
                    }
                    else
                    {
                        ma.actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
                    }

                    if (DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        ekranRenginiDegistir();
                    }
                    ma.geriSimgesiniEkle();

                    //ekran dondugu zaman actionbar guncellensin
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    getActivity().getActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>" + kategoriBasligi + "</font>"));
                    //////////

                    RelativeLayout rl = new RelativeLayout(getActivity());
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    etDegisecek = new EditText(getActivity());
                    etDegisecek.setText(getArguments().getString(Sabit.FRAGMENT_YAZI));
                    etDegisecek.requestFocus();
                    rl.addView(etDegisecek, lp);
                    anaRelativeLayout.addView(rl);

                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    klavyeAc(getActivity().getApplicationContext(), etDegisecek);
                    */

                    return rootView;
                }

                case Sabit.FRAGMENT_YEDEK_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    fragmentRootView = rootView;
                    anaLinearLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.anaLinearLayout);
                    anaRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.anaRelativeLayout);

                    //((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    //((ActionBarActivity)getActivity()).getSupportActionBar().setTitle((Html.fromHtml("<font color='" + Sabit.RENK_SIYAH + "'>" + cnt.getString(R.string.yedek_dosyalari) + "</font>")));
                    ekranRenginiDegistir(Sabit.RENK_BEYAZ2);
                    ma.actionBarArkaPlanDegistir(Sabit.RENK_BEYAZ2);
                    ma.geriSimgesiniEkle(Sabit.RENK_SIYAH);

                    /*
                    Spannable text = new SpannableString(getActivity().getActionBar().getTitle());
                    text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    getActivity().getActionBar().setTitle(text);
                    */

                    List<String> yedekler = yedekDosyalariniGetir();

                    for (int i = 0; i < yedekler.size(); i++)
                    {
                        final String yedekIsmi = yedekler.get(i);
                        YedekRelativeLayout yrl = new YedekRelativeLayout(getActivity(), yedekIsmi);
                        yrl.setId(i + 1);//id 0 olmamalı
                        if (i != 0)//ilk satırdaki eleman için param'a gerek yok
                        {
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.BELOW, i);
                            yrl.setLayoutParams(lp);
                        }
                        anaRelativeLayout.addView(yrl);
                    }
                    return rootView;
                }

                case Sabit.FRAGMENT_AYAR_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    fragmentRootView = rootView;
                    anaLinearLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.anaLinearLayout);
                    anaRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.anaRelativeLayout);

                    //((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    //((ActionBarActivity)getActivity()).getSupportActionBar().setTitle((Html.fromHtml("<font color='" + Sabit.RENK_SIYAH + "'>" + cnt.getString(R.string.ayarlar) + "</font>")));
                    ekranRenginiDegistir(Sabit.RENK_BEYAZ2);
                    ma.actionBarArkaPlanDegistir(Sabit.RENK_BEYAZ2);
                    ma.geriSimgesiniEkle(Sabit.RENK_SIYAH);
                    ayarlariAyarEkraninaEkle();

                    return rootView;
                }

                default:
                    ekranaHataYazdir("19", cnt.getString(R.string.hatali_fragment_id) + " : " + FRAGMENT_ETKIN_EKRAN);
                    return null;
            }
        }
    }
}
