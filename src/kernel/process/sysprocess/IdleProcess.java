/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;

import kernel.Defs.*;
import kernel.resource.Job;


public class IdleProcess extends SysProc {

    public IdleProcess(int pid) {
        super(pid);
    }

    @Override
    public void submitJob(Job job) {
        //should never happen
    }
    @Override
    public void commitNextJob() {
        // i have no jobs
    }
    
    @Override
    public int stepLogic() {
        this.state = State.READY;
        return this.singleJobDuration;
    }

    
}
