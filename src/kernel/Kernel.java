/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import java.util.Arrays;
import kernel.memory.Paging;
import kernel.memory.MemFrame;
import kernel.process.Process.State;
import kernel.process.UserProcess;
import kernel.stackvm.VM;

/**
 *
 * @author Lemmin
 */
public class Kernel {
    public static final int USER_PROC_LIMIT = 10;
    public static UserProcess[] userProcList = new UserProcess[USER_PROC_LIMIT];
    public static MemFrame[] usableSpace = new MemFrame[100];
    public static CPU cpu = new CPU();
    public static class KernelExe extends Exception{
        public KernelExe(String s){
            super(s);
        }
    }
    public Paging paging = new Paging();
    public Kernel(){
        for(int i = 0; i < usableSpace.length; i++){
            usableSpace[i] = new MemFrame();
        }
        for(int i = 0; i < USER_PROC_LIMIT; i++){
            UserProcess p = new UserProcess(new Integer[]{},0,i);
            userProcList[i] = p;
            p.state = State.UNUSED;
        }
    }
    public static void dumpMemory(){
        int i = 0;
        for(MemFrame frame:usableSpace){
            System.out.println(Arrays.asList(frame.mem));
        }
    }
}
