package com.example.ekcdr.uygulama3;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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

import java.util.List;

public class CustomRelativeLayout extends RelativeLayout
{
    private boolean crlSeciliMi = false;
    private TextView tvTik;
    private TextView tvBaslik;
    private ImageView tvDuzenle;
    private int crlTur;
    private String durum;
    private int satir;
    private List<int[]> matris;
    private int satirBasinaKayitSayisi;
    final static int ID0 = 10000;
    final static int ID1 = 10001;
    final static int ID2 = 10002;
    final static int YAZI_BUYUKLUGU_KAYIT = 15;
    final static int YAZI_BUYUKLUGU_KATEGORI = 30;
    final static int PADDING_DIKEY = 7;
    final static int PADDING_YATAY = 3;
    final static int PADDING_YAZI = 10;

    public CustomRelativeLayout(Context context, String baslik, int elemanTur, final int crlID, String durum, final MainActivity.PlaceholderFragment frag, List<int[]> matris)
    {
        super(context);
        setCrlSeciliMi(false);
        setDurum(durum);
        this.setId(crlID);
        this.matris = matris;
        satirBasinaKayitSayisi = Integer.valueOf(MainActivity.DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI);

        switch (elemanTur)
        {
            case MainActivity.ELEMAN_TUR_KATEGORI:
            {
                RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(MainActivity.elemanEnUzunluğu, RelativeLayout.LayoutParams.WRAP_CONTENT);
                pa.setMargins(0, (int) dpGetir(3), 0, (int) dpGetir(3));

                satirSayisiniHesapla(baslik, YAZI_BUYUKLUGU_KATEGORI);
                kayitMatriseEkle(pa);
                this.setLayoutParams(pa);

                arkaplanKategori();
                crlTur = MainActivity.ELEMAN_TUR_KATEGORI;

                tvTik = new TextView(context);
                tvTik.setTextSize(YAZI_BUYUKLUGU_KATEGORI);
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
                tvBaslik.setTextSize(YAZI_BUYUKLUGU_KATEGORI);
                tvBaslik.setText(baslik);
                tvBaslik.setTextColor(Color.WHITE);
                tvBaslik.setPadding(PADDING_YAZI, 0, PADDING_YAZI, 0);
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                //lp2.addRule(RelativeLayout.LEFT_OF, tvDuzenle.getId());
                lp2.addRule(RelativeLayout.RIGHT_OF, tvTik.getId());
                this.addView(tvBaslik, lp2);

                break;
            }
            case MainActivity.ELEMAN_TUR_KAYIT:
            {
                RelativeLayout.LayoutParams pa2 = new RelativeLayout.LayoutParams(MainActivity.elemanEnUzunluğu, RelativeLayout.LayoutParams.WRAP_CONTENT);
                pa2.setMargins(0, 0, 0, 0);

                satirSayisiniHesapla(baslik, YAZI_BUYUKLUGU_KAYIT);
                kayitMatriseEkle(pa2);
                this.setLayoutParams(pa2);

                arkaplanKayit();
                crlTur = MainActivity.ELEMAN_TUR_KAYIT;

                tvTik = new TextView(context);
                tvTik.setTextSize(YAZI_BUYUKLUGU_KAYIT);
                tvTik.setId(ID2);
                tvTik.setTextColor(Color.WHITE);
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp4.addRule(RelativeLayout.CENTER_VERTICAL);
                this.addView(tvTik, lp4);

                tvBaslik = new TextView(context);
                tvBaslik.setTextSize(YAZI_BUYUKLUGU_KAYIT);
                tvBaslik.setText(baslik);
                tvBaslik.setTextColor(Color.WHITE);
                tvBaslik.setPadding(PADDING_YAZI, 0, PADDING_YAZI, 0);
                RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                lp5.addRule(RelativeLayout.RIGHT_OF, tvTik.getId());
                this.addView(tvBaslik, lp5);

                break;
            }
        }
    }

