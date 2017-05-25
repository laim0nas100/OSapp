/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.memory;

import kernel.Kernel;
import static kernel.Defs.*;
import kernel.process.ProcessAPI;
import misc.Func;
import kernel.process.UserProc;
/**
 *
 * @author Lemmin
 */
public class Paging {
//    public static final int PTE_P =          0x001;   // Present
//    public static final int PTE_W =          0x002;   // Writeable
//    public static final int PTE_U =          0x004;   // User
//    public static final int PTE_PWT =        0x008;   // Write-Through
//    public static final int PTE_PCD =        0x010;   // Cache-Disable
//    public static final int PTE_A =          0x020;   // Accessed
//    public static final int PTE_D =          0x040;   // Dirty
//    public static final int PTE_PS =         0x080;   // Page Size
//    public static final int PTE_MBZ =        0x180;   // Bits must be zero
        
    // 1111 1111 1111 1111
    // dir
    // 1111 1111 1100 0000 0000 0000 0000 0000
    // table
    // 0000 0000 0011 1111 1111 0000 0000 0000
    // ppn
    // 1111 1111 1111 1111 1111 0000 0000 0000
    
//    private static final int DIR_MASK = 0xffa00000;
//    private static final int TABLE_MASK = 0x003ff000;
//    private static final int PPN_MASK = 0xfffff000;
    
    
    // virtual address to physical address with page table address
    public static int va2pa(int va, int ptr){
        MemFrame pagingTable = Kernel.ram[ptr];
        int frameIndex = va / PAGE_SIZE;
        int offset = va % PAGE_SIZE;
        int tableIndex = pagingTable.mem[frameIndex];
        int pa = offset + tableIndex * PAGE_SIZE;
        return pa;
    }
    
    public static int readMemory(int pa){
//        System.err.println("PA" + pa);
        int frameAddress = pa / PAGE_SIZE;
        MemFrame frame = Kernel.ram[frameAddress];
        int newpa = pa % PAGE_SIZE;
//        System.err.println("Addr "+frameAddress +" newPA "+newpa);
//        System.err.println(Arrays.toString(frame.mem));
        return frame.mem[newpa];
    }
    
    public static void setMemory(int pa, int val){
        int frameAddress = pa / PAGE_SIZE;
        MemFrame frame = Kernel.ram[frameAddress];
        int offset = pa % PAGE_SIZE;
        frame.mem[offset] = val;
    }
    
    public static int getBitValue(int val,int index){
        int bit = 1;
        for(int i = 0; i< index; i++){
            bit = bit << 1;
        }
        return val & bit;
    }
    
    public static int firstFreeFrameIndex(){
        for(int i = 0; i < Kernel.ram.length; i++){
            if(!Kernel.ram[i].used){
                return i;
            }
        }
        return -1;
    }  
    
    public static int countUsedPagesFromPageTable(MemFrame pageTable){
        int pagesUsed = 0;
        for(int i = 0; i < PAGE_SIZE; i++){
            int index = pageTable.mem[i];
            if(index > 0){ // page is indexed so it's in use
                pagesUsed++;
            }
        }
        return pagesUsed;
    }
    
    public static void markFreeFromPageTable(MemFrame pageTable){
        int usedPages = countUsedPagesFromPageTable(pageTable);
        for(int i = 0; i < usedPages; i++){
            int index = pageTable.mem[i];
            markFreeFrameIndex(index);
        }
    }
    
    public static void markFreeFrameIndex(int index){
        Kernel.ram[index].used = false;
        Func.fill(Kernel.ram[index].mem, -1);
    }
    
    public static void markUsedFrameIndex(int index){
        Kernel.ram[index].used = true;
        Func.fill(Kernel.ram[index].mem, 0);
    }
    
    public static int countFreeFrames(){
        int count = 0;
        for(int i = 0; i < Kernel.ram.length; i++){
            if(!Kernel.ram[i].used){
                count++;
            }
        }
        return count;
    }
    
    // asumes page is available
    public static void allocatePage(int pid){
        UserProc proc = ProcessAPI.userProc[pid];
        MemFrame table = Kernel.ram[proc.plr.get()];
        int tableSize = countUsedPagesFromPageTable(table);
        int freeFrameIndex = firstFreeFrameIndex();
        table.mem[tableSize] = freeFrameIndex;
        markUsedFrameIndex(freeFrameIndex);
    }

}
