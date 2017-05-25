/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.memory;

import java.util.Arrays;
import misc.Func;
import static kernel.Defs.*;

/**
 *
 * @author lemmin
 */
public class MemFrame {
    public Integer[] mem;
    public boolean used;
    public MemFrame(){
        mem = new Integer[PAGE_SIZE];
        Func.fill(mem,-1);
    }
    public String toString(){
        return Arrays.toString(mem);
    }
}
