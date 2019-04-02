package com.lyd.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lyd
 * @date 2019/2/15 0015 10:37
 * @desription
 */
public class KeyboardUtils {

    //隐藏系统键盘关键代码
    public static void hideSystemKeyBoard(EditText edit) {
        InputMethodManager imm = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }

        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            edit.setInputType(0);
        } else {
            try {
                Method setShowSoftInputOnFocus = EditText.class.getMethod(methodName, Boolean.TYPE);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit, Boolean.FALSE);
            } catch (NoSuchMethodException e) {
                edit.setInputType(0);
                e.printStackTrace();
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断触摸位置是否在指定view上面
     *
     * @return true：在 false：不在
     */
    public static boolean isTouchOnView(List<View> editList, float touchX, float touchY) {
        for (View view : editList) {
            boolean b = isTouchOnView(view, touchX, touchY);
            if (b) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断触摸位置是否在指定view上面
     *
     * @return true：在 false：不在
     */
    public static boolean isTouchOnView(View view, float touchX, float touchY) {
        int[] l = {0, 0};
        view.getLocationInWindow(l);
        int left = l[0];
        int top = l[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        //判断触摸位置是否在保存的EditText上
        if (touchX > left && touchX < right && touchY > top && touchY < bottom) {
            return true;
        }
        return false;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取View的高度
     *
     * @param view
     * @return
     */
    public static int getViewHeight(View view) {
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(0, h);
        return view.getMeasuredHeight();
    }

    /**
     * 获取界面中的id为content的布局（找不到就寻找该view的父布局）
     *
     * @param view
     * @return
     */
    public static View getDecorView(View view) {
        if (view == null) {
            return null;
        }
        if (view.getId() == android.R.id.content) {
            return view;
        }
        if (view.getParent() == null) {
            return null;
        }
        return getDecorView((View) view.getParent());
    }

    public static void setFocus(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

}
