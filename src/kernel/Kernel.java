/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import java.util.Arrays;
import kernel.memory.Paging;
import kernel.memory.MemFrame;
import kernel.process.UserProcess;
import static kernel.Defs.*;
/**
 *
 * @author Lemmin
 */
public class Kernel {
    public static UserProcess[] userProcList = new UserProcess[USER_PROC_LIMIT];
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

        for(int i = 0; i < userProcList.length; i++){
            userProcList[i] = new UserProcess(i);
        }
    }
    public static void dumpMemory(){
//        int i = 0;
        for(MemFrame frame:ram){
            System.out.println(Arrays.asList(frame.mem));
        }
    }
}
