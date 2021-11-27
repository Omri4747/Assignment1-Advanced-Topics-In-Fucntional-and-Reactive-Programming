package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class CloseACourseAction extends Action<ResultDetails> {

    private String courseName;

    public CloseACourseAction(String courseName){
        this.courseName = courseName;
        setActionName("Close Course");
    }
    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("Given non DepartmentPrivateState to a course actor");
        if(! ((DepartmentPrivateState) ps).getCourseList().contains(courseName)) {
            complete(new ResultDetails(false, "Course " + courseName + " is never opened in "+actorId));
            return;
        }

        UnregisterCourseAction unregisterCourseAction = new UnregisterCourseAction();
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(unregisterCourseAction);
        then(actions,()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean succeeded = res.isSucceeded();
            if(succeeded){
                // TODO: check if to remove the course from the dept ps i.e to keep or remove next line
                ((DepartmentPrivateState) ps).getCourseList().remove(actorId);
                complete(new ResultDetails(true, "Course "+courseName+" closed"));
            }
            else {
                complete(res);
            }
        });
        sendMessage(unregisterCourseAction, courseName, new CoursePrivateState());
    }
}
