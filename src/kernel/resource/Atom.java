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
public class Atom {
    public enum AtomID{INT};
    public AtomID id;
    public Process proc;
    public Atom(Process proc, AtomID id){
        this.id = id;
        this.proc = proc;
    }
}
