package com.example.yirmialtmart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;






public class MainActivity extends AppCompatActivity {

    TextView tryText;
    TextView cadText;
    TextView usdText;
    TextView jpyText;
    TextView chfText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryText = findViewById(R.id.tryText);
        cadText = findViewById(R.id.cadText);
        usdText = findViewById(R.id.usdText);
        jpyText = findViewById(R.id.jpyText);
        chfText = findViewById(R.id.chfText);
    }

    public void getRates(View view) {
        DownloadData downloadData = new DownloadData();
        try {
            String url = "https://api.exchangerate-api.com/v4/latest/USD";
            downloadData.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("DATA", s);
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject ratesObject = jsonObject.getJSONObject("rates");
                    String turkishLira = ratesObject.getString("TRY");
                    String usd = ratesObject.getString("USD");
                    String cad = ratesObject.getString("CAD");
                    String chf = ratesObject.getString("CHF");
                    String jpy = ratesObject.getString("JPY");

                    runOnUiThread(() -> {
                        tryText.setText("TRY: " + turkishLira);
                        usdText.setText("USD: " + usd);
                        cadText.setText("CAD: " + cad);
                        chfText.setText("CHF: " + chf);
                        jpyText.setText("JPY: " + jpy);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}