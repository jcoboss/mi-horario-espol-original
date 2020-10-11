/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.Serializable;

/**
 *
 * @author Josue
 */
public class Profesor implements Serializable{
    
    private String nombre;
    
    public Profesor(){
        
        
    }
    public Profesor(String nombre){
        this.nombre=nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return  nombre ;
    }
    
    @Override
    public boolean equals(Object obj){
        
        if(obj!=null){
            Profesor p=(Profesor)obj;
            if(this.nombre.equals(p.nombre)){
                return true;
            }
        }
        
        return false;
    }
    
    
    
            
}
