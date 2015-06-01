package com.example.ekcdr.uygulama3;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AyarlarRelativeLayout extends RelativeLayout
{
    private String metin;
    private String deger;
    private int ayarID;
    private EditText etSecenek;
    int ekranEnUzunluğu = getResources().getDisplayMetrics().widthPixels;

    public AyarlarRelativeLayout(Context context, String metin, String deger, int ayarID)
    {
        super(context);
        this.metin = metin;
        this.deger = deger;
        this.ayarID = ayarID;
        setId(ayarID);

        metniOlustur(context);
        secenegiOlustur(context);

        if (ayarID != 1)//ilk ayar için layoutParam a gerek yok
        {
            int oncekiID = ayarID - 1;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, oncekiID);
            this.setLayoutParams(lp);
        }
    }

    //ayar metni yazılıyor
    public void metniOlustur(Context cnt)
    {
        TextView tvAyar = new TextView(cnt);
        tvAyar.setText(metin);
        RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams((ekranEnUzunluğu / 10) * 6, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        pa.addRule(RelativeLayout.CENTER_VERTICAL);
        tvAyar.setLayoutParams(pa);
        tvAyar.setTextSize(20);
        this.addView(tvAyar);
    }

    //kullanıcının yapacagı secim elemanı olusturuluyor
    public void secenegiOlustur(Context cnt)
    {
        etSecenek = new EditText(cnt);
        RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams((ekranEnUzunluğu / 10) * 2, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pa.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        etSecenek.setLayoutParams(pa);
        etSecenek.setTextSize(20);
        etSecenek.setText(deger);
        etSecenek.setGravity(Gravity.CENTER_HORIZONTAL);
        etSecenek.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.addView(etSecenek);
    }

    public int getAyarID()
    {
        return ayarID;
    }

    public EditText getEtSecenek()
    {
        return etSecenek;
    }
}
