package com.example.ekcdr.uygulama3;

import java.util.ArrayList;
import java.util.List;

public class Yerlesim
{
    private List<int[]> yerlesimMatris;//elemanların ekrandaki yerlesimini tutuyor
    private int[] yerlesimSutunYukseklikleri;
    private int yerlesimMatrisSatirSayisi = 0;

    public Yerlesim(int satirBasinaElemanSayisi)
    {
        yerlesimMatris = new ArrayList<>();
        yerlesimSutunYukseklikleri = new int[Integer.valueOf(satirBasinaElemanSayisi)];
        for (int i = 0; i < satirBasinaElemanSayisi; i++)
        {
            yerlesimSutunYukseklikleri[i] = 0;
        }
    }

    public List<int[]> getYerlesimMatris()
    {
        return yerlesimMatris;
    }

    public int[] getYerlesimSutunYukseklikleri()
    {
        return yerlesimSutunYukseklikleri;
    }

    public int getYerlesimMatrisSatirSayisi()
    {
        return yerlesimMatrisSatirSayisi;
    }

    public void setYerlesimMatrisSatirSayisi(int yerlesimMatrisSatirSayisi)
    {
        this.yerlesimMatrisSatirSayisi = yerlesimMatrisSatirSayisi;
    }
}
