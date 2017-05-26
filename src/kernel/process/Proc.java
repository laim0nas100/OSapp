/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import kernel.Defs.State;
import kernel.Kernel;
import kernel.PIC;

/**
 *
 * @author lemmin
 */
public abstract class Proc {
    public int pid;
    public State state = State.UNUSED;
    public abstract int stepLogic();
    public Proc(int pid){
        this.pid = pid;
    }
    
    //should only do this if State is ACTIVE
    public final void step(){
        int timeTaken = stepLogic();
//        System.out.println("Time taken "+timeTaken);
        //timer is always running
        int timeLeft = Kernel.cpu.t.dec(timeTaken);
//        System.out.println("Time left "+timeLeft);
        if(timeLeft <= 0){// invoke timer interrupt
            if(Kernel.cpu.it.get() > 0){ // interrupts are not disabled
                PIC.trap(pid, PIC.TIMER);
            }
        }
    }
    
    public abstract void bindCPU();
    public abstract void unbindCPU();
}
