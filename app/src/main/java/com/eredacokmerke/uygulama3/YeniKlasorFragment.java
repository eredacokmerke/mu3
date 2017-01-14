package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YeniKlasorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YeniKlasorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YeniKlasorFragment extends FragmentYoneticisi
{
    private View rootView;
    private MainActivity ma;
    private int klasorID;//yeni klasorun icinde olusacagi klasorun id si
    private int parentKlasorID;//yeni klasorun icinde olusacagi klasorun parentinin id si
    private OnFragmentInteractionListener mListener;

    public YeniKlasorFragment()
    {
        // Required empty public constructor
    }

    /**
     * yeni bir fragment instance i baslatir
     */
    public static YeniKlasorFragment newInstance(MainActivity ma)
    {
        YeniKlasorFragment fragment = new YeniKlasorFragment();
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
        View v = inflater.inflate(R.layout.fragment_yeni_klasor, container, false);
        this.rootView = v;
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

        if (getArguments() != null)
        {
            int bilgiKlasorID = getArguments().getInt(SabitYoneticisi.BILGI_YENIKLASORFRAGMENT_KLASOR_ID);
            int bilgiParentKlasorID = getArguments().getInt(SabitYoneticisi.BILGI_YENIKLASORFRAGMENT_PARENT_KLASOR_ID);

            setKlasorID(bilgiKlasorID);
            setParentKlasorID(bilgiParentKlasorID);
        }
    }

    /**
     * baslik verisini doner
     *
     * @return
     */
    public String baslikGetir()
    {
        EditText et = (EditText) rootView.findViewById(R.id.fragment_yeni_klasor_edittext);
        String baslik = et.getText().toString();

        return baslik;
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
}
