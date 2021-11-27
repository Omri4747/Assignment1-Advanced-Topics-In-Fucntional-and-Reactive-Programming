package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class CheckAdministrativeObligationsAction extends Action<ResultDetails> {

    List<String> studentsId;
    String computerType;
    List<String> conditions;

    public CheckAdministrativeObligationsAction(List<String> studentsId, String computerType, List<String> conditions) {
        this.studentsId = studentsId;
        this.computerType = computerType;
        this.conditions = conditions;
    }


    @Override
    protected void start() throws IllegalAccessException {
        if (!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("Given non DepartmentPrivateState to a course actor");
        Computer computer = new Computer("a", 1, 0);
        List<Action<?>> actions = new LinkedList<>();
        for (String student : studentsId) {
            CheckInComputerAction checkInComputerAction = new CheckInComputerAction(conditions, computer);
            sendMessage(checkInComputerAction, student, new StudentPrivateState());
            actions.add(checkInComputerAction);
        }
        String courseName = "yuval";
        UnregisterCourseAction unregisterCourseAction = new UnregisterCourseAction();
        actions.add(unregisterCourseAction);
        then(actions, () -> {
            ResultDetails res = (ResultDetails) actions.get(0).getResult().get();
            boolean succeeded = res.isSucceeded();
            if (succeeded) {
                // TODO: check if to remove the course from the dept ps i.e to keep or remove next line
                ((DepartmentPrivateState) ps).getCourseList().remove(actorId);
                complete(new ResultDetails(true, "Course " + courseName + " closed"));
            } else {
                complete(res);
            }
        });
        sendMessage(unregisterCourseAction, courseName, new CoursePrivateState());
    }
}

