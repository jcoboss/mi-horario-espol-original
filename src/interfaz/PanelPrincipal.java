/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import mihorario.Clase;
import mihorario.ClassCollisionException;
import mihorario.Estudiante;
import mihorario.Materia;
import mihorario.Examen;
import mihorario.ExamenCategoria;
import mihorario.Horario;
import mihorario.LlaveSemana;
import mihorario.ScrapFile;
import Dato.Dato;
import externo.Browser;
import externo.MateriaCenacad;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

/**
 *
 * @author Josue
 */
public class PanelPrincipal {
    
    private BorderPane root;
    private VBox izquierda;
    private MenuBar arriba;
    private TabPane centro;
    private VBox abajo;
    private Stage primaryStage;
    private Label labelRuta;
    
    private VBox contenedorPreviews;
    
    private Estudiante estudiante;
    
    public PanelPrincipal(Stage primaryStage){
        this.primaryStage=primaryStage;
        
        root=new BorderPane();
        root.getStyleClass().add("border-pane");
        
        root.setPadding(new Insets(0,0,10,0));
        estudiante=new Estudiante();
        labelRuta=new Label("");
        crearArriba();
        
        crearIzquierda();
        crearCentro();
        crearAbajo();
        
        //getKeyCode();
        this.primaryStage.setOnCloseRequest(o->{
            o.consume();
                Alert mensajeExp=ventanaEmergente(this.primaryStage,Alert.AlertType.CONFIRMATION);
                mensajeExp.setHeaderText("Salir");
                mensajeExp.setContentText("Seguro que deseas salir de HorariosAP ?");
                
                mensajeExp.showAndWait();
                if(mensajeExp.getResult().equals(ButtonType.OK)){
                    this.primaryStage.close();
                }else if(mensajeExp.getResult().equals(ButtonType.CANCEL)){
                    mensajeExp.close();
                }
        });
    }
    
    public Scene getScene(){
        Scene ns=new Scene(getRoot(),Constantes.aplicacionAncho,Constantes.aplicacionAlto);
        ns.getStylesheets().add(getRutaCssFileDark());
        
        return ns;
    }
    
    public String getRutaCssFileDark(){
        return MihorarioEspol.class.getResource("/interfaz/darkMaterialDesign.css").toExternalForm();
    }
    
