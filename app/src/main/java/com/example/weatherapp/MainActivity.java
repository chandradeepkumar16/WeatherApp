package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Struct;

import static com.example.weatherapp.R.drawable.haze;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultTextView;

    ImageView foreImage;
    ImageView foreImage2;
    ImageView foreImage3;
    ImageView foreImage4;
    ImageView foreImage5;

    String main;
    String description;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextView);

        foreImage = findViewById(R.id.imageView);
        foreImage.setVisibility(View.INVISIBLE);

        foreImage2 = findViewById(R.id.imageView2);
        foreImage2.setVisibility(View.INVISIBLE);

        foreImage3 = findViewById(R.id.imageView3);
        foreImage3.setVisibility(View.INVISIBLE);

        foreImage4 = findViewById(R.id.imageView4);
        foreImage4.setVisibility(View.VISIBLE);

        foreImage5 = findViewById(R.id.imageView5);
        foreImage5.setVisibility(View.INVISIBLE);






    }


    public void getWeather (View view){

        try {
            DownlaodTask task = new DownlaodTask();

            // to convert spaces into a useful word or correct word !

            String endcodeCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=439d4b804bc8187953eb36d2a8c26a02");

            // to hide the keyboard on user's phone when our final result comes !

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch ( Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "could not find anything there", Toast.LENGTH_SHORT).show();


        }

    }


    public class DownlaodTask extends AsyncTask<String ,Void , String> {

        @Override
        protected String doInBackground(String... urls) {
            String result ="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url = new URL(urls[0]); // setting up first url connection
                urlConnection =(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data=reader.read();

                }
                return result;


            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "could not find anything there", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather content" , weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);

                String message ="";

                for (int i=0;i<arr.length();i++){                    JSONObject jsonPart = arr.getJSON
                    Object(i);
                                        main = jsonPart.getString("main");
                    description =  jsonPart.getString("description");


                    if (main.equals("Clouds") || main.equals("clouds")) {

                        foreImage2.setVisibility(View.VISIBLE);
                        foreImage.setVisibility(View.INVISIBLE);
                        foreImage4.setVisibility(View.INVISIBLE);
                        foreImage3.setVisibility(View.INVISIBLE);
                        foreImage5.setVisibility(View.INVISIBLE);

                    }else if (main.equals("Clear") || main.equals("clear sky")) {
                        foreImage3.setVisibility(View.VISIBLE);
                        foreImage2.setVisibility(View.INVISIBLE);
                        foreImage.setVisibility(View.INVISIBLE);
                        foreImage4.setVisibility(View.INVISIBLE);
                        foreImage5.setVisibility(View.INVISIBLE);
                    }else if (main.equals("Rain")) {
                        foreImage5.setVisibility(View.VISIBLE);
                        foreImage2.setVisibility(View.INVISIBLE);
                        foreImage.setVisibility(View.INVISIBLE);
                        foreImage4.setVisibility(View.INVISIBLE);
                        foreImage3.setVisibility(View.INVISIBLE);
                    }

                    else if (main.equals("Haze") || description.equals("haze")){

                        foreImage4.setVisibility(View.VISIBLE);
                        foreImage2.setVisibility(View.INVISIBLE);
                        foreImage.setVisibility(View.INVISIBLE);
                        foreImage3.setVisibility(View.INVISIBLE);
                        foreImage5.setVisibility(View.INVISIBLE);

                    } //else if (main.equals("Haze") || description.equals("haze")){
                        //foreImage.setVisibility(View.VISIBLE);


                    //}

                    if (!main.equals("") && !description.equals("")){
                        message += main + ": " + description + "\r\n";

                    }



                }


                if (!message.equals("")){
                    resultTextView.setText(message);
                }





            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "could not find anything there", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
