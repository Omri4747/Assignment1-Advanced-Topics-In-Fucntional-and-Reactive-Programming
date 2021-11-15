package bgu.atd.a1.sim.privateStates;

import java.util.LinkedList;
import java.util.List;


import bgu.atd.a1.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState {

    private Integer availableSpots;
    private Integer registered;
    private List<String> regStudents;
    private List<String> prequisites;

    /**
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     */
    public CoursePrivateState() {
        availableSpots = 0;
        registered = 0;
        regStudents = new LinkedList<>();
        prequisites = new LinkedList<>();
    }

    private void decreaseSpot() {
        availableSpots--;
    }

    private void increaseRegistered(){registered++;}

    //@pre: only if avaliableSpots > 0 and student is not registered yet
    /**
    @param student : student is not registered yet
     */
    public void registerStudent(String student){
        decreaseSpot();
        increaseRegistered();
        regStudents.add(student);
    }

    public boolean isRegistered(String student){
        return regStudents.contains(student);
    }

    public boolean hasSpot() {
        return availableSpots>0;
    }
    public void addSpots(int spots) {
        availableSpots += spots;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public Integer getRegistered() {
        return registered;
    }

    public List<String> getRegStudents() {
        return regStudents;
    }

    public List<String> getPrequisites() {
        return prequisites;
    }
}
