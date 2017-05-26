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
    public static final int FRAME_LIMIT = 16;
    public static final int PAGE_SIZE = 32;
    
    public static final int TIME_FRAME = 8;
    
    public static final int ACTION_REPEAT_LIMIT = 6;
    
    public static final int USER_PROC_LIMIT = 6;
    public static final int SYSTEM_PROC_LIMIT = 4;
    public static final int ALL_PROC_LIMIT = USER_PROC_LIMIT + SYSTEM_PROC_LIMIT;
    
    public static final int PID_IDLE = USER_PROC_LIMIT;
    public static final int PID_FS_HANDLER = USER_PROC_LIMIT + 1;
    public static final int PID_PROC_HANDLER = USER_PROC_LIMIT + 2;
    public static final int PID_MEMORY_HANDLER = USER_PROC_LIMIT + 3;
}
