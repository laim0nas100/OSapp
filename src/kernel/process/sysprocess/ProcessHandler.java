/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;


public class ProcessHandler extends SysProc {
    public ProcessHandler(int pid, int jobDuration) {
        super(pid);
        this.singleJobDuration = jobDuration;
    }
    
    
    
}
