/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Josue
 */
public class PanelTutorial {
    private Stage primaryStage;
    private BorderPane rootPrincipal;
    private Scene sceneTutorial;
    private BorderPane root;
    private ScrollPane scrollP;
    private static String dirImagenes="/tutorial";
    private static String rutaInstruccion="/tutorial/instrucciones.txt";
    
    private ArrayList<String> instrucciones;
    private ArrayList<ImageView> imaInstruc;
    
    public PanelTutorial(){
        
        instrucciones=leerArchivoInstrucciones();
        imaInstruc=leerImagenes();
        root=new BorderPane();
        root.setPadding(new Insets(10,10,10,10));
        scrollP=new ScrollPane();
        crearEncabezado();
        crearCuerpo();
        crearPie();
        
    }
    
    public PanelTutorial(Stage primaryStage,BorderPane rootPrincipal){
        this.primaryStage=primaryStage;
        this.rootPrincipal=rootPrincipal;
        instrucciones=leerArchivoInstrucciones();
        imaInstruc=leerImagenes();
        root=new BorderPane();
        root.setPadding(new Insets(10,10,10,10));
        scrollP=new ScrollPane();
        crearEncabezado();
        crearCuerpo();
        crearPie();
    }
    
    public void crearEncabezado(){
        Label titulo=new Label("Tutorial de uso");
        Label subtitulo=new Label("A continuación se presentan una suseción de pasos para el uso correcto del programa");
        VBox cabeza=new VBox();
        cabeza.getChildren().addAll(titulo,subtitulo);
        cabeza.setAlignment(Pos.CENTER);
        root.setTop(cabeza);
    
    }
    
    public void crearCuerpo(){
        VBox cuerpo=new VBox();
        
        cuerpo.setSpacing(18);
        for(int i=0;i<instrucciones.size();i++){
            Label label=new Label(String.valueOf(i+1)+".-"+instrucciones.get(i));
            ImageView imagen=(imaInstruc.get(i));
            //VBox paso=new VBox();
            //paso.getChildren().addAll(label,imagen);
            //paso.setSpacing(20);
            cuerpo.getChildren().addAll(label,imagen);
            
        }
        scrollP.setContent(cuerpo);
        root.setCenter(scrollP);
        
    }
    public void crearPie(){
        Button bSalir=new Button("Volver");
        HBox contendorBoton=new HBox(bSalir);
        contendorBoton.setAlignment(Pos.CENTER_RIGHT);
        bSalir.setOnAction(p->{
            primaryStage.getScene().setRoot(rootPrincipal);
            
        });
        root.setBottom(contendorBoton);
    }
    
    public ArrayList<String> leerArchivoInstrucciones(){
        ArrayList<String>lista=new ArrayList<>();
        try(
            BufferedReader lector=new BufferedReader( new InputStreamReader(
                      getClass().getResourceAsStream(rutaInstruccion), "UNICODE"))
                ){
            
            String linea;
            while((linea=lector.readLine())!=null){
                System.out.println(linea);
                lista.add(linea);
            }
            
        }catch(FileNotFoundException e1){
            System.err.println("No se encuentra el archivo");
        }catch(IOException e){
            System.out.println("Erro al leer el archivo");
        }
        return lista;
    }
   
    public  ArrayList<ImageView> leerImagenes(){
        ArrayList<ImageView> imagenesColectadas=new ArrayList<>();
        for(int i=1;i<11;i++){
            InputStream rutaStream=getClass().getResourceAsStream(dirImagenes+"/"+String.valueOf(i)+".jpg");
            //System.out.println(ruta);
            Image im=new Image(rutaStream);
            ImageView img=new ImageView(im);
            imagenesColectadas.add(img);
        }
        
        System.out.println(imagenesColectadas.size());
        return imagenesColectadas;
    }
    
    public Scene getScene(){
        sceneTutorial=new Scene(root,Constantes.aplicacionAncho,Constantes.aplicacionAlto);
        sceneTutorial.getStylesheets().addAll(primaryStage.getScene().getStylesheets());
        return sceneTutorial;
    }
    public BorderPane getRoot(){
        return root;
    }
    
}
