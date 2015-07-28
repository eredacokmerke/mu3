package com.example.ekcdr.uygulama3;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AyarlarRelativeLayout extends RelativeLayout
{
    int ekranEnUzunluğu = getResources().getDisplayMetrics().widthPixels;
    private String metin;
    private String deger;
    private int ayarID;
    private View viewSecenek;
    private AyarlarRelativeLayout aa = this;
    private AlertDialog alertRenk;//renkleri soran alertDialog. renk dugmesine dokunuca alertDialogu kapatabilmek için
    private String secilenRenk;

    public AyarlarRelativeLayout(Context context, String metin, String deger, int ayarID, int secenekTuru)
    {
        super(context);
        this.metin = metin;
        this.deger = deger;
        this.ayarID = ayarID;
        setId(ayarID);

        metniOlustur(context);
        secenegiOlustur(context, secenekTuru);

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
    public void secenegiOlustur(final Context cnt, int secenekTuru)
    {
        switch (secenekTuru)
        {
            case MainActivity.SECENEK_EDITTEXT:
                viewSecenek = new EditText(cnt);
                EditText etSecenek = (EditText) viewSecenek;
                RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams((ekranEnUzunluğu / 10) * 2, RelativeLayout.LayoutParams.WRAP_CONTENT);
                pa.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                etSecenek.setLayoutParams(pa);
                etSecenek.setTextSize(20);
                etSecenek.setText(deger);
                etSecenek.setGravity(Gravity.CENTER_HORIZONTAL);
                etSecenek.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.addView(etSecenek);
                break;

            case MainActivity.SECENEK_CHECKBOX:
                LinearLayout ll = new LinearLayout(cnt);
                ll.setGravity(Gravity.CENTER_HORIZONTAL);
                viewSecenek = new CheckBox(cnt);
                CheckBox cbSecenek = (CheckBox) viewSecenek;
                RelativeLayout.LayoutParams pa2 = new RelativeLayout.LayoutParams((ekranEnUzunluğu / 10) * 2, RelativeLayout.LayoutParams.WRAP_CONTENT);
                pa2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ll.setLayoutParams(pa2);
                if (deger.equals("0"))
                {
                    cbSecenek.setChecked(false);
                }
                else
                {
                    cbSecenek.setChecked(true);
                }
                ll.addView(viewSecenek);
                this.addView(ll);
                break;

            case MainActivity.SECENEK_BUTTON:
                secilenRenk = deger;
                viewSecenek = new ImageButton(cnt);
                ImageButton im = (ImageButton) viewSecenek;
                im.getBackground().setColorFilter(Color.parseColor(deger), PorterDuff.Mode.SRC_ATOP);
                RelativeLayout.LayoutParams pa3 = new RelativeLayout.LayoutParams((ekranEnUzunluğu / 10) * 2, (int) MainActivity.dpGetir(50));
                pa3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                im.setLayoutParams(pa3);
                this.addView(im);

                im.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        RenkDugmeleri alertLL = new RenkDugmeleri(cnt, deger, MainActivity.getListeRenkler());
                        alertLL.setCagiranYer(alertLL.CAGIRAN_YER_AYARLAR, aa);
                        CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(cnt, "Renk", "İptal", alertLL, MainActivity.ALERTDIALOG_CUSTOM_VIEW);
                        alertRenk = builder.create();
                        alertRenk.setCanceledOnTouchOutside(true);
                        alertRenk.show();
                    }
                });
                break;

            default:
                MainActivity.ekranaHataYazdir("51", "hatalı ayar türü, tür: " + secenekTuru);
        }
    }

    //ayarlar ekranında renk dugmesine tıklandığı zaman açılan alert dialogtan renk secimi yapılınca buraya geliyor
    public void renkSecimiYapildi(String secilenRenk)
    {
        ImageButton im = (ImageButton) viewSecenek;
        im.getBackground().setColorFilter(Color.parseColor(secilenRenk), PorterDuff.Mode.SRC_ATOP);
        this.secilenRenk = secilenRenk;
        alertRenk.dismiss();
    }

    public int getAyarID()
    {
        return ayarID;
    }

    public View getViewSecenek()
    {
        return viewSecenek;
    }

    public String getSecilenRenk()
    {
        return secilenRenk;
    }
}
