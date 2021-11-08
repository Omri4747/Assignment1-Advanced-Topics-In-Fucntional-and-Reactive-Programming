package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class AddStudentAction extends Action<String> {
    private int studentId;

    public AddStudentAction(int studentId){
        this.studentId = studentId;
    }

    @Override
    protected void start() throws IllegalAccessException {

    }
}
