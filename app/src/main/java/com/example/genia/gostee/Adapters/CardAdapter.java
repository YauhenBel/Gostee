package com.example.genia.gostee.Adapters;


import android.support.constraint.ConstraintLayout;

public interface CardAdapter {

    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    ConstraintLayout getConstraintLayoutwAt(int position);

    int getCount();
}
