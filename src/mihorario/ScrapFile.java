/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Josue
 */
public class ScrapFile implements Serializable{
   public final static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
   public final static DateFormat dateFormatClases = new SimpleDateFormat("HH:mm");
   private String nombreMateria="";
   private Document doc;
   private ArrayList<Materia> materiasColectadas=new ArrayList<>();
   private ArrayList<Materia> materiasPracticas=new ArrayList<>();
   

   public ScrapFile(String ruta)throws IOException,ParseException{
       //"src/archivo/fisica 2.htm"
       File input = new File(ruta);
       doc = Jsoup.parse(input, "UTF-8");
       materiasColectadas.add(getMateriaTeorica());
       materiasPracticas=getMateriasPracticas();
       materiasColectadas.addAll(materiasPracticas);
       /*if(doc.getElementsByClass("display").size()>2){
           materiasColectadas.add(getMateriaPractico());
       }*/
       
   }
   
   //Permite elegir una materia practica concreta, necesario para usar el combobox
   public Materia seleccionarMateriaPractica(int paralelo)throws ParseException{
       Materia m=null;
       for(Materia mat:getMateriasPracticas()){
           if(mat.getParalelo()==paralelo){
               m=mat;
           }
           
       }
       return m;
   }
   
   public ArrayList<Materia> getMateriasPracticas()throws ParseException{
       ArrayList<Materia> materiasPractico=new ArrayList<>();
       for(Element e:doc.getElementsByClass(" display tabla_horario")){
           
           Element tabla3=e.getElementsByClass(" display tabla_horario").get(0);
           Elements elementostPractico=tabla3.select("td:nth-of-type(2)");
           
           Profesor profesorPractico=new Profesor(elementostPractico.get(0).text());
           int paraleloPractico=Integer.parseInt(elementostPractico.get(1).text());
           //queda comentado ya que no todas las materias tienen dicho dato
            //String capacidadMaxima=elementostPractico.get(2).text();
           Materia materiaPractico=new Materia
        (nombreMateria,paraleloPractico,profesorPractico);//materia
           
           
            Element tabla4=e.getElementsByClass("display").get(1);
            Elements clasesPractico=tabla4.select("tr");
            ArrayList<Clase> clasePractico=new ArrayList<>();//arreglo de clases
            for(Element ee:clasesPractico.subList(1, clasesPractico.size())){
        //System.out.println(e);
                Elements campos=ee.select("td");
                Dias dia=Dias.valueOf(campos.get(0).text().toUpperCase());
                Date horai=dateFormatClases.parse(campos.get(1).text());
                Date horaf=dateFormatClases.parse(campos.get(2).text());
                String aula=campos.get(3).text();
                String aulaDetalle=campos.get(4).text();
                Clase clase=new Clase(dia, horai, horaf,aula,aulaDetalle);
                //System.out.println(clase);
                clasePractico.add(clase);
            }
            materiaPractico.setClases(clasePractico);
            materiasPractico.add(materiaPractico);
           }
           
       return materiasPractico;
   }
   
