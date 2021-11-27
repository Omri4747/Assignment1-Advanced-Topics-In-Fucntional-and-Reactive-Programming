package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RegisterWithPreferencesAction extends Action <ResultDetails> {

    private Queue<String> coursesPref;
    private Queue <Integer> coursesGrades;


    public RegisterWithPreferencesAction(Queue<String> coursesPref,Queue<Integer> coursesGrades){
        assert (coursesPref.size() == coursesGrades.size());
        this.coursesPref=coursesPref;
        this.coursesGrades=coursesGrades;
        this.setActionName("Register With Preferences");
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof StudentPrivateState))
            throw new IllegalAccessException("Given non StudentPrivateState to a student actor");
        tryRegisterToNextPrefCourse();
    }

    // recursive function that tries to register to the next pref at each time
    public void tryRegisterToNextPrefCourse() throws IllegalAccessException {

        // this is what happened to Yuval when he tried to register to BGU courses but the system crashed
        if(coursesPref.isEmpty()) {
            complete(new ResultDetails(false, "Student " + actorId + " did not success to registered to any of its Preferences"));
            return;
        }

        String courseNameToTry = coursesPref.poll();
        Integer gradeOfCourse = coursesGrades.poll();
        assert (courseNameToTry!=null && gradeOfCourse!=null);

        ParticipatingInCourseAction picAction = new ParticipatingInCourseAction(actorId,gradeOfCourse);
        sendMessage(picAction,courseNameToTry,new CoursePrivateState());
        List<Action<ResultDetails>> actions = new LinkedList<>();
        actions.add(picAction);
        then(actions, ()->{
            ResultDetails res = actions.get(0).getResult().get();
            boolean succeeded = res.isSucceeded();
            if(succeeded){
                complete(new ResultDetails(true, "Student "+actorId+"is registered to Course "+courseNameToTry+" and with grade "+gradeOfCourse));
            }
            else{
                tryRegisterToNextPrefCourse();
            }
        });
    }
}
