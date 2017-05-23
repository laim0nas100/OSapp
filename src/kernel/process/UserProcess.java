/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import java.util.ArrayList;
import kernel.CPU;
import kernel.CPU.Reg;
import kernel.Kernel;
import kernel.memory.Paging;
import kernel.stackvm.AbstractVM;
import kernel.stackvm.VM;

/**
 *
 * @author lemmin
 */
public class UserProcess extends Process{
    
    public AbstractVM vm;
    public int plr;
    public int codeSpaceSize;
    public UserProcess(Integer[] code, int nglobals, int id){
        codeSpaceSize = code.length;
        pid = id;
        plr = pid;
        
        Paging.setupPageTable(pid);
        
        vm = new AbstractVM(){
            @Override
            public void INT(int interrupt){
                PIC.trap(Kernel.userProcList[pid], interrupt);
            }
            @Override
            public int codeAccess(int va){
                int pa = Paging.va2pa(va, plr);
                System.err.println(va +" -> "+pa);
                return Paging.readMemory(pa);
            }
            @Override
            public int userMemoryAccess(int va){
                int pa = Paging.va2pa(va + codeSpaceSize, plr);
                System.err.println(va +" + "+ codeSpaceSize+" -> "+pa);

                return Paging.readMemory(pa);
            }
            @Override
            public void userMemorySet(int va, int val){
                int pa = Paging.va2pa(va + codeSpaceSize, plr);
                Paging.setMemory(pa,val);
            }

            @Override
            public int codeSpaceSize() {
                return codeSpaceSize;
            }
        };
        
        vm.sp.inc(nglobals);
        for(int i=0; i<code.length; i++){
            int pa = Paging.va2pa(i, plr);
//            System.out.println("Resolved pa "+pa);
            Paging.setMemory(pa,code[i]);
        }
        
    }
    public void step(){
        if(state == State.ACTIVE && cpu!=null){
            vm.step();
        }
    }
}
