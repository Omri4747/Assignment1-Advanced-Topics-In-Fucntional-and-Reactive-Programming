package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class CloseACourseAction extends Action<ResultDetails> {

    private String courseName;
    private String departmentName;

    public CloseACourseAction(String courseName, String departmentName){
        this.courseName = courseName;
        this.departmentName = departmentName;
        setActionName("Close Course");
    }
    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("Given non DepartmentPrivateState to a course actor");
        if(! ((DepartmentPrivateState) ps).getCourseList().contains(courseName)) {
            complete(new ResultDetails(false, "Course " + courseName + " is never opened in "+this.departmentName));
            return;
        }

        UnregisterCourseAction unregisterCourseAction = new UnregisterCourseAction();
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(unregisterCourseAction);
        then(actions,()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean succeeded = res.isSucceeded();
            if(succeeded){
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
