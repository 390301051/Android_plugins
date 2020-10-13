package com.example.windowheadtoast;
/*
2020.9.29
author：jie
company：csrd
* */
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.windowheadtoast.RomUtils.FloatWindowManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private WindowHeadToast windowHeadToast = new WindowHeadToast(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
        //   windowHeadToast.initHeadToastView();
    }

    private void initView() {
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.button) {
                windowHeadToast.showCustomToast("地址：四川阿坝藏族自治州金川县，震级7.7，经度：101.99，纬度：31.11");
            }

    }
}
