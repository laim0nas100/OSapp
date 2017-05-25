/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import static kernel.Defs.*;
import kernel.process.ProcessAPI;
import kernel.memory.Paging;
import kernel.process.Proc;
import kernel.process.sysprocess.MainProcess;
import kernel.resource.Job;

/**
 *
 * @author lemmin
 * Programmable interrupt controller
 * Distributes "jobs" (neat way to package message) to specific handlers
 */
public class PIC {
    
    
    public static final int USER_ERROR      = 15;//all results in process termination
    public static final int INVALID_ADRESS  = USER_ERROR - 1;
    public static final int INVALID_OPCODE  = USER_ERROR - 2;
    
    
    public static final int TIMER           = 16;
    public static final int PAGE_FAULT      = 17;
    public static final int SYSCALL         = 32;
    public static final int HALT            = SYSCALL + 32;
    public static final int WRITE           = SYSCALL + 1;
    public static final int READ            = SYSCALL + 2;
    
    
    public static CPU cpu = Kernel.cpu;
    
    public static void trap(int pid, int interrupt){
        
        //Block current process
        Proc proc = ProcessAPI.allProc[pid];
        proc.state = State.BLOCKED;
        
        if(interrupt <= USER_ERROR){
            System.out.println("User error "+interrupt);
            trap(pid,HALT);
            return;
        }
        Job job = new Job(pid);
        switch(interrupt){
            case PAGE_FAULT:
                job.run = () -> {
                    System.out.println("Allocate more memory!");
                    if(Paging.countFreeFrames() > 0){
                        Paging.allocatePage(pid);
                        System.out.println("Here you go!");
                    }else{
                        System.out.println("We are out of memory");
                        trap(pid,PAGE_FAULT);//try again
                    }
                };
                job.description = "Memory allocation";
                ProcessAPI.memoryHandler.submitJob(job);
                
                
                break;
            case HALT:
                
                job.run = () -> {
                    proc.state = State.ZOMBIE;
                    Job subJob = new Job(PID_IDLE);
                    subJob.run = () -> {
                        ProcessAPI.cleanupProcess(pid);
                    };
                    subJob.description = "Process cleanup";
                    ProcessAPI.memoryHandler.submitJob(subJob);
                };
                job.description = "Process cleanup invoke";
                ProcessAPI.processHandler.submitJob(job);               
                break;
                
            case WRITE:
                int sp = Kernel.cpu.sp.dec();
                int pa = Paging.va2pa(sp, cpu.plr.get());
                int val = Paging.readMemory(pa);
//                System.out.println("Write");
                job.run = () -> {
                    System.out.println(val);
                };
                job.description = "Write";
                ProcessAPI.fileSystemHandler.submitJob(job);
                break;
                
            case READ:
                System.out.println("Read");
                job.run = () -> {
                    
                };
                job.description = "Read";
                ProcessAPI.fileSystemHandler.submitJob(job);
                break;
                
            case TIMER:
                // don't actually block, just do context switch
                proc.state = State.READY;
                Kernel.cpu.t.set(Defs.TIME_FRAME); // reset timer
                System.out.println("Timer interrupt");
                MainProcess.contextSwitch();
                break;
            default:
                System.out.println("Uknown interrupt");
        }
    }
}
