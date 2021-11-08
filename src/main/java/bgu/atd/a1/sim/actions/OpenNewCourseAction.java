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

    private String courseName;
    private String departmentName;
    private int space;
    private List<String> prerequisites;

    public OpenNewCourseAction(String courseName, String departmentName, int space, List<String> prerequisites) {
        this.courseName = courseName;
        this.departmentName = departmentName;
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(pool.getActors().get(courseName) != null){
            complete(new ResultDetails(false, "Course "+ courseName +" is already opened."));
            return;
        }
        if(!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("Given non DepartmentPrivateState to a Department Actor");
        InitiateNewCourseAction initiateNewCourseAction = new InitiateNewCourseAction(courseName, space, prerequisites);
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(initiateNewCourseAction);
        System.out.println("hi4");
        then(actions, ()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean succeeded = res.isSucceeded();
            if(succeeded){
                ((DepartmentPrivateState) ps).getCourseList().add(courseName);
                complete(new ResultDetails(true, "Course "+courseName+" opened with "+space+" spots."));
                System.out.println("hi2");
            }
            else{
                complete(res);
                System.out.println("hi3");
            }
        });
        sendMessage(initiateNewCourseAction, courseName, new CoursePrivateState());
    }
}