    public void kayitMatriseEkle(RelativeLayout.LayoutParams pa)
    {
        if (!matris.isEmpty())
        {
            int matrisBoyu = matris.size();

            boolean bosIndeksVarMi = false;
            boolean idEklendiMi = false;
            for (int j = 0; j < matrisBoyu && !idEklendiMi; j++)
            {
                int[] a = matris.get(j);

                //Log.d("uyg3", "a : "+Arrays.toString(matris.get(j)));
                for (int i = 0; i < a.length; i++)
                {
                    //Log.d("uyg3", "i : "+i+" -- "+a[i]);
                    if (a[i] == -1)//bos indeks bulundu
                    {
                        //Log.d("uyg3", "-1 bulundu");
                        bosIndeksVarMi = true;

                        int bosSatirSayisi = matris.size() - j;//bulunduğum yerin altında daha kaç satır var
                        if (getSatir() > bosSatirSayisi)//alttaki bosluk yetersiz. yeni satir eklenecek
                        {
                            int eklenecekSatir = getSatir() - bosSatirSayisi;//eklenecek satir sayisi

                            for (int l = 0; l < eklenecekSatir; l++)
                            {
                                //Log.d("uyg3", "ekle 4");
                                int[] c = new int[satirBasinaKayitSayisi];
                                for (int k = 0; k < satirBasinaKayitSayisi; k++)
                                {
                                    c[k] = -1;
                                }

                                matris.add(c);
                            }

                            if (j == 0)//id en ustte eklenecek
                            {
                                int usttekiID = matris.get(0)[i - 1];
                                pa.addRule(RelativeLayout.RIGHT_OF, usttekiID);
                                pa.addRule(RelativeLayout.ALIGN_TOP, usttekiID);
                            }
                            else//id baska bir id nin altına yerlesecek
                            {
                                int usttekiID = matris.get(j - 1)[i];
                                pa.addRule(RelativeLayout.BELOW, usttekiID);
                                pa.addRule(RelativeLayout.ALIGN_LEFT, usttekiID);
                                //Log.d("uyg3", "ekle 1 -- ustteki : " + usttekiID);
                            }

                            for (int z = 0; z < getSatir(); z++)
                            {
                                //Log.d("uyg3", "ekle 1");
                                int[] b = matris.get(j + z);
                                b[i] = getId();

                                idEklendiMi = true;
                            }
                        }
                        else//alttaki bosluk yeterli
                        {
                            //Log.d("uyg3", "ekle 2 -- j : " + j + " i : " + i);
                            if (j == 0)//id en ustte eklenecek
                            {
                                int usttekiID = matris.get(0)[i - 1];
                                pa.addRule(RelativeLayout.RIGHT_OF, usttekiID);
                                pa.addRule(RelativeLayout.ALIGN_TOP, usttekiID);
                                //Log.d("uyg3", "ekle 2 -- usttekiID 1 : " + usttekiID);
                            }
                            else//id baska bir id nin altına yerlesecek
                            {
                                int usttekiID = matris.get(j - 1)[i];
                                pa.addRule(RelativeLayout.BELOW, usttekiID);
                                pa.addRule(RelativeLayout.ALIGN_LEFT, usttekiID);
                                //Log.d("uyg3", "ekle 2 -- usttekiID 2 : " + usttekiID);
                            }

                            for (int z = 0; z < getSatir(); z++)
                            {
                                int[] b = matris.get(j + z);
                                b[i] = getId();

                                idEklendiMi = true;
                            }
                        }
                        break;
                    }
                }
            }
            if (!bosIndeksVarMi)//bos indeks yok yeni satir eklenecek
            {
                int usttekiID = matris.get(matrisBoyu - 1)[0];
                pa.addRule(RelativeLayout.BELOW, usttekiID);

                //Log.d("uyg3", "ekle 3 -- ustteki : " + usttekiID);
                for (int i = 0; i < this.getSatir(); i++)
                {
                    int[] c = new int[satirBasinaKayitSayisi];
                    c[0] = getId();
                    for (int w = 1; w < satirBasinaKayitSayisi; w++)
                    {
                        c[w] = -1;
                    }

                    matris.add(c);
                }
            }
        }
        else//matris boş, ilk eleman ekleniyor
        {
            for (int i = 0; i < this.getSatir(); i++)
            {
                int[] a = new int[satirBasinaKayitSayisi];
                a[0] = getId();

                for (int j = 1; j < satirBasinaKayitSayisi; j++)
                {
                    a[j] = -1;
                }

                matris.add(a);
            }
        }
    }

    public void satirSayisiniHesapla(String baslik, int yaziBuyuklugu)
    {
        Paint textPaint = new Paint();
        textPaint.setTextSize(yaziBuyuklugu);
        float uzunluk = dpGetir((int) textPaint.measureText(baslik));
        int satir = (int) Math.ceil(uzunluk / (MainActivity.elemanEnUzunluğu - dpGetir(PADDING_YAZI * 2)));
        setSatir(satir + 1);//ust ve alttaki baslıklar için +1 ekliyorum
    }

    public void arkaplanSecili()
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFFFF2222);
        gd.setStroke((int) dpGetir(2), 0xFF880000);
        gd.setCornerRadius(dpGetir(7));
        setBackground(gd);
        setPadding((int) dpGetir(PADDING_YATAY), (int) dpGetir(PADDING_DIKEY), (int) dpGetir(PADDING_YATAY), (int) dpGetir(PADDING_DIKEY));
    }

    public void arkaplanKategori()
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF00CED1);
        gd.setStroke((int) dpGetir(2), 0xFF009095);
        gd.setCornerRadius(dpGetir(7));
        setBackground(gd);
        setPadding((int) dpGetir(PADDING_YATAY), (int) dpGetir(PADDING_DIKEY), (int) dpGetir(PADDING_YATAY), (int) dpGetir(PADDING_DIKEY));
    }

    public void arkaplanKayit()
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF009ED1);
        gd.setStroke((int) dpGetir(2), 0xFF004095);
        gd.setCornerRadius(dpGetir(7));
        setBackground(gd);
        setPadding((int) dpGetir(PADDING_YATAY), (int) dpGetir(PADDING_DIKEY), (int) dpGetir(PADDING_YATAY), (int) dpGetir(PADDING_DIKEY));
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

    public String getDurum()
    {
        return durum;
    }

    public void setDurum(String durum)
    {
        this.durum = durum;
    }

    public int getSatir()
    {
        return satir;
    }

    public void setSatir(int satir)
    {
        this.satir = satir;
    }

    public List<int[]> getMatris()
    {
        return matris;
    }
}
