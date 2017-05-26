/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;

import kernel.resource.Job;
import kernel.resource.JobCleanup;


public class MemoryHandler extends SysProc {
    public int failedMemoryAllocations = 0;
    public MemoryHandler(int pid, int jobDuration) {
        super(pid);
        this.singleJobDuration = jobDuration;
    }

    @Override
    public void submitJob(Job job) {
        if(job instanceof JobCleanup){
            for(Job j:this.pendingJobs){
                if(j instanceof JobCleanup){
                    if(((JobCleanup) job).invokedByPID == ((JobCleanup) j).invokedByPID){//
                        System.out.println("double cleanup job of "+((JobCleanup) j).invokedByPID+", abort"); //should never happen
                        return;
                    }
                }
            }
        }
        super.submitJob(job); //To change body of generated methods, choose Tools | Templates.
    }

    
}
