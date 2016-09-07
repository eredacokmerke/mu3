package com.eredacokmerke.uygulama3;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlAyar extends XmlYoneticisi
{
    public XmlAyar(String xmlDosyaYolu)
    {
        setXmlDosyasiDuzgnMu(xmlDosyasiKontrolEt(xmlDosyaYolu));
    }

    @Override
    public void xmlDosyasiOlustur(String xmlDosyaYolu)
    {
        super.xmlDosyasiOlustur(xmlDosyaYolu);

        try
        {
            //string i xml dosyasına yazıyor
            BufferedWriter out = new BufferedWriter(new FileWriter(xmlDosyaYolu));
            out.write("<?xml version=\"1.0\"?><root>" +
                    "<ayar id=\"" + SabitYoneticisi.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI + "\">" + SabitYoneticisi.ONTANIMLI_DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI + "</ayar>" +
                    "<ayar id=\"" + SabitYoneticisi.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN + "\">" + SabitYoneticisi.ONTANIMLI_DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN + "</ayar>" +
                    "<ayar id=\"" + SabitYoneticisi.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI + "\">" + SabitYoneticisi.ONTANIMLI_DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI + "</ayar>" +
                    //"<ayar id=\"" + Sabit.AYAR_ID_SIMGE_RENGI + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_SIMGE_RENGI + "</ayar>" +
                    //"<ayar id=\"" + Sabit.AYAR_ID_YAZI_RENGI + "\">" + Sabit.ONTANIMLI_DEGER_AYAR_YAZI_RENGI + "</ayar>" +
                    "</root>");
            out.close();
        }
        catch (IOException e)
        {
            HataYoneticisi.ekranaHataYazdir("9", MainActivity.getCnt().getString(R.string.xml_olusturulamadi) + " : " + e.getMessage() + "," + MainActivity.getCnt().getString(R.string.dosya) + " : " + xmlDosyaYolu);
        }
    }

    @Override
    public void xmlDosyasiniBellegeAl(Document document, String xmlDosyaYolu)
    {
        super.xmlDosyasiniBellegeAl(document, xmlDosyaYolu);

        yeniAyarVarsaEkle(document, xmlDosyaYolu);

    }

    //yeni ayar varsa xml ayar dosyasına ekle
    private void yeniAyarVarsaEkle(Document document, String xmlDosyaYolu)
    {
        boolean eksikAyarVarMi = false;
        List<Integer> xmldekiAyarlar = new ArrayList<>();
        org.w3c.dom.Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node nodeAyar = nodeList.item(i);
            int ayarID = Integer.valueOf(nodeAyar.getAttributes().getNamedItem(SabitYoneticisi.XML_ID).getNodeValue());

            xmldekiAyarlar.add(ayarID);
        }

        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);//ayar isimli etiket olşuturuluyor
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_SATIR_BASINA_KAYIT_SAYISI));//ayar etiketine id veriliyor
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI);//ayar etiketinin icerigi yazılıyor
            element.appendChild(elementAyar);//root etiketine ayar etiketi ekleniyor
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_SATIR_BOY_UZUNLUGU_SABIT_OLSUN));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_SATIR_BOY_UZUNLUGU_SABIT_OLSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_SUTUN_BASINA_KAYIT_SAYISI));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_SUTUN_BASINA_KAYIT_SAYISI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        /*
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_SIMGE_RENGI))
        {
            org.w3c.dom.Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_SIMGE_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_SIMGE_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        */
        /*
        if (!xmldekiAyarlar.contains(Sabit.AYAR_ID_YAZI_RENGI))
        {
            org.w3c.dom.Element elementAyar = documentAyar.createElement(Sabit.XML_AYAR);
            elementAyar.setAttribute(Sabit.XML_ID, String.valueOf(Sabit.AYAR_ID_YAZI_RENGI));
            elementAyar.setTextContent(Sabit.ONTANIMLI_DEGER_AYAR_YAZI_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        */
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_CERCEVE_GOZUKSUN))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_CERCEVE_GOZUKSUN));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_CERCEVE_GOZUKSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_CERCEVE_RENGI))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_CERCEVE_RENGI));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_CERCEVE_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_ARKAPLAN_RENGI_SABIT_OLSUN));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_ARKAPLAN_RENGI_SABIT_OLSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_ARKAPLAN_RENGI))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_ARKAPLAN_RENGI));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_ARKAPLAN_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_ACTIONBAR_RENGI_SABIT_OLSUN));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_ACTIONBAR_RENGI_SABIT_OLSUN);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }
        if (!xmldekiAyarlar.contains(SabitYoneticisi.AYAR_ID_ACTIONBAR_RENGI))
        {
            org.w3c.dom.Element elementAyar = document.createElement(SabitYoneticisi.XML_AYAR);
            elementAyar.setAttribute(SabitYoneticisi.XML_ID, String.valueOf(SabitYoneticisi.AYAR_ID_ACTIONBAR_RENGI));
            elementAyar.setTextContent(SabitYoneticisi.ONTANIMLI_DEGER_AYAR_ACTIONBAR_RENGI);
            element.appendChild(elementAyar);
            eksikAyarVarMi = true;
        }

        if (eksikAyarVarMi)
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
                HataYoneticisi.ekranaHataYazdir("31", MainActivity.getCnt().getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
            catch (TransformerException e)
            {
                HataYoneticisi.ekranaHataYazdir("32", MainActivity.getCnt().getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
            catch (IOException e)
            {
                HataYoneticisi.ekranaHataYazdir("33", MainActivity.getCnt().getString(R.string.document_dosyaya_yazilirken_hata_olustu) + " : " + e.getMessage());
            }
        }
    }
}


