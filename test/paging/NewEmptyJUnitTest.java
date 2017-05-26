/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paging;

import java.util.ArrayList;
import java.util.HashSet;
import kernel.Defs.State;
import kernel.Kernel;
import kernel.process.UserProc;
import kernel.process.ProcessAPI;
import kernel.process.Proc;
import misc.MiniCompiler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author lemmin
 */
public class NewEmptyJUnitTest {
    
    public NewEmptyJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // The methods must be annotated with annotation @Test. For example:
    //
    
    public void execute(UserProc p){
        while(p.state != State.ZOMBIE){
            p.stepLogic();
        }
    }
    
    public void iterate(ArrayList<Proc> list){
        HashSet<Integer> deadSet = new HashSet<>();
        for(Proc p:list){
            p.state = State.ACTIVE;
        }
        while(deadSet.size() < list.size()){
            for(Proc p:list){
                if(p.state == State.ACTIVE){
                    try{
                    p.step();
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                        deadSet.add(p.pid);
                    }
                }else{
                    deadSet.add(p.pid);
                }
            }
        }
    }
    
    @Test
    public void multipleProcessTest() throws Exception {
        Kernel.init();
        ArrayList<Proc> proc = new ArrayList<>();
        proc.add(ProcessAPI.createProcess(MiniCompiler.compile("forceoom1")));
        proc.add(ProcessAPI.createProcess(MiniCompiler.compile("forceoom2")));
        proc.add(ProcessAPI.createProcess(MiniCompiler.compile("forceoom3")));

        Kernel.dumpMemory();
        iterate(proc);
        Kernel.dumpMemory();
       
    }
//    @Test
    public void simpleProcessTest() throws Exception {
        Kernel k = new Kernel();
//         MiniCompiler.compile("loop");]
       ArrayList<UserProc> proc = new ArrayList<>();
       proc.add(ProcessAPI.createProcess(MiniCompiler.compile("forceoom")));
       proc.add(ProcessAPI.createProcess(MiniCompiler.compile("forceoom")));
       UserProc p1 = proc.get(0);
       UserProc p2 = proc.get(1);
       int i = 0;
       System.out.println("First");
       execute(p1);
       Kernel.dumpMemory();
       System.out.println("Second");
       execute(p2);
       Kernel.dumpMemory();
       ProcessAPI.cleanupProcess(p1.pid);
       System.out.println("Dumped");
       Kernel.dumpMemory();
       System.out.println("Again");
       execute(ProcessAPI.createProcess(MiniCompiler.compile("loop")));
       Kernel.dumpMemory();
       try{
           execute(ProcessAPI.createProcess(MiniCompiler.compile("forceoom")));
       }catch (Exception e){
           e.printStackTrace();


       }
       System.out.println("OOM");
       Kernel.dumpMemory();

    }
    
    
//    @Test
    public void badAddressTest() throws Exception{
        Kernel.init();
        ArrayList<Proc> proc = new ArrayList<>();
        proc.add(ProcessAPI.createProcess(MiniCompiler.compile("badAddress")));
        Kernel.dumpMemory();
        iterate(proc);
        Kernel.dumpMemory();
    }
}
