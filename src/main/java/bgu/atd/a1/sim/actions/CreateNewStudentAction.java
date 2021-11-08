package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class CreateNewStudentAction extends Action<ResultDetails> {

    private long signature;

    public CreateNewStudentAction(long signature){
        this.signature=signature;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof StudentPrivateState)){
            throw new IllegalAccessException("Given non StudentPrivateState to a student actor");
        }
        if (((StudentPrivateState) ps).getSignature()!=-1){
            complete(new ResultDetails(true,"Student "+ signature+" is already exists"));
            return;
        }
        ((StudentPrivateState) ps).setSignature(signature);
        complete(new ResultDetails(true,"Student "+signature+" added successfully"));
    }
}
