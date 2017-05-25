/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.resource;

/**
 *
 * @author lemmin
 */
public class Job{
    public boolean isDone = false;
    public int invokedByPID;
    public Runnable run;
    public Job(int pid){
        this.invokedByPID = pid;
    }
    
}
