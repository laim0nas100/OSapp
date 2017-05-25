/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import java.util.ArrayList;
import kernel.CPU;
import kernel.Defs.State;
import kernel.Kernel;
import kernel.PIC;

/**
 *
 * @author lemmin
 */
public abstract class Proc {
    public static CPU cpu = Kernel.cpu;
    public int pid;
    public int priority;
    public State state = State.UNUSED;
    public ArrayList<Integer> usedFD = new ArrayList<>();
    public abstract int stepLogic();
    public final void step(){
        int timeTaken = stepLogic();
        int timeLeft = cpu.t.dec(timeTaken);
        if(timeLeft <= 0){// invoke timer interrupt
            if(cpu.it.val > 0){ // interrupts are not disabled
                PIC.trap(pid, PIC.TIMER);
            }
        }
    }
}
