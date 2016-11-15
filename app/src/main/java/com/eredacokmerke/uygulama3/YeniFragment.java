package com.eredacokmerke.uygulama3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
 * {@link YeniFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YeniFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YeniFragment extends FragmentYoneticisi
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public YeniFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YeniFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YeniFragment newInstance()
    {
        YeniFragment fragment = new YeniFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
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
        return inflater.inflate(R.layout.fragment_yeni, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        editTextRenginiAyarla(R.id.fragment_yeni_llIcerik, R.id.fragment_yeni_llIcerik_edittext);
        editTextRenginiAyarla(R.id.fragment_yeni_llBaslik, R.id.fragment_yeni_llBaslik_edittext);
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

    /**
     * layout rengine gore edittext in rengini beyaz yada siyah yapar
     *
     * @param layout
     * @param edittext
     */
    public void editTextRenginiAyarla(int layout, int edittext)
    {
        View view = getView().findViewById(layout);
        EditText et = (EditText) getView().findViewById(edittext);

        ColorDrawable viewColor = (ColorDrawable) view.getBackground();
        int colorId = viewColor.getColor();

        int red = (colorId >> 16) & 0xFF;
        int green = (colorId >> 8) & 0xFF;
        int blue = (colorId >> 0) & 0xFF;

        String hex = String.format("#%02x%02x%02x", red, green, blue);

        if (Engine.renkKoyuMu(hex))
        {
            et.setTextColor(Color.WHITE);
        }
        else
        {
            et.setTextColor(Color.BLACK);
        }
    }
}
