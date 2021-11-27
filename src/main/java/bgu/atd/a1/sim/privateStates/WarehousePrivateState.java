package bgu.atd.a1.sim.privateStates;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.Computer;
import jdk.jshell.spi.ExecutionControl;

import java.util.LinkedList;
import java.util.List;

public class WarehousePrivateState extends PrivateState {

    Computer[] computers;
    Object[] locks;

    public WarehousePrivateState(){
         computers = new Computer[10];
         locks = new Object[10];
    }

    public Computer[] getComputers() {
        return computers;
    }

    public void setComputers(Computer[] computers) {
        this.computers = computers;
    }

    public Computer acquireComputer(String type){
        return computers[0];
    }

    public void releaseComputer(String type){

    }

}
