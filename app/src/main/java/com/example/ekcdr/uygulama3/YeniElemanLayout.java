package com.example.ekcdr.uygulama3;

import android.graphics.Color;
import android.util.TypedValue;
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
        //yeni kayıt fragmentinde baslik kısmı
        LinearLayout llBaslik = new LinearLayout(MainActivity.cnt);
        llBaslik.setId(llBaslikID);

        EditText etBaslik = new EditText(MainActivity.cnt);
        etBaslik.setId(etBaslikID);
        etBaslik.setSingleLine(true);
        etBaslik.setHint(MainActivity.cnt.getString(R.string.baslik));
        etBaslik.setBackground(null);//edittext te altcizgi cikmasin

        LinearLayout.LayoutParams lpBaslik = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lpBaslik.setMargins((int) dpGetir(20), (int) dpGetir(40), (int) dpGetir(20), (int) dpGetir(40));
        etBaslik.setLayoutParams(lpBaslik);

        llBaslik.addView(etBaslik);
        RelativeLayout.LayoutParams lpBaslik2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpBaslik2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        llBaslik.setLayoutParams(lpBaslik2);

        //yeni kayıt fragmentinde yazı kısmı
        LinearLayout llKayit = new LinearLayout(MainActivity.cnt);
        EditText etKayit = new EditText(MainActivity.cnt);
        etKayit.setId(etKayitID);
        etKayit.setHint(MainActivity.cnt.getString(R.string.not));
        etKayit.setBackground(null);//edittext te altcigi cikmasin
        LinearLayout.LayoutParams lpKayit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lpKayit.setMargins((int) dpGetir(20), 0, (int) dpGetir(20), 0);
        etKayit.setLayoutParams(lpKayit);
        llKayit.addView(etKayit);
        RelativeLayout.LayoutParams lpKayit2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpKayit2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpKayit2.addRule(RelativeLayout.BELOW, llBaslik.getId());
        llKayit.setLayoutParams(lpKayit2);

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
}
