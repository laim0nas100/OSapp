/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import kernel.process.Process.State;

/**
 *
 * @author lemmin
 * Programmable interrupt controller
 */
public class PIC {
    public static final int TIMER   = 5;
    public static final int SYSCALL = 32;
    public static final int HALT    = SYSCALL + 1;
    public static final int WRITE   = SYSCALL + 2;
    public static final int READ    = SYSCALL + 3;
    
    public static void trap(UserProcess proc, int interrupt){
//        System.err.println("PIC "+interrupt);
        switch(interrupt){
            case HALT:
                proc.state = State.ZOMBIE;
                break;
            case WRITE:
                
                System.out.println("Write");
                break;
            case READ:
                System.out.println("Read");
                break;
            case TIMER:
                //schedule
                break;
        }
    }
}
