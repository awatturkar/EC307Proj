package ggkaw.caces.doby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class AddHomeworkTime extends AppCompatActivity {
    String selectedDate;
    CourseWrapper cwrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework_time);

        Calendar day = Calendar.getInstance();
        // convert to string
        selectedDate = CourseInstance.calDateToString(day);

        CalendarView mCalendarView = (CalendarView) findViewById(R.id.Calendar_View);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = (month+1)+ "/" +  dayOfMonth+ "/" + year;
                //dateView.setText(date);

            }
        });
    }

    public void LaunchHWAssignmentTask(View view) {
        //
        EditText hwTimeName = (EditText) findViewById(R.id.HW_Name);
        EditText startTime = (EditText) findViewById(R.id.start_time_HW);
        EditText endTime = (EditText) findViewById(R.id.end_time_HW);
        Spinner APStart = (Spinner) findViewById(R.id.spinner1);
        Spinner APEnd = (Spinner) findViewById(R.id.spinner2);

        String sHWName = hwTimeName.getText().toString();
        String sTime = startTime.getText().toString();
        String eTime = endTime.getText().toString();

        String APs = APStart.getSelectedItem().toString();
        String APe = APEnd.getSelectedItem().toString();

        cwrap = new CourseWrapper((CourseWrapper) getIntent().getSerializableExtra("Course Wrapper"));

        cwrap.allCourses.elementAt(0).addInstance(new CourseInstance("HW Time", sHWName, sTime, eTime, APs, APe, "Homework time"));
        //String courseName, String name, String day, String date, String startTime, String endTime, String startap, String endap, String type
        // adding a course INSTANCE to HW COURSE
        // use selected date

    }

    public void LaunchHWDoneTask(View view) {

        Intent sendHWWrapper = new Intent(this, HomePage.class);
        //CourseWrapper cwrap = (CourseWrapper) sendNewWrapper.getSerializableExtra("Course Wrapper");

        sendHWWrapper.putExtra("Flag", "Homework Time Added");
        sendHWWrapper.putExtra("CourseWrap", cwrap); // Passing course class from this page to home page ...
        startActivity(sendHWWrapper);

    }
}
