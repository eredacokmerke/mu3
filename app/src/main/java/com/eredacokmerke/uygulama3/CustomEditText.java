package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;

/**
 * Created by ekcdr on 1/18/17.
 */

public class CustomEditText extends TextInputEditText
{
    private MainActivity ma;
    private BackPressedListener backPressedListener;
    private FragmentYoneticisi fy;

    public CustomEditText(Context context, MainActivity ma, FragmentYoneticisi fy)
    {
        super(context);
        this.ma = ma;
        this.fy = fy;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
        {
            //klavye acik degilse fragment kapatilacak
            if (!getMa().isKeyboardOpen())
            {
                fy.geriTusunaBasildi();
            }

            if (backPressedListener != null)
            {
                backPressedListener.onImeBack(this);
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    /**
     * editText edtkinken geri tusuna basilmasini yakalamak icin
     *
     * @param listener
     */
    public void setBackPressedListener(BackPressedListener listener)
    {
        backPressedListener = listener;
    }

    public interface BackPressedListener
    {

        void onImeBack(CustomEditText editText);
    }

    public MainActivity getMa()
    {
        return ma;
    }

    public void setMa(MainActivity ma)
    {
        this.ma = ma;
    }
}
