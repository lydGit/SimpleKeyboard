package com.lyd.keyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
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
public class KeyboardManage {

    /**
     * 界面的底层布局
     */
    private FrameLayout mDecorView;

    /**
     * 软键盘
     */
    private SimpleKeyboardView mKeyboardView;

    private List<EditText> mTextList;

    private KeyboardAdapter mAdapter;

    private Activity mActivity;

    private boolean isShow = false;

    public KeyboardManage(Activity activity, KeyboardAdapter adapter) {
        this.mActivity = activity;
        this.mAdapter = adapter;
        this.mTextList = new ArrayList<>();
        adapter.setKeyboardManage(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void add(final EditText editText) {
        addView(editText);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyboardUtils.hideSystemKeyBoard(editText);
                display(editText);
                scrollToY(v);
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
    }

    private void addView(EditText editText) {
        if (mTextList.contains(editText)) {
            mTextList.remove(editText);
        }
        mTextList.add(editText);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void display(EditText editText) {
        if (mDecorView == null) {
            mDecorView = (FrameLayout) mActivity.getWindow().getDecorView();
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            mDecorView.addView(mAdapter.getLayoutView(), layoutParams);
        }
        isShow = true;
        mAdapter.setEditText(editText);
        mAdapter.getLayoutView().setVisibility(View.VISIBLE);
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        if (!isShow) {
            return;
        }
        if (isTouchKeyboard(ev)) {
            return;
        }
        Log.e("lyd", " dispatchTouchEvent " + ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (HideKeyboardUtils.isShouldHideKeyboard(mTextList, ev)) {
                hide();
            }
        }
    }

    public void change(EditText editText) {
        List<EditText> list = getEditList(mDecorView);
        int position = list.indexOf(editText) >= 0 ? list.indexOf(editText) : 0;
        if (position + 1 > list.size() - 1) {
            return;
        }
        EditText focusView = list.get(position+1);
        focusView.setFocusable(true);
        focusView.setFocusableInTouchMode(true);
        focusView.requestFocus();
        if(mTextList.contains(focusView)) {
            mAdapter.setActionText(focusView);
            mDecorView.getChildAt(0).scrollTo(0, 0);
            scrollToY(list.get(position + 1));
        }else {
            hide();
        }
    }

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

    private boolean isTouchKeyboard(MotionEvent ev) {
        int[] vLocation = new int[2];
        mAdapter.getLayoutView().getLocationOnScreen(vLocation);
        Log.e("lyd", " isTouchKeyboard " + ev.getY() + "  " + vLocation[1]);
        if (ev.getY() >= vLocation[1]) {
            return true;
        }
        return false;
    }

    private void scrollToY(View view) {
        int[] vLocation = new int[2];
        view.getLocationOnScreen(vLocation);
        int bottomY = vLocation[1] + view.getHeight();
        // 获取屏幕的高度
        int screenHeight = HideKeyboardUtils.getWindowHeight(mActivity);
        int chaju = screenHeight - bottomY;
        if (bottomY <= screenHeight) {
            if ((screenHeight - mAdapter.getLayoutView().getHeight()) < bottomY) {
                mDecorView.getChildAt(0).scrollTo(0, mAdapter.getLayoutView().getHeight() - (screenHeight - bottomY));
            }
        } else {
            //控件高度大于屏幕高度的情况一般只会出现在可以滚动的控件中，我们只需要上移一个键盘高度既可以
            mDecorView.getChildAt(0).scrollTo(0, mAdapter.getLayoutView().getHeight());
        }
    }

    public void hide() {
        isShow = false;
        mDecorView.getChildAt(0).scrollTo(0, 0);
        mAdapter.getLayoutView().setVisibility(View.GONE);
    }

}
