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

    public boolean xmlDosyasiKontrolEt(String xmlDosyaYolu)
    {
        if (new File(xmlDosyaYolu).exists())//xml dosyası var mı
        {
            Document document = xmlDocumentNesnesiOlustur(xmlDosyaYolu);
            if (document == null)
            {
                HataYoneticisi.ekranaHataYazdir("4", MainActivity.getCnt().getString(R.string.xml_document_olusamadi));
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
                HataYoneticisi.ekranaHataYazdir("4", MainActivity.getCnt().getString(R.string.xml_document_olusamadi));
                return false;
            }
            else
            {
                xmlDosyasiniBellegeAl(document, xmlDosyaYolu);
                return true;
            }
        }
    }

    //xml i okumank için Document nesnesi olusturur
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
            HataYoneticisi.ekranaHataYazdir("28", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (FileNotFoundException e)
        {
            HataYoneticisi.ekranaHataYazdir("29", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (IOException e)
        {
            HataYoneticisi.ekranaHataYazdir("30", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
            return null;
        }
        catch (SAXException e)
        {
            HataYoneticisi.ekranaHataYazdir("15", MainActivity.getCnt().getString(R.string.xml_document_olusamadi) + e.getMessage() + " : " + xmlDosyaYolu);
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

    //xml dosyası yoksa oluşturuyor
    public void xmlDosyasiOlustur(String xmlDosyaYolu)
    {
    }

    public void xmlDosyasiniBellegeAl(Document document, String xmlDosyaYolu)
    {
    }
}
