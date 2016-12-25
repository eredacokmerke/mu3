package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YeniKayitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YeniKayitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YeniKayitFragment extends FragmentYoneticisi
{
    private View rootView;
    private MainActivity ma;
    private Spinner spinner;
    private OnFragmentInteractionListener mListener;

    public YeniKayitFragment()
    {
        // Required empty public constructor
    }

    /**
     * yeni bir fragment instance i baslatir
     */
    public static YeniKayitFragment newInstance(MainActivity ma)
    {
        YeniKayitFragment fragment = new YeniKayitFragment();
        fragment.setMa(ma);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_yeni_kayit, container, false);
        this.rootView = v;
        spinner = (Spinner) rootView.findViewById(R.id.fragment_yeni_spinner);
        //getFragmentYoneticisi().setFragmentRootView(rootView);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getFragmentYoneticisi().setAcikFragment(this);
        getFragmentYoneticisi().setFragmentAcikMi(false);//ilk deger
    }

    /*
    @Override
    public void setAcikFragment(FragmentYoneticisi acikFragment)
    {
        super.setAcikFragment(acikFragment);
    }
    */

    @Override
    public void setFragmentAcikMi(boolean fragmentAcikMi)
    {
        super.setFragmentAcikMi(fragmentAcikMi);
    }

    @Override
    public void geriTusunaBasildi()
    {
        super.geriTusunaBasildi();
    }

    @Override
    public void UIYukle()
    {
        super.UIYukle();

        spinnerDoldur();
    }

    @Override
    public void renkleriAyarla()
    {
        super.renkleriAyarla();

        editTextRenkAyarla(rootView.findViewById(R.id.fragment_yeni_llBaslik), (EditText) rootView.findViewById(R.id.fragment_yeni_llBaslik_edittext));
        editTextRenkAyarla(rootView.findViewById(R.id.fragment_yeni_llIcerik), (EditText) rootView.findViewById(R.id.fragment_yeni_llIcerik_edittext));
    }

    /**
     * editText yazi, imlec ve alt cizgi rengini zemin rengine gore degistirir
     *
     * @param view : zemindeki layout
     * @param et   : renk ayarlamasi yapilacak edittext
     */
    public void editTextRenkAyarla(View view, EditText et)
    {
        String renk = zeminRenginiGetir(view);

        if (renk != null)
        {
            if (renkKoyuMu(renk))
            {
                et.setTextColor(Color.WHITE);
                editTextImlecRenginiAyarla(et, Color.WHITE);

            }
            else
            {
                et.setTextColor(Color.BLACK);
                editTextImlecRenginiAyarla(et, Color.BLACK);
            }
        }
        else//zemin rengi alinamadi. siyah yazdirilacak
        {
            et.setTextColor(Color.BLACK);
            editTextImlecRenginiAyarla(et, Color.BLACK);
        }
    }

    /**
     * spinner a doldurulacak verileri veritabanindan alir
     *
     * @return
     */
    public List<String> veriTurleriniVeritabanindanAl()
    {
        return yeniFragmentVeriTurleriniVeritabanindanAl();
    }

    /**
     * parametrede gelen listeyi spinnera doldurur
     *
     * @param spinnerArray : spinner verileri
     */
    public void veriTurleriniSpinneraDoldur(List<String> spinnerArray)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * not turu spinner ini veritabanindan gelen verilerle doldurur
     */
    public void spinnerDoldur()
    {
        List<String> listeVeriler = veriTurleriniVeritabanindanAl();
        if (listeVeriler != null)
        {
            veriTurleriniSpinneraDoldur(listeVeriler);
        }
    }

    /**
     * spinner da secili nesneyi dondurur
     */
    public int spinnerSeciliNesneyiGetir()
    {
        spinner = (Spinner) rootView.findViewById(R.id.fragment_yeni_spinner);

        return (int) spinner.getSelectedItemId();
    }

    /**
     * baslik edittext indeki degeri dondurur
     *
     * @return
     */
    public String baslikGetir()
    {
        EditText et = (EditText) rootView.findViewById(R.id.fragment_yeni_llBaslik_edittext);
        String baslik = et.getText().toString();

        return baslik;
    }

    /**
     * icerik edittext indeki degeri dondurur
     *
     * @return
     */
    public String icerikGetir()
    {
        EditText et = (EditText) rootView.findViewById(R.id.fragment_yeni_llIcerik_edittext);
        String icerik = et.getText().toString();

        return icerik;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        //fragment arkaplana atildi. onplana geldigi zaman onStart() daki islemlerin tekrar etmemesi icin flag tutuyorum
        getFragmentYoneticisi().setFragmentAcikMi(true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public MainActivity getMa()
    {
        return ma;
    }

    @Override
    public void setMa(MainActivity ma)
    {
        this.ma = ma;
    }
}
