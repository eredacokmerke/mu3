package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KayitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KayitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KayitFragment extends FragmentYoneticisi
{
    private View rootView;
    private MainActivity ma;
    private int kayitID;//fragment te acik olan kaydin id si
    private int kayitKlasorID;//kaydin icinde yer aldigi klasorun id si
    private OnFragmentInteractionListener mListener;

    public KayitFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KayitFragment.
     */
    public static KayitFragment newInstance(MainActivity ma)
    {
        KayitFragment fragment = new KayitFragment();
        fragment.setMa(ma);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_kayit, container, false);
        this.rootView = v;

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        getFragmentYoneticisi().setAcikFragment(this);
        getFragmentYoneticisi().setFragmentAcikMi(false);//ilk deger
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
    public void geriTusunaBasildi()
    {
        super.geriTusunaBasildi();

        getFragmentYoneticisi().kayitEkranindaGeriTusunaBasildi();
    }

    @Override
    public void init()
    {
        super.init();

        if (getArguments() != null)
        {
            String bilgiKayitBaslik = getArguments().getString(SabitYoneticisi.BILGI_KAYITFRAGMENT_BASLIK);
            int bilgiKayitID = getArguments().getInt(SabitYoneticisi.BILGI_KAYITFRAGMENT_KAYIT_ID);
            int bilgiKayitKlasorID = getArguments().getInt(SabitYoneticisi.BILGI_KAYITFRAGMENT_KAYIT_KLASOR_ID);

            TextView tv = (TextView) rootView.findViewById(R.id.fragment_kayit_llBaslik_textView);
            tv.setText(bilgiKayitBaslik);

            //KayitFragment ta etkin ekran klasor ekrani
            SabitYoneticisi.setEtkinEkran(SabitYoneticisi.EKRAN_KAYIT);

            setKayitID(bilgiKayitID);
            setKayitKlasorID(bilgiKayitKlasorID);
        }
    }

    @Override
    public void UIYukle()
    {
        super.UIYukle();

        kayitAyrintisiniVeritabanindanAl();
    }

    public String kayitAyrintisiniVeritabanindanAl()
    {
        return kayitFragmentKayitAyrintisiniVeritabanindanAl();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        getFragmentYoneticisi().setFragmentAcikMi(true);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /////getter & setter/////


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

    public int getKayitID()
    {
        return kayitID;
    }

    public void setKayitID(int kayitID)
    {
        this.kayitID = kayitID;
    }

    public int getKayitKlasorID()
    {
        return kayitKlasorID;
    }

    public void setKayitKlasorID(int kayitKlasorID)
    {
        this.kayitKlasorID = kayitKlasorID;
    }
}
