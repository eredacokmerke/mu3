package com.example.ekcdr.uygulama3;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
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
    private static File xmlKlasorYolu;
    private static String xmlDosyaYolu;
    private static final String XML_PARCA = "parca";
    private static final String XML_RENK = "renk";
    private static final String XML_BASLIK = "baslik";
    private static final String XML_YAZILAR = "yazilar";
    private static final String XML_ROOT = "root";
    private static final String XML_KAYIT = "kayit";
    private static final String XML_ALTPARCA = "altparca";
    private static final String XML_ID = "id";
    private static final String DURUM_YENI = "0";
    private static final String DURUM_TAMAMLANDI = "1";
    private static final String XML_DURUM = "durum";
    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final int ELEMAN_TUR_KAYIT = 0;
    private static final int ELEMAN_TUR_KATEGORI = 1;
    private static final String FRAGMENT_SECIM = "fragment_secim";
    private static final String FRAGMENT_YAZI = "fragment_yazi";
    private static final int ACTIONBAR_EKLE = 0;
    private static final int ACTIONBAR_ONAY = 1;
    private static final int ACTIONBAR_SECIM = 2;
    private static final int ACTIONBAR_DEGISTIR = 3;
    private static int ACTIONBAR_TUR = ACTIONBAR_EKLE;
    private static final int FRAGMENT_KATEGORI_EKRANI = 0;
    private static final int FRAGMENT_KAYIT_EKRANI = 1;
    private static int FRAGMENT_ETKIN_EKRAN;
    private static final int OLAY_ICINE_GIR = 0;
    private static final int OLAY_SECIM_YAP = 1;
    private static int TIKLAMA_OLAYI;
    private static final String ACTIONBAR_ARKAPLAN_KATEGORI = "#00CED1";
    private static final String ACTIONBAR_ARKAPLAN_KAYIT = "#009ED1";
    private static final String ACTIONBAR_ARKAPLAN_SECILI = "#FF2222";
    private static final String UC_NOKTA = "/.../";
    private static String KAYIT_DURUM_TUR;
    private static Resources resources;
    private static float px7;
    private static int px2;
    private static String xmlKayitID = "-1";//içine girilen kayit id si
    private static String xmlParcaID = "0";//içinde olunan parçanın id si
    private static int xmlEnBuyukID;//eklenen kategori ve kayıtlara id verebilmek için
    private static final double ORAN_DIKEY = 0.3;
    private static final double ORAN_YATAY = 0.6;
    private static DisplayMetrics displayMetrics;
    private static ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String UYGULAMA_ADI = "uygulama3";

        if (hariciAlanVarMi())//sdcard var
        {
            xmlKlasorYolu = new File(Environment.getExternalStorageDirectory().getPath() + "/" + UYGULAMA_ADI);
        }
        else//sdcard yok
        {
            xmlKlasorYolu = getDir(UYGULAMA_ADI, Context.MODE_PRIVATE);
        }
        if (!xmlKlasorYolu.exists())
        {
            xmlKlasorYolu.mkdirs();
        }
        xmlDosyaYolu = xmlKlasorYolu + "/" + "new.xml";
        xmlDosyasiKontrolEt();

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, 0), FRAGMENT_TAG).commit();
        }

        bar = getActionBar();
        actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);

        resources = getResources();
        px7 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, resources.getDisplayMetrics());
        px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, resources.getDisplayMetrics());
        displayMetrics = resources.getDisplayMetrics();
    }

    //xml dosyası var mı diye kontrol ediyor. yoksa oluşturuyor ve en buyuk xml id sini buluyor
    public void xmlDosyasiKontrolEt()
    {
        File xmlDosyasi = new File(xmlDosyaYolu);
        if (xmlKlasorYolu.exists())//klasör var
        {
            if (xmlDosyasi.exists())//dosya sistemde mevcut
            {
                xmlEnBuyukID = enBuyukIDyiBul();
                if (xmlEnBuyukID == -1)
                {
                    ekranaHataYazdir("1", "xml okunamadı");
                }
            }
            else//dosya yok
            {
                xmlEnBuyukID = 0;
                xmlDosyasiOlustur();
            }
        }
        else//klasör yok
        {
            xmlKlasorYolu.mkdirs();
            xmlEnBuyukID = 0;
            xmlDosyasiOlustur();
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
        Log.d("proje32", "xmlDosyaYolu : " + xmlDosyaYolu);
        try
        {
            int sonIDParca = 0;
            int sonIDKayit = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

            NodeList nodeListParca = doc.getElementsByTagName(XML_PARCA);
            NodeList nodeListKayit = doc.getElementsByTagName(XML_KAYIT);

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
        catch (IOException e)
        {
            Log.e("hata[1]", e.getMessage());
            ekranaHataYazdir("1", e.getMessage());
            return -1;
        }
        catch (ParserConfigurationException e)
        {
            Log.e("hata[2]", e.getMessage());
            ekranaHataYazdir("2", e.getMessage());
            return -1;
        }
        catch (SAXException e)
        {
            Log.e("hata[28]", e.getMessage());
            ekranaHataYazdir("28", e.getMessage());
            return -1;
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

    public static void actionBarArkaPlanDegistir(String renk)
    {
        ColorDrawable actionBarArkaPlan = new ColorDrawable(Color.parseColor(renk));
        bar.setBackgroundDrawable(actionBarArkaPlan);
    }

    public static class PlaceholderFragment extends Fragment
    {
        private LinearLayout anaLayout;//viewların içine yerleşeceği ana layout
        private MenuInflater inflaterActionBar;
        private Menu menuActionBar;
        private EditText etEklenecek;//yeni kayıt eklemeye tıklandığı zaman olusan edittext
        private static List<Integer> listSeciliKategori;//seçilen kategorilerin listesi
        private static List<Integer> listSeciliKayit;//seçilen kayıtların listesi
        private String TAG = "uyg3";

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

            KAYIT_DURUM_TUR = durum;

            return fragment;
        }

        public void ekranaHataYazdir(String id, String hata)
        {
            Toast.makeText(getActivity(), "hata[" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
        }

        //parca etiketinin altındaki yazi ve kategorileri ekrana basıyor
        public boolean parseXml(String parcaID)
        {
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));
                Element element = doc.getElementById(parcaID);
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

                            kategorileriAnaEkranaEkle(kategoriYazi, Integer.parseInt(kategoriID), kategoriDurum);
                        }
                    }
                }

                return true;
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[1510]", "ust seviye hatasi");
                ekranaHataYazdir("1510", "ust seviye hatasi");
                return false;
            }
            catch (FileNotFoundException e)
            {
                Log.e("hata[1511]", "ust seviye hatasi");
                ekranaHataYazdir("1511", "ust seviye hatasi");
                return false;
            }
            catch (SAXException e)
            {
                Log.e("hata[1512]", "ust seviye hatasi");
                ekranaHataYazdir("1512", "ust seviye hatasi");
                return false;
            }
            catch (IOException e)
            {
                Log.e("hata[1513]", "ust seviye hatasi");
                ekranaHataYazdir("1513", "ust seviye hatasi");
                return false;
            }
        }

        //xml parse edildikten sonra kayitları ana ekrana ekler
        public void kayitlariAnaEkranaEkle(final String yazi, final int eklenenID, final String durum)
        {
            final customRelativeLayout crl = new customRelativeLayout(getActivity(), yazi, ELEMAN_TUR_KAYIT);
            crl.setCstID(eklenenID);
            crl.setId(eklenenID);
            //crl.setCstSeciliMi(false);
            if (durum.equals(DURUM_TAMAMLANDI))
            {
                crl.getTvTik().setText("\u2714");
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
                    if (!crl.isCstSeciliMi())//secili eleman tekrar secilemesin
                    {
                        listSeciliKayit.add(eklenenID);
                        crl.arkaplanSecili();
                        crl.setCstSeciliMi(true);
                        actionBarDegistir(ACTIONBAR_SECIM);
                        TIKLAMA_OLAYI = OLAY_SECIM_YAP;

                        actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_SECILI);
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
                        if (crl.isCstSeciliMi())
                        {
                            listSeciliKayit.remove(listSeciliKayit.indexOf(eklenenID));
                            crl.arkaplanKayit();
                            crl.setCstSeciliMi(false);

                            if (seciliElemanSayisi() == 0)
                            {
                                actionBarDegistir(ACTIONBAR_EKLE);
                                TIKLAMA_OLAYI = OLAY_ICINE_GIR;

                                actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
                            }
                        }
                        else
                        {
                            listSeciliKayit.add(eklenenID);
                            crl.arkaplanSecili();
                            crl.setCstSeciliMi(true);
                        }
                    }
                }
            });
            anaLayout.addView(crl);
        }

        public String kategoriYolunuGetir(String kategoriID)
        {
            String baslik = "";
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));
                Element elementParca = doc.getElementById(kategoriID);
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
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
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

            catch (ParserConfigurationException e)
            {
                return "";
            }
            catch (FileNotFoundException e)
            {
                return "";
            }
            catch (IOException e)
            {
                return "";
            }
            catch (SAXException e)
            {
                return "";
            }
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        //public void kategorileriAnaEkranaEkle(final String baslik, final String staticrenk, final String yazi, boolean cerceve, final int kategoriID)
        public void kategorileriAnaEkranaEkle(final String baslik, final int kategoriID, String durum)
        {
            final customRelativeLayout crl = new customRelativeLayout(getActivity(), baslik, ELEMAN_TUR_KATEGORI);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
            crl.setCstID(kategoriID);
            crl.setId(kategoriID);
            if (durum.equals(DURUM_TAMAMLANDI))
            {
                crl.getTvTik().setText("\u2714");
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
                        if (crl.isCstSeciliMi())
                        {
                            listSeciliKategori.remove(listSeciliKategori.indexOf(kategoriID));
                            crl.arkaplanKategori();
                            crl.setCstSeciliMi(false);

                            if (seciliElemanSayisi() == 0)
                            {
                                actionBarDegistir(ACTIONBAR_EKLE);
                                TIKLAMA_OLAYI = OLAY_ICINE_GIR;

                                actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
                            }
                        }
                        else
                        {
                            listSeciliKategori.add(kategoriID);
                            crl.arkaplanSecili();
                            crl.setCstSeciliMi(true);
                        }
                    }
                }
            });
            anaLayout.addView(crl);
            crl.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if (!crl.isCstSeciliMi())//secili eleman tekrar secilemesin
                    {
                        listSeciliKategori.add(kategoriID);
                        crl.arkaplanSecili();
                        crl.setCstSeciliMi(true);
                        actionBarDegistir(ACTIONBAR_SECIM);
                        TIKLAMA_OLAYI = OLAY_SECIM_YAP;

                        actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_SECILI);
                    }

                    return true;
                }
            });
        }

        //Document nesnesini dosyaya yazıyor
        public void documentToFile(Document doc)
        {
            try
            {
                //document i string e çeviriyor
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(doc), new StreamResult(writer));
                String output = "<?xml version=\"1.0\"?>" + writer.getBuffer().toString().replaceAll("\n|\r", "");
                ///////////////////////////////7
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
            try
            {
                //if (txtIsaretci.getText().equals(XML_ROOT))
                /*
                if (false)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Node nodeRoot = doc.getElementsByTagName(XML_ROOT).item(0);//ilk görülen root tagını alıyor
                    Element yeniNodeParca = doc.createElement(XML_PARCA);//parca isimli elemanı oluşturuyor
                    yeniNodeParca.setAttribute(XML_ID, String.valueOf(xmlEnBuyukID));//parca icinde id isimli özellik oluşturuluyor
                    nodeRoot.appendChild(yeniNodeParca);//root etiketine parca etiketi ekleniyor

                    Node nodeParca = doc.getElementById(String.valueOf(xmlEnBuyukID));//xmlid id sine sahip eleman alınıyor. üstte oluşturulan parca etiketi
                    Element yeniNodeBaslik = doc.createElement(XML_BASLIK);//baslik isimli etiket oluşturuluyor
                    yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri ekleniyor
                    Element yeniNodeRenk = doc.createElement(XML_RENK);//renk isimli etiket oluşturuluyor
                    yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri ekleniyor
                    Element yeniNodeYazi = doc.createElement(XML_YAZILAR);//yazi isimli etiket oluşturuluyor
                    Element yeniNodeAltparca = doc.createElement(XML_ALTPARCA);//altparca isimli etiket oluşturuluyor
                    nodeParca.appendChild(yeniNodeBaslik);//parca etiketine baslik etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeRenk);//parca etiketine renk etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeYazi);//parca etiketine yazi etiketi ekleniyor
                    nodeParca.appendChild(yeniNodeAltparca);//parca etiketine altparca etiketi ekleniyor

                    doc.normalize();
                    documentToFile(doc);
                }
                else
                {
                    */
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                Node nodeMevcutParca = doc.getElementById(String.valueOf(xmlParcaID));//içinde bulunulan parcaya giriyor
                NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                {
                    if (nodeParcaCocuklari.item(i).getNodeName().equals(XML_ALTPARCA))//parcanın içindeki altparca etiketine ulaşılıyor
                    {
                        Node nodeAltparca = nodeParcaCocuklari.item(i);//altparcaya giriliyor
                        Element yeniNodeParca = doc.createElement(XML_PARCA);//parca isimli etiket olşuturuluyor
                        yeniNodeParca.setAttribute(XML_ID, String.valueOf(xmlEnBuyukID));//parca ya id özelliği ekleniyor
                        yeniNodeParca.setAttribute(XML_DURUM, DURUM_YENI);//parca ya id özelliği ekleniyor
                        nodeAltparca.appendChild(yeniNodeParca);//altparca etiketine parca ekleniyor

                        Node nodeParca = doc.getElementById(String.valueOf(xmlEnBuyukID));//xmlid id sine sahip parca nın içine giriliyor. az önce oluşturulan parca
                        Element yeniNodeBaslik = doc.createElement(XML_BASLIK);//baslik etiketi oluşturuluyor
                        yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri giriliyor
                        Element yeniNodeRenk = doc.createElement(XML_RENK);//renk etiketi oluşturuluyor
                        yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri giriliyor
                        Element yeniNodeYazi = doc.createElement(XML_YAZILAR);//yazi etiketi oluşturuluyor
                        Element yeniNodeAltparca = doc.createElement(XML_ALTPARCA);//altparca etiketi oluşturuluyor
                        nodeParca.appendChild(yeniNodeBaslik);//parca etiketine baslik etiketi ekleniyor
                        nodeParca.appendChild(yeniNodeRenk);//parca etiketine renk etiketi ekleniyor
                        nodeParca.appendChild(yeniNodeYazi);//parca etiketine yazi etiketi ekleniyor
                        nodeParca.appendChild(yeniNodeAltparca);//parca etiketine altparca etiketi ekleniyor
                        break;
                    }
                }
                doc.normalize();
                documentToFile(doc);

                return xmlEnBuyukID;
                //}
            }
            catch (IOException e)
            {
                Log.e("hata[4]", e.getMessage());
                ekranaHataYazdir("4", e.getMessage());

                return -1;
            }
            catch (SAXException e)
            {
                Log.e("hata[5]", e.getMessage());
                ekranaHataYazdir("5", e.getMessage());

                return -1;
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[6]", e.getMessage());
                ekranaHataYazdir("6", e.getMessage());

                return -1;
            }
        }

        //listSeciliKategori ve listSeciliKayit'yi sıfırlar ve actionbar ı ilk haline döndürür
        public void seciliElemanListeleriniSifirla()
        {
            /*
            listSeciliKategori.clear();
            listSeciliKayit.clear();
            actionBarIlk();
            */
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

                        final customRelativeLayout crl = new customRelativeLayout(getActivity(), kategoriAdi, ELEMAN_TUR_KATEGORI);
                        crl.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                if (TIKLAMA_OLAYI == OLAY_ICINE_GIR)
                                {
                                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, xmlEnBuyukID), FRAGMENT_TAG).commit();
                                    actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
                                }
                                else if (TIKLAMA_OLAYI == OLAY_SECIM_YAP)
                                {
                                    if (crl.isCstSeciliMi())
                                    {
                                        listSeciliKategori.remove(listSeciliKategori.indexOf(xmlEnBuyukID));
                                        crl.arkaplanKategori();
                                        crl.setCstSeciliMi(false);

                                        if (seciliElemanSayisi() == 0)
                                        {
                                            actionBarDegistir(ACTIONBAR_EKLE);
                                            TIKLAMA_OLAYI = OLAY_ICINE_GIR;

                                            actionBarArkaPlanDegistir(ACTIONBAR_ARKAPLAN_KATEGORI);
                                        }
                                    }
                                    else
                                    {
                                        listSeciliKategori.add(xmlEnBuyukID);
                                        crl.arkaplanSecili();
                                        crl.setCstSeciliMi(true);
                                    }
                                }
                            }
                        });
                        anaLayout.addView(crl);
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
                {
                    FragmentManager fm = getFragmentManager();

                    if (fm.getBackStackEntryCount() > 0)
                    {
                        fm.popBackStackImmediate();
                        xmlKayitID = "-1";
                    }
                    FRAGMENT_ETKIN_EKRAN = FRAGMENT_KATEGORI_EKRANI;
                    ACTIONBAR_TUR = ACTIONBAR_EKLE;

                    break;
                }
                case FRAGMENT_KATEGORI_EKRANI:
                {
                    try
                    {
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        dbf.setValidating(false);
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                        Element element = doc.getElementById(xmlParcaID);
                        String ustSeviyeID = element.getParentNode().getParentNode().getAttributes().getNamedItem(XML_ID).getNodeValue();

                        xmlParcaID = ustSeviyeID;
                        getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlParcaID)), FRAGMENT_TAG).commit();
                    }
                    catch (FileNotFoundException e)
                    {
                        Log.e("hata[20]", e.getMessage());
                        ekranaHataYazdir("20", e.getMessage());
                    }
                    catch (ParserConfigurationException e)
                    {
                        Log.e("hata[21]", e.getMessage());
                        ekranaHataYazdir("21", e.getMessage());
                    }
                    catch (IOException e)
                    {
                        Log.e("hata[22]", e.getMessage());
                        ekranaHataYazdir("22", e.getMessage());
                    }
                    catch (SAXException e)
                    {
                        Log.e("hata[23]", e.getMessage());
                        ekranaHataYazdir("23", e.getMessage());
                    }

                    break;
                }
                default:
                    Log.d(TAG, "default : xmlParcaID : " + xmlParcaID);
            }
        }

        //kayit ekle tusuna basıldıktan sonra açılan edittext'e yazılan yazıyı xml'e ekler
        public void yaziyiKaydet(EditText et)
        {
            xmlEnBuyukID++;
            try
            {
                String alanYazi = et.getText().toString();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                kategoriDurumunuGuncelle(doc, xmlParcaID, DURUM_YENI);

                Node nodeMevcutParca = doc.getElementById(String.valueOf(xmlParcaID));//içinde bulunulan parcaya giriyor
                NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                {
                    if (nodeParcaCocuklari.item(i).getNodeName().equals(XML_YAZILAR))//parcanın içindeki yazilar etiketine ulaşılıyor
                    {
                        int eklenenID = xmlEnBuyukID;
                        Element yeniNodeKayit = doc.createElement(XML_KAYIT);//kayıt etiketi olusturuyor
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
                doc.normalize();
                documentToFile(doc);
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[16]", e.getMessage());
                ekranaHataYazdir("16", e.getMessage());
            }
            catch (FileNotFoundException e)
            {
                Log.e("hata[17]", e.getMessage());
                ekranaHataYazdir("17", e.getMessage());
            }
            catch (IOException e)
            {
                Log.e("hata[18]", e.getMessage());
                ekranaHataYazdir("18", e.getMessage());
            }
            catch (SAXException e)
            {
                Log.e("hata[19]", e.getMessage());
                ekranaHataYazdir("19", e.getMessage());
            }
        }

        //sil tusuna basıldığı zaman secili elemanları siler
        public void seciliElemanlariSil()
        {
            /*
            switch (FRAGMENT_ETKIN_EKRAN)
            {
                case FRAGMENT_KAYIT_EKRANI:

                    kayitSil();
                    ustSeviyeyiGetir();

                    break;

                case FRAGMENT_KATEGORI_EKRANI:

                    break;

                default:
            }
            */

            /*
            if (listSeciliKategori.isEmpty() && listSeciliKayit.isEmpty())
            {
                Toast.makeText(getActivity(), "Seçim yapılmadı", Toast.LENGTH_LONG).show();
            }
            else
            {
                String soru = "Kayıtlar silinsin mi ?";
                if (!listSeciliKategori.isEmpty())
                {
                    soru = "Kategori içindeki bütün kayıtlar silinsin mi ?";
                }

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
                        yaziSil(listSeciliKayit);
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
            */
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

        public void kayitSil()
        {
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                Element element = doc.getElementById(xmlKayitID);
                element.getParentNode().removeChild(element);
                doc.normalize();
                documentToFile(doc);

                xmlKayitID = "-1";
            }
            catch (FileNotFoundException e)
            {
                Log.e("hata[20]", e.getMessage());
                ekranaHataYazdir("20", e.getMessage());
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[21]", e.getMessage());
                ekranaHataYazdir("21", e.getMessage());
            }
            catch (IOException e)
            {
                Log.e("hata[22]", e.getMessage());
                ekranaHataYazdir("22", e.getMessage());
            }
            catch (SAXException e)
            {
                Log.e("hata[23]", e.getMessage());
                ekranaHataYazdir("23", e.getMessage());
            }
        }

        public void kategoriSil(List<Integer> listeSilinecek)
        {
            try
            {
                for (int i = 0; i < listeSilinecek.size(); i++)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Element element = doc.getElementById(String.valueOf(listeSilinecek.get(i)));
                    element.getParentNode().removeChild(element);
                    doc.normalize();
                    documentToFile(doc);

                    customRelativeLayout view = (customRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                    anaLayout.removeView(view);
                }
            }
            catch (FileNotFoundException e)
            {
                Log.e("hata[20]", e.getMessage());
                ekranaHataYazdir("20", e.getMessage());
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[21]", e.getMessage());
                ekranaHataYazdir("21", e.getMessage());
            }
            catch (IOException e)
            {
                Log.e("hata[22]", e.getMessage());
                ekranaHataYazdir("22", e.getMessage());
            }
            catch (SAXException e)
            {
                Log.e("hata[23]", e.getMessage());
                ekranaHataYazdir("23", e.getMessage());
            }
        }

        public void yaziSil(List<Integer> listeSilinecek)
        {
            try
            {
                for (int i = 0; i < listeSilinecek.size(); i++)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Element element = doc.getElementById(String.valueOf(listeSilinecek.get(i)));
                    element.getParentNode().removeChild(element);
                    doc.normalize();
                    documentToFile(doc);

                    customRelativeLayout view = (customRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                    anaLayout.removeView(view);
                    //customTextView view = (customTextView) anaLayout.findViewById(listeSilinecek.get(i));
                    //anaLayout.removeView(view);
                }
            }
            catch (FileNotFoundException e)
            {
                Log.e("hata[20]", e.getMessage());
                ekranaHataYazdir("20", e.getMessage());
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[21]", e.getMessage());
                ekranaHataYazdir("21", e.getMessage());
            }
            catch (IOException e)
            {
                Log.e("hata[22]", e.getMessage());
                ekranaHataYazdir("22", e.getMessage());
            }
            catch (SAXException e)
            {
                Log.e("hata[23]", e.getMessage());
                ekranaHataYazdir("23", e.getMessage());
            }
        }

        //secili kayıtları ve kategorilerin altındaki kayıtları tamamlandı olarak isaretler
        public void seciliElemanlariTamamla()
        {
            /*
            kayitTamamla(listSeciliKayit);
            kategoriTamamla(listSeciliKategori);
            seciliElemanListeleriniSifirla();
            */
        }

        public void kategorininButunCocuklariniGetir(XmlPullParser parser, int xmlEtiketNesil2)
        {
            try
            {
                boolean bulundu = false;
                int eventType = parser.getEventType();
                String tagName = parser.getName();
                //Log.d(TAG, "tagName 1 : " + tagName + " eventType : " + eventType);

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    parser.next();
                    eventType = parser.getEventType();
                    tagName = parser.getName();

                    if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_YAZILAR) && parser.getDepth() == xmlEtiketNesil2 + 1)
                    {
                        //Log.d(TAG, "kayit bulundu");
                        bulundu = true;
                        break;
                    }
                }

                if (bulundu)
                {
                    while (!(eventType == XmlPullParser.END_TAG && tagName.equals(XML_YAZILAR) && parser.getDepth() == xmlEtiketNesil2 + 1))
                    {
                        parser.next();
                        eventType = parser.getEventType();
                        tagName = parser.getName();

                        if (eventType == XmlPullParser.START_TAG)
                        {
                            if (tagName.equals(XML_KAYIT) && parser.getDepth() == xmlEtiketNesil2 + 2)
                            {
                                String kayitID = parser.getAttributeValue(null, XML_ID);
                                Log.d(TAG, "qq kayitID : " + kayitID);

                                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                                dbf.setValidating(false);
                                DocumentBuilder db = dbf.newDocumentBuilder();
                                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                                Element elementKayit = doc.getElementById(kayitID);
                                elementKayit.setAttribute(XML_DURUM, DURUM_TAMAMLANDI);

                                doc.normalize();
                                documentToFile(doc);
                            }
                        }
                    }
                }

                parser.next();
                eventType = parser.getEventType();
                tagName = parser.getName();

                boolean bulunduAltParca = false;
                if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_ALTPARCA) && parser.getDepth() == xmlEtiketNesil2 + 1)
                {
                    //Log.d(TAG, "qqq alt parca bulundu");
                    bulunduAltParca = true;
                    //break;
                }

                if (bulunduAltParca)
                {
                    while (!(eventType == XmlPullParser.END_TAG && tagName.equals(XML_ALTPARCA) && parser.getDepth() == xmlEtiketNesil2 + 1))
                    {
                        parser.next();
                        eventType = parser.getEventType();
                        tagName = parser.getName();

                        if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_PARCA) && parser.getDepth() == xmlEtiketNesil2 + 2)
                        {
                            String kategoriID = parser.getAttributeValue(null, XML_ID);
                            Log.d(TAG, "qqq parca bulundu : id : " + kategoriID);
                            kategorininButunCocuklariniGetir(parser, xmlEtiketNesil2 + 2);
                            //sonuc = sonuc && xmlKategoriBasliginiGetir(parser, kategoriID);
                        }
                    }
                    //return sonuc;
                }
            }
            catch (SAXException e)
            {
            }
            catch (ParserConfigurationException e)
            {
            }
            catch (IOException e)
            {
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[141]", e.getMessage());
                ekranaHataYazdir("149", e.getMessage());
            }
        }

        /*
        //kategorinin butun alt kayıtlarını tamamlandı olarak isaretler
        public void kategoriTamamla(List<Integer> listeSilinecek)
        {
            try
            {
                for (int i = 0; i < listeSilinecek.size(); i++)
                {
                    XmlPullParser parser;
                    parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(fileToIS(), null);
                    parser.next();

                    int xmlEtiketNesil2 = xmlEtiketNesil + 2;

                    if (xmlDogruParcayiBul(parser, String.valueOf(listeSilinecek.get(i)), xmlEtiketNesil2))
                    {
                        kategoriDurumunuGuncelle(String.valueOf(listeSilinecek.get(i)), DURUM_TAMAMLANDI);
                        kategorininButunCocuklariniGetir(parser, xmlEtiketNesil2);
                    }

                    customRelativeLayout crl = (customRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                    crl.getTvTik().setText("\u2714");
                    //crl.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kategori));
                    arkaplanKategori(crl);
                    crl.setCstSeciliMi(false);
                }
            }

            catch (FileNotFoundException e)
            {
            }
            catch (IOException e)
            {
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[141]", e.getMessage());
                ekranaHataYazdir("141", e.getMessage());
            }
        }
        */

        public void kategoriDurumunuGuncelle(Document doc, String id, String durum)
        {
            Element elementKayit = doc.getElementById(id);
            elementKayit.setAttribute(XML_DURUM, durum);
        }

        public void kategoriDurumunuGuncelle(String id, String durum)
        {
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                Element elementKayit = doc.getElementById(id);
                elementKayit.setAttribute(XML_DURUM, durum);

                doc.normalize();
                documentToFile(doc);
            }
            catch (ParserConfigurationException e)
            {
            }
            catch (FileNotFoundException e)
            {
            }
            catch (IOException e)
            {
            }
            catch (SAXException e)
            {
            }
        }

        //parcanın yazilarini ve altparcalarını kontrol eder. hepsi tamamlanmiş ise parcayı tamamlandı olarak işaretler
        public boolean parcayiIsaretleTamamlandi(Node nodeParca, Document doc)
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
                if (nodeListParca.item(i).getNodeName().equals(XML_ALTPARCA))
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

                    doc.normalize();
                    documentToFile(doc);

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        //yeni olarak işaretlenen kaydin ust parcalarını da yeni olarak işaretler
        public boolean parcayiIsaretleYeni(Node nodeParca, Document doc)
        {
            String idKategori = nodeParca.getAttributes().getNamedItem(XML_ID).getNodeValue();
            if (idKategori.equals("0"))
            {
                return false;
            }
            else
            {
                nodeParca.getAttributes().getNamedItem(XML_DURUM).setNodeValue(DURUM_YENI);

                doc.normalize();
                documentToFile(doc);

                return true;
            }
        }

        /*
        //yazilar altındaki kayitların durum degerlerini kontrol eder
        public boolean kayitDurumunuKontrolEt(Node nodeYazilar, String durum)
        {
            NodeList nodeList = nodeYazilar.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getAttributes().getNamedItem(XML_DURUM).getNodeValue().equals(durum))
                {
                    return false;
                }
            }
            return true;
        }

        //altparca içindeki parcaların durum degerlerini kontrol eder
        public boolean altParcaDurumunuKontrolEt(Node nodeParca, String durum)
        {
            NodeList nodeList = nodeParca.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeName().equals(XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        if (nodeListAltParca.item(j).getAttributes().getNamedItem(XML_DURUM).getNodeValue().equals(durum))
                        {
                            return false;
                        }
                    }
                    break;
                }
            }
            return true;
        }
        */

        //secilen kaydin durumunu yeni olarak değiştirir
        public void kayitYeni()
        {
            if (!xmlKayitID.equals("-1"))
            {
                try
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Element elementKayit = doc.getElementById(String.valueOf(xmlKayitID));
                    elementKayit.setAttribute(XML_DURUM, DURUM_YENI);

                    doc.normalize();
                    documentToFile(doc);

                    boolean sonuc;
                    Node nodeParca = elementKayit.getParentNode().getParentNode();
                    sonuc = parcayiIsaretleYeni(nodeParca, doc);

                    while (sonuc)
                    {
                        nodeParca = nodeParca.getParentNode().getParentNode();
                        sonuc = parcayiIsaretleYeni(nodeParca, doc);
                    }

                    /*
                    //bütün kayıtlar tamamlandi olarak isaretlenmis ise kategorinin içindeki kategorilere baksın
                    if (kayitDurumunuKontrolEt(elementKayit.getParentNode(), DURUM_TAMAMLANDI))
                    {
                        //altparca içindeki kategoriler tamamlandi olarak isaretlenmis ise içinde bulunulan kategori de tamamlandi olarak isaretlensi
                        if (altParcaDurumunuKontrolEt(elementKayit.getParentNode().getParentNode(), DURUM_TAMAMLANDI))
                        {
                            Node nodeKategori = elementKayit.getParentNode().getParentNode();
                            String idKategori = nodeKategori.getAttributes().getNamedItem(XML_ID).getNodeValue();
                            kategoriDurumunuGuncelle(doc, idKategori, DURUM_YENI);
                        }
                    }
                    doc.normalize();
                    documentToFile(doc);
                    */

                    Toast.makeText(getActivity(), "yeni olarak isaretlendi", Toast.LENGTH_SHORT).show();

                    menuActionBar.findItem(R.id.action_degistir_tamamlandi).setVisible(true);
                    menuActionBar.findItem(R.id.action_degistir_yeni).setVisible(false);
                }
                catch (ParserConfigurationException e)
                {
                }
                catch (FileNotFoundException e)
                {
                }
                catch (IOException e)
                {
                }
                catch (SAXException e)
                {
                }
            }
        }

        //secilen kaydin durumunu tamamlandı olarak değiştirir
        public void kayitTamamla()
        {
            if (!xmlKayitID.equals("-1"))
            {
                try
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Element elementKayit = doc.getElementById(String.valueOf(xmlKayitID));
                    elementKayit.setAttribute(XML_DURUM, DURUM_TAMAMLANDI);

                    doc.normalize();
                    documentToFile(doc);

                    boolean sonuc;
                    Node nodeParca = elementKayit.getParentNode().getParentNode();
                    sonuc = parcayiIsaretleTamamlandi(nodeParca, doc);

                    while (sonuc)
                    {
                        nodeParca = nodeParca.getParentNode().getParentNode();
                        sonuc = parcayiIsaretleTamamlandi(nodeParca, doc);
                    }

                    /*
                    //bütün kayıtlar tamamlandi olarak isaretlenmis ise kategorinin içindeki kategorilere baksın
                    if (kayitDurumunuKontrolEt(elementKayit.getParentNode(), DURUM_YENI))
                    {
                        //altparca içindeki kategoriler tamamlandi olarak isaretlenmis ise içinde bulunulan kategori de tamamlandi olarak isaretlensi
                        if (altParcaDurumunuKontrolEt(elementKayit.getParentNode().getParentNode(), DURUM_YENI))
                        {
                            Node nodeKategori = elementKayit.getParentNode().getParentNode();
                            String idKategori = nodeKategori.getAttributes().getNamedItem(XML_ID).getNodeValue();
                            kategoriDurumunuGuncelle(doc, idKategori, DURUM_TAMAMLANDI);
                        }
                    }

                    doc.normalize();
                    documentToFile(doc);
*/

                    Toast.makeText(getActivity(), "tamamlandi olarak isaretlendi", Toast.LENGTH_SHORT).show();

                    menuActionBar.findItem(R.id.action_degistir_tamamlandi).setVisible(false);
                    menuActionBar.findItem(R.id.action_degistir_yeni).setVisible(true);

                    //customRelativeLayout crl = (customRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                    //crl.getTvTik().setText("\u2714");
                    //crl.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));
                    //arkaplanKayit(crl);
                    //crl.setCstSeciliMi(false);

                /*
                for (int i = 0; i < listeSilinecek.size(); i++)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Element elementKayit = doc.getElementById(String.valueOf(listeSilinecek.get(i)));
                    elementKayit.setAttribute(XML_DURUM, DURUM_TAMAMLANDI);

                    //bütün kayıtlar tamamlandi olarak isaretlenmis ise kategorinin içindeki kategorilere baksın
                    if (kayitDurumunuKontrolEt(elementKayit.getParentNode()))
                    {
                        //altparca içindeki kategoriler tamamlandi olarak isaretlenmis ise içinde bulunulan kategori de tamamlandi olarak isaretlensi
                        if (altParcaDurumunuKontrolEt(elementKayit.getParentNode().getParentNode()))
                        {
                            Node nodeKategori = elementKayit.getParentNode().getParentNode();
                            String idKategori = nodeKategori.getAttributes().getNamedItem(XML_ID).getNodeValue();
                            kategoriDurumunuGuncelle(doc, idKategori, DURUM_TAMAMLANDI);
                        }
                    }

                    doc.normalize();
                    documentToFile(doc);

                    customRelativeLayout crl = (customRelativeLayout) anaLayout.findViewById(listeSilinecek.get(i));
                    crl.getTvTik().setText("\u2714");
                    //crl.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));
                    arkaplanKayit(crl);
                    crl.setCstSeciliMi(false);
                }
                */
                }
                catch (ParserConfigurationException e)
                {
                }
                catch (FileNotFoundException e)
                {
                }
                catch (IOException e)
                {
                }
                catch (SAXException e)
                {
                }
            }
        }

        public static void klavyeAc(Context c, EditText et)
        {
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        }

        public static void klavyeKapat(Context c, IBinder windowToken)
        {
            InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(windowToken, 0);
        }

        public void actionBarDegistir(int actionBarTur)
        {
            ACTIONBAR_TUR = actionBarTur;
            menuActionBar.clear();
            onCreateOptionsMenu(menuActionBar, inflaterActionBar);
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
                    //ACTIONBAR_TUR = ACTIONBAR_ONAY;
                    actionBarDegistir(ACTIONBAR_ONAY);
                    //menuActionBar.clear();
                    //onCreateOptionsMenu(menuActionBar, inflaterActionBar);
                    etEklenecek = yaziAlaniOlustur();
                    klavyeAc(getActivity(), etEklenecek);
                    return true;
                case R.id.action_onay_tamamlandi:
                    yaziyiKaydet(etEklenecek);
                    ACTIONBAR_TUR = ACTIONBAR_EKLE;
                    return true;
                case R.id.action_onay_tamamlanmadi:
                    klavyeKapat(getActivity(), etEklenecek.getWindowToken());
                    anaLayout.removeView(etEklenecek);
                    ACTIONBAR_TUR = ACTIONBAR_EKLE;
                    return true;
                //case R.id.action_onay_sil:
                //seciliElemanlariSil();
                //return true;
                case R.id.action_secim_tamamlandi:
                    seciliElemanlariTamamla();
                    return true;
                case R.id.action_degistir_sil:
                    kayitSilDiyaloguOlustur();
                    return true;
                case R.id.action_degistir_tamamlandi:
                    kayitTamamla();
                    return true;
                case R.id.action_degistir_yeni:
                    kayitYeni();
                    return true;
                case android.R.id.home:
                    ustSeviyeyiGetir();
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
                    EditText et = new EditText(getActivity());
                    et.setText(getArguments().getString(FRAGMENT_YAZI));
                    rl.addView(et, lp);
                    anaLayout.addView(rl);

                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

                    return rootView;
                }
                default:
                    return null;
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        PlaceholderFragment fr = (PlaceholderFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (xmlParcaID.equals("0"))//en ust seviyede ise uygulamadan çıksın
        {
            super.onBackPressed();
        }
        else
        {
            fr.ustSeviyeyiGetir();
        }
    }

    public static class customRelativeLayout extends RelativeLayout
    {
        private int cstID = -1;
        private boolean cstSeciliMi = false;
        private TextView tvTik;
        private TextView tvBaslik;
        final static int ID0 = 10000;
        final static int ID1 = 10001;
        final static int ID2 = 10002;

        public customRelativeLayout(Context context, String baslik, int elemanTur)
        {
            super(context);
            setCstSeciliMi(false);
            switch (elemanTur)
            {
                case ELEMAN_TUR_KATEGORI:
                {
                    LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pa.setMargins(0, 10, 0, 10);
                    this.setLayoutParams(pa);

                    arkaplanKategori();

                    tvTik = new TextView(context);
                    tvTik.setTextSize(30);
                    tvTik.setId(ID0);
                    tvTik.setTextColor(Color.WHITE);
                    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    lp3.addRule(RelativeLayout.CENTER_VERTICAL);
                    this.addView(tvTik, lp3);

                    tvBaslik = new TextView(context);
                    tvBaslik.setTextSize(30);
                    tvBaslik.setText(baslik);
                    tvBaslik.setTextColor(Color.WHITE);
                    tvBaslik.setPadding(5, 0, 5, 0);
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    //p2.addRule(RelativeLayout.LEFT_OF, tvIsaret.getId());
                    lp2.addRule(RelativeLayout.RIGHT_OF, tvTik.getId());
                    this.addView(tvBaslik, lp2);

                    break;
                }
                case ELEMAN_TUR_KAYIT:
                {
                    LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pa2.setMargins(0, 0, 0, 0);
                    this.setLayoutParams(pa2);

                    arkaplanKayit();

                    tvTik = new TextView(context);
                    tvTik.setTextSize(15);
                    tvTik.setId(ID2);
                    tvTik.setTextColor(Color.WHITE);
                    RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    lp4.addRule(RelativeLayout.CENTER_VERTICAL);
                    this.addView(tvTik, lp4);

                    tvBaslik = new TextView(context);
                    tvBaslik.setTextSize(15);
                    tvBaslik.setText(baslik);
                    tvBaslik.setTextColor(Color.WHITE);
                    tvBaslik.setPadding(5, 0, 5, 0);
                    RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    lp5.addRule(RelativeLayout.RIGHT_OF, tvTik.getId());
                    this.addView(tvBaslik, lp5);

                    break;
                }
            }
        }

        public void arkaplanSecili()
        {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFFFF2222);
            gd.setStroke(px2, 0xFF880000);
            gd.setCornerRadius(px7);
            setBackground(gd);
            setPadding(10, 20, 10, 20);
        }

        public void arkaplanKategori()
        {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFF00CED1);
            gd.setStroke(px2, 0xFF009095);
            gd.setCornerRadius(px7);
            setBackground(gd);
            setPadding(10, 20, 10, 20);
        }

        public void arkaplanKayit()
        {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFF009ED1);
            gd.setStroke(px2, 0xFF004095);
            gd.setCornerRadius(px7);
            setBackground(gd);
            setPadding(10, 20, 10, 20);
        }

        public TextView getTvTik()
        {
            return tvTik;
        }

        public TextView getTvBaslik()
        {
            return tvBaslik;
        }

        public int getCstID()
        {
            return cstID;
        }

        public void setCstID(int cstID)
        {
            this.cstID = cstID;
        }

        public boolean isCstSeciliMi()
        {
            return cstSeciliMi;
        }

        public void setCstSeciliMi(boolean cstSeciliMi)
        {
            this.cstSeciliMi = cstSeciliMi;
        }
    }
}
