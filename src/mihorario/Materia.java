/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Josue
 */
public class Materia implements Serializable{
    
    private String nombre;
    private int Paralelo;
    private Profesor profesor;
    private ArrayList<Examen> examenes=new ArrayList<>();
    private ArrayList<Clase> clases=new ArrayList<>();
    
    

    public Materia() {
    }

    public Materia(String nombre, int Paralelo, Profesor profesor) {
        this.nombre = nombre;
        this.Paralelo = Paralelo;
        this.profesor = profesor;
    }

    public Materia(String nombre, int Paralelo, Profesor profesor, ArrayList<Examen> examenes, ArrayList<Clase> clases) {
        this.nombre = nombre;
        this.Paralelo = Paralelo;
        this.profesor = profesor;
        this.examenes = examenes;
        this.clases = clases;
    }
    @Override
    public boolean equals(Object obj){
        if (obj!=null ){
            Materia m=(Materia)obj;
            if(this.Paralelo==m.getParalelo()&& this.nombre.equals(m.getNombre())
                    ){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + this.Paralelo;
        return hash;
    }
  
    public void anadirClase(Clase clase){
        clases.add(clase);
        
    }
    public void anadirExamen(Examen examen){
        examenes.add(examen);
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getParalelo() {
        return Paralelo;
    }

    public void setParalelo(int Paralelo) {
        this.Paralelo = Paralelo;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public ArrayList<Examen> getExamenes() {
        return examenes;
    }

    public void setExamenes(ArrayList<Examen> examenes) {
        this.examenes = examenes;
    }

    public ArrayList<Clase> getClases() {
        return clases;
    }

    public void setClases(ArrayList<Clase> clases) {
        this.clases = clases;
    }

    @Override
    public String toString() {
        return  nombre + "-" + Paralelo + "-" + profesor ;
    }

    
    
    public String mostrarClases(){
        String cadena="Clases:\n";
        if(!(clases.isEmpty())){
            for(Clase c:clases){
                cadena+=c.getInfo()+"\n";
            }
            
        }
        return cadena;
    }
    
    public String mostrarExamenes(){
        String cadena="Examenes:\n";
        if(!(examenes.isEmpty())){
            for(Examen e:examenes){
                cadena+=e.getInfo()+"\n";
            }
            
        }
        return cadena;
    }
    
    public String getParaleloString(){
        return String.valueOf(Paralelo);
        
    }
    
}
