package com.lyd.keyboard;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
     * 输入完成
     */
    public static final int STATUS_END = 1001;

    /**
     * 界面的底层布局
     */
    private FrameLayout mDecorView;

    /**
     * EditText的观察列表，(用于判断点击界面的时候是否要隐藏键盘)
     */
    private List<View> mEditList;

    /**
     * 触摸view列表
     * 因为键盘默认点击键盘和输入框外其它位置是隐藏键盘的
     * ，但使用中需要点击其它一些地方也不能关闭键盘，所以使用该列表保存这类view
     */
    private List<View> mTouchList;

    /**
     * 键盘适配器
     */
    private KeyboardAdapter mAdapter;

    /**
     * 判断当前键盘状态(0:未加载 1:显示 2:隐藏)
     */
    private int mKeyboardType = UNLOADED;

    private OnKeyboardHideListener onKeyboardHideListener;

    private OnKeyboardDisplayListener onKeyboardDisplayListener;

    /**
     * 记录屏幕触摸的位置，手指在界面上滑动时隐藏输入法
     */
    private float moveX, moveY;

    /**
     * 滑动的最大距离，超出这距离就视为滑动了屏幕
     */
    private final static float MOVE_MAX = 50;

    public KeyboardManage(FrameLayout layout, KeyboardAdapter adapter) {
        this.mDecorView = layout;
        this.mAdapter = adapter;
        this.mEditList = new ArrayList<>();
        this.mTouchList = new ArrayList<>();
        mAdapter.setKeyboardManage(this);
        mDecorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("lyd", " touch " + event.getY());
                dispatchTouchEvent(event);
                return false;
            }
        });
        if (mDecorView instanceof IKeyLayout) {
            ((IKeyLayout) mDecorView).setOnKeyLayoutTouchListener(new OnKeyLayoutTouchListener() {
                @Override
                public void onTouch(MotionEvent ev) {
                    dispatchTouchEvent(ev);
                }
            });
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

    /**
     * @param view
     */
    private void addView(EditText view) {
        if (mEditList.contains(view)) {
            mEditList.remove(view);
        }
        mEditList.add(view);
    }

    public void addTouchView(View view) {
        if (mTouchList.contains(view)) {
            mTouchList.remove(view);
        }
        mTouchList.add(view);
    }

    /**
     * 返回持有焦点的editText
     *
     * @return
     */
    public EditText getFocusView() {
        if (mEditList == null) {
            return null;
        }
        for (View view : mEditList) {
            if (view instanceof EditText && view.isFocused()) {
                return (EditText) view;
            }
        }
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void add(final EditText view) {
        addView(view);
        //触摸事件触发键盘显示
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (KeyboardUtils.isTouchOnView(v, event.getRawX(), event.getRawY())) {
                        display(view);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 显示键盘
     */
    public void display(View view) {
        EditText editText = (EditText) view;
        //隐藏系统键盘
        KeyboardUtils.hideSystemKeyBoard(editText);
        KeyboardUtils.setFocus(editText);
        //添加键盘控件进界面布局中
        if (UNLOADED == mKeyboardType) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            mDecorView.addView(mAdapter.getLayoutView(), layoutParams);
        }
        mKeyboardType = DISPLAY;
        mAdapter.getLayoutView().setVisibility(View.VISIBLE);
        scrollY(view);
        if (onKeyboardDisplayListener != null) {
            onKeyboardDisplayListener.onDisplay(editText);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hide() {
        mKeyboardType = HIDE;
        mDecorView.getChildAt(0).scrollTo(0, 0);
        mAdapter.getLayoutView().setVisibility(View.GONE);
        //隐藏键盘视为完成输入
        mAdapter.complete();
        if (onKeyboardHideListener != null) {
            onKeyboardHideListener.onHide();
        }
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
            moveX = ev.getRawX();
            moveY = ev.getRawY();
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //判断是否滑动了界面，手指在界面上滑动时隐藏输入法
            if (Math.abs(ev.getRawX() - moveX) > MOVE_MAX || Math.abs(ev.getRawY() - moveY) > MOVE_MAX) {
                hide();
                return;
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //是否点击到输入框
            boolean isTouchEdit = KeyboardUtils.isTouchOnView(mEditList, ev.getRawX(), ev.getRawY());
            //是否点击到可触摸view
            boolean isTouchView = KeyboardUtils.isTouchOnView(mTouchList, ev.getRawX(), ev.getRawY());
            if (!isTouchEdit && !isTouchView) {
                hide();
                return;
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
        if (mEditList.contains(focusView)) {
            //必须先让布局恢复原位，否则位置显示不正确
            mDecorView.getChildAt(0).scrollTo(0, 0);
            scrollY(focusView);
        } else {
            hide();
        }
    }

    @Override
    public KeyboardAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void scrollY(View view) {
        int[] vLocation = new int[2];
        view.getLocationOnScreen(vLocation);
        //EditText底部坐标
        int bottomY = vLocation[1] + view.getHeight();
        // 获取屏幕的高度
        int screenHeight = KeyboardUtils.getWindowHeight(mDecorView.getContext());
        //屏幕与EditText底部坐标差距
        int distance = screenHeight - bottomY;
        int viewHeight = KeyboardUtils.getViewHeight(mAdapter.getLayoutView());
        //EditText没有超出界面显示
        if (bottomY <= screenHeight) {
            //判断键盘弹出来的时候，是否把EditText给挡住
            if ((screenHeight - viewHeight) < bottomY) {
                mDecorView.getChildAt(0).scrollTo(0, viewHeight - distance);
            }
        } else {
            //控件高度大于屏幕高度的情况一般只会出现在可以滚动的控件中，我们只需要上移一个键盘高度既可以
            mDecorView.getChildAt(0).scrollTo(0, mAdapter.getLayoutView().getHeight());
        }
    }

    public void setOnKeyboardHideListener(OnKeyboardHideListener onKeyboardHideListener) {
        this.onKeyboardHideListener = onKeyboardHideListener;
    }

    public void setOnKeyboardDisplayListener(OnKeyboardDisplayListener onKeyboardDisplayListener) {
        this.onKeyboardDisplayListener = onKeyboardDisplayListener;
    }

    public class KeyboardHideBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            hide();
        }
    }
}
