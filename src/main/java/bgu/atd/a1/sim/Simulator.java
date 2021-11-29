/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.Promise;
import bgu.atd.a1.sim.actions.*;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
    enum ActionEnum {
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

    public Simulator(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public static ActorThreadPool actorThreadPool;

    /**
     * Begin the simulation Should not be called before attachActorThreadPool()
     */
    public static void start() {
        actorThreadPool.start();
    }

    /**
     * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
     *
     * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
     */
    public static void attachActorThreadPool(ActorThreadPool myActorThreadPool) {
        Simulator.actorThreadPool = myActorThreadPool;
    }

    /**
     * shut down the simulation
     * returns list of private states
     */
    public static HashMap<String, PrivateState> end() throws InterruptedException {
        actorThreadPool.shutdown();
        return new HashMap<>(actorThreadPool.getActors());
    }

    private static void createAndSendActions(InputDetails.ActionsInput[] actionsInputs, CountDownLatch count) throws IllegalAccessException {
        HashMap<String, ActionEnum> actions = new HashMap<>();

        actions.put("Open Course", ActionEnum.OpenCourse);
        actions.put("Add Student", ActionEnum.AddStudent);
        actions.put("Participate In Course", ActionEnum.ParticipateInCourse);
        actions.put("Unregister", ActionEnum.Unregister);
        actions.put("Close Course", ActionEnum.CloseCourse);
        actions.put("Add Spaces", ActionEnum.AddSpaces);
        actions.put("Administrative Check", ActionEnum.AdministrativeCheck);
        actions.put("Register With Preferences", ActionEnum.RegisterWithPreferences);


        for (InputDetails.ActionsInput actionsInput : actionsInputs) {
            String action = actionsInput.action;
            ActionEnum actionId = actions.get(action);
            Promise<?> result;
            switch (actionId) {
                case OpenCourse -> {
                    OpenNewCourseAction openNewCourseAction = new OpenNewCourseAction(actionsInput.course, actionsInput.space, actionsInput.prerequisites);
                    actorThreadPool.submit(openNewCourseAction, actionsInput.department, new DepartmentPrivateState());
                    result = openNewCourseAction.getResult();
                }
                case AddStudent -> {
                    AddStudentAction addStudentAction = new AddStudentAction(actionsInput.studentId);
                    actorThreadPool.submit(addStudentAction, actionsInput.department, new DepartmentPrivateState());
                    result = addStudentAction.getResult();
                }
                case ParticipateInCourse -> {
                    int grade = actionsInput.grade == null || actionsInput.grade.length == 0 || actionsInput.grade[0].equals("-") ? -1 : Integer.parseInt(actionsInput.grade[0]);
                    ParticipatingInCourseAction participatingInCourseAction = new ParticipatingInCourseAction(actionsInput.studentId, grade);
                    actorThreadPool.submit(participatingInCourseAction, actionsInput.course, new CoursePrivateState());
                    result = participatingInCourseAction.getResult();
                }
                case Unregister -> {
                    UnregisterAction unregisterAction = new UnregisterAction(actionsInput.studentId);
                    actorThreadPool.submit(unregisterAction, actionsInput.course, new CoursePrivateState());
                    result = unregisterAction.getResult();
                }
                case CloseCourse -> {
                    CloseACourseAction closeACourseAction = new CloseACourseAction(actionsInput.course);
                    actorThreadPool.submit(closeACourseAction, actionsInput.department, new DepartmentPrivateState());
                    result = closeACourseAction.getResult();
                }
                case AddSpaces -> {
                    AddSpacesAction addSpacesAction = new AddSpacesAction(actionsInput.space);
                    actorThreadPool.submit(addSpacesAction, actionsInput.course, new CoursePrivateState());
                    result = addSpacesAction.getResult();
                }
                case AdministrativeCheck -> {
                    CheckAdministrativeObligationsAction checkAdministrativeObligationsAction = new CheckAdministrativeObligationsAction(actionsInput.studentsId, actionsInput.computerType, actionsInput.conditions);
                    actorThreadPool.submit(checkAdministrativeObligationsAction, actionsInput.department, new DepartmentPrivateState());
                    result = checkAdministrativeObligationsAction.getResult();
                }
                case RegisterWithPreferences -> {
                    Queue<Integer> grades = new LinkedList<>();
                    for (String i : actionsInput.grade) {
                        grades.add(Integer.valueOf(i));
                    }
                    RegisterWithPreferencesAction registerWithPreferencesAction = new RegisterWithPreferencesAction(actionsInput.preferences, grades);
                    actorThreadPool.submit(registerWithPreferencesAction, actionsInput.studentId, new StudentPrivateState());
                    result = registerWithPreferencesAction.getResult();
                }
                default -> throw new IllegalArgumentException("Not a valid Action Name from json");
            }
            result.subscribe(count::countDown);
        }
    }

    public static InputDetails parseJsonInputFile( String path, Gson gson) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(String.valueOf(path)));
            return  gson.fromJson(reader, InputDetails.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateJsonOutputFile(OutputDetails outputDetails,Map<String, PrivateState> stringPrivateStateMap, String path, Gson gson)
    {
        outputDetails.Courses = new LinkedList<>();
        outputDetails.Departments = new LinkedList<>();
        outputDetails.Students = new LinkedList<>();
        for (Map.Entry<String, PrivateState> entry : stringPrivateStateMap.entrySet()) {
            String actorId = entry.getKey();
            PrivateState ps = entry.getValue();
            if (ps instanceof DepartmentPrivateState) {
                OutputDetails.DepratmentOutput depratmentOutput = new OutputDetails.DepratmentOutput();
                depratmentOutput.actions = ps.getLogger().toArray(new String[0]);
                depratmentOutput.courseList = ((DepartmentPrivateState) ps).getCourseList().toArray(new String[0]);
                depratmentOutput.Department = actorId;
                depratmentOutput.studentList = ((DepartmentPrivateState) ps).getStudentList().toArray(new String[0]);
                outputDetails.Departments.add(depratmentOutput);
            } else if (ps instanceof CoursePrivateState) {
                OutputDetails.CourseOutput courseOutput = new OutputDetails.CourseOutput();
                courseOutput.actions = ps.getLogger().toArray(new String[0]);
                courseOutput.availableSpots = ((CoursePrivateState) ps).getAvailableSpots().toString();
                courseOutput.Course = actorId;
                courseOutput.prequisites = ((CoursePrivateState) ps).getPrequisites().toArray(new String[0]);
                courseOutput.registered = ((CoursePrivateState) ps).getRegistered().toString();
                courseOutput.regStudents = ((CoursePrivateState) ps).getRegStudents().toArray(new String[0]);
                outputDetails.Courses.add(courseOutput);
            } else if (ps instanceof StudentPrivateState) {
                OutputDetails.StudentOutput studentOutput = new OutputDetails.StudentOutput();
                studentOutput.actions = ps.getLogger().toArray(new String[0]);
                Map<String, Integer> grades = ((StudentPrivateState) ps).getGrades();
                List<String> gradesList = new LinkedList<>();
                for (Map.Entry<String, Integer> entry1 : grades.entrySet()) {
                    gradesList.add("(" + entry1.getKey() + ", " + entry1.getValue().toString() + ")");
                }
                studentOutput.grades = gradesList.toArray(new String[0]);
                studentOutput.signature = "" + ((StudentPrivateState) ps).getSignature();
                studentOutput.Student = actorId;
                outputDetails.Students.add(studentOutput);
            }
        }
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(outputDetails, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, IllegalAccessException {
        String path = args[0];
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        InputDetails inputDetails = parseJsonInputFile( path, gson);
        ActorThreadPool actorThreadPool = new ActorThreadPool(inputDetails.threads);
        attachActorThreadPool(actorThreadPool);
        start();
        List<Computer> computers = new LinkedList<>();
        for (InputDetails.ComputerInput computerInput : inputDetails.computersInput) {
            computers.add(computerInput.createComputer());
        }
        WarehouseAddComputers warehouseAddComputers = new WarehouseAddComputers(computers);
        actorThreadPool.submit(warehouseAddComputers, "Warehouse", new WarehousePrivateState());
        CountDownLatch phase1 = new CountDownLatch(inputDetails.phase1.length);
        createAndSendActions(inputDetails.phase1, phase1);
        phase1.await();
        CountDownLatch phase2 = new CountDownLatch(inputDetails.phase2.length);
        createAndSendActions(inputDetails.phase2, phase2);
        phase2.await();
        CountDownLatch phase3 = new CountDownLatch(inputDetails.phase3.length);
        createAndSendActions(inputDetails.phase3, phase3);
        phase3.await();
        Map<String, PrivateState> stringPrivateStateMap = end();

        FileOutputStream fout = new FileOutputStream("result.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(stringPrivateStateMap);

        // uncomment to generate json from the simulation into args[1]:
//        OutputDetails outputDetails = new OutputDetails();
//        String pathToOutput="./output.json";
//        generateJsonOutputFile(outputDetails,stringPrivateStateMap,pathToOutput,gson);

    }
}
