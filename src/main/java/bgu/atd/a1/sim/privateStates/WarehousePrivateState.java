package bgu.atd.a1.sim.privateStates;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.Computer;
import jdk.jshell.spi.ExecutionControl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class WarehousePrivateState extends PrivateState {

    List<Computer> computers;
    Map<String, Computer> typeComputerMap = new HashMap<>();
    Map<Computer, Semaphore> lockMap = new HashMap<>();

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
        for(Computer computer:computers){
            typeComputerMap.put(computer.getComputerType(),computer);
            lockMap.put(computer, new Semaphore(1));
        }
    }

    public Computer acquireComputer(String type){
        Computer computer = typeComputerMap.get(type);
        if(lockMap.get(computer).tryAcquire()){
            return computer;
        }
        return null;
    }

    public void releaseComputer(Computer computer){
        lockMap.get(computer).release();
    }
}
