package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class UnregisterAction extends Action<ResultDetails> {

    private final String studentId;

    public UnregisterAction(String studentId){
        this.studentId = studentId;
        this.setActionName("Unregister");
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof CoursePrivateState))
            throw new IllegalAccessException("Given non CoursePrivateState to a Course Actor");
        if(!((CoursePrivateState) ps).getRegStudents().contains(studentId)){
            complete(new ResultDetails(false, "Student "+studentId+" isn't enrolled to course "+actorId));
            return;
        }
        UnregStudentFromCourseAction unregStudentFromCourseAction = new UnregStudentFromCourseAction(actorId);
        sendMessage(unregStudentFromCourseAction, studentId, new StudentPrivateState());
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(unregStudentFromCourseAction);
        then(actions,()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean succeeded = res.isSucceeded();
            if (!succeeded) {
                complete(res);
                return;
            }
            ((CoursePrivateState) ps).unregisterStudent(studentId);
            complete(new ResultDetails(true, "Unregistered "+ studentId+" from course "+actorId+"."));
        });
    }
}
