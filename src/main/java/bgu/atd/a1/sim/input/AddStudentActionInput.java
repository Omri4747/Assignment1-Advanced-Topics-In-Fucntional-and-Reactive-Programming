package bgu.atd.a1.sim.input;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.AddStudentAction;

public class AddStudentActionInput extends DepartmentActionInput{
    public int Student;
    @Override
    public AddStudentAction getActionbyInput() {
        return new AddStudentAction(Student);
    }
}
