/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;

import java.util.ArrayDeque;
import kernel.Defs.*;
import kernel.process.Proc;
import kernel.process.ProcessAPI;
import kernel.resource.Job;

/**
 *
 * @author lemmin
 */
public abstract class SysProc extends Proc implements Handler{
    public ArrayDeque<Job> pendingJobs = new ArrayDeque<>();
    public int singleJobDuration = 1;
    public SysProc(int pid) {
        super(pid);
        this.state = State.READY;
    }

    @Override
    public int stepLogic() {
        int timeTaken = 1;
        if(pendingJobs.isEmpty()){ //no jobs, block
            this.state = State.BLOCKED;
//            MainProcess.contextSwitch();
            return 1;//takes 1 tick
            
        }else{
            commitNextJob();
            return this.singleJobDuration;
        }
    }
    

    @Override
    public void submitJob(Job job) {
        this.pendingJobs.add(job);
        this.state = State.READY;
        System.out.println("Submit "+pid +": "+job.description);
    }

    @Override
    public void commitNextJob() {
        Job job = pendingJobs.pollFirst();
        if(job != null){//should never be null
            job.run.run();
            job.isDone = true;
            ProcessAPI.setReady(job.invokedByPID);
        }
        System.out.println("Commit "+pid +": "+job.description);
    }
    
    // system processes has no addressable memory so, no register switch is required
    public void bindCPU(){
        // no effect
    }
    public void unbindCPU(){
        // no effect
    }
        
}
