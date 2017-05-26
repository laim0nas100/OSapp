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
import kernel.Kernel;

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
        Kernel.cpu.it.set(0);//disable interrupts

        int timeTaken = 1;
        if(pendingJobs.isEmpty()){ //no jobs, block
            this.state = State.BLOCKED;
//            MainProcess.contextSwitch();
            timeTaken = 1;//takes 1 tick
            
        }else{
            commitNextJob();
            timeTaken = this.singleJobDuration;
        }
        Kernel.cpu.it.set(1);//enable interupts
        return timeTaken;
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
        System.out.println("Commit "+pid +": "+job.description);
        if(job != null){//should never be null
            job.run.run();
            job.isDone = true;
            
            ProcessAPI.setReady(job.invokedByPID);
            
            if(job.specialCase!=null){
                job.specialCase.run();
            }
        }
        
        if(pendingJobs.isEmpty()){
            this.state = State.BLOCKED;
        }
    }
    
    // system processes has no addressable memory so, no register switch is required
    public void bindCPU(){
        // no effect
    }
    public void unbindCPU(){
        // no effect
    }
        
}
