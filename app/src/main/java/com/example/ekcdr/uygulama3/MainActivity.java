package com.example.ekcdr.uygulama3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
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
    private static String xmlDosyaYolu;
    private static String xmlYedekKlasorYolu;
    private static final String UYGULAMA_ADI = "uygulama3";
    private static final String YEDEK_KLASORU_ADI = "backup";
    private static final String XML_PARCA = "parca";
    private static final String XML_RENK = "renk";
    private static final String XML_BASLIK = "baslik";
    private static final String XML_YAZILAR = "yazilar";
    //private static final String XML_ROOT = "root";
    private static final String XML_KAYIT = "kayit";
    private static final String XML_ALTPARCA = "altparca";
    private static final String XML_ID = "id";
    private static final String XML_DURUM = "durum";
    private static final String DURUM_YENI = "0";
    private static final String DURUM_TAMAMLANDI = "1";
    private static final String FRAGMENT_TAG = "fragment_tag";
    public static final int ELEMAN_TUR_KAYIT = 0;
    public static final int ELEMAN_TUR_KATEGORI = 1;
    private static final String FRAGMENT_SECIM = "fragment_secim";
    private static final String FRAGMENT_YAZI = "fragment_yazi";
    private static final int ACTIONBAR_EKLE = 0;
    private static final int ACTIONBAR_ONAY = 1;
    private static final int ACTIONBAR_SECIM = 2;
    private static final int ACTIONBAR_DEGISTIR = 3;
    private static final int ACTIONBAR_YEDEK = 4;
    private static final int SECIM_YAPILDI = 1;
    private static final int SECIM_IPTAL_EDILDI = 0;
    private static int ACTIONBAR_TUR = ACTIONBAR_EKLE;
    private static final int FRAGMENT_KATEGORI_EKRANI = 0;
    private static final int FRAGMENT_KAYIT_EKRANI = 1;
    private static final int FRAGMENT_YEDEK_EKRANI = 2;
    private static int FRAGMENT_ETKIN_EKRAN;
    private static final int OLAY_ICINE_GIR = 0;
    private static final int OLAY_SECIM_YAP = 1;
    private static int TIKLAMA_OLAYI;
    private static final String TIK_UNICODE = "\u2714";
    private static final String ACTIONBAR_ARKAPLAN_KATEGORI = "#00CED1";
    private static final String ACTIONBAR_ARKAPLAN_KAYIT = "#009ED1";
    private static final String ACTIONBAR_ARKAPLAN_SECILI = "#FF2222";
    private static final String UC_NOKTA = "/.../";
    private static String KAYIT_DURUM_TUR;
    private static String xmlKayitID = "-1";//içine girilen kayit id si
    private static String xmlParcaID = "0";//içinde olunan parçanın id si
    private static int xmlEnBuyukID;//eklenen kategori ve kayıtlara id verebilmek için
    private static final double ORAN_DIKEY = 0.3;
    private static final double ORAN_YATAY = 0.6;
    private static View activityRootView;
    private static Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File xmlKlasoru;
        File xmlYedekKlasoru;
        String xmlDosyasi;

        activityRootView = findViewById(R.id.container);
        xmlKlasoru = xmlKlasoruKontrolEt();
        xmlYedekKlasoru = xmlYedekKlasoruKontrolEt();
        if (xmlKlasoru != null && xmlYedekKlasoru != null)
        {
            xmlYedekKlasorYolu = xmlYedekKlasoru.getAbsolutePath();

            if (xmlKlasoru.exists())
            {
                xmlDosyasi = xmlKlasoru + "/" + "new.xml";

                if (xmlDosyasiKontrolEt(xmlDosyasi))
                {
                    document = xmlDocumentNesnesiOlustur(xmlDosyasi);
                    if (document == null)
                    {
                        ekranaHataYazdir("1", "document olusamadı");
                        finish();
                    }
                    else
                    {
                        xmlEnBuyukID = enBuyukIDyiBul();
                        if (xmlEnBuyukID == -1)
                        {
                            ekranaHataYazdir("1", "xml okunamadı");
                            finish();
                        }
                    }
                }
                else
                {
                    xmlDosyasiOlustur(xmlDosyasi);
                    xmlEnBuyukID = 0;
                    document = xmlDocumentNesnesiOlustur(xmlDosyasi);
                }

                if (savedInstanceState == null)
                {
                    getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, 0), FRAGMENT_TAG).commit();
                }

                getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(ACTIONBAR_ARKAPLAN_KATEGORI)));
                getActionBar().setDisplayUseLogoEnabled(false);
                getActionBar().setDisplayShowHomeEnabled(false);

                xmlDosyaYolu = xmlDosyasi;
            }
            else
            {
                ekranaHataYazdir("2", "xml klasoru olusurken hata");
                finish();
            }
        }
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
            return null;
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (SAXException e)
        {
            return null;
        }
    }

    //xml in duracagı klasoru olusturur
    public File xmlKlasoruKontrolEt()
    {
        File xmlKlasoru;

        if (hariciAlanVarMi())//sdcard var
        {
            xmlKlasoru = new File(Environment.getExternalStorageDirectory().getPath() + "/" + UYGULAMA_ADI);
        }
        else//sdcard yok
        {
            xmlKlasoru = getDir(UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!xmlKlasoru.exists())
        {
            if (!xmlKlasoru.mkdirs())
            {
                ekranaHataYazdir("2", "xml klasoru olusurken hata");
                return null;
            }
        }

        return xmlKlasoru;
    }

    public File xmlYedekKlasoruKontrolEt()
    {
        File xmlYedekKlasoru;

        if (hariciAlanVarMi())//sdcard var
        {
            xmlYedekKlasoru = new File(Environment.getExternalStorageDirectory().getPath() + "/" + UYGULAMA_ADI + "/" + YEDEK_KLASORU_ADI);
        }
        else//sdcard yok
        {
            xmlYedekKlasoru = getDir(UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!xmlYedekKlasoru.exists())
        {
            if (!xmlYedekKlasoru.mkdirs())
            {
                ekranaHataYazdir("2", "xml yedek klasoru olusurken hata");
                return null;
            }
        }

        return xmlYedekKlasoru;
    }

    //xml dosyası var mı diye kontrol ediyor. yoksa oluşturuyor ve en buyuk xml id sini buluyor
    public boolean xmlDosyasiKontrolEt(String xmlDosyaYolu)
    {
        File xmlDosyasi = new File(xmlDosyaYolu);
        return xmlDosyasi.exists();
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

        NodeList nodeListParca = document.getElementsByTagName(XML_PARCA);
        NodeList nodeListKayit = document.getElementsByTagName(XML_KAYIT);

        for (int i = 0; i < nodeListParca.getLength(); i++)
        {
            if (Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem(XML_ID).getNodeValue()) > sonIDParca)
            {
                sonIDParca = Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem(XML_ID).getNodeValue());
            }
        }
        for (int i = 0; i < nodeListKayit.getLength(); i++)
        {
            if (Integer.parseInt(nodeListKayit.item(i).getAttributes().getNamedItem(XML_ID).getNodeValue()) > sonIDKayit)
            {
                sonIDKayit = Integer.parseInt(nodeListKayit.item(i).getAttributes().getNamedItem(XML_ID).getNodeValue());
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
    public void xmlDosyasiOlustur(String xmlDosyaYolu)
    {
        try
        {
            //string i xml dosyasına yazıyor
            BufferedWriter out = new BufferedWriter(new FileWriter(xmlDosyaYolu));
            out.write("<?xml version=\"1.0\"?><root>" +
                    "<parca id=\"0\" durum=\"0\">" +
                    "<baslik/>" +
                    "<renk/>" +
                    "<yazilar/>" +
                    "<altparca/>" +
                    "</parca>" +
                    "</root>");
            out.close();
            ////////////////////////////////////////
        }
        catch (IOException e)
        {
            Log.e("hata[3]", e.getMessage());
            ekranaHataYazdir("3", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_ekle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_kategori_ekle://fragment sınıfının içinde yakaladığım için false döndürüyor
                return false;
            case R.id.action_kayit_ekle://fragment sınıfının içinde yakaladığım için false döndürüyor
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ekranaHataYazdir(String id, String hata)
    {
        Toast.makeText(getApplicationContext(), "hata[" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
    }

    public static class PlaceholderFragment extends Fragment
    {
        private LinearLayout anaLayout;//viewların içine yerleşeceği ana layout
        private MenuInflater inflaterActionBar;
        private Menu menuActionBar;
        private EditText etEklenecek;//yeni kayıt eklemeye tıklandığı zaman olusan edittext
        private EditText etDegisecek;//kayit degiştirmeye tıklandığı zaman olusan edittext
        private static List<Integer> listSeciliKategori;//seçilen kategorilerin listesi
        private static List<Integer> listSeciliKayit;//seçilen kayıtların listesi
        private static List<String> listSeciliElemanDurumu;//seçilen elemanların durumlarının listesi
        private static List<yedekRelativeLayout> listSeciliYedek;//seçilen kategorilerin listesi
        private String TAG = "uyg3";
        private Activity fAct;

        public PlaceholderFragment()
        {

        }

        public static PlaceholderFragment newInstanceKategori(int secim, int kategoriID)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(FRAGMENT_SECIM, secim);
            FRAGMENT_ETKIN_EKRAN = secim;
            fragment.setArguments(args);
            xmlParcaID = String.valueOf(kategoriID);
            fragment.setHasOptionsMenu(true);

            TIKLAMA_OLAYI = OLAY_ICINE_GIR;

            listSeciliKayit = new ArrayList<>();
            listSeciliKategori = new ArrayList<>();
            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();

            return fragment;
        }

        public static PlaceholderFragment newInstanceKayit(int secim, String yazi, int id, String durum)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(FRAGMENT_SECIM, secim);
            args.putString(FRAGMENT_YAZI, yazi);
            FRAGMENT_ETKIN_EKRAN = secim;
            fragment.setArguments(args);
            fragment.setHasOptionsMenu(true);

            ACTIONBAR_TUR = ACTIONBAR_DEGISTIR;
            xmlKayitID = String.valueOf(id);

            TIKLAMA_OLAYI = OLAY_ICINE_GIR;

            listSeciliKayit = new ArrayList<>();
            listSeciliKategori = new ArrayList<>();
            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();

            KAYIT_DURUM_TUR = durum;

            return fragment;
        }

        public static PlaceholderFragment newInstanceYedek(int secim)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(FRAGMENT_SECIM, secim);
            FRAGMENT_ETKIN_EKRAN = secim;
            fragment.setArguments(args);
            fragment.setHasOptionsMenu(true);

            TIKLAMA_OLAYI = OLAY_ICINE_GIR;

            listSeciliKayit = new ArrayList<>();
            listSeciliKategori = new ArrayList<>();
            listSeciliElemanDurumu = new ArrayList<>();
            listSeciliYedek = new ArrayList<>();

            return fragment;
        }

        public void actionBarArkaPlanDegistir(String renk)
        {
            ColorDrawable actionBarArkaPlan = new ColorDrawable(Color.parseColor(renk));
            getActivity().getActionBar().setBackgroundDrawable(actionBarArkaPlan);
        }

        public void ekranaHataYazdir(String id, String hata)
        {
            Toast.makeText(getActivity(), "hata[" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
        }

        //parca etiketinin altındaki yazi ve kategorileri ekrana basıyor
        public void parseXml(String parcaID)
        {
            Element element = document.getElementById(parcaID);
            NodeList nodeList = element.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeName().equals(XML_YAZILAR))
                {
                    Node nodeKayit = nodeList.item(i);
                    NodeList nodeListYazilar = nodeKayit.getChildNodes();

                    for (int j = 0; j < nodeListYazilar.getLength(); j++)
                    {
                        Node nodeYazi = nodeListYazilar.item(j);
                        String kayitYazi = nodeYazi.getTextContent();
                        String kayitDurum = nodeYazi.getAttributes().getNamedItem(XML_DURUM).getNodeValue();
                        String kayitID = nodeYazi.getAttributes().getNamedItem(XML_ID).getNodeValue();

                        kayitlariAnaEkranaEkle(kayitYazi, Integer.parseInt(kayitID), kayitDurum);
                    }
                }
                else if (nodeList.item(i).getNodeName().equals(XML_ALTPARCA))
                {
                    Node nodeAltParca = nodeList.item(i);
                    NodeList nodeListParcalar = nodeAltParca.getChildNodes();

                    for (int j = 0; j < nodeListParcalar.getLength(); j++)
                    {
                        Node nodeParca = nodeListParcalar.item(j);
                        Node nodeBaslik = nodeParca.getFirstChild();
                        String kategoriYazi = nodeBaslik.getTextContent();
                        String kategoriDurum = nodeParca.getAttributes().getNamedItem(XML_DURUM).getNodeValue();
                        String kategoriID = nodeParca.getAttributes().getNamedItem(XML_ID).getNodeValue();

                        kategoriyiAnaEkranaEkle(kategoriYazi, Integer.parseInt(kategoriID), kategoriDurum);
                    }
                }
            }
        }

        //secilen elemanların durum bilgilerine göre actionBar daki simgeleri gizler, gösterir
        public void secimEkranindaDurumuKontrolEt(String durum, int eylem)
        {
            switch (eylem)
            {
                case SECIM_YAPILDI:
                    listSeciliElemanDurumu.add(durum);
                    break;

                case SECIM_IPTAL_EDILDI:
                    listSeciliElemanDurumu.remove(listSeciliElemanDurumu.indexOf(durum));
                    break;

                default:
                    Log.d(TAG, "hata");
            }

            if (listSeciliElemanDurumu.contains(DURUM_YENI))
            {
                menuActionBar.findItem(R.id.action_secim_tamam).setVisible(true);
            }
            else
            {
                menuActionBar.findItem(R.id.action_secim_tamam).setVisible(false);
            }
            if (listSeciliElemanDurumu.contains(DURUM_TAMAMLANDI))
            {
                menuActionBar.findItem(R.id.action_secim_yeni).setVisible(true);
            }
            else
            {
                menuActionBar.findItem(R.id.action_secim_yeni).setVisible(false);
            }
        }

        //xml parse edildikten sonra kayitları ana ekrana ekler
        public void kayitlariAnaEkranaEkle(final String yazi, final int eklenenID, final String durum)
        {
            final CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), yazi, ELEMAN_TUR_KAYIT, eklenenID, this);
            //final customRelativeLayout crl = new customRelativeLayout(getActivity(), yazi, ELEMAN_TUR_KAYIT, eklenenID);
            crl.setId(eklenenID);
            if (durum.equals(DURUM_TAMAMLANDI))
            {
                crl.getTvTik().setText(TIK_UNICODE);
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
                        listSeciliKayit.add(eklenenID);
                        crl.arkaplanSecili();
                        crl.setCrlSeciliMi(true);
                        actionBarDegistir(ACTIONBAR_SECIM);
                        TIKLAMA_OLAYI = OLAY_SECIM_YAP;

                        actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_SECILI);

                        secimEkranindaDurumuKontrolEt(durum, SECIM_YAPILDI);
                    }

                    return true;
                }
            });
            crl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (TIKLAMA_OLAYI == OLAY_ICINE_GIR)
                    {
                        getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKayit(FRAGMENT_KAYIT_EKRANI, yazi, eklenenID, durum)).addToBackStack(null).commit();
                        actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KAYIT);
                    }
                    else if (TIKLAMA_OLAYI == OLAY_SECIM_YAP)
                    {
                        if (crl.isCrlSeciliMi())
                        {
                            listSeciliKayit.remove(listSeciliKayit.indexOf(eklenenID));
                            crl.arkaplanKayit();
                            crl.setCrlSeciliMi(false);

                            if (seciliElemanSayisi() == 0)
                            {
                                actionBarDegistir(ACTIONBAR_EKLE);
                                TIKLAMA_OLAYI = OLAY_ICINE_GIR;

                                actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
                                duzenleSimgesininGorunumunuDegistir(View.INVISIBLE);

                                listSeciliElemanDurumu.clear();
                            }
                            else
                            {
                                secimEkranindaDurumuKontrolEt(durum, SECIM_IPTAL_EDILDI);
                            }
                        }
                        else
                        {
                            listSeciliKayit.add(eklenenID);
                            crl.arkaplanSecili();
                            crl.setCrlSeciliMi(true);

                            secimEkranindaDurumuKontrolEt(durum, SECIM_YAPILDI);
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
            while (!elementParca.getAttributes().getNamedItem(XML_ID).getNodeValue().equals("0"));

            Rect bounds = new Rect();
            Paint textPaint = new Paint();
            textPaint.getTextBounds(baslik, 0, baslik.length(), bounds);
            int height = bounds.width();
            float dpWidth = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;
            float oran = height / dpWidth;
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                if (oran > ORAN_DIKEY)
                {
                    int a = 1;//ilk / işaretini ararken ilk döngüde 1. basamaktan, sonra ise 6. basamaktan aramaya başlayacak
                    while (oran > ORAN_DIKEY)
                    {
                        int yer = baslik.indexOf("/", a);
                        String atilacakKisim = baslik.substring(0, yer + 1);

                        baslik = baslik.replaceFirst(atilacakKisim, UC_NOKTA);

                        textPaint.getTextBounds(baslik, 0, baslik.length(), bounds);
                        height = bounds.width();
                        oran = height / dpWidth;

                        a = 6;
                    }
                    if (baslik.equals(UC_NOKTA))//baslik actionBar'a sığmıyor. sığdığı kadarı yazılacak
                    {
                        baslik = UC_NOKTA + asilBaslik;
                    }
                }
            }
            else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                if (oran > ORAN_YATAY)
                {
                    int a = 1;//ilk / işaretini ararken ilk döngüde 1. basamaktan, sonra ise 6. basamaktan aramaya başlayacak
                    while (oran > ORAN_YATAY)
                    {
                        int yer = baslik.indexOf("/", a);
                        String atilacakKisim = baslik.substring(0, yer + 1);
                        baslik = baslik.replaceFirst(atilacakKisim, UC_NOKTA);

                        textPaint.getTextBounds(baslik, 0, baslik.length(), bounds);
                        height = bounds.width();
                        oran = height / dpWidth;

                        a = 6;
                    }
                    if (baslik.equals(UC_NOKTA))//baslik actionBar'a sığmıyor. sığdığı kadarı yazılacak
                    {
                        baslik = UC_NOKTA + asilBaslik;
                    }
                }
            }

            return baslik;
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        public void kategoriyiAnaEkranaEkle(final String baslik, final int kategoriID, final String durum)
        {
            //final customRelativeLayout crl = new customRelativeLayout(getActivity(), baslik, ELEMAN_TUR_KATEGORI, kategoriID);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
            final CustomRelativeLayout crl = new CustomRelativeLayout(getActivity(), baslik, ELEMAN_TUR_KATEGORI, kategoriID, this);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
            crl.setId(kategoriID);
            if (durum.equals(DURUM_TAMAMLANDI))
            {
                crl.getTvTik().setText(TIK_UNICODE);
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
                    if (TIKLAMA_OLAYI == OLAY_ICINE_GIR)
                    {
                        getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, kategoriID), FRAGMENT_TAG).commit();
                        actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
                    }
                    else if (TIKLAMA_OLAYI == OLAY_SECIM_YAP)
                    {
                        if (crl.isCrlSeciliMi())
                        {
                            listSeciliKategori.remove(listSeciliKategori.indexOf(kategoriID));
                            crl.arkaplanKategori();
                            crl.setCrlSeciliMi(false);

                            if (seciliElemanSayisi() == 0)
                            {
                                actionBarDegistir(ACTIONBAR_EKLE);
                                TIKLAMA_OLAYI = OLAY_ICINE_GIR;

                                actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
                                duzenleSimgesininGorunumunuDegistir(View.INVISIBLE);

                                listSeciliElemanDurumu.clear();
                            }
                            else
                            {
                                secimEkranindaDurumuKontrolEt(durum, SECIM_IPTAL_EDILDI);
                            }
                        }
                        else
                        {
                            listSeciliKategori.add(kategoriID);
                            crl.arkaplanSecili();
                            crl.setCrlSeciliMi(true);

                            secimEkranindaDurumuKontrolEt(durum, SECIM_YAPILDI);
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
                        listSeciliKategori.add(kategoriID);
                        crl.arkaplanSecili();
                        crl.setCrlSeciliMi(true);
                        actionBarDegistir(ACTIONBAR_SECIM);
                        TIKLAMA_OLAYI = OLAY_SECIM_YAP;

                        actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_SECILI);
                        duzenleSimgesininGorunumunuDegistir(View.VISIBLE);

                        secimEkranindaDurumuKontrolEt(durum, SECIM_YAPILDI);
                    }

                    return true;
                }
            });
            anaLayout.addView(crl);
        }

        //kategori layout'undaki duzenle simgesini gösterir ve gizler
        public void duzenleSimgesininGorunumunuDegistir(int gorunum)
        {
            int childcount = anaLayout.getChildCount();
            for (int i = 0; i < childcount; i++)
            {
                //customRelativeLayout crl = (customRelativeLayout) anaLayout.getChildAt(i);
                CustomRelativeLayout crl = (CustomRelativeLayout) anaLayout.getChildAt(i);
                if (crl.getCrlTur() == ELEMAN_TUR_KATEGORI)
                {
                    crl.getTvDuzenle().setVisibility(gorunum);
                }
            }
        }

        //Document nesnesini dosyaya yazıyor
        public void documentToFile()
        {
            try
            {
                document.normalize();
                //document i string e çeviriyor
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(writer));
                String output = "<?xml version=\"1.0\"?>" + writer.getBuffer().toString().replaceAll("\n|\r", "");
                ///////////////////////////////
                //string i xml dosyasına yazıyor
                BufferedWriter out = new BufferedWriter(new FileWriter(xmlDosyaYolu));

                out.write(output);
                out.close();
                //////////////////////////////////
            }
            catch (TransformerConfigurationException e)
            {
                Log.e("hata[9]", e.getMessage());
                ekranaHataYazdir("9", e.getMessage());
            }
            catch (TransformerException e)
            {
                Log.e("hata[10]", e.getMessage());
                ekranaHataYazdir("10", e.getMessage());
            }
            catch (IOException e)
            {
                Log.e("hata[11]", e.getMessage());
                ekranaHataYazdir("11", e.getMessage());
            }
        }

        //başta oluşturulan xml e yeni eklenecek kısımları ekler ve en buyuk xml idsini döndürür
        public int xmlDosyasiniGuncelle(String baslik, String renk)
        {
            xmlEnBuyukID++;
            {
                Node nodeMevcutParca = document.getElementById(String.valueOf(xmlParcaID));//içinde bulunulan parcaya giriyor
                NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                {
                    if (nodeParcaCocuklari.item(i).getNodeName().equals(XML_ALTPARCA))//parcanın içindeki altparca etiketine ulaşılıyor
                    {
                        Node nodeAltparca = nodeParcaCocuklari.item(i);//altparcaya giriliyor
                        Element yeniNodeParca = document.createElement(XML_PARCA);//parca isimli etiket olşuturuluyor
                        yeniNodeParca.setAttribute(XML_ID, String.valueOf(xmlEnBuyukID));//parca ya id özelliği ekleniyor
                        yeniNodeParca.setAttribute(XML_DURUM, DURUM_YENI);//parca ya id özelliği ekleniyor
                        nodeAltparca.appendChild(yeniNodeParca);//altparca etiketine parca ekleniyor

                        Node nodeParca = document.getElementById(String.valueOf(xmlEnBuyukID));//xmlid id sine sahip parca nın içine giriliyor. az önce oluşturulan parca
                        Element yeniNodeBaslik = document.createElement(XML_BASLIK);//baslik etiketi oluşturuluyor
                        yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri giriliyor
                        Element yeniNodeRenk = document.createElement(XML_RENK);//renk etiketi oluşturuluyor
                        yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri giriliyor
                        Element yeniNodeYazi = document.createElement(XML_YAZILAR);//yazi etiketi oluşturuluyor
                        Element yeniNodeAltparca = document.createElement(XML_ALTPARCA);//altparca etiketi oluşturuluyor
                        nodeParca.appendChild(yeniNodeBaslik);//parca etiketine baslik etiketi ekleniyor
                        nodeParca.appendChild(yeniNodeRenk);//parca etiketine renk etiketi ekleniyor
                        nodeParca.appendChild(yeniNodeYazi);//parca etiketine yazi etiketi ekleniyor
                        nodeParca.appendChild(yeniNodeAltparca);//parca etiketine altparca etiketi ekleniyor
                        break;
                    }
                }
                documentToFile();

                return xmlEnBuyukID;
            }
        }

        //listSeciliKategori ve listSeciliKayit'yi sıfırlar ve actionbar ı ilk haline döndürür
        public void seciliElemanListeleriniSifirla()
        {
            listSeciliKategori.clear();
            listSeciliKayit.clear();
            actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
            actionBarDegistir(ACTIONBAR_EKLE);
            TIKLAMA_OLAYI = OLAY_ICINE_GIR;
            duzenleSimgesininGorunumunuDegistir(View.INVISIBLE);
        }

        //ana ekrana ve xml'e kategori ekler(parca)
        public void kategoriKaydet()
        {
            //alertdialog un içindeki ana LinearLayout
            LinearLayout alertLL = new LinearLayout(getActivity());
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            alertLL.setLayoutParams(pa);
            alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
            alertLL.setWeightSum(1f);

            //yazının yazılacagı EditText
            final EditText alertET = new EditText(getActivity());
            LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            alertET.setLayoutParams(pa2);
            alertET.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
            alertLL.addView(alertET);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Kategori Adı");
            builder.setView(alertLL);

            builder.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
            builder.setNegativeButton("İptal", null);
            final AlertDialog alert = builder.create();
            alert.show();

            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String kategoriAdi = alertET.getText().toString();
                    if (kategoriAdi.isEmpty())//edittext boşken tamam'a tıklandı
                    {
                        Toast.makeText(getActivity(), "Kategori adı boş olamaz", Toast.LENGTH_LONG).show();
                    }
                    else//anaLayout'a yeni alan ekliyor
                    {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        alert.dismiss();
                        final int eklenenID = xmlDosyasiniGuncelle(kategoriAdi, "");

                        kategoriyiAnaEkranaEkle(kategoriAdi, eklenenID, DURUM_YENI);
                    }
                }
            });
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    alert.dismiss();
                }
            });
        }

        public int seciliElemanSayisi()
        {
            return listSeciliKategori.size() + listSeciliKayit.size();
        }

        public EditText yaziAlaniOlustur()
        {
            //actionBarOnay();
            //ACTIONBAR_TUR = ACTIONBAR_ONAY;

            EditText edittext = new EditText(getActivity());
            anaLayout.addView(edittext);
            return edittext;
        }

        public void ustSeviyeyiGetir()
        {
            actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);

            switch (FRAGMENT_ETKIN_EKRAN)
            {
                case FRAGMENT_KAYIT_EKRANI:

                    FragmentManager fm = getFragmentManager();

                    if (fm.getBackStackEntryCount() > 0)
                    {
                        fm.popBackStackImmediate();
                        xmlKayitID = "-1";
                    }
                    FRAGMENT_ETKIN_EKRAN = FRAGMENT_KATEGORI_EKRANI;
                    ACTIONBAR_TUR = ACTIONBAR_EKLE;

                    break;
                case FRAGMENT_KATEGORI_EKRANI:

                    Element element = document.getElementById(xmlParcaID);
                    String ustSeviyeID = element.getParentNode().getParentNode().getAttributes().getNamedItem(XML_ID).getNodeValue();
                    xmlParcaID = ustSeviyeID;
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID)), FRAGMENT_TAG).commit();

                    break;
                case FRAGMENT_YEDEK_EKRANI:

                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID)), FRAGMENT_TAG).commit();
                    FRAGMENT_ETKIN_EKRAN = FRAGMENT_KATEGORI_EKRANI;
                    ACTIONBAR_TUR = ACTIONBAR_EKLE;

                    break;
                default:
                    ekranaHataYazdir("1", "geri giderken hata");
            }
        }

        //kayit ekle tusuna basıldıktan sonra açılan edittext'e yazılan yazıyı xml'e ekler
        public void yaziyiKaydet(EditText et)
        {
            xmlEnBuyukID++;
            {
                String alanYazi = et.getText().toString();

                Log.d(TAG, "xmlParcaID : " + xmlParcaID);
                kategoriDurumunuGuncelle(xmlParcaID, DURUM_YENI);

                Node nodeMevcutParca = document.getElementById(String.valueOf(xmlParcaID));//içinde bulunulan parcaya giriyor
                NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                {
                    if (nodeParcaCocuklari.item(i).getNodeName().equals(XML_YAZILAR))//parcanın içindeki yazilar etiketine ulaşılıyor
                    {
                        int eklenenID = xmlEnBuyukID;
                        Element yeniNodeKayit = document.createElement(XML_KAYIT);//kayıt etiketi olusturuyor
                        yeniNodeKayit.setAttribute(XML_ID, String.valueOf(xmlEnBuyukID));//parca ya id özelliği ekleniyor
                        yeniNodeKayit.setAttribute(XML_DURUM, DURUM_YENI);//parca ya id özelliği ekleniyor
                        nodeParcaCocuklari.item(i).appendChild(yeniNodeKayit);//yazilar etiketinin içine kayit etiketini ekliyor

                        StringBuilder str = new StringBuilder(alanYazi);//alt satıra geçmeyi anlayabilmek için \n <br> ile değiştiriliyor
                        int sayac = 0;
                        for (i = 0; i < alanYazi.length(); i++)//<br> eklendiği zaman stringin boyu 4 uzuyor onun için sayac tutuyoruz
                        {
                            if (alanYazi.charAt(i) == '\n')
                            {
                                str.insert(i + sayac, "<br>");
                                sayac = sayac + 4;
                            }
                        }
                        yeniNodeKayit.setTextContent(str.toString());

                        /*
                        Node nodeYazi = nodeParcaCocuklari.item(i);//altparcaya giriliyor
                        StringBuffer str = new StringBuffer(alanYazi);//alt satıra geçmeyi anlayabilmek için \n <br> ile değiştiriliyor
                        int sayac = 0;
                        for (i = 0; i < alanYazi.length(); i++)//<br> eklendiği zaman stringin boyu 4 uzuyor onun için sayac tutuyoruz
                        {
                            if (alanYazi.charAt(i) == '\n')
                            {
                                str.insert(i + sayac, "<br>");
                                sayac = sayac + 4;
                            }
                        }
                        nodeYazi.setTextContent(str.toString());
                        */

                        klavyeKapat(getActivity(), et.getWindowToken());
                        anaLayout.removeView(et);
                        kayitlariAnaEkranaEkle(alanYazi, eklenenID, DURUM_YENI);

                        break;
                    }
                }
                documentToFile();
            }
        }

        //sil tusuna basıldığı zaman secili elemanları siler
        public void seciliElemanlariSil()
        {
            String soru = "Seçilen kayıtlar silinsin mi ?";

            LinearLayout alertLL = new LinearLayout(getActivity());
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            alertLL.setLayoutParams(pa);
            alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
            alertLL.setWeightSum(1f);

            final TextView alertTV = new TextView(getActivity());
            LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            alertTV.setLayoutParams(pa2);
            alertTV.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
            alertTV.setText(soru);
            alertLL.addView(alertTV);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Onay");
            builder.setView(alertLL);

            builder.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
            builder.setNegativeButton("İptal", null);
            final AlertDialog alert = builder.create();
            alert.show();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    kayitlariSil(listSeciliKayit);
                    kategoriSil(listSeciliKategori);

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
            if (!xmlKayitID.equals("-1"))
            {
                LinearLayout alertLL = new LinearLayout(getActivity());
                LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                alertLL.setLayoutParams(pa);
                alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
                alertLL.setWeightSum(1f);

                final TextView alertTV = new TextView(getActivity());
                LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                alertTV.setLayoutParams(pa2);
                alertTV.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
                alertTV.setText("Kayıt silinsin mi ?");
                alertLL.addView(alertTV);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Onay");
                builder.setView(alertLL);

                builder.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
                builder.setNegativeButton("İptal", null);
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
            }
            else
            {
                Log.e("hata[23]", "kayıt id -1");
                ekranaHataYazdir("23", "kayıt id -1");
            }
        }

        //kayit ekranındayken sile tıklandı. bir kayıt siliyor
        public void kayitSil()
        {
            Element element = document.getElementById(xmlKayitID);
            element.getParentNode().removeChild(element);
            documentToFile();

            xmlKayitID = "-1";
        }

        //secili kategorileri siler
        public void kategoriSil(List<Integer> listeSilinecek)
        {
            for (int i = 0; i < listeSilinecek.size(); i++)
            {
                Element element = document.getElementById(String.valueOf(listeSilinecek.get(i)));
                element.getParentNode().removeChild(element);
                documentToFile();

                //customRelativeLayout view = (customRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                CustomRelativeLayout view = (CustomRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                anaLayout.removeView(view);
            }
        }

        //secili kayıtları siler
        public void kayitlariSil(List<Integer> listeSilinecek)
        {
            for (int i = 0; i < listeSilinecek.size(); i++)
            {
                Element element = document.getElementById(String.valueOf(listeSilinecek.get(i)));
                element.getParentNode().removeChild(element);
                documentToFile();

                //customRelativeLayout view = (customRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                CustomRelativeLayout view = (CustomRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                anaLayout.removeView(view);
            }
        }

        //secili elemanların durumunu yeni olarak isaretler
        public void seciliElemanlarYeni()
        {
            for (int i = 0; i < listSeciliKayit.size(); i++)
            {
                Element element = kayitYeni(String.valueOf(listSeciliKayit.get(i)));
                if (element != null)
                {
                    //customRelativeLayout crl = (customRelativeLayout) anaLayout.findViewById(listSeciliKayit.get(i));
                    CustomRelativeLayout crl = (CustomRelativeLayout) anaLayout.findViewById(listSeciliKayit.get(i));
                    crl.getTvTik().setText("");
                    crl.arkaplanKayit();
                    crl.setCrlSeciliMi(false);
                }
            }
            for (int i = 0; i < listSeciliKategori.size(); i++)
            {
                Element element = kayitYeni(String.valueOf(listSeciliKategori.get(i)));
                if (element != null)
                {
                    kategoriCocuklarDurum(element, DURUM_YENI);
                    //customRelativeLayout crl = (customRelativeLayout) anaLayout.findViewById(listSeciliKategori.get(i));
                    CustomRelativeLayout crl = (CustomRelativeLayout) anaLayout.findViewById(listSeciliKategori.get(i));
                    crl.getTvTik().setText("");
                    crl.arkaplanKategori();
                    crl.setCrlSeciliMi(false);
                }
            }

            if (!listSeciliKayit.isEmpty())
            {
                Element elementKayit = document.getElementById(String.valueOf(listSeciliKayit.get(0)));//secilen butun kayıtlar aynı parca altında olduğu için 1 kez kontrol yeterli
                ustParcaDurumunuKontrolEtYeni(elementKayit);
            }
            else
            {
                Element elementKayit = document.getElementById(String.valueOf(listSeciliKategori.get(0)));//secilen butun kayıtlar aynı parca altında olduğu için 1 kez kontrol yeterli
                ustParcaDurumunuKontrolEtYeni(elementKayit);
            }

            seciliElemanListeleriniSifirla();

            documentToFile();
        }

        //secili kayıtları ve kategorilerin altındaki kayıtları tamamlandı olarak isaretler
        public void seciliElemanlariTamamla()
        {
            for (int i = 0; i < listSeciliKayit.size(); i++)
            {
                Element element = kayitTamamla(String.valueOf(listSeciliKayit.get(i)));
                if (element != null)
                {
                    //customRelativeLayout crl = (customRelativeLayout) anaLayout.findViewById(listSeciliKayit.get(i));
                    CustomRelativeLayout crl = (CustomRelativeLayout) anaLayout.findViewById(listSeciliKayit.get(i));
                    crl.getTvTik().setText(TIK_UNICODE);
                    crl.arkaplanKayit();
                    crl.setCrlSeciliMi(false);
                }
            }
            for (int i = 0; i < listSeciliKategori.size(); i++)
            {
                Element element = kayitTamamla(String.valueOf(listSeciliKategori.get(i)));
                if (element != null)
                {
                    kategoriCocuklarDurum(element, DURUM_TAMAMLANDI);
                    //customRelativeLayout crl = (customRelativeLayout) anaLayout.findViewById(listSeciliKategori.get(i));
                    CustomRelativeLayout crl = (CustomRelativeLayout) anaLayout.findViewById(listSeciliKategori.get(i));
                    crl.getTvTik().setText(TIK_UNICODE);
                    crl.arkaplanKategori();
                    crl.setCrlSeciliMi(false);
                }
            }

            if (!listSeciliKayit.isEmpty())
            {
                Element elementKayit = document.getElementById(String.valueOf(listSeciliKayit.get(0)));//secilen butun kayıtlar aynı parca altında olduğu için 1 kez kontrol yeterli
                ustParcaDurumunuKontrolEtTamamla(elementKayit);
            }
            else
            {
                Element elementKayit = document.getElementById(String.valueOf(listSeciliKategori.get(0)));//secilen butun kayıtlar aynı parca altında olduğu için 1 kez kontrol yeterli
                ustParcaDurumunuKontrolEtTamamla(elementKayit);
            }

            seciliElemanListeleriniSifirla();

            documentToFile();
        }

        public void kategoriCocuklarDurum(Element elementKategori, String durum)
        {
            NodeList nodeList = elementKategori.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeName().equals(XML_YAZILAR))
                {
                    NodeList nodeListKayitlar = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListKayitlar.getLength(); j++)
                    {
                        nodeListKayitlar.item(j).getAttributes().getNamedItem(XML_DURUM).setNodeValue(durum);
                    }
                }
                else if (nodeList.item(i).getNodeName().equals(XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        nodeListAltParca.item(j).getAttributes().getNamedItem(XML_DURUM).setNodeValue(durum);
                        kategoriCocuklarDurum((Element) nodeListAltParca.item(j), durum);
                    }
                }
            }
        }

        public void kategoriDurumunuGuncelle(String id, String durum)
        {
            Element elementKayit = document.getElementById(id);
            elementKayit.setAttribute(XML_DURUM, durum);
        }

        //parcanın yazilarini ve altparcalarını kontrol eder. hepsi tamamlanmiş ise parcayı tamamlandı olarak işaretler
        public boolean parcayiIsaretleTamamlandi(Node nodeParca)
        {
            String idKategori = nodeParca.getAttributes().getNamedItem(XML_ID).getNodeValue();

            boolean sonucYazilar = true;
            boolean sonucAltParcalar = true;
            NodeList nodeListParca = nodeParca.getChildNodes();//parcanın alt etiketlerini tarıyor. yazılar etiketini bulmak için
            for (int i = 0; i < nodeListParca.getLength(); i++)
            {
                if (nodeListParca.item(i).getNodeName().equals(XML_YAZILAR))
                {
                    Node nodeYazilar = nodeListParca.item(i);

                    NodeList nodeListKayit = nodeYazilar.getChildNodes();
                    for (int j = 0; j < nodeListKayit.getLength(); j++)
                    {
                        if (nodeListKayit.item(j).getAttributes().getNamedItem(XML_DURUM).getNodeValue().equals(DURUM_YENI))
                        {
                            sonucYazilar = false;
                            break;
                        }
                    }
                }
                else if (nodeListParca.item(i).getNodeName().equals(XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeListParca.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        if (nodeListAltParca.item(j).getAttributes().getNamedItem(XML_DURUM).getNodeValue().equals(DURUM_YENI))
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
                    nodeParca.getAttributes().getNamedItem(XML_DURUM).setNodeValue(DURUM_TAMAMLANDI);
                    documentToFile();

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
            String idKategori = nodeParca.getAttributes().getNamedItem(XML_ID).getNodeValue();
            if (idKategori.equals("0"))
            {
                return false;
            }
            else
            {
                nodeParca.getAttributes().getNamedItem(XML_DURUM).setNodeValue(DURUM_YENI);
                documentToFile();

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
        public Element kayitYeni(String kayitID)
        {
            if (!kayitID.equals("-1"))
            {
                Element elementKayit = document.getElementById(String.valueOf(kayitID));
                elementKayit.setAttribute(XML_DURUM, DURUM_YENI);

                documentToFile();

                Toast.makeText(getActivity(), "yeni olarak isaretlendi", Toast.LENGTH_SHORT).show();

                return elementKayit;
            }
            else
            {
                return null;
            }
        }

        public void kayitDegistir()
        {
            if (!xmlKayitID.equals("-1"))
            {
                Element elementKayit = document.getElementById(String.valueOf(xmlKayitID));
                elementKayit.setTextContent(etDegisecek.getText().toString());

                documentToFile();
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
        public Element kayitTamamla(String kayitID)
        {
            if (!kayitID.equals("-1"))
            {
                Element elementKayit = document.getElementById(String.valueOf(kayitID));
                elementKayit.setAttribute(XML_DURUM, DURUM_TAMAMLANDI);

                documentToFile();

                Toast.makeText(getActivity(), "tamamlandi olarak isaretlendi", Toast.LENGTH_SHORT).show();

                return elementKayit;
            }
            else
            {
                Log.d(TAG, "hata");
                return null;
            }
        }

        //duruma göre actionbarDegistirdeki simgeleri gösterir, gizler
        public void actionBarDegistirSimgeDurumu(String durum)
        {
            switch (durum)
            {
                case DURUM_TAMAMLANDI:
                    menuActionBar.findItem(R.id.action_degistir_tamamlandi).setVisible(false);
                    menuActionBar.findItem(R.id.action_degistir_yeni).setVisible(true);
                    break;

                case DURUM_YENI:
                    menuActionBar.findItem(R.id.action_degistir_tamamlandi).setVisible(true);
                    menuActionBar.findItem(R.id.action_degistir_yeni).setVisible(false);
                    break;

                default:
                    Log.d(TAG, "hata");
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

        public void klavyeKapat(Context c, IBinder windowToken)
        {
            InputMethodManager mgr = (InputMethodManager) fAct.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (getActivity() == null)
            {
                if (activityRootView.getRootView().getHeight() / 2 > activityRootView.getHeight())//kullanıcı klavyeyi kapatmıs mi diye kontrol
                {
                    mgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                }
            }
            else
            {
                mgr.hideSoftInputFromWindow(windowToken, 0);
            }

            //InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            //mgr.hideSoftInputFromWindow(windowToken, 0);
            //mgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
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
            }
            catch (IOException e)
            {
            }
        }

        //xml dosyasını yedek klasörüne kopyalar
        public void xmlYedekle()
        {
            String zaman = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            //alertdialog un içindeki ana LinearLayout
            LinearLayout alertLL = new LinearLayout(getActivity());
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            alertLL.setLayoutParams(pa);
            alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
            alertLL.setWeightSum(1f);

            //yazının yazılacagı EditText
            final EditText alertET = new EditText(getActivity());
            LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            alertET.setLayoutParams(pa2);
            alertET.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
            alertET.setText(zaman);
            alertLL.addView(alertET);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Yedek Adı");
            builder.setView(alertLL);

            builder.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
            builder.setNegativeButton("İptal", null);
            final AlertDialog alert = builder.create();
            alert.show();

            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String yedekAdi = alertET.getText().toString();
                    if (yedekAdi.isEmpty())//edittext boşken tamam'a tıklandı
                    {
                        Toast.makeText(getActivity(), "Yedek adı boş olamaz", Toast.LENGTH_LONG).show();
                    }
                    else//anaLayout'a yeni alan ekliyor
                    {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        alert.dismiss();

                        dosyaKopyala(xmlDosyaYolu, xmlYedekKlasorYolu + "/" + alertET.getText() + ".xml");
                    }
                }
            });
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    alert.dismiss();
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
                String a = file[i].getName().substring(file[i].getName().length() - 4);
                if (a.equals(".xml"))//yedek dosyalarının uzantıları xml olmalı
                {
                    String b = file[i].getName().substring(0, file[i].getName().length() - 4);
                    yedekler.add(b);
                }
                else
                {
                    ekranaHataYazdir("11", "yedek dosyası uzantı hatası");
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

                    LinearLayout alertLL2 = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams pa22 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    alertLL2.setLayoutParams(pa22);
                    alertLL2.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
                    alertLL2.setWeightSum(1f);

                    final TextView alertTV2 = new TextView(getActivity());
                    LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                    alertTV2.setLayoutParams(pa2);
                    alertTV2.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
                    alertTV2.setText(vv.getText() + " Yedek dosyası yüklensin mi ?");
                    alertLL2.addView(alertTV2);

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setTitle("Onay");
                    builder2.setView(alertLL2);
                    builder2.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
                    builder2.setNegativeButton("İptal", null);

                    final AlertDialog alert2 = builder2.create();
                    alert2.show();

                    alert2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            File xmlDosyasi = new File(xmlDosyaYolu);
                            xmlDosyasi.delete();

                            dosyaKopyala(xmlYedekKlasorYolu + "/" + vv.getText() + ".xml", xmlDosyaYolu);

                            anaLayout.removeAllViews();
                            document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);
                            xmlParcaID = "0";
                            parseXml(xmlParcaID);
                            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                            getActivity().getActionBar().setTitle("/");

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
            getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceYedek(FRAGMENT_YEDEK_EKRANI), FRAGMENT_TAG).commit();
        }

        public void seciliYedekDosylariniSil()
        {
            for (int i = 0; i < listSeciliYedek.size(); i++)
            {
                String dosya = xmlYedekKlasorYolu + "/" + listSeciliYedek.get(i).getIsim() + ".xml";
                File yedekDosya = new File(dosya);

                if (!yedekDosya.delete())
                {
                    ekranaHataYazdir("1", "dosya silinirken hata");
                }
                anaLayout.removeView(listSeciliYedek.get(i));
            }
            listSeciliYedek.clear();
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

        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            fAct = activity;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
            super.onCreateOptionsMenu(menu, inflater);

            inflaterActionBar = inflater;
            menuActionBar = menu;

            switch (ACTIONBAR_TUR)
            {
                case ACTIONBAR_EKLE:
                    inflater.inflate(R.menu.menu_ekle, menu);
                    break;
                case ACTIONBAR_SECIM:
                    inflater.inflate(R.menu.menu_secim, menu);
                    break;
                case ACTIONBAR_ONAY:
                    inflater.inflate(R.menu.menu_onay, menu);
                    break;
                case ACTIONBAR_DEGISTIR:
                    inflater.inflate(R.menu.menu_degistir, menu);
                    switch (KAYIT_DURUM_TUR)
                    {
                        case DURUM_TAMAMLANDI:
                            menu.findItem(R.id.action_degistir_tamamlandi).setVisible(false);
                            break;
                        case DURUM_YENI:
                            menu.findItem(R.id.action_degistir_yeni).setVisible(false);
                            break;
                        default:
                            Log.e("hata[141]", "KAYIT_DURUM_TUR hatalı : " + KAYIT_DURUM_TUR);
                            ekranaHataYazdir("141", "KAYIT_DURUM_TUR hatalı : " + KAYIT_DURUM_TUR);
                    }
                    break;
                case ACTIONBAR_YEDEK:
                    inflater.inflate(R.menu.menu_yedek, menu);
                    break;
                default:
                    Log.e("hata[142]", "ACTIONBAR_TUR hatalı : " + ACTIONBAR_TUR);
                    ekranaHataYazdir("142", "ACTIONBAR_TUR hatalı : " + ACTIONBAR_TUR);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.action_kategori_ekle:
                    kategoriKaydet();
                    return true;
                case R.id.action_kayit_ekle:
                    actionBarDegistir(ACTIONBAR_ONAY);
                    etEklenecek = yaziAlaniOlustur();
                    klavyeAc(getActivity().getApplicationContext(), etEklenecek);
                    return true;
                case R.id.action_onay_kaydet:
                    yaziyiKaydet(etEklenecek);
                    actionBarDegistir(ACTIONBAR_EKLE);
                    return true;
                case R.id.action_onay_iptal:
                    klavyeKapat(getActivity().getApplicationContext(), etEklenecek.getWindowToken());
                    anaLayout.removeView(etEklenecek);
                    actionBarDegistir(ACTIONBAR_EKLE);
                    return true;
                case R.id.action_secim_tamam:
                    seciliElemanlariTamamla();
                    return true;
                case R.id.action_secim_yeni:
                    seciliElemanlarYeni();
                    return true;
                case R.id.action_secim_sil:
                    seciliElemanlariSil();
                    return true;
                case R.id.action_degistir_sil:
                    kayitSilDiyaloguOlustur();
                    return true;
                case R.id.action_degistir_tamamlandi:
                    Element elementKayitTamam = kayitTamamla(xmlKayitID);
                    if (elementKayitTamam != null)
                    {
                        ustParcaDurumunuKontrolEtTamamla(elementKayitTamam);
                        actionBarDegistirSimgeDurumu(DURUM_TAMAMLANDI);
                    }
                    return true;
                case R.id.action_degistir_yeni:
                    Element elementKayitYeni = kayitYeni(xmlKayitID);
                    if (elementKayitYeni != null)
                    {
                        ustParcaDurumunuKontrolEtYeni(elementKayitYeni);
                        actionBarDegistirSimgeDurumu(DURUM_YENI);
                    }
                    return true;
                case R.id.action_degistir_kaydet:
                    kayitDegistir();
                    return true;
                case R.id.action_yedekle:
                    xmlYedekle();
                    return true;
                case R.id.action_yedekten_yukle:
                    xmlYedektenYukle();
                    return true;
                case R.id.action_yedekleri_goster:
                    actionBarDegistir(ACTIONBAR_YEDEK);
                    yedekDosyalariGoster();
                    return true;
                case R.id.action_yedek_sil:
                    seciliYedekDosylariniSil();
                    return true;
                case android.R.id.home:
                    ustSeviyeyiGetir();
                    klavyeKapat(fAct, null);
                    break;
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
            switch (getArguments().getInt(FRAGMENT_SECIM))
            {
                case FRAGMENT_KATEGORI_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    anaLayout = (LinearLayout) rootView.findViewById(R.id.anaLayout);
                    if (xmlEnBuyukID > 0)//xml de kayıt varsa ekrana eklesin
                    {
                        parseXml(xmlParcaID);
                        if (xmlParcaID.equals("0"))
                        {
                            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                            getActivity().getActionBar().setTitle("/");
                        }
                        else
                        {
                            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                            getActivity().getActionBar().setTitle(kategoriYolunuGetir(xmlParcaID));
                        }
                    }
                    return rootView;
                }
                case FRAGMENT_KAYIT_EKRANI:
                {
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    anaLayout = (LinearLayout) rootView.findViewById(R.id.anaLayout);

                    RelativeLayout rl = new RelativeLayout(getActivity());
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    etDegisecek = new EditText(getActivity());
                    etDegisecek.setText(getArguments().getString(FRAGMENT_YAZI));
                    etDegisecek.requestFocus();
                    rl.addView(etDegisecek, lp);
                    anaLayout.addView(rl);

                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    klavyeAc(getActivity().getApplicationContext(), etDegisecek);

                    return rootView;
                }
                case FRAGMENT_YEDEK_EKRANI:
                {
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    getActivity().getActionBar().setTitle("yedek dosyaları");

                    List<String> yedekler = yedekDosyalariniGetir();
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    anaLayout = (LinearLayout) rootView.findViewById(R.id.anaLayout);

                    for (int i = 0; i < yedekler.size(); i++)
                    {
                        final String yedekIsmi = yedekler.get(i);

                        yedekRelativeLayout yrl = new yedekRelativeLayout(getActivity(), yedekIsmi);
                        anaLayout.addView(yrl);
                    }
                    return rootView;
                }
                default:
                    return null;
            }
        }

        //kategori seciliyken duzenleye tıklanıldığı zaman girilen ismi xml'e kaydeder
        public void kategoriBaslikGuncelle(String baslik, int kategoriID)
        {
            Element element = document.getElementById(String.valueOf(kategoriID));
            Node nodeBaslik = element.getFirstChild();
            nodeBaslik.setTextContent(baslik);
            documentToFile();
        }

        public class yedekRelativeLayout extends RelativeLayout
        {
            private String isim;

            public yedekRelativeLayout(Context context, String isim)
            {
                super(context);
                this.isim = isim;

                layoutOlustur();
            }

            public void layoutOlustur()
            {
                int ID0 = 10000;
                final String yedekIsmi = this.getIsim();
                final RelativeLayout rl = this;

                CheckBox cb = new CheckBox(getActivity());
                cb.setId(ID0);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp1.addRule(RelativeLayout.CENTER_VERTICAL);
                rl.addView(cb, lp1);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {

                        if (b)
                        {
                            GradientDrawable gd = new GradientDrawable();
                            gd.setColor(0xFF00CED1);
                            rl.setBackground(gd);
                            listSeciliYedek.add((yedekRelativeLayout) rl);
                        }
                        else
                        {
                            GradientDrawable gd = new GradientDrawable();
                            gd.setColor(0x00000000);
                            rl.setBackground(gd);
                            listSeciliYedek.remove(listSeciliYedek.indexOf(rl));
                        }

                    }
                });

                final TextView tv = new TextView(getActivity());
                tv.setText(yedekIsmi);
                tv.setTextSize(20);
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp2.addRule(RelativeLayout.LEFT_OF, cb.getId());
                lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp2.addRule(RelativeLayout.CENTER_VERTICAL);
                rl.addView(tv, lp2);
                tv.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //alertdialog un içindeki ana LinearLayout
                        LinearLayout alertLL = new LinearLayout(getActivity());
                        LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                        alertLL.setLayoutParams(pa);
                        alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
                        alertLL.setWeightSum(1f);

                        File xmlDosyasi = new File(xmlYedekKlasorYolu + "/" + yedekIsmi + ".xml");
                        long olusturma = xmlDosyasi.lastModified();
                        Date date = new Date(olusturma);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = sdf.format(date);

                        //yazının yazılacagı EditText
                        TextView alertTV = new TextView(getActivity());
                        LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f);
                        alertTV.setLayoutParams(pa2);
                        alertTV.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
                        alertTV.setText("\nOluşturulma : " + formattedDate + "\nBoyut : " + xmlDosyasi.length());
                        alertLL.addView(alertTV);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(yedekIsmi);
                        builder.setView(alertLL);

                        builder.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
                        final AlertDialog alert = builder.create();
                        alert.show();

                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                alert.dismiss();
                            }
                        });
                    }
                });
            }

            public String getIsim()
            {
                return isim;
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        PlaceholderFragment fr = (PlaceholderFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (FRAGMENT_ETKIN_EKRAN == FRAGMENT_YEDEK_EKRANI)//yedek ekranında ise kategori ekranına dönsün
        {
            fr.parseXml(xmlParcaID);
        }
        else
        {
            if (xmlParcaID.equals("0"))//en ust seviyede ise uygulamadan çıksın
            {
                super.onBackPressed();
            }
            else
            {
                fr.ustSeviyeyiGetir();
            }
        }
    }
}
