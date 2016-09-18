package com.eredacokmerke.uygulama3;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class XmlVeri extends XmlYoneticisi
{
    private int xmlEnBuyukID;

    public XmlVeri(String xmlDosyaYolu)
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
            out.write("<?xml version=\"1.0\"?><" + SabitYoneticisi.XML_ROOT + ">" +
                    "<" + SabitYoneticisi.XML_PARCA + " " + SabitYoneticisi.XML_ID + "=\"0\">" +
                    "<" + SabitYoneticisi.XML_BASLIK + "/>" +
                    "<" + SabitYoneticisi.XML_RENK + ">" + SabitYoneticisi.KATEGORI_ONTANIMLI_RENK + "</" + SabitYoneticisi.XML_RENK + ">" +
                    "<" + SabitYoneticisi.XML_YAZILAR + "/>" +
                    "<" + SabitYoneticisi.XML_DURUM + ">" + SabitYoneticisi.DURUM_YENI + "</" + SabitYoneticisi.XML_DURUM + ">" +
                    "<" + SabitYoneticisi.XML_ALTPARCA + "/>" +
                    "</" + SabitYoneticisi.XML_PARCA + ">" +
                    "</" + SabitYoneticisi.XML_ROOT + ">");
            out.close();
        }
        catch (IOException e)
        {
            HataYoneticisi.ekranaHataYazdir("8", MainActivity.getCnt().getString(R.string.xml_olusturulamadi) + e.getMessage() + ", " + MainActivity.getCnt().getString(R.string.dosya) + " : " + xmlDosyaYolu);
        }
    }

    @Override
    public void xmlDosyasiniBellegeAl(Document document, String xmlDosyaYolu)
    {
        super.xmlDosyasiniBellegeAl(document, xmlDosyaYolu);

        setXmlEnBuyukID(enBuyukIDyiBul(document));
        XMLParser.parseXml(document, getXmlEnBuyukID());
    }


    /**
     * uygulama açıldığında xml dosyasındaki en büyük id yi buluyor ve id vermeye o sayıdan devam ediyor
     *
     * @param document  : xml e ait document nesnesi
     * @return xml deki en buyuk id
     */
    public int enBuyukIDyiBul(Document document)
    {
        int sonIDParca = 0;
        int sonIDKayit = 0;

        NodeList nodeListParca = document.getElementsByTagName(SabitYoneticisi.XML_PARCA);
        NodeList nodeListKayit = document.getElementsByTagName(SabitYoneticisi.XML_KAYIT);

        for (int i = 0; i < nodeListParca.getLength(); i++)
        {
            if (Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem(SabitYoneticisi.XML_ID).getNodeValue()) > sonIDParca)
            {
                sonIDParca = Integer.parseInt(nodeListParca.item(i).getAttributes().getNamedItem(SabitYoneticisi.XML_ID).getNodeValue());
            }
        }
        for (int i = 0; i < nodeListKayit.getLength(); i++)
        {
            if (Integer.parseInt(nodeListKayit.item(i).getAttributes().getNamedItem(SabitYoneticisi.XML_ID).getNodeValue()) > sonIDKayit)
            {
                sonIDKayit = Integer.parseInt(nodeListKayit.item(i).getAttributes().getNamedItem(SabitYoneticisi.XML_ID).getNodeValue());
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

    public int getXmlEnBuyukID()
    {
        return xmlEnBuyukID;
    }

    public void setXmlEnBuyukID(int xmlEnBuyukID)
    {
        this.xmlEnBuyukID = xmlEnBuyukID;
    }
}
