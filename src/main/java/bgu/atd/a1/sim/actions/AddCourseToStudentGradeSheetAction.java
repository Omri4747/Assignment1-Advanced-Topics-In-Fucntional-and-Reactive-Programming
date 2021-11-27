package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class AddCourseToStudentGradeSheetAction extends Action<ResultDetails> {
    private final String courseName;
    private final int grade;

    public AddCourseToStudentGradeSheetAction( String courseName,int grade) {
        this.courseName=courseName;
        this.grade=grade;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof StudentPrivateState))
            throw new IllegalAccessException("Given non StudentPrivateState to a Student Actor");
        ((StudentPrivateState) ps).getGrades().put(courseName, grade);
        complete(new ResultDetails(true, actorId + " has successfully registered course "+courseName+" with grade "+grade+" to it's grades list"));
    }
}
