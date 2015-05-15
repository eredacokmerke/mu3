package com.example.ekcdr.uygulama3;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class customAlertDialogBuilder extends AlertDialog.Builder
{
    private String baslik;
    private String yazi;
    private String olumluDugmeYazi;
    private String olumsuzDugmeYazi;
    private EditText alertET;

    public customAlertDialogBuilder(Context context, String baslik, String olumsuzDugmeYazi, String olumluDugmeYazi, String yazi, int tur)
    {
        super(context);
        this.baslik = baslik;
        this.olumsuzDugmeYazi = olumsuzDugmeYazi;
        this.olumluDugmeYazi = olumluDugmeYazi;
        this.yazi = yazi;

        alertDialogOlustur(tur);
    }

    public void alertDialogOlustur(int tur)
    {
        //alertdialog un içindeki ana LinearLayout
        LinearLayout alertLL = new LinearLayout(getContext());
        LinearLayout.LayoutParams pa1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        alertLL.setLayoutParams(pa1);
        alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
        alertLL.setWeightSum(1f);

        LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
        //yazının yazılacagı kısım
        switch (tur)
        {
            case MainActivity.ALERTDIALOG_EDITTEXT:

                alertET = new EditText(getContext());
                //LinearLayout.LayoutParams pa1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                alertET.setLayoutParams(pa2);
                alertET.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
                alertET.setText(yazi);
                alertLL.addView(alertET);
                break;

            case MainActivity.ALERTDIALOG_TEXTVIEW:
                TextView alertTV = new TextView(getContext());
                //LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                alertTV.setLayoutParams(pa2);
                alertTV.setGravity(Gravity.CENTER);//yazı TextView in ortasında yazılsın
                alertTV.setText(yazi);
                alertLL.addView(alertTV);
                break;

            default:
                //ekranaHataYazdir("1", "alert dialog hatasi");
                break;
        }


        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.setTitle(this.getBaslik());
        this.setView(alertLL);

        this.setPositiveButton(this.getOlumluDugmeYazi(), null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
        this.setNegativeButton(this.getOlumsuzDugmeYazi(), null);
    }

    public EditText getAlertET()
    {
        return alertET;
    }

    public String getBaslik()
    {
        return baslik;
    }

    public String getOlumluDugmeYazi()
    {
        return olumluDugmeYazi;
    }

    public String getOlumsuzDugmeYazi()
    {
        return olumsuzDugmeYazi;
    }
}