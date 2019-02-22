package com.lyd.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author lyd
 * @date 2019/2/15 0015 10:37
 * @desription
 */
public class HideKeyboardUtils {

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
     * 判断是否应该隐藏键盘
     *
     * @param event
     * @return
     */
    public static boolean isShouldHideKeyboard(List<EditText> textList, MotionEvent event) {
        for (View view : textList) {
            //判断触碰的View是否为EditText
            if (view != null) {
                int[] l = {0, 0};
                view.getLocationInWindow(l);
                int left = l[0];
                int top = l[1];
                int bottom = top + view.getHeight();
                int right = left + view.getWidth();
                if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
