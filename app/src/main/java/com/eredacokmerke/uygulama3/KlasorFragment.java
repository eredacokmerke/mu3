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
 * {@link KlasorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KlasorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KlasorFragment extends FragmentYoneticisi
{
    private MainActivity ma;
    private View rootView;
    private int klasorID;//fragment te acilan klasorun id si
    private int parentKlasorID;//fragment te acilan klasorun parent inin id si
    private OnFragmentInteractionListener mListener;

    public KlasorFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KlasorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KlasorFragment newInstance(MainActivity ma)
    {
        KlasorFragment fragment = new KlasorFragment();
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
        View v = inflater.inflate(R.layout.fragment_klasor, container, false);
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

    @Override
    public void geriTusunaBasildi()
    {
        super.geriTusunaBasildi();
        getFragmentYoneticisi().klasorEkranindaGeriTusunaBasildi();
    }

    @Override
    public void init()
    {
        super.init();

        if (getArguments() != null)
        {
            String bilgiKlasorBaslik = getArguments().getString(SabitYoneticisi.BILGI_KLASORFRAGMENT_BASLIK);
            int bilgiKlasorID = getArguments().getInt(SabitYoneticisi.BILGI_KLASORFRAGMENT_KLASOR_ID);
            int bilgiParentKlasorID = getArguments().getInt(SabitYoneticisi.BILGI_KLASORFRAGMENT_PARENT_KLASOR_ID);
            Engine.HAREKET hareket = (Engine.HAREKET) getArguments().get(SabitYoneticisi.BILGI_KLASORFRAGMENT_HAREKET);

            //KlasorFragment ta etkin ekran klasor ekrani
            SabitYoneticisi.setEtkinEkran(SabitYoneticisi.EKRAN_KLASOR);
            setKlasorID(bilgiKlasorID);

            switch (hareket)
            {
                case ILERI:
                    setParentKlasorID(bilgiParentKlasorID);
                    //setParentFragmentKlasorID(getFragmentKlasorID());//alt fragment acilirken icinde bulundugum fragmentin id sini parenta kopyaliyorum

                    //eger hareket ileri ise ve baslik bos gelmisse baslik degistirilmeyecek
                    //ornegin yeni klasor ekraninda tamam a basinca hareket ileri fakat basligin degismesine gerek yok
                    if (!bilgiKlasorBaslik.equals(""))
                    {
                        ma.toolbarBaslikGuncelle(bilgiKlasorBaslik);
                    }

                    break;

                case GERI:
                    setParentKlasorID(ma.getEngine().parentKlasorIDyiGetir(bilgiKlasorID));
                    //setParentFragmentKlasorID(ma.getEngine().parentKlasorIDyiGetir(klasorID));//fragmentte acilan klasorun parent ini veritabanindan aliyorum
                    ma.toolbarBaslikGuncelle(ma.getEngine().klasorBaslikGetir(bilgiKlasorID));

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void UIYukle()
    {
        super.UIYukle();

        List<KlasorLayout> listeKlasorler = klasorleriVeritabanindanAl();
        List<KayitLayout> listeKayitlar = kayitlariVeritabanindanAl();

        if (listeKlasorler != null && listeKayitlar != null)
        {
            KlasorLayout sonKL = klasorleriEkranaYazdir(listeKlasorler, null);
            kayitlariEkranaYazdir(listeKayitlar, sonKL);
        }

        /*
        // TODO: 12/6/16 ekrana once kayit sonra klasor eklenirse bu sekilde olacak. ayarlardan degistirilebilir yap

        List<KayitLayout> listeKayitlar = kayitlariVeritabanindanAl();
        KayitLayout sonKL = kayitlariEkranaYazdir(listeKayitlar, null);

        List<KayitLayout> listeKlasorler = klasorleriVeritabanindanAl();
        klasorleriEkranaYazdir(listeKlasorler, sonKL);
        */
    }

    /**
     * parametrede gelen klasorleri ekrana yazdirir
     *
     * @param listeKlasorler : ekrana yazdirilacak klasorlerin listesi
     * @param sonKL          : ekrana onceden eklenmis son KayitLayout nesnesi
     * @return : ekrana son eklenen kayitLayout nesnesi
     */
    public KlasorLayout klasorleriEkranaYazdir(List<KlasorLayout> listeKlasorler, KayitLayout sonKL)
    {
        RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.mainFragmentRelativeLayout);

        int idBaslangic = 100000;

        if (sonKL != null)//eger oncden ekrana yerlestirilmis klasor nesneleri varsa son nesnenin id sini aliyorum
        {
            idBaslangic = sonKL.getId() + 1;
        }

        for (int i = 0; i < listeKlasorler.size(); i++)
        {
            KlasorLayout kl = listeKlasorler.get(i);
            kl.setId(i + idBaslangic);
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

            if (sonKL != null && i == 0)//eger onceden klasor eklenmisse, kayitlar son klasor nesnesinin altina yerlesecekler
            {
                kl_lp.addRule(RelativeLayout.BELOW, idBaslangic - 1);
            }
            if (i != 0)
            {
                kl_lp.addRule(RelativeLayout.BELOW, i + idBaslangic - 1);//ilk kayit layout elemani en uste yerlesecek. digerleri kendilerinden onceki elemanin altina yerlesecek
            }
            if (i == listeKlasorler.size() - 1)
            {
                kl_lp.setMargins(20, 20, 20, 20);//son kaydin alt margini var
            }
            kl.setLayoutParams(kl_lp);

            TextView tv = new TextView(getContext());
            tv.setText(kl.getBaslik());
            tv.setTextColor(Color.YELLOW);

            kl.addView(tv);
            rl.addView(kl);
        }

        if (listeKlasorler.isEmpty())
        {
            return null;
        }
        else
        {
            return listeKlasorler.get(listeKlasorler.size() - 1);
        }
    }

    /**
     * parametrede gelen kayitlari ekrana yazdirir
     *
     * @param listeKayitlar : ekrana yazdirilacak kayitlarin listesi
     * @param sonKL         : ekrana onceden eklenmis son KayitLayout nesnesi
     * @return : ekrana son eklenen kayitLayout nesnesi
     */
    public KayitLayout kayitlariEkranaYazdir(List<KayitLayout> listeKayitlar, KlasorLayout sonKL)
    {
        RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.mainFragmentRelativeLayout);

        int idBaslangic = 100000;

        if (sonKL != null)//eger oncden ekrana yerlestirilmis klasor nesneleri varsa son nesnenin id sini aliyorum
        {
            idBaslangic = sonKL.getId() + 1;
        }

        for (int i = 0; i < listeKayitlar.size(); i++)
        {
            KayitLayout kl = listeKayitlar.get(i);
            kl.setId(i + idBaslangic);
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

            if (sonKL != null && i == 0)//eger onceden klasor eklenmisse, kayitlar son klasor nesnesinin altina yerlesecekler
            {
                kl_lp.addRule(RelativeLayout.BELOW, idBaslangic - 1);
            }
            if (i != 0)//ilk kayit layout elemani en uste yerlesecek. digerleri kendilerinden onceki elemanin altina yerlesecek
            {
                kl_lp.addRule(RelativeLayout.BELOW, i + idBaslangic - 1);
            }
            if (i == listeKayitlar.size() - 1)//son kaydin alt margini var
            {
                kl_lp.setMargins(20, 20, 20, 20);
            }
            kl.setLayoutParams(kl_lp);

            TextView tv = new TextView(getContext());
            tv.setText(kl.getBaslik());
            tv.setTextColor(Color.YELLOW);

            kl.addView(tv);
            rl.addView(kl);
        }

        if (listeKayitlar.isEmpty())
        {
            return null;
        }
        else
        {
            return listeKayitlar.get(listeKayitlar.size() - 1);
        }
    }

    /**
     * kayitlari veritabanindan alir
     *
     * @return
     */
    public List<KayitLayout> kayitlariVeritabanindanAl()
    {
        return mainFragmentKayitlariVeritabanindanAl();
    }

    /**
     * klsorleri veritabanindan alir
     *
     * @return
     */
    public List<KlasorLayout> klasorleriVeritabanindanAl()
    {
        return mainFragmentKlasorleriVeritabanindanAl();
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

    public int getParentKlasorID()
    {
        return parentKlasorID;
    }

    public void setParentKlasorID(int parentKlasorID)
    {
        this.parentKlasorID = parentKlasorID;
    }

    public int getKlasorID()
    {
        return klasorID;
    }

    public void setKlasorID(int klasorID)
    {
        this.klasorID = klasorID;
    }
}
