package com.samples.katy.kalarm.utils;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewTreeHelper {

    public static List<View> getAllSubviews(View rootView){

        if (!(rootView instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(rootView);
            return viewArrayList;
        }

        List<View> subviews = new ArrayList<>();

        for (int i = 0; i < ((ViewGroup)rootView).getChildCount(); i++){
            View v = ((ViewGroup)rootView).getChildAt(i);

            ArrayList<View> children = new ArrayList<>();
            children.addAll(getAllSubviews(v));

            subviews.addAll(children);
        }
        return subviews;
    }
}
