/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Josue
 */
public class Estudiante implements Serializable{
    
    
    private ArrayList<Materia> materias=new ArrayList<>();
    
    private HashMap<LlaveSemana,Horario> examenesParciales=new HashMap<>();
    
    private HashMap<LlaveSemana,Horario> examenesFinales=new HashMap<>();
    
    private HashMap<LlaveSemana,Horario> examenesRecuperacion=new HashMap<>();
    
    private Horario horarioClases=new Horario(this.materias);

    public Estudiante() {
       
    }
    
    public Estudiante(ArrayList<Materia> materias) {
        this.materias = materias;
        
    }
    
    public HashMap<LlaveSemana,Horario>cargarExamenesTipo(ExamenCategoria categoria)throws ParseException
    ,ClassCollisionException{
        HashMap<LlaveSemana,Horario> examenesMap=new HashMap<>();
        System.out.println("Materias con los examenes en horario"+categoria.toString()+ ":");
        for(Materia m:this.materias){
            if(!(m.getExamenes().isEmpty())){
            for(Examen e:m.getExamenes()){
                if(e.cat==(categoria)){
                    LlaveSemana llave=Horario.IFSemana(e.getFecha());
                    Horario h;
                    //(examenesMap.keySet().isEmpty() || !(examenesMap.containsKey(llave)
                    if(examenesMap.containsKey(llave)){
                        h=examenesMap.get(llave);
                    }else {
                        h=new Horario();
                        examenesMap.put(llave, h);
                    }
                    System.out.println(m.toString());
                    //h.hayEspacioExamen(m, e);
                    h.agregarExamenHorario(m, e);
                    
                }
            }
            }
        }
        return examenesMap;
        
    }

    public void actualizarHorariosExamenes()throws ParseException,ClassCollisionException {
        this.examenesParciales=cargarExamenesTipo(ExamenCategoria.PARCIAL);
        this.examenesFinales=cargarExamenesTipo(ExamenCategoria.FINAL);
        this.examenesRecuperacion=cargarExamenesTipo(ExamenCategoria.MEJORAMIENTO);
        
        
    }
    
    public void hayEspacioExamenesTipo(ArrayList<Materia>materias,ExamenCategoria categoria)throws ParseException
    ,ClassCollisionException{
        HashMap<LlaveSemana,Horario> examenesMap=new HashMap<>();
        for(Materia m:materias){
            if(!(m.getExamenes().isEmpty())){
            for(Examen e:m.getExamenes()){
                if(e.cat==(categoria)){
                    LlaveSemana llave=Horario.IFSemana(e.getFecha());
                    Horario h;
                    //(examenesMap.keySet().isEmpty() || !(examenesMap.containsKey(llave)
                    if(examenesMap.containsKey(llave)){
                        h=examenesMap.get(llave);
                    }else {
                        h=new Horario();
                        examenesMap.put(llave, h);
                    }
                    //System.out.println(llave.toString());
                    h.hayEspacioExamen(m, e);
                    h.agregarExamenHorario(m, e);
                }
            }
            }
        }
        //return examenesMap;
        
    }
    public void hayEspacioMateriasExamenes(ArrayList<Materia>materias)throws ParseException,ClassCollisionException {
        hayEspacioExamenesTipo(materias,ExamenCategoria.PARCIAL);
        hayEspacioExamenesTipo(materias,ExamenCategoria.FINAL);
        hayEspacioExamenesTipo(materias,ExamenCategoria.MEJORAMIENTO);
        
        
    }
    /*
    public void verificaEspacioMateria(ArrayList<Materia>materias) throws ParseException,ClassCollisionException{
        hayEspacioMateriasExamenes(materias);
        this.horarioClases.hayEspacioMateriasClases(materias);
    }*/
    
    public void actualizarTodo()throws ParseException,ClassCollisionException{
        this.horarioClases.actualizarHorario();
        actualizarHorariosExamenes();
    }
        
    
    public void setMaterias(ArrayList<Materia> materias) {
        this.materias = materias;
    }

    public void setHorarioClases(Horario horarioClases) {
        this.horarioClases = horarioClases;
    }
    
    
    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public HashMap<LlaveSemana, Horario> getExamenesParciales() {
        return examenesParciales;
    }

    public HashMap<LlaveSemana, Horario> getExamenesFinales() {
        return examenesFinales;
    }

    public HashMap<LlaveSemana, Horario> getExamenesRecuperacion() {
        return examenesRecuperacion;
    }

    public Horario getHorarioClases() {
        return horarioClases;
    }
    
    public void eliminarMateria(Materia materia){
       
        this.materias.remove(materia);
        
    }
    
    public void elminarMaterias(ArrayList<Materia> materias){
        for(Materia m:materias){
            eliminarMateria(m);
        }
        
    }

    
}
