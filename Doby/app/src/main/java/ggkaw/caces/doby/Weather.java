package ggkaw.caces.doby;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class Weather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ImageView Im = (ImageView) findViewById(R.id.weather_Icon);
        Im.setVisibility(View.INVISIBLE);

    }
    public void zipCodeSet(View view) throws ExecutionException, InterruptedException {
        TextView weatherText = (TextView) findViewById(R.id.Weather_View);
        EditText zipCode = (EditText) findViewById(R.id.zipCode_text);
        ImageView Icon = (ImageView) findViewById(R.id.weather_Icon);
        String sZipCode = zipCode.getText().toString();
        String weatherID;
        try {
            String rawText = new GetWeatherData().execute(sZipCode).get();

//        String testString = GetWeatherData.getWeatherInfo(rawText);
//        System.out.println(testString);
//        String testString2 = GetWeatherData.getTempertaureInfo(rawText);
//        System.out.println(testString2);
//        String currentWeather = GetWeatherData.getWeatherInfo(rawText);
//        String currentTemp = GetWeatherData.getTempertaureInfo(rawText);
//
            String dispText = GetWeatherData.getAPIDataString(rawText);
            weatherID = GetWeatherData.getWeatherID(rawText);
            weatherText.setText(dispText);
            Drawable mDrawable;
            switch (weatherID.charAt(0)) {
                case '2':
                    mDrawable = getResources().getDrawable(R.drawable.d11);
                    Icon.setImageDrawable(mDrawable);
                    break;
                case '3':
                    mDrawable = getResources().getDrawable(R.drawable.d09);
                    Icon.setImageDrawable(mDrawable);
                    break;
                case '5':
                    mDrawable = getResources().getDrawable(R.drawable.d10);
                    Icon.setImageDrawable(mDrawable);
                    break;
                case '6':
                    mDrawable = getResources().getDrawable(R.drawable.d13);
                    Icon.setImageDrawable(mDrawable);
                    break;
                case '7':
                    mDrawable = getResources().getDrawable(R.drawable.d50);
                    Icon.setImageDrawable(mDrawable);
                    break;
                case '8':
                    switch (weatherID.charAt(2)) {
                        case '1':
                            mDrawable = getResources().getDrawable(R.drawable.d02);
                            Icon.setImageDrawable(mDrawable);
                            break;
                        case '2':
                            mDrawable = getResources().getDrawable(R.drawable.d03);
                            Icon.setImageDrawable(mDrawable);
                            break;
                    }
            }
//
//        switch(weatherID.charAt(2)){
//            case '1': mDrawable = getResources().getDrawable(R.drawable.d02);
//                Icon.setImageDrawable(mDrawable);
//                break;
//            case '2': mDrawable = getResources().getDrawable(R.drawable.d03);
//                Icon.setImageDrawable(mDrawable);
//                break;
//        }
            Icon.setVisibility(View.VISIBLE);


        }catch(StringIndexOutOfBoundsException e){

        }
    }
}
