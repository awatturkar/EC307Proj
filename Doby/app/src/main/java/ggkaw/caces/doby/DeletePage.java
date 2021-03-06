package ggkaw.caces.doby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

public class DeletePage extends AppCompatActivity {

    CourseWrapper cwrap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_page);

        cwrap = new CourseWrapper((CourseWrapper) getIntent().getSerializableExtra("Course Wrapper"));

        String[] courseNames = getIntent().getStringArrayExtra("Course Names");
        Spinner s1 = (Spinner) findViewById(R.id.Course_Spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter1);

        String[] assignmentNames = getIntent().getStringArrayExtra("Assignment Names");
        Spinner s2 = (Spinner) findViewById(R.id.Assignment_Spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, assignmentNames);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);

        String[] studyTimeNames = getIntent().getStringArrayExtra("Study Names");
        Spinner s3 = (Spinner) findViewById(R.id.study_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, studyTimeNames);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s3.setAdapter(adapter3);
    }

    public void deleteCourse(View view) {
        // get coursename from dropdown and delete it using the delete the delete function
        Spinner s = (Spinner) findViewById(R.id.Course_Spinner);
        String courseName = s.getSelectedItem().toString();
        cwrap.deleteCourse(courseName);
    }

    public void deleteAssignment(View view) {
        Spinner s = (Spinner) findViewById(R.id.Assignment_Spinner);
        String assignmentName = s.getSelectedItem().toString();
        cwrap.deleteAssignment(assignmentName);
    }

    public void launchDone(View view) {
        Intent sendNewWrapper = new Intent(this, HomePage.class);
        //CourseWrapper cwrap = (CourseWrapper) sendNewWrapper.getSerializableExtra("Course Wrapper");

        sendNewWrapper.putExtra("Flag", "Course Deleted");
        sendNewWrapper.putExtra("CourseWrap", cwrap); // Passing course class from this page to home page ...
        startActivity(sendNewWrapper);
    }

    public void deleteStudyTimes(View view) {
        Spinner s = (Spinner) findViewById(R.id.study_spinner);
        int idx = s.getSelectedItemPosition();
        cwrap.allCourses.elementAt(0).classTimes.remove(idx);
        // doesn't remove from "all instances"
    }
}

