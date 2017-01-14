package com.eredacokmerke.uygulama3;

import android.content.Context;

/**
 * Created by ekcdr on 11/15/16.
 */
public class KayitLayout extends LayoutYoneticisi
{
    private String baslik;//kaydin basligi, layoutta yatacak
    private String icerik;//kaydin icerigi
    private String renk;//kaydin rengi
    private String icerikTuruID;//kayitta gosterilecek icerigin id si
    private int kayitID;//kaydin id si
    private MainActivity ma;

    /**
     * kayitlari gostermek icin constructor
     *
     * @param context
     * @param baslik
     * @param icerik
     * @param renk
     * @param icerikTuruID
     * @param ma
     */
    public KayitLayout(Context context, final String baslik, String icerik, String renk, String icerikTuruID, int kayitID, MainActivity ma)
    {
        super(context);
        this.baslik = baslik;
        this.icerik = icerik;
        this.renk = renk;
        this.icerikTuruID = icerikTuruID;
        this.kayitID = kayitID;
        this.ma = ma;
    }

    @Override
    public void tiklandi()
    {
        super.tiklandi();

        getMa().getEngine().kayitAc(getKayitID(), getBaslik());
    }


    /////getter & setter/////


    public String getRenk()
    {
        return renk;
    }

    public void setRenk(String renk)
    {
        this.renk = renk;
    }

    public String getIcerik()
    {
        return icerik;
    }

    public void setIcerik(String icerik)
    {
        this.icerik = icerik;
    }

    public String getBaslik()
    {
        return baslik;
    }

    public void setBaslik(String baslik)
    {
        this.baslik = baslik;
    }

    public String getIcerikTuruID()
    {
        return icerikTuruID;
    }

    public void setIcerikTuruID(String icerikTuruID)
    {
        this.icerikTuruID = icerikTuruID;
    }

    public int getKayitID()
    {
        return kayitID;
    }

    public void setKayitID(int kayitID)
    {
        this.kayitID = kayitID;
    }

    public MainActivity getMa()
    {
        return ma;
    }

    public void setMa(MainActivity ma)
    {
        this.ma = ma;
    }
}
