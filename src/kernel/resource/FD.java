/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.resource;

/**
 *
 * @author lemmin
 */
public class FD {
    public int id;
    public boolean isfree = true;
    public FD(int id){
        this.id = id;
    }
    public void write(int value){
        if(id == 1){//standard out
            System.out.println(value);
        }else{
            // maybe add file system
        }
    }
}
