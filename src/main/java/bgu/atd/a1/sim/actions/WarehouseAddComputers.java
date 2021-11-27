package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.ResultDetails;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

import java.util.List;

public class WarehouseAddComputers extends Action<ResultDetails> {

    List<Computer> computers;

    public WarehouseAddComputers(List<Computer> computers){
        this.computers = computers;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(ps instanceof WarehousePrivateState)){
            throw new IllegalAccessException("Given non WarehousePrivateState to a warehouse actor");
        }
        ((WarehousePrivateState) ps).setComputers(computers);
    }
}
