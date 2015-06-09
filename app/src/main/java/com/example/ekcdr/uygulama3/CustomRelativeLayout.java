package com.example.ekcdr.uygulama3;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
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
    //private List<int[]> matris;
    private int satirBasinaKayitSayisi;
    final static int ID0 = 10000;
    final static int ID1 = 10001;
    final static int ID2 = 10002;
    final static int YAZI_BUYUKLUGU_KAYIT = 15;
    final static int YAZI_BUYUKLUGU_KATEGORI = 30;
    final static int PADDING_DIKEY = 7;
    final static int PADDING_YATAY = 3;
    final static int PADDING_YAZI = 10;

    public CustomRelativeLayout(Context context, String baslik, int elemanTur, final int crlID, String durum, final MainActivity.PlaceholderFragment frag, Yerlesim ylsm)
    {
        super(context);
        setCrlSeciliMi(false);
        setDurum(durum);
        this.setId(crlID);
        satirBasinaKayitSayisi = Integer.valueOf(MainActivity.DEGER_AYAR_SATIR_BASINA_KAYIT_SAYISI);

        switch (elemanTur)
        {
            case MainActivity.ELEMAN_TUR_KATEGORI:
            {
                RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(MainActivity.elemanEnUzunluğu, RelativeLayout.LayoutParams.WRAP_CONTENT);
                pa.setMargins(0, 0, (int) dpGetir(3), (int) dpGetir(3));

                this.setLayoutParams(pa);
                viewBoyunuGetir(this, ylsm, crlID, pa);

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
                pa2.setMargins(0, 0, (int) dpGetir(3), (int) dpGetir(3));

                this.setLayoutParams(pa2);
                viewBoyunuGetir(this, ylsm, crlID, pa2);

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

    //verilen sutundaki ilk -1 degerine sahip satir sayisini donduruyor
    public int matristeBosSatiriBul(Yerlesim ylsm, int sutunNo)
    {
        List<int[]> matris = ylsm.getYerlesimMatris();
        for (int i = 0; i < ylsm.getYerlesimMatrisSatirSayisi(); i++)
        {
            if (matris.get(i)[sutunNo] == -1)
            {
                return i;
            }
        }

        return -1;
    }

    //matrise elemnaları -1 olan yeni bir satir ekler
    public void matriseSatirEkle(Yerlesim ylsm)
    {
        List<int[]> matris = ylsm.getYerlesimMatris();
        int[] a = new int[satirBasinaKayitSayisi];

        for (int j = 0; j < satirBasinaKayitSayisi; j++)
        {
            a[j] = -1;
        }

        matris.add(a);
        int matrisSatir = ylsm.getYerlesimMatrisSatirSayisi();
        matrisSatir++;
        ylsm.setYerlesimMatrisSatirSayisi(matrisSatir);
    }

    public void viewBoyunuGetir(final CustomRelativeLayout crl, final Yerlesim ylsm, final int elemanID, final RelativeLayout.LayoutParams pa)
    {
        final int[] viewYukseklikleri = ylsm.getYerlesimSutunYukseklikleri();
        final List<int[]> matris = ylsm.getYerlesimMatris();

        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        if (viewTreeObserver.isAlive())
        {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    crl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    //int viewWidth = crl.getWidth();
                    int viewHeight = crl.getHeight();

                    int enKisaSutunNo = 0;//matris sutunlarından hangisinin boyu en kisa
                    int enKisaUzunluk = viewYukseklikleri[enKisaSutunNo];//en kisa boya sahip sutunun uzunluğu

                    boolean eklendiMi = false;

                    /*
                    for (int i = 0; i < viewYukseklikleri.length; i++)
                    {
                        Log.d("uyg3", "1 i : " + i + " -- " + viewYukseklikleri[i]);
                    }
                    */

                    if (matris.isEmpty())
                    {
                        matriseSatirEkle(ylsm);
                    }

                    /*
                    for (int i = 0; i < matris.size(); i++)
                    {
                        Log.d("uyg3", "matris : 1 : " + i + ":" + Arrays.toString(matris.get(i)));
                    }
                    */

                    for (int i = 0; i < viewYukseklikleri.length; i++)
                    {
                        if (viewYukseklikleri[i] == 0)//sutuna ilk eleman ekleniyor
                        {
                            enKisaUzunluk = 0;
                            enKisaSutunNo = i;
                            viewYukseklikleri[i] = viewHeight;
                            eklendiMi = true;

                            int satirNo = matristeBosSatiriBul(ylsm, i);
                            if (satirNo == -1)
                            {
                                matriseSatirEkle(ylsm);
                                matris.get(ylsm.getYerlesimMatrisSatirSayisi() - 1)[i] = elemanID;
                            }
                            else
                            {
                                matris.get(satirNo)[i] = elemanID;
                            }

                            if (i == 0)//ekrana ilk eleman ekleniyor, en ust soldaki eleman
                            {
                                crl.setLayoutParams(pa);
                            }
                            else//ilk satira eleman ekleniyor
                            {
                                int soldakiID = matris.get(0)[i - 1];
                                pa.addRule(RelativeLayout.RIGHT_OF, soldakiID);
                                pa.addRule(RelativeLayout.ALIGN_TOP, soldakiID);
                                crl.setLayoutParams(pa);
                            }

                            break;
                        }
                        else
                        {
                            if (viewYukseklikleri[i] < enKisaUzunluk)
                            {
                                enKisaUzunluk = viewYukseklikleri[i];
                                enKisaSutunNo = i;
                            }
                        }
                    }

                    //Log.d("uyg3", "en kisa : " + enKisaSutunNo + " -- deger : " + viewYukseklikleri[enKisaSutunNo] + " -- eklenecek : " + viewHeight);
                    if (!eklendiMi)
                    {
                        int a = viewYukseklikleri[enKisaSutunNo];
                        viewYukseklikleri[enKisaSutunNo] = a + viewHeight;

                        int satirNo = matristeBosSatiriBul(ylsm, enKisaSutunNo);
                        if (satirNo == -1)//sutunda boş yer yok. yeni satir eklenecek
                        {
                            matriseSatirEkle(ylsm);
                            matris.get(ylsm.getYerlesimMatrisSatirSayisi() - 1)[enKisaSutunNo] = elemanID;

                            int usttekiID = matris.get((ylsm.getYerlesimMatrisSatirSayisi() - 1) - 1)[enKisaSutunNo];
                            pa.addRule(RelativeLayout.BELOW, usttekiID);
                            pa.addRule(RelativeLayout.ALIGN_LEFT, usttekiID);
                            crl.setLayoutParams(pa);
                        }
                        else//sutunda boş yer var
                        {
                            matris.get(satirNo)[enKisaSutunNo] = elemanID;

                            int usttekiID = matris.get(satirNo - 1)[enKisaSutunNo];
                            pa.addRule(RelativeLayout.BELOW, usttekiID);
                            pa.addRule(RelativeLayout.ALIGN_LEFT, usttekiID);
                            crl.setLayoutParams(pa);
                        }
                    }

                    /*
                    for (int i = 0; i < viewYukseklikleri.length; i++)
                    {
                        Log.d("uyg3", "2 i : " + i + " -- " + viewYukseklikleri[i]);
                    }

                    for (int i = 0; i < matris.size(); i++)
                    {
                        Log.d("uyg3", "matris : 2 : " + i + ":" + Arrays.toString(matris.get(i)));
                    }
                    */
                }
            });
        }
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
}
