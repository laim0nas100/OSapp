/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import kernel.Defs.State;
import kernel.Kernel;
import kernel.process.ProcessAPI;
import kernel.memory.Paging;
import kernel.process.Proc;
import kernel.resource.Job;

/**
 *
 * @author lemmin
 * Programmable interrupt controller
 * Distributes "jobs" to specific handlers
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
    
    
    // TODO:
    public static boolean cleanupInvoked = false;
    
    public static void trap(int pid, int interrupt){
//        System.err.println("PIC "+interrupt);
        Job job = new Job(pid);
        
        
        Proc proc = Kernel.userProcList[pid];
        if(interrupt <= USER_ERROR){
            System.out.println("User error "+interrupt);
            trap(pid,HALT);
            return;
        }
        switch(interrupt){
            case PAGE_FAULT:
                System.out.println("Allocate more memory!");
                if(Paging.countFreeFrames()>0){
                    Paging.allocatePage(pid);
                    System.out.println("Here you go!");
                }else{
                    System.out.println("We are out of memory");
                    trap(pid,HALT);
                    cleanupInvoked = true;
//                    ProcessAPI.cleanupProcess(pid);
                    
                }
                break;
            case HALT:
                proc.state = State.ZOMBIE;
                
                break;
            case WRITE:
                
                System.out.println("Write");
                break;
            case READ:
                System.out.println("Read");
                break;
            case TIMER:
                Kernel.cpu.t.set(Defs.TIME_FRAME);
                System.out.println("Timer interrupt");
                //TODO: schedule
                break;
            default:
                System.out.println("Uknown interrupt");
        }
    }
}
