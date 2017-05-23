/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import java.util.ArrayList;
import kernel.CPU;

/**
 *
 * @author lemmin
 */
public abstract class Process {
    public enum State{UNUSED,READY,ACTIVE,BLOCKED,ZOMBIE};
    public CPU cpu;
    public int pid;
    public int priority;
    public State state;
    public ArrayList<Integer> usedFD;
    public abstract void step();
}
