package ggkaw.caces.doby;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by sadie.la on 12/4/2018.
 */
public class CourseWrapper implements Serializable {
    Vector<Course> allCourses; // holds all courses added
    Vector<CourseInstance> allInstances; // holds every calendar event

    CourseWrapper() {
        this.allCourses = new Vector<Course>();
        this.allCourses.add(new Course("Homework Times", 1, "1/1/2010", "1/1/3000"));
        // always first course
        this.allInstances = new Vector<CourseInstance>();
    }

    CourseWrapper(int p) {
        // need to denote that its the one from the load function
        this.allCourses = new Vector<Course>();
        //this.allCourses.add(new Course("Homework Times", 1, "1/1/2010", "1/1/3000"));
        // always first course
        this.allInstances = new Vector<CourseInstance>();
    }

    // CourseWrapper(Vector<CourseInstance> cVec)
    // {
    //     // cvec holds homework instances
    //     this.allCourses = new Vector<Course>();
    //     this.allInstances = new Vector<CourseInstance>();
    //     for(int i = 0; i < cVec.size(); i ++)
    //     {
    //         this.allCourses.elementAt(0).addInstance(cVec.elementAt(i));
    //     }
    // }

    // Copy Constructor
    CourseWrapper(CourseWrapper c) {
        this.allCourses = new Vector<Course>();
        this.allInstances = new Vector<CourseInstance>();
        for(int i = 0; i < c.allCourses.size(); i++) {
            this.allCourses.add(c.allCourses.elementAt(i));
        }
        for(int j = 0; j < c.allInstances.size(); j++) {
            this.allInstances.add(c.allInstances.elementAt(j));
        }
    }

    public void addCourse(Course course) {
        this.allCourses.add(course);
        for(int i = 0; i < course.classTimes.size(); i++) {
            this.allInstances.add(course.classTimes.get(i));
        }
    }

    public void addCourseInstances(Vector<CourseInstance> newInstances) {
        for (int i = 0; i < newInstances.size(); i++) {
            String courseNameOfCurrInst = newInstances.elementAt(i).courseName;
            for(int j = 0; j < this.allCourses.size(); j++) {
                String currentCourseName = this.allCourses.elementAt(j).name;
                if(currentCourseName.equals(courseNameOfCurrInst)) {
                    this.allCourses.elementAt(j).addInstance(newInstances.elementAt(i));
                    //break; // will not have the same name as multiple courses
                }
            }
            this.allInstances.add(newInstances.elementAt(i));
        }
    }



    public void populateInstances() {
        // puts all course instances into "all instances" field of course wrapper
        int numCourses = this.allCourses.size();
        for(int i = 0; i < numCourses; i++) {
            for(int j = 0; j < this.allCourses.elementAt(i).classTimes.size(); j++) {
                this.allInstances.add(this.allCourses.elementAt(i).classTimes.elementAt(j));
            }
        }

    }

    // for deleting entire courses and all instances
    public void deleteCourse(String courseName) {
        for(int i = 0; i < this.allCourses.size(); i++) {
            if(this.allCourses.elementAt(i).name.equals(courseName)) {
                // delete this course from the wrapper!!!
                this.allCourses.remove(i);
                break; // should only delete one course at a time
            }
        }
        // also need to remove all course instances with the passed in coursename (go backward to avoid skips)
        for(int i = this.allInstances.size()-1; i >= 0; i--) {
            if(this.allInstances.elementAt(i).courseName.equals(courseName)) {
                this.allInstances.remove(i);
            }
        }
    }

