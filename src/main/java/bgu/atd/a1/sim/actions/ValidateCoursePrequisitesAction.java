package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.List;

public class ValidateCoursePrequisitesAction extends Action<ResultDetails> {

    private List<String> prequisites;

    public ValidateCoursePrequisitesAction(List<String> prequisites) {
        this.prequisites = prequisites;
    }

    @Override
    protected void start() throws IllegalAccessException {
        HashMap<String, Integer> gradesMap = ((StudentPrivateState) ps).getGrades();
        for(String course : prequisites){
            if(gradesMap.get(course) == null){
                complete(new ResultDetails(false,"Student "+actorId+" hasn't done the course "+course+" yet."));
                return;
            }
        }
        complete(new ResultDetails(true, "Student "+actorId+" has done all the prequisites"));
    }
}