    public void crearArriba(){
        arriba=new MenuBar();
        Menu iArchivo=new Menu("Archivo");
        MenuItem iNuevo=new MenuItem("Nuevo");
        iNuevo.setOnAction(new manejadorNuevo());
        MenuItem iGuardar=new MenuItem("Guardar");
        iGuardar.setOnAction(new manejadorGuardar());
        
        Menu iCargar=new Menu("Cargar");
        
        iArchivo.setOnShowing(e-> {
            
            ArrayList<String> rutasE=Dato.cargarDato();
            
            if(rutasE.isEmpty()){
                iCargar.getItems().clear();
                MenuItem itemVacio=new MenuItem("No hay datos");
                iCargar.getItems().add(itemVacio);
            }else if(rutasE.size()>=1){
                iCargar.getItems().clear();
                for(String ruta:rutasE){
                    MenuItem item=new MenuItem(ruta.split("/")[5]);
                    item.setOnAction(new manejadorCargarItem(ruta));
                    iCargar.getItems().add(item);
                }
                
            }
            
        });
        
        iArchivo.getItems().addAll(iNuevo,iGuardar,iCargar);
        
        Menu iExportar=new Menu("Exportar");
        
        Menu iPNG=new Menu(".PNG");
        //empieza implementacion de IPNG
        
        MenuItem ehorarioClases=new MenuItem("Clases");
        
        Menu eIParcial=new Menu(ExamenCategoria.PARCIAL.toString());
        Menu eIFinal=new Menu(ExamenCategoria.FINAL.toString());
        Menu eIRecuperacion=new Menu(ExamenCategoria.MEJORAMIENTO.toString());
        
        iPNG.setOnShowing(r->{
            ObservableList<Tab> tabs=centro.getTabs();
            eIParcial.getItems().clear();
            eIFinal.getItems().clear();
            eIRecuperacion.getItems().clear();
            ehorarioClases.setOnAction(new manejadorExportarPNG(
                tabs.get(0).getContent(), null));
            
            if(tabs.get(1).getContent()instanceof TabPane){
                System.out.println("Es instancia de TabsPane");
                TabPane tPanel=(TabPane)tabs.get(1).getContent();
                int i=1;
                for(Tab t:tPanel.getTabs()){
                    MenuItem item=new MenuItem("Semana "+i);
                    i++;
                    item.setOnAction(new manejadorExportarPNG(t.getContent(), tabs.get(1).getText()));
                    eIParcial.getItems().add(item);
                }
            }else if (tabs.get(1).getContent() instanceof TableView ){
                System.out.println("Es instancia de TableView");
                TableView tablita=(TableView)tabs.get(1).getContent();
                MenuItem item=new MenuItem("Semana 1");
                item.setOnAction(new manejadorExportarPNG(tablita, tabs.get(1).getText()));
                eIParcial.getItems().add(item);
            }
            
            if(tabs.get(2).getContent()instanceof TabPane){
                System.out.println("Es instancia de TabsPane");
                TabPane tPanel=(TabPane)tabs.get(2).getContent();
                int i=1;
                for(Tab t:tPanel.getTabs()){
                    MenuItem item=new MenuItem("Semana "+i);
                    i++;
                    item.setOnAction(new manejadorExportarPNG(t.getContent(), tabs.get(2).getText()));
                    eIFinal.getItems().add(item);
                }
            }else if (tabs.get(2).getContent() instanceof TableView ){
                System.out.println("Es instancia de TableView");
                TableView tablita=(TableView)tabs.get(2).getContent();
                MenuItem item=new MenuItem("Semana 1");
                item.setOnAction(new manejadorExportarPNG(tablita, tabs.get(2).getText()));
                eIFinal.getItems().add(item);
            }
            
            if(tabs.get(3).getContent()instanceof TabPane){
                System.out.println("Es instancia de TabsPane");
                TabPane tPanel=(TabPane)tabs.get(3).getContent();
                int i=1;
                for(Tab t:tPanel.getTabs()){
                    MenuItem item=new MenuItem("Semana "+i);
                    i++;
                    item.setOnAction(new manejadorExportarPNG(t.getContent(), tabs.get(3).getText()));
                    eIRecuperacion.getItems().add(item);
                }
            }else if (tabs.get(3).getContent() instanceof TableView ){
                System.out.println("Es instancia de TableView");
                TableView tablita=(TableView)tabs.get(3).getContent();
                MenuItem item=new MenuItem("Semana 1");
                item.setOnAction(new manejadorExportarPNG(tablita, tabs.get(3).getText()));
                eIRecuperacion.getItems().add(item);
            }
            
            
            
        });
        
        iPNG.getItems().add(ehorarioClases);
       iPNG.getItems().addAll(eIParcial,eIFinal,eIRecuperacion);
        
        //termina implementacion iPNG
        
        
        Menu iCSV=new Menu(".CSV");
        MenuItem horarioClases=new MenuItem("Clases");
        
        
        Menu eParcial=new Menu(ExamenCategoria.PARCIAL.toString());
        Menu eFinal=new Menu(ExamenCategoria.FINAL.toString());
        Menu eRecuperacion=new Menu(ExamenCategoria.MEJORAMIENTO.toString());
        
        iCSV.setOnShowing(r->{
            eParcial.getItems().clear();
            eFinal.getItems().clear();
            eRecuperacion.getItems().clear();
            horarioClases.setOnAction(new manejadorExportarH(
                estudiante.getHorarioClases(), null));
                
            for(LlaveSemana llave:estudiante.getExamenesParciales().keySet()){
                MenuItem iHEx=new MenuItem(llave.toString());
                iHEx.setOnAction(new manejadorExportarH(estudiante.getExamenesParciales().get(llave),"PARCIAL"));
                eParcial.getItems().add(iHEx);
            }
            for(LlaveSemana llave:estudiante.getExamenesFinales().keySet()){
                MenuItem iHEx=new MenuItem(llave.toString());
                iHEx.setOnAction(new manejadorExportarH(estudiante.getExamenesFinales().get(llave),"FINAL"));
                eFinal.getItems().add(iHEx);
            }
            for(LlaveSemana llave:estudiante.getExamenesRecuperacion().keySet()){
                MenuItem iHEx=new MenuItem(llave.toString());
                iHEx.setOnAction(new manejadorExportarH(estudiante.getExamenesRecuperacion().get(llave),"MEJORAMIENTO"));
                eRecuperacion.getItems().add(iHEx);
            }
        });
        
        iCSV.getItems().add(horarioClases);
       iCSV.getItems().addAll(eParcial,eFinal,eRecuperacion); 
        
        
        //iExportar.getItems().addAll(iCSV,iPNG);
        iExportar.getItems().add(iCSV);
        
        Menu iTema=new Menu("Tema");
        MenuItem iDark=new MenuItem("Oscuro");
        iDark.setOnAction(e->{
               primaryStage.getScene().getStylesheets().add(getRutaCssFileDark());
        });
        MenuItem iDefault=new MenuItem("Defecto");
        iDefault.setOnAction(e->{
                    primaryStage.getScene().getStylesheets().clear();
               
        });
        iTema.getItems().addAll(iDark,iDefault);
        
        Menu iAyuda=new Menu("Ayuda");
        MenuItem iTutorial=new MenuItem("Tutorial");
        iTutorial.setOnAction(lop->{
            
            primaryStage.getScene().setRoot(new PanelTutorial(primaryStage,getRoot()).getRoot());
            
        });
        MenuItem iInfo=new MenuItem("Información");
        iInfo.setOnAction(r->{
        VBox f=new VBox();
        f.setPadding(new Insets(10,10,10,10));
        f.setSpacing(8);
        f.setAlignment(Pos.CENTER);
        TextArea ll=new TextArea("Información de producto:"+"\n"
                + "HorariosAP v0.9\n" +
                    "24 feb 2019\n" +
                    "\n" +
                    "Contacto: jacobos@espol.edu.ec");
        ll.setEditable(false);
        f.getChildren().addAll(ll);
        VentanaEmergente.display(primaryStage,f,"Información");
        
    });
        iAyuda.getItems().addAll(iTutorial,iInfo);
        arriba.getMenus().addAll(iArchivo,iExportar,iTema,iAyuda);
        root.setTop(arriba);
        
    }
            
