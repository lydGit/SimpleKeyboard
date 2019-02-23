package com.lyd.keyboard;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyd
 * @date 2019/2/14 13:40
 * @desription 方便SimpleKeyboardView使用的工具类
 */
public class KeyboardManage implements IManage {

    /**
     * 键盘状态：未加载
     */
    private static final int UNLOADED = 0;
    /**
     * 键盘状态：显示
     */
    private static final int DISPLAY = 1;
    /**
     * 键盘状态：隐藏
     */
    private static final int HIDE = 2;

    /**
     * 界面的底层布局
     */
    private FrameLayout mDecorView;

    /**
     * EditText的观察列表，(用于判断点击界面的时候是否要隐藏键盘)
     */
    private List<EditText> mTextList;

    /**
     * 键盘适配器
     */
    private KeyboardAdapter mAdapter;

    /**
     * 判断当前键盘状态(0:未加载 1:显示 2:隐藏)
     */
    private int mKeyboardType = UNLOADED;

    public KeyboardManage(FrameLayout decorView, KeyboardAdapter adapter) {
        this.mDecorView = decorView;
        this.mAdapter = adapter;
        this.mTextList = new ArrayList<>();
        adapter.setKeyboardManage(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void add(final EditText editText) {
        addView(editText);
        //触摸事件触发键盘显示
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                display(editText);
                return false;
            }
        });
    }

    /**
     * @param editText
     */
    private void addView(EditText editText) {
        if (mTextList.contains(editText)) {
            mTextList.remove(editText);
        }
        mTextList.add(editText);
    }

    public void display(EditText editText) {
        //隐藏系统键盘
        HideKeyboardUtils.hideSystemKeyBoard(editText);
        //添加键盘控件进界面布局中
        if (UNLOADED == mKeyboardType) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            mDecorView.addView(mAdapter.getLayoutView(), layoutParams);
        }
        mKeyboardType = DISPLAY;
        mAdapter.setActionText(editText);
        mAdapter.getLayoutView().setVisibility(View.VISIBLE);
        scrollY(editText);
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        //键盘不显示，不需要隐藏键盘
        if (DISPLAY != mKeyboardType) {
            return;
        }
        //是否触摸到键盘，触摸键盘不需要隐藏键盘
        if (isTouchKeyboard(ev)) {
            return;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (HideKeyboardUtils.isShouldHideKeyboard(mTextList, ev)) {
                hide();
            }
        }
    }

    /**
     * 切换键盘焦点(自动下移)
     *
     * @param editText
     */
    public void change(EditText editText) {
        List<EditText> list = getEditList(mDecorView);
        int position = list.indexOf(editText);
        //判断当前EditText下方是否还有EditText控件
        if (position + 1 > list.size() - 1) {
            return;
        }
        //需要设置焦点的控件
        EditText focusView = list.get(position + 1);
        focusView.setFocusable(true);
        focusView.setFocusableInTouchMode(true);
        focusView.requestFocus();
        //判断该控件是否加入观察列表中
        if (mTextList.contains(focusView)) {
            mAdapter.setActionText(focusView);
            //必须先让布局恢复原位，否则位置显示不正确
            mDecorView.getChildAt(0).scrollTo(0, 0);
            scrollY(focusView);
        } else {
            hide();
        }
    }

    /**
     * 获取该布局下所有EditText控件
     *
     * @param viewGroup
     * @return
     */
    private List<EditText> getEditList(ViewGroup viewGroup) {
        List<EditText> list = new ArrayList<>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof EditText) {
                list.add((EditText) view);
                continue;
            }
            if (view instanceof ViewGroup) {
                list.addAll(getEditList((ViewGroup) view));
            }
        }
        return list;
    }

    /**
     * 判断是否点击键盘的位置
     *
     * @param ev
     * @return
     */
    private boolean isTouchKeyboard(MotionEvent ev) {
        int[] vLocation = new int[2];
        mAdapter.getLayoutView().getLocationOnScreen(vLocation);
        if (ev.getY() >= vLocation[1]) {
            return true;
        }
        return false;
    }

    public void hide() {
        mKeyboardType = HIDE;
        mDecorView.getChildAt(0).scrollTo(0, 0);
        mAdapter.getLayoutView().setVisibility(View.GONE);
    }

    @Override
    public void scrollY(View view) {
        int[] vLocation = new int[2];
        view.getLocationOnScreen(vLocation);
        //EditText底部坐标
        int bottomY = vLocation[1] + view.getHeight();
        // 获取屏幕的高度
        int screenHeight = HideKeyboardUtils.getWindowHeight(mDecorView.getContext());
        //屏幕与EditText底部坐标差距
        int distance = screenHeight - bottomY;
        //EditText没有超出界面显示
        if (bottomY <= screenHeight) {
            //判断键盘弹出来的时候，是否把EditText给挡住
            if ((screenHeight - mAdapter.getLayoutView().getHeight()) < bottomY) {
                mDecorView.getChildAt(0).scrollTo(0, mAdapter.getLayoutView().getHeight() - distance);
            }
        } else {
            //控件高度大于屏幕高度的情况一般只会出现在可以滚动的控件中，我们只需要上移一个键盘高度既可以
            mDecorView.getChildAt(0).scrollTo(0, mAdapter.getLayoutView().getHeight());
        }
    }

}
