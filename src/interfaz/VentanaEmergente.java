/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author Josue
 */
public class VentanaEmergente {
    
   
   
    public static void display(Stage primario,Node contenido, String titutlo) {
        
        BorderPane bp=new BorderPane();
        bp.setPadding(new Insets(10,0,10,0));
        Button cerrar=new Button("Cerrar");
        
        HBox contCerrrar=new HBox(cerrar);
        
        contCerrrar.setAlignment(Pos.CENTER);
        bp.setBottom(contCerrrar);
        bp.setCenter(contenido);
        
        Scene escena=new Scene(bp, 250,250);
        escena.getStylesheets().addAll(primario.getScene().getStylesheets());
        Stage ventana=new Stage();
        
        
        ventana.initModality(Modality.WINDOW_MODAL);
        
        ventana.initOwner(primario);
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.setTitle(titutlo);
        ventana.initStyle(StageStyle.DECORATED);
        
        cerrar.setOnAction(p->{
        ventana.close();
        });
        
        
        ventana.showAndWait();
        
        
        
    }
    
    public static Alert ventanaEmergente(Stage primaryStage,AlertType tipo){
        
        Alert alerta=new Alert(tipo);
        
        alerta.setResizable(false);
        alerta.initStyle(StageStyle.UNDECORATED);
        alerta.showAndWait();
        return alerta;
    }
    public static void mensajeEmergente2(Stage primaryStage,String titulo){
        
        Alert alerta=new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setResizable(false);
        alerta.initStyle(StageStyle.UNDECORATED);
        alerta.showAndWait();
        
    }
    public static void mensajeEmergente3(Stage primaryStage,String titulo){
        
        Alert alerta=new Alert(Alert.AlertType.CONFIRMATION);
        
        
        alerta.setTitle(titulo);
        alerta.setResizable(false);
        alerta.initStyle(StageStyle.UNDECORATED);
        alerta.showAndWait();
        System.out.println(alerta.getResult().getText());
    }
    public static void mensajeEmergente4(Stage primaryStage,String titulo){
        
        Alert alerta=new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setResizable(false);
        alerta.initStyle(StageStyle.UNDECORATED);
        alerta.showAndWait();
        
    }
    public static void mensajeEmergente5(Stage primaryStage,String titulo){
        
        Alert alerta=new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setResizable(false);
        alerta.initStyle(StageStyle.UNDECORATED);
        alerta.showAndWait();
        
    }
    public static void mensajeEmergente6(Stage primaryStage,String titulo){
        
        Alert alerta=new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setResizable(false);
        alerta.initStyle(StageStyle.UNDECORATED);
        alerta.showAndWait();
        
    }
}
