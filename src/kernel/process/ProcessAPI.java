/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import java.util.ArrayDeque;
import kernel.Kernel;
import kernel.Kernel;
import kernel.process.UserProcess;
import misc.MiniCompiler;
import static kernel.Defs.*;
import kernel.memory.MemFrame;
import kernel.memory.Paging;

/**
 *
 * @author lemmin
 * 
 * update user processes, which got some resources (i.e. start new process or resume blocked)
 * iterate through all user processes via timer interrupt or sys call
 * gather some input(i.e. interrupts) data
 * execute all system processes
 * gather system resources
 * distribute gathered resources
 * repeat
 * 
 */
public class ProcessAPI {
        
    public static ArrayDeque<Integer[]> enqueuedProcessesToLaunch = new ArrayDeque<>();
    public static UserProcess[] procList = Kernel.userProcList;
    
    
    
    public static int currentPID = 0;

    

    
    
    
    public static int firstFreePID(){
        for(int i = 0; i < USER_PROC_LIMIT; i++){
            if(Kernel.userProcList[i].state == State.UNUSED){
                return i;
            }
        }
        return -1;
    }
    
    
//    public static UserProcess conditionalCreateProcess(Integer[] code){
//        
//    }
    
    
    // does not account for page table
    public static int requiredFrames(Integer[] code){
        int requiredMemory = requiredAllignWithPage(code.length) + code[0]; //code size + global variables
        return requiredMemory / PAGE_SIZE;
    }
    
    //need to check all resources before this
    public static UserProcess createProcess(Integer[] code) throws Exception{
        // asume all resources is available   
        
        int requiredFrames = requiredFrames(code);
        
        //allocate page for table
        int plr = Paging.firstFreeFrameIndex();//index of page table
        
        Paging.markUsedFrameIndex(plr);
        MemFrame pageTable = Kernel.ram[plr];
        //allocate pages
        for(int i = 0; i < requiredFrames; i++){
            int freePageIndex = Paging.firstFreeFrameIndex();
            pageTable.mem[i] = freePageIndex;
            Paging.markUsedFrameIndex(freePageIndex);
        }
        
        
        UserProcess proc = procList[firstFreePID()];
        proc.initialize(code,plr);
        // TODO:
        proc.state = State.ACTIVE;
        return proc;
    }
    
    public static void enqueueProcess(Integer[] code){
        enqueuedProcessesToLaunch.add(code);
    }
    
    public static void cleanupProcess(int pid){
        UserProcess proc = procList[pid];
        int plr = proc.plr;
        MemFrame page = Kernel.ram[plr];
        Paging.markFreeFromPageTable(page);
        Paging.markFreeFrameIndex(plr);
        proc.state = State.UNUSED;
    }
    
    
    
    public static void resumeProcess(UserProcess proc){
        proc.state = State.READY;
    }
    
    
    public static int requiredAllignWithPage(int required){
        int leftover = required % PAGE_SIZE;
        int pages = (required / PAGE_SIZE) + 1;
        if(leftover > 0){
            pages++;
        }
        return PAGE_SIZE * pages;
    }
}
