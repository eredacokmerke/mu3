package com.eredacokmerke.uygulama3;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

public class FragmentYoneticisi extends Fragment
{
    private static View fragmentRootView;//acilan fragmentin root view i
    private static FragmentYoneticisi acikFragment;//acilan fragmentin nesnesi
    private boolean fragmentAcikMi = false;
    private static int fragmentKlasorID = 0;

    /**
     * yeni fragment i ekranda gosterir
     *
     * @param fy       : gosterilecek fragment
     * @param ma       : activity nesnesi
     * @param klasorID : fragment te gosterilecek klasorun id si
     */
    public static void fragmentAc(Fragment fy, MainActivity ma, Bundle b, int klasorID)
    {
        if (b == null)
        {
            setFragmentKlasorID(klasorID);

            FragmentTransaction ft = ma.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fy);
            //ft.commit();//java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState hatasindan kurtulmak icin alttaki metodu kullandim
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * fragment onStart metodunda yapilacaklar
     */
    public void fragmentBasladi()
    {
        UIYukle();
    }

    /**
     * fragment ui nesnelerini yukler
     */
    public void UIYukle()
    {
        renkleriAyarla();
        //override
    }

    /**
     * fragmentteki bilesenleri renklerini ontanimli degerlerle degistirir
     */
    public void renkleriAyarla()
    {
        //override
    }

    /**
     * fragment basladi
     */
    @Override
    public void onStart()
    {
        super.onStart();

        if (!getAcikFragment().isFragmentAcikMi())//eger fragment aciksa tekrardan islem yapmasin
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
     * mainFragment ta gosterilecek verileri veritabanindan alir
     *
     * @return
     */
    public static List<KayitLayout> mainFragmentVerileriVeritabanindanAl()
    {
        return Engine.mainFragmentVerileriVeritabanindanAl();
    }

    /**
     * yeniFragment te spinner da secili nesneyi dondurur
     */
    public static int yeniFragmentSpinnerSeciliNesneyiGetir()
    {
        YeniKayitFragment yf = (YeniKayitFragment) getAcikFragment();
        int seciliID = yf.spinnerSeciliNesneyiGetir();
        yf = null;

        return seciliID;
    }

    /**
     * yeniFragment te baslik verisini dondurur
     */
    public static String yeniFragmentBaslikGetir()
    {
        YeniKayitFragment yf = (YeniKayitFragment) getAcikFragment();
        String baslik = yf.baslikGetir();
        yf = null;

        return baslik;
    }

    /**
     * yeniFragment te icerik verisini dondurur
     */
    public static String yeniFragmentIcerikGetir()
    {
        YeniKayitFragment yf = (YeniKayitFragment) getAcikFragment();
        String baslik = yf.icerikGetir();
        yf = null;

        return baslik;
    }

    /**
     * spinner a doldurulacak verileri veritabanindan alir
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
    public static String zeminRenginiGetir(View view)
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
            HataYoneticisi.ekranaHataYazdir("8", "ui hatasi");
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

    /////getter & setter/////


    public static View getFragmentRootView()
    {
        return fragmentRootView;
    }

    public static void setFragmentRootView(View fragmentRootView)
    {
        FragmentYoneticisi.fragmentRootView = fragmentRootView;
    }

    public static FragmentYoneticisi getAcikFragment()
    {
        return acikFragment;
    }

    public static void setAcikFragment(FragmentYoneticisi acikFragment)
    {
        FragmentYoneticisi.acikFragment = acikFragment;
    }

    public boolean isFragmentAcikMi()
    {
        return fragmentAcikMi;
    }

    public void setFragmentAcikMi(boolean fragmentAcikMi)
    {
        this.fragmentAcikMi = fragmentAcikMi;
    }

    public static int getFragmentKlasorID()
    {
        return fragmentKlasorID;
    }

    public static void setFragmentKlasorID(int fragmentKlasorID)
    {
        FragmentYoneticisi.fragmentKlasorID = fragmentKlasorID;
    }
}
