package com.lyd.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    /**
     * 当前持有焦点的EditText
     */
    private EditText mActionText;

    /**
     * 键盘管理类
     */
    private IManage mManage;

    /**
     * 获取布局资源
     *
     * @return
     */
    public abstract int getLayoutRes();

    /**
     * 获取键盘资源
     *
     * @return
     */
    public abstract int getKeyboardRes();

    /**
     * 输入完成监听
     */
    private OnEditCompleteListener onEditCompleteListener;

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
            mLayoutView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        return mLayoutView;
    }

    /**
     * 初始化软件盘控件
     */
    private void initKeyboardView() {
        //查找布局中的软键盘，并获取
        checkKeyboardView((ViewGroup) mLayoutView);
        Keyboard keyboard = new Keyboard(mContext, getKeyboardRes());
        mKeyboardView.setKeyboard(keyboard);
        mKeyboardView.setOnDrawKeboardListener(new OnKeboardDrawListener() {
            @Override
            public void onDraw(int keyCode, Keyboard.Key key, Canvas canvas) {
                draw(keyCode, key, canvas);
            }
        });
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                click(mActionText, primaryCode, keyCodes);
            }
        });
    }

    public void setActionText(EditText actionText) {
        this.mActionText = actionText;
    }

    public EditText getActionText() {
        return mActionText;
    }

    public void setKeyboardManage(IManage manage) {
        this.mManage = manage;
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

    @Override
    public void complete() {
        if (onEditCompleteListener != null) {
            onEditCompleteListener.onComplete(getActionText());
        }
    }

    public void hide() {
        mManage.hide();
    }

    public void setOnEditCompleteListener(OnEditCompleteListener onEditCompleteListener) {
        this.onEditCompleteListener = onEditCompleteListener;
    }
}