   public void crearIzquierda(){
       izquierda=new VBox();
       izquierda.getStyleClass().add("tarjeta");
       izquierda.setPadding(new Insets(0,10,0,10));
       izquierda.setAlignment(Pos.CENTER);
       Label botonesAyuda=new Label("Botones De Ayuda");
       //izquierda.getChildren().add(new Separator(Orientation.HORIZONTAL));
       //izquierda.getChildren().add(botonesAyuda);
       //izquierda.getChildren().add(new Separator(Orientation.HORIZONTAL));
       contenedorPreviews=new VBox();
       contenedorPreviews.getStyleClass().add("tarjeta");
       contenedorPreviews.setAlignment(Pos.CENTER);
       contenedorPreviews.setSpacing(10);
       
       HBox contenedorBotones=new HBox();
       contenedorBotones.getStyleClass().add("tarjeta");
       Button bAnadir=new Button("Añadir Instancia");
       
       bAnadir.setTooltip(new Tooltip("Añade una nueva materia"));
       bAnadir.setOnAction(new manejadorAnadir());
       
       Button bEliminar=new Button("Añadir Carpeta");
       bEliminar.setTooltip(new Tooltip("Añade todas las instancias encontradas en una carpeta"));
       bEliminar.setOnAction(new manejadorAñanirDirectorio());
       
       Button bBorrarTodo=new Button("Limpiar");
       //bBorrarTodo.setGraphic(new ImageView(new Image(PanelPrincipal.class.getResourceAsStream("/iconos/clean.png"))));
       //bBorrarTodo.getGraphic().getStyleClass().add("glyph-icon");
       bBorrarTodo.setTooltip(new Tooltip("Elimina todas las instancias creadas"));
       bBorrarTodo.setOnAction(new manejadorLimpiar());
       
       contenedorBotones.getChildren().addAll(bAnadir,bEliminar,bBorrarTodo);
       contenedorBotones.setSpacing(10);
       contenedorBotones.setAlignment(Pos.CENTER);
       
       izquierda.getChildren().add(contenedorBotones);
       //izquierda.getChildren().add(new Separator(Orientation.HORIZONTAL));
       ScrollPane sP=new ScrollPane((contenedorPreviews));
       sP.setFitToWidth(true);
       sP.setFitToHeight(true);
       sP.setPrefHeight(520);
       sP.setPrefWidth(300);
       sP.getStyleClass().add("panel");
       
       izquierda.getChildren().add(sP);
       izquierda.setSpacing(5);
       
       root.setLeft(izquierda);
       
       
   }
    
