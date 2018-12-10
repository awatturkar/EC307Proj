package ggkaw.caces.doby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class Weather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

    }
    public void zipCodeSet(View view) throws ExecutionException, InterruptedException {
        TextView weatherText = (TextView) findViewById(R.id.Weather_View);
        EditText zipCode = (EditText) findViewById(R.id.zipCode_text);
        String sZipCode = zipCode.getText().toString();

        String rawText = new GetWeatherData().execute(sZipCode).get();
        String dispText = GetWeatherData.getAPIDataString(rawText);


        weatherText.setText(dispText);

    }
}
