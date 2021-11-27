package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

import java.util.LinkedList;
import java.util.List;

public class CheckAdministrativeObligationsAction extends Action<ResultDetails> {

    List<String> studentsId;
    String computerType;
    List<String> conditions;

    public CheckAdministrativeObligationsAction(List<String> studentsId, String computerType, List<String> conditions) {
        this.studentsId = studentsId;
        this.computerType = computerType;
        this.conditions = conditions;
        this.setActionName("Administrative Check");
    }


    @Override
    protected void start() throws IllegalAccessException {
        if (!(ps instanceof DepartmentPrivateState))
            throw new IllegalAccessException("Given non DepartmentPrivateState to a course actor");
        String warehouse = "Warehouse";
        WarehouseAcquireAction warehouseAcquireAction = new WarehouseAcquireAction(computerType);
        List<Action<Computer>> actions = new LinkedList<>();
        actions.add(warehouseAcquireAction);
        sendMessage(warehouseAcquireAction, warehouse, new WarehousePrivateState());
        then(actions,()->{
            Computer computer = actions.get(0).getResult().get();
            List<Action<?>> secondActions = new LinkedList<>();
            for (String student : studentsId) {
                CheckInComputerAction checkInComputerAction = new CheckInComputerAction(conditions, computer);
                sendMessage(checkInComputerAction, student, new StudentPrivateState());
                secondActions.add(checkInComputerAction);
            }
            then(secondActions,()->{
                WarehouseReleaseAction warehouseReleaseAction = new WarehouseReleaseAction(computer);
                sendMessage(warehouseReleaseAction, warehouse, new WarehousePrivateState());
                List<Action<?>> thirdActions = new LinkedList<>();
                thirdActions.add(warehouseReleaseAction);
                then(thirdActions,()->{
                    complete(new ResultDetails(true,"Finished checking all the students obligations."));
                });
            });
        });
    }
}

