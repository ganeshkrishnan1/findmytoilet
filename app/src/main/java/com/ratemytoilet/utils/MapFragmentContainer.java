package com.ratemytoilet.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class MapFragmentContainer extends LinearLayout {


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }

    public MapFragmentContainer(Context context) {
        super(context);
    }

    public MapFragmentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapFragmentContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}