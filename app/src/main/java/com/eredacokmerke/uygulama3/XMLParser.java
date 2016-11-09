package com.eredacokmerke.uygulama3;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser
{
    /**
     * parca etiketinin altındaki yazi ve kategorileri ekrana basıyor
     *
     * @param document : xml ait document nesnesi
     * @param xmlID    : parse edilecek xml parcasinin id si
     */
    public static void parseXml(Document document, int xmlID)
    {
        org.w3c.dom.Element element = document.getElementById(String.valueOf(xmlID));
        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            if (nodeList.item(i).getNodeName().equals(SabitYoneticisi.XML_YAZILAR))
            {
                Node nodeYazilar = nodeList.item(i);
                NodeList nodeListKayitlar = nodeYazilar.getChildNodes();

                for (int j = 0; j < nodeListKayitlar.getLength(); j++)
                {
                    Node nodeKayit = nodeListKayitlar.item(j);

                    String kayitBaslik = etiketBilgisiniGetir((org.w3c.dom.Element) nodeKayit, SabitYoneticisi.XML_BASLIK);
                    String kayitRenk = etiketBilgisiniGetir((org.w3c.dom.Element) nodeKayit, SabitYoneticisi.XML_RENK);
                    String kayitYazi = etiketBilgisiniGetir((org.w3c.dom.Element) nodeKayit, SabitYoneticisi.XML_YAZI);
                    String kayitDurum = etiketBilgisiniGetir((org.w3c.dom.Element) nodeKayit, SabitYoneticisi.XML_DURUM);

                    String kayitID = nodeKayit.getAttributes().getNamedItem(SabitYoneticisi.XML_ID).getNodeValue();

                    //kayitlariAnaEkranaEkle(kayitBaslik, kayitYazi, Integer.parseInt(kayitID), kayitDurum, kayitRenk, globalYerlesim);
                }
            }
            else if (nodeList.item(i).getNodeName().equals(SabitYoneticisi.XML_ALTPARCA))
            {
                Node nodeAltParca = nodeList.item(i);
                NodeList nodeListParcalar = nodeAltParca.getChildNodes();

                for (int j = 0; j < nodeListParcalar.getLength(); j++)
                {
                    Node nodeParca = nodeListParcalar.item(j);

                    String kategoriBaslik = etiketBilgisiniGetir((org.w3c.dom.Element) nodeParca, SabitYoneticisi.XML_BASLIK);
                    String kategoriRenk = etiketBilgisiniGetir((org.w3c.dom.Element) nodeParca, SabitYoneticisi.XML_RENK);
                    String kategoriDurum = etiketBilgisiniGetir((org.w3c.dom.Element) nodeParca, SabitYoneticisi.XML_DURUM);


                    //String kategoriDurum = nodeParca.getAttributes().getNamedItem(Sabit.XML_DURUM).getNodeValue();
                    String kategoriID = nodeParca.getAttributes().getNamedItem(SabitYoneticisi.XML_ID).getNodeValue();

                    //kategoriyiAnaEkranaEkle(kategoriBaslik, Integer.parseInt(kategoriID), kategoriDurum, kategoriRenk, globalYerlesim);
                }
            }
        }
    }

    /**
     * etiket isminden etiket değerini dondurur
     *
     * @param element   : xml elementi
     * @param etiketAdi : degeri alinacak xml etiketi
     * @return xml etiketinin degerini doner, hata olusursa bos string doner
     */
    public static String etiketBilgisiniGetir(org.w3c.dom.Element element, String etiketAdi)
    {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            if (nodeList.item(i).getNodeName().equals(etiketAdi))
            {
                return nodeList.item(i).getTextContent();
            }
        }

        HataYoneticisi.ekranaHataYazdir("14", MainActivity.getCnt().getString(R.string.xml_etiket_okunamadi) + " : " + etiketAdi);
        return "";
    }

}
