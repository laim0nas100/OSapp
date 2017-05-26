/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import LibraryLB.FX.SceneManagement.Frame;
import LibraryLB.FX.SceneManagement.MultiStageManager;
import java.net.URL;
import javafx.application.Platform;
import kernel.Kernel;
import kernel.MainProcess;

/**
 *
 * @author Lemmin
 */
public class WithGui {
    public static MultiStageManager manager;
    public static MainController scene;
    public static void main(String[] args){
        manager = new MultiStageManager();
        try{
            URL resource = manager.getResource("/GUI/mainController.fxml");
            Frame newFrame = manager.newFrame(resource, "OS info");
            Kernel.init();

            Platform.runLater(() ->{
                newFrame.getStage().show();
                scene = (MainController) newFrame.getController();
                MainProcess.delay.bind(scene.delaySlider.valueProperty());
                scene.delayLabel.textProperty().bind(scene.delaySlider.valueProperty().asString("%.0f"));
                new Thread(() ->{
                    try {
                        MainProcess.start();
                    } catch (InterruptedException ex) {
                    }
                }).start();
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
