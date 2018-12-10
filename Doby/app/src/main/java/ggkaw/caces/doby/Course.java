package ggkaw.caces.doby;

import java.io.Serializable;
import java.util.*;

import static java.lang.Math.abs;

// 5 instances at any given time
public class Course implements Serializable {
    String name;
    Vector<CourseInstance> classTimes; // row 1 holds times it meets, row 2 holds duration, row 3 holds lec, lab, HW
    double multiplier; // absolute conversion rate (GPA/time)
    String startDate;
    String endDate;
    String saveString = "";

    // eventually add grade breakdown??

    public Course(String name, Vector<CourseInstance> classTimes, double multiplier) {
        this.name = name;
        for (int i = 0; i < classTimes.size(); i++) {
            this.classTimes.add(classTimes.get(i));
        }
        this.multiplier = multiplier;
    }

    public Course() {}

    public Course(String name, double multiplier, String startDate, String endDate) {
        this.name = name;
        this.classTimes = new Vector<CourseInstance>();
        this.multiplier = multiplier;
        this.startDate = startDate;
        this.endDate = endDate;
        saveString = name + "$" + multiplier + "$" +  startDate + "$" + endDate;
    }


    void addInstance(CourseInstance newInstance) {
        // add a new instance to the classTimes
        classTimes.add(newInstance);
    }

    public static void addInstances(Course course, String dayOfWeek, String type, String startTime, String endTime, String startap, String endap) {
        // will add all instances of a repeating course meeting, eg, all monday lectures
        Vector<String> necessaryDates = findDates(course.startDate, course.endDate, dayOfWeek);
        for(int i = 0; i < necessaryDates.size(); i++) {
            course.addInstance(new CourseInstance(course.name, dayOfWeek, necessaryDates.elementAt(i), startTime, endTime, startap, endap, type));
        }

    }

    public boolean hasDuplicate(String startDate, String classDay, String classType, String startTime, String endTime, String APStart, String APEnd, String name) {
        // check if same instance already exists
        Calendar sTime = CourseInstance.settingTime(startDate, startTime, APStart);
        // definitely does not work yet!
        if(classType.equals("Lecture") || classType.equals("Lab") || classType.equals("Discussion")) {
            for(int i = 0; i < classTimes.size(); i++) {
                if(classDay.equals(classTimes.elementAt(i).day) && sTime.equals(classTimes.elementAt(i).startTime)) {
                    return true;
                }
            }
        }
        if(classType.equals("Assignment") || classType.equals("Lab Report") || classType.equals("Exam")) {
            for(int i = 0; i < classTimes.size(); i++) {
                if(name.equals(classTimes.elementAt(i).name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Vector<String> findDates(String startDate, String endDate, String dayOfWeek) {
        int dOW = CourseInstance.dayOfWeekInt(dayOfWeek);
        Vector<String> stringDates = new Vector<String>();
        // get start and end date in calendar form
        Calendar startDay = CourseInstance.settingTime(startDate);
        Calendar endDay = CourseInstance.settingTime(endDate);
        //System.out.println("Start day: " + startDay.getTime());
        int dayDiff = 0;
        if(dOW > startDay.DAY_OF_WEEK) {
            dayDiff = abs(dOW-startDay.DAY_OF_WEEK);
        } else if (dOW < startDay.DAY_OF_WEEK){
            dayDiff = 7-startDay.DAY_OF_WEEK + dOW;
        } // else just leave it alone
        startDay.add(Calendar.DATE, dayDiff);
        //System.out.println("First date w/ correct day of week: " + startDay.getTime());
        Date d = startDay.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        stringDates.add(CourseInstance.calDateToString(startDay));
        // at the end of this loop, startDay is the first xday you want
        // now add 7 to this date, stop when the date is "larger" than the last date
        int seeComp;
        while(startDay.compareTo(endDay) <= 0) {
            startDay.add(Calendar.DAY_OF_YEAR, 7);
            stringDates.add(CourseInstance.calDateToString(startDay));
            seeComp = startDay.compareTo(endDay);
            //System.out.println(startDay);
        }
        return stringDates;
    }


    CourseInstance removeInstance(int idx) {
        // remove class instance at index idx
        CourseInstance removedCourse = classTimes.get(idx);
        classTimes.remove(idx);
        return removedCourse;
    }


    void removeInstance(String type) {
        // remove all instances of type "type"
        CourseInstance lastRemoved;
        for (int i = 0 ; i < classTimes.size(); i++) {
            if (classTimes.get(i).type.equals(type)) {
                lastRemoved = removeInstance(i);
                lastRemoved.printCI();
            }
        }
    }


    void printCourseInfo() {
        System.out.println("Class name: " + name);
        System.out.println("Class Instances: ");
        // System.out.println(classTimes.length);
        for (int j = 0; j < classTimes.size(); j++) {
            classTimes.get(j).printCI();
        }
        System.out.println("Class multiplier: " + multiplier);
        System.out.println("Number of Instances: " + classTimes.size());
    }

    void printCourseInfo(int courseNum) {
        System.out.println("Class name: " + name);
        System.out.println("Class Instances: ");
        // System.out.println(classTimes.length);
        for (int j = 0; j < classTimes.size(); j++) {
            System.out.println("-------(" + courseNum + "," + j + ")--------");
            classTimes.get(j).printCI();
            System.out.println("++++++-(" + courseNum + "," + j + ")-+++++++");
        }
        System.out.println("&&&&&&&&&" + courseNum + "&&&&&&&&&&");
        System.out.println("Class multiplier: " + multiplier);
        System.out.println("Number of Instances: " + classTimes.size());
        System.out.println("%%%%%%%%%" + courseNum + "%%%%%%%%%%%");
    }

    String returnCourseInfo(int courseNum){
        String info = "";
        info = info.concat(":CourseConstructor:" + saveString + "\n");
        for (int j = 0; j < classTimes.size(); j++) {
            info = info.concat(classTimes.get(j).returnCI() + "\n");
        }
        return info;
    }


//    public static void main(String[] args) {
//        Course course1 = new Course("EK307", 0.75, "9/1/2018", "12/13/2018");
//        // String startDate, String endDate, String dayOfWeek, String type, String startTime, String endTime, String startap, String endap) {
//        addInstances(course1, "Monday", "Lecture", "10:10", "11:55", "AM", "AM");
//        course1.printCourseInfo();
//    }

}
