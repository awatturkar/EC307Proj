package ggkaw.caces.doby;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

public class HomePage extends AppCompatActivity {

    String today;
    String tomorrow;
    String nextDay;
    String todaySched;
    String tomorrowSched;
    String nextDaySched;

    public CourseWrapper cwrap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        // Check if file where stuff is saved exists
        // If it does, read from file
        // otherwise, do nuttin

        Intent intent = getIntent();
        String checkFlag = intent.getStringExtra("Flag");
        try {
            if(checkFlag == null) {
                // this is where we will load from file if file exists
                cwrap = CourseWrapper.loadCourses();
                if(cwrap == null) {
                    cwrap = new CourseWrapper();
                }

            }
            else { // coming from another page
                cwrap = (CourseWrapper)intent.getSerializableExtra("CourseWrap");
            }
        } catch (Throwable t) {

        }

        // populate 3-day schedule
        Calendar day = Calendar.getInstance();
        // convert to string
        today = CourseInstance.calDateToString(day);

        day.add(Calendar.DATE, 1);
        tomorrow = CourseInstance.calDateToString(day);

        day.add(Calendar.DATE, 1);
        nextDay = CourseInstance.calDateToString(day);

        // HOW DO I PUT THESE IN THE GUI?
        TextView d1 = (TextView) findViewById(R.id.Day_View_1);
        TextView d2 = (TextView) findViewById(R.id.Day_View_2);
        TextView d3 = (TextView) findViewById(R.id.Day_View_3);

        // get schedule for these days
        todaySched = cwrap.stringTodaysSchedule(today);
        tomorrowSched = cwrap.stringTodaysSchedule(tomorrow);
        nextDaySched = cwrap.stringTodaysSchedule(nextDay);

        String todayStr = today + "\n" + todaySched;
        String tomorrowStr = tomorrow + "\n" + tomorrowSched;
        String nextDayStr = nextDay + "\n" + nextDaySched;

        d1.setText(todayStr);
        d2.setText(tomorrowStr);
        d3.setText(nextDayStr);

        // PUT THESE IN THE GUI TO!!
        String saveText = cwrap.saveCourses();

        try {
            String fileName = "C:\\Users\\sadie.la\\Documents\\fall2018\\EC327\\Robin\\EC307Proj\\Doby\\app\\src\\main\\java\\ggkaw\\caces\\doby\\save_test.txt";
            FileOutputStream fileOS = openFileOutput(fileName, MODE_PRIVATE);
            fileOS.write(saveText.getBytes());
            fileOS.close();
            Toast.makeText(getApplicationContext(), "Wrapper Saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// end of onCreate


    public void LaunchNewClassPage(View view) {
        Intent NewClassIntent = new Intent(this, NewClassPage.class);

        NewClassIntent.putExtra("Course Wrapper", cwrap); // Passing course class from this page to home page ...

        startActivity(NewClassIntent);
    }

    public void LaunchAssignmentPage(View view) {
        Intent NewAssignIntent = new Intent(this, AssignmentPage.class);
        // all we need to pass is string of Course names

        NewAssignIntent.putExtra("Course Wrapper", cwrap);

        Vector<String> courseNames = new Vector<String>();

        for(int i = 0; i < cwrap.allCourses.size(); i++) {
            courseNames.add(cwrap.allCourses.elementAt(i).name);
        }

        String[] stringNames = courseNames.toArray(new String[courseNames.size()]);

        NewAssignIntent.putExtra("Course Names", stringNames);

        startActivity(NewAssignIntent);
    }

    public void Right_Click(View view) {
        // get a calendar date for what "today" is
        Calendar c = CourseInstance.settingTime(today);

        c.add(Calendar.DATE, 1);
        today = CourseInstance.calDateToString(c);

        c.add(Calendar.DATE, 1);
        tomorrow = CourseInstance.calDateToString(c);

        c.add(Calendar.DATE, 1);
        nextDay = CourseInstance.calDateToString(c);

        // HOW DO I PUT THESE IN THE GUI?
         //HOW DO I PUT THESE IN THE GUI?
        TextView d1 = (TextView) findViewById(R.id.Day_View_1);
        TextView d2 = (TextView) findViewById(R.id.Day_View_2);
        TextView d3 = (TextView) findViewById(R.id.Day_View_3);


        // get schedule for these days
        todaySched = cwrap.stringTodaysSchedule(today);
        tomorrowSched = cwrap.stringTodaysSchedule(tomorrow);
        nextDaySched = cwrap.stringTodaysSchedule(nextDay);

         //put these in the GUI!

        String todayStr = today + "\n" + todaySched;
        String tomorrowStr = tomorrow + "\n" + tomorrowSched;
        String nextDayStr = nextDay + "\n" + nextDaySched;

        d1.setText(todayStr);
        d2.setText(tomorrowStr);
        d3.setText(nextDayStr);

    }

    public void Left_Click(View view) {
        // BACK UP
        // get whatever the current day in the first block is
        Calendar cfirst = CourseInstance.settingTime(today);
        cfirst.add(Calendar.DATE,-1);

        today = CourseInstance.calDateToString(cfirst);
        tomorrow = today;
        nextDay = tomorrow;

        //put today, tomorrow, and next day in the gui!

        // HOW DO I PUT THESE IN THE GUI?
        TextView d1 = (TextView) findViewById(R.id.Day_View_1);
        TextView d2 = (TextView) findViewById(R.id.Day_View_2);
        TextView d3 = (TextView) findViewById(R.id.Day_View_3);


        todaySched = cwrap.stringTodaysSchedule(today);
        tomorrowSched = cwrap.stringTodaysSchedule(tomorrow);
        nextDaySched = cwrap.stringTodaysSchedule(nextDay);

        String todayStr = today + "\n" + todaySched;
        String tomorrowStr = tomorrow + "\n" + tomorrowSched;
        String nextDayStr = nextDay + "\n" + nextDaySched;

        d1.setText(todayStr);
        d2.setText(tomorrowStr);
        d3.setText(nextDayStr);
        // put these in the GUI!

    }
}
