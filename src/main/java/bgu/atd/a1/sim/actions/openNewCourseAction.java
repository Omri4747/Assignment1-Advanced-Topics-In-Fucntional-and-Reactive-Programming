package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class openNewCourseAction extends Action<Boolean> {

    private String courseName;
    private String departmentName;
    private int space;
    private String[] prerequisites;

    public openNewCourseAction(String courseName, String departmentName, int space, String[] prerequisites) {
        this.courseName = courseName;
        this.departmentName = departmentName;
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(pool.getActors().get(courseName) != null){
            complete(true);
        }
        DepartmentPrivateState ps = (DepartmentPrivateState) pool.getActors().get(this.departmentName);
        openNewPlacesInCourseAction op = new openNewPlacesInCourseAction(this.courseName, this.space);
        List<Action<Boolean>> actions = new ArrayList<>();
        actions.add(op);
        ps.getCourseList().add(courseName);
        then(actions,()->{
            Boolean confirmResult = actions.get(0).getResult().get();
            if(confirmResult){
                complete(true);
            }
            else{
                complete(false);
            }
        });
        sendMessage(op, this.courseName, new CoursePrivateState());
    }
}
