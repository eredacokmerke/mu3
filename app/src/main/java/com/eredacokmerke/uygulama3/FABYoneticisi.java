package com.eredacokmerke.uygulama3;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class FABYoneticisi extends FloatingActionButton
{
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;

    FloatingActionButton fab1;
    FloatingActionButton fab2;

    //Save the FAB's active status
    //false -> fab = close
    //true -> fab = open
    private boolean FABdurumu = false;

    public FABYoneticisi(final MainActivity ma, final Engine eng)
    {
        super(ma);

        final FloatingActionButton fab = (FloatingActionButton) ma.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) ma.findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) ma.findViewById(R.id.fab_2);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (SabitYoneticisi.etkinEkran)
                {
                    //kayit ekrani aciliyken fab a basildi
                    case SabitYoneticisi.EKRAN_KAYIT:

                        //acik olan fab menusu kapaniyor
                        menuDurumunuDegistir();

                        break;

                    //yeni kayit ekrani aciliyken fab a basildi
                    case SabitYoneticisi.EKRAN_YENI_KAYIT:

                        eng.yeniKayitFragmentKaydet();

                        Engine.mainFragmentAc(Engine.getFragmentKlasorID());
                        fab.setImageResource(R.drawable.ic_menu_slideshow);//fab in resmi degisiyor

                        Engine.klavyeKapat(view, ma);

                        break;

                    case SabitYoneticisi.EKRAN_YENI_KLASOR:

                        eng.yeniKlasorFragmentKaydet();

                        Engine.mainFragmentAc(Engine.getFragmentKlasorID());
                        fab.setImageResource(R.drawable.ic_menu_slideshow);//fab in resmi degisiyor

                        Engine.klavyeKapat(view, ma);

                    default:
                }

                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
            }
        });

        fab1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menuDurumunuDegistir();

                switch (SabitYoneticisi.etkinEkran)
                {
                    //kayit ekrani aciliyken fab1 e basildi
                    case SabitYoneticisi.EKRAN_KAYIT:

                        //yeni kayit ekrani acilacak
                        SabitYoneticisi.setEtkinEkran(SabitYoneticisi.EKRAN_YENI_KAYIT);

                        //fab in resmi degisiyor
                        fab.setImageResource(R.drawable.ic_menu_camera);

                        //fragment aciliyor
                        FragmentYoneticisi.fragmentAc(YeniKayitFragment.newInstance(), ma, Engine.getFragmentKlasorID());

                        break;

                    //yeni kayit ekrani aciliyken fab1 e basildi
                    case SabitYoneticisi.EKRAN_YENI_KAYIT:

                        //bu menu yeni kayit ekraninda gozukmuyor

                        break;

                    default:
                        break;
                }

            }
        });
        fab2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menuDurumunuDegistir();
                switch (SabitYoneticisi.etkinEkran)
                {
                    //kayit ekrani aciliyken fab2 ye basildi
                    case SabitYoneticisi.EKRAN_KAYIT:

                        //yeni klasor ekrani acilacak
                        SabitYoneticisi.setEtkinEkran(SabitYoneticisi.EKRAN_YENI_KLASOR);

                        //fab in resmi degisiyor
                        fab.setImageResource(R.drawable.ic_menu_camera);

                        //fragment aciliyor
                        FragmentYoneticisi.fragmentAc(YeniKlasorFragment.newInstance(), ma, Engine.getFragmentKlasorID());

                        break;

                    //yeni kayit ekrani aciliyken fab2 ye basildi
                    case SabitYoneticisi.EKRAN_YENI_KAYIT:

                        //bu menu yeni kayit ekraninda gozukmuyor

                        break;

                    default:

                        break;
                }
            }
        });

        show_fab_1 = AnimationUtils.loadAnimation(ma, R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(ma, R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(ma, R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(ma, R.anim.fab2_hide);
    }

    /**
     * fab menusunu acar
     */
    private void gosterFAB()
    {
        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 0);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 1.5);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab2.getWidth() * 0);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 2.7);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);
    }

    /**
     * fab menusunu kapatir
     */
    private void gizleFAB()
    {
        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 0);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 1.5);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);


        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 0);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 2.7);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);
    }

    /**
     * menu aciksa kapatacak, kapaliysa acacak
     */
    public void menuDurumunuDegistir()
    {
        if (!FABdurumu)
        {
            //Display FAB menu
            gosterFAB();
            FABdurumu = true;
        }
        else
        {
            //Close FAB menu
            gizleFAB();
            FABdurumu = false;
        }
    }
}
