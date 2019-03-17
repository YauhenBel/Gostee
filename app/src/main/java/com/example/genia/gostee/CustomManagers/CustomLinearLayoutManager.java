package com.example.genia.gostee.CustomManagers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;


    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag){
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }


}
