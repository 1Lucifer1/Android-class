package com.bytedance.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView textView;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity");

        //使用findViewById 得到TextView对象
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editTextNumber);
        //使用setText()方法修改文本
        //textView.setText(R.string.modify);
    }

    public void onClick(View v){
        textView.setText(editText.getText());
    }
}