   public void crearCentro() {
       
       
       centro=new TabPane();
       centro.getStyleClass().add("tab-pane");
       Tab tHorario=new Tab("CLASES");
       tHorario.setClosable(false);
       centro.getTabs().add(tHorario);
       
       TableView<FilaHorario> t1=new TableView();
       
       //t1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       //t1.setPrefSize(900, 600);
       
       //System.out.println("ancho: "+t1.getPrefWidth());
       t1.setItems(getFilaHorarioGeneral(estudiante.getHorarioClases()));
       
       for(TableColumn columna:getTableColumns()){
           columna.setSortable(false);
         
           t1.getColumns().add(columna);
           
       }
       System.out.println("\n");
       //t1.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
       tHorario.setContent(t1);
       
       crearHorariosExamenes(ExamenCategoria.PARCIAL,estudiante.getExamenesParciales());
       crearHorariosExamenes(ExamenCategoria.FINAL,estudiante.getExamenesFinales());
       crearHorariosExamenes(ExamenCategoria.MEJORAMIENTO,estudiante.getExamenesRecuperacion());
       
       root.setCenter(centro);
       centro.heightProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue args0,Object o1,Object o2){
                double anchoActual=(Double) o2;
                ((ScrollPane)izquierda.getChildren().get(1)).setPrefHeight(anchoActual);
            }
        }
        );
   } 
   
   public void crearHorariosExamenes(ExamenCategoria cat,HashMap<LlaveSemana,Horario> examenesMap){
       //general para todos 
       
       //particular
       Tab tExamenes=new Tab("Examenes "+cat.toString());
       tExamenes.setClosable(false);
       if(examenesMap.size()<=1){
           TableView<FilaHorario> tabla=new TableView();
            for(LlaveSemana llave:examenesMap.keySet()){
            tabla.setItems(getFilaHorarioGeneral(examenesMap.get(llave)));
            }
            for(TableColumn columna:getTableColumns()){
                columna.setSortable(false);
                tabla.getColumns().add(columna);
                }
            tExamenes.setContent(tabla);
       }else{
           TabPane panelTabs=new TabPane();
           for(LlaveSemana llave:examenesMap.keySet()){
                TableView<FilaHorario> table=new TableView();
                Tab semana=new Tab(llave.toString());
                semana.setClosable(false);
                table.setItems(getFilaHorarioGeneral(examenesMap.get(llave)));
                
                for(TableColumn columna:getTableColumns()){
                    columna.setSortable(false);
                table.getColumns().add(columna);
                }
                semana.setContent(table);
                panelTabs.getTabs().add(semana);
           }
           tExamenes.setContent(panelTabs);
           
       }
       centro.getTabs().add(tExamenes);
   }
   
   public ArrayList<TableColumn> getTableColumns(){
       ArrayList<TableColumn> columnas=new ArrayList<>();
       TableColumn <FilaHorario,String> cHoras=new TableColumn<>("Horas");
      
       cHoras.setCellValueFactory(new PropertyValueFactory<>("intervaloHora"));
       
       TableColumn <FilaHorario,String> cLunes=new TableColumn<>("Lunes");
        cLunes.setCellValueFactory(new PropertyValueFactory<FilaHorario,String> ("lunes"));
        
       TableColumn <FilaHorario,String> cMartes=new TableColumn<>("Martes");
        cMartes.setCellValueFactory(new PropertyValueFactory<FilaHorario,String> ("martes"));
       
       TableColumn <FilaHorario,String> cMiercoles=new TableColumn<>("Miércoles");
        cMiercoles.setCellValueFactory(new PropertyValueFactory<FilaHorario,String> ("miercoles"));
       
       TableColumn <FilaHorario,String> cJueves=new TableColumn<>("Jueves");
        cJueves.setCellValueFactory(new PropertyValueFactory<FilaHorario,String> ("jueves"));
       
       TableColumn <FilaHorario,String> cViernes=new TableColumn<>("Viernes");
        cViernes.setCellValueFactory(new PropertyValueFactory<FilaHorario,String> ("viernes"));
        
       TableColumn <FilaHorario,String> cSabado=new TableColumn<>("Sábado");
        cSabado.setCellValueFactory(new PropertyValueFactory<FilaHorario,String> ("sabado"));
       
       columnas.add(cHoras);
       columnas.add(cLunes);
       columnas.add(cMartes);
       columnas.add(cMiercoles);
       columnas.add(cJueves);
       columnas.add(cViernes);
       columnas.add(cSabado);
       return columnas;
   }
   
   public ObservableList<FilaHorario> getFilaHorarioGeneral(Horario h){
       ObservableList<FilaHorario> filas=FXCollections.observableArrayList();
       
       int i=0;
       for(Materia[] mat:h.getHorario()){
           FilaHorario fila=new FilaHorario(h.getHoras(), mat, i);
           filas.add(fila);
           i++;
           //System.out.println(Arrays.deepToString(mat));
           
       }
       System.out.println("\n");
       
       return filas;
   }
   
   public void crearAbajo(){
       abajo=new VBox();
       abajo.getStyleClass().add("hbox");
       abajo.setPadding(new Insets(0,0,0,10));
       abajo.getChildren().add(labelRuta);
       root.setBottom(abajo);
       root.widthProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue args0,Object o1,Object o2){
                double anchoActual=(Double) o2;
                abajo.setPrefWidth(anchoActual);
            }
        }
        );
   }
   
   public BorderPane getRoot(){
       return root;
   }
   
   /*Queue<KeyCode> keyCodeCaptures =new LinkedList();
   keyCodeCaptures.add(KeyCode.A);
  */
   KeyCode []keyCapture={KeyCode.A,KeyCode.B};
   
   public void getKeyCode(){
    //IMPORTANTE: UN OBJETO DE TIPO PANA NO TIENE LA PROPIEDAD FOCUS,LOS EVENTOS
    //DE TIPO KEYEVENT SOLO FUNCIONAN EN LOS OBJETOS QUE TIENE FOCUS
    root.setFocusTraversable(true); 
    root.setOnKeyPressed(new EventHandler<KeyEvent>() {
    @Override public void handle(KeyEvent event) {
    
        keyCapture[1]=keyCapture[0];
        keyCapture[0]=event.getCode();
        if(keyCapture[0].equals(KeyCode.C)&& keyCapture[1].equals(KeyCode.CONTROL)){
            System.out.println("Se presiono CTRL and C");
            
        }
        System.out.println(Arrays.deepToString(keyCapture)); 
    }
    });
    }
   
    public VBox getContenedorParalelo(Materia materia){
        VBox contenedorParalelo=new VBox();
        contenedorParalelo.getStyleClass().add("tarjeta");
        contenedorParalelo.setAlignment(Pos.CENTER);
        Label nombre=new Label(materia.getNombre());
        Label Tclases=new Label("Clases");
        Label paralelo=new Label("Paralelo:"+materia.getParaleloString());
        Label nombreP=new Label(materia.getProfesor().getNombre());
        nombreP.setGraphic(new ImageView(new Image(PanelPrincipal.class.getResourceAsStream("/iconos/buscar.png"))));
        nombreP.setTooltip(new Tooltip("Da clic para buscar información de este profesor en cenacad"));
        contenedorParalelo.getChildren().addAll(nombre,paralelo,nombreP);
        contenedorParalelo.getChildren().add(Tclases);
        
        for(Clase cl:materia.getClases()){
            Label claseInfo=new Label(cl.getInfo());
            contenedorParalelo.getChildren().add(claseInfo);
            }
        if(!(materia.getExamenes().isEmpty())){
            Label TExamenes=new Label("Examenes");
            contenedorParalelo.getChildren().add(TExamenes);
            for(Examen examen:materia.getExamenes()){
                Label examenInfo=new Label(examen.getInfo());
                contenedorParalelo.getChildren().add(examenInfo);
                                    
                }
            }
        //contenedorParalelo.setStyle("-fx-background-color: #d0ff78");
        nombreP.setOnMouseClicked(new manejadorBrowser(contenedorParalelo,materia,nombreP));
        
        return contenedorParalelo;
       
   }
    
    public class CustomProgressBar implements Runnable{
        
        public ProgressBar barra = new ProgressBar();
        
        @Override
        public void run() {
            Platform.runLater(()->{
            System.out.println("Entro en empezar");
            barra.setPrefWidth(200);
            abajo.getChildren().add(barra);
            });
        }
}
    
    public class manejadorBrowser implements EventHandler{
        
        public VBox contenedor;
        public String nombreProfesor;
        public String nombreMateria;
        public Label label;
        
        public manejadorBrowser(VBox contenedor,Materia materia,Label label){
            this.contenedor=contenedor;
            this.nombreProfesor=materia.getProfesor().getNombre();
            this.nombreMateria=materia.getNombre();
            this.label=label;
        }
        
        public String colorChooser(double promedio){
           String hsb="-fx-border-color:hsb("+Math.round(promedio)+",73%,96%)";
            return hsb;
        }
        
        @Override
        public void handle(Event event){
            Browser br=null;
            try{
                /*CustomProgressBar barraCarga=new CustomProgressBar();
                Thread hiloCarga=new Thread(barraCarga);
                hiloCarga.start();*/
                br=new Browser(nombreProfesor);
               
                if(br.getProfe()!=null){
                    
                    Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.CONFIRMATION);
                    mensajeExp.setHeaderText("Búsqueda");
                    mensajeExp.setContentText("Este es el profesor que buscas?: "+"\n"+br.getProfe().toString()+"\nSi presionas 'ACEPTAR' se empezará a buscar información de la materia");
                
                    mensajeExp.showAndWait();
                    
                    if(mensajeExp.getResult().equals(ButtonType.OK)){
                        br.buscarMaterias();
                        //System.out.println(abajo.getChildren().size());
                        //barraCarga.detener();
                        System.out.println(abajo.getChildren().size());
                        if(!br.getMateriasEncontradas().isEmpty()){
                            MateriaCenacad matCad=br.buscarMateria(nombreMateria);
                            label.setTooltip(new Tooltip("Profesor: "+br.getProfe().toString()+"\nResultado:"+matCad.toString()));
                            contenedor.setStyle(colorChooser(matCad.getPromedio()));
                        }
                    }else if(mensajeExp.getResult().equals(ButtonType.CANCEL)){
                        mensajeExp.close();
                        label.setTooltip(new Tooltip("No se encontró información :("));
                    }
                }else{
                    label.setTooltip(new Tooltip("No se encontró información :("));
                }
            }catch(IOException exp){
                 
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.WARNING);
                mensajeExp.setHeaderText("Busqueda");
                mensajeExp.setContentText("No se puede establecer conexión a internet");
                mensajeExp.showAndWait();
                System.err.println("No hay internet");
            }
        }
    }
   
    
   public static Alert ventanaEmergente(Stage primaryStage,Alert.AlertType tipo){
        
        Alert alerta=new Alert(tipo);
        
        alerta.setResizable(false);
        alerta.initStyle(StageStyle.UNDECORATED);
        alerta.getDialogPane().getStylesheets().addAll(primaryStage.getScene().getStylesheets());
        return alerta;
    }
   
    
    public class manejadorFileChooser implements EventHandler<ActionEvent>{

       File file;
       VBox contenedorMateria=new VBox();
            
       
        public manejadorFileChooser(File file){
           this.file=file;
       }
        @Override
        public void handle(ActionEvent event) {
            contenedorMateria.getStyleClass().add("vbox");
            contenedorMateria.setAlignment(Pos.CENTER);
            if (file!=null){
                Label rutaFuente = new Label();
                rutaFuente.setText(file.getPath().replaceAll("\\\\", "/"));
                //contenedorMateria.getChildren().remove(examinar);
                ScrapFile sc = null;
                ObservableList<Materia> listaCombo = null;
                try {
                    sc = new ScrapFile(rutaFuente.getText());
                    listaCombo = FXCollections.observableArrayList(sc.getMateriasPracticas());

                } catch (IOException e) {
                    System.err.println("Error al leer el archivo");
                } catch (ParseException e2) {
                    System.err.println("Error al formatear");
                }

                        //reinicio incio // la primera materia guardada es la teorica
                Materia materiaTeorica = sc.getMateriasColectadas().get(0);
                contenedorMateria.getChildren().add(getContenedorParalelo(materiaTeorica));

                if (sc.getMateriasColectadas().size() == 2) {
                    Materia materiaPractica = sc.getMateriasColectadas().get(1);
                    contenedorMateria.getChildren().add(getContenedorParalelo(materiaPractica));
                } else if (sc.getMateriasColectadas().size() > 2) {

                    ComboBox combo = new ComboBox(listaCombo);

                    Callback<ListView<Materia>, ListCell<Materia>> factory = lv -> new ListCell<Materia>() {

                        @Override
                        protected void updateItem(Materia item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? "" : item.getParaleloString());
                        }

                    };
                    combo.setCellFactory(factory);
                    combo.setButtonCell(factory.call(null));

                    ArrayList<Materia> nuevaCollected = new ArrayList<>();

                    combo.setOnAction((r)-> {
                            if (contenedorMateria.getChildren().size() == 4) {
                                contenedorMateria.getChildren().remove(2);
                            }
                            Materia mSelected = (Materia) combo.getValue();
                            contenedorMateria.getChildren().add(2, getContenedorParalelo(mSelected));
                            nuevaCollected.clear();
                            nuevaCollected.add(materiaTeorica);
                            nuevaCollected.add(mSelected);
                    });
                    sc.setMateriasColectadas(nuevaCollected);
                    HBox contenedorCombo = new HBox();
                    contenedorCombo.getStyleClass().add("tarjeta");
                    contenedorCombo.getChildren().add(new Label("Paralelo: "));
                    contenedorCombo.getChildren().add(combo);
                    contenedorMateria.getChildren().add(contenedorCombo);

                }

                HBox contenedorSeleccion = new HBox();
                contenedorSeleccion.getStyleClass().add("tarjeta");
                Button bAgregarHorario = new Button("Agregar al horario");
                Button bRetirarHorario = new Button("Quitar del horario");
                bRetirarHorario.setDisable(true);

                bAgregarHorario.setOnAction(new manejadorAgregarHorario(sc, bAgregarHorario, bRetirarHorario));
                bRetirarHorario.setOnAction(new manejadorQuitarHorario(sc, bAgregarHorario, bRetirarHorario));
                //bAgregarHorario.setDisable(true);
                Button bBorrarInsta = new Button("X");
                bBorrarInsta.setOnAction(
                        (e) -> {
                            contenedorPreviews.getChildren().remove(contenedorMateria);
                        });

                contenedorSeleccion.getChildren().addAll(bAgregarHorario, bRetirarHorario, bBorrarInsta);
                contenedorSeleccion.setAlignment(Pos.CENTER);
                contenedorSeleccion.setSpacing(6);
                //contenedorMateria.getChildren().add(new Separator(Orientation.HORIZONTAL));
                contenedorMateria.getChildren().add(contenedorSeleccion);

                contenedorMateria.setOnMouseClicked(new manejadorLabelBotton(rutaFuente));

                contenedorPreviews.getChildren().add(contenedorMateria);
                //reinicio fin
            } else {
                System.out.println("No se selecciono nada");
            }

            
        }
   }
           
   public class manejadorAnadir implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event)  {
            
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Seleccionar archivo");
            fileChooser.getExtensionFilters().add(
                    new ExtensionFilter("Web", "*.htm", "*html"));

            File file = fileChooser.showOpenDialog(primaryStage);

            new manejadorFileChooser(file).handle(event);
            
        }
       
   }
   
   public class manejadorAñanirDirectorio implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event)  {
            
            DirectoryChooser direcChooser = new DirectoryChooser();

           direcChooser.setTitle("Seleccionar archivo");

           File directorio = direcChooser.showDialog(primaryStage);

           if (directorio != null) {

               if (directorio.isDirectory()) {

                   System.out.println("Es directorio");
                   for (File f : directorio.listFiles()) {
                       String[] rutaCompleta = f.getPath().split("\\\\");
                       if (rutaCompleta[rutaCompleta.length - 1].endsWith(".htm")
                               || rutaCompleta[rutaCompleta.length - 1].endsWith(".html")) {
                           //System.out.println("Es del formato correcto");
                           new manejadorFileChooser(f).handle(event);
                       }
                   }

               } else {
                   System.out.println("No es directorio");
               }
           }
       }
       
   }
   
   public class manejadorExportarPNG implements EventHandler<ActionEvent>{
       
       TableView tablaCopia;
       String categoria;
       
       public manejadorExportarPNG(Node tabla,String categoria){
           //System.out.println("Constructor de exportarH");
           this.tablaCopia=(TableView)tabla;
           this.categoria=categoria;
           
       }
       
       @Override
       public void handle(ActionEvent event){
           
           try{
           DirectoryChooser dChooser= new DirectoryChooser();
            dChooser.setTitle("Seleccionar ruta");
           
            File directorioChooser= dChooser.showDialog(primaryStage);
            if(directorioChooser==null ){
                
                System.out.println("No se selecciono ningun directorio");
            }else{
                this.tablaCopia.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                String rutaDirectorio=directorioChooser.getAbsolutePath().replaceAll("\\\\","/");
                Dato.saveAsPng(tablaCopia, rutaDirectorio, categoria);
                
                this.tablaCopia.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.INFORMATION);
                mensajeExp.setHeaderText("Datos");
                mensajeExp.setContentText("Se ha exportado correctamente");
                mensajeExp.showAndWait();
                
            }
            
           
           }catch(IOException e){
               Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
               mensajeExp.setHeaderText("Exportar Datos");
               mensajeExp.setContentText(e.getClass().getName());
               mensajeExp.showAndWait();
           }
       }
   }
   
   
   public class manejadorExportarH implements EventHandler<ActionEvent>{
       
       Horario horario;
       String categoria;
       
       public manejadorExportarH(Horario horario,String categoria){
           //System.out.println("Constructor de exportarH");
           this.horario=horario;
           this.categoria=categoria;
           
       }
       
       @Override
       public void handle(ActionEvent event){
           try{
           DirectoryChooser dChooser= new DirectoryChooser();
            dChooser.setTitle("Seleccionar ruta");
            File directorioChooser= dChooser.showDialog(primaryStage);
            if(directorioChooser==null ){
                
                System.out.println("No se selecciono ningun directorio");
            }else{
                String rutaDirectorio=directorioChooser.getAbsolutePath().replaceAll("\\\\","/");
                Dato.ExportarCSV(horario, rutaDirectorio, categoria);
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.INFORMATION);
                mensajeExp.setHeaderText("Datos");
                mensajeExp.setContentText("Se ha exportado correctamente");
                mensajeExp.showAndWait();
            }
           }catch(IOException e){
               Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
               mensajeExp.setHeaderText("Exportar Datos");
               mensajeExp.setContentText(e.getClass().getName());
               mensajeExp.showAndWait();
           }
           
       }
       
   }
   
   public class manejadorCargarItem implements EventHandler<ActionEvent>{
       
       public String ruta;
       
       public manejadorCargarItem(String ruta){
           System.out.println("ruta seleccionada");
           this.ruta=ruta;
       }
       
       @Override
       public void handle(ActionEvent event){
           try{
               estudiante=new Estudiante();
               estudiante=Dato.cargarEstudiante(ruta);
               //System.out.println("horario clases antes vacio: "+estudiante.getHorarioClases().isEmpty());
               estudiante.getHorarioClases().actualizarHorario();
               //System.out.println("horario clases despues vacio: "+estudiante.getHorarioClases().isEmpty());
               estudiante.actualizarHorariosExamenes();
               crearCentro();
               crearIzquierda();
               Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.INFORMATION);
                mensajeExp.setHeaderText("Datos");
                mensajeExp.setContentText("Se ha cargado correctamente");
                mensajeExp.showAndWait();
           }catch(ClassNotFoundException ex){
               //anadir una pantalla emergente
               Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
               mensajeExp.setHeaderText("Datos");
               mensajeExp.setContentText(ex.getClass().getName());
               mensajeExp.showAndWait();
               System.err.println("No se encuentra la clase");
           }catch(ParseException ex2){
               Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
               mensajeExp.setHeaderText("Datos");
               mensajeExp.setContentText(ex2.getClass().getName());
               mensajeExp.showAndWait();
                System.err.println("Error al hacer parse al cargar datos");
           }catch(ClassCollisionException ex3){
               //esto nunca deberia ocurrir ya que se trabaja con materias que ya se agregaron anteriormente
               //System.err.println("Clases del estudiante cargado Chocan");
            }catch(IOException ex4){
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
               mensajeExp.setHeaderText("Datos");
               mensajeExp.setContentText(ex4.getClass().getName());
               mensajeExp.showAndWait();
            }
           
       }
       
   }
    
   public class manejadorNuevo implements EventHandler<ActionEvent>{
       
       @Override
        public void handle(ActionEvent event) {
            estudiante=new Estudiante();
            crearCentro();
            crearIzquierda();
            crearAbajo();
        }
   }
   
   public class manejadorGuardar implements EventHandler<ActionEvent>{
       
       @Override
        public void handle(ActionEvent event) {
            try{
                Dato.guardarDato(estudiante);
                   Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.INFORMATION);
                   mensajeExp.setHeaderText("Datos");
                   mensajeExp.setContentText("Se ha guardado correctamente");
                   mensajeExp.showAndWait();
            }catch(IOException e){
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
                mensajeExp.setHeaderText("Datos");
                mensajeExp.setContentText("Error al guardar Datos");
                mensajeExp.showAndWait();
            }
            
        }
   }
   
   public class manejadorLimpiar implements EventHandler<ActionEvent>{
       
       @Override
        public void handle(ActionEvent event) {
            
            contenedorPreviews.getChildren().clear();
            abajo.getChildren().clear();
            /*
            CustomProgressBar barrita=new CustomProgressBar();
            Thread lop=new Thread(barrita);
            lop.start();*/
            
            
        }
   }
   
   public class manejadorBorrar implements EventHandler<ActionEvent>{
       
       @Override
        public void handle(ActionEvent event) {
            if(!(contenedorPreviews.getChildren().isEmpty())){
                int indice=contenedorPreviews.getChildren().size()-1;
                System.out.println(indice);
                    contenedorPreviews.getChildren().remove(indice);
            }
        }
   }
   
   public class manejadorLabelBotton implements EventHandler<MouseEvent>{
       
       Label lb;
       public manejadorLabelBotton(Label lb){
           this.lb=lb;
       }
       
       @Override
        public void handle(MouseEvent event) {
            if(!(contenedorPreviews.getChildren().isEmpty())){
                 labelRuta.setText(lb.getText());
                 
            }
        }
   }
   
    public class manejadorAgregarHorario implements EventHandler<ActionEvent> {
        
        ScrapFile value;
        Button b1;
        Button b2;
        
        public manejadorAgregarHorario(ScrapFile value,Button b1,Button b2) {
            this.value=value;
            this.b1=b1;
            this.b2=b2;
        }
        
        @Override
        public void handle(ActionEvent event)  {
            ArrayList<Materia> materiasViejas=estudiante.getMaterias();
            ArrayList<Materia> materiasNuevas= value.getMateriasColectadas();
            ArrayList<Materia> materiasFull=new ArrayList<Materia>();
            materiasFull.addAll(materiasViejas);
            materiasFull.addAll(materiasNuevas);
            try{
                
                estudiante.hayEspacioMateriasExamenes(materiasFull);
                estudiante.getHorarioClases().hayEspacioMateriasClases(materiasNuevas);
                
                estudiante.getMaterias().addAll(materiasNuevas);
                //estudiante.getHorarioClases().getMaterias().addAll(materiasNuevas);
                estudiante.actualizarTodo();
                crearCentro();
                
                if (b1.isDisable()) {
                    b1.setDisable(false);
                } else {
                    b1.setDisable(true);
                }
                if (b2.isDisable()) {
                    b2.setDisable(false);
                } else {
                    b2.setDisable(true);
                }
                
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.INFORMATION);
                mensajeExp.setHeaderText("Horario");
                mensajeExp.setContentText("Se ingresó correctamente el paralelo");
                mensajeExp.showAndWait();
                //Solo comprueba por consola lo que se esta agregando
                
                
                
            }catch(ClassCollisionException e){
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.WARNING);
                mensajeExp.setHeaderText("Horario");
                mensajeExp.setContentText("El paralelo ingresado tiene clases o examenes que chocan con el horario actual");
                mensajeExp.showAndWait();
                //pantallaEmergenteChoques();
            }catch(ParseException e2){
                System.err.println("Error al hacer el parse dentro del manejador AgregarHorario");
            }
            
            
        }
        
    } 
    
    public class manejadorQuitarHorario implements EventHandler<ActionEvent>  {
        
        ScrapFile value;
        Button b1;
        Button b2;
        
        public manejadorQuitarHorario(ScrapFile value,Button b1,Button b2){
            this.value=value;
            this.b1=b1;
            this.b2=b2;
        }
        
        @Override
        public void handle(ActionEvent event) {
            ArrayList<Materia> materias= value.getMateriasColectadas();
            try{
                estudiante.elminarMaterias(materias);
                estudiante.getHorarioClases().actualizarHorario();
                estudiante.actualizarHorariosExamenes();
                
                System.out.println("Materias quitadas: ");
                for(Materia m:materias){
                    System.out.println(m);
                }
                System.out.println("\n");
                crearCentro();
                
                if (b1.isDisable()) {
                    b1.setDisable(false);
                } else {
                    b1.setDisable(true);
                }
                if (b2.isDisable()) {
                    b2.setDisable(false);
                } else {
                    b2.setDisable(true);
                }
                
            }catch(ClassCollisionException e){
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
                mensajeExp.setHeaderText("Horario");
                mensajeExp.setContentText("Error: "+e.getClass().getName());
                mensajeExp.showAndWait();
                System.err.println("Este error no es posible pues se esta borrando un paralelo");
            }catch(ParseException e2){
                Alert mensajeExp=ventanaEmergente(primaryStage,Alert.AlertType.ERROR);
                mensajeExp.setHeaderText("Horario");
                mensajeExp.setContentText("Error: "+e2.getClass().getName());
                mensajeExp.showAndWait();
                System.err.println("Error al hacer el parse dentro del manejadorQuitarrHorario");
            }
            
            
            
            
            
        }
        
    } 
   
}
