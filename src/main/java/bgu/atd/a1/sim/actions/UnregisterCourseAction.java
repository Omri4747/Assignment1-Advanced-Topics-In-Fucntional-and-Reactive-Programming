package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class UnregisterCourseAction extends Action<ResultDetails> {

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof CoursePrivateState))
            throw new IllegalAccessException("Given non CoursePrivateState to a Course Actor");
        if(((CoursePrivateState) ps).getAvailableSpots() == -1){
            complete(new ResultDetails(false,"Course "+actorId+" is already closed."));
        }
        ((CoursePrivateState) ps).closeCourseForReg(); // set to -1
        List<Action<ResultDetails>> actions = new LinkedList<>();
        for(String student : ((CoursePrivateState) ps).getRegStudents()){
            UnregStudentFromCourseAction unregStudentFromCourseAction = new UnregStudentFromCourseAction(actorId);
            sendMessage(unregStudentFromCourseAction,student, new StudentPrivateState());
            actions.add(unregStudentFromCourseAction);
        }
        then(actions,()->{
            for (Action<ResultDetails> action : actions) {
                ResultDetails res = action.getResult().get();
                boolean succeeded = res.isSucceeded();
                if (!succeeded) {
                    complete(res);
                    return;
                }
            }
            ((CoursePrivateState) ps).closeCourse();
            complete(new ResultDetails(true, "Course "+actorId+" is closed."));
        });
    }
}
