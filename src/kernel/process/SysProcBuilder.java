/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

import kernel.Kernel;

/**
 *
 * @author lemmin
 */
public class SysProcBuilder {
    public static Process idle(){
        Process proc = new Process() {
            @Override
            public void step() {
                //do nothing
            }
        };
        return proc;
    }
    public static Process loader(){
        Process proc = new Process() {
            @Override
            public void step() {
            }
        };
        return proc;
    }
    
    public static Process memoryManager(){
        Process proc = new Process() {
            @Override
            public void step() {
            }
        };
        return proc;
    }
    public static Process interruptManager(){
        Process proc = new Process() {
            @Override
            public void step() {
            }
        };
        return proc;
    }
    
}
