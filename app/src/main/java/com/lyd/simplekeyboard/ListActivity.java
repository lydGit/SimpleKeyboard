package com.lyd.simplekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

/**
 * @author lyd
 * @date 2019/2/19 0019 15:38
 * @desription
 */
public class ListActivity extends AppCompatActivity {
    ListFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        fragment.getKeyboardManage().dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}