package com.procleaus.tea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by suraj on 07-03-2017.
 */

public class DecryptActivity extends FilePickerHelper {

    Button btn1, btn2;
    String src;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        btn1 = (Button) findViewById(R.id.btn_da);
        btn2 = (Button) findViewById(R.id.btn_de);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickafile();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                src = setSrc();
                AesED.setReq(2);
                Intent i = new Intent(DecryptActivity.this, AesED.class);
                startActivity(i);
            }
        });
    }
}