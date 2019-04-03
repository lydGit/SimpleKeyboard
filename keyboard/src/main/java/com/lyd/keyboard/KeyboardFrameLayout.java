package com.lyd.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author lyd
 * @date 2019/4/3 0003 10:27
 * @desription
 */
public class KeyboardFrameLayout extends FrameLayout implements IKeyLayout {

    OnKeyLayoutTouchListener onKeyLayoutTouchListener;

    public KeyboardFrameLayout(Context context) {
        super(context);
    }

    public KeyboardFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnKeyLayoutTouchListener(OnKeyLayoutTouchListener onKeyLayoutTouchListener) {
        this.onKeyLayoutTouchListener = onKeyLayoutTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (onKeyLayoutTouchListener != null) {
            onKeyLayoutTouchListener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}