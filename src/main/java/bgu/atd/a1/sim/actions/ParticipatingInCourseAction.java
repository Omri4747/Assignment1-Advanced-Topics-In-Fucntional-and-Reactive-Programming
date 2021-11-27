package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class ParticipatingInCourseAction extends Action<ResultDetails> {

    private String studentId;
    private int grade;

    public ParticipatingInCourseAction(String studentId, int grade) {
        this.studentId = studentId;
        this.grade = grade;
        this.setActionName("Participate In Course");
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(ps instanceof CoursePrivateState)) {
            throw new IllegalAccessException("Given non CoursePrivateState to a Course Actor.");
        }

        ValidateCoursePrequisitesAction validateCoursePrequisitesAction = new ValidateCoursePrequisitesAction(((CoursePrivateState) ps).getPrequisites());
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(validateCoursePrequisitesAction);
        then(actions, () -> {
            ResultDetails res = actions.get(0).getResult().get();
            boolean success = res.isSucceeded();
            if (success) {  //Student has all the prerequisites
                if (!((CoursePrivateState) ps).hasSpot()) {
                    System.out.println("No spots available in "+actorId+".");
                    complete(new ResultDetails(false, "No spots available in course."));
                    return;
                } else if (((CoursePrivateState) ps).isRegistered(studentId)) {
                    System.out.println("Student " + studentId + " is already registered to course.");
                    complete(new ResultDetails(false, "Student " + studentId + " is already registered to course."));
                    return;
                }
                ((CoursePrivateState) ps).registerStudent(studentId);
                AddCourseToStudentGradeSheetAction addCourseToStudentGradeSheetAction = new AddCourseToStudentGradeSheetAction(actorId, grade);
                List<Action<ResultDetails>> secondActions = new LinkedList<>();
                secondActions.add(addCourseToStudentGradeSheetAction);
                then(secondActions, () -> {
                    complete(secondActions.get(0).getResult().get());
                });
                sendMessage(addCourseToStudentGradeSheetAction, studentId, new StudentPrivateState());
            } else {
                System.out.println("failed to register in course "+actorId);
                complete(res);
            }
        });
        sendMessage(validateCoursePrequisitesAction, studentId, new StudentPrivateState());
    }
}
