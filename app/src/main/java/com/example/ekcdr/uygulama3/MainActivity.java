package com.example.ekcdr.uygulama3;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
    private static final String DURUM = "durum";
    private static final String FRAGMENT_TAG = "fragment_tag";

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
        int sonuc = xmlDosyasiKontrolEt();

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment(sonuc), FRAGMENT_TAG).commit();
        }

        ActionBar bar = getActionBar();
        ColorDrawable actionBarArkaPlan = new ColorDrawable(Color.parseColor("#009ED1"));
        bar.setBackgroundDrawable(actionBarArkaPlan);
        //bar.setDisplayHomeAsUpEnabled(true);
        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0000")));
    }

    //xml dosyası var mı diye kontrol ediyor. yoksa oluşturuyor ve son xmlID'sini donduruyor
    public int xmlDosyasiKontrolEt()
    {
        int xmlID;
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

        return xmlID;
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
                    "<parca id=\"0\">" +
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
        private int xmlID;//eklenen kategori ve kayıtlara id verebilmek için
        private String xmlEtiketID = "0";//içinde olunan parçanın id si
        private int xmlEtiketNesil = 2;//içinde olunan parcanın nesli
        private MenuInflater inflaterActionBar;
        private Menu menuActionBar;
        private EditText etEklenecek;//yeni kayıt eklemeye tıklandığı zaman olusan edittext
        private List<Integer> listSeciliKategori;//seçilen kategorilerin listesi
        private List<Integer> listSeciliYazi;//seçilen kayıtların listesi
        private String TAG = "uyg3";

        public PlaceholderFragment(int sonuc)
        {
            xmlID = sonuc;
            listSeciliYazi = new ArrayList<>();
            listSeciliKategori = new ArrayList<>();
        }

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

        public void ekranaHataYazdir(String id, String hata)
        {
            Toast.makeText(getActivity(), "hata[" + id + "]: " + hata, Toast.LENGTH_SHORT).show();
        }

        public boolean xmlKayitlariGetir(XmlPullParser parser)
        {
            try
            {
                boolean bulundu = false;
                int eventType = parser.getEventType();
                String tagName = parser.getName();

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    parser.next();
                    eventType = parser.getEventType();
                    tagName = parser.getName();
                    //Log.d(TAG, "1 tagname : " + tagName + " -- event type : " + eventType + " -- derinlik : " + parser.getDepth() + " -- nesil : " + xmlEtiketNesil );

                    if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_YAZILAR) && parser.getDepth() == xmlEtiketNesil + 1)
                    {
                        Log.d(TAG, "kayit bulundu");
                        bulundu = true;
                        break;
                    }
                }

                if (bulundu)
                {
                    while (!(eventType == XmlPullParser.END_TAG && tagName.equals(XML_YAZILAR) && parser.getDepth() == xmlEtiketNesil + 1))
                    {
                        parser.next();
                        eventType = parser.getEventType();
                        tagName = parser.getName();

                        Log.d(TAG, "tagName 3 : " + tagName + " eventType : " + eventType);

                        if (tagName.equals(XML_KAYIT) && eventType == XmlPullParser.START_TAG && parser.getDepth() == xmlEtiketNesil + 2)
                        {
                                String kayitID = parser.getAttributeValue(null, XML_ID);
                                String kayitDurum = parser.getAttributeValue(null, "durum");
                                Log.d(TAG, "kayitDurum : " + kayitDurum);

                                parser.next();
                                eventType = parser.getEventType();
                                tagName = parser.getName();

                                if (eventType == XmlPullParser.TEXT)
                                {
                                    //Log.d(TAG, "1 kayit text : " + parser.getText() + " kayitID : " + kayitID +" durum : "+parser.getAttributeValue(1));
                                    kayitlariAnaEkranaEkle(parser.getText(), Integer.parseInt(kayitID), kayitDurum);
                                }

                        }
                    }
                }
                return bulundu;
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[142]", e.getMessage());
                ekranaHataYazdir("14", e.getMessage());
                return false;
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e.getMessage());
                return false;
            }

        }

        public boolean xmlKategoriBasliginiGetir(XmlPullParser parser, String kategoriID)
        {
            try
            {
                boolean bulunduBaslik = false;
                int eventType = parser.getEventType();
                String tagName = parser.getName();

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    parser.next();
                    eventType = parser.getEventType();
                    tagName = parser.getName();
                    //Log.d(TAG, "4 tagname : " + tagName + " -- event type : " + eventType + " -- derinlik : " + parser.getDepth() + " -- nesil : " + xmlEtiketNesil );

                    if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_BASLIK) && parser.getDepth() == xmlEtiketNesil + 3)
                    {
                        String baslik = parser.nextText();
                        Log.d(TAG, "bulundu baslik : " + baslik);

                        kategorileriAnaEkranaEkle(baslik, Integer.parseInt(kategoriID));
                        bulunduBaslik = true;
                        break;
                    }
                }
                return bulunduBaslik;
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[143]", e.getMessage());
                ekranaHataYazdir("14", e.getMessage());
                return false;
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e.getMessage());
                return false;
            }
        }

        public boolean xmlKategorileriGetir(XmlPullParser parser)
        {
            try
            {
                boolean bulunduAltParca = false;
                int eventType = parser.getEventType();
                String tagName = parser.getName();
                boolean sonuc = true;

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    parser.next();
                    eventType = parser.getEventType();
                    tagName = parser.getName();
                    //Log.d(TAG, "2 tagname : " + tagName + " -- event type : " + eventType + " -- derinlik : " + parser.getDepth() + " -- nesil : " + xmlEtiketNesil );

                    if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_ALTPARCA) && parser.getDepth() == xmlEtiketNesil + 1)
                    {
                        Log.d(TAG, "alt parca bulundu");
                        bulunduAltParca = true;
                        break;
                    }
                }

                if (bulunduAltParca)
                {
                    while (!(eventType == XmlPullParser.END_TAG && tagName.equals(XML_ALTPARCA) && parser.getDepth() == xmlEtiketNesil + 1))
                    {
                        parser.next();
                        eventType = parser.getEventType();
                        tagName = parser.getName();
                        //Log.d(TAG, "3 tagname : " + tagName + " -- event type : " + eventType + " -- derinlik : " + parser.getDepth() + " -- nesil : " + xmlEtiketNesil);

                        if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_PARCA) && parser.getDepth() == xmlEtiketNesil + 2)
                        {
                            String kategoriID = parser.getAttributeValue(null, XML_ID);
                            Log.d(TAG, "parca bulundu : id : " + kategoriID);
                            sonuc = sonuc && xmlKategoriBasliginiGetir(parser, kategoriID);
                        }

                    }
                    return sonuc;
                }
                else
                {
                    return false;
                }
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[144]", e.getMessage());
                ekranaHataYazdir("14", e.getMessage());
                return false;
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e.getMessage());
                return false;
            }
        }

        //xml'de aranan parcayi id'yi kullanarak bulur
        public boolean xmlDogruParcayiBul(XmlPullParser parser, String parcaID, int nesil)
        {
            try
            {
                //boolean dogruParca = false;//doğru parcanın altparcalarını yazdırması için
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT)
                {

                    String tagName = parser.getName();
                    if (parser.getDepth() == nesil && eventType == XmlPullParser.START_TAG)
                    {
                        if (tagName.equals(XML_PARCA))
                        {
                            if (parcaID.equals(parser.getAttributeValue(null, XML_ID)))
                            {
                                Log.d(TAG, "parca bulundu");
                                return true;
                            }
                        }
                    }
                    eventType = parser.next();
                }
                return false;
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[145]", e.getMessage());
                ekranaHataYazdir("14", e.getMessage());
                return false;
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e.getMessage());
                return false;
            }
        }

        public List<String> xmlUstIDyiBul(String parcaID)
        {
            List<String> sonuc = new ArrayList<>();

            try
            {
                String ustID = "-1";
                String ustBaslik = "-1";

                XmlPullParser parser;
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(fileToIS(), null);
                parser.next();

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    String tagName = parser.getName();
                    if (parser.getDepth() == xmlEtiketNesil - 2 && eventType == XmlPullParser.START_TAG)//girdiği parçanın id ve basligini tutuyor. eğer parent ise döndürüyor
                    {
                        if (tagName.equals(XML_PARCA))
                        {
                            ustID = parser.getAttributeValue(null, XML_ID);

                            while (eventType != XmlPullParser.END_DOCUMENT)
                            {
                                parser.next();
                                eventType = parser.getEventType();
                                tagName = parser.getName();

                                if (eventType == XmlPullParser.START_TAG && tagName.equals(XML_BASLIK) && parser.getDepth() == xmlEtiketNesil - 1)
                                {
                                    ustBaslik = parser.nextText();
                                    break;
                                }
                            }

                        }
                    }
                    if (parser.getDepth() == xmlEtiketNesil && eventType == XmlPullParser.START_TAG)
                    {
                        if (tagName.equals(XML_PARCA))
                        {
                            if (parcaID.equals(parser.getAttributeValue(null, XML_ID)))
                            {
                                sonuc.add(ustID);
                                sonuc.add(ustBaslik);
                                return sonuc;
                            }
                        }
                    }
                    eventType = parser.next();
                }
                return sonuc;
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[146]", e.getMessage());
                ekranaHataYazdir("14", e.getMessage());
                return sonuc;
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e.getMessage());
                return sonuc;
            }
        }

        //parca etiketinin altındaki yazi ve kategorileri ekrana basıyor
        public boolean parseXmlIlkNesil(String parcaID)
        {
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
                    ekranaHataYazdir("14", "xml dosyası okunamadı");
                    return false;
                }
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[148]", e.getMessage());
                ekranaHataYazdir("14", e.getMessage());
                return false;
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e.getMessage());
                return false;
            }
        }

        //xml parse edildikten sonra kayitları ana ekrana ekler
        public void kayitlariAnaEkranaEkle(String yazi, final int eklenenID, String durum)
        {
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

            /*
            tv.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    ActionBar bar = getActivity().getActionBar();
                    ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.red));
                    ColorDrawable[] color = {actionBarArkaPlan, red};
                    TransitionDrawable trans = new TransitionDrawable(color);
                    actionBarArkaPlan = red;
                    bar.setBackgroundDrawable(trans);
                    trans.startTransition(250);

                    final int parcaID = ((customTextView) view).getCstID();
                    final View islemYapilanView = view;

                    //ActionBar bar = getActivity().getActionBar();
                    //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FF00")));

                    //alertdialog un içindeki ana LinearLayout
                    LinearLayout alertLL = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    alertLL.setLayoutParams(pa);
                    alertLL.setOrientation(LinearLayout.VERTICAL);
                    alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
                    alertLL.setWeightSum(1f);

                    Button btnTamamlandi = new Button(getActivity());
                    btnTamamlandi.setText("Tamanlandı olarak işaretle");
                    alertLL.addView(btnTamamlandi);
                    Button btnSil = new Button(getActivity());
                    btnSil.setText("Sil");
                    alertLL.addView(btnSil);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(alertLL);
                    final AlertDialog alert = builder.create();
                    alert.show();

                    //kaydı siliyor
                    btnSil.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            try
                            {
                                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                                dbf.setValidating(false);
                                DocumentBuilder db = dbf.newDocumentBuilder();
                                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                                Element element = doc.getElementById(String.valueOf(parcaID));
                                element.getParentNode().removeChild(element);
                                doc.normalize();
                                documentToFile(doc);

                                anaLayout.removeView(islemYapilanView);
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
                            alert.dismiss();
                        }
                    });

                    //kayıt durumunu tamamlandı olarak değiştiriyor
                    btnTamamlandi.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            try
                            {
                                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                                dbf.setValidating(false);
                                DocumentBuilder db = dbf.newDocumentBuilder();
                                Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                                Element element = doc.getElementById(String.valueOf(parcaID));
                                element.setAttribute(DURUM, DURUM_TAMAMLANDI);
                                doc.normalize();
                                documentToFile(doc);
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
                            alert.dismiss();
                        }
                    });

                    return false;
                }
            });
            */
            anaLayout.addView(tv);
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        //public void kategorileriAnaEkranaEkle(final String baslik, final String staticrenk, final String yazi, boolean cerceve, final int kategoriID)
        public void kategorileriAnaEkranaEkle(final String baslik, final int kategoriID)
        {
            final customRelativeLayout ll = kategoriAlaniOlustur(baslik, kategoriID);
            ll.setCstID(kategoriID);
            ll.setId(kategoriID);
            ll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
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
                }
            });
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

                    parseXmlIlkNesil(String.valueOf(kategoriID));
                }
            });
            */
            anaLayout.addView(ll);
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
            listSeciliKategori.clear();
            listSeciliYazi.clear();
            actionBarIlk();
        }

        //ana ekrana eklenecek kategori alanini olusturuyor
        public customRelativeLayout kategoriAlaniOlustur(final String kategoriBaslik, final int kategoriID)
        {
            customRelativeLayout ll = new customRelativeLayout(getActivity());//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
            //ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            pa.setMargins(0, 10, 0, 10);
            ll.setLayoutParams(pa);
            ll.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kategori));

            TextView tvIsaret = new TextView(getActivity());
            tvIsaret.setTextSize(30);
            tvIsaret.setId(View.generateViewId());
            tvIsaret.setText("\u2192");
            tvIsaret.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            ll.addView(tvIsaret, lp);

            TextView tvBaslik = new TextView(getActivity());
            tvBaslik.setTextSize(30);
            tvBaslik.setText(kategoriBaslik);
            tvBaslik.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.LEFT_OF,tvIsaret.getId());
            ll.addView(tvBaslik, lp2);

            tvIsaret.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    anaLayout.removeAllViews();
                    getActivity().getActionBar().setTitle(kategoriBaslik);
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    xmlEtiketID = String.valueOf(kategoriID);
                    xmlEtiketNesil = xmlEtiketNesil + 2;

                    seciliElemanListeleriniSifirla();

                    parseXmlIlkNesil(String.valueOf(kategoriID));
                }
            });

            return ll;

            /*
            LinearLayout ll = new LinearLayout(getActivity());//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            pa.setMargins(0, 10, 0, 10);
            ll.setLayoutParams(pa);
            ll.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kategori));

            TextView tv = new TextView(getActivity());
            tv.setTextSize(30);
            tv.setText(yazi);
            tv.setTextColor(Color.WHITE);
            ll.addView(tv);

            return ll;
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
                    String kategoriAdi = alertET.getText().toString();
                    if (kategoriAdi.isEmpty())//edittext boşken tamam'a tıklandı
                    {
                        Toast.makeText(getActivity(), "Kategori adı boş olamaz", Toast.LENGTH_LONG).show();
                    }
                    else//anaLayout'a yeni alan ekliyor
                    {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                        alert.dismiss();
                        RelativeLayout ll = kategoriAlaniOlustur(kategoriAdi, xmlID + 1);
                        anaLayout.addView(ll);
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
            List<String> ustSeviyeListe = xmlUstIDyiBul(String.valueOf(xmlEtiketID));

            if (ustSeviyeListe.size() == 2)
            {
                String ustID = ustSeviyeListe.get(0);
                String ustBaslik = ustSeviyeListe.get(1);

                anaLayout.removeAllViews();
                getActivity().getActionBar().setTitle(ustBaslik);
                xmlEtiketNesil = xmlEtiketNesil - 2;
                xmlEtiketID = ustID;
                parseXmlIlkNesil(xmlEtiketID);

                if (xmlEtiketID.equals("0"))
                {
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                }

            }
            else
            {
                Log.e("hata[15]", "ust seviye hatasi");
                ekranaHataYazdir("15", "ust seviye hatasi");
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

                Node nodeMevcutParca = doc.getElementById(String.valueOf(xmlEtiketID));//içinde bulunulan parcaya giriyor
                NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                {
                    if (nodeParcaCocuklari.item(i).getNodeName().equals(XML_YAZILAR))//parcanın içindeki yazilar etiketine ulaşılıyor
                    {
                        int eklenenID = xmlID;
                        Element yeniNodeKayit = doc.createElement(XML_KAYIT);//kayıt etiketi olusturuyor
                        yeniNodeKayit.setAttribute(XML_ID, String.valueOf(xmlID));//parca ya id özelliği ekleniyor
                        yeniNodeKayit.setAttribute(DURUM, DURUM_YENI);//parca ya id özelliği ekleniyor
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

                    customTextView view = (customTextView) anaLayout.findViewById(listeSilinecek.get(i));
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


        //secili kayıtları ve kategorilerin altındaki kayıtları tamamalandı olarak isaretler
        public void seciliElemanlariTamamla()
        {
            yaziTamamla(listSeciliYazi);
            kategoriTamamla(listSeciliKategori);

            seciliElemanListeleriniSifirla();
        }

        public void kategorininButunCocuklariniGetir(XmlPullParser parser, int xmlEtiketNesil2)
        {
            try
            {
                boolean bulundu = false;
                int eventType = parser.getEventType();
                String tagName = parser.getName();
                Log.d(TAG, "tagName 1 : " + tagName + " eventType : " + eventType);

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
                                elementKayit.setAttribute(DURUM, DURUM_TAMAMLANDI);

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
                    Log.d(TAG, "qqq alt parca bulundu");
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
                ekranaHataYazdir("14", e.getMessage());
            }
        }

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
                        kategorininButunCocuklariniGetir(parser, xmlEtiketNesil2);
                    }
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
                ekranaHataYazdir("14", e.getMessage());
            }
        }

        //secilen yaziyi tamamlandı olarak değiştirir
        public void yaziTamamla(List<Integer> listeSilinecek)
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
                    elementKayit.setAttribute(DURUM, DURUM_TAMAMLANDI);

                    doc.normalize();
                    documentToFile(doc);

                    customTextView ctv = (customTextView) anaLayout.findViewById(listeSilinecek.get(i));
                    ctv.setText("\u2714" + ctv.getText());
                    ctv.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            anaLayout = (LinearLayout) rootView.findViewById(R.id.anaLayout);
            if (xmlID > 0)//xml de kayıt varsa ekrana eklesin
            {
                parseXmlIlkNesil("0");
                //parseIlkNesil();
            }
            return rootView;
        }
    }

    @Override
    public void onBackPressed()
    {
        PlaceholderFragment fr = (PlaceholderFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fr.xmlEtiketID.equals("0"))//en ust seviyede ise uygulamadan çıksın
        {
            super.onBackPressed();
        }
        else
        {
            fr.ustSeviyeyiGetir();
        }
    }

    public static class xmlYapisi
    {
        public String mRenk;
        public String mBaslik;
        public String mYazi;
        public String mID;

        /*
        public static final String XML_PARCA = XML_PARCA;
        public static final String XML_BASLIK = XML_BASLIK;
        public static final String XML_RENK = XML_RENK;
        public static final String YAZI = "yazi";
        public static final String XML_ID = "id";
        */
    }

    public static class customTextView extends TextView
    {
        private int cstID = -1;
        private boolean cstSeciliMi = false;

        public customTextView(Context context)
        {
            super(context);
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

    public static class customRelativeLayout extends RelativeLayout
    {
        private int cstID = -1;
        private boolean cstSeciliMi = false;

        public customRelativeLayout(Context context)
        {
            super(context);
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
