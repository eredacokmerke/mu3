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

    public FABYoneticisi(final MainActivity ma)
    {
        super(ma);

        FloatingActionButton fab = (FloatingActionButton) ma.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                durumuDegistir();
                //FragmentYoneticisi.fragmentAc(new YeniFragment(), MainActivity.this);
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
            }
        });

        fab1 = (FloatingActionButton) ma.findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) ma.findViewById(R.id.fab_2);

        fab1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentYoneticisi.fragmentAc(YeniFragment.newInstance(), ma);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
    public void durumuDegistir()
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
