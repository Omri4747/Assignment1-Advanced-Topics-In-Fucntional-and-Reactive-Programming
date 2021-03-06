package bgu.atd.a1.sim.privateStates;

import java.util.HashMap;


import bgu.atd.a1.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private final HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		signature = 0;
		grades = new HashMap<>();
	}

	public HashMap<String, Integer> getGrades() {
		return grades;
	}

	public long getSignature() {
		return signature;
	}

	public void setSignature(long signature) {
		this.signature = signature;
	}
}
