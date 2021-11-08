package bgu.atd.a1.sim.input;

import bgu.atd.a1.Action;

public abstract class ActionInput {
    private String Action;
    public String getAction() {
        return Action;
    }
    public abstract bgu.atd.a1.Action getActionbyInput();
}
