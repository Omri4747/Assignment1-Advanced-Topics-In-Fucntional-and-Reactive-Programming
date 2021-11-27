package bgu.atd.a1.sim;

import java.io.Serializable;
import java.util.List;

public class OutputDetails implements Serializable {
    public static class DepratmentOutput implements Serializable{
        public String Department;
        public String[] actions;
        public String[] courseList;
        public String[] studentList;
    }
    public static class CourseOutput implements Serializable{
        public String Course;
        public String[] actions;
        public String availableSpots;
        public String registered;
        public String[] regStudents;
        public String[] prequisites;
    }
    public static class StudentOutput implements Serializable{
        public String Student;
        public String[] actions;
        public String[] grades;
        public String signature;
    }
    List<DepratmentOutput> Departments;
    List<CourseOutput> Courses;
    List<StudentOutput> Students;

}
