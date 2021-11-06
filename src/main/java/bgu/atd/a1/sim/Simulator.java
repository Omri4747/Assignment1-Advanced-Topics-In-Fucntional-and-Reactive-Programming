/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import com.google.gson.Gson;

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


	public static void main(String[] args){
		//TODO: replace method body with real implementation
		if (args.length == 0){
			throw new IllegalArgumentException("Can't receive 0 agrguments. Should receive a path to input file");
		}
		Gson gson = new Gson();
		String path = args[0];
		JsonInput input = new JsonInput();
		System.out.println(input);
		try {
			Reader reader = Files.newBufferedReader(Paths.get(String.valueOf(path)));
			input = gson.fromJson(reader,JsonInput.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(input);
		System.exit(0);
	}

}
