package com.example.ekcdr.uygulama3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class RenkDugmesi extends ImageButton
{
    String renk;

    public RenkDugmesi(Context context, final String renk, final RenkDugmeleri rd, boolean seciliMi)
    {
        super(context);
        this.renk = renk;

        //LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
        LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams((int) dpGetir(70), (int) dpGetir(50), 0.2f);

        setLayoutParams(pa);
        getBackground().setColorFilter(Color.parseColor(renk), PorterDuff.Mode.SRC_ATOP);
        if (seciliMi)
        {
            setImageResource(R.drawable.tamam);
        }
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                rd.secimYapildi(renk);
            }
        });
    }

    public RenkDugmesi(Context context, int renkID, final RenkDugmeleri rd)
    {
        super(context);
        String hexColor = String.format("#%06X", (0xFFFFFF & renkID));
        this.renk = hexColor;

        //LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
        LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams((int) dpGetir(70), (int) dpGetir(50), 0.2f);
        setLayoutParams(pa);
        getBackground().setColorFilter(renkID, PorterDuff.Mode.SRC_ATOP);

        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                rd.secimYapildi(renk);
            }
        });
    }

    public String getRenk()
    {
        return renk;
    }

    public void setRenk(String renk)
    {
        this.renk = renk;
    }

    //px birimini dp ye cevirir
    public float dpGetir(int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }
}
