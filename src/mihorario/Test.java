/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import Dato.Dato;
import java.io.File;
/**
 *
 * @author Josue
 */
public class Test {
   
    public final static long unDia=86400000;
    
    public static final DateFormat formato=new SimpleDateFormat("dd/MM/yyyy");
    
    
    
    
   /*public static void main(String []args)throws IOException,ParseException,ClassCollisionException{
        
        ScrapFile sc=new ScrapFile("src/archivo/Computacion.htm");
        //ScrapFile sc2=new ScrapFile("src/archivo/Estadistica.htm");
        //ScrapFile sc3=new ScrapFile("src/archivo/Poo.htm");
        //ScrapFile sc4=new ScrapFile("src/archivo/ecuaciones.htm");
        //ScrapFile sc5=new ScrapFile("src/archivo/EntrenamientodeFuerza.htm");
        Estudiante e1=new Estudiante();
        e1.getHorarioClases().anadirMaterias(sc.getMateriasColectadas());
        //e1.getHorarioClases().anadirMaterias(sc2.getMateriasColectadas());
        //e1.getHorarioClases().anadirMaterias(sc3.getMateriasColectadas());
        //e1.getHorarioClases().anadirMaterias(sc4.getMateriasColectadas());
        //e1.getHorarioClases().anadirMaterias(sc5.getMateriasColectadas());
        
        
        e1.getHorarioClases().actualizarHorario();
        e1.actualizarHorariosExamenes();
        Dato.ExportarCSV(e1.getHorarioClases(), "src/Guardados", ExamenCategoria.MEJORAMIENTO.toString());
        
        
    }*/
    public static void main(String []args)throws IOException,ParseException,ClassCollisionException{
        
        String rutaUsuario=System.getProperty("user.home").replaceAll("\\\\", "/");  //C:\Users\Josue
        System.out.println(new File(rutaUsuario+"/DatosHorariosAp").mkdirs());
               System.out.println(new File(rutaUsuario+"/DatosHorariosAp").getAbsolutePath());
                System.out.println();
        
    }
  
    
    
    
}
