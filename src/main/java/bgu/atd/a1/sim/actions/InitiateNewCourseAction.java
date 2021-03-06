package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.List;

public class InitiateNewCourseAction extends Action<ResultDetails> {
    private String courseName;
    private int space;
    private List<String> prerequisites;

    public InitiateNewCourseAction(String courseName, int space, List<String> prerequisites) {
        this.courseName = courseName;
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof CoursePrivateState))
            throw new IllegalAccessException("Given non CoursePrivateState to a course actor");
        ((CoursePrivateState) ps).addSpots(space);
        ((CoursePrivateState) ps).getPrequisites().addAll(prerequisites);
        complete(new ResultDetails(true, "Added "+ space + " spots to course " + courseName));
    }
}
