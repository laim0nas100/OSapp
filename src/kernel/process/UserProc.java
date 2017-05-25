/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import java.util.logging.Level;
import java.util.logging.Logger;
import kernel.PIC;
import static kernel.Defs.*;
import kernel.Defs.State;
import kernel.Kernel;
import kernel.memory.MemFrame;
import kernel.memory.Paging;
import static kernel.PIC.*;
import kernel.Reg;
import kernel.stackvm.AbstractVM;

/**
 *
 * @author lemmin
 */
public class UserProc extends Proc {
    
    public AbstractVM vm;
    public Reg plr = new Reg(-1);
    public int codeSpaceSize = 0;
    public UserProc(int id){
        super(id);
        vm = new AbstractVM(){
            @Override
            public void INT(int interrupt){
                PIC.trap(pid, interrupt);
            }
            
            @Override
            public int codeAccess(int va){
                va = offsetRangeCheck(va);
                int pa = Paging.va2pa(va, plr.get());
                if(traceMemoryMapping)
                    System.err.println(va +" -> "+pa);
                return Paging.readMemory(pa);
            }
            
            @Override
            public int userMemoryAccess(int va){
                int offset = va + codeSpaceSize;
                offset = offsetRangeCheck(offset);
                int pa = Paging.va2pa(offset, plr.get());
                if(traceMemoryMapping)
                    System.err.println(va +" + "+ codeSpaceSize+" -> "+pa);

                return Paging.readMemory(pa);
            }
            
            @Override
            public void userMemorySet(int va, int val){
                int offset = va + codeSpaceSize;
                offset = offsetRangeCheck(offset);
                int pa = Paging.va2pa(offset, plr.get());
                Paging.setMemory(pa,val);
            }

            @Override
            public int codeSpaceSize() {
                return codeSpaceSize;
            }
        };
        
        
    }
    /**
        
        Set variables and set state to READY
    */
    public void initialize(Integer[] code, int pageIndex){
        this.plr.set(pageIndex);
        vm.sp.set(code[0]); //acount for global variables
        vm.ip.set(0);
        codeSpaceSize = code.length;
        state = State.READY;
        for(int i = 0;  i< codeSpaceSize; i++){
            int pa = Paging.va2pa(i, plr.get());
            Paging.setMemory(pa,code[i]);
        }  
    }
    
    @Override
    public int stepLogic(){
        try {
            vm.step();
        } catch (Kernel.KernelExe ex) {
            PIC.trap(pid, PIC.INVALID_OPCODE);
        }
        return 1; // always takes 1 tick
    }
    
    public int pagesInUse(){
        MemFrame table = Kernel.ram[this.plr.get()];
        return Paging.countUsedPagesFromPageTable(table);
    }
    public int offsetRangeCheck(int offset){
        if(offset < 0){
            PIC.trap(pid, PIC.INVALID_ADRESS);
            return 0;
        }
        if((double)(offset + PAGE_SIZE) / PAGE_SIZE >= pagesInUse()){
            System.out.println("Need more memory");
            PIC.trap(pid, PAGE_FAULT);

        }
        return offset;
    }

    @Override
    public void bindCPU() {
        Kernel.cpu.ip.bindBidirectional(vm.ip);
        Kernel.cpu.sp.bindBidirectional(vm.sp);
        Kernel.cpu.plr.bindBidirectional(this.plr);
    }

    @Override
    public void unbindCPU() {
        Kernel.cpu.ip.unbindBidirectional(vm.ip);
        Kernel.cpu.sp.unbindBidirectional(vm.sp);
        Kernel.cpu.plr.unbindBidirectional(this.plr);
    }
    
}
