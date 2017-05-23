/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import kernel.stackvm.VM;

/**
 *
 * @author Lemmin
 */
public class CPU {
    public static class Reg{
        public int val;
        
        
        
        public int incBefore(int i){
            val += i;
            return val;
        }
        public int decBefore(int i){
            val -= i;
            return val;
        }
        public int inc(int i){
            int save = val;
            val += i;
            return save;
        }
        public int dec(int i){
            int save = val;
            val -= i;
            return save;
        }
        public int dec(){
            return dec(1);
        }
        public int inc(){
            return inc(1);
        }
        public Reg(int v){
            val = v;
        }
        public Reg(){
            this(0);
        }
        
    }
    
    // registers
    public Reg ip = new Reg(0);     // instruction pointer register
    public Reg sp = new Reg(-1);    // stack pointer register
    public Reg it = new Reg(0);     //interrupt enable flag
    public Reg plr = new Reg(0);
}
