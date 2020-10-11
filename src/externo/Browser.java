package externo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.math.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Josue
 */
public class Browser {
    //final String linkInicial="https://cenacad.espol.edu.ec/index.php?module=Report&action=Profesores&l=0&q=marcos+mendoza&Submit=Enviar";
    //private Document doc=null;
    private ProfesorCenacad profe=null;
    private String linkMaterias="";
    private ArrayList<MateriaCenacad> materiasEncontradas=new ArrayList<>();
    private ArrayList<ProfesorCenacad> profesorEncontrados=new ArrayList<>();
    
    public Browser(String nombreCompleto) throws IOException{
        buscarProfesores(nombreCompleto);
        profe=obtenerProfesor(nombreCompleto,profesorEncontrados);
        linkMaterias=profe.getLink();
    }
    
    public static Document obtenerDocumento(String url) throws IOException{
        Document documento=null;
        if (url!=null){
            documento=Jsoup.connect(url).get();
        }else{
            System.out.println("Estas en la ultima Pagina");
        }
        return documento; 
    }
    
    public static ArrayList<ProfesorCenacad> obtenerResultados(Document doc){
        ArrayList<ProfesorCenacad> resultados=new ArrayList<>();
        
        Elements tablaContenedora=doc.getElementsByClass("tbl");
        if(!tablaContenedora.isEmpty()){
             int indice=0;
        for(Element e2:tablaContenedora.select("tr")){
            if(indice>0){//con esto me salto al primero
                String apellidos=e2.select("td.izquierda:nth-of-type(2)").text();
                String prefijo="https://cenacad.espol.edu.ec";
                String nombres=e2.select("td.izquierda:nth-of-type(3)").text();
                String link=prefijo+e2.select("td:nth-of-type(4)").select("a").attr("href");
                ProfesorCenacad pro=new ProfesorCenacad(apellidos,nombres,link);
                resultados.add(pro);
                }
            indice++;
            }
        System.out.println("\n");
        }
        return resultados;
    }
    
    public static ProfesorCenacad obtenerProfesor(String apellidosNombres,ArrayList<ProfesorCenacad> resultados){
        ProfesorCenacad profesor=new ProfesorCenacad();
        double percentAnterior=0.0f;
        for(ProfesorCenacad pro:resultados){
            double percent=Diccionario.coincidencias(pro.toString(),apellidosNombres);
                if(percentAnterior<percent){
                    profesor=pro;
                    percentAnterior=percent;
                }
                
        }
        return profesor;
    }
    
    public static String obtenerLink(String nombre){
        //String nombre="Mendoza Lombana Sonya patricia";
        //string de ejemplo el de arriba
        String nombrePlus="";
        for(String s:nombre.split(" ")){
            nombrePlus+=(s+"+");
        }
        nombrePlus=nombrePlus.substring(0, nombrePlus.length()-1);
        
        String url="https://cenacad.espol.edu.ec/index.php?module=Report&action=Profesores&l=0&q=marcos+mendoza&Submit=Enviar";
        String []separados=url.split("=");
        separados[4]=nombrePlus+"&Submit";
        String nuevoLink="";
        for(String d:separados){
            nuevoLink+=(d+"=");
        }
        nuevoLink=nuevoLink.substring(0, nuevoLink.length()-1);
        return (nuevoLink);
    }
    
    public static String obtenerSiguienteLink(Document doc){
        String linkSiguiente=null;
        Elements barritaPaginas=doc.select("table.centrado");
        for(Element e:barritaPaginas.select("a")){
            if(e.text().equals("Siguiente")){
                linkSiguiente=e.absUrl("href");
            }
        }
        return linkSiguiente;
    }
    /*Este metodo es interno, no se debe de llamar fuera de esta clase*/
    public static ArrayList<MateriaCenacad> obtenerMaterias(Document doc){
        ArrayList<MateriaCenacad> materias=new ArrayList<>();
        Elements tablaContenedora=doc.getElementsByClass("tbl");
        int indice=0;
        for(Element e2:tablaContenedora.select("tr")){
            if(indice>0){//con esto me salto al primero
                short año=Short.parseShort(e2.select("td.centrado:nth-of-type(2)").text());
                String nombre=e2.select("td:nth-of-type(5)").text();
                double promedio=Double.parseDouble(e2.select("td.centrado:nth-of-type(7)").text());
                
                MateriaCenacad mat=new MateriaCenacad(nombre,promedio,año);
                materias.add(mat);
                System.out.println(mat);
            }
            indice++;
            }
        //System.out.println("\n");
        return materias;
    }
    
    public void buscarProfesores(String nombreCompleto) throws IOException{
        ArrayList<ProfesorCenacad> profesoresColectados=new ArrayList<>();
        String linkInicial=obtenerLink(nombreCompleto);
        
        boolean flag=true;
        int k=1;
        do{
            if(k<=3){
                
                Document documento=obtenerDocumento(linkInicial);
                profesoresColectados.addAll(obtenerResultados(documento));
                String siguiente=obtenerSiguienteLink(documento);
                if(siguiente==null ){
                    flag=false;
                }else{
                    linkInicial=siguiente;
                }
            }else{
                flag=false;
            }
            k++;
        }while(flag);
        this.profesorEncontrados=profesoresColectados;
    }
    
    public void buscarMaterias() throws IOException{
        ArrayList<MateriaCenacad> materiasColectadas=new ArrayList<>();
        String linkInicial=this.linkMaterias;
        
        boolean flag=true;
        int k=1;
        do{
            if(k<=3){
                
                Document documento=obtenerDocumento(linkInicial);
                materiasColectadas.addAll(obtenerMaterias(documento));
            
                String siguiente=obtenerSiguienteLink(documento);
                if(siguiente==null ){
                    flag=false;
                }else{
                    linkInicial=siguiente;
                }
            }else{
                flag=false;
            }
            k++;
        }while(flag);
        this.materiasEncontradas=materiasColectadas;
    }
    
    public MateriaCenacad buscarMateria(String nombre){
        MateriaCenacad resultado=null;
        double percentPre=0.0f;
        for(MateriaCenacad m:this.materiasEncontradas){
            
            double percent=Diccionario.coincidencias(nombre,m.getNombre());
            //System.out.println(m+"  "+percent);
            if(percent>percentPre){
                resultado=m;
                percentPre=percent;
            }
            
        }
        return resultado;
    }
    
    public ProfesorCenacad getProfe() {
        return profe;
    }

    public ArrayList<MateriaCenacad> getMateriasEncontradas() {
        return materiasEncontradas;
    }
    
    
}
