package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class ParticipatingInCourseAction extends Action<ResultDetails> {

    private long studentSignature;
    private int grade;

    public ParticipatingInCourseAction(long studentSignature, int grade) {
        this.studentSignature = studentSignature;
        this.grade = grade;
        this.setActionName("Participate In Course");
    }

    @Override
    protected void start() throws IllegalAccessException {
        String studentId = "" + studentSignature;
        if (!(ps instanceof CoursePrivateState)) {
            throw new IllegalAccessException("Given non CoursePrivateState to a Course Actor.");
        }

        ValidateCoursePrequisitesAction validateCoursePrequisitesAction = new ValidateCoursePrequisitesAction(((CoursePrivateState) ps).getPrequisites());
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(validateCoursePrequisitesAction);
        then(actions, () -> {
            ResultDetails res = actions.get(0).getResult().get();
            boolean success = res.isSucceeded();
            if (success) {  //Student has all the prequisites
                AddCourseToStudentGradeSheetAction addCourseToStudentGradeSheetAction = new AddCourseToStudentGradeSheetAction(actorId, grade);
                List<Action<ResultDetails>> secondActions = new LinkedList<>();
                secondActions.add(addCourseToStudentGradeSheetAction);
                then(secondActions, () -> {
                    ResultDetails secondRes = secondActions.get(0).getResult().get();
                    boolean secondSuccess = secondRes.isSucceeded();
                    if (secondSuccess) {    //Student successfully added the course to it's grades list
                        System.out.println("debug with "+studentId);
                        System.out.println(actorId + " " +((CoursePrivateState) ps).hasSpot());
                        System.out.println("actorid is "+actorId + " " +((CoursePrivateState) ps).getAvailableSpots());
                        if (!((CoursePrivateState) ps).hasSpot()) {
                            complete(new ResultDetails(false, "No spots available in course."));
                            return;
                        } else if (((CoursePrivateState) ps).isRegistered(studentId)) {
                            complete(new ResultDetails(false, "Student " + studentId + " is already registered to course."));
                            return;
                        }
                        ((CoursePrivateState) ps).registerStudent(studentId);
                        complete(new ResultDetails(true, secondRes.getMessage()));
                    } else {
                        complete(secondRes);
                    }
                });
                sendMessage(addCourseToStudentGradeSheetAction, studentId, new StudentPrivateState());
            } else {
                complete(res);
            }
        });
        sendMessage(validateCoursePrequisitesAction, studentId, new StudentPrivateState());
    }
}
