/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.resource;

import static kernel.Defs.PID_IDLE;

/**
 *
 * @author lemmin
 */
public class Job{
    public boolean isDone = false;
    public int invokedByPID = PID_IDLE; //idle process
    public String description = "no desc"; 
    public Runnable run;
    public Runnable specialCase;
    public Job(int pid){
        this.invokedByPID = pid;
    }
    
}
