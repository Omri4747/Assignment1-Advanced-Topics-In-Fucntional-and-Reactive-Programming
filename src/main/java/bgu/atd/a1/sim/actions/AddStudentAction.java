package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class AddStudentAction extends Action<ResultDetails> {

    public String studentId;

    public AddStudentAction(String studentId){
        this.studentId = studentId;
        setActionName("Add Student");
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("");
        List<String> studentList = ((DepartmentPrivateState) ps).getStudentList();
        if(studentList.contains(studentId)){
            complete(new ResultDetails(false,"Student "+studentId +" is already enrolled."));
            return;
        }
        if(pool.getActors().get(studentId) != null){
            studentList.add(studentId);
            complete(new ResultDetails(true,"Student "+studentId +" is exists."));
            return;
        }
        CreateNewStudentAction createNewStudentAction = new CreateNewStudentAction();
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(createNewStudentAction);
        then(actions,()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean success = res.isSucceeded();
            if(success){
                studentList.add(studentId);
                complete(new ResultDetails(true, studentId +" added successfully."));
            }
            else{
                complete(res);
            }
        });
        sendMessage(createNewStudentAction, studentId, new StudentPrivateState());
    }
}
