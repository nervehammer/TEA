package com.procleaus.tea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
//    private ProgressBar progress;
//    private TextView text;
    ProgressDialog progressDialog;
    static int ReqCode=0;
    String src;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        progress = (ProgressBar) findViewById(R.id.progressBar1);
//        text = (TextView) findViewById(R.id.textView1);
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
            if(ReqCode==1){
                try {
                    encryptfile(src);
                   // Toast.makeText(AesED.this, "Encryption Successful", Toast.LENGTH_SHORT).show();
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
                    decrypt(src);
                   // Toast.makeText(AesED.this, "Decryption Successful", Toast.LENGTH_SHORT).show();
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
       public static void encryptfile(String path) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Log.i("AES","Encryption started");
//        File folder = new File(Environment.getExternalStorageDirectory() + "/Encrypted");
//        boolean success = true;
//        if (!folder.exists()) {
//            success = folder.mkdir();
//        }
//        if (success) {
        // Do something on success
            FileInputStream fis = new FileInputStream(path);
            FileOutputStream fos = new FileOutputStream(path.concat(".crypt"));
            byte[] key = (salt.concat(cryptPassword)).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key,16);
            SecretKeySpec sks = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            int b;
            byte[] d = new byte[8];
            while((b = fis.read(d)) != -1) {
                cos.write(d, 0, b);
            }
            cos.flush();
            cos.close();
            fis.close();
            Log.i("AES","Encryption done");
//        } else {
//        // Do something else on failure
//            Log.i("GG","No folder exists");
//        }


    }

    public static void decrypt(String path) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Log.i("AES", "Decryption started");
//        File folder = new File(Environment.getExternalStorageDirectory() + "/Decrypted");
//        boolean success = true;
//        if (!folder.exists()) {
//            success = folder.mkdir();
//        }
//        if (success) {
//            // Do something on success
            FileInputStream fis = new FileInputStream(path);
            FileOutputStream fos = new FileOutputStream(path.concat("dc"));
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
//        } else {
//            Log.i("GG", "No folder exists");
//        }
    }
}