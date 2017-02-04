package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by ekcdr on 2/4/17.
 */

public class CustomTextInputLayout extends TextInputLayout
{
    CustomEditText cet;

    public CustomTextInputLayout(Context context)
    {
        super(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void addCET(CustomEditText cet)
    {
        setCet(cet);
        this.addView(cet);
    }

    public CustomEditText getCet()
    {
        return cet;
    }

    public void setCet(CustomEditText cet)
    {
        this.cet = cet;
    }
}
