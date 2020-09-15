package com.example.studymate2;

import android.content.Context;

import java.util.ArrayList;

public class MarkerItme_Adapter  {
private ArrayList<MarkerItem> arrayList;
private Context context;


    public MarkerItme_Adapter(ArrayList<MarkerItem> arrayList,Context context){
        this.arrayList = arrayList;
        this.context = context;
    }


}
