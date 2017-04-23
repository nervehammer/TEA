package com.procleaus.tea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    private static String cryptPassword;
    private static String salt,unlockTime;
    private static String id2,salt2;
    String unlockTime2="1491529806";
    private static String id;
    private static final String LOG_TAG = AesED.class.getSimpleName();
    ProgressDialog progressDialog;
    static int ReqCode=0;
    String src,filename;



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
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    public static void setReq(int sr){
        ReqCode=sr;
    }
    public static  void setCryptPassword(String cp){
        cryptPassword=cp;
    }

  //  public static void setSalt(String ss){ salt=ss; }

    public static void setId(String sid){ id=sid; }

    class Tasker implements Runnable{
        @Override
        public void run() {
            Intent i= new Intent(AesED.this,MainActivity.class);
            src=FilePickerHelper.setSrc();
            filename=FilePickerHelper.setFname();
            if(ReqCode==1){
                try {
                    postEncrypt();
                    encryptfile(src, filename);
                    progressDialog.dismiss();
                    startActivity(i);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                //new PostClass().execute();

            } else {
                 //new GetClass().execute();
                try {
                    getDecrypt();
                    decrypt(src, filename);
                    progressDialog.dismiss();
                    startActivity(i);

                }catch (Exception e){
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

    public void postEncrypt(){
        try {
            URL url2 = null;
            try {
                url2 = new URL("http://api.ttencrypt.tk/demo");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
            String urlParameters = "unlockTime=" + unlockTime2;
            connection2.setRequestMethod("POST");
            connection2.setConnectTimeout(5000);
            connection2.setReadTimeout(5000);
            connection2.setDoOutput(true);
            Log.e("test",connection2.getRequestMethod().toString());
            connection2.setRequestProperty("USER-AGENT", "TTE Android App");
            connection2.setDoInput(true);
            DataOutputStream dStream2 = new DataOutputStream(connection2.getOutputStream());
            dStream2.writeBytes(urlParameters);
            dStream2.flush();
            dStream2.close();
            connection2.connect();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
            String line2;
            StringBuilder responseOutput2 = new StringBuilder();
            while ((line2 = br2.readLine()) != null) {
                responseOutput2.append(line2);
            }
            try {
                JSONObject reader2 = new JSONObject(responseOutput2.toString());
                salt2 = reader2.get("salt").toString();
                Log.e("test",salt2);
                id2 = reader2.get("id").toString();
              //  Toast.makeText(getApplicationContext(),"Your ID:"+id2,Toast.LENGTH_LONG).show();
                Log.i("id:",id2);

            } catch (Exception e) {
                e.printStackTrace();
            }
            br2.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    public void getDecrypt(){
        try {
//            id=14;
            URL url = new URL("http://api.ttencrypt.tk/demo?id=" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = "", line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            Log.e("test",content.toString());
            JSONObject reader = new JSONObject(content.toString());
            salt = reader.get("salt").toString();
            unlockTime = reader.get("unlockTime").toString();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void encryptfile(String path,String fn) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Log.i(LOG_TAG, "Encryption started");
        MainActivity.createDirIfNotExists("/Encrypted/");
        File file = new File(Environment.getExternalStoragePublicDirectory("TEA").getAbsolutePath() + "/Encrypted/",fn.concat(".crypt"));
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(file);
        Log.i("server salt=",salt2);
        byte[] key = (salt2.concat(cryptPassword)).getBytes("UTF-8");
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

    private void decrypt(String path,String fn) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Log.i("AES", "Decryption started");
        MainActivity.createDirIfNotExists("/Decrypted/");
        String nfn=fn.replace(".crypt","");
        File file = new File(Environment.getExternalStoragePublicDirectory("TEA").getAbsolutePath() + "/Decrypted/",nfn);
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