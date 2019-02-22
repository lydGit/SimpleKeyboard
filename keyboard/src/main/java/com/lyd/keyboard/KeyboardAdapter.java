package com.lyd.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * @author lyd
 * @date 2019/2/15 0015 17:57
 * @desription
 */
public abstract class KeyboardAdapter implements IKeyboard {

    private Context mContext;

    private View mLayoutView;

    private SimpleKeyboardView mKeyboardView;

    private EditText mActionText;

    private KeyboardManage keyboardManage;

    public abstract int getLayoutRes();

    public abstract int getKeyboardRes();

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
            //查找布局中的软键盘，并获取
            checkKeyboardView((ViewGroup) mLayoutView);
            Keyboard keyboard = new Keyboard(mContext, getKeyboardRes());
            mKeyboardView.setKeyboard(keyboard);
            mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener() {
                @Override
                public void onKey(int primaryCode, int[] keyCodes) {
                    onClickKeyBoard(mActionText, primaryCode, keyCodes);
                }
            });
            mLayoutView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        return mLayoutView;
    }

    public void setEditText(EditText mEditText) {
        this.mActionText = mEditText;
    }

    public void onClickKeyBoard(EditText view, int primaryCode, int[] keyCodes) {
        setText(view, String.valueOf((char) primaryCode));
    }

    public void setKeyboardManage(KeyboardManage keyboardManage) {
        this.keyboardManage = keyboardManage;
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

    public void setActionText(EditText mActionText) {
        this.mActionText = mActionText;
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

    public void change(EditText editText){
        keyboardManage.change(editText);
    }

    public void hide(){
        keyboardManage.hide();
    }

    /**
     * 查找软键盘控件
     *
     * @param group
     * @return true:找到软件盘控件
     */
    private boolean checkKeyboardView(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View childView = group.getChildAt(i);
            //判断当前childView是否软键盘
            if (childView instanceof SimpleKeyboardView) {
                mKeyboardView = (SimpleKeyboardView) childView;
                return true;
            }
            //判断当前childView是否为ViewGroup
            if (childView instanceof ViewGroup) {
                boolean b = checkKeyboardView((ViewGroup) childView);
                if (b) {
                    return true;
                }
            }
        }
        return false;
    }
}
