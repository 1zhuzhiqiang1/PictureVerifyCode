package com.zzq.pictureverifycode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private EditText et_code = null;
    private ImageView iv_code = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        createCodeMap();
    }

    private void createCodeMap() {
        Bitmap bitmap = CodeUtils.getInstance().createBitmap();
        iv_code.setImageBitmap(bitmap);
    }

    private void initView() {
        et_code = findView(R.id.et_code);
        iv_code = findView(R.id.iv_code);
    }

    private <T extends View> T findView(int resId) {
        View view = findViewById(resId);
        return (T) view;
    }
}
