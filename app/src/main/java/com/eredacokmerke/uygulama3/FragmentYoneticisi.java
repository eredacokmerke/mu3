package com.eredacokmerke.uygulama3;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

public class FragmentYoneticisi extends Fragment
{
    //private View fragmentRootView;//acilan fragmentin root view i
    private FragmentYoneticisi acikFragment;//acilan fragmentin nesnesi
    private boolean fragmentAcikMi = false;
    //private static int fragmentKlasorID = 0;//acilan fragment in id si
    //private static int parentFragmentKlasorID = 0;//acilan fragment in parent inin id si
    private MainActivity ma;

    /**
     * yeni fragment i ekranda gosterir
     *
     * @param fy : gosterilecek fragment
     * @param ma : activity nesnesi
     */
    public static void fragmentAc(Fragment fy, MainActivity ma)
    {
        FragmentTransaction ft = ma.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fy);
        //ft.commit();//java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState hatasindan kurtulmak icin alttaki metodu kullandim
        ft.commitAllowingStateLoss();
    }

    /**
     * fragment onStart metodunda yapilacaklar
     */
    public void fragmentBasladi()
    {
        init();
        geriTusunuYakala();
        UIYukle();
    }

    /**
     * fragment icin gerekli bilgiler set ediliyor
     */
    public void init()
    {
        //override
    }

    /**
     * fragment ui nesnelerini yukler
     */
    public void UIYukle()
    {
        //override
    }

    /**
     * geri tusuna basildiginda yapilacaklar
     */
    public void geriTusunaBasildi()
    {
        //override
    }

    /**
     * geri tusuna basilmasinda fonksiyon cagrilabilmesi icin gerekli islemler yapiliyor
     */
    public void geriTusunuYakala()
    {
        getFragmentYoneticisi().getAcikFragment().getView().setFocusableInTouchMode(true);
        getFragmentYoneticisi().getAcikFragment().getView().requestFocus();
        getFragmentYoneticisi().getAcikFragment().getView().setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) && (keyEvent.getAction() == KeyEvent.ACTION_UP))
                {
                    geriTusunaBasildi();
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * klasor ekrani acikken geri tusuna basilirsa yapilacaklar
     */
    public void klasorEkranindaGeriTusunaBasildi()
    {
        if (getFragmentParentKlasorID() == 0)//parent id si 0 ise root klasordur. root klasordeyken geriye basilirsa uygulama kapanacak
        {
            getMa().finish();
        }
        else
        {
            getMa().getEngine().klasorAc(getFragmentParentKlasorID(), -1, Engine.HAREKET.GERI, "");
        }
    }

    /**
     * kayit ekrani acikken geri tusuna basilirsa yapilacaklar
     */
    public void kayitEkranindaGeriTusunaBasildi()
    {
        getMa().getEngine().klasorAc(getFragmentKayitKlasorID(), -1, Engine.HAREKET.GERI, "");
    }

    /**
     * fragment basladi
     */
    @Override
    public void onStart()
    {
        super.onStart();

        if (!getFragmentYoneticisi().getAcikFragment().isFragmentAcikMi())//eger fragment aciksa tekrardan islem yapmasin
        {
            fragmentBasladi();
        }
    }

    /**
     * fragment arkaplandan onplana getirildi
     */
    @Override
    public void onResume()
    {
        super.onResume();
    }

    /**
     * fragment arkaplana atildi
     */
    @Override
    public void onPause()
    {
        super.onPause();
        //override
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    /**
     * mainFragment ta gosterilecek kayitlari veritabanindan alir
     *
     * @return
     */
    public static List<KayitLayout> mainFragmentKayitlariVeritabanindanAl()
    {
        return Engine.mainFragmentKayitlariVeritabanindanAl();
    }

    /**
     * mainFragment ta gosterilecek klasorleri veritabanindan alir
     *
     * @return
     */
    public static List<KlasorLayout> mainFragmentKlasorleriVeritabanindanAl()
    {
        return Engine.mainFragmentKlasorleriVeritabanindanAl();
    }

    public String kayitFragmentKayitAyrintisiniVeritabanindanAl()
    {
        return getMa().getEngine().kayitFragmentKayitAyrintisiniVeritabanindanAl();
    }

    /**
     * yeniKlasorFragment te baslik verisini dondurur
     *
     * @return
     */
    public String yeniKlasorFragmentBaslikGetir()
    {
        YeniKlasorFragment ykf = (YeniKlasorFragment) getAcikFragment();
        String baslik = ykf.baslikGetir();

        return baslik;
    }

    /**
     * yeniKayitFragment te spinner da secili veriyi dondurur
     *
     * @return : spinner da secili verinin id si
     */
    public int yeniKayitFragmentSpinnerSeciliNesneyiGetir()
    {
        YeniKayitFragment yf = (YeniKayitFragment) getAcikFragment();
        int seciliID = yf.spinnerSeciliNesneyiGetir();

        return seciliID;
    }

    /**
     * yeniKayitFragment te baslik verisini dondurur
     *
     * @return : baslik verisi
     */
    public String yeniKayitFragmentBaslikGetir()
    {
        YeniKayitFragment yf = (YeniKayitFragment) getAcikFragment();
        String baslik = yf.baslikGetir();

        return baslik;
    }

    /**
     * yeniKayitFragment te icerik verisini dondurur
     *
     * @return icerik verisi
     */
    public String yeniKayitFragmentIcerikGetir()
    {
        YeniKayitFragment yf = (YeniKayitFragment) getAcikFragment();
        String baslik = yf.icerikGetir();

        return baslik;
    }

    /**
     * yeniKayitFragment ta spinner a doldurulacak verileri veritabanindan alir
     *
     * @return
     */
    public static List<String> yeniFragmentVeriTurleriniVeritabanindanAl()
    {
        return Engine.yeniFragmentVeriTurleriniVeritabanindanAl();
    }

    /**
     * edittextlerdeki imlec ve alt cizgi renklerini degistirir
     */
    public static void editTextImlecRenginiAyarla(EditText et, int color)
    {
        try
        {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(et);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(et);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = et.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[1] = et.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        }
        catch (Throwable ignored)
        {
        }

        et.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * parametredeki view in zemin rengini hex olarak doner
     *
     * @param view : zemin rengi alinacak view
     * @return : hex olarak zemin rengi
     */
    public String zeminRenginiGetir(View view)
    {
        try
        {
            ColorDrawable viewColor = (ColorDrawable) view.getBackground();
            int colorId = viewColor.getColor();

            int red = (colorId >> 16) & 0xFF;
            int green = (colorId >> 8) & 0xFF;
            int blue = (colorId >> 0) & 0xFF;

            String hex = String.format("#%02x%02x%02x", red, green, blue);

            return hex;
        }
        catch (NullPointerException e)
        {
            HataYoneticisi.ekranaHataYazdir(getMa().getApplicationContext(), "8", "ui hatasi");
            return null;
        }
    }

    /**
     * verilen rengin koyu,açık bilgisini döndürür
     *
     * @param renk renk kodu
     * @return acik ise false koyu ise true doner
     */
    public static boolean renkKoyuMu(String renk)
    {
        int irenk = Color.parseColor(renk);
        double darkness = 1 - (0.299 * Color.red(irenk) + 0.587 * Color.green(irenk) + 0.114 * Color.blue(irenk)) / 255;
        if (darkness < 0.5)
        {
            return false; //açık renk
        }
        else
        {
            return true; //koyu renk
        }
    }

    /**
     * fragment te acik olan klasorun id sini getirir
     *
     * @return : klasor id si
     */
    public int getFragmentKlasorID()
    {
        KlasorFragment kf = (KlasorFragment) getAcikFragment();
        return kf.getKlasorID();
    }

    /**
     * fragment te acik olan klasorun parent ini getirir
     *
     * @return
     */
    public int getFragmentParentKlasorID()
    {
        KlasorFragment kf = (KlasorFragment) getAcikFragment();
        return kf.getParentKlasorID();
    }

    /**
     * fragment te acik olan kaydin id sini getirir
     *
     * @return
     */
    public int getFragmentKayitID()
    {
        KayitFragment kf = (KayitFragment) getAcikFragment();
        return kf.getKayitID();
    }

    /**
     * kaydin icinde yer aldigi klasorun id si
     *
     * @return
     */
    public int getFragmentKayitKlasorID()
    {
        KayitFragment kf = (KayitFragment) getAcikFragment();
        return kf.getKayitKlasorID();
    }

    /**
     * kaydin icinde yer aldigi klasorun id si
     *
     * @return
     */
    public int getYeniKlasorFragmentKlasorID()
    {
        YeniKlasorFragment kf = (YeniKlasorFragment) getAcikFragment();
        return kf.getKlasorID();
    }

    /**
     * kaydin icinde yer aldigi klasorun parent inin id si
     *
     * @return
     */
    public int getYeniKlasorFragmentParentKlasorID()
    {
        YeniKlasorFragment kf = (YeniKlasorFragment) getAcikFragment();
        return kf.getParentKlasorID();
    }


    /////getter & setter/////


    public FragmentYoneticisi getAcikFragment()
    {
        return acikFragment;
    }

    public void setAcikFragment(FragmentYoneticisi acikFragment)
    {
        this.acikFragment = acikFragment;
    }

    public boolean isFragmentAcikMi()
    {
        return fragmentAcikMi;
    }

    public void setFragmentAcikMi(boolean fragmentAcikMi)
    {
        this.fragmentAcikMi = fragmentAcikMi;
    }

    /*
    public static int getFragmentKlasorID()
    {
        return fragmentKlasorID;
    }

    public static void setFragmentKlasorID(int fragmentKlasorID)
    {
        FragmentYoneticisi.fragmentKlasorID = fragmentKlasorID;
    }
    */

    public MainActivity getMa()
    {
        return ma;
    }

    public void setMa(MainActivity ma)
    {
        this.ma = ma;
    }

    public FragmentYoneticisi getFragmentYoneticisi()
    {
        return getMa().getEngine().getFry();
    }

    /*
    public static int getParentFragmentKlasorID()
    {
        return parentFragmentKlasorID;
    }

    public static void setParentFragmentKlasorID(int parentFragmentKlasorID)
    {
        FragmentYoneticisi.parentFragmentKlasorID = parentFragmentKlasorID;
    }
    */
}
