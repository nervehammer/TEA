package com.procleaus.tea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AesED extends AppCompatActivity {

    private static final String salt = "s420";
    private static final String cryptPassword = "987965cbn9s5'319'79513";
    private static final String LOG_TAG = AesED.class.getSimpleName();
    ProgressDialog progressDialog;
    static int ReqCode=0;
    String src,filename,filetype;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(AesED.this, R.style.Theme_AppCompat_Light_Dialog);
        if(ReqCode==1){
            progressDialog.setMessage("Encrypting ...");
        }
        else
        {
            progressDialog.setMessage("Decrypting ...");
        }
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }

    public static void setReq(int sr){
        ReqCode=sr;
    }

    class Tasker implements Runnable{
        @Override
        public void run() {
            Intent i= new Intent(AesED.this,MainActivity.class);
            src=FilePickerHelper.setSrc();
            filename=FilePickerHelper.setFname();
          //  filetype=FilePickerHelper.setFtype();
            if(ReqCode==1){
                try {
                    encryptfile(src,filename);
                    progressDialog.dismiss();
                    startActivity(i);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    decrypt(src,filename);
                    progressDialog.dismiss();
                    startActivity(i);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Tasker()).start();
    }

    public void encryptfile(String path,String fn) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Log.i(LOG_TAG, "Encryption started");
        MainActivity.createDirIfNotExists("/Encrypted/");
        File file = new File(Environment.getExternalStoragePublicDirectory("TEA").getAbsolutePath() + "/Encrypted/",fn.concat(".crypt"));
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] key = (salt.concat(cryptPassword)).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        cos.flush();
        cos.close();
        fis.close();
        Log.i("AES", "Encryption done");
    }

    public static void decrypt(String path,String fn) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Log.i("AES", "Decryption started");
        MainActivity.createDirIfNotExists("/Decrypted/");
        File file = new File(Environment.getExternalStoragePublicDirectory("TEA").getAbsolutePath() + "/Decrypted/",fn);
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] key = (salt.concat(cryptPassword)).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
        Log.i("AES", "Decryption Done");

    }
}