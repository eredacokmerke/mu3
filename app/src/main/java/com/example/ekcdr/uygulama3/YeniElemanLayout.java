package com.example.ekcdr.uygulama3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class YeniElemanLayout
{
    private RelativeLayout anaRelativeLayout;
    private CustomRelativeLayout seciliCRL;
    private int ekranTuru;
    private int llBaslikID = 1000;
    private int etBaslikID = 1001;
    private int etKayitID = 1002;

    public YeniElemanLayout(RelativeLayout anaRelativeLayout, CustomRelativeLayout seciliCRL, int ekranTuru)
    {
        this.anaRelativeLayout = anaRelativeLayout;
        this.seciliCRL = seciliCRL;
        this.ekranTuru = ekranTuru;

        layoutOlustur();
    }

    public void layoutOlustur()
    {
        //yeni fragmentinde baslik kısmı
        LinearLayout llBaslik = new LinearLayout(MainActivity.cnt);
        llBaslik.setId(llBaslikID);

        EditText etBaslik = new EditText(MainActivity.cnt);
        etBaslik.setId(etBaslikID);
        etBaslik.setSingleLine(true);
        etBaslik.setHint(MainActivity.cnt.getString(R.string.baslik));
        etBaslik.setBackground(null);//edittext te altcizgi cikmasin

        LinearLayout.LayoutParams lpETBaslik = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lpETBaslik.setMargins((int) dpGetir(20), (int) dpGetir(40), (int) dpGetir(20), (int) dpGetir(40));
        etBaslik.setLayoutParams(lpETBaslik);

        llBaslik.addView(etBaslik);
        RelativeLayout.LayoutParams lpLLBaslik = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpLLBaslik.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        llBaslik.setLayoutParams(lpLLBaslik);

        //yeni fragmentinde yazı kısmı
        LinearLayout llKayit = new LinearLayout(MainActivity.cnt);
        EditText etKayit = new EditText(MainActivity.cnt);
        etKayit.setId(etKayitID);
        etKayit.setHint(MainActivity.cnt.getString(R.string.not));
        etKayit.setHintTextColor(Color.DKGRAY);
        etKayit.setBackground(null);//edittext te altcizgi cikmasin
        etKayit.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams lpETKayit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lpETKayit.setMargins((int) dpGetir(20), 0, (int) dpGetir(20), 0);
        etKayit.setLayoutParams(lpETKayit);
        llKayit.addView(etKayit);

        RelativeLayout.LayoutParams lpLLKayit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpLLKayit.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpLLKayit.addRule(RelativeLayout.BELOW, llBaslik.getId());
        llKayit.setLayoutParams(lpLLKayit);

        switch (ekranTuru)
        {
            case Sabit.FRAGMENT_KATEGORI_DUZENLE_EKRANI:
                llBaslik.setBackgroundColor(Color.parseColor(seciliCRL.getRenk()));
                etBaslik.setText(seciliCRL.getBaslik());
                break;

            case Sabit.FRAGMENT_YENI_KATEGORI_EKRANI:
                llBaslik.setBackgroundColor(Color.parseColor(Sabit.KATEGORI_ONTANIMLI_RENK));
                break;

            case Sabit.FRAGMENT_YENI_KAYIT_EKRANI:
                llBaslik.setBackgroundColor(Color.parseColor(Sabit.KAYIT_ONTANIMLI_RENK));
                break;

            case Sabit.FRAGMENT_KAYIT_EKRANI:
                llBaslik.setBackgroundColor(Color.parseColor(seciliCRL.getRenk()));
                etBaslik.setText(seciliCRL.getTvBaslik().getText());
                etKayit.setText(seciliCRL.getTvYazi().getText());
                break;
        }

        if (renkKoyuMu(arkaplanRenginiGetir(llBaslik)))
        {
            etBaslik.setHintTextColor(Color.LTGRAY);
            etBaslik.setTextColor(Color.WHITE);
        }
        else
        {
            etBaslik.setHintTextColor(Color.DKGRAY);
            etBaslik.setTextColor(Color.BLACK);
        }

        LinearLayout llYeniKayit = new LinearLayout(MainActivity.cnt);
        llYeniKayit.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams lpYeniKayit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        llYeniKayit.setLayoutParams(lpYeniKayit);
        anaRelativeLayout.addView(llBaslik);
        anaRelativeLayout.addView(llKayit);
    }

    //px birimini dp ye cevirir
    public float dpGetir(int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, MainActivity.cnt.getResources().getDisplayMetrics());
    }

    //verilen rengin koyu,açık bilgisini döndürür
    public boolean renkKoyuMu(String renk)
    {
        int irenk = Color.parseColor(renk);
        double darkness = 1 - (0.299 * Color.red(irenk) + 0.587 * Color.green(irenk) + 0.114 * Color.blue(irenk)) / 255;
        if (darkness < 0.5)
        {
            return false; //açık renk
        }
        else
        {
            return true; //koyu renk
        }
    }

    //view'ın arka plan rengini getirir
    public String arkaplanRenginiGetir(View v)
    {
        String renkKodu = Integer.toHexString(((ColorDrawable) v.getBackground()).getColor()).toUpperCase();
        renkKodu = renkKodu.substring(2, 8);//alfa degerini siliyorum
        renkKodu = "#" + renkKodu;
        return renkKodu;
    }
}
