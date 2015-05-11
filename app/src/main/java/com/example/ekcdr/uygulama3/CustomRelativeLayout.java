package com.example.ekcdr.uygulama3;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CustomRelativeLayout extends RelativeLayout
{
    private boolean crlSeciliMi = false;
    private TextView tvTik;
    private TextView tvBaslik;
    private ImageView tvDuzenle;
    private int crlTur;
    final static int ID0 = 10000;
    final static int ID1 = 10001;
    final static int ID2 = 10002;

    public CustomRelativeLayout(Context context, String baslik, int elemanTur, final int crlID, final MainActivity.PlaceholderFragment frag)
    {
        super(context);
        setCrlSeciliMi(false);

        switch (elemanTur)
        {
            case MainActivity.ELEMAN_TUR_KATEGORI:
            {
                LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                pa.setMargins(0, (int) dpGetir(3), 0, (int) dpGetir(3));
                this.setLayoutParams(pa);

                arkaplanKategori();

                crlTur = MainActivity.ELEMAN_TUR_KATEGORI;

                tvTik = new TextView(context);
                tvTik.setTextSize(30);
                tvTik.setId(ID0);
                tvTik.setTextColor(Color.WHITE);
                RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp3.addRule(RelativeLayout.CENTER_VERTICAL);
                this.addView(tvTik, lp3);

                tvDuzenle = new ImageView(context);
                tvDuzenle.setImageResource(R.drawable.duzenle);
                tvDuzenle.setId(ID1);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp1.addRule(RelativeLayout.CENTER_VERTICAL);
                this.addView(tvDuzenle, lp1);
                tvDuzenle.setVisibility(INVISIBLE);
                tvDuzenle.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //alertdialog un içindeki ana LinearLayout
                        LinearLayout alertLL = new LinearLayout(getContext());
                        LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                        alertLL.setLayoutParams(pa);
                        alertLL.setGravity(Gravity.CENTER);//içerik linearlayout un ortasına yerleşsin
                        alertLL.setWeightSum(1f);

                        //yazının yazılacagı EditText
                        final EditText alertET = new EditText(getContext());
                        LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                        alertET.setLayoutParams(pa2);
                        alertET.setGravity(Gravity.CENTER);//yazı Edittext in ortasında yazılsın
                        alertLL.addView(alertET);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Kategori Adı");
                        builder.setView(alertLL);

                        builder.setPositiveButton("Tamam", null);//dugmeye tıklama olayını aşağıda yakaladığım için buraya null değeri giriyorum
                        builder.setNegativeButton("İptal", null);
                        final AlertDialog alert = builder.create();
                        alert.show();

                        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                alert.dismiss();

                                String yeniBaslik = alertET.getText().toString();

                                tvBaslik.setText(yeniBaslik);

                                frag.kategoriBaslikGuncelle(yeniBaslik, crlID);
                            }
                        });
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                alert.dismiss();
                            }
                        });
                    }
                });

                tvBaslik = new TextView(context);
                tvBaslik.setTextSize(30);
                tvBaslik.setText(baslik);
                tvBaslik.setTextColor(Color.WHITE);
                tvBaslik.setPadding(5, 0, 5, 0);
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                lp2.addRule(RelativeLayout.LEFT_OF, tvDuzenle.getId());
                lp2.addRule(RelativeLayout.RIGHT_OF, tvTik.getId());
                this.addView(tvBaslik, lp2);

                break;
            }
            case MainActivity.ELEMAN_TUR_KAYIT:
            {
                LinearLayout.LayoutParams pa2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                pa2.setMargins(0, 0, 0, 0);
                this.setLayoutParams(pa2);

                arkaplanKayit();

                crlTur = MainActivity.ELEMAN_TUR_KAYIT;

                tvTik = new TextView(context);
                tvTik.setTextSize(15);
                tvTik.setId(ID2);
                tvTik.setTextColor(Color.WHITE);
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp4.addRule(RelativeLayout.CENTER_VERTICAL);
                this.addView(tvTik, lp4);

                tvBaslik = new TextView(context);
                tvBaslik.setTextSize(15);
                tvBaslik.setText(baslik);
                tvBaslik.setTextColor(Color.WHITE);
                tvBaslik.setPadding(5, 0, 5, 0);
                RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                lp5.addRule(RelativeLayout.RIGHT_OF, tvTik.getId());
                this.addView(tvBaslik, lp5);

                break;
            }
        }
    }

    public void arkaplanSecili()
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFFFF2222);
        gd.setStroke((int) dpGetir(2), 0xFF880000);
        gd.setCornerRadius(dpGetir(7));
        setBackground(gd);
        setPadding((int) dpGetir(3), (int) dpGetir(7), (int) dpGetir(3), (int) dpGetir(7));
    }

    public void arkaplanKategori()
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF00CED1);
        gd.setStroke((int) dpGetir(2), 0xFF009095);
        gd.setCornerRadius(dpGetir(7));
        setBackground(gd);
        setPadding((int) dpGetir(3), (int) dpGetir(7), (int) dpGetir(3), (int) dpGetir(7));
    }

    public void arkaplanKayit()
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF009ED1);
        gd.setStroke((int) dpGetir(2), 0xFF004095);
        gd.setCornerRadius(dpGetir(7));
        setBackground(gd);
        setPadding((int) dpGetir(3), (int) dpGetir(7), (int) dpGetir(3), (int) dpGetir(7));
    }

    //px birimini dp ye cevirir
    public float dpGetir(int px)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    public TextView getTvTik()
    {
        return tvTik;
    }

    public boolean isCrlSeciliMi()
    {
        return crlSeciliMi;
    }

    public void setCrlSeciliMi(boolean crlSeciliMi)
    {
        this.crlSeciliMi = crlSeciliMi;
    }

    public ImageView getTvDuzenle()
    {
        return tvDuzenle;
    }

    public int getCrlTur()
    {
        return crlTur;
    }
}
