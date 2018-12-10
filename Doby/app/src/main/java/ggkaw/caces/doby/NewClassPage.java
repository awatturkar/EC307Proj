package ggkaw.caces.doby;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static ggkaw.caces.doby.Course.addInstances;

public class NewClassPage extends AppCompatActivity {

    // declare the instance of Course class you will use
    Course createdCourse;
    boolean courseExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class_page);
        courseExists = false;
    }

    public void SetNewClass(View view) {
        // throw error if you have already added the class

        EditText startDateText = (EditText) findViewById(R.id.Start_Date_Edit);
        EditText endDateText = (EditText) findViewById(R.id.End_Date_Edit);
        EditText multiplier = (EditText) findViewById(R.id.multiplier);
        EditText courseName = (EditText) findViewById(R.id.Class_Name_Edit);

        String sstartDate = startDateText.getText().toString();
        String sendDate = endDateText.getText().toString();

        try {
            if(isValidDate(sstartDate, sendDate)) {

                String scourseName = courseName.getText().toString();
                double mult = Double.parseDouble(multiplier.getText().toString());

                // check if start date < end date

                createdCourse = new Course(scourseName, mult, sstartDate, sendDate); // CREATE NEW CONSTRUCTOR w/ 2 args
                courseExists = true;
            } else {
                throw new ArithmeticException("Invalid date");
            }
        } catch (ArithmeticException a) {
            Toast.makeText(this, "Please enter valid dates!", Toast.LENGTH_LONG).show();
        }
    }

    public void addAndStore(View view) {
        TextView existingSections = (TextView) findViewById(R.id.Dynam_Text_View);
        Spinner classTypeSpin = (Spinner) findViewById(R.id.Class_Type_Drop);
        Spinner classDaySpin = (Spinner) findViewById(R.id.Day_Drop);
        Spinner AMPMStart = (Spinner) findViewById(R.id.AM_PM_Start_Spin);
        Spinner AMPMEnd = (Spinner) findViewById(R.id.AM_PM_End_Spin);
        EditText className = (EditText) findViewById(R.id.Class_Name_Edit);
        EditText startTimeText = (EditText) findViewById(R.id.Start_Time_Edit);
        EditText endTimeText = (EditText) findViewById(R.id.End_Time_Edit);

        String classType = classTypeSpin.getSelectedItem().toString();
        String classDay = classDaySpin.getSelectedItem().toString();
        String APStart = AMPMStart.getSelectedItem().toString();
        String APEnd = AMPMEnd.getSelectedItem().toString();
        String startTime =  startTimeText.getText().toString();
        String endTime =  endTimeText.getText().toString();

        EditText startDateText = (EditText) findViewById(R.id.Start_Date_Edit);
        String sstartDate = startDateText.getText().toString();

        // check if start time and end time are valid
        try {
            if(courseExists) {
                if(isValidTime(startTime, endTime, APStart, APEnd)) {
                    // check if no duplicate instances
                    if(!createdCourse.hasDuplicate(sstartDate, classDay, classType, startTime, endTime, APStart, APEnd, "-")) {
                        addInstances(createdCourse, classDay, classType, startTime, endTime, APStart, APEnd);

                        Toast.makeText(this, "Lecture/Lab Added", Toast.LENGTH_LONG);

                        existingSections.append(className.getText().toString());
                        existingSections.append(" ");
                        existingSections.append(classTypeSpin.getSelectedItem().toString());
                        existingSections.append(" ");
                        existingSections.append(classDaySpin.getSelectedItem().toString());
                        existingSections.append(" ");
                        existingSections.append(startTime);
                        existingSections.append(AMPMStart.getSelectedItem().toString());
                        existingSections.append(" to ");
                        existingSections.append(endTime);
                        existingSections.append(AMPMEnd.getSelectedItem().toString());
                        existingSections.append("\n-------------\n");
                    } else {
                        throw new Exception("Duplicate instances!");
                    }

                } else {
                    throw new ArithmeticException("Invalid time");
                }
            } else {
                throw new NullPointerException("No course");
            }

        } catch (ArithmeticException a){
            Toast.makeText(this, "Please enter valid times!", Toast.LENGTH_LONG).show();
        } catch (NullPointerException n) {
            Toast.makeText(this, "Please set a class before adding times!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "You already added this section!", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isValidDate(String startDate, String endDate) {
        // check if start and end date are in mm/dd/yyyy format
        if(!startDate.contains("/") || !endDate.contains("/")) {
            return false;
        }

        String[] smdy = startDate.split("/");
        int sMonth = Integer.parseInt(smdy[0]);
        int sDay = Integer.parseInt(smdy[1]);
        int sYear = Integer.parseInt(smdy[2]);

        String[] emdy = endDate.split("/");
        int eMonth = Integer.parseInt(emdy[0]);
        int eDay = Integer.parseInt(emdy[1]);
        int eYear = Integer.parseInt(emdy[2]);

        if(eYear < sYear) {
            return false;
        } else if(eYear == sYear) {
            if(eMonth < sMonth) {
                return false;
            } else if(eMonth == sMonth) {
                if(eDay <= sDay) {
                    return false;
                }
            }
        }

        boolean startValid = GFG.isValidDate(sDay, sMonth, sYear);
        boolean endValid = GFG.isValidDate(eDay, eMonth, eYear);

        return startValid && endValid;
    }

    private boolean isValidTime(String sTime, String eTime, String sAP, String eAP) {
        if(eAP.equals("AM") && sAP.equals("PM")) {
            return false;
        }
        if(!sTime.contains(":") || !eTime.contains(":")) {
            return false;
        }

        String[] sHM = sTime.split(":");
        int sHour = Integer.parseInt(sHM[0]);
        int sMin = Integer.parseInt(sHM[1]);

        String[] eHM = eTime.split(":");
        int eHour = Integer.parseInt(eHM[0]);
        int eMin = Integer.parseInt(eHM[1]);

        if(sHour > 12 || sHour < 1 || eHour > 12 || eHour < 1 || sMin < 0 || sMin > 59 || eMin < 0 || eMin >59) {
            return false;
        }

        if(sAP.equals(eAP) && sHour > eHour) {
            return false;
        } else if(sAP.equals(eAP) && sHour == eHour) {
            if(sMin > eMin) {
                return false;
            }
        }

        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void launchDoneTask(View view) {
        CourseWrapper cwrap = new CourseWrapper((CourseWrapper) getIntent().getSerializableExtra("Course Wrapper"));
        cwrap.addCourse(createdCourse);

        Intent sendNewWrapper = new Intent(this, HomePage.class);
        //

        sendNewWrapper.putExtra("Flag", "Assignment Added");
        sendNewWrapper.putExtra("CourseWrap", cwrap); // Passing course class from this page to home page ...
        startActivity(sendNewWrapper);
    }
}
