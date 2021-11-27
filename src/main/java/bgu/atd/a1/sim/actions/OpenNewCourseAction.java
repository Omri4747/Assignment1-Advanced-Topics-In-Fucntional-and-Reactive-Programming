package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OpenNewCourseAction extends Action<ResultDetails> {

    private final String courseName;
    private final int space;
    private final List<String> prerequisites;

    public OpenNewCourseAction(String courseName, int space, List<String> prerequisites) {
        this.courseName = courseName;
        this.space = space;
        this.prerequisites = prerequisites;
        setActionName("Open Course");
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("Given non DepartmentPrivateState to a Department Actor");
        if(((DepartmentPrivateState) ps).getCourseList().contains(courseName)){
            complete(new ResultDetails(false, "Course "+ courseName +" is already opened."));
            return;
        }
        InitiateNewCourseAction initiateNewCourseAction = new InitiateNewCourseAction(courseName, space, prerequisites);
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(initiateNewCourseAction);
        then(actions, ()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean succeeded = res.isSucceeded();
            if(succeeded){
                ((DepartmentPrivateState) ps).addCourse(courseName);
                complete(new ResultDetails(true, "Course "+courseName+" opened with "+space+" spots."));
            }
            else{
                complete(res);
            }
        });
        sendMessage(initiateNewCourseAction, courseName, new CoursePrivateState());
    }
}
