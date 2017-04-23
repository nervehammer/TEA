package com.procleaus.tea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by suraj on 07-03-2017.
 */

public class DecryptActivity extends FilePickerHelper {

    Button btn1, btn2;
    String src;
    EditText et_id,et_dp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        btn1 = (Button) findViewById(R.id.btn_da);
        btn2 = (Button) findViewById(R.id.btn_de);
        et_id= (EditText)findViewById(R.id.et_id);
        et_dp=(EditText) findViewById(R.id.et_dp);
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
                AesED.setId(et_id.getText().toString());
                AesED.setCryptPassword(et_dp.getText().toString());
                Intent i = new Intent(DecryptActivity.this, AesED.class);
                startActivity(i);
            }
        });
    }
}