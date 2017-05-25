/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import LibraryLB.Parsing.Lexer;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import kernel.Kernel;
import static kernel.Defs.*;
import kernel.memory.MemFrame;
import kernel.memory.Paging;
import kernel.process.sysprocess.SysProc;
import kernel.resource.Job;
import misc.MiniCompiler;

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

    public static UserProc[] userProc = new UserProc[USER_PROC_LIMIT];
    public static Proc[] allProc = new Proc[USER_PROC_LIMIT + SYSTEM_PROC_LIMIT];
    public static SysProc idleProcess;
    public static SysProc fileSystemHandler;
    public static SysProc memoryHandler;
    public static SysProc processHandler;

    private static int enqueuedProc = 0;

  
    
    public static int firstFreePID(){
        for(int i = 0; i < USER_PROC_LIMIT; i++){
            if(userProc[i].state == State.UNUSED){
                return i;
            }
        }
        return -1;
    }
    
    
    
    /**
     * does not account for page table
     */
    public static int requiredFrames(Integer[] code){
        int requiredMemory = requiredAllignWithPage(code.length + code[0]); //code size + global variables
        return requiredMemory / PAGE_SIZE;
    }
    
    /**
     * assume all resources is available
     */
    public static UserProc createProcess(Integer[] code){
        
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

        UserProc proc = userProc[firstFreePID()];
        proc.initialize(code,plr);
        return proc;
    }
    
    
    
    public static void cleanupProcess(int pid){
        UserProc proc = userProc[pid];
        int plr = proc.plr.get();
        MemFrame page = Kernel.ram[plr];
        Paging.markFreeFromPageTable(page);
        Paging.markFreeFrameIndex(plr);
        proc.state = State.UNUSED;
    }
    
    
    
    public static void setReady(int pid){
        allProc[pid].state = State.READY;
    }
    
    
    public static int requiredAllignWithPage(int required){
        int leftover = required % PAGE_SIZE;
        int pages = (required / PAGE_SIZE) + 1;
        if(leftover > 0){
            pages++;
        }
        return PAGE_SIZE * pages;
    }
    
    
    public static void addLoadProcessJob(String filePath){
        Job job = new Job(PID_IDLE);
        job.run = () -> {
            try {
                Integer[] code = MiniCompiler.compile(filePath);
                enqueueProcess(code);
            } catch (IOException e){
                System.out.println("File path exception");
            } catch (Lexer.NoSuchLexemeException | Lexer.StringNotTerminatedException ex) {
                System.out.println("Compiler exception, check code");
            }
        };
        job.description = "Enqueue process creation";
        ProcessAPI.fileSystemHandler.submitJob(job);
        
    }
    
    public static void enqueueProcess(Integer[] code){
        if(enqueuedProc < USER_PROC_LIMIT){
            enqueuedProc++;
            Job job = new Job(PID_IDLE);
            job.run = () ->{
                int requiredFrames = ProcessAPI.requiredFrames(code) + 1; // code size + paging table
                int freeFrames = Paging.countFreeFrames();
                if(freeFrames < requiredFrames){
                    enqueueProcess(code); // try again later

                }else{
                    ProcessAPI.createProcess(code);
                }
                enqueuedProc--;

            };
            job.description = "Create process";
            ProcessAPI.processHandler.submitJob(job);
        }
        
        
        
    }
}
