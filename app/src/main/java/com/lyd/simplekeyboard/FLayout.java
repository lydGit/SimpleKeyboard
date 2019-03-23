package com.lyd.simplekeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.lyd.keyboard.IKeyLayout;
import com.lyd.keyboard.OnKeyLayoutTouchListener;

/**
 * @author lyd
 * @date 2019/3/23 0023 16:43
 * @desription
 */
public class FLayout extends FrameLayout implements IKeyLayout {

    OnKeyLayoutTouchListener onKeyLayoutTouchListener;

    public FLayout(@NonNull Context context) {
        super(context);
    }

    public FLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnKeyLayoutTouchListener(OnKeyLayoutTouchListener onKeyLayoutTouchListener) {
        this.onKeyLayoutTouchListener = onKeyLayoutTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(onKeyLayoutTouchListener!=null){
            onKeyLayoutTouchListener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