   public Materia getMateriaTeorica() throws IOException,ParseException{
       
       //Se extraen los datos principales de la materia parte teorica
    Elements tabla0=doc.getElementsByClass("hdrtop");
    Elements tablaDatosMateria0=tabla0.select("tr");
    Elements tablaDatosMateria1=tablaDatosMateria0.select("td");
    
    if(tablaDatosMateria1.get(0).text().split(":").length==2){
        nombreMateria=
             tablaDatosMateria1.get(0).text().split(":")[1].trim();
    }else if(tablaDatosMateria1.get(0).text().split(":").length==3){
        nombreMateria=
             tablaDatosMateria1.get(0).text().split(":")[2].trim();
    }
     
    int paraleloMateriaTeorico=Integer.parseInt(tablaDatosMateria1.get(1).text().split(":")[1].trim());
    Profesor profesorTeorico=new Profesor
        (tablaDatosMateria0.get(1).text().split(":")[1].trim());
    Materia materiaTeorico=new Materia(nombreMateria, paraleloMateriaTeorico, profesorTeorico);
    
    
    //En el documento existe mas de un elemento con el nombre de clase display
    //Por lo que se buscan todas con getElementsByClass
    //La primera hace referencia a toda la tabla con la informacion de los examenes
    Element tabla1=doc.getElementsByClass("display").get(0);
    Elements tablaDatosExamenes=tabla1.select("td:nth-of-type(2)");
    
    //String del cupo maximo de la materia
    String cupoMaximo=tablaDatosExamenes.get(0).text();
    //String del cupo disponible
    Elements tablaDatosCupoD=tabla1.select("td:nth-of-type(4)");
    String cupoDisponible=tablaDatosCupoD.get(0).text();
    
    
    //Se crea el arreglo de examenes
    ArrayList<Examen> examenes=new ArrayList<>();
    int indicadorCategoria=0;
    for (Element e:tablaDatosExamenes.subList(1, 4)){
        ExamenCategoria cat=ExamenCategoria.values()[indicadorCategoria];
        indicadorCategoria++;
        String[] detallesEx1=e.text().split("-");
        
        String fechaS=detallesEx1[0].trim();
    
        String horaIS=detallesEx1[1].split("a")[0].trim();
    
        String horaFS=detallesEx1[1].split("a")[1].trim();
        
        Date fecha=dateFormat.parse(fechaS+"-"+horaIS);
        Date horaI=dateFormatClases.parse(horaIS);
        Date horaF=dateFormatClases.parse(horaFS);
        //System.out.println(dateFormat.format(fecha));
        //System.out.println(dateFormat.format(horaF));
        
        Examen ex=new Examen(fecha,horaI,horaF,cat);
        examenes.add(ex);
        //System.out.println(ex);
    }
    
    
    //Se extrae la 2da tabla que tiene la informacion de las clases
    //Crea un arreglo con las clases de la tabla 2
    ArrayList<Clase> clases=new ArrayList<>();
    Element tabla2=doc.getElementsByClass("display").get(1);
    Elements fila=tabla2.select("tr");
    for(Element elemento:fila.subList(1, fila.size())){
        Elements subFila=elemento.select("td");
        
        String dia=subFila.get(0).text();
        String horaI=subFila.get(1).text();
        String horaF=subFila.get(2).text();
        String aula=subFila.get(3).text();
        String aulaDetalle=subFila.get(4).text();
        Date horaIn=dateFormatClases.parse(horaI);
        Date horaFi=dateFormatClases.parse(horaF);
        Dias diaS=Dias.valueOf(dia.toUpperCase());
        //System.out.println(dateFormatClases.format(horaFi));
        Clase clase=new Clase(diaS,horaIn,horaFi,aula,aulaDetalle);
        //System.out.println(clase);
        clases.add(clase);
    }
    materiaTeorico.setExamenes(examenes);
    materiaTeorico.setClases(clases);
       
       return materiaTeorico;
   }
   
   public  Materia getMateriaPractico()throws ParseException{
       
       //Se selecciona la 3era tabla(indice 2) de todos los elementos display
    //Se crea la material del practico
    Element tabla3=doc.getElementsByClass("display").get(2);
    Elements elementostPractico=tabla3.select("td:nth-of-type(2)");
    
    Profesor profesorPractico=new Profesor(elementostPractico.get(0).text());
    int paraleloPractico=Integer.parseInt(elementostPractico.get(1).text());
    String capacidadMaxima=elementostPractico.get(2).text();
    Materia materiaPractico=new Materia
        (nombreMateria,paraleloPractico,profesorPractico);//materia
    
    
    //Selecciona a la 4ta tabla con la informacion de las clases
    //del paralelo practico, se crea y se llena un arreglo con las clases
    Element tabla4=doc.getElementsByClass("display").get(3);
    Elements clasesPractico=tabla4.select("tr");
    ArrayList<Clase> clasePractico=new ArrayList<>();//arreglo de clases
    for(Element e:clasesPractico.subList(1, clasesPractico.size())){
        //System.out.println(e);
        Elements campos=e.select("td");
        Dias dia=Dias.valueOf(campos.get(0).text().toUpperCase());
        Date horai=dateFormatClases.parse(campos.get(1).text());
        Date horaf=dateFormatClases.parse(campos.get(2).text());
        String aula=campos.get(3).text();
        String aulaDetalle=campos.get(4).text();
        Clase clase=new Clase(dia, horai, horaf,aula,aulaDetalle);
        //System.out.println(clase);
        clasePractico.add(clase);
    }
    
    //seteo el arreglo de clases del practico en la materiaPractico
    materiaPractico.setClases(clasePractico);
       return materiaPractico;
   }
   
    public String getNombreMateria() {
        return nombreMateria;
    }

    public Document getDoc() {
        return doc;
    }

    public ArrayList<Materia> getMateriasColectadas() {
        return materiasColectadas;
    }

    public void setMateriasColectadas(ArrayList<Materia> materiasColectadas) {
        this.materiasColectadas = materiasColectadas;
    }
   
   
}