package com.example.whatistheweatherudemy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather");
        editText=(EditText)findViewById(R.id.editText);
        textView=(TextView)findViewById(R.id.textView);
        button=(Button)findViewById(R.id.button);
    }
    public void getWeather(View view)
    {
        try {
            DownloadTask task = new DownloadTask();
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=439d4b804bc8187953eb36d2a8c26a02").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class DownloadTask extends AsyncTask<String,Void,String> {
        HttpURLConnection httpURLConnection=null;
        String result="";
        URL url;
        @Override
        protected String doInBackground(String... urls) {

            try {
                url=new URL(urls[0]);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream in=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data != -1){
                    char current=(char) data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                Log.i("result",s);
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("weather info",weatherInfo);
                JSONArray jsonArray=new JSONArray(weatherInfo);
                String msg = "";
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPArt=jsonArray.getJSONObject(i);
                    String main=jsonPArt.getString("main");
                    String description=jsonPArt.getString("description");
                    if(!main.equals("") && !description.equals("")){
                        msg +=main + ":" + description;
                    }
                    if(!msg.equals("")){
                        textView.setText(msg);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
