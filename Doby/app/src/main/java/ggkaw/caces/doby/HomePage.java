package ggkaw.caces.doby;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Vector;

public class HomePage extends AppCompatActivity {
    private static final String FILE_NAME = "saved_data2.txt";
    public CourseWrapper cwrap;
    String today;
    String tomorrow;
    String nextDay;
    String todaySched;
    String tomorrowSched;
    String nextDaySched;

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
                cwrap = load();
                if(cwrap == null) {
                    cwrap = new CourseWrapper();
                }

            }
            else { // coming from another page
                cwrap = new CourseWrapper((CourseWrapper) getIntent().getSerializableExtra("CourseWrap"));
            }
        } catch (Throwable t) {
            if(cwrap == null) {
                cwrap = new CourseWrapper();
            }
        }

        // PUT THESE IN THE GUI TO!!
        if(cwrap.allCourses.size() != 0) {
            save(cwrap);
        }

        popSched();

    }// end of onCreate

    public void popSched() {

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
        tomorrowSched = cwrap.stringTodaysSchedule(tomorrow); // I THINK THE ERROR IS HERE
        nextDaySched = cwrap.stringTodaysSchedule(nextDay);

        String todayStr = today + "\n" + todaySched;
        String tomorrowStr = tomorrow + "\n" + tomorrowSched;
        String nextDayStr = nextDay + "\n" + nextDaySched;

        d1.setText(todayStr);
        d2.setText(tomorrowStr);
        d3.setText(nextDayStr);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save(CourseWrapper course) {
        String saveText = cwrap.saveCourses();
        FileOutputStream fos =  null;

        try {
            fos =  openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(saveText.getBytes());
            //Toast.makeText(this,"Saved!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } // /data/user/0/ggkaw.caces.doby/files/saved_data.txt
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CourseWrapper load() {
        String courseName = "";
        double multiplier;
        String startDate = "";
        String endDate = "";
        String name = "";
        String day = "";
        String date = "";
        String startTime = "";
        String endTime = "";
        String startap = "";
        String endap = "";
        String type = "";
        String current = "";
        int numOfCourses = 0;
        CourseWrapper courseWrapper = new CourseWrapper(1);
        Vector<Course> addedCourses = new Vector<Course>();

        FileInputStream fis = null;
        Course temp = new Course();

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            int count = 0;
            while ((text = br.readLine()) != null) {
                if (count == 0) {
                    numOfCourses = Integer.parseInt(text.substring(12));
                    count++;
                }
                for (int i = 0; i < numOfCourses; i++) {
                    while ((text = br.readLine()) != null) {
                        if (text.contains("####-")) {
                            text = br.readLine();
                            current = text;
                            current = current.substring(19);
                            courseName = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            multiplier = Double.parseDouble(current.substring(0, current.indexOf("$")));
                            current = current.substring(current.indexOf("$") + 1);
                            startDate = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            endDate = current;
                            temp = new Course(courseName, multiplier, startDate, endDate);
                            //courseWrapper.addCourse(temp);
                        }

                        if (text.contains(":Instance:")) {
                            current = text;
                            courseName = current.substring(10, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            name = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            day = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            date = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            startTime = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            endTime = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            startap = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            endap = current.substring(0, current.indexOf("$"));
                            current = current.substring(current.indexOf("$") + 1);
                            type = current;
                            temp.addInstance(new CourseInstance(courseName, name, day, date, startTime, endTime, startap, endap, type));
                        }
                        if (text.contains("END-OF-CLASS")) {
                            courseWrapper.addCourse(temp);
                            //delete temp;
                            break;
                        }
                    }
                }
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        } catch(IOException e){
            e.printStackTrace();
        }
        // load courseInstances into courseInstance array
        //courseWrapper.populateInstances();
        return courseWrapper;
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

        cfirst.add(Calendar.DATE, 1);
        tomorrow = CourseInstance.calDateToString(cfirst);

        cfirst.add(Calendar.DATE, 1);
        nextDay = CourseInstance.calDateToString(cfirst);

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

    public void LaunchNewClassPage(View view) {
        Intent NewClassIntent = new Intent(this, NewClassPage.class);

        NewClassIntent.putExtra("Course Wrapper", cwrap); // Passing course class from this page to home page ...

        startActivity(NewClassIntent);
    }

    public void LaunchAssignmentPage(View view) {
        Intent NewAssignIntent = new Intent(this, AssignmentPage.class);
        // all we need to pass is string of Course names

        NewAssignIntent.putExtra("Course Wrapper", cwrap);

        NewAssignIntent.putExtra("Course Names", cwrap.getCourseNames());

        startActivity(NewAssignIntent);
    }

    public void AddHWButtonPressed(View view) {
        // go to add hw page
        Intent AddHWIntent = new Intent(this, AddHomeworkTime.class);

        AddHWIntent.putExtra("Course Wrapper", cwrap);

        AddHWIntent.putExtra("Course Names", cwrap.getCourseNames());

        startActivity(AddHWIntent);

    }

    public void DeleteButtonPressed(View view) {
        // go to delete page
        Intent DeleteIntent = new Intent(this, DeletePage.class);

        DeleteIntent.putExtra("Course Wrapper", cwrap);

        DeleteIntent.putExtra("Course Names", cwrap.getCourseNames());

        DeleteIntent.putExtra("Assignment Names", cwrap.returnAssignments());

        Vector<String> studyNames = new Vector<String>();
        for(int i = 0; i < cwrap.allCourses.elementAt(0).classTimes.size(); i++) {
            studyNames.add(cwrap.allCourses.elementAt(0).classTimes.elementAt(i).courseName + cwrap.allCourses.elementAt(0).classTimes.elementAt(i).date);
        }
        String[] studNames = studyNames.toArray(new String[studyNames.size()]);

        DeleteIntent.putExtra("Study Names", studNames);

        startActivity(DeleteIntent);
    }

    public void LaunchWeatherPage(View view) {
    Intent weatherIntent = new Intent(this, Weather.class);
    startActivity(weatherIntent);
    }

    public void helpPage(View view) {
        Intent helpIntent = new Intent(this, Help.class);
        startActivity(helpIntent);
    }


//    public void PlanHWTimePressed(View view) {
//        int numStudies = 0;
//        for(int i = 0; i < cwrap.allCourses.elementAt(0).classTimes.size(); i++) {
//            if (!cwrap.allCourses.elementAt(0).classTimes.elementAt(i).split) {
//                numStudies++;
//            }
//        }
//        Vector<CourseInstance> newSplits = new Vector<CourseInstance>();
//        for(int i = 0; i < numStudies; i++) {
//            //GET THE DATES
//            newSplits = cwrap.splitHomework(cwrap.allCourses.elementAt(0).classTimes.elementAt(i).date);
//            // add new splits
//            for(int j = 0; j < newSplits.size(); j++) {
//                cwrap.allCourses.elementAt(0).addInstance(newSplits.elementAt(i));
//            }
//        }
        // remove original study blocks
//        for(int i = cwrap.allCourses.elementAt(0).classTimes.size(); i >= 0; i-- ) {
//            CourseInstance currElem = cwrap.allCourses.elementAt(0).classTimes.elementAt(i);
//            if(!currElem.split) {
//                cwrap.allCourses.elementAt(0).removeInstance(i);
//            }
//        }
//        popSched();
//    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void onDestroy() {
//        save(cwrap);
//        super.onDestroy();
//
//    }

}
