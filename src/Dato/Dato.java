/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dato;


import com.sun.javaws.Launcher;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import mihorario.Estudiante;
import mihorario.ExamenCategoria;
import mihorario.Horario;
import mihorario.Materia;

/**
 *
 * @author Josue
 */
public class Dato {
    
    public static void guardarDato(Estudiante e)throws IOException {
        
        DateFormat formato=new SimpleDateFormat("dd-MM-yyyy@HH-mm-ss");
        String rutaUsuario=System.getProperty("user.home").replaceAll("\\\\", "/");  //C:\Users\Josue
        //Crea el directorio y retorna true si se crea y false caso contrario
        new File(rutaUsuario+"/DatosHorariosAp/guardados").mkdirs();
        //codigo para guardar usando un IDE
        
        ObjectOutputStream objOutputStream=
            new ObjectOutputStream(new FileOutputStream(
                    new File(rutaUsuario+"/DatosHorariosAp/guardados").getAbsolutePath().replaceAll("\\\\", "/")+
                "/"+formato.format(Date.from(Instant.now()))+".dat"));
        objOutputStream.writeObject(e);
        objOutputStream.close();
        
        
        System.out.println("Guardado Exitoso");
        
    }
    
    public static ArrayList<String> cargarDato(){
        ArrayList<String> eEncontrados=new ArrayList<>();
        String rutaUsuario=System.getProperty("user.home").replaceAll("\\\\", "/");
        
        File directorio=new File(rutaUsuario+"/DatosHorariosAp/guardados");
        directorio.mkdirs();
        System.out.println("Es directorio");
            
        for (File f:directorio.listFiles()){
               
            eEncontrados.add(f.getPath().replaceAll("\\\\", "/"));
            
            System.out.println(f.getPath());//este es
        }
        if(directorio.listFiles().length==0){
            System.out.println("No hay archivos guardados en el directorio");
        }
        return eEncontrados;
    }
    
    public static Estudiante cargarEstudiante(String ruta)throws IOException,ClassNotFoundException{
        Estudiante estudiante=null;
        
        ObjectInputStream objInputStream=
                new ObjectInputStream(new FileInputStream(ruta));
        //new ObjectInputStream(Dato.class.getResourceAsStream(ruta));
        estudiante=(Estudiante)objInputStream.readObject();
        objInputStream.close();
        System.out.println("Carga Exitosa");
        return estudiante;
    }
    
    public static void saveAsPng(Node NODE,String directorio,String cat)throws IOException {
        DateFormat formatoNombre=new SimpleDateFormat("HH-mm-ss");
    String categoria;
        if(cat==null){
            categoria="Horario";
        } else {
            categoria="Horario-"+cat;
        } 
    final WritableImage SNAPSHOT = NODE.snapshot(new SnapshotParameters(), null);
    final String        NAME     = directorio.replace("\\.[a-zA-Z]{3,4}", "");
    final File          FILE     = new File(NAME +"/"+categoria+"@"+formatoNombre.format(Date.from(Instant.now()))+".png");
    
    
        ImageIO.write(SwingFXUtils.fromFXImage(SNAPSHOT, null), "png", FILE);
   
        System.out.println("Se guardo con exito");
}
    
    public static void ExportarCSV(Horario h,String directorio,String cat)throws IOException{
        String separador=",";
        DateFormat formatoNombre=new SimpleDateFormat("HH-mm-ss");
        DateFormat formatoHoras=new SimpleDateFormat("HH:mm");
        
        String categoria;
        if(cat==null){
            categoria="Horario";
        } else {
            categoria="Horario-Ex"+cat;
        }
        String nombreArchivo=directorio+"/"+categoria+"@"+formatoNombre.format(Date.from(Instant.now()))+".csv";
        int indice=0;
        Date[][] arregloHoras=h.getHoras();
        String cabecera="Horas,Lunes,Martes,Miércoles,Jueves,Viernes\n";
        
        for(Materia[] filas: h.getHorario()){
            String hora0=formatoHoras.format(arregloHoras[indice][0]);
            String hora1=formatoHoras.format(arregloHoras[indice][1]);
            String cadena=hora0+"-"+hora1;
            String linea=cadena;
            indice++;
            for(Materia mat:filas){
                if(mat!=null){
                 
                linea=linea+separador+mat.toString();   
                }else{
                    linea=linea+separador+" ";
                }
            }
            linea+="\n";
            cabecera+=linea;
            
        }
        //new FileWriter(nombreArchivo,true))
        
        BufferedWriter escritor=new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(nombreArchivo)),Charset.forName("UNICODE")));
        
         escritor.write(cabecera);
         escritor.close();
        
        System.out.println("Se exporto con exito");
    }
    
    
    
}
