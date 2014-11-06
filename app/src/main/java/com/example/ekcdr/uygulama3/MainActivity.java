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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private final String UYGULAMA_ADI = "uygulama3";
    //private static String TAG = "uyg3";
    private static File xmlKlasorYolu;
    private static String xmlDosyaYolu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00CED1")));
    }

    //xml dosyası var mı diye kontrol ediyor. yoksa oluşturuyor
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

            NodeList nodeListParca = doc.getElementsByTagName("parca");
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
            out.write("<?xml version=\"1.0\"?><root></root>");
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

                InputStream is = new ByteArrayInputStream(xmlMetin.getBytes("UTF-8"));
                return is;
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

        //http://android-developers.blogspot.com.tr/2011/12/watch-out-for-xmlpullparsernexttext.html
        private String safeNextText(XmlPullParser parser) throws XmlPullParserException, IOException
        {
            String result = parser.nextText();
            if (parser.getEventType() != XmlPullParser.END_TAG)
            {
                parser.nextTag();
            }
            return result;
        }

        public void parseIlkNesil()
        {
            try
            {
                XmlPullParser parser;
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(fileToIS(), null);

                xmlYapisi yapi = new xmlYapisi();
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    String tagName = parser.getName();

                    if (eventType == XmlPullParser.START_TAG)
                    {
                        if (parser.getDepth() == 3)//root'un altındaki ilk girişler
                        {
                            if (tagName.equals("parca"))
                            {
                                //Log.d(TAG, "parca : " + safeNextText(parser));
                                //Log.e("id", parser.getAttributeValue(null, "id")+" "+parser.getDepth());
                            }
                            else if (tagName.equals("baslik"))
                            {
                                yapi.mBaslik = safeNextText(parser);
                            }
                            else if (tagName.equals("renk"))
                            {
                                yapi.mRenk = safeNextText(parser);
                            }
                            else if (tagName.equals("yazi"))
                            {
                                yapi.mYazi = safeNextText(parser);
                            }
                            else if (tagName.equals("altparca"))
                            {
                                alanEkleKendiligindenEkle(yapi.mBaslik, yapi.mRenk, yapi.mYazi, false);
                            }
                        }
                    }
                    eventType = parser.next();
                }
                yapi = null;
            }
            catch (XmlPullParserException e)
            {
                Log.e("hata[24]", e.getMessage());
                ekranaHataYazdir("24", e);
            }
            catch (IOException e)
            {
                Log.e("hata[24-2]", e.getMessage());
                ekranaHataYazdir("24", e);
            }
        }

        //xml okunduktan xml deki bilgilere göre bir üst seviye alanlarını oluşturuyor
        public void alanEkleKendiligindenEkle(final String baslik, final String staticrenk, final String yazi, boolean cerceve)
        {
            LinearLayout ll = alanOlustur(baslik);
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
        public void eklenecekXml(String baslik, String renk)
        {
            xmlID++;
            try
            {
                //if (txtIsaretci.getText().equals("root"))
                if (true)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setValidating(false);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new FileInputStream(new File(xmlDosyaYolu)));

                    Node nodeRoot = doc.getElementsByTagName("root").item(0);//ilk görülen root tagını alıyor
                    Element yeniNodeParca = doc.createElement("parca");//parca isimli elemanı oluşturuyor
                    yeniNodeParca.setAttribute("id", String.valueOf(xmlID));//parca icinde id isimli özellik oluşturuluyor
                    nodeRoot.appendChild(yeniNodeParca);//root etiketine parca etiketi ekleniyor

                    Node nodeParca = doc.getElementById(String.valueOf(xmlID));//xmlid id sine sahip eleman alınıyor. üstte oluşturulan parca etiketi
                    Element yeniNodeBaslik = doc.createElement("baslik");//baslik isimli etiket oluşturuluyor
                    yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri ekleniyor
                    Element yeniNodeRenk = doc.createElement("renk");//renk isimli etiket oluşturuluyor
                    yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri ekleniyor
                    Element yeniNodeYazi = doc.createElement("yazi");//yazi isimli etiket oluşturuluyor
                    Element yeniNodeAltparca = doc.createElement("altparca");//altparca isimli etiket oluşturuluyor
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
                        if (nodeParcaCocuklari.item(i).getNodeName().equals("altparca"))//parcanın içindeki altparca etiketine ulaşılıyor
                        {
                            Node nodeAltparca = nodeParcaCocuklari.item(i);//altparcaya giriliyor
                            Element yeniNodeParca = doc.createElement("parca");//parca isimli etiket olşuturuluyor
                            yeniNodeParca.setAttribute("id", String.valueOf(xmlID));//parca ya id özelliği ekleniyor
                            nodeAltparca.appendChild(yeniNodeParca);//altparca etiketine parca ekleniyor

                            Node nodeParca = doc.getElementById(String.valueOf(xmlID));//xmlid id sine sahip parca nın içine giriliyor. az önce oluşturulan parca
                            Element yeniNodeBaslik = doc.createElement("baslik");//baslik etiketi oluşturuluyor
                            yeniNodeBaslik.setTextContent(baslik);//baslik etiketine baslik değeri giriliyor
                            Element yeniNodeRenk = doc.createElement("renk");//renk etiketi oluşturuluyor
                            yeniNodeRenk.setTextContent(renk);//renk etiketine renk değeri giriliyor
                            Element yeniNodeYazi = doc.createElement("yazi");//yazi etiketi oluşturuluyor
                            Element yeniNodeAltparca = doc.createElement("altparca");//altparca etiketi oluşturuluyor
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

        public LinearLayout alanOlustur(String yazi)//ana ekrana eklenecek layout u olusturuyor
        {
            LinearLayout ll = new LinearLayout(getActivity());//tamam'a tıklanıldığı zaman ana ekrana eklenecek küçük ekran
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            pa.setMargins(0, 10, 0, 10);
            ll.setLayoutParams(pa);
            ll.setBackground(getResources().getDrawable(R.drawable.ana_ekran));

            TextView tv = new TextView(getActivity());
            tv.setTextSize(30);
            tv.setText(yazi);
            tv.setTextColor(Color.WHITE);
            ll.addView(tv);

            return ll;
        }

        public void alanEkle()
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

            builder.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için butaya null değeri giriyorum
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
                        LinearLayout ll = alanOlustur(kategoriAdi);
                        anaLayout.addView(ll);
                        eklenecekXml(kategoriAdi, "");
                    }
                }
            });
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.action_alan_ekle:
                    alanEkle();
                    return true;
                case R.id.action_kayit_ekle:
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
                parseIlkNesil();
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

        public static final String PARCA = "parca";
        public static final String BASLIK = "baslik";
        public static final String RENK = "renk";
        public static final String YAZI = "yazi";
        public static final String ID = "id";
    }
}
