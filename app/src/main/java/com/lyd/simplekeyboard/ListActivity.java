package com.lyd.simplekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * @author lyd
 * @date 2019/2/19 0019 15:38
 * @desription
 */
public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Log.e("lyd"," ListActivity "+getWindow().getDecorView());
    }
}
