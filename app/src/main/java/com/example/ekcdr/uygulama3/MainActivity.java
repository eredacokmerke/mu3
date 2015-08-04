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
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainActivity extends Activity
{
    public static String xmlYedekKlasorYolu;
    public static int elemanEnUzunluğu;
    public static int elemanBoyUzunluğu;
    public static String DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI;
    public static String DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN;
    public static String DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI;
    public static String DEGER_AYAR_SIMGE_RENGI;
    public static String DEGER_AYAR_YAZI_RENGI;
    public static String DEGER_AYAR_CERCEVE_GOZUKSUN;
    public static String DEGER_AYAR_CERCEVE_RENGI;
    public static String DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN;
    public static String DEGER_AYAR_ARKAPLAN_RENGI;
    public static String DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN;
    public static String DEGER_AYAR_ACTIONBAR_RENGI;
    public static Resources mResources;
    private static String xmlDosyaYolu;
    private static String xmlAyarDosyaYolu;
    private static int ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;
    private static int FRAGMENT_ETKIN_EKRAN;
    private static int TIKLAMA_OLAYI;
    private static String xmlParcaID = "0";//içinde olunan parçanın id si
    private static int xmlEnBuyukID;//eklenen kategori ve kayıtlara id verebilmek için
    private static View activityRootView;
    private static Document document;
    private static Document documentAyar;
    private static int actionBarBoy;
    public static Context cnt;
    private static List<String> listeRenkler;
    private static String kategoriBasligi = "";//kayıt ekranındayken ekran dönerse baslik siliniyor. tekrar yazırabilmek için


    public static List<String> getListeRenkler()
    {
        return listeRenkler;
    }

    //xml i okumank için Document nesnesi olusturur
    public static Document xmlDocumentNesnesiOlustur(String xmlDosyaYolu, MainActivity ma)
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
            ekranaHataYazdir("28", "document olusamadı : " + e.getMessage() + " ,xml : " + xmlDosyaYolu);
            return null;
        }
        catch (FileNotFoundException e)
        {
            ekranaHataYazdir("29", "document olusamadı : " + e.getMessage() + " ,xml : " + xmlDosyaYolu);
            return null;
        }
        catch (IOException e)
        {
            ekranaHataYazdir("30", "document olusamadı : " + e.getMessage() + " ,xml : " + xmlDosyaYolu);
            return null;
        }
        catch (SAXException e)
        {
            ekranaHataYazdir("15", "document olusamadı : " + e.getMessage() + " ,xml : " + xmlDosyaYolu);
            return null;
        }
    }

    //elemanların en uzunluğunu hesaplar
    public static void elemanEniniHesapla()
    {
        int ekranEnUzunluğu = mResources.getDisplayMetrics().widthPixels;
        float fazlalık = (mResources.getDimension(R.dimen.activity_horizontal_margin) * 2);
        fazlalık = fazlalık + dpGetir(3) * ((Integer.valueOf(DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI)) - 1);
        elemanEnUzunluğu = (int) ((ekranEnUzunluğu - fazlalık) / Integer.valueOf(DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI));
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                xmlDosyaYolu = uygulamaKlasoru + "/" + "new.xml";
                xmlAyarDosyaYolu = uygulamaKlasoru + "/" + "opt.xml";

                if (xmlDosyasiKontrolEt() && xmlAyarDosyasiKontrolEt())//xml dosyaları ile ilgili hata yoksa devam etsin, varsa uygulamayı sonlandırsın
                {
                    if (savedInstanceState == null)
                    {
                        CustomRelativeLayout crl = new CustomRelativeLayout(cnt, kategoriRenkBilgisiniGetir("0"), 0);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                        getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, 0, crl), Sabit.FRAGMENT_TAG).commit();
                    }

                    if (DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN.equals("0"))
                    {
                        actionBarArkaPlanDegistir(kategoriRenkBilgisiniGetir("0"));
                    }
                    else
                    {
                        actionBarArkaPlanDegistir(DEGER_AYAR_ACTIONBAR_RENGI);
                    }
                    getActionBar().setDisplayUseLogoEnabled(false);
                    getActionBar().setDisplayShowHomeEnabled(false);

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
                    listeRenkler.add(Sabit.RENK_TURKUAZ);
                    listeRenkler.add(Sabit.RENK_TURUNCU);
                    listeRenkler.add(Sabit.RENK_YESIL);

                    geriSimgesiniEkle();
                }
                else
                {
                    ekranaHataYazdir("45", "xml dosylarıyla ilgili hata oluştu, uygulama kapatıldı");
                    finish();
                }
            }
            else
            {
                ekranaHataYazdir("1", "uygulama klasoru yok : " + uygulamaKlasoru);
                finish();
            }
        }
        else
        {
            ekranaHataYazdir("37", "klasörlerle ilgili hata oluştu, uygulama kapatıldı");
            finish();
        }
    }

    //actionbarın solundaki geri oku
    public void geriSimgesiniEkle(String renk)
    {
        final Drawable upArrow = getResources().getDrawable(R.drawable.geri);
        upArrow.setColorFilter(Color.parseColor(renk), PorterDuff.Mode.SRC_ATOP);
        getActionBar().setHomeAsUpIndicator(upArrow);
    }

    //actionbarın solundaki geri oku
    public void geriSimgesiniEkle()
    {
        final Drawable upArrow = getResources().getDrawable(R.drawable.geri);
        upArrow.setColorFilter(Color.parseColor(DEGER_AYAR_SIMGE_RENGI), PorterDuff.Mode.SRC_ATOP);
        getActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void actionBarBoyUzunlugunuGetir()
    {
        TypedValue tv = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        actionBarBoy = actionBarHeight;
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
                ekranaHataYazdir("2", "uygulama klasöru oluşturulurken hata oluştu : " + uygKlasoru);
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
                ekranaHataYazdir("3", "xml yedek klasörü oluşturulurken hata oluştu : " + xmlYedekKlasoru);
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
            document = xmlDocumentNesnesiOlustur(xmlDosyaYolu, this);
            if (document == null)
            {
                ekranaHataYazdir("4", "xml document oluşamadı");
                return false;
            }
            else
            {
                xmlEnBuyukID = enBuyukIDyiBul();
                if (xmlEnBuyukID == -1)
                {
                    ekranaHataYazdir("5", "xml okunamadı");
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
            document = xmlDocumentNesnesiOlustur(xmlDosyaYolu, this);
            return true;
        }
    }

    //uygulama baslarken sistemde ayar dosyası var mı diye bakar. varsa ayarları alır. yoksa öntanımlı ayarla ile dosyayı olusturur
    public boolean xmlAyarDosyasiKontrolEt()
    {
        if (new File(xmlAyarDosyaYolu).exists())//xml ayar dosyası var mı
        {
            documentAyar = xmlDocumentNesnesiOlustur(xmlAyarDosyaYolu, this);
            if (documentAyar == null)
            {
                ekranaHataYazdir("6", "xml document ayar oluşamadı");
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
            documentAyar = xmlDocumentNesnesiOlustur(xmlAyarDosyaYolu, this);
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
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_YAZI_RENGI))
        {
            Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_YAZI_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_YAZI_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
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
                ekranaHataYazdir("31", "xml documenti dosyaya yazarken hata oluştu : " + e.getMessage());
            }
            catch (TransformerException e)
            {
                ekranaHataYazdir("32", "xml documenti dosyaya yazarken hata oluştu : " + e.getMessage());
            }
            catch (IOException e)
            {
                ekranaHataYazdir("33", "xml documenti dosyaya yazarken hata oluştu : " + e.getMessage());
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

                case Sabit.AYAR_ID_YAZI_RENGI:
                    DEGER_AYAR_YAZI_RENGI = ayarDeger;
                    break;

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
                    ekranaHataYazdir("7", "hatalı ayar id, ayar id : " + ayarID);
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
            out.write("<?xml version=\"1.0\"?><root>" +
                    "<parca id=\"0\" durum=\"0\">" +
                    "<baslik/>" +
                    "<renk>" + Sabit.KATEGORI_ONTANIMLI_RENK + "</renk>" +
                    "<yazilar/>" +
                    "<altparca/>" +
                    "</parca>" +
                    "</root>");
            out.close();
        }
        catch (IOException e)
        {
            ekranaHataYazdir("8", "boş xml dosyası oluşturulurken hata oluştu : " + e.getMessage() + ", dosya : " + xmlDosyaYolu);
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
                    "<ayar id=\"" + Sabit.AYAR_ID_YAZI_RENGI + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_YAZI_RENGI + "</ayar>" +
                    "</root>");
            out.close();
        }
        catch (IOException e)
        {
            ekranaHataYazdir("9", "xml ayar dosyası oluşturulurken hata oluştu : " + e.getMessage() + ", dosya : " + xmlAyarDosyaYolu);
        }
    }

    public static void ekranaHataYazdir(String id, String hata)
    {
        Toast.makeText(cnt, "hata[" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
    }

    //actionBar'ın arkaplan rengini degiştirir
    public void actionBarArkaPlanDegistir(String renk)
    {
        ColorDrawable actionBarArkaPlan = new ColorDrawable(Color.parseColor(renk));
        getActionBar().setBackgroundDrawable(actionBarArkaPlan);
    }

    //kategorinin renk degerinin dondurur
    public static String kategoriRenkBilgisiniGetir(String kategoriID)
    {
        Element element = document.getElementById(kategoriID);

        Node nodeRenk = etiketiGetir(element, Sabit.XML_RENK);
        if (nodeRenk == null)
        {
            ekranaHataYazdir("39", "etiket getirilemedi");
            return Sabit.KATEGORI_ONTANIMLI_RENK;
        }
        else
        {
            return nodeRenk.getTextContent();
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
        private RelativeLayout anaLayout;//viewların içine yerleşeceği ana layout
        private MenuInflater inflaterActionBar;
        private Menu menuActionBar;
        private EditText etDegisecek;//kayit degiştirmeye tıklandığı zaman olusan edittext
        private Activity fAct;
        private static MainActivity ma;//MainActivity fonksiyonlarını calistirabilmek için
        private AlertDialog alertRenk;//renkleri soran alertDialog. renk dugmesine dokunuca alertDialog^u kapatabilmek için
        private static int elemanTuru;
        private static View fragmentRootView;//fragment icindeki layoutlara ulasabilmek için fragment view ı

        public PlaceholderFragment()
        {
        }

        public static PlaceholderFragment newInstanceKategori(int secim, int kategoriID, CustomRelativeLayout crl)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            //Bundle args = new Bundle();
            FRAGMENT_ETKIN_EKRAN = secim;
            //fragment.setArguments(args);
            xmlParcaID = String.valueOf(kategoriID);
            fragment.setHasOptionsMenu(true);

            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;
            seciliCRL = crl;

            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();
            listSeciliCRL = new ArrayList<>();
            elemanTuru = Sabit.ELEMAN_TUR_KATEGORI;

            return fragment;
        }

        public static PlaceholderFragment newInstanceKayit(int secim, String yazi, CustomRelativeLayout crl)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(Sabit.FRAGMENT_YAZI, yazi);
            FRAGMENT_ETKIN_EKRAN = secim;
            fragment.setArguments(args);
            fragment.setHasOptionsMenu(true);

            ACTIONBAR_TUR = Sabit.ACTIONBAR_DEGISTIR;
            seciliCRL = crl;

            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;

            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();
            listSeciliCRL = new ArrayList<>();
            elemanTuru = Sabit.ELEMAN_TUR_KAYIT;

            return fragment;
        }

        public static PlaceholderFragment newInstanceYedek(int secim)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            //Bundle args = new Bundle();
            //args.putInt(FRAGMENT_SECIM, secim);
            FRAGMENT_ETKIN_EKRAN = secim;
            //fragment.setArguments(args);
            fragment.setHasOptionsMenu(true);

            TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;

            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();
            listSeciliCRL = new ArrayList<>();
            elemanTuru = Sabit.ELEMAN_TUR_YEDEK;

            return fragment;
        }

        public static PlaceholderFragment newInstanceAyarlar(int secim)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            //args.putInt(FRAGMENT_SECIM, secim);
            FRAGMENT_ETKIN_EKRAN = secim;
            fragment.setArguments(args);
            fragment.setHasOptionsMenu(true);

            //TIKLAMA_OLAYI = Sabit.OLAY_ICINE_GIR;

            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();
            listSeciliCRL = new ArrayList<>();
            elemanTuru = Sabit.ELEMAN_TUR_AYARLAR;

            return fragment;
        }

        //kaydin renk degerinin dondurur
        public static String kayitRenkBilgisiniGetir(String kayitID)
        {
            Element element = document.getElementById(kayitID);

            Node nodeRenk = etiketiGetir(element, Sabit.XML_RENK);
            if (nodeRenk == null)
            {
                ekranaHataYazdir("48", "etiket getirilemedi");
                return Sabit.KAYIT_ONTANIMLI_RENK;
            }
            else
            {
                return nodeRenk.getTextContent();
            }
        }

        public CustomRelativeLayout findCRLbyID(int id)
        {
            CustomRelativeLayout crl = (CustomRelativeLayout) anaLayout.findViewById(id);
            return crl;
        }

        //kutuların ekranda yerlesimini ayarlamak için yerlesim nesnesi
        public static void yeniYerlesimOlustur()
        {
            Yerlesim ylsm = new Yerlesim(Integer.valueOf(DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI));
            globalYerlesim = ylsm;
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

                        String kayitYazi = "";
                        String kayitRenk = Sabit.KAYIT_ONTANIMLI_RENK;
                        Node nodeKayitYazi = etiketiGetir((Element) nodeKayit, Sabit.XML_YAZI);
                        if (nodeKayitYazi == null)
                        {
                            ekranaHataYazdir("40", "etiket getirilemedi");
                        }
                        else
                        {
                            kayitYazi = nodeKayitYazi.getTextContent();
                        }
                        Node nodeRenk = etiketiGetir((Element) nodeKayit, Sabit.XML_RENK);
                        if (nodeRenk == null)
                        {
                            ekranaHataYazdir("47", "etiket getirilemedi");
                        }
                        else
                        {
                            kayitRenk = nodeRenk.getTextContent();
                        }
                        String kayitDurum = nodeKayit.getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue();
                        String kayitID = nodeKayit.getAttributes().getNamedItem(Sabit.XML_ID).getNodeValue();

                        kayitlariAnaEkranaEkle(kayitYazi, Integer.parseInt(kayitID), kayitDurum, kayitRenk, globalYerlesim);
                    }
                }
                else if (nodeList.item(i).getNodeName().equals(Sabit.XML_ALTPARCA))
                {
                    Node nodeAltParca = nodeList.item(i);
                    NodeList nodeListParcalar = nodeAltParca.getChildNodes();

                    for (int j = 0; j < nodeListParcalar.getLength(); j++)
                    {
                        Node nodeParca = nodeListParcalar.item(j);
                        Node nodeBaslik = etiketiGetir((Element) nodeParca, Sabit.XML_BASLIK);

                        String kategoriBaslik = "";
                        String kategoriRenk = Sabit.KATEGORI_ONTANIMLI_RENK;
                        if (nodeBaslik == null)
                        {
                            ekranaHataYazdir("41", "etiket getirilemedi");
                        }
                        else
                        {
                            kategoriBaslik = nodeBaslik.getTextContent();
                        }

                        Node nodeRenk = etiketiGetir((Element) nodeParca, Sabit.XML_RENK);
                        if (nodeRenk == null)
                        {
                            ekranaHataYazdir("42", "etiket getirilemedi");
                        }
                        else
                        {
                            kategoriRenk = nodeRenk.getTextContent();
                        }

                        String kategoriDurum = nodeParca.getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue();
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
                    ekranaHataYazdir("34", "hatalı seçim türü : " + eylem);
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
        public void kayitlariAnaEkranaEkle(final String yazi, final int eklenenID, final String durum, String renk, Yerlesim ylsm)
        {
            final CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), yazi, Sabit.ELEMAN_TUR_KAYIT, eklenenID, durum, renk, this, ylsm);

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

                    return true;
                }
            });
            crl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (TIKLAMA_OLAYI == Sabit.OLAY_ICINE_GIR)
                    {
                        getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKayit(Sabit.FRAGMENT_KAYIT_EKRANI, yazi, crl)).addToBackStack(null).commit();
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
            });
            anaLayout.addView(crl);
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

            kategoriBasligi = baslik;

            return baslik;
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        public void kategoriyiAnaEkranaEkle(final String baslik, final int kategoriID, final String durum, String renk, Yerlesim ylsm)
        {
            final CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), baslik, Sabit.ELEMAN_TUR_KATEGORI, kategoriID, durum, renk, this, ylsm);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran

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
                    if (TIKLAMA_OLAYI == Sabit.OLAY_ICINE_GIR)
                    {
                        getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, kategoriID, crl), Sabit.FRAGMENT_TAG).commit();
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
            });
            crl.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
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

                    return true;
                }
            });
            anaLayout.addView(crl);
        }

        //kategori secildigi zaman kategori basligini duzenle simgesini yanına alır
        public void basligiDuzenleninYaninaAl()
        {
            for (int i = 0; i < anaLayout.getChildCount(); i++)
            {
                CustomRelativeLayout c = (CustomRelativeLayout) anaLayout.getChildAt(i);
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
            for (int i = 0; i < anaLayout.getChildCount(); i++)
            {
                CustomRelativeLayout c = (CustomRelativeLayout) anaLayout.getChildAt(i);
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
            for (int i = 0; i < anaLayout.getChildCount(); i++)
            {
                CustomRelativeLayout crl = (CustomRelativeLayout) anaLayout.getChildAt(i);
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
                        ekranaHataYazdir("10", "hatalı xml document turu : " + tur);
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
                ekranaHataYazdir("11", "xml documenti dosyaya yazarken hata oluştu : " + e.getMessage());
            }
            catch (TransformerException e)
            {
                ekranaHataYazdir("12", "xml documenti dosyaya yazarken hata oluştu : " + e.getMessage());
            }
            catch (IOException e)
            {
                ekranaHataYazdir("13", "xml documenti dosyaya yazarken hata oluştu : " + e.getMessage());
            }
        }

        //başta oluşturulan xmle yeni eklenen kategoriyi ekler ve en buyuk xml idsini döndürür
        public int xmlDosyasinaKategoriEkle(String baslik)
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
                    yeniNodeParca.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);//parca ya id özelliği ekleniyor
                    nodeAltparca.appendChild(yeniNodeParca);//altparca etiketine parca ekleniyor

                    Node nodeParca = document.getElementById(String.valueOf(xmlEnBuyukID));//xmlid id sine sahip parca nın içine giriliyor. az önce oluşturulan parca
                    Element yeniNodeBaslik = document.createElement(Sabit.XML_BASLIK);//baslik etiketi oluşturuluyor
                    yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri giriliyor
                    Element yeniNodeRenk = document.createElement(Sabit.XML_RENK);//renk etiketi oluşturuluyor
                    yeniNodeRenk.setTextContent(Sabit.KATEGORI_ONTANIMLI_RENK);//renk etiketine renk değeri giriliyor
                    Element yeniNodeYazi = document.createElement(Sabit.XML_YAZILAR);//yazi etiketi oluşturuluyor
                    Element yeniNodeAltparca = document.createElement(Sabit.XML_ALTPARCA);//altparca etiketi oluşturuluyor
                    nodeParca.appendChild(yeniNodeBaslik);//parca etiketine baslik etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeRenk);//parca etiketine renk etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeYazi);//parca etiketine yazi etiketi ekleniyor
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
                ma.actionBarArkaPlanDegistir(kategoriRenkBilgisiniGetir(xmlParcaID));
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

        //ana ekrana ve xml'e kategori ekler(parca)
        public void kategoriKaydet()
        {
            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Kategori Adı", "İptal", "Tamam", "", Sabit.ALERTDIALOG_EDITTEXT);
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
                        Toast.makeText(getActivity(), "Kategori adı boş olamaz", Toast.LENGTH_LONG).show();
                    }
                    else//anaLayout'a yeni alan ekliyor
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
                {
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;
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
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran

                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                case Sabit.FRAGMENT_YEDEK_EKRANI:
                {
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                case Sabit.FRAGMENT_AYAR_EKRANI:
                {
                    CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir(xmlParcaID), Integer.parseInt(xmlParcaID));//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID), crl), Sabit.FRAGMENT_TAG).commit();
                    ACTIONBAR_TUR = Sabit.ACTIONBAR_EKLE;

                    break;
                }
                default:
                    ekranaHataYazdir("14", "hatalı fragment id, fragment id : " + FRAGMENT_ETKIN_EKRAN);
            }
        }

        //başta oluşturulan xmle yeni eklenen kaydı ekler ve en buyuk xml idsini döndürür
        public int xmlDosyasinaKayitEkle(String baslik)
        {
            xmlEnBuyukID++;
            kategoriDurumunuGuncelle(xmlParcaID, Sabit.DURUM_YENI);

            Node nodeMevcutParca = document.getElementById(String.valueOf(xmlParcaID));//içinde bulunulan parcaya giriyor
            NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
            for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
            {
                if (nodeParcaCocuklari.item(i).getNodeName().equals(Sabit.XML_YAZILAR))//parcanın içindeki yazilar etiketine ulaşılıyor
                {
                    int eklenenID = xmlEnBuyukID;
                    Element yeniNodeKayit = document.createElement(Sabit.XML_KAYIT);//kayıt etiketi olusturuyor
                    yeniNodeKayit.setAttribute(Sabit.XML_ID, String.valueOf(xmlEnBuyukID));//parca ya id özelliği ekleniyor
                    yeniNodeKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);//parca ya id özelliği ekleniyor

                    StringBuilder str = new StringBuilder(baslik);//alt satıra geçmeyi anlayabilmek için \n <br> ile değiştiriliyor
                    int sayac = 0;
                    for (int j = 0; j < baslik.length(); j++)//<br> eklendiği zaman stringin boyu 4 uzuyor onun için sayac tutuyoruz
                    {
                        if (baslik.charAt(j) == '\n')
                        {
                            str.insert(j + sayac, "<br>");
                            sayac = sayac + 4;
                        }
                    }
                    //yeniNodeKayit.setTextContent(str.toString());
                    Element yeniNodeBaslik = document.createElement(Sabit.XML_YAZI);//yazi etiketi oluşturuluyor
                    yeniNodeBaslik.setTextContent(str.toString());//yazi etiketine yazi değeri giriliyor
                    yeniNodeKayit.appendChild(yeniNodeBaslik);

                    Element yeniNodeRenk = document.createElement(Sabit.XML_RENK);//renk etiketi oluşturuluyor
                    yeniNodeRenk.setTextContent(Sabit.KAYIT_ONTANIMLI_RENK);//renk etiketine renk değeri giriliyor
                    yeniNodeKayit.appendChild(yeniNodeRenk);

                    nodeParcaCocuklari.item(i).appendChild(yeniNodeKayit);//yazilar etiketinin içine kayit etiketini ekliyor

                    break;
                }
            }
            documentToFile(Sabit.DOCUMENT_ASIL);

            return xmlEnBuyukID;
        }

        //ana ekrana ve xml'e kayıt ekler
        public void yaziyiKaydet()
        {
            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Kayıt", "İptal", "Tamam", "", Sabit.ALERTDIALOG_EDITTEXT);
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
                        Toast.makeText(getActivity(), "Kayıt boş olamaz", Toast.LENGTH_LONG).show();
                    }
                    else//anaLayout'a yeni alan ekliyor
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
        }

        //sil tusuna basıldığı zaman secili elemanları siler
        public void seciliElemanlariSil()
        {
            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Onay", "İptal", "Tamam", "Seçilen kayıtlar silinsin mi ?", Sabit.ALERTDIALOG_TEXTVIEW);
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

        public void kayitSilDiyaloguOlustur()
        {
            if (seciliCRL != null)
            {
                final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Onay", "İptal", "Tamam", "Kayıt silinsin mi ?", Sabit.ALERTDIALOG_TEXTVIEW);
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

                klavyeKapat(etDegisecek.getWindowToken());
            }
            else
            {
                ekranaHataYazdir("16", "secili layout bos");
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
                //anaLayout.removeView(crl);
            }
            anaLayout.removeAllViews();
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
                            kategoriCocuklarDurum(element, Sabit.DURUM_YENI);
                            crl.arkaplanKategori();
                            break;

                        default:
                            ekranaHataYazdir("17", "hatalı kayıt türü : " + crl.getCrlTur());
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
                            kategoriCocuklarDurum(element, Sabit.DURUM_TAMAMLANDI);
                            crl.arkaplanKategori();
                            break;

                        case Sabit.ELEMAN_TUR_KAYIT:
                            crl.arkaplanKayit();
                            break;

                        default:
                            ekranaHataYazdir("18", "hatalı kayıt türü : " + crl.getCrlTur());
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

        public void kategoriCocuklarDurum(Element elementKategori, String durum)
        {
            NodeList nodeList = elementKategori.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeName().equals(Sabit.XML_YAZILAR))
                {
                    NodeList nodeListKayitlar = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListKayitlar.getLength(); j++)
                    {
                        nodeListKayitlar.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(durum);
                    }
                }
                else if (nodeList.item(i).getNodeName().equals(Sabit.XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        nodeListAltParca.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(durum);
                        kategoriCocuklarDurum((Element) nodeListAltParca.item(j), durum);
                    }
                }
            }
        }

        public void kategoriDurumunuGuncelle(String id, String durum)
        {
            Element elementKayit = document.getElementById(id);
            elementKayit.setAttribute(Sabit.XML_DURUM, durum);
        }

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
                        if (nodeListKayit.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue().equals(Sabit.DURUM_YENI))
                        {
                            sonucYazilar = false;
                            break;
                        }
                    }
                }
                else if (nodeListParca.item(i).getNodeName().equals(Sabit.XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeListParca.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        if (nodeListAltParca.item(j).getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue().equals(Sabit.DURUM_YENI))
                        {
                            sonucAltParcalar = false;
                            break;
                        }
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
                    nodeParca.getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(Sabit.DURUM_TAMAMLANDI);
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
                nodeParca.getAttributes().getNamedItem(Sabit.XML_DURUM).setNodeValue(Sabit.DURUM_YENI);
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
            //seciliCRL.setDurum(Sabit.DURUM_YENI);
            Element elementKayit = document.getElementById(idd);
            elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);
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
                elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_YENI);
                documentToFile(Sabit.DOCUMENT_ASIL);

                return elementKayit;
            }
            else
            {
                return null;
            }
        }

        public void kayitDegistir()
        {
            if (seciliCRL != null)
            {
                Element elementKayit = document.getElementById(String.valueOf(seciliCRL.getId()));
                //elementKayit.setTextContent(etDegisecek.getText().toString());
                Node NodeYazi = etiketiGetir(elementKayit, Sabit.XML_YAZI);
                if (NodeYazi == null)
                {
                    ekranaHataYazdir("43", "etiket getirilemedi");
                }
                else
                {
                    NodeYazi.setTextContent(etDegisecek.getText().toString());
                }

                documentToFile(Sabit.DOCUMENT_ASIL);
                ustSeviyeyiGetir();
                klavyeKapat(null);
            }
            else
            {
                ekranaHataYazdir("19", "seçili layout boş");
            }
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
            //seciliCRL.setDurum(Sabit.DURUM_TAMAMLANDI);
            Element elementKayit = document.getElementById(idd);
            elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_TAMAMLANDI);
            documentToFile(Sabit.DOCUMENT_ASIL);

            return elementKayit;
        }

        //secilen kaydin durumunu tamamlandı olarak değiştirir
        public Element kayitDurumunuTamamlandiYap()
        {
            if (seciliCRL != null)
            {
                seciliCRL.setDurum(Sabit.DURUM_TAMAMLANDI);
                Element elementKayit = document.getElementById(String.valueOf(seciliCRL.getId()));
                elementKayit.setAttribute(Sabit.XML_DURUM, Sabit.DURUM_TAMAMLANDI);
                documentToFile(Sabit.DOCUMENT_ASIL);

                return elementKayit;
            }
            else
            {
                ekranaHataYazdir("35", "seçili layout boş");
                return null;
            }
        }

        //duruma göre actionbarDegistirdeki simgeleri gösterir, gizler
        public void actionBarDegistirSimgeDurumu(String durum)
        {
            switch (durum)
            {
                case Sabit.DURUM_TAMAMLANDI:
                    menuActionBar.findItem(Sabit.ACTION_DEGISTIR_TAMAM).setVisible(false);
                    menuActionBar.findItem(Sabit.ACTION_DEGISTIR_YENI).setVisible(true);
                    break;

                case Sabit.DURUM_YENI:
                    menuActionBar.findItem(Sabit.ACTION_DEGISTIR_TAMAM).setVisible(true);
                    menuActionBar.findItem(Sabit.ACTION_DEGISTIR_YENI).setVisible(false);
                    break;

                default:
                    ekranaHataYazdir("36", "hatalı durum türü : " + durum);
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
            if (activityRootView.getRootView().getHeight() / 2 > activityRootView.getHeight())
            {
                return true;
            }
            else
            {
                return false;
            }
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
                ekranaHataYazdir("37", "yedek dosyasıkpyalanırken hata oluştu : " + e.getMessage() + ", kaynak : " + kaynak + ", hedef : " + hedef);
            }
            catch (IOException e)
            {
                ekranaHataYazdir("38", "yedek dosyasıkpyalanırken hata oluştu : " + e.getMessage() + ", kaynak : " + kaynak + ", hedef : " + hedef);
            }
        }

        //xml dosyasını yedek klasörüne kopyalar
        public void xmlYedekle()
        {
            String zaman = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Onay", "İptal", "Tamam", zaman, Sabit.ALERTDIALOG_EDITTEXT);
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
                        Toast.makeText(getActivity(), "Yedek adı boş olamaz", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        klavyeKapat();

                        final String hedefDosya = xmlYedekKlasorYolu + "/" + builder.getAlertET().getText() + ".xml";
                        File fileHedef = new File(hedefDosya);
                        if (fileHedef.exists())
                        {
                            CustomAlertDialogBuilder builder2 = new CustomAlertDialogBuilder(getActivity(), "Onay", "İptal", "Tamam", "Bu isme sahip dosya var. Üzerine yazılsın mı ?", Sabit.ALERTDIALOG_TEXTVIEW);
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
                if (uzanti.equals(".xml"))//yedek dosyalarının uzantıları xml olmalı
                {
                    String b = file[i].getName().substring(0, file[i].getName().length() - 4);
                    yedekler.add(b);
                }
                else
                {
                    ekranaHataYazdir("20", "yedek dosyasının uzantısı hatalı : " + uzanti);
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
            builder.setTitle("Yedek Adı");
            builder.setView(alertLL);
            builder.setNegativeButton("İptal", null);
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
                    final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Onay", "İptal", "Tamam", vv.getText() + " Yedek dosyası yüklensin mi ?", Sabit.ALERTDIALOG_TEXTVIEW);
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
                                dosyaKopyala(xmlYedekKlasorYolu + "/" + vv.getText() + ".xml", xmlDosyaYolu);

                                document = xmlDocumentNesnesiOlustur(xmlDosyaYolu, (MainActivity) fAct);
                                CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), kategoriRenkBilgisiniGetir("0"), 0);
                                getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(Sabit.FRAGMENT_KATEGORI_EKRANI, 0, crl), Sabit.FRAGMENT_TAG).commit();

                                /*
                                anaLayout.removeAllViews();
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
                                ekranaHataYazdir("21", "xml dosyası silinirken hata oluştu : " + xmlDosyasi);
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
            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceYedek(Sabit.FRAGMENT_YEDEK_EKRANI), Sabit.FRAGMENT_TAG).commit();
        }

        public void seciliYedekDosyalariniSil()
        {
            if (!listSeciliYedek.isEmpty())
            {
                String soru = "Yedek dosyaları silinsin mi ?\n";
                for (int i = 0; i < listSeciliYedek.size(); i++)
                {
                    soru = soru + "\n" + listSeciliYedek.get(i).getIsim();
                }

                final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Onay", "İptal", "Tamam", soru, Sabit.ALERTDIALOG_TEXTVIEW);
                final AlertDialog alert = builder.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        for (int i = 0; i < listSeciliYedek.size(); i++)
                        {
                            String dosya = xmlYedekKlasorYolu + "/" + listSeciliYedek.get(i).getIsim() + ".xml";
                            File yedekDosya = new File(dosya);

                            if (!yedekDosya.delete())
                            {
                                ekranaHataYazdir("22", "yedek xml dosyasi silinirken hata oluştu : " + yedekDosya);
                            }
                            anaLayout.removeView(listSeciliYedek.get(i));
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
            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceAyarlar(Sabit.FRAGMENT_AYAR_EKRANI)).addToBackStack(null).commit();
        }

        //ayar degerlerini öntanımlı değerler yapar
        public void ayarlariSifirla()
        {
            for (int i = 0; i < anaLayout.getChildCount(); i++)
            {
                AyarlarRelativeLayout arl = (AyarlarRelativeLayout) anaLayout.getChildAt(i);
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
                        ekranaHataYazdir("23", "hatalı ayar id, ayar id : " + arl.getAyarID());
                }
            }
        }

        //ayar ekranina ayar metinlerini ekler
        public void ayarlariAyarEkraninaEkle()
        {
            AyarlarRelativeLayout arl1 = new AyarlarRelativeLayout(getActivity(), "bir satırda gösterilecek eleman sayısı", DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI, Sabit.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI, Sabit.SECENEK_EDITTEXT);
            anaLayout.addView(arl1);

            AyarlarRelativeLayout arl2 = new AyarlarRelativeLayout(getActivity(), "satır boy uzunluğu sabit olsun", DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN, Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN, Sabit.SECENEK_CHECKBOX);
            anaLayout.addView(arl2);

            final AyarlarRelativeLayout arl3 = new AyarlarRelativeLayout(getActivity(), "ekranda gösterilecek satir sayisi", DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI, Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI, Sabit.SECENEK_EDITTEXT);
            anaLayout.addView(arl3);
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

            AyarlarRelativeLayout arl4 = new AyarlarRelativeLayout(getActivity(), "simge rengi", DEGER_AYAR_SIMGE_RENGI, Sabit.AYAR_ID_SIMGE_RENGI, Sabit.SECENEK_BUTTON);
            anaLayout.addView(arl4);

            AyarlarRelativeLayout arl5 = new AyarlarRelativeLayout(getActivity(), "yazı rengi", DEGER_AYAR_YAZI_RENGI, Sabit.AYAR_ID_YAZI_RENGI, Sabit.SECENEK_BUTTON);
            anaLayout.addView(arl5);

            AyarlarRelativeLayout arl6 = new AyarlarRelativeLayout(getActivity(), "çerçeve gözüksün", DEGER_AYAR_CERCEVE_GOZUKSUN, Sabit.AYAR_ID_CERCEVE_GOZUKSUN, Sabit.SECENEK_CHECKBOX);
            anaLayout.addView(arl6);

            final AyarlarRelativeLayout arl7 = new AyarlarRelativeLayout(getActivity(), "cerceve rengi", DEGER_AYAR_CERCEVE_RENGI, Sabit.AYAR_ID_CERCEVE_RENGI, Sabit.SECENEK_BUTTON);
            anaLayout.addView(arl7);
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

            AyarlarRelativeLayout arl8 = new AyarlarRelativeLayout(getActivity(), "ekran arkaplan rengi sabit olsun", DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN, Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN, Sabit.SECENEK_CHECKBOX);
            anaLayout.addView(arl8);

            final AyarlarRelativeLayout arl9 = new AyarlarRelativeLayout(getActivity(), "ekran arkaplan rengi", DEGER_AYAR_ARKAPLAN_RENGI, Sabit.AYAR_ID_ARKAPLAN_RENGI, Sabit.SECENEK_BUTTON);
            anaLayout.addView(arl9);
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

            AyarlarRelativeLayout arl10 = new AyarlarRelativeLayout(getActivity(), "actionbar arkaplan rengi sabit olsun", DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN, Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN, Sabit.SECENEK_CHECKBOX);
            anaLayout.addView(arl10);

            final AyarlarRelativeLayout arl11 = new AyarlarRelativeLayout(getActivity(), "actionbar arkaplan rengi", DEGER_AYAR_ACTIONBAR_RENGI, Sabit.AYAR_ID_ACTIONBAR_RENGI, Sabit.SECENEK_BUTTON);
            anaLayout.addView(arl11);
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
                    AyarlarRelativeLayout arl1 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
                    String yeniDeger1 = ((EditText) arl1.getViewSecenek()).getText().toString();
                    return yeniDeger1;

                case Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN:
                    AyarlarRelativeLayout arl2 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
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
                    AyarlarRelativeLayout arl3 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
                    String yeniDeger3 = ((EditText) arl3.getViewSecenek()).getText().toString();
                    return yeniDeger3;

                case Sabit.AYAR_ID_SIMGE_RENGI:
                    AyarlarRelativeLayout arl4 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
                    return arl4.getSecilenRenk();

                case Sabit.AYAR_ID_YAZI_RENGI:
                    AyarlarRelativeLayout arl5 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
                    return arl5.getSecilenRenk();

                case Sabit.AYAR_ID_CERCEVE_GOZUKSUN:
                    AyarlarRelativeLayout arl6 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
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
                    AyarlarRelativeLayout arl7 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
                    return arl7.getSecilenRenk();

                case Sabit.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN:
                    AyarlarRelativeLayout arl8 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
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
                    AyarlarRelativeLayout arl9 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
                    return arl9.getSecilenRenk();

                case Sabit.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN:
                    AyarlarRelativeLayout arl10 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
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
                    AyarlarRelativeLayout arl11 = (AyarlarRelativeLayout) anaLayout.findViewById(ayarID);
                    return arl11.getSecilenRenk();

                default:
                    ekranaHataYazdir("50", "hatalı ayar id, ayar id : " + ayarID);
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
                                Toast.makeText(getActivity(), "satır başına kayıt sayısı 1 den küçük olamaz", Toast.LENGTH_SHORT).show();
                                sonuc = sonuc & false;
                            }
                            break;

                        case Sabit.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN:
                            //checkbox olduğu için kontrole gerek yok
                            break;

                        case Sabit.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI:
                            if (Integer.valueOf(yeniDeger) < 1)
                            {
                                Toast.makeText(getActivity(), "ekranda gösterilecek kayıt sayısı 1 den küçük olamaz", Toast.LENGTH_SHORT).show();
                                sonuc = sonuc & false;
                            }
                            break;

                        case Sabit.AYAR_ID_SIMGE_RENGI:
                            //önerilerden secildiği icin kontrol edecek birşey yok
                            break;

                        case Sabit.AYAR_ID_YAZI_RENGI:
                            //önerilerden secildiği icin kontrol edecek birşey yok
                            break;

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
                            ekranaHataYazdir("24", "hatalı ayar id, ayar id : " + ayarID);
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

                    case Sabit.AYAR_ID_YAZI_RENGI:
                        nodeAyar.setTextContent(yeniDeger);//xml i degistiriyor
                        DEGER_AYAR_YAZI_RENGI = yeniDeger;
                        break;

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
                        ekranaHataYazdir("25", "hatalı ayar id, ayar id : " + ayarID);
                }
            }
            documentToFile(Sabit.DOCUMENT_AYAR);
        }

        //actionBar daki kategori basligini yazar
        public void kategoriBasliginiYaz()
        {
            if (seciliCRL.getId() == 0)
            {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                getActivity().getActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>/</font>"));
            }
            else
            {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                getActivity().getActionBar().setTitle(Html.fromHtml("<font color='" + DEGER_AYAR_SIMGE_RENGI + "'>" + kategoriYolunuGetir(String.valueOf(seciliCRL.getId())) + "</font>"));
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
            //LinearLayout alertLL = new RenkDugmeleri(getActivity(), seciliCRL.getRenk(), listeRenkler, this);
            RenkDugmeleri alertLL = new RenkDugmeleri(getActivity(), seciliCRL.getRenk(), listeRenkler);
            alertLL.setCagiranYer(alertLL.CAGIRAN_YER_FRAGMENT, this);
            CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(getActivity(), "Renk", "İptal", alertLL, Sabit.ALERTDIALOG_CUSTOM_VIEW);
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
                    kategorininRenkBilgisiniGuncelle(secilenRenk);
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
                    break;

                case Sabit.ELEMAN_TUR_KAYIT:
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
                    break;

                /* ayarlar AyarlarRelativeLayout içinde yakalanıyor
                case Sabit.ELEMAN_TUR_AYARLAR:
                    break;
                case Sabit.ELEMAN_TUR_YEDEK:
                    break;
                */

                default:
                    ekranaHataYazdir("49", "hatalı eleman türü, tür : " + getElemanTuru());
            }
        }

        public void kategorininRenkBilgisiniGuncelle(String secilenRenk)
        {
            alertRenk.dismiss();
            Element element = document.getElementById(String.valueOf(xmlParcaID));
            //Node nodeRenk = element.getChildNodes().item(1);

            Node nodeRenk = etiketiGetir(element, Sabit.XML_RENK);
            if (nodeRenk == null)
            {
                ekranaHataYazdir("44", "etiket getirilemedi");
            }
            else
            {
                nodeRenk.setTextContent(secilenRenk);
                documentToFile(Sabit.DOCUMENT_ASIL);
            }
        }

        public void kaydinRenkBilgisiniGuncelle(String secilenRenk)
        {
            alertRenk.dismiss();
            Element element = document.getElementById(String.valueOf(seciliCRL.getId()));
            //Node nodeRenk = element.getChildNodes().item(1);

            Node nodeRenk = etiketiGetir(element, Sabit.XML_RENK);
            if (nodeRenk == null)
            {
                ekranaHataYazdir("46", "etiket getirilemedi");
            }
            else
            {
                nodeRenk.setTextContent(secilenRenk);
                documentToFile(Sabit.DOCUMENT_ASIL);
            }
        }

        public static int getElemanTuru()
        {
            return elemanTuru;
        }

        //icine girilen kategori ve kayıt rengine göre ekranın rengini degistirir
        public void ekranRenginiDegistir()
        {
            LinearLayout ll = (LinearLayout) fragmentRootView.findViewById(R.id.al);
            ll.setBackgroundColor(Color.parseColor(seciliCRL.getRenk()));
            //ll.getBackground().setAlpha(128);
        }

        //verilen renge göre ekran engini degistirir
        public static void ekranRenginiDegistir(String renk)
        {
            LinearLayout ll = (LinearLayout) fragmentRootView.findViewById(R.id.al);
            ll.setBackgroundColor(Color.parseColor(renk));
            //ll.getBackground().setAlpha(128);
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

        public void menuAnaEkran(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.renk_degistir), MenuItem.SHOW_AS_ACTION_ALWAYS, "Renk Değiştir", Sabit.ACTION_ANA_EKRAN_RENK_DEGISTIR);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kategori_ekle), MenuItem.SHOW_AS_ACTION_ALWAYS, "Kategori Ekle", Sabit.ACTION_ANA_EKRAN_KATEGORI_EKLE);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kayit_ekle), MenuItem.SHOW_AS_ACTION_ALWAYS, "Kayit Ekle", Sabit.ACTION_ANA_EKRAN_KAYIT_EKLE);
            menu.add(0, Sabit.ACTION_ANA_EKRAN_YEDEKLE, 0, "Yedekle");
            menu.add(0, Sabit.ACTION_ANA_EKRAN_YEDEGI_YUKLE, 0, "Yedeği Yükle");
            menu.add(0, Sabit.ACTION_ANA_EKRAN_YEDEKLERI_GOSTER, 0, "Yedek Dosyaları");
            menu.add(0, Sabit.ACTION_ANA_EKRAN_AYARLAR, 0, "Ayarlar");
        }

        public void menuSecim(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.tamam), MenuItem.SHOW_AS_ACTION_ALWAYS, "Tamamla", Sabit.ACTION_SECIM_TAMAM);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.yeni), MenuItem.SHOW_AS_ACTION_ALWAYS, "Yeni", Sabit.ACTION_SECIM_YENI);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.sil), MenuItem.SHOW_AS_ACTION_ALWAYS, "Sil", Sabit.ACTION_SECIM_SIL);
        }

        public void menuKayitDegistir(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.renk_degistir), MenuItem.SHOW_AS_ACTION_ALWAYS, "Renk Değiştir", Sabit.ACTION_DEGISTIR_RENK_DEGISTIR);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kaydet), MenuItem.SHOW_AS_ACTION_ALWAYS, "Kaydet", Sabit.ACTION_DEGISTIR_KAYDET);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.tamam), MenuItem.SHOW_AS_ACTION_ALWAYS, "Tamamla", Sabit.ACTION_DEGISTIR_TAMAM);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.yeni), MenuItem.SHOW_AS_ACTION_ALWAYS, "Yeni", Sabit.ACTION_DEGISTIR_YENI);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.sil), MenuItem.SHOW_AS_ACTION_ALWAYS, "Sil", Sabit.ACTION_DEGISTIR_SIL);
        }

        public void menuYedek(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.sil), MenuItem.SHOW_AS_ACTION_ALWAYS, "Sil", Sabit.ACTION_YEDEK_SIL, Sabit.RENK_SIYAH);
        }

        public void menuAyar(Menu menu)
        {
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.ontanimli), MenuItem.SHOW_AS_ACTION_ALWAYS, "Sıfırla", Sabit.ACTION_AYAR_ONTANIMLI, Sabit.RENK_SIYAH);
            menuIkonEkle(menu, getResources().getDrawable(R.drawable.kaydet), MenuItem.SHOW_AS_ACTION_ALWAYS, "Kaydet", Sabit.ACTION_AYAR_KAYDET, Sabit.RENK_SIYAH);
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
                            menu.findItem(Sabit.ACTION_DEGISTIR_TAMAM).setVisible(false);
                            break;

                        case Sabit.DURUM_YENI:
                            menu.findItem(Sabit.ACTION_DEGISTIR_YENI).setVisible(false);
                            break;

                        default:
                            ekranaHataYazdir("26", "hatalı kayıt durum türü : " + seciliCRL.getDurum());
                    }
                    break;

                case Sabit.ACTIONBAR_YEDEK:
                    menuYedek(menu);
                    break;

                case Sabit.ACTIONBAR_AYAR:
                    menuAyar(menu);
                    break;

                default:
                    ekranaHataYazdir("27", "hatalı actionBar türü : " + ACTIONBAR_TUR);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                //ana ekran
                case Sabit.ACTION_ANA_EKRAN_KATEGORI_EKLE:
                    kategoriKaydet();
                    return true;

                case Sabit.ACTION_ANA_EKRAN_KAYIT_EKLE:
                    yaziyiKaydet();
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
                //ana ekran
                //secim
                case Sabit.ACTION_SECIM_TAMAM:
                    seciliElemanlariTamamla();
                    Toast.makeText(getActivity(), "seçili kayıtlar tamamlandı olarak işaretlendi", Toast.LENGTH_SHORT).show();
                    return true;

                case Sabit.ACTION_SECIM_YENI:
                    seciliElemanlarYeni();
                    Toast.makeText(getActivity(), "seçili kayıtlar yeni olarak isaretlendi", Toast.LENGTH_SHORT).show();
                    return true;

                case Sabit.ACTION_SECIM_SIL:
                    seciliElemanlariSil();
                    return true;
                //secim
                //degistir
                case Sabit.ACTION_DEGISTIR_SIL:
                    kayitSilDiyaloguOlustur();
                    return true;

                case Sabit.ACTION_DEGISTIR_TAMAM:
                    Element elementKayitTamam = kayitDurumunuTamamlandiYap();
                    if (elementKayitTamam != null)
                    {
                        ustParcaDurumunuKontrolEtTamamla(elementKayitTamam);
                        actionBarDegistirSimgeDurumu(Sabit.DURUM_TAMAMLANDI);
                        Toast.makeText(getActivity(), "kayıt tamamlandı olarak işaretlendi", Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case Sabit.ACTION_DEGISTIR_YENI:
                    Element elementKayitYeni = kayitDurumunuYeniYap();
                    if (elementKayitYeni != null)
                    {
                        ustParcaDurumunuKontrolEtYeni(elementKayitYeni);
                        actionBarDegistirSimgeDurumu(Sabit.DURUM_YENI);
                        Toast.makeText(getActivity(), "kayıt yeni olarak isaretlendi", Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case Sabit.ACTION_DEGISTIR_KAYDET:
                    kayitDegistir();
                    klavyeKapat();
                    return true;

                case Sabit.ACTION_DEGISTIR_RENK_DEGISTIR:
                    renkDialogunuOlustur();
                    return true;
                //degistir
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
                case android.R.id.home:
                    klavyeKapat();
                    ustSeviyeyiGetir();
                    break;

                /*
                case R.id.action_onay_kaydet:
                    //yaziyiKaydet(etEklenecek);
                    //actionBarDegistir(Sabit.ACTIONBAR_EKLE);
                    return true;
                case R.id.action_onay_iptal:
                    //klavyeKapat(etEklenecek.getWindowToken());
                    //anaLayout.removeView(etEklenecek);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            fragmentRootView = rootView;
            anaLayout = (RelativeLayout) rootView.findViewById(R.id.anaLayout);

            switch (FRAGMENT_ETKIN_EKRAN)
            {
                case Sabit.FRAGMENT_KATEGORI_EKRANI:
                {
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
                    ma.geriSimgesiniEkle();

                    if (xmlEnBuyukID > 0)//xml de kayıt varsa ekrana eklesin
                    {
                        yeniYerlesimOlustur();
                        //parseXml(xmlParcaID);
                        parseXml(String.valueOf(seciliCRL.getId()));
                        kategoriBasliginiYaz();
                    }
                    else//xml'de kayıt yok
                    {
                        yeniYerlesimOlustur();
                        kategoriBasliginiYaz();
                    }
                    return rootView;
                }

                case Sabit.FRAGMENT_KAYIT_EKRANI:
                {
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
                    anaLayout.addView(rl);

                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    klavyeAc(getActivity().getApplicationContext(), etDegisecek);

                    return rootView;
                }

                case Sabit.FRAGMENT_YEDEK_EKRANI:
                {
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    getActivity().getActionBar().setTitle((Html.fromHtml("<font color='" + Sabit.RENK_SIYAH + "'>yedek dosyaları</font>")));
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
                        anaLayout.addView(yrl);
                    }
                    return rootView;
                }

                case Sabit.FRAGMENT_AYAR_EKRANI:
                {
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    getActivity().getActionBar().setTitle((Html.fromHtml("<font color='" + Sabit.RENK_SIYAH + "'>ayarlar</font>")));
                    ekranRenginiDegistir(Sabit.RENK_BEYAZ2);
                    ma.actionBarArkaPlanDegistir(Sabit.RENK_BEYAZ2);
                    ma.geriSimgesiniEkle(Sabit.RENK_SIYAH);
                    ayarlariAyarEkraninaEkle();

                    return rootView;
                }

                default:
                    return null;
            }
        }
    }
}
