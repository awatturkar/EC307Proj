package ggkaw.caces.doby;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CourseInstance implements Serializable {
    String courseName;
    String name;
    Calendar startTime;
    Calendar endTime;
    String type;
    String saveString;
    String day;

    // use for lectures, labs, discussions (type, day, start time, end time, start date, end date) REPEATERS
    public CourseInstance(String courseName, String day, String date, String startTime, String endTime, String startap, String endap, String type) {
        this.courseName = courseName;
        this.name = ""; // repeating instances do not need a name
        this.startTime = settingTime(date, startTime, startap);
        this.endTime = settingTime(date, endTime, endap);
        this.type = type;
        this.day = day;
        saveString = ":Instance:"+courseName+"$"+name+"$"+this.day+"$"+date+"$"+startTime+"$"+endTime+"$"+startap+"$"+endap+"$"+type;
    }

    //Added name parameter for uniform loading function
    public CourseInstance(String courseName, String name, String day, String date, String startTime, String endTime, String startap, String endap, String type) {
        this.courseName = courseName;
        this.name = name;
        this.startTime = settingTime(date, startTime, startap);
        this.endTime = settingTime(date, endTime, endap);
        this.type = type;
        this.day = day;
        saveString = ":Instance:"+courseName+"$"+name+"$"+this.day+"$"+date+"$"+startTime+"$"+endTime+"$"+startap+"$"+endap+"$"+type;
    }

    // use for assignments, tests (take date, name, type) SINGLE INSTANCES
    public CourseInstance(String courseName, String name, String date, String type) {
        this.courseName = courseName;
        this.name = name;
        this.startTime = settingTime(date);
        this.endTime = this.startTime;
        this.day = dayOfWeekString(this.startTime.get(this.startTime.DAY_OF_WEEK));
        this.type = type;
        saveString = ":Instance:"+courseName+"$"+name+"$"+day+"$"+date+"$"+"0:00"+"$"+"0:00"+"$"+"AM"+"$"+"AM"+"$"+type;
    }

    public static String dayOfWeekString(int i) {
        //1 = Sunday
        String day;
        switch (i) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            default: // day 7
                day = "Saturday";
                break;
        }
        return day;
    }

    public static int dayOfWeekInt(String s) {
        int i;
        switch(s) {
            case "Sunday":
                i = 1;
                break;
            case "Monday":
                i = 2;
                break;
            case "Tuesday":
                i = 3;
                break;
            case "Wednesday":
                i = 4;
                break;
            case "Thursday":
                i = 5;
                break;
            case "Friday":
                i = 6;
                break;
            default:
                i = 7;
                break;
        }
        return i;
    }

    public static Calendar settingTime(String date, String time, String ap) {
        String[] mdy = date.split("/");
        int month = Integer.parseInt(mdy[0]);
        int day = Integer.parseInt(mdy[1]);
        int year = Integer.parseInt(mdy[2]);
        String[] hm = time.split(":");
        int hour = Integer.parseInt(hm[0]);
        if(ap.equals("AM")) { // CONVERT TO READABLE BY CALENDAR
            if(hour == 12) {
                hour -= 12;
            }
        } else { //ap is "PM"
            if(hour != 12) {
                hour += 12;
            }
        }
        int minute = Integer.parseInt(hm[1]);
        Calendar c = Calendar.getInstance();
        c.set(year, month-1, day, hour, minute, 0);
        return c;
    }

    public static Calendar settingTime(String date) {
        String[] mdy = date.split("/");
        int month = Integer.parseInt(mdy[0]);
        int day = Integer.parseInt(mdy[1]);
        int year = Integer.parseInt(mdy[2]);
        Calendar c = Calendar.getInstance();
        c.set(year, month-1, day, 0, 0, 0);
        return c;
    }

    private int getDuration() { // in minutes
        long milLength = this.endTime.getTimeInMillis() - this.startTime.getTimeInMillis();
        int milLengthint = (int) milLength;
        int minLengthint = milLengthint/60000;
        return minLengthint;
    }

    public static int convertToHH(int hour) {
        if(hour == 0) {
            hour = 12;
        }
        if(hour > 12) {
            hour -= 12;
        }
        return hour;
    }

    public static String amOrpm(Calendar c) {
        if (c.get(c.AM_PM) == 0) {
            return "AM";
        } else {
            return "PM";
        }
    }

    public String getInfo() {
        // returns string info about a specific instance
        // Current types: Lecture, Lab, Discussion, Assignment, Exam, Lab Report
        // if one of the first three, don't need to print name, otherwise do
        String s;
        if(this.type.equals("Lecture") || this.type.equals("Lab") || this.type.equals("Discussion")) {
            int startTime = this.startTime.get(this.startTime.MINUTE);
            if(startTime < 10) {
                String firstDig = startTime+ "";
                String minVal = "0" + firstDig;
                s = this.courseName + " " + this.type + ": " + convertToHH(this.startTime.get(this.startTime.HOUR)) + ":" + minVal + amOrpm(this.startTime) + "\n";
            } else {
                s = this.courseName + " " + this.type + ": " + convertToHH(this.startTime.get(this.startTime.HOUR)) + ":" + this.startTime.get(this.startTime.MINUTE) + amOrpm(this.startTime) + "\n";
            }
        } else {
            s = this.courseName + " " + this.type + ": " + this.name+ "\n";
        }
        return s;
    }

    void printCI() {
        System.out.println("Course Name: " +this.courseName);
        System.out.println("Date/Time: " + this.startTime.getTime());
        System.out.println("Type: " + this.type);
        System.out.println("Name: " + this.name);
        int hours = this.getDuration()/60; // integer division
        int minutes = this.getDuration() - hours*60;
        System.out.println("Duration: " + hours + ":" + minutes);
    }

    String returnCI() {
        return saveString;
    }

    public static String calDateToString(Calendar c) {
        // takes a calendar date and convert it to a string
        int day = c.get(c.DAY_OF_MONTH);
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH)+1;
        String dateStr = month + "/" + day + "/" + year;
        return dateStr;
    }

//    public static void main(String[] args) {
//        CourseInstance repeating = new CourseInstance("Monday", "12/3/2018", "10:10", "11:55", "AM", "AM", "Lecture");
//        CourseInstance single = new CourseInstance("HW #9", "12/9/2018", "Assignment");
//
//        // Print info for lecture
//        System.out.println("Lecture info:");
//        repeating.printCI();
//        // Print info for assignment
//        System.out.println("\n\nAssignment Info:");
//        single.printCI();
//
//    }


}