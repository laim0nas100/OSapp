/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

/**
 *
 * @author lemmin
 */
public class Reg {
    public int val;
    public void set(int i){
        this.val = i;
    }


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
