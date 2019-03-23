package com.lyd.keyboard;

import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;

/**
 * @author lyd
 * @date 2019/2/15 0015 17:50
 * @desription
 */
public interface IKeyboard {

    /**
     * 获取键盘总布局
     *
     * @return
     */
    View getLayoutView();

    /**
     * 键盘绘制事件，覆盖绘制
     *
     * @param keyCode
     * @param key
     * @param canvas
     */
    void draw(int keyCode, Keyboard.Key key, Canvas canvas);

    /**
     * 点击键盘触发的事件
     *
     * @param view
     * @param primaryCode
     * @param keyCodes
     */
    void click(EditText view, int primaryCode, int[] keyCodes);

    /**
     * 设置文字到键盘上
     *
     * @param view
     * @param text
     */
    void setText(EditText view, String text);

    /**
     * 删除文字内容
     *
     * @param view
     */
    void delete(EditText view);

    /**
     * 切换键盘焦点(自动下移)
     *
     * @param editText
     */
    void change(EditText editText);

    /**
     * 输入完成
     */
    void complete();

}
