package com.lyd.keyboard;

import android.view.KeyEvent;
import android.view.View;

/**
 * @author lyd
 * @date 2019/3/20 11:29
 * @desription 键盘点击完成监听
 */
public abstract class OnKeyCompleteListener implements View.OnKeyListener {

    public abstract void onComplete(View view, int keyCode);

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (KeyboardManage.STATUS_END == keyCode) {
            onComplete(v, keyCode);
        }
        return false;
    }
}
