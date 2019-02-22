package com.lyd.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * @author lyd
 * @date 2019/2/14 13:35
 * @desription 自定义键盘
 */
public class SimpleKeyboardView extends KeyboardView {

    public SimpleKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SimpleKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
