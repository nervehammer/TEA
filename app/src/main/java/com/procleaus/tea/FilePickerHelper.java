package com.procleaus.tea;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;


/**
 * Created by suraj on 08-04-2017.
 */

public class FilePickerHelper extends AppCompatActivity{
    private static final int PICKFILE_RESULT_CODE = 1;
    public static String s,fn,ft;

    public void pickafile(){
        Log.i("Filepicker:","started");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    s=uri.getPath();
                    //ft= getContentResolver().getType(uri);
                    File file = new File(s);
                    fn=file.getName();

                }
                break;
        }
    }

    public static String setSrc(){
        return s;
    }
    public static String setFname(){
        return fn;
    }
    public static String setFtype(){
        return ft;
    }

}
