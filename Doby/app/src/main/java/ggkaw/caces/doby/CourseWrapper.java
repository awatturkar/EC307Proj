package ggkaw.caces.doby;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by sadie.la on 12/4/2018.
 */
public class CourseWrapper implements Serializable {
    Vector<Course> allCourses; // holds all courses added
    Vector<CourseInstance> allInstances; // holds every calendar event

    CourseWrapper() {
        this.allCourses = new Vector<Course>();
        this.allInstances = new Vector<CourseInstance>();
    }

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
            for(int j = 0; j < this.allCourses.size(); j++) {
                if(this.allCourses.elementAt(j).name == newInstances.elementAt(i).name) {
                    this.allCourses.elementAt(j).addInstance(newInstances.elementAt(i));
                    break; // will not have the same name as multiple courses
                }
            }
            this.allInstances.add(newInstances.elementAt(i));
        }
    }

    public void printCourses() {
        for(int i = 0; i < this.allCourses.size(); i++) {
            allCourses.elementAt(i).printCourseInfo();
        }
    }

    public static void main(String[] args) {
        Course EK307 = new Course("EK307", 2, "9/1/2018", "12/17/2018");
        Course.addInstances(EK307,"Monday", "Lecture", "10:10", "11:55", "AM", "AM");
        // add a hw
        CourseInstance hw1EK307 = new CourseInstance("EK307", "Wednesday", "12/5/2018", "Homework");
        EK307.addInstance(hw1EK307);

        Course EC327 = new Course("EC327", 4, "9/1/2018", "12/17/2018");
        Course.addInstances(EC327, "Monday", "Lecture", "2:30", "4:15", "PM", "PM");
        CourseInstance hw1EC327 = new CourseInstance("EC327", "Monday", "12/10/2018", "Homework");
        EC327.addInstance(hw1EC327);

        CourseWrapper cwrap = new CourseWrapper();
        cwrap.addCourse(EK307);
        cwrap.addCourse(EC327);

        // Try printing
        cwrap.printCourses();


    }
}

