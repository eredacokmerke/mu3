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
    int btnBoyut;//tuslarin boyutlari
    int btnMargin;//tuslarin paddingleri
    RelativeLayout anaLayout;//mainactivity'den gelen layout
    ImageButton btnKategoriEkle;
    ImageButton btnKayitEkle;

    //public FloatingActionButton(final Activity act, final RelativeLayout anaLayout, final MainActivity.PlaceholderFragment frag)
    public FloatingActionButton(final Activity act)
    {
        this.act = act;
        //this.frag = frag;
        //this.anaLayout = anaLayout;
        setAcikMi(false);

        btnBoyut = (int) dpGetir(60);
        btnMargin = (int) dpGetir(5);

        /*
        LayerDrawable layers = (LayerDrawable)  act.getResources().getDrawable(R.drawable.golge);
        GradientDrawable shape = (GradientDrawable) (layers.findDrawableByLayerId(R.id.asd));
        shape.setColor(act.getResources().getColor(android.R.color.background_dark));
        */

        /*
        final ImageButton btnAna = new ImageButton(act);
        btnAna.setId(Button.generateViewId());
        btnAna.setImageResource(R.drawable.yeni_ekle);
        btnAna.setMinimumWidth(btnBoyut);
        btnAna.setMinimumHeight(btnBoyut);
        btnAna.setBackgroundResource(R.drawable.golge);
        RelativeLayout.LayoutParams lpBtnAna = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpBtnAna.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpBtnAna.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpBtnAna.setMargins(btnMargin, btnMargin, btnMargin, btnMargin);
        btnAna.setLayoutParams(lpBtnAna);
        anaLayout.addView(btnAna);

        btnKategoriEkle = new ImageButton(act);
        btnKategoriEkle.setId(Button.generateViewId());
        btnKategoriEkle.setImageResource(R.drawable.kategori_ekle);
        btnKategoriEkle.setMinimumWidth(btnBoyut);
        btnKategoriEkle.setMinimumHeight(btnBoyut);
        btnKategoriEkle.setBackgroundResource(R.drawable.golge);
        RelativeLayout.LayoutParams lpbtnKategoriEkle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpbtnKategoriEkle.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //lpbtnKategoriEkle.addRule(RelativeLayout.ABOVE, btnAna.getId());
        lpbtnKategoriEkle.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpbtnKategoriEkle.setMargins(btnMargin, btnMargin, btnMargin + (int) dpGetir(16), btnMargin + (int) dpGetir(16));
        btnKategoriEkle.setLayoutParams(lpbtnKategoriEkle);

        btnKayitEkle = new ImageButton(act);
        btnKayitEkle.setId(Button.generateViewId());
        btnKayitEkle.setImageResource(R.drawable.kayit_ekle);
        btnKayitEkle.setMinimumWidth(btnBoyut);
        btnKayitEkle.setMinimumHeight(btnBoyut);
        btnKayitEkle.setBackgroundResource(R.drawable.golge);
        RelativeLayout.LayoutParams lpbtnKayitEkle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpbtnKayitEkle.addRule(RelativeLayout.ABOVE, btnKategoriEkle.getId());
        lpbtnKayitEkle.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpbtnKayitEkle.setMargins(btnMargin, btnMargin, btnMargin+(int) dpGetir(16), btnMargin);
        btnKayitEkle.setLayoutParams(lpbtnKayitEkle);

        btnAna.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!acikMi)//secenek tuslari kapali, acilacak
                {
                    anaLayout.setPadding(0, 0, 0, 0);

                    frag.ekraniBlurYap();


                    anaLayout.addView(btnKategoriEkle);
                    btnKategoriEkle.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            frag.yeniKategoriEkranininAc();
                        }
                    });

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
        */
    }

    public void tuslariOlustur()
    {
        final ImageButton btnAna = new ImageButton(act);
        btnAna.setId(Button.generateViewId());
        btnAna.setImageResource(R.drawable.yeni_ekle);
        btnAna.setMinimumWidth(btnBoyut);
        btnAna.setMinimumHeight(btnBoyut);
        btnAna.setBackgroundResource(R.drawable.golge);
        RelativeLayout.LayoutParams lpBtnAna = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpBtnAna.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpBtnAna.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpBtnAna.setMargins(btnMargin, btnMargin, btnMargin, btnMargin);
        btnAna.setLayoutParams(lpBtnAna);
        anaLayout.addView(btnAna);

        btnKategoriEkle = new ImageButton(act);
        btnKategoriEkle.setId(Button.generateViewId());
        btnKategoriEkle.setImageResource(R.drawable.kategori_ekle);
        btnKategoriEkle.setMinimumWidth(btnBoyut);
        btnKategoriEkle.setMinimumHeight(btnBoyut);
        btnKategoriEkle.setBackgroundResource(R.drawable.golge);
        RelativeLayout.LayoutParams lpbtnKategoriEkle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpbtnKategoriEkle.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //lpbtnKategoriEkle.addRule(RelativeLayout.ABOVE, btnAna.getId());
        lpbtnKategoriEkle.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpbtnKategoriEkle.setMargins(btnMargin, btnMargin, btnMargin + (int) dpGetir(16), btnMargin + (int) dpGetir(16));
        btnKategoriEkle.setLayoutParams(lpbtnKategoriEkle);

        btnKayitEkle = new ImageButton(act);
        btnKayitEkle.setId(Button.generateViewId());
        btnKayitEkle.setImageResource(R.drawable.kayit_ekle);
        btnKayitEkle.setMinimumWidth(btnBoyut);
        btnKayitEkle.setMinimumHeight(btnBoyut);
        btnKayitEkle.setBackgroundResource(R.drawable.golge);
        RelativeLayout.LayoutParams lpbtnKayitEkle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpbtnKayitEkle.addRule(RelativeLayout.ABOVE, btnKategoriEkle.getId());
        lpbtnKayitEkle.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpbtnKayitEkle.setMargins(btnMargin, btnMargin, btnMargin + (int) dpGetir(16), btnMargin);
        btnKayitEkle.setLayoutParams(lpbtnKayitEkle);

        btnAna.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!isAcikMi())//secenek tuslari kapali, acilacak
                {
                    anaLayout.setPadding(0, 0, 0, 0);
                    frag.ekraniBlurYap();

                    anaLayout.addView(btnKategoriEkle);
                    btnKategoriEkle.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            frag.yeniKategoriEkranininAc();
                            frag.bluruIptalEt();
                        }
                    });

                    anaLayout.addView(btnKayitEkle);
                    btnKayitEkle.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            frag.yeniKayitEkraniniAc();
                            frag.bluruIptalEt();
                        }
                    });

                    setAcikMi(true);
                }
                else//secenek tuslari acik, kapanacak
                {
                    anaLayout.removeView(btnKategoriEkle);
                    anaLayout.removeView(btnKayitEkle);

                    setAcikMi(false);
                }
            }
        });
    }

    public void tuslariGizle()
    {
        anaLayout.removeView(btnKategoriEkle);
        anaLayout.removeView(btnKayitEkle);

        setAcikMi(false);
    }

    public boolean isAcikMi()
    {
        return acikMi;
    }

    public void setAcikMi(boolean acikMi)
    {
        this.acikMi = acikMi;
    }

    //px birimini dp ye cevirir
    public float dpGetir(int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, act.getResources().getDisplayMetrics());
    }

    public void setFrag(MainActivity.PlaceholderFragment frag)
    {
        this.frag = frag;
    }

    public void setAnaLayout(RelativeLayout anaLayout)
    {
        this.anaLayout = anaLayout;
    }
}
