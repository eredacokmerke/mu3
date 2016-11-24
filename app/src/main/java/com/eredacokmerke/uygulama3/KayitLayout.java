package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by ekcdr on 11/15/16.
 */
public class KayitLayout extends RelativeLayout
{
    private String baslik;
    private String icerik;
    private String renk;
    private String icerikTuruID;

    public KayitLayout(Context context, String baslik, String icerik, String renk, String icerikTuruID)
    {
        super(context);
        this.baslik = baslik;
        this.icerik = icerik;
        this.renk = renk;
        this.icerikTuruID = icerikTuruID;
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
}
