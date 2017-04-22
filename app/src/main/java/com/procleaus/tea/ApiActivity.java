package com.procleaus.tea;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ApiActivity extends AppCompatActivity {
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
    }
    public void encryptPOST(View v){
        new PostClass(this).execute();
    }

    public void decryptGET(View v){
        new GetClass(this).execute();
    }

    private class GetClass extends AsyncTask<String, Void, Void> {
        private final Context context;

        int id;
        String salt, unlockTime;

        private GetClass(Context context) {
            this.context = context;
        }


        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                id=14;
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            t = (TextView) findViewById(R.id.textView2);
            t.setText("salt: " + salt + "     unlockTime: " + unlockTime);
        }
    }
    private class PostClass extends AsyncTask<String, Void, Void> {
        private final Context context;

        String salt2,id2;
        String unlockTime2 = "1491529806";
        private PostClass(Context context) {
            this.context = context;
        }


        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url2 = null;
                try {
                    url2 = new URL("http://api.ttencrypt.tk/demo");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                String urlParameters = "unlockTime=" + unlockTime2;
                Log.e("test",connection2.getURL().toString());
                Log.e("test",connection2.getRequestMethod().toString());
                connection2.setRequestMethod("POST");
                Log.e("test",connection2.getRequestMethod().toString());
                connection2.setRequestProperty("USER-AGENT", "TTE Android App");
                connection2.setDoOutput(true);
                DataOutputStream dStream2 = new DataOutputStream(connection2.getOutputStream());
                dStream2.writeBytes(urlParameters);
                dStream2.flush();
                dStream2.close();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                String line2;
                StringBuilder responseOutput2 = new StringBuilder();
                while ((line2 = br2.readLine()) != null) {
                    responseOutput2.append(line2);
                }
                try {
                    JSONObject reader2 = new JSONObject(responseOutput2.toString());
                    salt2 = reader2.get("salt").toString();
                    id2 = reader2.get("id").toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                br2.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            t = (TextView) findViewById(R.id.textView2);
            t.setText("salt: " + salt2 + "     id: " + id2);
        }
    }
}
