package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.view.View;
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
    private int tur;//layout turu. ture gore gorunus degisecek. 0:kayit 1:klasor
    private int klasorID;//layut bir klasorse id sini tutuyor
    private MainActivity ma;

    public final static int TUR_KAYIT = 0;//layout turu kayit
    public final static int TUR_KLASOR = 1;//layout turu klasor


    public KayitLayout(Context context, String baslik, String icerik, String renk, String icerikTuruID, MainActivity ma)
    {
        super(context);
        this.baslik = baslik;
        this.icerik = icerik;
        this.renk = renk;
        this.icerikTuruID = icerikTuruID;
        this.ma = ma;
        setTur(TUR_KAYIT);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tiklandi(getTur());
            }
        });
    }

    public KayitLayout(Context context, String baslik, String renk, int klasorID, MainActivity ma)
    {
        super(context);
        this.baslik = baslik;
        this.renk = renk;
        this.klasorID = klasorID;
        this.ma = ma;
        setTur(TUR_KLASOR);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tiklandi(getTur());
            }
        });
    }

    /**
     * kayitLayout a tiklandigi zaman buraya geliyor
     *
     * @param tur : kayit yada klasor e tiklandigini anlamak icin
     */
    public void tiklandi(int tur)
    {
        switch (tur)
        {
            case TUR_KAYIT:
                break;

            case TUR_KLASOR:
                getMa().getEngine().klasorAc(getKlasorID(), Engine.HAREKET.ILERI);
                break;

            default:
                break;
        }
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

    public int getTur()
    {
        return tur;
    }

    public void setTur(int tur)
    {
        this.tur = tur;
    }

    public int getKlasorID()
    {
        return klasorID;
    }

    public void setKlasorID(int klasorID)
    {
        this.klasorID = klasorID;
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
