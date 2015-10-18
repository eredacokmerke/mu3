package com.example.ekcdr.uygulama3;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class FloatingActionButton
{
    public MainActivity.PlaceholderFragment frag;//fragment fonksiyonlarina erisebilmek icin
    Activity act;//ana activity nesnesi
    boolean acikMi;//secenek tuslari acik mi
    int boyut;//tuslarin boyutlari
    int padding;//tuslarin paddingleri

    public FloatingActionButton(final Activity act, final RelativeLayout anaLayout, final MainActivity.PlaceholderFragment frag)
    {
        this.act = act;
        this.frag = frag;
        acikMi = false;

        boyut = (int) dpGetir(60);
        padding = (int) dpGetir(5);

        /*
        LayerDrawable layers = (LayerDrawable)  act.getResources().getDrawable(R.drawable.golge);
        GradientDrawable shape = (GradientDrawable) (layers.findDrawableByLayerId(R.id.asd));
        shape.setColor(act.getResources().getColor(android.R.color.background_dark));
        */

        final ImageButton btnAna = new ImageButton(act);
        btnAna.setId(Button.generateViewId());
        btnAna.setImageResource(R.drawable.yeni_ekle);
        btnAna.setMinimumWidth(boyut);
        btnAna.setMinimumHeight(boyut);
        btnAna.setBackgroundResource(R.drawable.golge);
        RelativeLayout.LayoutParams lpBtnAna = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpBtnAna.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpBtnAna.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpBtnAna.setMargins(padding, padding, padding, padding);
        btnAna.setLayoutParams(lpBtnAna);
        anaLayout.addView(btnAna);

        final ImageButton btnKategoriEkle = new ImageButton(act);
        final ImageButton btnKayitEkle = new ImageButton(act);

        btnAna.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!acikMi)//secenek tuslari kapali, acilacak
                {
                    btnKategoriEkle.setId(Button.generateViewId());
                    btnKategoriEkle.setImageResource(R.drawable.kategori_ekle);
                    btnKategoriEkle.setMinimumWidth(boyut);
                    btnKategoriEkle.setMinimumHeight(boyut);
                    btnKategoriEkle.setBackgroundResource(R.drawable.golge);
                    RelativeLayout.LayoutParams lpbtnKategoriEkle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpbtnKategoriEkle.addRule(RelativeLayout.ABOVE, btnAna.getId());
                    lpbtnKategoriEkle.addRule(RelativeLayout.ALIGN_PARENT_END);
                    lpbtnKategoriEkle.setMargins(padding, padding, padding, padding);
                    btnKategoriEkle.setLayoutParams(lpbtnKategoriEkle);
                    anaLayout.addView(btnKategoriEkle);

                    btnKategoriEkle.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            frag.yeniKategoriEkranininAc();
                        }
                    });

                    btnKayitEkle.setId(Button.generateViewId());
                    btnKayitEkle.setImageResource(R.drawable.kayit_ekle);
                    btnKayitEkle.setMinimumWidth(boyut);
                    btnKayitEkle.setMinimumHeight(boyut);
                    btnKayitEkle.setBackgroundResource(R.drawable.golge);
                    RelativeLayout.LayoutParams lpbtnKayitEkle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lpbtnKayitEkle.addRule(RelativeLayout.ABOVE, btnKategoriEkle.getId());
                    lpbtnKayitEkle.addRule(RelativeLayout.ALIGN_PARENT_END);
                    lpbtnKayitEkle.setMargins(padding, padding, padding, padding);
                    btnKayitEkle.setLayoutParams(lpbtnKayitEkle);
                    anaLayout.addView(btnKayitEkle);

                    btnKayitEkle.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            frag.yeniKayitEkraniniAc();
                        }
                    });

                    acikMi = true;
                }
                else//secenek tuslari acik, kapanacak
                {
                    anaLayout.removeView(btnKategoriEkle);
                    anaLayout.removeView(btnKayitEkle);

                    acikMi = false;
                }
            }
        });
    }

    //px birimini dp ye cevirir
    public float dpGetir(int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, act.getResources().getDisplayMetrics());
    }
}
