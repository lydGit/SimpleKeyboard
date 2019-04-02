package com.lyd.keyboard;

import android.view.View;
import android.widget.EditText;

/**
 * @author lyd
 * @date 2019/2/23 11:25
 * @desription 打印管理(接口)
 */
public interface IManage {

    /**
     * 显示键盘
     */
    void display(View view);

    /**
     * 隐藏键盘
     */
    void hide();

    /**
     * 移动键盘所在界面的布局
     *
     * @param view
     */
    void scrollY(View view);

    /**
     * 切换键盘焦点(自动下移)
     *
     * @param editText
     */
    void change(EditText editText);

    /**
     * 获取adapter
     * @return
     */
    KeyboardAdapter getAdapter();
}