/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import misc.StandaloneVM;

/**
 *
 * @author Lemmin
 */
public class CPU {
    /*
    
    */
    
    // registers
    public Reg ip = new Reg(0);     // instruction pointer register
    public Reg sp = new Reg(0);    // stack pointer register
    public Reg it = new Reg(1);     //interrupt enable flag
    public Reg t  = new Reg(0);
    public Reg plr = new Reg(0);
}
