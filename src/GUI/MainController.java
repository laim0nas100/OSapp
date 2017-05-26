/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import LibraryLB.FX.SceneManagement.BaseController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import kernel.CPU;
import kernel.Defs;
import kernel.MainProcess;
import kernel.Kernel;
import kernel.memory.MemFrame;
import kernel.process.Proc;
import kernel.process.ProcessAPI;
import kernel.process.UserProc;
import kernel.process.sysprocess.SysProc;

/**
 * FXML Controller class
 *
 * @author lemmin
 */
public class MainController extends BaseController {
    
    
    @FXML public Slider delaySlider;
    @FXML public TextArea memoryText;
    @FXML public TextArea procText;
    @FXML public TextArea cpuText;
    @FXML public TextField procField;
    @FXML public Label delayLabel;
    public static CPU cpu = Kernel.cpu;
    @Override
    public void exit() {
        WithGui.manager.closeFrame(this.frame.getID());
        MainProcess.keepRunning = false;
    }

    @Override
    public void update() {
        final StringBuilder memStr = new StringBuilder();
        final StringBuilder procStr = new StringBuilder();
        final StringBuilder cpuStr = new StringBuilder();
        for(MemFrame frame:Kernel.ram){
            memStr.append(frame.toString()+"\n");
        }
        
        for(Proc p:ProcessAPI.allProc){
            procStr.append(p.pid+"\t"+p.state.toString()+"\t");
            if(p instanceof UserProc){
                UserProc up = (UserProc)p;
                procStr.append("\tplr :"+up.plr);
            }else{
                SysProc sp = (SysProc)p;
                procStr.append(" \tJob count: "+sp.pendingJobs.size());
            }
            procStr.append("\n");
        }
        
        cpuStr.append("ip:\t"+cpu.ip.get()+"\n");
        cpuStr.append("sp:\t"+cpu.sp.get()+"\n");
        cpuStr.append("plr:\t"+cpu.plr.get()+"\n");
        cpuStr.append("it:\t"+cpu.it.get()+"\n");
        cpuStr.append("t:\t"+cpu.t.get()+"\n");
        Platform.runLater(() ->{
            memoryText.setText(memStr.toString());
            procText.setText(procStr.toString());
            cpuText.setText(cpuStr.toString());
        });
    }
    
    public void newProc(){
        ProcessAPI.addLoadProcessJob(procField.getText());
    }

   
    
}
