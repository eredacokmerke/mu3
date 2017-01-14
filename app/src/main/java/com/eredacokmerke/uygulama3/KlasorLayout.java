package com.eredacokmerke.uygulama3;

import android.content.Context;

/**
 * Created by ekcdr on 1/11/17.
 */
public class KlasorLayout extends LayoutYoneticisi
{
    private String baslik;//klasorun basligi, layout ta ve toolbar da yazdirilacak
    private String renk;//layout un rengi
    private int klasorID;//layouta tiklaninca acilacak klasorun id si
    private int parentKlasorID;//layouta tiklaninca acilacak klasorun id si
    private MainActivity ma;//mainActivity nesnesi

    public KlasorLayout(Context context, String baslik, String renk, int klasorID, int parentKlasorID, MainActivity ma)
    {
        super(context);
        this.baslik = baslik;
        this.renk = renk;
        this.klasorID = klasorID;
        this.parentKlasorID = parentKlasorID;
        this.ma = ma;
    }

    @Override
    public void tiklandi()
    {
        super.tiklandi();

        getMa().getEngine().klasorAc(getKlasorID(), getParentKlasorID(), Engine.HAREKET.ILERI, getBaslik());
    }


    /////getter & setter/////


    public String getBaslik()
    {
        return baslik;
    }

    public void setBaslik(String baslik)
    {
        this.baslik = baslik;
    }

    public String getRenk()
    {
        return renk;
    }

    public void setRenk(String renk)
    {
        this.renk = renk;
    }

    public int getKlasorID()
    {
        return klasorID;
    }

    public void setKlasorID(int klasorID)
    {
        this.klasorID = klasorID;
    }

    public int getParentKlasorID()
    {
        return parentKlasorID;
    }

    public void setParentKlasorID(int parentKlasorID)
    {
        this.parentKlasorID = parentKlasorID;
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
