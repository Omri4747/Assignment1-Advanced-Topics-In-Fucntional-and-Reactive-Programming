package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class AddStudentAction extends Action<ResultDetails> {

    public long signature;

    public AddStudentAction(long signature){
        this.signature = signature;
    }

    @Override
    protected void start() throws IllegalAccessException {
        String studentName = "student id: " + signature;
        if(!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("");
        List<String> studentList = ((DepartmentPrivateState) ps).getStudentList();
        if(studentList.contains(studentName)){
            complete(new ResultDetails(false,"Student "+signature +" is already enrolled."));
            return;
        }
        if(pool.getActors().get(studentName) != null){
            studentList.add(studentName);
            complete(new ResultDetails(true,"Student "+signature +" is exists."));
            return;
        }
        CreateNewStudentAction createNewStudentAction = new CreateNewStudentAction(signature);
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(createNewStudentAction);
        then(actions,()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean success = res.isSucceeded();
            if(success){
                studentList.add(studentName);
                System.out.println("hi "+studentName);
                complete(new ResultDetails(true, studentName +"added successfully."));
            }
            else{
                complete(res);
            }
        });
        sendMessage(createNewStudentAction, studentName, new StudentPrivateState());
    }
}
