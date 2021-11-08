/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.OpenNewCourseAction;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	public static void main(String [] args) throws InterruptedException {
		List<String> preq = new LinkedList<>();
		preq.add("yuval ve omri");
		OpenNewCourseAction openNewCourseAction = new OpenNewCourseAction("SPL", "CS", 100, preq);
		ActorThreadPool pool = new ActorThreadPool(10);
		pool.submit(openNewCourseAction,"CS", new DepartmentPrivateState());
		pool.start();
		Thread.sleep(1000);
		pool.shutdown();
		System.out.println("omri forgot about sout");
	}
}
