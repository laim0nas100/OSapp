/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import java.util.Arrays;
import kernel.memory.Paging;
import kernel.memory.MemFrame;
import kernel.process.UserProc;
import static kernel.Defs.*;
import kernel.process.ProcessAPI;
import kernel.process.sysprocess.FileSystemHandler;
import kernel.process.sysprocess.IdleProcess;
import kernel.process.sysprocess.MemoryHandler;
import kernel.process.sysprocess.ProcessHandler;
/**
 *
 * @author Lemmin
 */
public class Kernel {
    public static MemFrame[] ram = new MemFrame[USER_FRAMES];
    public static CPU cpu = new CPU();
    
    
    public static class KernelExe extends Exception{
        public KernelExe(String s){
            super(s);
        }
    }
    public Paging paging = new Paging();
    public static void init(){
        
        for(int i = 0; i < ram.length; i++){
            ram[i] = new MemFrame();
        }
        ProcessAPI.idleProcess = new IdleProcess(PID_IDLE);
        ProcessAPI.fileSystemHandler = new FileSystemHandler(PID_FS_HANDLER, 10);
        ProcessAPI.processHandler = new ProcessHandler(PID_PROC_HANDLER, 5);
        ProcessAPI.memoryHandler = new MemoryHandler(PID_MEMORY_HANDLER, 8);
        
        ProcessAPI.allProc[PID_IDLE] = ProcessAPI.idleProcess;
        ProcessAPI.allProc[PID_FS_HANDLER] = ProcessAPI.fileSystemHandler;
        ProcessAPI.allProc[PID_PROC_HANDLER] = ProcessAPI.processHandler;
        ProcessAPI.allProc[PID_MEMORY_HANDLER] = ProcessAPI.memoryHandler;
        
        for(int i = 0; i < USER_PROC_LIMIT; i++){
            UserProc p = new UserProc(i);
            ProcessAPI.allProc[i] = p;
        }
        
    }
    public static void dumpMemory(){
        for(MemFrame frame:ram){
            System.out.println(Arrays.asList(frame.mem));
        }
    }
   
    
}
