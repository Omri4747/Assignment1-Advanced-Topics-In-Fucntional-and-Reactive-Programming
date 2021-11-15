package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class UnregStudentFromCourseAction extends Action<ResultDetails> {
    private String courseName;
    public UnregStudentFromCourseAction(String courseName){
        this.courseName= courseName;
    }
    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof StudentPrivateState))
            throw new IllegalAccessException("Given non StudentPrivateState to a Student Actor");
        if (! ((StudentPrivateState) ps).getGrades().containsKey(courseName)) {
            complete(new ResultDetails(true,"Student "+actorId+" did not have course "+courseName+" in his grade sheet"));
            return;
        }
        ((StudentPrivateState) ps).getGrades().remove(courseName);
        complete(new ResultDetails(true,"Course "+courseName+" removed from student "+actorId+" successfully"));
    }
}
