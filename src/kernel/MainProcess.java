package kernel;

import GUI.WithGui;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import static kernel.Defs.*;
import kernel.process.Proc;
import kernel.process.ProcessAPI;
/**
 *
 * @author lemmin
 */
public class MainProcess {
    public static SimpleDoubleProperty delay = new SimpleDoubleProperty(500);
    public static int currentPID = PID_IDLE;
    public static boolean keepRunning = true;
    public static Proc current;
    public static void start() throws InterruptedException{
        findNextReady();
        while(keepRunning){
            while(current.state == State.ACTIVE){
                while((int)delay.get() >= 3000){
                    Thread.sleep(500);
                    System.out.print(".");
                }
                Thread.sleep((int)delay.get());
                
                
                if(WithGui.scene != null){
                    WithGui.scene.update();
                }
                current.step();
            }
            contextSwitch();
        }
    }
    

    private static void incrementCurrentPID(){
        currentPID = (currentPID + 1) % ALL_PROC_LIMIT;
    }
    private static void findNextReady(){
        incrementCurrentPID();
        int i = 0;
        current = ProcessAPI.allProc[currentPID];
        while(current.state != State.READY){
            incrementCurrentPID();
            current = ProcessAPI.allProc[currentPID];
            i++;
            if(i>1000){//something went wrong
                current = ProcessAPI.allProc[PID_IDLE];
                System.out.println("Scheduler malfunctioned");
                return;
            }
            
        }
//        System.out.println("Found next "+currentPID);
        current.state = State.ACTIVE;
    }
    
    
    public static void contextSwitch(){
        current.unbindCPU();
        findNextReady();
        current.bindCPU();
        System.out.println("Switch to "+currentPID);
    }
    
    public static void main(String[] args){
        Kernel.init();
        
        
        //somehow load processes
//        ProcessAPI.addLoadProcessJob("forceoom1");
        ProcessAPI.addLoadProcessJob("print");
        try{
            start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
}
