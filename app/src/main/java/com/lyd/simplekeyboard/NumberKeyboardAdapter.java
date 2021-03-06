package com.lyd.simplekeyboard;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.lyd.keyboard.KeyboardAdapter;
import com.lyd.keyboard.OnEditCompleteListener;

/**
 * @author lyd
 * @date 2019/2/15 17:58
 * @desription 数字键盘（模板）
 */
public class NumberKeyboardAdapter extends KeyboardAdapter {

    public NumberKeyboardAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_number;
    }

    @Override
    public int getKeyboardXml() {
        return com.lyd.keyboard.R.xml.keyboard_num;
    }

    @Override
    public int getKeyboardId() {
        return R.id.skv_key;
    }

    @Override
    public void click(EditText view, int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case -1:
                setText(view, ".");
                break;
            case -2:
                hide();
                break;
            case -3:
                complete();
                change(view);
                break;
            case -4:
                delete(view);
                break;
            default:
                super.click(view, primaryCode, keyCodes);
        }
    }

}
