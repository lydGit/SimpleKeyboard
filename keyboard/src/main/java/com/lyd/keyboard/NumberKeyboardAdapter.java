package com.lyd.keyboard;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

/**
 * @author lyd
 * @date 2019/2/15 0015 17:58
 * @desription
 */
public class NumberKeyboardAdapter extends KeyboardAdapter {

    public NumberKeyboardAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.keyboard_number;
    }

    @Override
    public int getKeyboardRes() {
        return R.xml.keyboard_num;
    }

    @Override
    public void onClickKeyBoard(EditText view, int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case -1:
                Log.e("lyd"," 点 ");
                setText(view,".");
                break;
            case -2:
                Log.e("lyd"," 收起 ");
                hide();
                break;
            case -3:
                Log.e("lyd"," 确定 ");
                change(view);
                break;
            case -4:
                delete(view);
                Log.e("lyd"," 删除 ");
                break;
            default:
                super.onClickKeyBoard(view, primaryCode, keyCodes);
        }
    }
}
