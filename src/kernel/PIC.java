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
import kernel.process.UserProc;
import kernel.resource.Job;
import kernel.resource.JobCleanup;

/**
 *
 * @author lemmin
 * Programmable interrupt controller
 * Distributes "jobs" (neat way to package message) to specific  handlers (System processes)
 */
public class PIC {
    
    
    public static final int USER_ERROR      = 15;//all results in process termination
    public static final int INVALID_ADRESS  = USER_ERROR - 1;
    public static final int INVALID_OPCODE  = USER_ERROR - 2;
    public static final int UNKNOWN_ERROR   = USER_ERROR - 3;
    
    public static final int TIMER           = 16;
    public static final int PAGE_FAULT      = 17;
    public static final int SYSCALL         = 32;
    public static final int HALT            = SYSCALL + 32;
    public static final int WRITE           = SYSCALL + 1;
    public static final int READ            = SYSCALL + 2;
    public static final int FORK            = SYSCALL + 3;
    
    
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
                    System.out.println("Please more memory?");
                    if(Paging.countFreeFrames() > 0){
                        Paging.allocatePage(pid);
                        System.out.println("Here you go!");
                    }else{
                        System.out.println("We are out of memory");
                        job.specialCase = () -> {
                            ProcessAPI.memoryHandler.failedMemoryAllocations++;
                            if(ProcessAPI.memoryHandler.failedMemoryAllocations > ACTION_REPEAT_LIMIT){
                                ProcessAPI.memoryHandler.failedMemoryAllocations = 0;
                                System.out.println("Sorry, memory did not become available, die");
                                trap(pid,HALT);
                            }else{
                                trap(pid,PAGE_FAULT);
                            }
                        };  
                    }
                };
                job.description = "Memory allocation";
                ProcessAPI.memoryHandler.submitJob(job);
                
                
                break;
            case HALT:
                Job otherjob = new JobCleanup(pid);
                proc.state = State.ZOMBIE;
                otherjob.run = () -> {
                    ProcessAPI.cleanupProcess(pid);
                };
                otherjob.specialCase = () ->{
                    proc.state = State.UNUSED;
                };
                otherjob.description = "Process cleanup " +pid;
                ProcessAPI.memoryHandler.submitJob(otherjob);               
                break;
                
            case WRITE:
                
                job.run = () -> {
                    UserProc up = (UserProc) ProcessAPI.allProc[pid];
                    int sp = up.vm.sp.dec() + up.codeSpaceSize ;
                    int pa = Paging.va2pa(sp, up.plr);
//                    System.out.println("PA: "+pa);
                    int val = Paging.readMemory(pa);
                    System.out.println(pid+" STD OUT:"+val);
                };
                job.description = "Write";
                ProcessAPI.fileSystemHandler.submitJob(job);
                break;
                
            case READ:
                
                //NOT IMPLEMENTED YET
                System.out.println("Read");
                job.run = () -> {
                    
                };
                job.description = "Read";
                ProcessAPI.fileSystemHandler.submitJob(job);
                break;
                
            case FORK:
                //BETA
//                ProcessAPI.forkProcess(pid);
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
