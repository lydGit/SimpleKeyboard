package com.lyd.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * @author lyd
 * @date 2019/2/15 17:57
 * @desription 键盘适配器，主要针对对键盘进行细微处理
 */
public abstract class KeyboardAdapter implements IKeyboard {

    private Context mContext;

    /**
     * 键盘控件的布局
     */
    private View mLayoutView;

    private SimpleKeyboardView mKeyboardView;

    private KeyboardManage mManage;

    /**
     * 获取布局资源
     *
     * @return
     */
    public abstract int getLayoutRes();

    /**
     * 获取键盘资源Xml
     *
     * @return
     */
    public abstract int getKeyboardXml();

    /**
     * 获取键盘控件的id
     *
     * @return
     */
    public abstract int getKeyboardId();

    public KeyboardAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 获取键盘控件
     *
     * @return
     */
    public View getLayoutView() {
        if (mLayoutView == null) {
            mLayoutView = LayoutInflater.from(mContext).inflate(getLayoutRes(), null);
            initKeyboardView();
        }
        return mLayoutView;
    }

    /**
     * 初始化软件盘控件
     */
    private void initKeyboardView() {
        mKeyboardView = mLayoutView.findViewById(getKeyboardId());
        Keyboard keyboard = new Keyboard(mContext, getKeyboardXml());
        mKeyboardView.setKeyboard(keyboard);
        //键盘绘制监听
        mKeyboardView.setOnDrawKeboardListener(new OnKeboardDrawListener() {
            @Override
            public void onDraw(int keyCode, Keyboard.Key key, Canvas canvas) {
                draw(keyCode, key, canvas);
            }
        });
        //键盘点击监听
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                click(mManage.getFocusView(), primaryCode, keyCodes);
            }
        });
    }

    /**
     * 触发EditText中的点击事件
     */
    private void dispatchKeyDown(int code){
        EditText editText = mManage.getFocusView();
        if (editText != null) {
            //触发EditText中的OnKeyListener
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,code);
            editText.dispatchKeyEvent(event);
        }
    }

    public void setKeyboardManage(KeyboardManage mManage) {
        this.mManage = mManage;
    }

    @Override
    public void draw(int keyCode, Keyboard.Key key, Canvas canvas) {

    }

    @Override
    public void click(EditText view, int primaryCode, int[] keyCodes) {
        setText(view, String.valueOf((char) primaryCode));
    }

    /**
     * 设置输入框的文字内容
     *
     * @param view
     * @param text
     */
    public void setText(EditText view, String text) {
        Editable editable = view.getText();
        int start = view.getSelectionStart();
        int end = view.getSelectionEnd();
        editable.replace(start, end, text);
    }

    /**
     * 删除
     *
     * @param view
     */
    public void delete(EditText view) {
        Editable editable = view.getText();
        int start = view.getSelectionStart();
        int end = view.getSelectionEnd();
        // 回退键,删除字符
        if (editable != null && editable.length() > 0) {
            if (start == end) { //光标开始和结束位置相同, 即没有选中内容
                editable.delete(start - 1, start);
            } else { //光标开始和结束位置不同, 即选中EditText中的内容
                editable.delete(start, end);
            }
        }
    }

    public void change(EditText editText) {
        mManage.change(editText);
    }

    public void hide() {
        mManage.hide();
    }

    @Override
    public void complete() {
        dispatchKeyDown(KeyboardManage.STATUS_END);
    }
}
