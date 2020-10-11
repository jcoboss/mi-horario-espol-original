/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 *
 * @author Josue
 */
public class MihorarioEspol extends Application{

    @Override
    public void start(Stage primaryStage) {
        
        PanelPrincipal pp=new PanelPrincipal(primaryStage);
        //ns.getStylesheets().add(u.toString());
        
        primaryStage.setScene(pp.getScene());
        primaryStage.setTitle("Mi Horario ESPOL 0.9");
        primaryStage.getIcons().add(new Image(MihorarioEspol.class.getResourceAsStream("/iconos/stage.png")));
        primaryStage.show();
        
    }
    public static void main(String [] args){
        launch(args);
        
        
    }
    
}
