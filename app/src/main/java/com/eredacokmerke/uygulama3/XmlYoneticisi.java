package com.eredacokmerke.uygulama3;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlYoneticisi
{
    //private Document document;
    private boolean xmlDosyasiDuzgnMu;

    /**
     * xml dosyasinin olup olmadigina bakar
     *
     * @param xmlDosyaYolu : xml dosyasinin dosya yolu
     * @return hata varsa false, yoksa true doner
     */
    public boolean xmlDosyasiKontrolEt(String xmlDosyaYolu)
    {
        if (new File(xmlDosyaYolu).exists())
        {
            Document document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);
            if (document == null)
            {
                HataYoneticisi.ekranaHataYazdir("8", MainActivity.getCnt().getString(R.string.xml_document_olusamadi));
                return false;
            }
            else
            {
                xmlDosyasiniBellegeAl(document, xmlDosyaYolu);
                return true;
            }
        }
        else
        {
            xmlDosyasiOlustur(xmlDosyaYolu);
            Document document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);
            if (document == null)
            {
                HataYoneticisi.ekranaHataYazdir("9", MainActivity.getCnt().getString(R.string.xml_document_olusamadi));
                return false;
            }
            else
            {
                xmlDosyasiniBellegeAl(document, xmlDosyaYolu);
                return true;
            }
        }
    }

    /**
     * xml i okumank için Document nesnesi olusturur
     * @param xmlDosyaYolu : xml dosyasinin dosya yolu
     * @return xml e ait document nesnesi
     */
    public Document xmlDocumentNesnesiOlustur(String xmlDosyaYolu)
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
            HataYoneticisi.ekranaHataYazdir("10", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (FileNotFoundException e)
        {
            HataYoneticisi.ekranaHataYazdir("11", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (IOException e)
        {
            HataYoneticisi.ekranaHataYazdir("12", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (SAXException e)
        {
            HataYoneticisi.ekranaHataYazdir("13", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
    }

    public boolean isXmlDosyasiDuzgnMu()
    {
        return xmlDosyasiDuzgnMu;
    }

    public void setXmlDosyasiDuzgnMu(boolean xmlDosyasiDuzgnMu)
    {
        this.xmlDosyasiDuzgnMu = xmlDosyasiDuzgnMu;
    }

    /**
     * ontanimli degerlerle xml dosyasi olusturuyor
     *
     * @param xmlDosyaYolu : xml dosyasinin dosya yolu
     */
    public void xmlDosyasiOlustur(String xmlDosyaYolu)
    {
        //override
    }

    /**
     * xml dosyasindaki verileri degiskenlere atar
     *
     * @param document      : xml in document nesnesi
     * @param xmlDosyaYolu  : xml dosyasinin dosya yolu
     */
    public void xmlDosyasiniBellegeAl(Document document, String xmlDosyaYolu)
    {
        //override
    }
}
