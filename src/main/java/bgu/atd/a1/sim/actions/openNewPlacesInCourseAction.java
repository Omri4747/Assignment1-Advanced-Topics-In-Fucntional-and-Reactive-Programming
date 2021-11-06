package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

public class openNewPlacesInCourseAction extends Action<Boolean> {

    private String courseName;
    private int space;

    public openNewPlacesInCourseAction(String courseName, int space) {
        this.courseName = courseName;
        this.space = space;
    }

    @Override
    protected void start() throws IllegalAccessException {
        CoursePrivateState ps = (CoursePrivateState) pool.getActors().get(this.courseName);
        ps.addSpots(space);
        complete(true);
    }
}
