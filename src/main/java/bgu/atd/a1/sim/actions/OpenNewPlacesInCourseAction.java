package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

public class OpenNewPlacesInCourseAction extends Action<ResultDetails> {

    private String courseName;
    private int space;

    public OpenNewPlacesInCourseAction(String courseName, int space) {
        this.courseName = courseName;
        this.space = space;
        setActionName("Add Spaces");
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof CoursePrivateState))
            throw new IllegalAccessException("Given non CoursePrivateState to a course actor");
        ((CoursePrivateState) ps).addSpots(space);
        complete(new ResultDetails(true, "Added "+ space + " spots to course " + courseName));
    }
}
