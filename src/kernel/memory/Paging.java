/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import kernel.Func;
import kernel.Kernel;
import kernel.Kernel.KernelExe;
import kernel.process.UserProcess;

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
    private static final int DIR_MASK = 0xffa00000;
    private static final int TABLE_MASK = 0x003ff000;
    private static final int PPN_MASK = 0xfffff000;
    
    
    public static final int PAGE_SIZE = 50;
    public static final int FIRST_USER_ADDRESS = PAGE_SIZE * Kernel.USER_PROC_LIMIT;
    
    public static int va2pa(int va, int ptr){
        MemFrame frame = Kernel.usableSpace[ptr];
        int pa = frame.mem[va];
        return pa;
    }
    public static int readMemory(int pa){
//        System.err.println("PA" + pa);
        int frameAddress = pa / PAGE_SIZE;
        MemFrame frame = Kernel.usableSpace[frameAddress];
        int newpa = pa % PAGE_SIZE;
//        System.err.println("Addr "+frameAddress +" newPA "+newpa);
//        System.err.println(Arrays.toString(frame.mem));
        return frame.mem[newpa];
    }
    
    public static void setMemory(int pa, int val){
        int frameAddress = pa / PAGE_SIZE;
        MemFrame frame = Kernel.usableSpace[frameAddress];
        pa = pa % PAGE_SIZE;
        frame.mem[pa] = val;
    }
    //adress to page table
    public static int setupPageTable(int order){
        
        MemFrame frame = Kernel.usableSpace[order];
        for(int i = 0; i < PAGE_SIZE; i++){
            frame.mem[i] = FIRST_USER_ADDRESS + PAGE_SIZE * order + i;
        }
        return order * PAGE_SIZE;
    }
    
    
    public static int getBitValue(int val,int index){
        int bit = 1;
        for(int i =0; i< index; i++){
            bit = bit << 1;
        }
        return val & bit;
    }
}
