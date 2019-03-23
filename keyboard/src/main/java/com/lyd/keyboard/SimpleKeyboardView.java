package com.lyd.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import java.util.List;

/**
 * @author lyd
 * @date 2019/2/14 13:35
 * @desription 自定义键盘
 */
public class SimpleKeyboardView extends KeyboardView {

    OnKeboardDrawListener onDrawKeboardListener;

    public SimpleKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public SimpleKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (onDrawKeboardListener == null) {
            return;
        }
        try {
            List<Keyboard.Key> keys = getKeyboard().getKeys();
            for (Keyboard.Key key : keys) {
                onDrawKeboardListener.onDraw(key.codes[0], key, canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnDrawKeboardListener(OnKeboardDrawListener onDrawKeboardListener) {
        this.onDrawKeboardListener = onDrawKeboardListener;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        return false;
//    }
}
