/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author lemmin
 */
public class Reg {
    private Reg update;
    public int val;
    public void bind(Reg other){
        update = other;
    }
    public void unbind(){
        update = null;
    }
    public int get(){
        return val;
    }
    public void set(int v){
        this.val = v;
        if(update!=null){
            update.set(v);
        }    
    }
    public int incBefore(int i){
        this.set(get() + 1);
        return get();
    }
    public int decBefore(int i){
        this.set(get() - i);
        return get();
    }
    public int inc(int i){
        int save = get();
        this.set(save + i);
        return save;
    }
    public int dec(int i){
        int save = get();
        this.set(save - i);
        return save;
    }
    public int dec(){
        return dec(1);
    }
    public int inc(){
        return inc(1);
    }
    public Reg(int v){
        this.val = v;
    }
    public Reg(){
        this(0);
    }
}
