package com.procleaus.tea;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by suraj on 07-04-2017.
 */

public class SencryptActivity extends AppCompatActivity {
    Button btnatt2,btn_et2;
    private static final int PICKFILE_RESULT_CODE = 1;
    private String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_sencrypt);

        btnatt2 = (Button) findViewById(R.id.btnatt2);
        btn_et2=(Button)findViewById(R.id.btn_eit2);

        btnatt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

        btn_et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AesED.encryptfile(src);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    //TODO : Implement file fetch from storage and send for encryption.
                    Toast.makeText(SencryptActivity.this, "File Picked ", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    src=uri.getPath();
                    Log.i("Datapath:", src);

                    break;
                }
        }
    }
}