package com.example.ekcdr.uygulama3;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    //private static String TAG = "uyg3";
    private static File xmlKlasorYolu;
    private static String xmlDosyaYolu;
    private static final String PARCA = "parca";
    private static final String RENK = "renk";
    private static final String BASLIK = "baslik";
    private static final String YAZILAR = "yazilar";
    private static final String ROOT = "root";
    private static final String KAYIT = "kayit";
    private static final String ALTPARCA = "altparca";
    private static ColorDrawable actionBarArkaPlan;

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
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment(sonuc)).commit();
        }

        ActionBar bar =  getActionBar();
        actionBarArkaPlan = new ColorDrawable(Color.parseColor("#009ED1"));
        bar.setBackgroundDrawable(actionBarArkaPlan);
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
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return false;
        }
        else
        {
            return false;
        }
    }

    //uygulama açıldığında xml dosyasındaki en büyük id yi buluyor ve id vermeye o sayıdan devam ediyor
    public int sonIDyiBul()
    {
        int sonID = 0;
        Log.d("proje32", "xmlDosyaYolu : " + xmlDosyaYolu);
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

            NodeList nodeListParca = doc.getElementsByTagName(PARCA);
            for (int i = 0; i < nodeListParca.getLength(); i++)
            {
                if (Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem("id").getNodeValue()) > sonID)
                {
                    sonID = Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem("id").getNodeValue());
                }
            }
        }
        catch (IOException e)
        {
            Log.e("hata[1]", e.getMessage());
            ekranaHataYazdir("1", e);
        }
        catch (ParserConfigurationException e)
        {
            Log.e("hata[2]", e.getMessage());
            ekranaHataYazdir("2", e);
        }
        catch (SAXException e)
        {
            Log.e("hata[28]", e.getMessage());
            ekranaHataYazdir("28", e);
        }
        return sonID;
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
            ekranaHataYazdir("3", e);
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
            case R.id.action_alan_ekle://fragment sınıfının içinde yakaladığım için false döndürüyor
                return false;
            case R.id.action_kayit_ekle://fragment sınıfının içinde yakaladığım için false döndürüyor
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ekranaHataYazdir(String id, Exception e)
    {
        Toast.makeText(getApplicationContext(), "hata[" + id + "]: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public static class PlaceholderFragment extends Fragment
    {
        private LinearLayout anaLayout;
        private int xmlID;
        private String xmlIsaretciID = "0";//içinde olunan parçanın id si
        private MenuInflater inflaterActionBar;
        private Menu menuActionBar;
        private int xmlIsaretciNesil = 2;//içinde olunan parcanın nesli
        private String TAG = "uyg3";
        private EditText etEklenecek;

        public PlaceholderFragment(int sonuc)
        {
            xmlID = sonuc;
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
                ekranaHataYazdir("12", e);
                return null;
            }
            catch (IOException e)
            {
                Log.e("hata[13]", e.getMessage());
                ekranaHataYazdir("13", e);
                return null;
            }
        }

        public void ekranaHataYazdir(String id, Exception e)
        {
            Toast.makeText(getActivity(), "hata[" + id + "]: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //parcanın altparcalarını ekrana döşüyor
        public String parseXml(String parcaID)
        {
            String tabanRenk = "";//içine girilen parçanın taban rengi
            String parcaYazi = "";//parçanın yazısı
            boolean dogruParca = false;//doğru parcanın altparcalarını yazdırması için
            try
            {
                XmlPullParser parser;
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(fileToIS(), null);

                xmlYapisi study = new xmlYapisi();
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    String tagName = parser.getName();
                    if (eventType == XmlPullParser.START_TAG)
                    {
                        //Log.d(TAG, "tagName : "+tagName + " parser.getDepth() : "+parser.getDepth() + " xmlIsaretciNesil : " + xmlIsaretciNesil);
                        if (parser.getDepth() == xmlIsaretciNesil)
                        {
                            if (tagName.equals(PARCA))
                            {
                                dogruParca = parcaID.equals(parser.getAttributeValue(null, "id"));
                            }
                            //Log.d(TAG, "dogru parca : "+dogruParca);
                        }
                        else if (parser.getDepth() == xmlIsaretciNesil + 1)
                        {
                            if (dogruParca == true)
                            {
                                if (tagName.equals(RENK))//tabanın rengini alıyor
                                {
                                    tabanRenk = parser.nextText();
                                }
                                else if (tagName.equals(BASLIK))//içine girilen parcanın baslığını yukarıya yazdırıyor
                                {
                                    //txtIsaretci.setText(parser.nextText());
                                }
                                else if (tagName.equals("yazi"))
                                {
                                    parcaYazi = parser.nextText();
                                }
                            }
                        }
                        else if (parser.getDepth() == xmlIsaretciNesil + 2)
                        {
                            if (dogruParca == true)
                            {
                                if (tagName.equals(KAYIT))
                                {
                                    String ID = parser.getAttributeValue(null, "id");
                                    kayitlariAnaEkranaEkle(parser.nextText(), Integer.parseInt(ID));
                                }
                            }
                        }
                        else if (parser.getDepth() == xmlIsaretciNesil + 3)
                        {
                            if (dogruParca == true)
                            {
                                if (tagName.equals(BASLIK))
                                {
                                    study.mBaslik = parser.nextText();
                                }
                                else if (tagName.equals(RENK))
                                {
                                    study.mRenk = parser.nextText();
                                }
                                else if (tagName.equals("yazi"))
                                {
                                    study.mYazi = parser.nextText();
                                }
                                else if (tagName.equals(ALTPARCA))
                                {
                                    if (study.mRenk.equals(tabanRenk))
                                    {
                                        kategorileriAnaEkranaEkle(study.mBaslik, study.mRenk, study.mYazi, true);
                                    }
                                    else
                                    {
                                        kategorileriAnaEkranaEkle(study.mBaslik, study.mRenk, study.mYazi, false);
                                    }
                                }
                            }
                        }
                    }
                    else if (eventType == XmlPullParser.END_TAG)
                    {
                        if (parser.getDepth() == xmlIsaretciNesil + 1)
                        {
                            if (dogruParca == true)
                            {
                                if (tagName.equals(ALTPARCA))
                                {
                                    break;
                                }
                            }
                        }
                    }
                    eventType = parser.next();
                }
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[14]", e.getMessage());
                ekranaHataYazdir("14", e);
            }
            catch (IOException e)
            {
                Log.e("hata[15]", e.getMessage());
                ekranaHataYazdir("15", e);
            }
            return parcaYazi;
        }

        //xml parse edildikten sonra kayitları ana ekrana ekler
        public void kayitlariAnaEkranaEkle(String yazi, int eklenenID)
        {
            customTextView tv = new customTextView(getActivity());
            tv.setCstID(eklenenID);
            tv.setText(yazi);
            tv.setBackground(getResources().getDrawable(R.drawable.ana_ekran_kayit));
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

                    final int parcaID = ((customTextView)view).getCstID();
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
                                ekranaHataYazdir("20", e);
                            }
                            catch (ParserConfigurationException e)
                            {
                                Log.e("hata[21]", e.getMessage());
                                ekranaHataYazdir("21", e);
                            }
                            catch (IOException e)
                            {
                                Log.e("hata[22]", e.getMessage());
                                ekranaHataYazdir("22", e);
                            }
                            catch (SAXException e)
                            {
                                Log.e("hata[23]", e.getMessage());
                                ekranaHataYazdir("23", e);
                            }
                            alert.dismiss();
                        }
                    });

                    return false;
                }
            });
            anaLayout.addView(tv);
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        public void kategorileriAnaEkranaEkle(final String baslik, final String staticrenk, final String yazi, boolean cerceve)
        {
            LinearLayout ll = kategoriAlaniOlustur(baslik);
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
                ekranaHataYazdir("9", e);
            }
            catch (TransformerException e)
            {
                Log.e("hata[10]", e.getMessage());
                ekranaHataYazdir("10", e);
            }
            catch (IOException e)
            {
                Log.e("hata[11]", e.getMessage());
                ekranaHataYazdir("11", e);
            }
        }

        //başta oluşturulan xml e yeni eklenecek kısımları ekliyor
        public void xmlDosyasiniGuncelle(String baslik, String renk)
        {
            xmlID++;
            try
            {
                //if (txtIsaretci.getText().equals(ROOT))
                if (false)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Node nodeRoot = doc.getElementsByTagName(ROOT).item(0);//ilk görülen root tagını alıyor
                    Element yeniNodeParca = doc.createElement(PARCA);//parca isimli elemanı oluşturuyor
                    yeniNodeParca.setAttribute("id", String.valueOf(xmlID));//parca icinde id isimli özellik oluşturuluyor
                    nodeRoot.appendChild(yeniNodeParca);//root etiketine parca etiketi ekleniyor

                    Node nodeParca = doc.getElementById(String.valueOf(xmlID));//xmlid id sine sahip eleman alınıyor. üstte oluşturulan parca etiketi
                    Element yeniNodeBaslik = doc.createElement(BASLIK);//baslik isimli etiket oluşturuluyor
                    yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri ekleniyor
                    Element yeniNodeRenk = doc.createElement(RENK);//renk isimli etiket oluşturuluyor
                    yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri ekleniyor
                    Element yeniNodeYazi = doc.createElement(YAZILAR);//yazi isimli etiket oluşturuluyor
                    Element yeniNodeAltparca = doc.createElement(ALTPARCA);//altparca isimli etiket oluşturuluyor
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

                    Node nodeMevcutParca = doc.getElementById(String.valueOf(xmlIsaretciID));//içinde bulunulan parcaya giriyor
                    NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                    for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                    {
                        if (nodeParcaCocuklari.item(i).getNodeName().equals(ALTPARCA))//parcanın içindeki altparca etiketine ulaşılıyor
                        {
                            Node nodeAltparca = nodeParcaCocuklari.item(i);//altparcaya giriliyor
                            Element yeniNodeParca = doc.createElement(PARCA);//parca isimli etiket olşuturuluyor
                            yeniNodeParca.setAttribute("id", String.valueOf(xmlID));//parca ya id özelliği ekleniyor
                            nodeAltparca.appendChild(yeniNodeParca);//altparca etiketine parca ekleniyor

                            Node nodeParca = doc.getElementById(String.valueOf(xmlID));//xmlid id sine sahip parca nın içine giriliyor. az önce oluşturulan parca
                            Element yeniNodeBaslik = doc.createElement(BASLIK);//baslik etiketi oluşturuluyor
                            yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri giriliyor
                            Element yeniNodeRenk = doc.createElement(RENK);//renk etiketi oluşturuluyor
                            yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri giriliyor
                            Element yeniNodeYazi = doc.createElement("yazi");//yazi etiketi oluşturuluyor
                            Element yeniNodeAltparca = doc.createElement(ALTPARCA);//altparca etiketi oluşturuluyor
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
                ekranaHataYazdir("4", e);
            }
            catch (SAXException e)
            {
                Log.e("hata[5]", e.getMessage());
                ekranaHataYazdir("5", e);
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[6]", e.getMessage());
                ekranaHataYazdir("6", e);
            }
        }

        //ana ekrana eklenecek kategori alanini olusturuyor
        public LinearLayout kategoriAlaniOlustur(String yazi)
        {
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
                        alert.dismiss();
                        LinearLayout ll = kategoriAlaniOlustur(kategoriAdi);
                        anaLayout.addView(ll);
                        xmlDosyasiniGuncelle(kategoriAdi, "");
                    }
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

                Node nodeMevcutParca = doc.getElementById(String.valueOf(xmlIsaretciID));//içinde bulunulan parcaya giriyor
                NodeList nodeParcaCocuklari = nodeMevcutParca.getChildNodes();//parcanın cocuk etiketleri alnıyor
                for (int i = 0; i < nodeParcaCocuklari.getLength(); i++)
                {
                    if (nodeParcaCocuklari.item(i).getNodeName().equals(YAZILAR))//parcanın içindeki yazilar etiketine ulaşılıyor
                    {
                        int eklenenID = xmlID;
                        Element yeniNodeKayit = doc.createElement(KAYIT);//kayıt etiketi olsuturuyor
                        yeniNodeKayit.setAttribute("id", String.valueOf(xmlID));//parca ya id özelliği ekleniyor
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
                        kayitlariAnaEkranaEkle(alanYazi, eklenenID);

                        break;
                    }
                }
                doc.normalize();
                documentToFile(doc);
            }
            catch (ParserConfigurationException e)
            {
                Log.e("hata[16]", e.getMessage());
                ekranaHataYazdir("16", e);
            }
            catch (FileNotFoundException e)
            {
                Log.e("hata[17]", e.getMessage());
                ekranaHataYazdir("17", e);
            }
            catch (IOException e)
            {
                Log.e("hata[18]", e.getMessage());
                ekranaHataYazdir("18", e);
            }
            catch (SAXException e)
            {
                Log.e("hata[19]", e.getMessage());
                ekranaHataYazdir("19", e);
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
                case R.id.action_alan_ekle:
                    kategoriKaydet();
                    return true;
                case R.id.action_kayit_ekle:
                    etEklenecek = yaziAlaniOlustur();
                    klavyeAc(getActivity(), etEklenecek);
                    return true;
                case R.id.action_tamam:
                    //yaziyiKaydet(etEklenecek.getText().toString());
                    yaziyiKaydet(etEklenecek);
                    actionBarIlk();
                    return true;
                case R.id.action_iptal:
                    klavyeKapat(getActivity(), etEklenecek.getWindowToken());
                    actionBarIlk();
                    return true;
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
                parseXml("0");
                //parseIlkNesil();
            }
            return rootView;
        }
    }

    public static class xmlYapisi
    {
        public String mRenk;
        public String mBaslik;
        public String mYazi;
        public String mID;

        /*
        public static final String PARCA = PARCA;
        public static final String BASLIK = BASLIK;
        public static final String RENK = RENK;
        public static final String YAZI = "yazi";
        public static final String ID = "id";
        */
    }

    public static class customTextView extends TextView
    {
        private int cstID = -1;

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
    }
}
