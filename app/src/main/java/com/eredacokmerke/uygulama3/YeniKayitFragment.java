package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private int klasorID;//yeni klasorun icinde olusacagi klasorun id si
    private int parentKlasorID;//yeni klasorun icinde olusacagi klasorun parentinin id si
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

        CustomEditText cetBaslik = new CustomEditText(getContext(), getMa(), this);
        cetBaslik.setHint("title");
        cetBaslik.setSingleLine(true);
        CustomTextInputLayout ctilBaslik = (CustomTextInputLayout) rootView.findViewById(R.id.fragment_yeni_kayit_textInputLayout_baslik);
        ctilBaslik.addCET(cetBaslik);

        CustomEditText cetIcerik = new CustomEditText(getContext(), getMa(), this);
        cetIcerik.setHint("description");
        CustomTextInputLayout ctilIcerik = (CustomTextInputLayout) rootView.findViewById(R.id.fragment_yeni_kayit_textInputLayout_icerik);
        ctilIcerik.addCET(cetIcerik);

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

    @Override
    public void geriTusunaBasildi()
    {
        super.geriTusunaBasildi();
    }

    @Override
    public void init()
    {
        super.init();

        SabitYoneticisi.setEtkinEkran(SabitYoneticisi.EKRAN_YENI_KAYIT);

        if (getArguments() != null)
        {
            int bilgiKlasorID = getArguments().getInt(SabitYoneticisi.BILGI_YENIKAYITFRAGMENT_KLASOR_ID);
            int bilgiParentKlasorID = getArguments().getInt(SabitYoneticisi.BILGI_YENIKAYITFRAGMENT_PARENT_KLASOR_ID);

            setKlasorID(bilgiKlasorID);
            setParentKlasorID(bilgiParentKlasorID);
        }
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
    public void UIYukle()
    {
        super.UIYukle();

        renkleriAyarla();
        spinnerDoldur();
    }

    public void renkleriAyarla()
    {
        CustomTextInputLayout ctilBaslik = (CustomTextInputLayout) rootView.findViewById(R.id.fragment_yeni_kayit_textInputLayout_baslik);
        CustomTextInputLayout ctilIcerik = (CustomTextInputLayout) rootView.findViewById(R.id.fragment_yeni_kayit_textInputLayout_icerik);

        editTextRenkAyarla(rootView.findViewById(R.id.fragment_yeni_llBaslik), ctilBaslik.getCet());
        editTextRenkAyarla(rootView.findViewById(R.id.fragment_yeni_llIcerik), ctilIcerik.getCet());
    }

    /**
     * editText yazi, imlec ve alt cizgi rengini zemin rengine gore degistirir
     *
     * @param view : zemindeki layout
     * @param cet  : renk ayarlamasi yapilacak edittext
     */
    public void editTextRenkAyarla(View view, CustomEditText cet)
    {
        String renk = zeminRenginiGetir(view);

        if (renk != null)
        {
            if (renkKoyuMu(renk))
            {
                cet.setTextColor(Color.WHITE);
                editTextImlecRenginiAyarla(cet, Color.WHITE);

            }
            else
            {
                cet.setTextColor(Color.BLACK);
                editTextImlecRenginiAyarla(cet, Color.BLACK);
            }
        }
        else//zemin rengi alinamadi. siyah yazdirilacak
        {
            cet.setTextColor(Color.BLACK);
            editTextImlecRenginiAyarla(cet, Color.BLACK);
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
        TextInputLayout til = (TextInputLayout) rootView.findViewById(R.id.fragment_yeni_kayit_textInputLayout_baslik);
        CustomEditText cet = (CustomEditText) til.getRootView();
        String baslik = cet.getText().toString();

        return baslik;
    }

    /**
     * icerik edittext indeki degeri dondurur
     *
     * @return
     */
    public String icerikGetir()
    {
        TextInputLayout til = (TextInputLayout) rootView.findViewById(R.id.fragment_yeni_kayit_textInputLayout_icerik);
        CustomEditText cet = (CustomEditText) til.getRootView();
        String icerik = cet.getText().toString();

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

    public int getKlasorID()
    {
        return klasorID;
    }

    public void setKlasorID(int klasorID)
    {
        this.klasorID = klasorID;
    }

    public int getParentKlasorID()
    {
        return parentKlasorID;
    }

    public void setParentKlasorID(int parentKlasorID)
    {
        this.parentKlasorID = parentKlasorID;
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
