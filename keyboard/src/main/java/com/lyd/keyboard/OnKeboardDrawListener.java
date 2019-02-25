package com.lyd.keyboard;

import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;

/**
 * @author lyd
 * @date 2019/2/25 11:09
 * @desription 键盘绘制监听
 */
public interface OnKeboardDrawListener {

    /**
     *
     * @param keyCode 按钮号码
     * @param key     按钮信息
     * @param canvas
     */
    void onDraw(int keyCode, Keyboard.Key key, Canvas canvas);

}
