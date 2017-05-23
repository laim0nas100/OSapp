/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;

import java.util.ArrayDeque;
import kernel.Kernel;
import kernel.process.UserProcess;
import kernel.process.Process.State;

/**
 *
 * @author lemmin
 */
public class Scheduler {
        
    public static ArrayDeque<Integer[]> waitList = new ArrayDeque<>();
    public static UserProcess[] procList = Kernel.userProcList;
    public static boolean startNewProcess(Integer[] st){
        for(int i = 0; i<procList.length; i++){
            if(procList[i].state == State.UNUSED){//found empty process slot
                UserProcess proc = new UserProcess(st,i);
                Kernel.userProcList[i] = proc;
                proc.state = State.READY;
                return true;
            }
        }
        // no empty slot
        waitList.add(st);
        return false;
        
        
    }
    public static int currentPID = 0;
    public static void sched(){
        
        int i = 0;
        for (; i < procList.length; i++) {
            UserProcess pro = procList[i];
            //clean up dead
            if (pro.state == State.ZOMBIE) {
                pro.state = State.UNUSED;
            }
            
        }
        if(!waitList.isEmpty()){
            startNewProcess(waitList.pop());
        }
        for (; i < procList.length; i++) {
            UserProcess pro = procList[i];
            //clean up dead
            if (pro.state == State.ZOMBIE) {
                pro.state = State.UNUSED;
            }
            if (pro.state == State.READY) {
                pro.state = State.ACTIVE;
            }
            
        }
    }
    
}
