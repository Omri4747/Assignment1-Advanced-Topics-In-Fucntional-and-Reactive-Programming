package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.Warehouse;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

public class WarehouseReleaseAction extends Action<Boolean> {

    private Computer computer;

    public WarehouseReleaseAction(Computer computer){
        this.computer = computer;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof WarehousePrivateState)){
            throw new IllegalAccessException("Given non WarehousePrivateState to a warehouse actor");
        }
        ((WarehousePrivateState) ps).releaseComputer(computer);
        complete(true);
    }
}
