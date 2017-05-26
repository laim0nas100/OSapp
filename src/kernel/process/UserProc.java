/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

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
    public int plr = -1;
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
                if(state != State.ACTIVE){
                    System.out.println("Not in active state "+pid);
                }
                va = offsetRangeCheck(va);
                if(va< 0 || va > codeSpaceSize){
                    PIC.trap(pid, PIC.INVALID_ADRESS);
                    return 0;
                }
                int pa = Paging.va2pa(va, plr);
                if(traceMemoryMapping)
                    System.err.println(va +" -> "+pa);
                return Paging.readMemory(pa);
            }
            
            @Override
            public int userMemoryAccess(int va){
                if(state != State.ACTIVE){
                    System.out.println("Not in active state "+pid);
                }
                int offset = va + codeSpaceSize;
                offset = offsetRangeCheck(offset);
                if(offset<0){
                    PIC.trap(pid, PIC.INVALID_ADRESS);
                    return 0;
                }
                int pa = Paging.va2pa(offset, plr);
                if(traceMemoryMapping)
                    System.err.println(va +" + "+ codeSpaceSize+" -> "+pa);

                return Paging.readMemory(pa);
            }
            
            @Override
            public void userMemorySet(int va, int val){
                
                int offset = va + codeSpaceSize;
                offset = offsetRangeCheck(offset);
                if(offset < 0){
                    PIC.trap(pid, PIC.INVALID_ADRESS);

                    return;
                }
                int pa = Paging.va2pa(offset, plr);
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
        this.plr = (pageIndex);
        vm.sp.set(code[0]); //acount for global variables
        vm.ip.set(0);
        codeSpaceSize = code.length;
        state = State.READY;
        for(int i = 0;  i< codeSpaceSize; i++){
            int pa = Paging.va2pa(i, plr);
            Paging.setMemory(pa,code[i]);
        }  
    }
    
    @Override
    public int stepLogic(){
        try {
            vm.step();
        } catch (Kernel.KernelExe ex) {
            PIC.trap(pid, PIC.INVALID_OPCODE);
        } catch(Exception e){
            //Something else went wrong
            e.printStackTrace();
            PIC.trap(pid, 3);
        }
        return 1; // always takes 1 tick
    }
    
    public int pagesInUse(){
        MemFrame table = Kernel.ram[this.plr];
        return Paging.countUsedPagesFromPageTable(table);
    }
    public int offsetRangeCheck(int offset){
        if(offset < 0){
            return -1;
        }
        if((double)(offset + PAGE_SIZE) / PAGE_SIZE >= pagesInUse()){
            System.out.println("Need more memory");
            PIC.trap(pid, PAGE_FAULT);
            
        }
        return offset;
    }

    @Override
    public void bindCPU() {
        
        vm.ip.bind(Kernel.cpu.ip);
        vm.sp.bind(Kernel.cpu.sp);
        Kernel.cpu.plr.set(this.plr);
    }

    @Override
    public void unbindCPU() {
        vm.ip.unbind();
        vm.sp.unbind();
//        Kernel.cpu.plr.unbind();
    }
    
}
