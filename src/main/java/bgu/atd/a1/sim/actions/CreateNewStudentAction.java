package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class CreateNewStudentAction extends Action<ResultDetails> {

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof StudentPrivateState)){
            throw new IllegalAccessException("Given non StudentPrivateState to a student actor");
        }
        complete(new ResultDetails(true,"Student "+actorId+" added successfully"));
    }
}
