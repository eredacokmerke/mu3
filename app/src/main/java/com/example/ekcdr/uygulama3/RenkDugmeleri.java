package com.example.ekcdr.uygulama3;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Random;

public class RenkDugmeleri extends LinearLayout
{
    public final int CAGIRAN_YER_FRAGMENT = 0;//sınıf fragment ten cagriliyor
    public final int CAGIRAN_YER_AYARLAR = 1;//sınıf ayarlar dan çagriliyor
    public MainActivity.PlaceholderFragment frag;
    public AyarlarRelativeLayout arl;
    public int cagiranYer;//renkDugmeleri nereden cagrildi

    //public RenkDugmeleri(Context context, String seciliRenk, List<String> listeRenkler, MainActivity.PlaceholderFragment frag)
    public RenkDugmeleri(Context context, String seciliRenk, List<String> listeRenkler)
    {
        super(context);

        setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams pa1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        setLayoutParams(pa1);
        setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
        setPadding((int) dpGetir(20), 0, (int) dpGetir(20), 0);
        LinearLayout.LayoutParams pa3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

        LinearLayout alertLLSatir = null;
        //boolean rastgeleSecim=true;//daha once tanımlı renkler mi yoksa rastgele renk mi seçilmiş. rastgele renk seçilmişse fazladan bir dugme konacak
        for (int i = 0; i < listeRenkler.size(); i++)
        {
            if (i % 5 == 0)
            {
                alertLLSatir = new LinearLayout(context);
                alertLLSatir.setLayoutParams(pa3);
                alertLLSatir.setWeightSum(1f);
                alertLLSatir.setGravity(Gravity.CENTER);

                if (seciliRenk.equals(listeRenkler.get(i)))
                {
                    alertLLSatir.addView(new RenkDugmesi(context, listeRenkler.get(i), this, true));
                    //rastgeleSecim=false;
                }
                else
                {
                    alertLLSatir.addView(new RenkDugmesi(context, listeRenkler.get(i), this, false));
                }
            }
            else
            {
                if (seciliRenk.equals(listeRenkler.get(i)))
                {
                    alertLLSatir.addView(new RenkDugmesi(context, listeRenkler.get(i), this, true));
                    //rastgeleSecim=false;
                }
                else
                {
                    alertLLSatir.addView(new RenkDugmesi(context, listeRenkler.get(i), this, false));
                }
                if (i % 5 == 4)
                {
                    addView(alertLLSatir);
                }
            }
        }

        /*
        LinearLayout alertLLSatir3 = new LinearLayout(context);
        alertLLSatir3.setLayoutParams(pa3);
        alertLLSatir3.setWeightSum(1f);
        alertLLSatir3.setGravity(Gravity.CENTER);

        final RenkDugmesi btnRastgeleRenk = new RenkDugmesi(context, rastgeleRenkGetir(), this);
        final Button btnRastgele = new Button(context);
        btnRastgele.setText("rastgele");
        btnRastgele.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int yeniRenk = rastgeleRenkGetir();
                String hexColor = String.format("#%06X", (0xFFFFFF & yeniRenk));
                btnRastgeleRenk.setRenk(hexColor);
                btnRastgeleRenk.getBackground().setColorFilter(yeniRenk, PorterDuff.Mode.SRC_ATOP);
            }
        });

        if(rastgeleSecim)
        {
            RenkDugmesi btnRastgeleSecim = new RenkDugmesi(context, seciliRenk, this, true);
            alertLLSatir3.addView(btnRastgeleSecim);
        }


        alertLLSatir3.addView(btnRastgele);
        alertLLSatir3.addView(btnRastgeleRenk);
        addView(alertLLSatir3);
        */
    }

    public void secimYapildi(String secilenRenk)
    {
        switch (cagiranYer)
        {
            case CAGIRAN_YER_AYARLAR://sınıf ayarlar ekranından cagrildi
                arl.renkSecimiYapildi(secilenRenk);
                break;

            case CAGIRAN_YER_FRAGMENT://sınıf fragment ten cagrildi
                frag.renkSecimiYapildi(secilenRenk);
                break;

            default:
                MainActivity.ekranaHataYazdir("50", "cağıran sınıf hatalı, id : " + cagiranYer);
        }
    }

    public float dpGetir(int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    public int rastgeleRenkGetir()
    {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        return color;
    }

    public void setCagiranYer(int yer, AyarlarRelativeLayout arl)
    {
        cagiranYer = yer;
        this.arl = arl;
    }

    public void setCagiranYer(int yer, MainActivity.PlaceholderFragment frag)
    {
        cagiranYer = yer;
        this.frag = frag;
    }
}
