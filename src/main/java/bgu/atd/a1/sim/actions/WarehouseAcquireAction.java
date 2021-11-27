package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

public class WarehouseAcquireAction extends Action<Computer> {

    private String computerType;

    public WarehouseAcquireAction(String type){
        computerType = type;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof WarehousePrivateState)){
            throw new IllegalAccessException("Given non WarehousePrivateState to a warehouse actor");
        }
        Computer computer = ((WarehousePrivateState) ps).acquireComputer(computerType);
        if(computer != null){
            complete(computer);
            return;
        }
        sendMessage(this, actorId, null);
    }
}
