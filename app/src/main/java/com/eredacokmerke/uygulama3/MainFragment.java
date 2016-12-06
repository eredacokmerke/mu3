package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends FragmentYoneticisi
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private OnFragmentInteractionListener mListener;

    public MainFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2)
    {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        this.rootView = v;
        FragmentYoneticisi.setFragmentRootView(rootView);

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
        setAcikFragment(this);
        setFragmentAcikMi(false);//ilk deger
    }

    @Override
    public void UIYukle()
    {
        super.UIYukle();

        List<KayitLayout> listeVeriler = verileriVeritabanindanAl();
        verileriEkranaYazdir(listeVeriler);
    }

    /**
     * parametrede gelen verileri ekrana yazdirir
     *
     * @param listeVeriler : ekrana yazdirilacak verilerin listesi
     */
    public void verileriEkranaYazdir(List<KayitLayout> listeVeriler)
    {
        RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.mainFragmentRelativeLayout);

        for (int i = 0; i < listeVeriler.size(); i++)
        {
            KayitLayout kl = listeVeriler.get(i);
            kl.setId(i + 100000);
            kl.setPadding(20, 20, 20, 20);

            RelativeLayout.LayoutParams kl_lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            //layout koselerini yumustamak icin
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(10);
            shape.setColor(Color.parseColor(kl.getRenk()));
            kl.setBackground(shape);

            kl_lp.setMargins(20, 20, 20, 0);

            //layout a golge eklemek icin
            ViewCompat.setElevation(kl, 5);

            if (i != 0)
            {
                kl_lp.addRule(RelativeLayout.BELOW, i - 1 + 100000);//ilk kayit layout elemani en uste yerlesecek. digerleri kendilerinden onceki elemanin altina yerlesecek
            }
            if (i == listeVeriler.size() - 1)
            {
                kl_lp.setMargins(20, 20, 20, 20);//son kaydin alt margini var
            }
            kl.setLayoutParams(kl_lp);

            TextView tv = new TextView(MainActivity.getCnt());
            tv.setText(kl.getBaslik());
            tv.setTextColor(Color.YELLOW);

            kl.addView(tv);
            rl.addView(kl);
        }
    }

    public List<KayitLayout> verileriVeritabanindanAl()
    {
        return FragmentYoneticisi.mainFragmentVerileriVeritabanindanAl();
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
        setFragmentAcikMi(true);
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
}
