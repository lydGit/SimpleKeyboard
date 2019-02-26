package com.lyd.simplekeyboard;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final MotionEvent event = ev;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mManage.dispatchTouchEvent(event);
            }
        }, 1000);
        return super.dispatchTouchEvent(ev);
    }
}
