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
public class Defs {
    public static boolean trace = false;
    public static boolean traceMemoryMapping = false;
    
    public enum State{UNUSED,READY,ACTIVE,BLOCKED,ZOMBIE};
    public static final int USER_FRAMES = 15;
    public static final int PAGE_SIZE = 64;
    public static final int USER_PROC_LIMIT = 10;
    public static final int TIME_FRAME = 8;

}
