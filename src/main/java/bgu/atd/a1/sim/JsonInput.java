package bgu.atd.a1.sim;

import bgu.atd.a1.sim.input.ActionInput;

public class JsonInput {
    public int threads;
    public Computer[] Computers;
    public ActionInput[] Phase1;
    public ActionInput[] Phase2;
    public ActionInput[] Phase3;

    public JsonInput(){ }

    public int getThreads() {
        return threads;
    }

    public Computer[] getComputers() {
        return Computers;
    }

    public ActionInput[] getPhase1() {
        return Phase1;
    }

    public ActionInput[] getPhase2() {
        return Phase2;
    }

    public ActionInput[] getPhase3() {
        return Phase3;
    }
}

