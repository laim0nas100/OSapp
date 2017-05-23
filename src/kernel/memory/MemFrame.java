/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.memory;

import java.util.Arrays;
import kernel.Func;

/**
 *
 * @author lemmin
 */
public class MemFrame {
    public Integer[] mem;
    public MemFrame(){
        mem = new Integer[Paging.PAGE_SIZE];
        Func.fillZero(mem);
    }
    public String toString(){
        return Arrays.toString(mem);
    }
}