    // this function might not work
    public void deleteAssignment(String assignmentName) {
        String courseName= "";
        for(int i = 0; i < this.allInstances.size(); i++) {
            if(this.allInstances.elementAt(i).name.equals(assignmentName)) {
                courseName = this.allInstances.elementAt(i).courseName;
                this.allInstances.remove(i);
                break; // remove one assignment at a time
            }
        }

        for(int i = 0; i < this.allCourses.size(); i++) {
            if(this.allCourses.elementAt(i).name.equals(courseName)) {
                for(int j = 0; j < this.allCourses.elementAt(i).classTimes.size(); j++) {
                    if(this.allCourses.elementAt(i).classTimes.elementAt(j).name.equals(assignmentName)) {
                        this.allCourses.elementAt(i).classTimes.remove(j);
                    }
                    break;
                }
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String saveCourses(){
        String text = "";
            int i;
            for(i = 0; i < this.allCourses.size(); i++) {
                text = text.concat("####-" + i + "-####\n");
                text = text.concat(allCourses.elementAt(i).returnCourseInfo(i));
                text = text.concat("END-OF-CLASS\n");
            }

        return ":NumCourses:" + i + "\n" + text;
    }


    public Vector<CourseInstance> getTodaysSchedule(String date) {
        // given a date in the format mm/dd/yyyy, return a vector of all CourseInstances occuring on that day
        Calendar calVal;
        String dateStr;
        Vector<CourseInstance> todaysSchedule = new Vector<CourseInstance>();
        for(int i = 0; i < this.allInstances.size(); i++) {
            calVal = this.allInstances.elementAt(i).startTime;
            dateStr = CourseInstance.calDateToString(calVal);
            if(dateStr.equals(date)) {
                todaysSchedule.add(this.allInstances.elementAt(i));
            }
        }
        return todaysSchedule;
    }

    public String stringTodaysSchedule(String date) {
        // print classes first, then assignments
        Vector<CourseInstance> instances = this.getTodaysSchedule(date);
        String schedule = "";
        CourseInstance currentInstance;
        for(int i = 0; i < instances.size(); i++) {
            currentInstance = instances.elementAt(i);
            schedule = schedule + currentInstance.getInfo();
            //schedule.concat(currentInstance.courseName + ":" + currentInstance.type +  )
        }
        return schedule;
    }

    public String[] returnAssignments() {
        Vector<String> totalInstances = new Vector<String>();
        String curType;
        for(int i = 0; i < this.allInstances.size(); i++) {
            curType = this.allInstances.elementAt(i).type;
            if(curType.equals("Assignment") || curType.equals("Exam") || curType.equals("Lab Report")) {
                totalInstances.add(this.allInstances.elementAt(i).name);
            }
        }
        String[] assignmentNames = totalInstances.toArray(new String[totalInstances.size()]);

        return assignmentNames;
    }

    public String[] getCourseNames() {
        Vector<String> courseNames = new Vector<String>();

        // get names of all courses
        for(int i = 0; i < this.allCourses.size(); i++) {
            if(!this.allCourses.elementAt(i).name.equals("Homework Times")) // dont want to show this class
                courseNames.add(this.allCourses.elementAt(i).name);
        }

        String[] stringNames = courseNames.toArray(new String[courseNames.size()]);
        return stringNames;
    }

    public  Vector<Double> getWeightedDistance(String date){
        Vector<Integer> distance = getDistance(date);
        Vector<Double> weightedDistance = new Vector<Double>();

        for(int i = 0; i < distance.size(); i++){
            weightedDistance.add(this.allCourses.elementAt(i).multiplier * distance.elementAt(i));
        }
        return weightedDistance;
    }

    //For every assignment in the next 7 days, add a "point" to the distance vector
    //Check how "far" assignments are. Essentially, giving priority by distance from due
    //date. 6 is highest priority, 1 lowest
    public Vector<Integer> getDistance(String date){
        Vector<CourseInstance> todayCInst = this.getTodaysSchedule(date);
        Calendar thisDate = CourseInstance.settingTime(date);

        Vector<Integer> distance = new Vector<Integer>(10);
        int tempDist = 0;

        for(int i = 0; i < this.allCourses.size()-1; i ++){
            for(int j = 1 ; j < 7; j++) {
                thisDate.add(Calendar.DATE, 1);
                todayCInst = this.getTodaysSchedule(CourseInstance.calDateToString(thisDate));

                for (int k = 0; k < todayCInst.size(); k++) {
                    if (this.allCourses.elementAt(i + 1).name == todayCInst.elementAt(k).courseName) {
                        tempDist = tempDist + (7-j);
                    }
                }
            }
            thisDate = CourseInstance.settingTime(date);
            distance.add(tempDist);
            tempDist = 0;
        }//end of outer for
        return distance;
    }//end of func


    public void splitHomework(String date)
    {
        CourseInstance initial = new CourseInstance();
        Vector<CourseInstance> result = new Vector<CourseInstance>();
        Vector<Double> distances = getWeightedDistance(date);
        Calendar lastEnd;
        Calendar startTime;
        Calendar endTime;
        String startStr;
        String endStr;
        long duration;   //Duration in minutes
        int newDuration;
        double totalWeight = 0;
        double weight = 0;
        Course currentCourse;

        for(int j = 1; j < this.allCourses.size() ; j ++)
        {
            totalWeight = totalWeight + distances.elementAt(j-1);
        }

        for(int i = 0; i < this.allCourses.elementAt(0).classTimes.size(); i++) {
            if (this.allCourses.elementAt(0).classTimes.elementAt(i).date.equals(date)){
                initial = this.allCourses.elementAt(0).classTimes.elementAt(i);
                startTime = initial.startTime;
                endTime = initial.endTime;
                lastEnd = startTime;
                duration = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / (60 * 1000);
                System.out.println(duration);
                for (int j = 1; j < this.allCourses.size(); j++) {
                    newDuration = (int) ((distances.get(j-1)/totalWeight)*duration);
                    System.out.println(newDuration);
                    startTime = lastEnd;
                    endTime = (Calendar) startTime.clone();
                    endTime.add(Calendar.MINUTE, newDuration);
                    startStr = startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE);
                    endStr = endTime.get(Calendar.HOUR) + ":" + endTime.get(Calendar.MINUTE);
                    lastEnd = endTime;
                    currentCourse = this.allCourses.elementAt(j);
                    // add new instance to HW course, name should be name of that course
                    result.add(new CourseInstance(currentCourse.name, CourseInstance.dayOfWeekString(endTime.get(Calendar.DAY_OF_WEEK)), date, startStr, endStr, CourseInstance.amOrpm(startTime) , CourseInstance.amOrpm(endTime), (currentCourse.name + "HWTime")));
                }
            }
        }
        // delete original instance and add new split bois
        this.allCourses.elementAt(0).classTimes.remove(0);
        for(int i = 0; i < result.size(); i++) {
            this.allCourses.elementAt(0).classTimes.add(result.elementAt(i));
        }
        //this.allInstances.size()
        //ALSO MUST ADD AND REMOVE FROM ALL INSTANCES FIELD
    }


}
