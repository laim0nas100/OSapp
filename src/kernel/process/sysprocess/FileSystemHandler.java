/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;

import kernel.Defs.*;


public class FileSystemHandler extends SysProc {
    
    public FileSystemHandler(int pid, int jobDuration) {
        super(pid);
        this.singleJobDuration = jobDuration;
    }
    
    @Override
    public int stepLogic() {
        if(pendingJobs.isEmpty()){ //no jobs, wait for jobs
            this.state = State.READY;
            return 1;//takes 1 tick
            
        }else{
            commitNextJob();
            return this.singleJobDuration;
        }
    }
    
    
}
