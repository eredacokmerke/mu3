package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by ekcdr on 1/11/17.
 */

public class LayoutYoneticisi extends RelativeLayout
{
    public LayoutYoneticisi(Context context)
    {
        super(context);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tiklandi();
            }
        });
    }

    /**
     * layout a tiklandigi zaman buraya geliyor
     */
    public void tiklandi()
    {
        //override
    }
}
