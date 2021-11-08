package bgu.atd.a1.sim.input;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.openNewCourseAction;

import java.util.List;

public class OpenCourseActionInput extends DepartmentActionInput{
    public String Course;
    public int Space;
    public String[] Prerequisites;

    public openNewCourseAction getActionbyInput() {
        return new openNewCourseAction(Course, Department, Space, Prerequisites);
    }
}
