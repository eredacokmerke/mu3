package com.example.ekcdr.uygulama3;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
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
    private static final int FRAGMENT_KATEGORI_EKRANI = 0;
    private static final int FRAGMENT_KAYIT_EKRANI = 1;
    private static int FRAGMENT_ETKIN_EKRAN = FRAGMENT_KATEGORI_EKRANI;
    static Resources resources;
    static float px7;
    static int px2;
    private static String xmlEtiketID = "0";//içinde olunan parçanın id si
    private static int xmlID;//eklenen kategori ve kayıtlara id verebilmek için

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
            //getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment(sonuc), FRAGMENT_TAG).commit();
            getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_ETKIN_EKRAN, 0), FRAGMENT_TAG).commit();
        }

        ActionBar bar = getActionBar();
        ColorDrawable actionBarArkaPlan = new ColorDrawable(Color.parseColor("#009ED1"));
        bar.setBackgroundDrawable(actionBarArkaPlan);

        resources = getResources();
        px7 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, resources.getDisplayMetrics());
        px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, resources.getDisplayMetrics());
    }

    //xml dosyası var mı diye kontrol ediyor. yoksa oluşturuyor ve son xmlID'sini donduruyor
    public void xmlDosyasiKontrolEt()
    {
        File xmlDosyasi = new File(xmlDosyaYolu);
        if (xmlKlasorYolu.exists())//klasör var
        {
            if (xmlDosyasi.exists())//dosya sistemde mevcut
            {
                xmlID = sonIDyiBul();
                if (xmlID == -1)
                {
                    ekranaHataYazdir("1", "xml okunamadı");
                }
            }
            else//dosya yok
            {
                xmlID = 0;
                xmlDosyasiOlustur();
            }
        }
        else//klasör yok
        {
            xmlKlasorYolu.mkdirs();
            xmlID = 0;
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
    public int sonIDyiBul()
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
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
        //private List<Integer> listSeciliKategori;//seçilen kategorilerin listesi
        //private List<Integer> listSeciliYazi;//seçilen kayıtların listesi
        private String TAG = "uyg3";

        public PlaceholderFragment()
        {

        }

        /*
        public PlaceholderFragment(int sonuc)
        {
            //xmlID = sonuc;
            //listSeciliYazi = new ArrayList<>();
            //listSeciliKategori = new ArrayList<>();
        }
        */

        public static PlaceholderFragment newInstanceKategori(int secim, int kategoriID)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(FRAGMENT_SECIM, secim);
            FRAGMENT_ETKIN_EKRAN = secim;
            fragment.setArguments(args);
            xmlEtiketID = String.valueOf(kategoriID);

            return fragment;
        }

        public static PlaceholderFragment newInstanceKayit(int secim, String yazi)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(FRAGMENT_SECIM, secim);
            args.putString(FRAGMENT_YAZI, yazi);
            FRAGMENT_ETKIN_EKRAN = secim;
            fragment.setArguments(args);

            return fragment;
        }

        /*
        //new.xml dosyasını inputstream a dönüştürüyor
        public InputStream fileToIS()
        {
            try
            {
                String xmlMetin = "";
                File newxmlfile = new File(xmlDosyaYolu);

                FileInputStream fIn = new FileInputStream(newxmlfile);

                InputStreamReader isr = new InputStreamReader(fIn);
                BufferedReader buffreader = new BufferedReader(isr);

                String readString = buffreader.readLine();
                while (readString != null)
                {
                    xmlMetin = xmlMetin + readString;
                    readString = buffreader.readLine();
                }
                isr.close();

                //InputStream is = new ByteArrayInputStream(xmlMetin.getBytes("UTF-8"));
                //return is;

                return new ByteArrayInputStream(xmlMetin.getBytes("UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                Log.e("hata[12]", e.getMessage());
                ekranaHataYazdir("12", e.getMessage());
                return null;
            }
            catch (IOException e)
            {
                Log.e("hata[13]", e.getMessage());
                ekranaHataYazdir("13", e.getMessage());
                return null;
            }
        }
        */

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
            /*
            try
            {
                XmlPullParser parser;
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(fileToIS(), null);
                parser.next();

                if (xmlDogruParcayiBul(parser, parcaID, xmlEtiketNesil))
                {
                    if (!xmlKayitlariGetir(parser))
                    {
                        return false;
                    }
                    if (!xmlKategorileriGetir(parser))
                    {
                        return false;
                    }
                    return true;
                }
                else
                {
                    Log.e("hata[147]", "xml dosyası okunamadı");
                    ekranaHataYazdir("147", "xml dosyası okunamadı");
                    return false;
                }
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[148]", e.getMessage());
                ekranaHataYazdir("148", e.getMessage());
                return false;
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e.getMessage());
                return false;
            }
            */
        }

        //xml parse edildikten sonra kayitları ana ekrana ekler
        public void kayitlariAnaEkranaEkle(final String yazi, final int eklenenID, String durum)
        {
            final customRelativeLayout crl = new customRelativeLayout(getActivity(), yazi, ELEMAN_TUR_KAYIT);
            crl.setCstID(eklenenID);
            crl.setId(eklenenID);
            crl.setCstSeciliMi(false);
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
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKayit(FRAGMENT_KAYIT_EKRANI, yazi)).addToBackStack(null).commit();
                    actionBarKayit();

                     /*
                    if (crl.isCstSeciliMi())
                    {
                        listSeciliYazi.remove(listSeciliYazi.indexOf(eklenenID));
                        crl.setCstSeciliMi(false);
                        //crl.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));
                        arkaplanKayit(crl);
                        if (listSeciliKategori.isEmpty() && listSeciliYazi.isEmpty())
                        {
                            actionBarIlk();
                        }
                    }
                    else
                    {
                        listSeciliYazi.add(eklenenID);
                        crl.setCstSeciliMi(true);
                        actionBarKayit();
                        //crl.setBackground(getResources().getDrawable(R.drawable.ana_ekran_secili));
                        arkaplanSecili(crl);

                    }
                     */
                }
            });
            anaLayout.addView(crl);

            /*
            final customTextView tv = new customTextView(getActivity());
            tv.setCstID(eklenenID);
            tv.setId(eklenenID);
            tv.setCstSeciliMi(false);
            if(durum.equals(DURUM_TAMAMLANDI))
            {
             tv.setText("\u2714" + " " + yazi);
            }
            else
            {
                tv.setText(yazi);
            }
            //tv.setText(yazi);
            tv.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (tv.isCstSeciliMi())
                    {
                        listSeciliYazi.remove(listSeciliYazi.indexOf(eklenenID));
                        tv.setCstSeciliMi(false);
                        tv.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));
                        if (listSeciliKategori.isEmpty() && listSeciliYazi.isEmpty())
                        {
                            actionBarIlk();
                        }
                    }
                    else
                    {
                        listSeciliYazi.add(eklenenID);
                        tv.setCstSeciliMi(true);
                        actionBarKayit();
                        tv.setBackground(getResources().getDrawable(R.drawable.ana_ekran_secili));
                    }
                }
            });
            anaLayout.addView(tv);
            */
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        //public void kategorileriAnaEkranaEkle(final String baslik, final String staticrenk, final String yazi, boolean cerceve, final int kategoriID)
        public void kategorileriAnaEkranaEkle(final String baslik, final int kategoriID, String durum)
        {
            customRelativeLayout crl = new customRelativeLayout(getActivity(), baslik, ELEMAN_TUR_KATEGORI);//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran

            /*
            crl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    anaLayout.removeAllViews();
                    getActivity().getActionBar().setTitle(kategoriBaslik);
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    xmlEtiketID = String.valueOf(kategoriID);

                    //seciliElemanListeleriniSifirla();

                    parseXml(String.valueOf(kategoriID));
                }
            });
            */

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
                    getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, kategoriID), FRAGMENT_TAG).commit();
                    getActivity().getActionBar().setTitle(baslik);
                    /*
                    if (crl.isCstSeciliMi())
                    {
                        listSeciliKategori.remove(listSeciliKategori.indexOf(kategoriID));
                        crl.setCstSeciliMi(false);
                        //crl.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kategori));
                        arkaplanKategori(crl);
                        if (listSeciliKategori.isEmpty() && listSeciliYazi.isEmpty())
                        {
                            actionBarIlk();
                        }
                    }
                    else
                    {
                        listSeciliKategori.add(kategoriID);
                        crl.setCstSeciliMi(true);
                        actionBarKayit();
                        //crl.setBackground(getResources().getDrawable(R.drawable.ana_ekran_secili));
                        arkaplanSecili(crl);
                    }
                    */
                }
            });
            anaLayout.addView(crl);
            /*
            ll.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if (ll.isCstSeciliMi())
                    {
                        listSeciliKategori.remove(listSeciliKategori.indexOf(kategoriID));
                        ll.setCstSeciliMi(false);
                        ll.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kategori));
                        if (listSeciliKategori.isEmpty() && listSeciliYazi.isEmpty())
                        {
                            actionBarIlk();
                        }
                    }
                    else
                    {
                        listSeciliKategori.add(kategoriID);
                        ll.setCstSeciliMi(true);
                        actionBarKayit();
                        ll.setBackground(getResources().getDrawable(R.drawable.ana_ekran_secili));
                    }
                    return true;
                }
            });

            ll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    anaLayout.removeAllViews();
                    getActivity().getActionBar().setTitle(baslik);
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    xmlEtiketID = String.valueOf(kategoriID);
                    xmlEtiketNesil = xmlEtiketNesil + 2;

                    parseXml(String.valueOf(kategoriID));
                }
            });
            */
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

        //başta oluşturulan xml e yeni eklenecek kısımları ekliyor
        public void xmlDosyasiniGuncelle(String baslik, String renk)
        {
            xmlID++;
            try
            {
                //if (txtIsaretci.getText().equals(XML_ROOT))
                if (false)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Node nodeRoot = doc.getElementsByTagName(XML_ROOT).item(0);//ilk görülen root tagını alıyor
                    Element yeniNodeParca = doc.createElement(XML_PARCA);//parca isimli elemanı oluşturuyor
                    yeniNodeParca.setAttribute(XML_ID, String.valueOf(xmlID));//parca icinde id isimli özellik oluşturuluyor
                    nodeRoot.appendChild(yeniNodeParca);//root etiketine parca etiketi ekleniyor

                    Node nodeParca = doc.getElementById(String.valueOf(xmlID));//xmlid id sine sahip eleman alınıyor. üstte oluşturulan parca etiketi
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
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Node nodeMevcutParca = doc.getElementById(String.valueOf(xmlEtiketID));//içinde bulunulan parcaya giriyor
                    NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                    for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                    {
                        if (nodeParcaCocuklari.item(i).getNodeName().equals(XML_ALTPARCA))//parcanın içindeki altparca etiketine ulaşılıyor
                        {
                            Node nodeAltparca = nodeParcaCocuklari.item(i);//altparcaya giriliyor
                            Element yeniNodeParca = doc.createElement(XML_PARCA);//parca isimli etiket olşuturuluyor
                            yeniNodeParca.setAttribute(XML_ID, String.valueOf(xmlID));//parca ya id özelliği ekleniyor
                            yeniNodeParca.setAttribute(XML_DURUM, DURUM_YENI);//parca ya id özelliği ekleniyor
                            nodeAltparca.appendChild(yeniNodeParca);//altparca etiketine parca ekleniyor

                            Node nodeParca = doc.getElementById(String.valueOf(xmlID));//xmlid id sine sahip parca nın içine giriliyor. az önce oluşturulan parca
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
                }
            }
            catch (IOException e)
            {
                Log.e("hata[4]", e.getMessage());
                ekranaHataYazdir("4", e.getMessage());
            }
            catch (SAXException e)
            {
                Log.e("hata[5]", e.getMessage());
                ekranaHataYazdir("5", e.getMessage());
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[6]", e.getMessage());
                ekranaHataYazdir("6", e.getMessage());
            }
        }

        //listSeciliKategori ve listSeciliYazi'yi sıfırlar ve actionbar ı ilk haline döndürür
        public void seciliElemanListeleriniSifirla()
        {
            /*
            listSeciliKategori.clear();
            listSeciliYazi.clear();
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

                        customRelativeLayout crl = new customRelativeLayout(getActivity(), kategoriAdi, ELEMAN_TUR_KATEGORI);
                        crl.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, xmlID),FRAGMENT_TAG).commit();
                                getActivity().getActionBar().setTitle(kategoriAdi);
                            }
                        });

                        anaLayout.addView(crl);
                        xmlDosyasiniGuncelle(kategoriAdi, "");
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

        public EditText yaziAlaniOlustur()
        {
            actionBarOnay();

            EditText edittext = new EditText(getActivity());
            anaLayout.addView(edittext);
            return edittext;
        }

        public void ustSeviyeyiGetir()
        {
            switch (FRAGMENT_ETKIN_EKRAN)
            {
                case FRAGMENT_KAYIT_EKRANI:
                {
                    FragmentManager fm = getFragmentManager();

                    if (fm.getBackStackEntryCount() > 0)
                    {
                        fm.popBackStackImmediate();
                    }
                    FRAGMENT_ETKIN_EKRAN = FRAGMENT_KATEGORI_EKRANI;

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

                        Element element = doc.getElementById(xmlEtiketID);
                        String ustSeviyeID = element.getParentNode().getParentNode().getAttributes().getNamedItem(XML_ID).getNodeValue();

                        xmlEtiketID = ustSeviyeID;
                        getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstanceKategori(FRAGMENT_KATEGORI_EKRANI, Integer.parseInt(xmlEtiketID)),FRAGMENT_TAG).commit();
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
                    Log.d(TAG, "default : xmlEtiketID : " + xmlEtiketID);
            }
        }

        //kayit ekle tusuna basıldıktan sonra açılan edittext'e yazılan yazıyı xml'e ekler
        public void yaziyiKaydet(EditText et)
        {
            xmlID++;
            try
            {
                String alanYazi = et.getText().toString();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                kategoriDurumunuGuncelle(doc, xmlEtiketID, DURUM_YENI);

                Node nodeMevcutParca = doc.getElementById(String.valueOf(xmlEtiketID));//içinde bulunulan parcaya giriyor
                NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                {
                    if (nodeParcaCocuklari.item(i).getNodeName().equals(XML_YAZILAR))//parcanın içindeki yazilar etiketine ulaşılıyor
                    {
                        int eklenenID = xmlID;
                        Element yeniNodeKayit = doc.createElement(XML_KAYIT);//kayıt etiketi olusturuyor
                        yeniNodeKayit.setAttribute(XML_ID, String.valueOf(xmlID));//parca ya id özelliği ekleniyor
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
            if (listSeciliKategori.isEmpty() && listSeciliYazi.isEmpty())
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
                        yaziSil(listSeciliYazi);
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

        //secili kayıtları ve kategorilerin altındaki kayıtları tamamalandı olarak isaretler
        public void seciliElemanlariTamamla()
        {
            /*
            kayitTamamla(listSeciliYazi);
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

        //yazilar altındaki kayitların durum degerlerini kontrol eder
        public boolean kayitDurumunuKontrolEt(Node nodeYazilar)
        {
            NodeList nodeList = nodeYazilar.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getAttributes().getNamedItem(XML_DURUM).getNodeValue().equals("0"))
                {
                    return false;
                }
            }
            return true;
        }

        //altparca içindeki parcaların durum degerlerini kontrol eder
        public boolean altParcaDurumunuKontrolEt(Node nodeParca)
        {
            NodeList nodeList = nodeParca.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeName().equals(XML_ALTPARCA))
                {
                    NodeList nodeListAltParca = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodeListAltParca.getLength(); j++)
                    {
                        if (nodeListAltParca.item(j).getAttributes().getNamedItem(XML_DURUM).getNodeValue().equals("0"))
                        {
                            return false;
                        }
                    }
                    break;
                }
            }
            return true;
        }

        //secilen yaziyi tamamlandı olarak değiştirir
        public void kayitTamamla(List<Integer> listeSilinecek)
        {
            try
            {
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

        //kayıt secildiği zaman actionBar'a menu_secim tuslarını cizer
        public void actionBarKayit()
        {
            menuActionBar.clear();
            inflaterActionBar.inflate(R.menu.menu_secim, menuActionBar);
        }

        //actionBar'a menu_main_onay tuslarını cizer
        public void actionBarOnay()
        {
            menuActionBar.clear();
            inflaterActionBar.inflate(R.menu.menu_main_onay, menuActionBar);
        }

        //actionBarın ilk hali
        public void actionBarIlk()
        {
            menuActionBar.clear();
            inflaterActionBar.inflate(R.menu.menu_main, menuActionBar);
        }

        public void arkaplanSecili(customRelativeLayout crl)
        {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFFFF2222);
            gd.setStroke(px2, 0xFF880000);
            gd.setCornerRadius(px7);
            crl.setBackground(gd);
        }

        public void arkaplanKategori(customRelativeLayout crl)
        {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFF00CED1);
            gd.setStroke(px2, 0xFF009095);
            gd.setCornerRadius(px7);
            crl.setBackground(gd);
        }

        public void arkaplanKayit(customRelativeLayout crl)
        {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFF009ED1);
            gd.setStroke(px2, 0xFF004095);
            gd.setCornerRadius(px7);
            crl.setBackground(gd);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
            super.onCreateOptionsMenu(menu, inflater);
            inflaterActionBar = inflater;
            menuActionBar = menu;
            inflaterActionBar.inflate(R.menu.menu_main, menuActionBar);
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
                    etEklenecek = yaziAlaniOlustur();
                    klavyeAc(getActivity(), etEklenecek);
                    return true;
                case R.id.action_tamam:
                    yaziyiKaydet(etEklenecek);
                    actionBarIlk();
                    return true;
                case R.id.action_iptal:
                    klavyeKapat(getActivity(), etEklenecek.getWindowToken());
                    anaLayout.removeView(etEklenecek);
                    actionBarIlk();
                    return true;
                case R.id.action_sil:
                    seciliElemanlariSil();
                    return true;
                case R.id.action_tamamlandi:
                    seciliElemanlariTamamla();
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
                    if (xmlID > 0)//xml de kayıt varsa ekrana eklesin
                    {
                        parseXml(xmlEtiketID);
                        if (xmlEtiketID.equals("0"))
                        {
                            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                            getActivity().getActionBar().setTitle("");
                        }
                        else
                        {
                            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (xmlEtiketID.equals("0"))//en ust seviyede ise uygulamadan çıksın
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
        //private TextView tvIsaret;
        private TextView tvBaslik;
        final static int ID0 = 10000;
        final static int ID1 = 10001;
        final static int ID2 = 10002;

        public customRelativeLayout(Context context, String baslik, int elemanTur)
        {
            super(context);
            switch (elemanTur)
            {
                case ELEMAN_TUR_KATEGORI:
                {
                    LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pa.setMargins(0, 10, 0, 10);
                    this.setLayoutParams(pa);
                    //this.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kategori));

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(0xFF00CED1);
                    gd.setStroke(px2, 0xFF009095);
                    gd.setCornerRadius(px7);
                    this.setBackground(gd);
                    this.setPadding(10, 20, 10, 20);

                    tvTik = new TextView(context);
                    tvTik.setTextSize(30);
                    tvTik.setId(ID0);
                    tvTik.setTextColor(Color.WHITE);
                    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    lp3.addRule(RelativeLayout.CENTER_VERTICAL);
                    this.addView(tvTik, lp3);

                    /*
                    tvIsaret = new TextView(context);
                    tvIsaret.setTextSize(30);
                    tvIsaret.setId(ID1);
                    tvIsaret.setText("\u2192");
                    tvIsaret.setTextColor(Color.WHITE);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp.addRule(RelativeLayout.CENTER_VERTICAL);
                    this.addView(tvIsaret, lp);
                    */

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
                    //this.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(0xFF009ED1);
                    gd.setStroke(px2, 0xFF004095);
                    gd.setCornerRadius(px7);
                    this.setBackground(gd);
                    this.setPadding(10, 20, 10, 20);

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

        public TextView getTvTik()
        {
            return tvTik;
        }

        /*
                public TextView getTvIsaret()
                {
                    return tvIsaret;
                }
        */

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
