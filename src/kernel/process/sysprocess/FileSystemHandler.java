/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;


public class FileSystemHandler extends SysProc {
    
    public FileSystemHandler(int pid, int jobDuration) {
        super(pid);
        this.singleJobDuration = jobDuration;
    }
    
}
