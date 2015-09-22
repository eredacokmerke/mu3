package com.example.ekcdr.uygulama3;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YedekRelativeLayout extends RelativeLayout
{
    private String isim;
    private CheckBox cb;

    public YedekRelativeLayout(Context context, String isim)
    {
        super(context);
        this.isim = isim;

        layoutOlustur(context);
    }

    public void layoutOlustur(final Context cnt)
    {
        int ID0 = 10000;
        final String yedekIsmi = this.getIsim();
        final RelativeLayout rl = this;

        cb = new CheckBox(cnt);
        cb.setId(ID0);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL);
        rl.addView(cb, lp1);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(0xFF00CED1);
                    rl.setBackground(gd);
                    MainActivity.PlaceholderFragment.listSeciliYedek.add((YedekRelativeLayout) rl);
                }
                else
                {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(0x00000000);
                    rl.setBackground(gd);
                    MainActivity.PlaceholderFragment.listSeciliYedek.remove(MainActivity.PlaceholderFragment.listSeciliYedek.indexOf(rl));
                }
            }
        });

        final TextView tv = new TextView(cnt);
        tv.setText(yedekIsmi);
        tv.setTextSize(20);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.LEFT_OF, cb.getId());
        lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp2.addRule(RelativeLayout.CENTER_VERTICAL);
        rl.addView(tv, lp2);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                File xmlDosyasi = new File(MainActivity.xmlYedekKlasorYolu + "/" + yedekIsmi + "." + Sabit.XML_DOSYA_UZANTISI);
                long olusturma = xmlDosyasi.lastModified();
                Date date = new Date(olusturma);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);

                final CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(cnt, cnt.getString(R.string.onay), cnt.getString(R.string.tamam), "\n" + cnt.getString(R.string.olusturma) + " : " + formattedDate + "\n" + cnt.getString(R.string.boyut) + " : " + xmlDosyasi.length(), Sabit.ALERTDIALOG_TEXTVIEW);
                final AlertDialog alert = builder.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alert.dismiss();
                    }
                });
            }
        });
    }

    public String getIsim()
    {
        return isim;
    }

    public CheckBox getCb()
    {
        return cb;
    }
}
