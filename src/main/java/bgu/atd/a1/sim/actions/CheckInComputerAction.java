package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;

public class CheckInComputerAction extends Action<ResultDetails> {

    Computer computer;
    List<String> courses;

    public CheckInComputerAction(List<String> courses, Computer computer){
        this.computer = computer;
        this.courses = courses;
    }

    @Override
    protected void start() throws IllegalAccessException {
        long signature = computer.checkAndSign(courses,((StudentPrivateState) ps).getGrades());
        ((StudentPrivateState) ps).setSignature(signature);
        complete(new ResultDetails(true,"finished checking for student "+actorId+" and the result is "+signature+"."));
    }
}
