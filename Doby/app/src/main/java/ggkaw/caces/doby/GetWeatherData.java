package ggkaw.caces.doby;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class GetWeatherData extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?zip="+URLEncoder.encode(params[0], "UTF-8")+"&APPID=880e041b4a395ef756fa25edb3b7f54d");
            conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            if (in != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                    result += line;
            }
            in.close();
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if(conn!=null)
                conn.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);


    }
    public static final String getWeatherID(String Raw_API_String){
        String idCode = Raw_API_String.substring(Raw_API_String.indexOf("id")+4, Raw_API_String.indexOf("main")-2);
        return idCode;
    }
    public static final String getWeatherInfo(String Raw_API_String){
        String formattedString ="Id:" +
                Raw_API_String.substring(Raw_API_String.indexOf("id")+4,
                        Raw_API_String.indexOf("main")-2)
                + " Type:"+ Raw_API_String.substring(Raw_API_String.indexOf("main")+7,
                Raw_API_String.indexOf("description")-3) + " Description:" +
                Raw_API_String.substring(Raw_API_String.indexOf("description")+14,
                Raw_API_String.indexOf("icon")-3);
        return formattedString;
    }

    public static final String getTempertaureInfo(String Raw_API_String){

        double formatTemperature = Double.parseDouble(Raw_API_String.substring(Raw_API_String.indexOf("temp")+6,
                Raw_API_String.indexOf("pressure")-2));
        formatTemperature = (formatTemperature-273)*(9/5) + 32;
        String tempinF = String.format("%.2f", formatTemperature);
        String formattedString ="Current_Temperature:" +
                tempinF;
//                + " Low:"+ Raw_API_String.substring(Raw_API_String.indexOf("temp_min")+10,
//                Raw_API_String.indexOf("temp_max")-2) + " High:" +
//                Raw_API_String.substring(Raw_API_String.indexOf("temp_max")+10,
//                        Raw_API_String.indexOf("wind")-3);
        return formattedString;
    }

    public static final String getWeatherLocation(String Raw_API_String){
        String cityName = Raw_API_String.substring(Raw_API_String.indexOf("name")+5,Raw_API_String.indexOf("cod")-3);
        return cityName;

    }
    public static final String getAPIDataString(String Raw_API_String){
        String outString;
        double tempNum = Double.parseDouble(Raw_API_String.substring(Raw_API_String.indexOf("temp")+6,
                Raw_API_String.indexOf("pressure")-2));
        String idCode = Raw_API_String.substring(Raw_API_String.indexOf("id")+4, Raw_API_String.indexOf("main")-2);
        String humidity = Raw_API_String.substring(Raw_API_String.indexOf("humidity")+10,Raw_API_String.indexOf("temp_min")-2);

        String weatherparta = Raw_API_String.substring(Raw_API_String.indexOf("{"), Raw_API_String.indexOf("description\""));
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        Raw_API_String = Raw_API_String.replace("description\"","");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf(":"), Raw_API_String.indexOf(","));
        weatherparta = weatherparta.replace(":\"", "");
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = weatherparta.replace("\"", "");
        String forecast = weatherparta; //forecast
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf(":"), Raw_API_String.indexOf(","));
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf(","), Raw_API_String.indexOf("{")+1);
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf("\""), Raw_API_String.indexOf(":"));
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf(":"), Raw_API_String.indexOf(","));
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = weatherparta.replace(":", "");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf(","), Raw_API_String.indexOf(":"));
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf(":"), Raw_API_String.indexOf("\""));
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf("\""), Raw_API_String.indexOf(":"));
        Raw_API_String = Raw_API_String.replace(weatherparta, "");
        weatherparta = Raw_API_String.substring(Raw_API_String.indexOf(":"), Raw_API_String.indexOf(","));
        weatherparta = weatherparta.replace(":", "");
        String cityName = Raw_API_String.substring(Raw_API_String.indexOf("name")+5,Raw_API_String.indexOf("cod")-3);


        tempNum = (tempNum-273)*(9/5) + 32;
        String tempString = String.format("%.2f", tempNum);
        outString = "Location: " + cityName + "\nForecast: " + forecast +"\nTemperature: " + tempString + "\u00b0" + "F" + "\nHumidity: " + humidity + "%";
        if(tempNum < 45) {
            outString = outString + "\n\nBrrr. It's cold outside. You should probably wear a jacket and long pants.";
        }
        if(idCode.charAt(0) == '2' || idCode.charAt(0) == '3' || idCode.charAt(0) == '5'){
            outString = outString + "\n\nBetter wear some rain boots.";
        }

        if(idCode.charAt(0) == '6') {
            outString = outString + "\n\nIt's snowing! Get your snowboots on and dress warmly!";
        }

        if(idCode.charAt(0) == '8'){
            outString = outString + "\n\nBut at least there are clear skies!";
        }
        return outString;
    }

}

