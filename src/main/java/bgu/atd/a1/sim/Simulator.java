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
import java.util.*;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.*;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import com.google.gson.Gson;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	enum ActionEnum{
		OpenCourse,
		AddStudent,
		ParticipateInCourse,
		Unregister,
		CloseCourse,
		AddSpaces,
		AdministrativeCheck,
		RegisterWithPreferences
	}
	String jsonPath;

	public Simulator(String jsonPath){
		this.jsonPath = jsonPath;
	}
	
	public static ActorThreadPool actorThreadPool;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		actorThreadPool.start();
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		Simulator.actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static Map<String,PrivateState> end() throws InterruptedException {
		actorThreadPool.shutdown();
		return actorThreadPool.getActors();
	}

	private static void createAndSendActions(InputDetails.ActionsInput[] actionsInputs){
		HashMap<String, ActionEnum> actions = new HashMap<>();

		actions.put("Open Course", ActionEnum.OpenCourse);
		actions.put("Add Student", ActionEnum.AddStudent);
		actions.put("Participate In Course", ActionEnum.ParticipateInCourse);
		actions.put("Unregister", ActionEnum.Unregister);
		actions.put("Close Course", ActionEnum.CloseCourse);
		actions.put("Add Spaces",ActionEnum.AddSpaces);
		actions.put("Administrative Check", ActionEnum.AdministrativeCheck);
		actions.put("Register With Preferences",ActionEnum.RegisterWithPreferences);


		for(InputDetails.ActionsInput actionsInput : actionsInputs){
			String action = actionsInput.action;
			ActionEnum actionId = actions.get(action);
			switch (actionId) {
				case OpenCourse -> {
					OpenNewCourseAction openNewCourseAction = new OpenNewCourseAction(actionsInput.course, actionsInput.space, actionsInput.prerequisites);
					actorThreadPool.submit(openNewCourseAction, actionsInput.department, new DepartmentPrivateState());
				}
				case AddStudent -> {
					AddStudentAction addStudentAction = new AddStudentAction(actionsInput.studentId);
					actorThreadPool.submit(addStudentAction, actionsInput.department, new DepartmentPrivateState());
				}
				case ParticipateInCourse -> {
					int grade = actionsInput.grade == null || actionsInput.grade.length == 0 ? -1 : actionsInput.grade[0];
					ParticipatingInCourseAction participatingInCourseAction = new ParticipatingInCourseAction(actionsInput.studentId, grade);
					actorThreadPool.submit(participatingInCourseAction, actionsInput.course, new CoursePrivateState());
				}
				case Unregister -> {
					UnregisterAction unregisterAction = new UnregisterAction(actionsInput.studentId);
					actorThreadPool.submit(unregisterAction, actionsInput.course, new CoursePrivateState());
				}
				case CloseCourse -> {
					CloseACourseAction closeACourseAction = new CloseACourseAction(actionsInput.course);
					actorThreadPool.submit(closeACourseAction, actionsInput.department, new DepartmentPrivateState());
				}
				case AddSpaces -> {
					AddSpacesAction addSpacesAction = new AddSpacesAction(actionsInput.space);
					actorThreadPool.submit(addSpacesAction, actionsInput.course, new CoursePrivateState());
				}
				case AdministrativeCheck -> {
					CheckAdministrativeObligationsAction checkAdministrativeObligationsAction = new CheckAdministrativeObligationsAction(actionsInput.studentsId, actionsInput.computerType, actionsInput.conditions);
					actorThreadPool.submit(checkAdministrativeObligationsAction, actionsInput.department, new DepartmentPrivateState());
				}
				case RegisterWithPreferences -> {
					Queue<Integer> grades = new LinkedList<>();
					for (int i : actionsInput.grade) {
						grades.add(i);
					}
					RegisterWithPreferencesAction registerWithPreferencesAction = new RegisterWithPreferencesAction(actionsInput.preferences, grades);
					actorThreadPool.submit(registerWithPreferencesAction, actionsInput.studentId, new StudentPrivateState());
				}
				default -> throw new IllegalArgumentException("Not a valid Action Name from json");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		InputDetails inputDetails = new InputDetails();
		String path = args[0];
		Gson gson = new Gson();
		try {
			Reader reader = Files.newBufferedReader(Paths.get(String.valueOf(path)));
			inputDetails = gson.fromJson(reader,InputDetails.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(inputDetails);
		ActorThreadPool actorThreadPool = new ActorThreadPool(inputDetails.threads);
		attachActorThreadPool(actorThreadPool);
		start();
		List<Computer> computers = new LinkedList<>();
		for(InputDetails.ComputerInput computerInput: inputDetails.computersInput){
			computers.add(computerInput.createComputer());
		}
		createAndSendActions(inputDetails.phase1);
		Thread.sleep(50);
		createAndSendActions(inputDetails.phase2);
		Thread.sleep(50);
		createAndSendActions(inputDetails.phase3);
		Thread.sleep(1000);
		Map<String, PrivateState> stringPrivateStateMap = end();
		System.out.println("hi");
	}
}
