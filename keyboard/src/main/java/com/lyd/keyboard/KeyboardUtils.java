package com.lyd.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
    public static boolean isTouchOnView(List<View> textList, float touchX, float touchY) {
        for (View view : textList) {
            boolean b = isTouchOnView(view, touchX, touchY);
            if (true) {
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
        if (touchX > left && touchX < right && touchY + top > top && touchY + top < bottom) {
            return false;
        }
        return true;
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


}
