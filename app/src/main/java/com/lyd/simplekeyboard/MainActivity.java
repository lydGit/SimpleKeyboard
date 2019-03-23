package com.lyd.simplekeyboard;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.lyd.keyboard.KeyboardAdapter;
import com.lyd.keyboard.KeyboardManage;

public class MainActivity extends AppCompatActivity {

    private KeyboardManage mManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeyboardAdapter adapter = new NumberKeyboardAdapter(this);
        mManage = new KeyboardManage((FrameLayout) getWindow().getDecorView(), adapter);
        mManage.add((EditText) findViewById(R.id.text1));
        mManage.add((EditText) findViewById(R.id.text2));
        findViewById(R.id.key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        getPrand(findViewById(R.id.text1));
//        findViewById(R.id.text2).setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.e("lyd"," OnKeyListener "+keyCode);
//                return false;
//            }
//        });
//        ((EditText) findViewById(R.id.text1)).setKeyListener(new KeyListener() {
//            @Override
//            public int getInputType() {
//                return 0;
//            }
//
//            @Override
//            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
//                Log.e("lyd"," onKeyDown "+text+"  "+keyCode+"  "+event.toString());
//                return false;
//            }
//
//            @Override
//            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
//                Log.e("lyd"," onKeyUp "+text+"  "+keyCode+"  "+event.toString());
//                return false;
//            }
//
//            @Override
//            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
//                Log.e("lyd"," onKeyOther "+text+"  "+text+"  "+event.toString());
//                return false;
//            }
//
//            @Override
//            public void clearMetaKeyState(View view, Editable content, int states) {
//                Log.e("lyd"," clearMetaKeyState "+content+"  "+states);
//            }
//        });
    }

    private void getPrand(View view){
        if(view.getParent()!=null){
            Log.e("lyd"," view "+view);
            getPrand((View) view.getParent());
        }
    }

    private void decor(String tag, ViewGroup group) {
        if (group == null) {
            return;
        }
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i) instanceof ViewGroup) {
                Log.e("lyd", tag + " G " + group.getChildAt(i));
                decor(tag, (ViewGroup) group.getChildAt(i));
                continue;
            }
            Log.e("lyd", tag + " V " + group.getChildAt(i));
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        mManage.dispatchTouchEvent(ev);
//        return super.dispatchTouchEvent(ev);
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideKeyboard(v, ev)) {
//                Intent intent = new Intent();
//                intent.setAction("com.lyd.keyboard.KeyboardManage.hide");
//                MainActivity.this.sendBroadcast(intent);
//
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    /**
     * 判断是否应该隐藏键盘
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideKeyboard(View v, MotionEvent event) {
        //判断触碰的View是否为EditText
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
}
