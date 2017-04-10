package com.procleaus.tea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by suraj on 07-04-2017.
 */

public class SencryptActivity extends FilePickerHelper{
    Button btnatt2,btn_et2;
    String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_sencrypt);

        btnatt2 = (Button) findViewById(R.id.btnatt2);
        btn_et2=(Button)findViewById(R.id.btn_eit2);

        btnatt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickafile();

            }
        });

        btn_et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                src=setSrc();
                AesED.setReq(1);
                try {
                    Intent i= new Intent(SencryptActivity.this,AesED.class);
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}